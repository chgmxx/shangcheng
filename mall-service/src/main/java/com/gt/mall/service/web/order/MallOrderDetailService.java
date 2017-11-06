package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrderDetail;

import java.util.List;

/**
 * <p>
 * 商城订单详情表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderDetailService extends BaseService< MallOrderDetail > {

    /**
     * 根据订单id查询订单详情信息
     *
     * @param orderId 订单id
     *
     * @return 订单详情信息
     */
    List< MallOrderDetail > getOrderDetailList( Integer orderId );
}
