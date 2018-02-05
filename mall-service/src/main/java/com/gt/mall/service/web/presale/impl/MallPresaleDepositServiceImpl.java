package com.gt.mall.service.web.presale.impl;

import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.KeysUtil;
import com.gt.entityBo.ErpRefundBo;
import com.gt.entityBo.NewErpPaySuccessBo;
import com.gt.entityBo.PaySuccessBo;
import com.gt.entityBo.PayTypeBo;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.presale.*;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.entity.presale.*;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.presale.PhoneAddDepositDTO;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallCountIncomeService;
import com.gt.mall.service.web.basic.MallIncomeListService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.wallet.TRefundOrder;
import com.gt.util.entity.result.pay.WxPayOrder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 用户缴纳定金表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPresaleDepositServiceImpl extends BaseServiceImpl< MallPresaleDepositDAO,MallPresaleDeposit > implements MallPresaleDepositService {

    private Logger log = Logger.getLogger( MallPresaleDepositServiceImpl.class );

    @Autowired
    private MallPresaleDepositDAO mallPresaleDepositDAO;

    @Autowired
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallStoreDAO mallStoreDAO;

    @Autowired
    private MallPresaleRankDAO mallPresaleRankDAO;

    @Autowired
    private MallPresaleGiveDAO mallPresaleGiveDAO;

    @Autowired
    private MallPresaleMessageRemindDAO mallPresaleMessageRemindDAO;

    @Autowired
    private MallPresaleDAO mallPresaleDAO;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberPayService memberPayService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayService             payService;
    @Autowired
    private MallCountIncomeService mallCountIncomeService;
    @Autowired
    private MallIncomeListService  mallIncomeListService;

    /**
     * 通过店铺id来查询拍定金
     */
    @Override
    public PageUtil selectPresaleByShopId( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPresaleDepositDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mPresale/deposit.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断预售是否有数据
	    List< MallPresaleDeposit > depositList = mallPresaleDepositDAO.selectByPage( params );
	    if ( depositList != null && depositList.size() > 0 ) {
		for ( MallPresaleDeposit mallPresaleDeposit : depositList ) {
		    if ( shoplist != null && shoplist.size() > 0 ) {
			for ( Map< String,Object > shopMaps : shoplist ) {
			    int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			    if ( mallPresaleDeposit.getShopId() == shop_id ) {
				mallPresaleDeposit.setShopName( shopMaps.get( "sto_name" ).toString() );
				break;
			    }
			}
		    }
		}
	    }
	    page.setSubList( depositList );
	}
	return page;
    }

    /**
     * 定金支付成功的回调函数
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int paySuccessPresale( Map< String,Object > params ) {
	int num = 0;
	String aucNo = params.get( "out_trade_no" ).toString();
	//根据返回的定金单号来查询竞拍定金
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( aucNo );
	if ( deposit != null ) {
	    MallPresaleDeposit dep = new MallPresaleDeposit();
	    dep.setId( deposit.getId() );
	    if ( deposit.getPayWay().toString().equals( "1" ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "transaction_id" ) ) ) {
		    dep.setPayNo( params.get( "transaction_id" ).toString() );
		}
	    }
	    dep.setDepositStatus( 1 );
	    dep.setPayTime( new Date() );
	    num = mallPresaleDepositDAO.updateById( dep );
	    if ( num > 0 ) {
		Member member = memberService.findMemberById( deposit.getUserId(), null );
		//添加交易记录
		MallIncomeList incomeList = new MallIncomeList();
		incomeList.setBusId( member.getBusid() );
		incomeList.setIncomeType( 1 );
		incomeList.setIncomeCategory( 1 );
		incomeList.setIncomeMoney( deposit.getDepositMoney() );
		incomeList.setShopId( deposit.getShopId() );
		incomeList.setBuyerId( deposit.getUserId() );
		incomeList.setBuyerName( member.getNickname() );
		incomeList.setTradeId( deposit.getId() );
		incomeList.setTradeType( 3 );
		incomeList.setProName( deposit.getProName() );
		incomeList.setProNo( deposit.getDepositNo() );
		incomeList.setCreateTime( new Date() );
		mallIncomeListService.insert( incomeList );

		//支付成功，添加当天营业额记录
		mallCountIncomeService.saveTurnover( deposit.getShopId(), deposit.getDepositMoney(), null );

		diffInvNum( deposit );//从redis扣除商品库存
		Map< String,Object > maps = new HashMap< String,Object >();
		maps.put( "presaleId", deposit.getPresaleId() );
		int ranks = 1;//排名
		//判断用户的名次
		MallPresaleRank presaleRank = mallPresaleRankDAO.selectByPresaleId( maps );
		if ( CommonUtil.isNotEmpty( presaleRank ) ) {
		    ranks = presaleRank.getRank() + 1;
		}
		int busUserId = 0;//商家id
		if ( CommonUtil.isNotEmpty( member ) ) {
		    busUserId = member.getBusid();
		}

		if ( busUserId > 0 ) {
		    MallPresaleRank rank = new MallPresaleRank();
		    boolean isInsert = false;
		    //通过排名查询奖品
		    List< MallPresaleGive > giveList = mallPresaleGiveDAO.selectByUserId( busUserId );
		    if ( giveList != null && giveList.size() > 0 ) {
			for ( int i = 0; i < giveList.size(); i++ ) {
			    MallPresaleGive give = giveList.get( i );
			    if ( ranks < give.getGiveRanking() ) {
				rank.setType( give.getGiveType() );
				rank.setGiveName( give.getGiveName() );
				rank.setGiveNum( give.getGiveNum() );
				isInsert = true;
				break;
			    }
			}
		    }
		    if ( isInsert ) {
			rank.setDepositId( deposit.getId() );
			rank.setPresaleId( deposit.getPresaleId() );
			rank.setRank( ranks );
			rank.setMemberId( deposit.getUserId() );
			mallPresaleRankDAO.insert( rank );
		    }

		}

		//储值卡支付
		if ( deposit.getPayWay() == 2 && num > 0 ) {
		    num = petCartPay( deposit );
		}
	    }
	}
	return num;
    }

    /**
     * 储值卡支付
     */
    private int petCartPay( MallPresaleDeposit deposit ) {
	MallStore store = mallStoreDAO.selectById( deposit.getShopId() );

	PaySuccessBo sucess = new PaySuccessBo();
	sucess.setTotalMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//原价
	sucess.setDiscountMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//折扣后金额
	sucess.setPay( CommonUtil.toDouble( deposit.getDepositMoney() ) );//支付金额
	int payType = 1;//微信支付
	if ( deposit.getPayWay() == 2 ) {
	    payType = 5;//储值卡支付
	}
	sucess.setPayType( payType );//支付方式
	sucess.setUcType( 101 );//消费方式 对应字典表 1197
	sucess.setDelay( -1 );//不赠送物品
	sucess.setDataSource( deposit.getBuyerUserType() );

	NewErpPaySuccessBo successBo = new NewErpPaySuccessBo();
	successBo.setMemberId( deposit.getUserId() );//会员id
	successBo.setStoreId( store.getWxShopId() );//门店id

	successBo.setOrderCode( deposit.getOrderNo() );//订单号
	successBo.setTotalMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//应付金额
	successBo.setDiscountMoney( 0d );//优惠金额
	successBo.setDiscountAfterMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//优惠后金额
	successBo.setUcType( Constants.PRESALE_PAY_TYPE );//消费类型 字典1197
	//	    successBo.setUseCoupon( 1 );//是否使用优惠券  0不使用 1使用
	//	    successBo.setCouponType( couponType );//优惠券类型 0微信 1多粉优惠券
	//	    successBo.setCardId( order.getCouponId() ); //卡券id
	//	    successBo.setNumber( order.getCouponUseNum() );//卡券使用数量
	//	    successBo.setUserFenbi( 1 );
	//	    successBo.setFenbiNum( fenbiNum );
	//	    successBo.setUserJifen( 1 );
	//	    successBo.setJifenNum( CommonUtil.toIntegerByDouble( jifenNum ) );

	PayTypeBo payTypeBo = new PayTypeBo();
	payTypeBo.setPayType( CommonUtil.getMemberPayTypeByPresale( deposit.getPayWay(), 0 ) );//支付方式 查询看字典1198
	payTypeBo.setPayMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );
	List< PayTypeBo > typeBoList = new ArrayList<>();
	typeBoList.add( payTypeBo );
	successBo.setPayTypeBos( typeBoList );

	successBo.setDataSource( deposit.getBuyerUserType() );//数据来源 0:pc端 1:微信 2:uc端 3:小程序 4魔盒 5:ERP
	successBo.setIsDiatelyGive( 1 );////是否立即赠送 0延迟送 1立即送

	//支付
	Map< String,Object > payMap = memberPayService.paySuccessNewDan( successBo );
	if ( CommonUtil.isNotEmpty( payMap ) ) {
	    if ( CommonUtil.toInteger( payMap.get( "code" ) ) != 1 ) {
		String msg = ResponseEnums.INTER_ERROR.getDesc();
		if ( CommonUtil.isNotEmpty( payMap.get( "errorMsg" ) ) ) {
		    msg = payMap.get( "errorMsg" ).toString();
		}
		throw new BusinessException( CommonUtil.toInteger( payMap.get( "code" ) ), msg );
	    }
	}
	return 1;
    }

    /**
     * 减去预售商品的库存（redis）
     */
    private void diffInvNum( MallPresaleDeposit dep ) {
	String invKey = "presale_num";//秒杀库存的key
	String field = dep.getPresaleId().toString();
	String specificas = "";

	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( dep.getProSpecificaIds() ) ) {
	    specificas = dep.getProSpecificaIds();
	}
	//查询预售商品的库存
	Integer invNum = 0;
	String value = JedisUtil.maoget( invKey, field );
	if ( CommonUtil.isNotEmpty( value ) ) {
	    JSONObject specObj = JSONObject.fromObject( value );
	    JSONArray arr = new JSONArray();
	    if ( !specificas.equals( "" ) ) {
		//有规格，取规格的库存
		if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
		    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
		    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			for ( Object obj : preSpecArr ) {
			    JSONObject preObj = JSONObject.fromObject( obj );
			    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
				invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
				//break;
				preObj.put( "invNum", invNum - 1 );
			    }
			    arr.add( preObj );
			}
		    }
		}
	    }
	    if ( arr != null && arr.size() > 0 ) {
		specObj.put( "specArr", arr );
	    }
	    if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
		invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
		specObj.put( "invNum", invNum - 1 );
	    }
	    JedisUtil.map( invKey, field, specObj.toString() );
	}
    }

    /**
     * 交纳定金成功
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > addDeposit( Map< String,Object > params, Member member, Integer browser ) throws Exception {
	Map< String,Object > result = new HashMap<>();

	MallPresaleDeposit deposit = com.alibaba.fastjson.JSONObject.parseObject( params.get( "presale" ).toString(), MallPresaleDeposit.class );
	deposit.setUserId( member.getId() );
	deposit.setIsSubmit( 0 );
	MallPresaleDeposit dep = mallPresaleDepositDAO.selectByDeposit( deposit );

	deposit.setBuyerUserType( browser );

	String depNo = "YS" + System.currentTimeMillis();

	if ( dep == null ) {
	    deposit.setCreateTime( new Date() );
	    deposit.setDepositNo( depNo );
	    deposit.setUserId( member.getId() );
	    mallPresaleDepositDAO.insert( deposit );
	} else {//已经交纳了定金，无需再次交纳
	    if ( dep.getDepositStatus().toString().equals( "1" ) ) {
		result.put( "errorMsg", "您已经交纳了定金，无需再次交纳" );
		result.put( "isReturn", 1 );
		result.put( "code", ResponseEnums.ERROR.getCode() );
	    } else {
		deposit.setId( dep.getId() );
		mallPresaleDepositDAO.updateById( deposit );
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		deposit.setDepositNo( dep.getDepositNo() );
	    }
	}
	if ( CommonUtil.isNotEmpty( deposit.getId() ) ) {

	    result = isInvNum( deposit );//判断商品的库存
	    if ( result.get( "code" ).toString().equals( CommonUtil.toString( ResponseEnums.ERROR.getCode() ) ) ) {
		return result;
	    }

	    if ( deposit.getId() > 0 ) {
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		result.put( "id", deposit.getId() );
		result.put( "no", deposit.getDepositNo() );
		result.put( "payWay", deposit.getPayWay() );
		MallPresale presale = mallPresaleDAO.selectById( deposit.getPresaleId() );
		if ( deposit.getPayWay() == 1 || deposit.getPayWay() == 3 ) {
		    deposit.setShopId( presale.getShopId() );
		    String url = getWxAlipay( deposit, member );
		    result.put( "payUrl", url );
		} else if ( deposit.getPayWay() == 2 ) {
		    params.put( "out_trade_no", deposit.getDepositNo() );
		    paySuccessPresale( params );
		    result.put( "payUrl", "/mallPage/" + deposit.getProductId() + "/" + presale.getShopId() + "/79B4DE7C/phoneProduct.do" );
		}
	    } else {
		result.put( "code", ResponseEnums.ERROR.getCode() );
		result.put( "errorMsg", "交纳定金失败" );
	    }
	}
	return result;
    }

    @Override
    public Map< String,Object > addDeposit( PhoneAddDepositDTO depositDTO, Member member, Integer browser ) throws Exception {
	Map< String,Object > result = new HashMap<>();
	MallPresaleDeposit deposit = new MallPresaleDeposit();
	EntityDtoConverter converter = new EntityDtoConverter();
	converter.entityConvertDto( depositDTO, deposit );

	deposit.setUserId( member.getId() );
	deposit.setIsSubmit( 0 );
	MallPresaleDeposit dep = mallPresaleDepositDAO.selectByDeposit( deposit );

	deposit.setBuyerUserType( browser );

	String depNo = "YS" + System.currentTimeMillis();

	if ( dep == null ) {
	    deposit.setCreateTime( new Date() );
	    deposit.setDepositNo( depNo );
	    deposit.setUserId( member.getId() );
	    mallPresaleDepositDAO.insert( deposit );
	} else {//已经交纳了定金，无需再次交纳
	    if ( dep.getDepositStatus().toString().equals( "1" ) ) {
		//		result.put( "errorMsg", "您已经交纳了定金，无需再次交纳" );
		result.put( "isReturn", 1 );
		//		result.put( "code", ResponseEnums.ERROR.getCode() );
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "您已经交纳了定金，无需再次交纳" );
	    } else {
		deposit.setId( dep.getId() );
		mallPresaleDepositDAO.updateById( deposit );
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		deposit.setDepositNo( dep.getDepositNo() );
	    }
	}
	if ( CommonUtil.isNotEmpty( deposit.getId() ) ) {

	    result = isInvNum( deposit );//判断商品的库存
	    if ( result.get( "code" ).toString().equals( CommonUtil.toString( ResponseEnums.ERROR.getCode() ) ) ) {
		return result;
	    }

	    if ( deposit.getId() > 0 ) {
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		result.put( "id", deposit.getId() );
		result.put( "no", deposit.getDepositNo() );
		result.put( "payWay", deposit.getPayWay() );
		if ( deposit.getPayWay() == 1 || deposit.getPayWay() == 3 || deposit.getPayWay() == 4 ) {
		    MallPresale presale = mallPresaleDAO.selectById( deposit.getPresaleId() );
		    deposit.setShopId( presale.getShopId() );
		    String url = getWxAlipay( deposit, member );
		    result.put( "payUrl", url );
		} else if ( deposit.getPayWay() == 2 ) {
		    Map< String,Object > params = new HashMap<>();
		    params.put( "out_trade_no", deposit.getDepositNo() );
		    paySuccessPresale( params );
		    //		    result.put( "payUrl", "/mallPage/" + deposit.getProductId() + "/" + presale.getShopId() + "/79B4DE7C/phoneProduct.do" );
		}
	    } else {
		result.put( "code", ResponseEnums.ERROR.getCode() );
		result.put( "errorMsg", "交纳定金失败" );
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "交纳定金失败" );
	    }
	}
	return result;
    }

    private String getWxAlipay( MallPresaleDeposit deposit, Member member ) throws Exception {
	SubQrPayParams subQrPayParams = new SubQrPayParams();
	subQrPayParams.setTotalFee( CommonUtil.toDouble( deposit.getDepositMoney() ) );
	subQrPayParams.setModel( Constants.PAY_MODEL );
	subQrPayParams.setBusId( member.getBusid() );
	subQrPayParams.setAppidType( 0 );
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( deposit.getDepositNo() );//订单号
	subQrPayParams.setMemberId( member.getId() );//会员id
	subQrPayParams.setDesc( "预售缴纳定金" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	subQrPayParams.setReturnUrl(
			PropertiesUtil.getPhoneWebHomeUrl() + "/goods/details/" + deposit.getShopId() + "/" + member.getBusid() + "/6/" + deposit.getProductId() + "/" + deposit
					.getPresaleId() );
	subQrPayParams.setNotifyUrl( PropertiesUtil.getHomeUrl()
			+ "mallCallback/callbackApi/paySuccessPresale" );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 0 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	//	subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "/mPresale/deposit.do" );//推送路径(尽量不要带参数)
	int payWay = 1;//微信支付
	if ( deposit.getPayWay() == 3 ) {//支付宝支付
	    payWay = 2;
	}
	if ( deposit.getPayWay() == 4 ) {//多粉钱包支付
	    payWay = 3;
	}
	subQrPayParams.setPayWay( payWay );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付
	subQrPayParams.setSourceType( Constants.PAY_SOURCE_TYPE );//墨盒默认0即啊祥不用填,其他人调用填1
	subQrPayParams.setTakeState( 2 );//此订单是否可立即提现(1:是 2:否,不填默认为1)，不可立即提现表示此订单有担保期；注：如传值为2,各erp系统需各自写定时器将超过担保期的订单发送到指定接口

	logger.info( "预售缴纳定金参数：" + com.alibaba.fastjson.JSONObject.toJSONString( subQrPayParams ) );
	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( com.alibaba.fastjson.JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    private Map< String,Object > isInvNum( MallPresaleDeposit dep ) {
	Map< String,Object > result = new HashMap<>();
	String invKey = "presale_num";//秒杀库存的key
	String specificas = "";

	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( dep.getProSpecificaIds() ) ) {
	    specificas = dep.getProSpecificaIds();
	}
	//查询秒杀商品的库存
	Integer invNum = 0;
	String value = JedisUtil.maoget( invKey, dep.getPresaleId().toString() );
	boolean flag = true;
	if ( CommonUtil.isNotEmpty( value ) ) {
	    JSONObject specObj = JSONObject.fromObject( value );
	    if ( !specificas.equals( "" ) ) {
		//有规格，取规格的库存
		if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
		    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
		    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			for ( Object obj : preSpecArr ) {
			    JSONObject preObj = JSONObject.fromObject( obj );
			    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
				invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
				flag = false;
				break;
			    }
			}
		    }
		}
	    }
	    if ( CommonUtil.isEmpty( specificas ) || flag ) {
		//无规格，则取商品库存
		if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
		    invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
		}
	    }
	}

	int proNum = dep.getProNum();//购买商品的用户
	//判断库存是否够
	if ( invNum >= proNum && invNum > 0 ) {
	    result.put( "code", ResponseEnums.SUCCESS.getCode() );
	} else {
	    result.put( "errorMsg", "预售商品的库存不够" );
	    result.put( "code", ResponseEnums.ERROR.getCode() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "预售商品的库存不够" );
	}
	return result;
    }

    /**
     * 根据用户id查询我的所有的定金
     */
    public List< MallPresaleDeposit > getMyPresale( MallPresaleDeposit deposit ) {
	return mallPresaleDepositDAO.selectListByDepositPro( deposit );
    }

    /**
     * 退定金
     */
    @Override
    public void returnDeposit() throws Exception {
	List< Map< String,Object > > List = mallPresaleDepositDAO.selectDepositByEnd();
	if ( List != null && List.size() > 0 ) {
	    for ( int i = 0; i < List.size(); i++ ) {
				/*boolean isReturn = true;*/
		try {
		    Map< String,Object > map = List.get( i );
		    log.info( map );
		    map.put( "isAlipay", false );
		    returnEndPresale( map );
		} catch ( Exception e ) {
		    logger.error( "扫描已结束的预售定金异常" + e );
		    e.printStackTrace();
		}

		/*String endTimes = map.get("create_time").toString();
		String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
		String eDate = DateTimeKit.format(new Date(), format);
		long hour = DateTimeKit.minsBetween(endTimes, eDate, 3600000, format);
		if(hour >= 24){//24小时内 未付款的不退定金
			if(CommonUtil.isNotEmpty(map.get("order_id")) && CommonUtil.isNotEmpty(map.get("order_status"))){
				if(map.get("order_status").toString().equals("1")){//未付款的不退定金
					isReturn = false;
				}
			}
		}

		if(!isReturn){//不能退款，修改定金的状态
			Integer preId = CommonUtil.toInteger(map.get("id"));
			MallPresaleDeposit dep = new MallPresaleDeposit();
			dep.setId(preId);
			dep.setDepositStatus(Byte.valueOf("-2"));

			mallPresaleDepositDao.updateByPrimaryKeySelective(dep);
		}else{
			returnEndMargin(map);
		}*/
	    }
	}
    }

    @Override
    public Map< String,Object > returnEndPresale( Map< String,Object > map ) throws Exception {
	log.info( map );
	Map< String,Object > resultMap = new HashMap< String,Object >();
	Integer memberId = CommonUtil.toInteger( map.get( "user_id" ) );
	Integer payWay = CommonUtil.toInteger( map.get( "pay_way" ) );
	Double money = CommonUtil.toDouble( map.get( "deposit_money" ) );
	String depNo = CommonUtil.toString( map.get( "deposit_no" ) );
	WxPublicUsers pUser = wxPublicUserService.selectByUserId( memberId );
	String returnNo = "YSHTK" + System.currentTimeMillis();
	map.put( "return_no", returnNo );

	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( depNo );
	map.put( "shop_id", deposit.getShopId() );
	if ( payWay.toString().equals( "1" ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款

	    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( depNo );
	    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
		WxmemberPayRefund refund = new WxmemberPayRefund();
		refund.setAppid( pUser.getAppid() );
		refund.setMchid( pUser.getMchId() );
		refund.setRefundFee( money );
		refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );
		refund.setTotalFee( wxPayOrder.getTotalFee() );
		Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );
		log.info( "JSONObject.fromObject(resultmap).toString()" + JSONObject.fromObject( resultmap ).toString() );
		if ( resultmap != null ) {
		    String code = resultmap.get( "code" ).toString();
		    if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
			resultMap.put( "result", true );
			//退款成功修改退款状态
			updateReturnStatus( pUser, map, returnNo );//微信退款
		    } else {
			resultMap.put( "result", false );
			resultMap.put( "msg", resultmap.get( "errorMsg" ) );
		    }
		}
	    } else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
		resultMap.put( "result", true );
		//退款成功修改退款状态
		updateReturnStatus( pUser, map, returnNo );//微信退款
	    }

	} else if ( payWay.toString().equals( "2" ) ) {//储值卡退款
	    Member member = memberService.findMemberById( memberId, null );
	    ErpRefundBo erpRefundBo = new ErpRefundBo();
	    erpRefundBo.setBusId( member.getBusid() );//商家id
	    erpRefundBo.setOrderCode( deposit.getDepositNo() );////订单号
	    erpRefundBo.setRefundPayType( CommonUtil.getMemberPayTypeByPresale( deposit.getPayWay(), 0 ) );////退款方式 字典1198
	    erpRefundBo.setRefundMoney( money ); //退款金额
	    erpRefundBo.setRefundDate( new Date().getTime() );
	    Map< String,Object > memberResultMap = memberService.refundMoney( erpRefundBo );
	    if ( CommonUtil.toInteger( memberResultMap.get( "code" ) ) != 1 ) {
		//同步失败，存入redis
		JedisUtil.rPush( Constants.REDIS_KEY + "member_return_jifen", com.alibaba.fastjson.JSONObject.toJSONString( erpRefundBo ) );

		resultMap.put( "result", true );
		/*if ( CommonUtil.isNotEmpty( memberResultMap.get( "errorMsg" ) ) ) {
		    resultMap.put( "msg", memberResultMap.get( "errorMsg" ) );
		}*/
	    } else {
		updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
        resultMap.put( "result", true );

	    }
	} else if ( payWay.toString().equals( "3" ) && !map.containsKey( "isAlipay" ) ) {
	    //	    updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
	} else if ( payWay.toString().equals( "4" ) ) {
	    Member member = memberService.findMemberById( memberId, null );
	    TRefundOrder refundOrder = new TRefundOrder();
	    refundOrder.setBizOrderNo( returnNo );//商户退款订单号
	    refundOrder.setOriBizOrderNo( deposit.getDepositNo() );//商户原订单号(支付单号)
	    refundOrder.setAmount( money );//订单金额
	    refundOrder.setBackUrl( Constants.PRESALE_REFUND_URL );//异步回调通知
	    refundOrder.setBusId( member.getBusid() );//商家id
	    Map< String,Object > refundResultMap = payService.walletRefund( refundOrder );
	    if ( CommonUtil.toInteger( refundResultMap.get( "code" ) ) == 1 ) {
		resultMap.put( "result", true );
	    } else {
		resultMap.put( "result", false );
		if ( CommonUtil.isNotEmpty( refundResultMap.get( "errorMsg" ) ) ) {
		    resultMap.put( "msg", refundResultMap.get( "errorMsg" ) );
		}
	    }
	}
	return resultMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void returnAlipayDeposit( Map< String,Object > params ) {
	String aucNo = params.get( "out_trade_no" ).toString();//订单号
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( aucNo );
	params.put( "user_id", deposit.getUserId() );
	params.put( "id", deposit.getId() );
	params.put( "deposit_money", deposit.getDepositMoney() );
	params.put( "deposit_no", deposit.getDepositNo() );
	String returnNo = "YSHTK" + System.currentTimeMillis();
	params.put( "return_no", returnNo );
	params.put( "pay_way", deposit.getPayWay() );
	params.put( "shop_id", deposit.getShopId() );
	WxPublicUsers wx = wxPublicUserService.selectByMemberId( deposit.getUserId() );
	updateReturnStatus( wx, params, returnNo );

    }

    /**
     * 修改退款定金的状态
     */
    private void updateReturnStatus( WxPublicUsers pUser, Map< String,Object > map, String returnNo ) {
	/*Integer memberId = CommonUtil.toInteger(map.get("user_id"));*/
	Integer preId = CommonUtil.toInteger( map.get( "id" ) );
	/*Double money = CommonUtil.toDouble(map.get("deposit_money"));
	String depNo = map.get("deposit_no").toString();*/

	MallPresaleDeposit deposit = new MallPresaleDeposit();

	deposit.setId( preId );
	deposit.setDepositStatus( -1 );
	deposit.setReturnNo( map.get( "return_no" ).toString() );
	deposit.setReturnTime( new Date() );
	int num = mallPresaleDepositDAO.updateById( deposit );

	if ( num > 0 ) {
	    String depNo = CommonUtil.toString( map.get( "deposit_no" ) );
	    MallPresaleDeposit deposit1 = mallPresaleDepositDAO.selectByPreNo( depNo );
	    Member member = memberService.findMemberById( deposit1.getUserId(), null );
	    //添加交易记录
	    MallIncomeList incomeList = new MallIncomeList();
	    incomeList.setBusId( member.getBusid() );
	    incomeList.setIncomeType( 2 );
	    incomeList.setIncomeCategory( 1 );
	    incomeList.setIncomeMoney( deposit1.getDepositMoney() );
	    incomeList.setShopId( deposit1.getShopId() );
	    incomeList.setBuyerId( deposit1.getUserId() );
	    incomeList.setBuyerName( member.getNickname() );
	    incomeList.setTradeId( deposit1.getId() );
	    incomeList.setTradeType( 3 );
	    incomeList.setProName( deposit1.getProName() );
	    incomeList.setProNo( deposit1.getDepositNo() );
	    incomeList.setCreateTime( new Date() );
	    mallIncomeListService.insert( incomeList );

	    //退款成功，添加当天营业额记录
	    mallCountIncomeService.saveTurnover( CommonUtil.toInteger( map.get( "shop_id" ) ), null, CommonUtil.toBigDecimal( map.get( "deposit_money" ) ) );
	    //储值卡添加退款记录
	    /*if(map.get("pay_way").toString().equals("2")){
		    //查询该定金的消费记录
		    UserConsume consume = consumeMapper.findByOrderCode1(depNo);
		    if(CommonUtil.isNotEmpty(map.get("shop_id"))){
			    //通过店铺id查询店铺信息
			    Store store = mallStoreDao.selectByPrimaryKey(CommonUtil.toInteger(map.get("shop_id")));
			    int shopsId = store.getWxShopId();
			    if(shopsId <= 0){
				    shopsId = store.getId();
			    }
			    consume.setStoreid(shopsId);//店铺id
		    }
		    int xiaofeiId = 0;
		    if(consume == null){
			    consume =  new UserConsume();
		    }else{
			    xiaofeiId = consume.getId();
			    consume.setId(null);
		    }
		    if(CommonUtil.isNotEmpty(pUser)){
			    consume.setPublicId(pUser.getId());//公众号id
		    }
		    Member member = memberService.findById(memberId);
		    consume.setBususerid(member.getBusid());//商户id
		    consume.setModuletype(Byte.valueOf("0"));//模块类型 0 商城

		    consume.setMemberid(memberId);//买家id
		    consume.setRecordtype(Byte.valueOf("3"));//记录类型 3退款记录
		    consume.setUctype(Byte.valueOf("12"));//消费类型
		    consume.setTotalmoney(money);//退款金额
		    consume.setDiscountmoney(null);
		    consume.setOrderid(preId);//订单id
		    int payWay = CommonUtil.toInteger(map.get("pay_way"));//支付方式
		    if(payWay == 1){//微信支付
			    payWay = 1;
		    }else if(payWay == 2){//储值卡支付
			    payWay = 5;
		    }else if(payWay == 3){//支付宝支付
			    payWay = 0;
		    }
		    consume.setUctable("t_mall_auction_margin");//详情表名
		    consume.setCreatedate(new Date());//创建时间
		    consume.setPaymenttype(Byte.valueOf(payWay+""));//支付方式
		    consume.setPaystatus(Byte.valueOf("3"));//支付状态 3 退单
		    consume.setOrdercode(returnNo);//订单号微信或支付宝或自定义
		    //添加退款记录
		    consumeMapper.insertSelective(consume);
		    if(consume.getId() > 0){//退款成功
			    if(xiaofeiId > 0){
				    UserConsumeRefundKey refund = new UserConsumeRefundKey();
				    refund.setConsumeid(xiaofeiId);//消费id
				    refund.setRefundid(consume.getId());//新添加的退款id
				    consumeRefundMapper.insertSelective(refund);
			    }
		    }
	    }*/

	}
    }

    @Override
    public MallPresaleDeposit getPresaleDepositById( int id ) {
	return mallPresaleDepositDAO.selectById( id );
    }

    @Override
    public int updatePresaleDepositById( MallPresaleDeposit deposit ) {
	return mallPresaleDepositDAO.updateById( deposit );
    }

    @Override
    public MallPresaleDeposit selectCountByPresaleId( Map< String,Object > maps ) {
	return mallPresaleDepositDAO.selectCountByPresaleId( maps );
    }

    /**
     * 交纳定金成功
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > addMessage( Map< String,Object > params, String memberId ) {
	Map< String,Object > result = new HashMap< String,Object >();
	int preId = 0;
	if ( CommonUtil.isNotEmpty( params.get( "preId" ) ) ) {
	    preId = CommonUtil.toInteger( params.get( "preId" ) );
	}
	MallPresaleMessageRemind message = new MallPresaleMessageRemind();
	message.setRemindUserId( CommonUtil.toInteger( memberId ) );
	message.setPresaleId( preId );
	message.setIsRemind( 0 );
	MallPresaleMessageRemind preMessage = mallPresaleMessageRemindDAO.selectByPresale( message );

	MallPresale presale = mallPresaleDAO.selectById( preId );

	message.setIsOpen( 1 );

	int num = 0;
	if ( preMessage == null ) {
	    message.setCreateTime( new Date() );
	    num = mallPresaleMessageRemindDAO.insert( message );
	} else {
	    message.setId( preMessage.getId() );
	    num = mallPresaleMessageRemindDAO.updateById( message );
	}
	if ( num > 0 ) {

	    String key = Constants.REDIS_KEY + "presale_message";
	    String field = presale.getId().toString();
	    JSONArray arr = new JSONArray();
	    String json = JedisUtil.maoget( key, field );
	    if ( CommonUtil.isNotEmpty( json ) ) {
		arr = JSONArray.fromObject( json );
	    }

	    JSONObject obj = new JSONObject();
	    obj.put( "busUserId", presale.getUserId() );
	    obj.put( "wxMemberId", memberId );
	    obj.put( "isStartRemain", 0 );
	    obj.put( "isEndRemain", 0 );
	    arr.add( obj );
	    JedisUtil.map( key, field, arr.toString() );

	    result.put( "result", true );
	} else {
	    result.put( "result", false );
	}
	return result;
    }

    public static void main( String[] args ) {
	int num = 0;
	System.out.println( "num = " + num );
    }

    @Override
    public MallPresaleDeposit selectByDeposit( int depositId ) {
	return mallPresaleDepositDAO.selectById( depositId );
    }

}
