package com.gt.mall.service.web.seller.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.seller.MallSellerDAO;
import com.gt.mall.dao.seller.MallSellerIncomeDAO;
import com.gt.mall.dao.seller.MallSellerSetDAO;
import com.gt.mall.dao.seller.MallSellerWithdrawDAO;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerIncome;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.entity.seller.MallSellerWithdraw;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.seller.MallSellerWithdrawService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.util.entity.param.pay.ApiEnterprisePayment;
import com.gt.util.entity.result.pay.EnterprisePaymentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 超级销售员提现记录表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSellerWithdrawServiceImpl extends BaseServiceImpl< MallSellerWithdrawDAO,MallSellerWithdraw > implements MallSellerWithdrawService {

    @Autowired
    private MallSellerWithdrawDAO mallSellerWithdrawDAO;
    @Autowired
    private MallSellerDAO         mallSellerDAO;
    @Autowired
    private MallSellerSetDAO      mallSellerSetDAO;
    @Autowired
    private MallSellerIncomeDAO   mallSellerIncomeDAO;
    @Autowired
    private MemberService         memberService;
    @Autowired
    private WxPublicUserService   wxPublicUserService;
    @Autowired
    private PayService            payService;

    /**
     * 查询销售员的提现记录
     */
    @Override
    public List< MallSellerWithdraw > selectBySaleMemberId( Member member ) {
	Map< String,Object > params = new HashMap< String,Object >();
	if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
	    List< String > lists = new ArrayList< String >();
	    for ( String oldMemberId : member.getOldid().split( "," ) ) {
		if ( CommonUtil.isNotEmpty( oldMemberId ) ) {
		    lists.add( oldMemberId );
		}
	    }
	    params.put( "oldMemberIds", lists );
	} else {
	    params.put( "memberId", member.getId() );
	}
	return mallSellerWithdrawDAO.selectBySaleMemberId( params );
    }

    /**
     * 保存提现信息
     */
    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > saveWithdraw( int saleMemberId, Map< String,Object > params, int paySource ) throws Exception {
	Map< String,Object > resultMap = new HashMap< String,Object >();

	MallSellerWithdraw withdraw = JSONObject.parseObject( params.get( "withdraw" ).toString(), MallSellerWithdraw.class );
	double withdrawMoney = CommonUtil.toDouble( withdraw.getWithdrawMoney() );
	if ( withdrawMoney < 1 ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "可提现的金额必须大于等于1" );
	    return resultMap;
	}

	MallSeller seller = mallSellerDAO.selectSellerByMemberId( saleMemberId );
	DecimalFormat df = new DecimalFormat( "######0.00" );
	if ( CommonUtil.isNotEmpty( seller ) ) {
	    double canCommission = 0;
	    if ( CommonUtil.isNotEmpty( seller.getCanPresentedCommission() ) ) {
		if ( CommonUtil.toDouble( seller.getCanPresentedCommission() ) >= 1 ) {
		    canCommission = CommonUtil.toDouble( seller.getCanPresentedCommission() );
					/*withdraw.setWithdrawMoney(seller.getCanPresentedCommission());*/
		} else {
		    resultMap.put( "flag", false );
		    resultMap.put( "msg", "可提现的金额必须大于等于1元" );
		    return resultMap;
		}
	    }
	    MallSellerSet sellerSet = mallSellerSetDAO.selectByBusUserId( seller.getBusUserId() );
	    if ( CommonUtil.isNotEmpty( sellerSet ) && canCommission > 0 ) {
		double multipe = CommonUtil.toDouble( sellerSet.getWithdrawalMultiple() );
		if ( sellerSet.getWithdrawalType() == 1 ) {//按照最底金额来提现
		    if ( CommonUtil.isNotEmpty( sellerSet.getWithdrawalLowestMoney() ) ) {
			if ( CommonUtil.toDouble( sellerSet.getWithdrawalLowestMoney() ) > canCommission ) {
			    resultMap.put( "flag", false );
			    resultMap.put( "msg", "您可提现的金额不能小于提现最底金额" );
			    return resultMap;
			}
		    }
		} else if ( sellerSet.getWithdrawalType() == 2 ) {//按照倍数来提现
		    if ( canCommission >= multipe ) {
			double mulite = canCommission - canCommission % multipe;
			mulite = CommonUtil.toDouble( df.format( mulite ) );
			if ( mulite != withdrawMoney ) {
			    resultMap.put( "flag", false );
			    resultMap.put( "msg", "您可提现的金额必须是" + multipe + "的倍数" );
			    return resultMap;
			}
		    } else {
			resultMap.put( "flag", false );
			resultMap.put( "msg", "您可提现的金额必须是" + multipe + "的倍数" );
			return resultMap;
		    }

		}

	    }
	}

	MallSellerIncome in = new MallSellerIncome();
	in.setSaleMemberId( saleMemberId );
	List< MallSellerIncome > incomeList = mallSellerIncomeDAO.selectByCanWithIncome( in );
	double wxCommission = 0;//微信领取佣金
	if ( incomeList != null && incomeList.size() > 0 ) {
	    for ( MallSellerIncome mallSellerIncome : incomeList ) {
		double canCommission = CommonUtil.toDouble( mallSellerIncome.getIncomeCommission() );
		double noCommisssion = ( withdrawMoney - wxCommission );
		MallSellerIncome income = new MallSellerIncome();
		income.setId( mallSellerIncome.getId() );
		if ( wxCommission < withdrawMoney ) {
		    if ( wxCommission + canCommission <= withdrawMoney ) {
			wxCommission += canCommission;
			income.setIsGet( 1 );
			income.setNoReceiveCommission( BigDecimal.valueOf( 0 ) );
		    } else {
			wxCommission += noCommisssion;
			double noReceive = CommonUtil.toDouble( df.format( canCommission - noCommisssion ) );
			income.setNoReceiveCommission( BigDecimal.valueOf( noReceive ) );
		    }
		    mallSellerIncomeDAO.updateById( income );
		} else {
		    break;
		}

	    }
	}
	withdraw.setSaleMemberId( saleMemberId );
	withdraw.setApplayTime( new Date() );
	withdraw.setWithdrawOrderNo( "TX" + System.currentTimeMillis() );
	withdraw.setWithdrawStatus( 2 );
	int count = mallSellerWithdrawDAO.insert( withdraw );
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	    Map< String,Object > sellerMap = new HashMap<>();
	    sellerMap.put( "money", withdraw.getWithdrawMoney() );
	    sellerMap.put( "type", 1 );
	    sellerMap.put( "saleMemberId", saleMemberId );
	    //修改提现金额和冻结金额
	    mallSellerDAO.updateByWithdrawSelective( sellerMap );

	    EnterprisePaymentResult result = wxWithdrawMoney( withdraw, paySource );
	    if ( CommonUtil.isEmpty( result ) ) {
		resultMap.put( "flag", false );
		throw new Exception( "提现失败，请稍后提现" );
	    }
	} else {
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "我要提现失败" );
	}
	return resultMap;
    }

    private EnterprisePaymentResult wxWithdrawMoney( MallSellerWithdraw withdraw, int paySource ) throws Exception {
	MallSeller seller = mallSellerDAO.selectSellerByMemberId( withdraw.getSaleMemberId() );

	WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( seller.getBusUserId() );
	Member member = memberService.findMemberById( seller.getMemberId(), null );//根据粉丝id查询粉丝信息

	ApiEnterprisePayment payment = new ApiEnterprisePayment();
	payment.setAppid( wxPublicUsers.getAppid() );
	payment.setPartner_trade_no( withdraw.getWithdrawOrderNo() );
	payment.setOpenid( member.getOpenid() );
	payment.setDesc( "超级销售员佣金提现" );
	payment.setAmount( CommonUtil.toDouble( withdraw.getWithdrawMoney() ) );
	payment.setBusId( member.getBusid() );
	payment.setModel( CommonUtil.toInteger( Constants.ENTER_PAY_MODEL ) );
	payment.setPaySource( paySource );
	return payService.enterprisePayment( payment );
    }

    @Override
    public PageUtil withdrawPage( int busUserId, Map< String,Object > params ) {
	params.put( "busUserId", busUserId );
	int count = mallSellerWithdrawDAO.selectCountWithdraw( params );

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallSellers/withDrawList.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > withdrawList = mallSellerWithdrawDAO.selectWithdrawList( params );

	page.setSubList( withdrawList );
	return page;
    }

    @Override
    public List< Map< String,Object > > selectWithdrawList( Map< String,Object > params ) {
	return mallSellerWithdrawDAO.selectWithdrawList( params );
    }

}
