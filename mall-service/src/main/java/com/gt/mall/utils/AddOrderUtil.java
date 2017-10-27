package com.gt.mall.utils;

import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.order.add.PhoneAddOrderBusDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderProductDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderShopDTO;
import org.apache.log4j.Logger;

public class AddOrderUtil {

    private static final Logger logger = Logger.getLogger( AddOrderUtil.class );

    /**
     * 判断订单传参是否完整
     */
    public PhoneAddOrderDTO isOrderParams( PhoneAddOrderDTO params ) {
	if ( CommonUtil.isEmpty( params.getBusResultList() ) ) {
	    logger.error( "商家传参不完整" );
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
	}
	if ( CommonUtil.isEmpty( params.getSelectPayWayId() ) || params.getSelectPayWayId() == 0 ) {
	    logger.error( "还没选则支付方式" );
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您还未选择支付方式" );
	}
	int addressId = 0;
	if ( CommonUtil.isNotEmpty( params.getSelectMemberAddressId() ) ) {
	    addressId = params.getSelectMemberAddressId();
	}
	for ( PhoneAddOrderBusDTO busDTO : params.getBusResultList() ) {//循环商家集合
	    if ( CommonUtil.isEmpty( busDTO.getShopResultList() ) ) {
		logger.error( "店铺参数不完整" );
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
	    }
	    if ( CommonUtil.isEmpty( busDTO.getSelectDeliveryWayId() ) || busDTO.getSelectDeliveryWayId() == 0 ) {
		logger.error( "您还没选中配送方式" );
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您还没选中配送方式" );
	    }
	    if ( busDTO.getSelectDeliveryWayId() == 1 && addressId == 0 ) {//1 快递配送
		logger.error( "您还没选中配送方式" );
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您还没选中配送方式" );
	    }
	    for ( PhoneAddOrderShopDTO shopDTO : busDTO.getShopResultList() ) {//循环店铺集合
		if ( CommonUtil.isEmpty( shopDTO.getSelectCouponsId() ) ) {
		    logger.error( "商品参数不完整" );
		    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
		}
		int selectCouponsId = 0;//选中的优惠券id
		if ( CommonUtil.isNotEmpty( shopDTO.getSelectCouponsId() ) ) {
		    selectCouponsId = shopDTO.getSelectCouponsId();
		}
		for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合

		}
	    }
	}

	return params;
    }

    /**
     * 计算订单信息
     */
    public PhoneAddOrderDTO calculateOrder( PhoneAddOrderDTO params ) {
	for ( PhoneAddOrderBusDTO busDTO : params.getBusResultList() ) {//循环商家集合
	    for ( PhoneAddOrderShopDTO shopDTO : busDTO.getShopResultList() ) {//循环店铺集合
		int selectCouponsId = 0;//选中的优惠券id
		if ( CommonUtil.isNotEmpty( shopDTO.getSelectCouponsId() ) ) {
		    selectCouponsId = shopDTO.getSelectCouponsId();
		}
		for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合

		}
	    }
	}

	return params;
    }
}
