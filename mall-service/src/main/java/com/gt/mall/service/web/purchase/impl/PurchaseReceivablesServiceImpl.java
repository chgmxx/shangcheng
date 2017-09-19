package com.gt.mall.service.web.purchase.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.purchase.PurchaseOrderDAO;
import com.gt.mall.dao.purchase.PurchaseReceivablesDAO;
import com.gt.mall.dao.purchase.PurchaseTermDAO;
import com.gt.mall.entity.purchase.PurchaseOrder;
import com.gt.mall.entity.purchase.PurchaseReceivables;
import com.gt.mall.entity.purchase.PurchaseTerm;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.purchase.PurchaseReceivablesService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.result.pay.WxPayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Service
public class PurchaseReceivablesServiceImpl extends BaseServiceImpl< PurchaseReceivablesDAO,PurchaseReceivables > implements PurchaseReceivablesService {

    @Autowired
    private PurchaseReceivablesDAO purchaseReceivablesDAO;
    @Autowired
    private PurchaseOrderDAO       purchaseOrderDAO;
    @Autowired
    private PurchaseTermDAO        purchaseTermDAO;
    @Autowired
    private MemberService          memberService;
    @Autowired
    private WxPublicUserService    wxPublicUserService;
    @Autowired
    private PayOrderService        payOrderService;

    @Override
    public SortedMap< Object,Object > cgPay( String url, Integer memberId, Integer busId, String termId, Double money, Double discountmoney, Double fenbi, Integer jifen,
		    Integer discount, String paymentType, Integer orderId, Integer dvId, String disCountdepict ) throws Exception {
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( money ) ) {
	    throw new Exception();
	}
	Member member = memberService.findMemberById( memberId, null );
	//新增收款记录
	Integer deduction_jifen = 0;
	Double deduction_fenbi = 0.0;
	//TODO 需关连会员支付 memberPayService deductJifen,deductFenbi方法
	//        if (jifen != null && jifen > 0) {
	//            deduction_jifen = memberPayService.deductJifen(Double.parseDouble(jifen.toString()), busId);
	//        }
	//        if (fenbi != null && fenbi > 0) {
	//            deduction_fenbi = memberPayService.deductFenbi(fenbi, busId);
	//        }

