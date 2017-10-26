package com.gt.mall.service.web.applet;

import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;

import java.util.Map;

/**
 * <p>
 * 小程序商城订单
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallNewOrderAppletService extends BaseService< MallAppletImage > {

    /**
     * 进入提交订单的页面
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > toSubmitOrder( Map< String,Object > params );

    /**
     * 计算订单的优惠信息（积分抵扣、粉币抵扣、优惠券、商家联盟折扣）  1.1新增方法
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > calculationPreferential( Map< String,Object > params );

    /**
     * 提交订单的页面
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > submitOrder( Map< String,Object > params );

    /**
     * 计算订单的优惠信息（积分抵扣、粉币抵扣、优惠券、商家联盟折扣） 调用会员接口
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > newCalculationPreferential( Map< String,Object > params );

    /**
     * 验证订单信息
     */
    public Map< String,Object > validateOrder( MallOrder order, Member member );

    /**
     * 验证订单详情信息
     */
    public Map< String,Object > validateOrderDetail( MallOrder order, MallOrderDetail detail );
}
