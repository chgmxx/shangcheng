package com.gt.mall.service.web.order;

import com.gt.mall.entity.order.MallOrder;

import java.util.Map;

/**
 * 订单接口
 */
public interface QuartzOrderService {

    void closeOrderNoPay( String key );

    void newCloseOrderNoPay( String key );

    /**
     * 添加购买日志（用在支付有礼）
     */
    void insertPayLog( MallOrder order );

    /**
     * 预售商品，订单完成赠送虚拟物品
     *
     * @param map
     */
    void presaleOrderGive( Map< String,Object > map );

    void closeOrderByDaoDian();

    /**
     * 发放销售佣金和积分
     *
     * @param dMap
     * @param map
     */
    void sellerIncome( Map< String,Object > dMap, Map< String,Object > map );

    /**
     * 订单是否还能退款
     *
     * @return
     */
    boolean isOrderReturn( Map< String,Object > map, Map< String,Object > dMap );

    /**
     * 充值失败，回滚订单状态
     *
     * @param params orderCode   订单号
     *
     * @return code  1 修改成功 -1 修改失败   ，  msg 修改失败信息
     */
    boolean rollbackOrderByFlow( Map< String,Object > params );

}
