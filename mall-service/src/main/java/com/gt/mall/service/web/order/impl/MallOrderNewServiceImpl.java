package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
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
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.util.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MallSeckillService        mallSeckillService;

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
			shopEntity.setCouponType( CommonUtil.toInteger( map.get( "couponType" ) ) );
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
    public Map< String,Object > submitOrder( Map< String,Object > params, Member member, Integer browser ) {
	Map< String,Object > result = new HashMap<>();
	List< MallOrder > orderList = JSONArray.parseArray( params.get( "order" ).toString(), MallOrder.class );
	List< Map > couponList = null;
	if ( CommonUtil.isNotEmpty( params.get( "couponArr" ) ) ) {
	    couponList = JSONArray.parseArray( params.get( "couponArr" ).toString(), Map.class );
	}
	MallAllEntity allEntity = null;
	if ( orderList == null || orderList.size() == 0 ) {
	    result.put( "code", ResponseEnums.NULL_ERROR.getCode() );
	    result.put( "errorMsg", ResponseEnums.NULL_ERROR.getDesc() );
	    return result;
	}
	//计算订单
	if ( params.containsKey( "useFenbi" ) || params.containsKey( "useJifen" ) || params.containsKey( "couponArr" ) ) {
	    allEntity = calculateOrder( params, member, orderList );
	}
	MallOrder order = orderList.get( 0 );
	int code = 1;
	String errorMsg = "";
	double freightMoney = 0;
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
	    mallOrder = getOrderParams( mallOrder, browser, member, shopEntity );

	    freightMoney += CommonUtil.toDouble( mallOrder.getOrderFreightMoney() );

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

	    mallOrder.setUseJifen( useJifen );
	    mallOrder.setUseFenbi( useFenbi );
	}

	if ( code == -1 ) {
	    result.put( "code", code );
	    result.put( "errorMsg", errorMsg );
	    return result;
	}
	MallOrderDetail detail = new MallOrderDetail();
	MallOrder parentOrder = orderList.get( 0 ).clone();
	int shopId = 0;
	//如果有多个订单，则生成一个主订单
	if ( orderList.size() > 1 ) {
	    double money = CommonUtil.toDouble( parentOrder.getOrderMoney() );
	    if ( CommonUtil.isNotEmpty( allEntity ) ) {
		money = allEntity.getBalanceMoney();
		parentOrder.setUseFenbi( allEntity.getFenbiNum() );
		parentOrder.setUseJifen( allEntity.getJifenNum() );
	    }
	    double orderAllMoney = CommonUtil.add( freightMoney, money );
	    parentOrder.setOrderMoney( CommonUtil.toBigDecimal( orderAllMoney ) );
	    parentOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( freightMoney ) );
	    parentOrder.setMallOrderDetail( null );
	    orderList.add( 0, parentOrder );
	}
	logger.info( "orderParams" + JSONArray.toJSON( orderList ) );
	if ( orderList.size() > 0 ) {
	    throw new BusinessException( "ss" );
	}

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
	    int count = 0;
	    if ( orderList.size() > 1 && i == 0 ) {
		count = mallOrderDAO.insert( mallOrder );
		if ( count > 0 ) {
		    orderPId = mallOrder.getId();
		    continue;
		}
	    } else {
		count = mallOrderDAO.insert( mallOrder );
		if ( i == 0 && count > 0 ) {
		    orderPId = mallOrder.getId();
		}
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
	    String key = "hOrder_nopay";
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
	if ( CommonUtil.toDouble( order.getOrderMoney() ) == 0 || ( order.getOrderPayWay() != 1 && order.getOrderPayWay() != 9 ) ) {
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
	result.put( "url", url );
	return result;
    }

    private MallOrder getOrderParams( MallOrder mallOrder, int browser, Member member, MallShopEntity shopEntity ) {
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
