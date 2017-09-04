package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.bean.member.MallEntity;
import com.gt.mall.bean.member.MallShopEntity;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MallOrderDAO     mallOrderDAO;
    @Autowired
    private MemberPayService memberPayService;

    @Override
    public MallAllEntity calculateOrder( Map< String,Object > params, Member member ) {

	List< MallOrder > orderList = JSONArray.parseArray( params.get( "order" ).toString(), MallOrder.class );
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
	result.put( "hyDiscountMoney", mallAllEntity.getDiscountMemberMoney() );
	result.put( "yhqDiscountMoney", mallAllEntity.getDiscountConponMoney() );
	result.put( "fbDiscountMoney", mallAllEntity.getDiscountfenbiMoney() );
	result.put( "jfDiscountMoney", mallAllEntity.getDiscountjifenMoney() );
	result.put( "leagueMoney", mallAllEntity.getLeagueMoney() );

	result.put( "balanceMoney", mallAllEntity.getBalanceMoney() );
	return result;
    }
}