	PurchaseReceivables receivables = new PurchaseReceivables();
	receivables.setBusId( busId );
	receivables.setBuyMode( paymentType );
	receivables.setBuyTime( new Date() );
	receivables.setCoupon( dvId != null ? dvId.toString() : null );
	receivables.setDiscount( discount );
	receivables.setFansCorrency( deduction_fenbi );
	receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
	receivables.setIntegral( Double.parseDouble( deduction_jifen.toString() ) );
	receivables.setMemberId( memberId );
	receivables.setMoney( discountmoney );
	receivables.setOrderId( orderId );
	receivables.setBuyStatus( 0 );
	String receivablesNumber = "CG" + System.currentTimeMillis();
	receivables.setReceivablesNumber( receivablesNumber );
	receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
	purchaseReceivablesDAO.insert( receivables );
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectById( member.getPublicId() );
	WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( receivablesNumber );
	if ( wxPayOrder != null ) {
	    receivablesNumber = "CG" + System.currentTimeMillis();
	    receivables.setReceivablesNumber( receivablesNumber );
	    purchaseReceivablesDAO.updateById( receivables );
	}
	// 统一下单调用
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "appid", wxPublicUsers.getAppid() );
	params.put( "mchid", wxPublicUsers.getMchId() );
	params.put( "sysOrderNo", receivablesNumber );
	params.put( "productId", System.currentTimeMillis() );
	params.put( "key", wxPublicUsers.getApiKey() );
	params.put( "desc", "对外报价订单支付" );
	params.put( "totalFee", discountmoney );
	params.put( "ip", "127.0.0.1" );
	params.put( "openid", member.getOpenid() );
	params.put( "url", url );
	params.put( "model", 35 );
	params.put( "authRefreshToken", wxPublicUsers.getAuthRefreshToken() );
	//TODO 需关连 wxPayService 方法
	SortedMap< Object,Object > sortedMap = null;
	//                wxPayService.memberPay(params);
	if ( CommonUtil.isNotEmpty( sortedMap.get( "code" ) ) && sortedMap.get( "code" ).equals( "1" ) ) {
	    sortedMap.put( "public_id", member.getPublicId() );
	}
	//封装收款记录的地址
	sortedMap.put( "red_url", "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId );
	return sortedMap;
    }

    @Override
    public Map< String,Object > aliCgPay( Integer memberId, Integer busId, String termId, Double money, Double discountmoney, Double fenbi, Integer jifen, Integer discount,
		    String paymentType, Integer orderId, Integer dvId, String disCountdepict ) throws Exception {
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( money ) ) {
	    throw new Exception();
	}
	try {
	    //新增收款记录
	    Integer deduction_jifen = 0;
	    Double deduction_fenbi = 0.0;
	    //TODO 需关连会员支付 memberPayService deductJifen,deductFenbi方法
	    //            if (jifen != null && jifen > 0) {
	    //                deduction_jifen = memberPayService.deductJifen(Double.parseDouble(jifen.toString()), busId);
	    //            }
	    //            if (fenbi != null && fenbi > 0) {
	    //                deduction_fenbi = memberPayService.deductFenbi(fenbi, busId);
	    //            }

	    PurchaseReceivables receivables = new PurchaseReceivables();
	    receivables.setBusId( busId );
	    receivables.setBuyMode( paymentType );
	    receivables.setBuyTime( new Date() );
	    receivables.setCoupon( dvId != null ? dvId.toString() : null );
	    receivables.setDiscount( discount );
	    receivables.setFansCorrency( deduction_fenbi );
	    receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
	    receivables.setIntegral( Double.parseDouble( deduction_jifen.toString() ) );
	    receivables.setMemberId( memberId );
	    receivables.setMoney( discountmoney );
	    String receivablesNumber = "CG" + System.currentTimeMillis();
	    receivables.setReceivablesNumber( receivablesNumber );
	    receivables.setOrderId( orderId );
	    receivables.setBuyStatus( 0 );
	    receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
	    purchaseReceivablesDAO.insert( receivables );

	    Member member = memberService.findMemberById( memberId, null );
	    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo(receivablesNumber);
	    if ( wxPayOrder != null ) {
		receivablesNumber = "CG" + System.currentTimeMillis();
		receivables.setReceivablesNumber( receivablesNumber );
		purchaseReceivablesDAO.updateById( receivables );
	    }
	    Map< String,Object > params = new HashMap< String,Object >();
	    String red_url = "/alipay/79B4DE7C/alipayApi.do?out_trade_no=" + receivablesNumber
			    + "&subject=对外报价订单支付&model=35&businessUtilName=alipayNotifyUrlBuinessServicereCgOrderPay&total_fee=" + discountmoney + "&busId=" + member.getBusid()
			    + "&return_url=" + PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId;
	    params.put( "result", true );
	    params.put( "message", "未支付" );
	    params.put( "red_url", red_url );
	    return params;
	} catch ( Exception e ) {
	    throw new Exception();
	}
    }

    @Override
    public void addReceivables( String receivablesNumber ) throws Exception {
	try {
	    //得到收款信息
	    PurchaseReceivables receivable = purchaseReceivablesDAO.selectReceivable( receivablesNumber );
	    //如果收款中包含了积分  扣除用户积分
	    if ( receivable.getIntegral() != null && receivable.getIntegral() > 0 ) {
		//TODO 需关连会员支付 memberPayService.updateMemberIntergral方法
		//                Map<String, Object> map = memberPayService.updateMemberIntergral(null, receivable.getMemberId(), -receivable.getIntegral().intValue());
		//                if (map.get("result") != null && map.get("result").equals("1")) {
		//                    throw new Exception("积分扣除时出现异常");
		//                }
	    }
	    //如果收款中包含了粉币 扣除用户粉币
	    if ( receivable.getFansCorrency() != null && receivable.getFansCorrency() > 0 ) {
		//TODO 需关连会员支付 memberPayService.updateMemberFansCurrency方法
		//                Map<String, Object> map = memberPayService.updateMemberFansCurrency(null, receivable.getMemberId(), receivable.getBusId(), -receivable.getFansCorrency());
		//                if (map.get("result") != null && map.get("result").equals("1")) {
		//                    throw new Exception("粉币扣除时出现异常!");
		//                }
	    }
	    //如果收款的订单涉及到分期支付  修改分期状态
	    if ( receivable.getHaveTerm() != null && receivable.getHaveTerm().equals( "1" ) && receivable.getTermId() != null ) {
		PurchaseTerm term = new PurchaseTerm();
		term.setId( receivable.getTermId() );
		term.setTermBuy( "1" );
		purchaseTermDAO.updateById( term );
	    }
	    //把收款状态改为已收款
	    receivable.setBuyStatus( 1 );
	    purchaseReceivablesDAO.updateById( receivable );
	    //修改会员记录
	    updateUserConsume( receivable );

	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    public void updateUserConsume( PurchaseReceivables receivable ) {
	PurchaseOrder order = purchaseOrderDAO.selectById( receivable.getOrderId() );
	if ( CommonUtil.isEmpty( order.getMemberId() ) ) {
	    order.setMemberId( receivable.getMemberId() );
	    purchaseOrderDAO.updateById( order );
	}
	//TODO 需关连会员消费记录 userConsumeMapper.findByOrderCode()方法
	List< Map< String,Object > > list = null;
	//                userConsumeMapper.findByOrderCode(order.getOrderNumber());
	Member member = memberService.findMemberById( receivable.getMemberId(), null );
	/*UserConsume userConsume = new UserConsume();
	if ( list != null && list.size() > 0 ) {
	    userConsume.setId( Integer.parseInt( list.get( 0 ).get( "id" ).toString() ) );
	    if ( CommonUtil.isEmpty( list.get( 0 ).get( "memberid" ) ) ) {
		if ( order.getOrderType().equals( "1" ) ) {
		    userConsume.setPaymenttype( Byte.parseByte( "11" ) );
		    //查询分期
		    List< Map< String,Object > > termList = purchaseTermDAO.findTermList( order.getId() );
		    for ( int i = 0; i < termList.size(); i++ ) {
			PurchaseTerm term = (PurchaseTerm) termList.get( i );
			if ( term.getTermBuy().equals( "0" ) ) {
			    break;
			}
			if ( i == termList.size() - 1 ) {
			    PurchaseOrder orderEntity = new PurchaseOrder();
			    orderEntity.setId( order.getId() );
			    orderEntity.setOrderStatus( "3" );
			    purchaseOrderDAO.updateById( orderEntity );
			}
		    }
		} else {
		    if ( !receivable.getBuyMode().equals( "4" ) ) {
			PurchaseOrder orderEntity = new PurchaseOrder();
			orderEntity.setId( order.getId() );
			orderEntity.setOrderStatus( "3" );
			purchaseOrderDAO.updateById( orderEntity );
		    }
		    userConsume.setPaymenttype( Byte.parseByte( receivable.getBuyMode() ) );
		}
		userConsume.setPaystatus( Byte.parseByte( "1" ) );
		userConsume.setMemberid( member.getId() );
		userConsume.setMcid( member.getMcId() );
		userConsume.setPublicId( member.getPublicId() );
		userConsume.setBususerid( member.getBusid() );
		if ( member.getMcId() != null ) {
		    //TODO 需关连cardMapper方法
		    Card card = null;
		    //                            cardMapper.selectByPrimaryKey(member.getMcId());
		    if ( card != null ) {
			userConsume.setCtid( card.getCtId() );
			userConsume.setGtId( card.getGtId() );
			if ( card.getCtId() == 1 ) {
			    userConsume.setUctype( Byte.parseByte( "0" ) );
			} else if ( card.getCtId() == 2 ) {
			    userConsume.setDiscount( receivable.getDiscount() * 10 );
			    userConsume.setUctype( Byte.parseByte( "2" ) );
			} else if ( card.getCtId() == 3 ) {
			    userConsume.setUctype( Byte.parseByte( "1" ) );
			} else if ( card.getCtId() == 5 ) {
			    userConsume.setUctype( Byte.parseByte( "3" ) );
			}
		    }
		}

		userConsume.setPaystatus( Byte.parseByte( "1" ) );
	    }
	    Integer oldIntegral = 0;
	    if ( receivable.getIntegral() != null && receivable.getIntegral() > 0 ) {
		if ( CommonUtil.isNotEmpty( list.get( 0 ).get( "integral" ) ) ) {
		    oldIntegral = Integer.parseInt( list.get( 0 ).get( "integral" ).toString() );
		}
		userConsume.setIntegral( receivable.getIntegral().intValue() + oldIntegral );
	    }
	    Double oldFenbi = 0.0;
	    if ( receivable.getFansCorrency() != null && receivable.getFansCorrency() > 0 ) {
		if ( CommonUtil.isNotEmpty( list.get( 0 ).get( "fenbi" ) ) ) {
		    oldFenbi = Double.parseDouble( list.get( 0 ).get( "fenbi" ).toString() );
		}
		userConsume.setFenbi( oldFenbi + receivable.getFansCorrency() );
	    }
	    Double oldDiscountMoney = 0.0;
	    if ( receivable.getMoney() != null && receivable.getMoney() > 0 ) {
		if ( CommonUtil.isNotEmpty( list.get( 0 ).get( "discountMoney" ) ) ) {
		    oldDiscountMoney = Double.parseDouble( list.get( 0 ).get( "discountMoney" ).toString() );
		}
		userConsume.setDiscountmoney( receivable.getMoney() + oldDiscountMoney );
	    }
	    //TODO 需关连会员消费记录 userConsumeMapper.updateByPrimaryKeySelective()方法
	    //            userConsumeMapper.updateByPrimaryKeySelective(userConsume);
	}*/
    }

    /**
     * 使用储值卡或者货到付款等支付方式时进入
     *
     * @param request
     * @param memberId
     * @param orderId
     * @param busId
     * @param buyMode
     * @param money
     * @param fansCurrency
     * @param integral
     * @param coupon
     * @param termId
     * @param discount
     */
    @Transactional( rollbackFor = Exception.class )
    public void addReceivables( HttpServletRequest request, Integer memberId, Integer orderId, Integer busId, String buyMode, Double money, Double fansCurrency, Double integral,
		    String coupon, String termId, Integer discount ) {
	try {
	    Integer deduction_jifen = 0;
	    Double deduction_fenbi = 0.0;
	    if ( integral != null && integral > 0 ) {
		//TODO 积分 粉币 方法 memberPayService. deductJifen updateMemberIntergral updateMemberFansCurrency
		//                deduction_jifen = memberPayService.deductJifen(integral, busId);
		//                Map<String, Object> map = memberPayService.updateMemberIntergral(request, memberId, -deduction_jifen);
		//                if (map.get("result") != null && map.get("result").equals("1")) { throw new Exception("积分扣除时出现异常"); }
	    }
	    if ( fansCurrency != null && fansCurrency > 0 ) {
		//                deduction_fenbi = memberPayService.deductFenbi(fansCurrency, busId);
		//                Map<String, Object> map = memberPayService.updateMemberFansCurrency(request, memberId, busId, -deduction_fenbi);
		//                if (map.get("result") != null && map.get("result").equals("1")) { throw new Exception("粉币扣除时出现异常!"); }
	    }
	    //新增收款记录
	    PurchaseReceivables receivables = new PurchaseReceivables();
	    receivables.setBusId( busId );
	    receivables.setBuyMode( buyMode );
	    receivables.setBuyTime( new Date() );
	    receivables.setCoupon( coupon );
	    receivables.setDiscount( discount );
	    receivables.setFansCorrency( deduction_fenbi );
	    receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
	    receivables.setIntegral( Double.parseDouble( deduction_jifen.toString() ) );
	    receivables.setMemberId( memberId );
	    receivables.setMoney( money );
	    receivables.setReceivablesNumber( "CG" + System.currentTimeMillis() );
	    receivables.setOrderId( orderId );
	    receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
	    receivables.setBuyStatus( 1 );
	    purchaseReceivablesDAO.insert( receivables );

	    if ( CommonUtil.isNotEmpty( termId ) ) {
		//修改分期状态
		PurchaseTerm term = new PurchaseTerm();
		term.setId( Integer.parseInt( termId ) );
		term.setTermBuy( "1" );
		purchaseTermDAO.updateById( term );
	    }

	    if ( buyMode.equals( "5" ) && money > 0 ) {
		//TODO 储值卡 方法 memberPayService.storePay
		//                Map<String, Object> map = memberPayService.storePay(memberId, money);
		//                if (map.get("result") != null && map.get("result").equals("1")) { throw new Exception("储值卡消费时扣除出现异常!"); }
	    }
	    updateUserConsume( receivables );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
