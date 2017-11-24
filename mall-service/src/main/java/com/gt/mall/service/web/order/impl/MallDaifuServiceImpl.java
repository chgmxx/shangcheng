package com.gt.mall.service.web.order.impl;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.order.MallDaifuDAO;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.phone.order.daifu.PhoneGetDaiFuProductResult;
import com.gt.mall.result.phone.order.daifu.PhoneGetDaiFuResult;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderDetailService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 找人代付 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallDaifuServiceImpl extends BaseServiceImpl< MallDaifuDAO,MallDaifu > implements MallDaifuService {

    @Autowired
    private MallDaifuDAO           mallDaifuDAO;
    @Autowired
    private MallOrderService       mallOrderService;
    @Autowired
    private MallOrderDetailService mallOrderDetailService;

    @Override
    public MallDaifu selectByDfOrderNo( String dfOrderNo ) {
	return mallDaifuDAO.selectByDfOrderNo( dfOrderNo );
    }

    @Override
    public PhoneGetDaiFuResult getDaifuResult( Integer orderId, Member member, Integer browerType ) {
	MallOrder mallOrder = mallOrderService.selectById( orderId );
	if ( CommonUtil.isEmpty( mallOrder ) ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}
	List< MallOrderDetail > detailList = mallOrderDetailService.getOrderDetailList( orderId );
	if ( detailList == null || detailList.size() == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}

	PhoneGetDaiFuResult daiFuResult = new PhoneGetDaiFuResult();
	List< PhoneGetDaiFuProductResult > productResultList = new ArrayList<>();
	for ( MallOrderDetail detail : detailList ) {
	    PhoneGetDaiFuProductResult productResult = new PhoneGetDaiFuProductResult();
	    productResult.setProductId( detail.getProductId() );
	    productResult.setShopId( detail.getShopId() );
	    productResult.setProductName( detail.getDetProName() );
	    productResult.setProductImageUrl( detail.getProductImageUrl() );
	    productResult.setProductNum( detail.getDetProNum() );
	    productResult.setProductPrice( CommonUtil.toDouble( detail.getDetProPrice() ) );
	    productResultList.add( productResult );
	    daiFuResult.setShopId( detail.getShopId() );
	}
	daiFuResult.setBusId( mallOrder.getBusUserId() );
	daiFuResult.setOrderMoney( CommonUtil.toDouble( mallOrder.getOrderMoney() ) );
	daiFuResult.setRecevieUserName( mallOrder.getReceiveName() );
	daiFuResult.setProductResultList( productResultList );
	Date endTime = mallOrder.getCreateTime();
	Date endHourTime = DateTimeKit.addHours( endTime, 24 );
	Date nowTime = new Date();
	long times = ( endHourTime.getTime() - nowTime.getTime() ) / 1000;
	if ( times > 0 && mallOrder.getOrderStatus() == 1 ) {
	    daiFuResult.setIsShowTimes( 1 );
	    daiFuResult.setTimes( times );
	}
	if ( browerType == 1 && CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getPublicId() ) && member.getPublicId() > 0 ) {
	    daiFuResult.setIsShowWxPay( 1 );
	} else {
	    daiFuResult.setIsShowAliPay( 1 );
	}
	if ( CommonUtil.isNotEmpty( member ) ) {
	    MallDaifu daifu = new MallDaifu();
	    daifu.setOrderId( orderId );
	    daifu.setDfUserId( member.getId() );
	    MallDaifu df = mallDaifuDAO.selectBydf( daifu );
	    if ( CommonUtil.isNotEmpty( df ) ) {
		daiFuResult.setDaifuId( df.getId() );
	    }
	}
	if ( CommonUtil.isNotEmpty( mallOrder.getOrderType() ) ) {
	    daiFuResult.setOrderType( mallOrder.getOrderType() );
	}
	if ( CommonUtil.isNotEmpty( mallOrder.getGroupBuyId() ) ) {
	    daiFuResult.setActivityId( mallOrder.getGroupBuyId() );
	}
	return daiFuResult;
    }
}
