package com.gt.mall.service.web.order;

import com.gt.entityBo.MallAllEntity;
import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.param.phone.order.PhoneToOrderDTO;
import com.gt.mall.result.phone.order.PhoneToOrderResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderNewService extends BaseService< MallOrder > {

    /**
     * 计算订单
     */
    MallAllEntity calculateOrder( Map< String,Object > params, Member member, List< MallOrder > orderList );

    /**
     * 计算值
     */
    Map< String,Object > getCalculateData( MallAllEntity allEntity );

    /**
     * 提交订单
     */
    Map< String,Object > submitOrder( Map< String,Object > params, Member member, Integer browser ) throws Exception;

    /**
     * 微信支付
     *
     * @param orderAllMoney 订单总额
     * @param orderNo       订单号
     * @param order         订单
     *
     * @return 支付地址
     */
    String wxPayWay( double orderAllMoney, String orderNo, MallOrder order, int orderPayWay ) throws Exception;

    /**
     * 进入提交订单页面
     */
    PhoneToOrderResult toOrder( PhoneToOrderDTO params, Member member );
}
