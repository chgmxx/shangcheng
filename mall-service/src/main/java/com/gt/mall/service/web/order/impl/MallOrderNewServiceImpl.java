package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.bean.member.MallEntity;
import com.gt.mall.bean.member.MallShopEntity;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MallAuctionService        mallAuctionService;
    @Autowired
    private MallPresaleService        mallPresaleService;
    @Autowired
    private FenBiFlowService          fenBiFlowService;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallNewOrderAppletService mallNewOrderAppletService;

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
	return result;
    }

    @Override
    public Map< String,Object > submitOrder( Map< String,Object > params, Member member, Integer browser ) {
	Map< String,Object > result = new HashMap<>();
	List< MallOrder > orderList = JSONArray.parseArray( params.get( "order" ).toString(), MallOrder.class );
	List< Map > couponList = null;
	if ( CommonUtil.isNotEmpty( params.get( "couponArr" ) ) ) {
	    couponList = JSONArray.parseArray( params.get( "couponArr" ).toString(), Map.class );
	}
	MallAllEntity allEntity = calculateOrder( params, member, orderList );
	if ( orderList == null || orderList.size() == 0 ) {
	    result.put( "code", ResponseEnums.NULL_ERROR.getCode() );
	    result.put( "errorMsg", ResponseEnums.NULL_ERROR.getDesc() );
	    return result;
	}
	MallOrder order = orderList.get( 0 );

	int code = 1;
	String errorMsg = "";
	for ( MallOrder mallOrder : orderList ) {
	    if ( code == -1 ) {
		break;
	    }
	    Map< String,Object > resultMap = mallNewOrderAppletService.validateOrder( order, member );
	    code = CommonUtil.toInteger( resultMap.get( "code" ) );
	    if ( code == -1 ) {
		if ( CommonUtil.isNotEmpty( resultMap.get( "errorMsg" ) ) ) {
		    errorMsg = result.get( "errorMsg" ).toString();
		}
		break;
	    }
	    mallOrder.setBuyerUserType( browser );
	    mallOrder.setBuyerUserId( member.getId() );
	    mallOrder.setBusUserId( member.getBusid() );
	    mallOrder.setSellerUserId( member.getPublicId() );
	    mallOrder.setOrderStatus( 1 );
	    mallOrder.setCreateTime( new Date() );
	    mallOrder.setMemberName( member.getNickname() );

	    Map< Integer,MallShopEntity > mallShops = allEntity.getMallShops();
	    MallShopEntity shopEntity = null;
	    if ( CommonUtil.isNotEmpty( mallShops ) ) {
		shopEntity = mallShops.get( mallOrder.getWxShopId() );
	    }

	    mallOrder.setOrderMoney( CommonUtil.toBigDecimal( allEntity.getBalanceMoney() ) );

	    for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		//判断商品的库存是不是足够
		result = mallNewOrderAppletService.validateOrderDetail( mallOrder, orderDetail );
		code = CommonUtil.toInteger( result.get( "code" ) );
		if ( code == -1 ) {
		    if ( CommonUtil.isNotEmpty( result.get( "errorMsg" ) ) ) {
			errorMsg = result.get( "errorMsg" ).toString();
		    }
		    break;
		}

		orderDetail = getOrderDetailParams( mallOrder, orderDetail, shopEntity, couponList );

	    }
	}
	result.put( "code", code );
	if ( code == -1 ) {
	    result.put( "errorMsg", errorMsg );
	    return result;
	}
	MallOrder parentOrder = orderList.get( 0 );
	if ( CommonUtil.isNotEmpty( allEntity.getBalanceMoney() ) && allEntity.getBalanceMoney() > 0 ) {
	    double freight = CommonUtil.add( CommonUtil.toDouble( order.getOrderFreightMoney() ), allEntity.getBalanceMoney() );
	    order.setOrderMoney( CommonUtil.toBigDecimal( freight ) );
	}

	result.put( "payWay", order.getOrderPayWay() );

	return result;
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
		orderDetail.setTotalPrice( mallEntity.getBalanceMoney() );

		orderDetail.setCouponCode( shopEntity.getCodes() );

		orderDetail.setDiscountedPrices( CommonUtil.toBigDecimal( CommonUtil.div( mallEntity.getTotalMoneyAll(), mallEntity.getBalanceMoney(), 2 ) ) );
		if ( mallEntity.getUseFenbi() == 1 ) {
		    orderDetail.setUseFenbi( mallEntity.getFenbiNum() );
		    orderDetail.setFenbiYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountfenbiMoney() ) );
		}
		if ( mallEntity.getUserJifen() == 1 ) {
		    orderDetail.setUseJifen( mallEntity.getJifenNum() );
		    orderDetail.setIntegralYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountjifenMoney() ) );
		}

		if ( orderDetail.getUseCoupon() == 1 && couponList != null && couponList.size() > 0 ) {
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
