package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.KeysUtil;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.bean.member.MallEntity;
import com.gt.mall.bean.member.MallShopEntity;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderNewServiceImpl extends BaseServiceImpl< MallOrderDAO,MallOrder > implements MallOrderNewService {

    private static Logger logger = LoggerFactory.getLogger( MallOrderNewServiceImpl.class );

    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MemberPayService          memberPayService;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallNewOrderAppletService mallNewOrderAppletService;
    @Autowired
    private MallOrderDetailDAO        mallOrderDetailDAO;
    @Autowired
    private MallOrderService          mallOrderService;
    @Autowired
    private MallShopCartDAO           mallShopCartDAO;
    @Autowired
    private MallAuctionBiddingService mallAuctionBiddingService;
    @Autowired
    private MemberAddressService      memberAddressService;
    @Autowired
    private WxPublicUserService       wxPublicUserService;
    @Autowired
    private PayService                payService;

    @Override
    public MallAllEntity calculateOrder( Map< String,Object > params, Member member, List< MallOrder > orderList ) {

	List< Map > couponList = null;
	if ( CommonUtil.isNotEmpty( params.get( "couponArr" ) ) ) {
	    couponList = JSONArray.parseArray( params.get( "couponArr" ).toString(), Map.class );
	}

	if ( CommonUtil.isEmpty( orderList ) ) {
	    return null;
	}

	Map< Integer,MallShopEntity > mallShops = new HashMap<>();
	for ( MallOrder mallOrder : orderList ) {
	    Map< Integer,MallEntity > productMap = new HashMap<>();
	    MallShopEntity shopEntity = new MallShopEntity();
	    if ( mallOrder.getMallOrderDetail() != null && mallOrder.getMallOrderDetail().size() > 0 ) {
		for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		    MallEntity entity = new MallEntity();
		    entity.setMallId( orderDetail.getIndex() );
		    entity.setNumber( orderDetail.getDetProNum() );
		    entity.setTotalMoneyOne( CommonUtil.toDouble( orderDetail.getDetProPrice() ) );
		    entity.setTotalMoneyAll( orderDetail.getTotalPrice() );
		    entity.setUserCard( orderDetail.getUserCard() );
		    entity.setUseCoupon( orderDetail.getUseCoupon() );
		    entity.setUseFenbi( orderDetail.getUserFenbi() );
		    entity.setUserJifen( orderDetail.getUserJifen() );

		    productMap.put( orderDetail.getIndex(), entity );

		}
	    }
	    shopEntity.setShopId( mallOrder.getWxShopId() );
	    shopEntity.setMalls( productMap );
	    if ( mallOrder.getUseCoupon() == 1 && couponList != null && couponList.size() > 0 ) {
		shopEntity.setUseCoupon( mallOrder.getUseCoupon() );

		for ( Map map : couponList ) {
		    String wxShopId = map.get( "wxShopId" ).toString();
		    if ( wxShopId.equals( mallOrder.getWxShopId().toString() ) ) {
			int couponType = CommonUtil.toInteger( map.get( "couponType" ) );
			couponType = couponType == 2 ? 1 : 0;
			shopEntity.setCouponType( couponType );
			shopEntity.setCoupondId( CommonUtil.toInteger( map.get( "coupondId" ) ) );
			break;
		    }
		}
	    }
	    mallShops.put( mallOrder.getWxShopId(), shopEntity );
	}

	MallAllEntity allEntity = new MallAllEntity();
	allEntity.setMemberId( member.getId() );
	allEntity.setTotalMoney( CommonUtil.toDouble( params.get( "productAll" ) ) );
	if ( CommonUtil.isNotEmpty( params.get( "useFenbi" ) ) ) {
	    allEntity.setUseFenbi( CommonUtil.toInteger( params.get( "useFenbi" ) ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "useJifen" ) ) ) {
	    allEntity.setUserJifen( CommonUtil.toInteger( params.get( "useJifen" ) ) );
	}
	allEntity.setMallShops( mallShops );

	return memberPayService.memberCountMoneyByShop( allEntity );
    }

    @Override
    public Map< String,Object > getCalculateData( MallAllEntity mallAllEntity ) {
	Map< String,Object > result = new HashMap<>();
	result.put( "discountMemberMoney", mallAllEntity.getDiscountMemberMoney() );
	result.put( "discountConponMoney", mallAllEntity.getDiscountConponMoney() );
	result.put( "discountfenbiMoney", mallAllEntity.getDiscountfenbiMoney() );
	result.put( "discountjifenMoney", mallAllEntity.getDiscountjifenMoney() );
	result.put( "leagueMoney", mallAllEntity.getLeagueMoney() );

	result.put( "balanceMoney", mallAllEntity.getBalanceMoney() );

	result.put( "userJifen", mallAllEntity.getUserJifen() );
	result.put( "useFenbi", mallAllEntity.getUseFenbi() );

	result.put( "canUsefenbi", mallAllEntity.getCanUsefenbi() );
	result.put( "canUseJifen", mallAllEntity.getCanUseJifen() );

	result.put( "fenbiNum", mallAllEntity.getFenbiNum() );
	result.put( "jifenNum", mallAllEntity.getJifenNum() );
	return result;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > submitOrder( Map< String,Object > params, Member member, Integer browser ) throws Exception {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Map< String,Object > result = new HashMap<>();
	List< MallOrder > orderList = JSONArray.parseArray( params.get( "order" ).toString(), MallOrder.class );
	List< Map > couponList = null;
	if ( CommonUtil.isNotEmpty( params.get( "couponArr" ) ) ) {
	    couponList = JSONArray.parseArray( params.get( "couponArr" ).toString(), Map.class );
	}

	if ( orderList == null || orderList.size() == 0 ) {
	    result.put( "code", ResponseEnums.NULL_ERROR.getCode() );
	    result.put( "errorMsg", ResponseEnums.NULL_ERROR.getDesc() );
	    return result;
	}
	MallOrder order = orderList.get( 0 );
	//计算订单
	MallAllEntity allEntity = null;
	if ( params.containsKey( "useFenbi" ) || params.containsKey( "useJifen" ) || params.containsKey( "couponArr" ) ) {
	    allEntity = calculateOrder( params, member, orderList );
	}
	MemberAddress memberAddress = null;
	//根据地址id查询地址信息
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    memberAddress = memberAddressService.addreSelectId( order.getReceiveId() );
	}
	int code = 1;
	String errorMsg = "";
	double freightMoney = 0;
	double orderMoneyAll = 0;
	for ( MallOrder mallOrder : orderList ) {
	    if ( code == -1 ) {
		break;
	    }
	    Map< String,Object > resultMap = mallNewOrderAppletService.validateOrder( mallOrder, member );//验证订单
	    code = CommonUtil.toInteger( resultMap.get( "code" ) );
	    if ( code == -1 ) {
		if ( CommonUtil.isNotEmpty( resultMap.get( "errorMsg" ) ) ) {
		    errorMsg = result.get( "errorMsg" ).toString();
		}
		break;
	    }
	    MallShopEntity shopEntity = null;
	    if ( CommonUtil.isNotEmpty( allEntity ) ) {
		Map< Integer,MallShopEntity > mallShops = allEntity.getMallShops();
		if ( CommonUtil.isNotEmpty( mallShops ) ) {
		    shopEntity = mallShops.get( mallOrder.getWxShopId() );
		}
	    }
	    mallOrder = getOrderParams( mallOrder, browser, member, shopEntity, memberAddress );

	    freightMoney += CommonUtil.toDouble( mallOrder.getOrderFreightMoney() );
	    orderMoneyAll += CommonUtil.toDouble( mallOrder.getOrderMoney() );

	    double useFenbi = 0;
	    double useJifen = 0;
	    for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		//判断商品的库存是不是足够
		result = mallNewOrderAppletService.validateOrderDetail( mallOrder, orderDetail );//验证订单详情
		code = CommonUtil.toInteger( result.get( "code" ) );
		if ( code == -1 ) {
		    if ( CommonUtil.isNotEmpty( result.get( "errorMsg" ) ) ) {
			errorMsg = result.get( "errorMsg" ).toString();
		    }
		    break;
		}
		orderDetail = getOrderDetailParams( mallOrder, orderDetail, shopEntity, couponList );
		if ( CommonUtil.isNotEmpty( orderDetail.getUseFenbi() ) ) {
		    useFenbi += orderDetail.getUseFenbi();
		}
		if ( CommonUtil.isNotEmpty( orderDetail.getUseJifen() ) ) {
		    useJifen += orderDetail.getUseJifen();
		}
	    }
	    if ( useJifen > 0 ) {
		mallOrder.setUseJifen( CommonUtil.toDouble( df.format( useJifen ) ) );
	    }
	    if ( useFenbi > 0 ) {
		mallOrder.setUseFenbi( CommonUtil.toDouble( df.format( useFenbi ) ) );
	    }
	}

	if ( code == -1 ) {
	    result.put( "code", code );
	    result.put( "errorMsg", errorMsg );
	    return result;
	}
	MallOrderDetail detail = new MallOrderDetail();
	MallOrder parentOrder = orderList.get( 0 ).clone();
	int shopId = 0;
	double orderAllMoney = 0;
	//如果有多个订单，则生成一个主订单
	if ( orderList.size() > 0 ) {
	    if ( CommonUtil.isNotEmpty( allEntity ) ) {
		orderAllMoney = allEntity.getBalanceMoney();
		parentOrder.setUseFenbi( allEntity.getFenbiNum() );
		parentOrder.setUseJifen( allEntity.getJifenNum() );
		orderAllMoney = CommonUtil.add( freightMoney, orderAllMoney );
	    } else {
		orderAllMoney = CommonUtil.toDouble( df.format( orderMoneyAll ) );
	    }

	    parentOrder.setOrderMoney( CommonUtil.toBigDecimal( orderAllMoney ) );
	    parentOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( freightMoney ) );
	    parentOrder.setMallOrderDetail( null );
	    orderList.add( 0, parentOrder );
	}
	logger.info( "orderParams" + JSONArray.toJSON( orderList ) );

	/*if ( orderList.size() > 0 ) {
	    throw new BusinessException( "ss" );
	}*/

	int orderPId = 0;
	String orderNo = "";

	//新增订单
	for ( int i = 0; i < orderList.size(); i++ ) {
	    MallOrder mallOrder = orderList.get( i );
	    mallOrder.setOrderNo( "SC" + System.currentTimeMillis() );
	    mallOrder.setCreateTime( new Date() );
	    if ( orderPId > 0 ) {
		mallOrder.setOrderPid( orderPId );
	    }
	    if ( i == 0 ) {
		orderNo = mallOrder.getOrderNo();
	    }
	    int count = mallOrderDAO.insert( mallOrder );
	    if ( i == 0 && count > 0 ) {
		orderPId = mallOrder.getId();
	    }
	    if ( count < 0 ) {
		code = -1;
		break;
	    }
	    if ( mallOrder.getMallOrderDetail() != null && mallOrder.getMallOrderDetail().size() > 0 ) {
		for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		    detail = orderDetail;
		    shopId = detail.getShopId();
		    orderDetail.setOrderId( mallOrder.getId() );
		    orderDetail.setCreateTime( new Date() );
		    count = mallOrderDetailDAO.insert( orderDetail );
		    if ( count < 0 ) {
			code = -1;
			break;
		    }
		}
	    }

	}
	if ( code < 0 ) {
	    result.put( "code", code );
	    result.put( "errorMsg", "提交订单失败，请稍后重试" );
	    return result;
	}

	//订单生成成功，把订单加入到未支付的队列中（秒杀商品除外）
	if ( order.getOrderType() != 3 ) {
	    String key = Constants.REDIS_KEY + "hOrder_nopay";
	    net.sf.json.JSONObject objs = new net.sf.json.JSONObject();
	    String times = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    objs.put( "times", times );
	    objs.put( "orderId", orderPId );
	    JedisUtil.map( key, orderPId + "", objs.toString() );
	}
	//拍卖，添加拍卖竞拍
	if ( order.getOrderType() == 4 ) {
	    //加入拍卖竞拍
	    mallAuctionBiddingService.addBidding( order, orderList.get( 0 ).getMallOrderDetail() );
	}
	//联盟
	String unionKey = Constants.REDIS_KEY + "union_order_" + order.getId();
	if ( CommonUtil.isNotEmpty( params.get( "union_id" ) ) && CommonUtil.isNotEmpty( params.get( "cardId" ) ) ) {
	    JSONObject unionJson = new JSONObject();
	    unionJson.put( "union_id", params.get( "union_id" ) );
	    unionJson.put( "cardId", params.get( "cardId" ) );
	    unionJson.put( "orderId", orderPId );
	    unionJson.put( "orderStatus", 1 );//未支付
	    JedisUtil.set( unionKey, unionJson.toString(), 60 * 60 );//以秒为单位，保存1个小时 60*60*1
	}
	//删除购物车的商品
	if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
	    String[] cartIds = params.get( "cartIds" ).toString().split( "," );
	    if ( cartIds != null && cartIds.length > 0 ) {
		for ( Object str : cartIds ) {
		    if ( CommonUtil.isNotEmpty( str ) ) {
			mallShopCartDAO.deleteById( CommonUtil.toInteger( str ) );
		    }
		}
	    }
	}

	//货到付款或支付金额为0的订单，直接修改订单状态为已支付，且修改商品库存和销量
	if ( orderAllMoney == 0 || ( order.getOrderPayWay() != 1 && order.getOrderPayWay() != 9 ) ) {
	    Map< String,Object > payParams = new HashMap<>();
	    payParams.put( "status", 2 );
	    payParams.put( "out_trade_no", orderNo );
	    mallOrderService.paySuccessModified( payParams, member );//修改库存和订单状态
	}

	result.put( "orderNo", orderNo );
	result.put( "payWay", order.getOrderPayWay() );
	result.put( "proTypeId", detail.getProTypeId() );
	result.put( "code", code );
	String url = "/phoneOrder/79B4DE7C/orderList.do?isPayGive=1&&orderId=" + orderPId + "&&uId=" + member.getBusid();
	if ( order.getOrderPayWay() == 7 ) {
	    url = "/phoneOrder/" + orderPId + "/79B4DE7C/getDaiFu.do?uId=" + member.getBusid();
	} else if ( order.getOrderPayWay() == 4 ) {
	    url = "/phoneIntegral/" + shopId + "/79B4DE7C/recordList.do?uId=" + member.getBusid() + "&&orderId=" + orderPId;
	}
	if ( orderAllMoney > 0 && ( order.getOrderPayWay() == 1 || order.getOrderPayWay() == 9 ) ) {

	    url = wxPayWay( orderAllMoney, orderNo, order );
	}
	result.put( "url", url );
	return result;
    }

    @Override
    public String wxPayWay( double orderAllMoney, String orderNo, MallOrder order ) throws Exception {
	if ( orderAllMoney == 0 ) {
	    orderAllMoney = CommonUtil.toDouble( order.getOrderMoney() );
	}
	if ( CommonUtil.isEmpty( orderNo ) ) {
	    orderNo = order.getOrderNo();
	}
	SubQrPayParams subQrPayParams = new SubQrPayParams();
	subQrPayParams.setTotalFee( orderAllMoney );
	subQrPayParams.setModel( Constants.PAY_MODEL );
	subQrPayParams.setBusId( order.getBusUserId() );
	subQrPayParams.setAppidType( 0 );
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( orderNo );//订单号
	subQrPayParams.setMemberId( order.getBuyerUserId() );//会员id
	subQrPayParams.setDesc( "商城下单" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	subQrPayParams.setReturnUrl( PropertiesUtil.getHomeUrl() + "/phoneOrder/79B4DE7C/orderList.do" );
	subQrPayParams.setNotifyUrl( PropertiesUtil.getHomeUrl()
			+ "/phoneOrder/79B4DE7C/paySuccessModified.do" );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 1 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "mallOrder/toIndex.do" );//推送路径(尽量不要带参数)
	int payWay = 1;
	if ( order.getOrderPayWay() == 9 ) {
	    payWay = 2;
	}
	if ( order.getIsWallet() == 1 ) {
	    payWay = 3;
	}
	subQrPayParams.setPayWay( payWay );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付

	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    private MallOrder getOrderParams( MallOrder mallOrder, int browser, Member member, MallShopEntity shopEntity, MemberAddress memberAddress ) {
	mallOrder.setBuyerUserType( browser );
	mallOrder.setBuyerUserId( member.getId() );
	mallOrder.setBusUserId( member.getBusid() );
	mallOrder.setSellerUserId( member.getPublicId() );
	mallOrder.setOrderStatus( 1 );
	mallOrder.setCreateTime( new Date() );
	mallOrder.setMemberName( member.getNickname() );
	double freightMoney = 0;
	if ( CommonUtil.isNotEmpty( mallOrder.getOrderFreightMoney() ) ) {
	    freightMoney = CommonUtil.toDouble( mallOrder.getOrderFreightMoney() );
	}
	if ( CommonUtil.isNotEmpty( shopEntity ) ) {
	    mallOrder.setOrderMoney( CommonUtil.toBigDecimal( CommonUtil.add( shopEntity.getBalanceMoney(), freightMoney ) ) );
	}
	if ( CommonUtil.isEmpty( mallOrder.getOrderType() ) ) {
	    mallOrder.setOrderType( 0 );
	}
	if ( CommonUtil.isNotEmpty( memberAddress ) ) {
	    mallOrder.setReceiveName( memberAddress.getMemName() );
	    mallOrder.setReceivePhone( memberAddress.getMemPhone() );
	    String address = memberAddress.getProvincename() + memberAddress.getCityname() + memberAddress.getAreaname() + memberAddress.getMemAddress();
	    if ( CommonUtil.isNotEmpty( memberAddress.getMemZipCode() ) ) {
		address += memberAddress.getMemZipCode();
	    }
	    mallOrder.setReceiveAddress( address );

	}
	return mallOrder;
    }

    private MallOrderDetail getOrderDetailParams( MallOrder mallOrder, MallOrderDetail orderDetail, MallShopEntity shopEntity, List< Map > couponList ) {
	MallProduct product = mallProductService.selectByPrimaryKey( orderDetail.getProductId() );
	orderDetail.setReturnDay( product.getReturnDay() );
	orderDetail.setProTypeId( product.getProTypeId() );
	orderDetail.setDetProName( product.getProName() );
	if ( CommonUtil.isNotEmpty( product.getCardType() ) && product.getCardType() > 0 ) {
	    orderDetail.setCardReceiveId( product.getCardType() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowId() ) && product.getFlowId() > 0 ) {
	    orderDetail.setFlowId( product.getFlowId() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) && product.getFlowRecordId() > 0 ) {
	    orderDetail.setFlowRecordId( product.getFlowRecordId() );
	}
	//查询计算后的商品价格
	if ( CommonUtil.isNotEmpty( shopEntity ) ) {
	    Map< Integer,MallEntity > malls = shopEntity.getMalls();
	    MallEntity mallEntity = malls.get( orderDetail.getIndex() );
	    if ( CommonUtil.isNotEmpty( mallEntity ) ) {
		orderDetail.setDetProPrice( CommonUtil.toBigDecimal( mallEntity.getUnitPrice() ) );
		orderDetail.setDetPrivivilege( CommonUtil.toBigDecimal( mallEntity.getTotalMoneyOne() ) );
		orderDetail.setDiscountedPrices( CommonUtil.toBigDecimal( CommonUtil.subtract( orderDetail.getTotalPrice(), mallEntity.getBalanceMoney() ) ) );
		orderDetail.setTotalPrice( mallEntity.getBalanceMoney() );

		if ( mallEntity.getUseFenbi() == 1 ) {
		    orderDetail.setUseFenbi( mallEntity.getFenbiNum() );
		    orderDetail.setFenbiYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountfenbiMoney() ) );
		}
		if ( mallEntity.getUserJifen() == 1 ) {
		    orderDetail.setUseJifen( mallEntity.getJifenNum() );
		    orderDetail.setIntegralYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountjifenMoney() ) );
		}

		if ( orderDetail.getUseCoupon() == 1 && couponList != null && couponList.size() > 0 ) {
		    orderDetail.setCouponCode( shopEntity.getCodes() );
		    for ( Map couponMap : couponList ) {
			String wxShopId = couponMap.get( "wxShopId" ).toString();
			if ( wxShopId.equals( mallOrder.getWxShopId().toString() ) ) {
			    couponMap.put( "couponCode", shopEntity.getCodes() );
			    orderDetail.setDuofenCoupon( JSONObject.toJSONString( couponMap ) );
			    orderDetail.setUseCardId( CommonUtil.toInteger( couponMap.get( "coupondId" ) ) );
			    break;
			}
		    }
		}
	    }
	}
	return orderDetail;
    }

}
