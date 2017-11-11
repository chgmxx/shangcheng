package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.result.phone.order.returns.PhoneReturnProductResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWayResult;

import java.util.List;

/**
 * <p>
 * 商品退货 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderReturnService extends BaseService< MallOrderReturn > {

    /**
     * 系统退款（不是买家申请的）
     */
    boolean returnEndOrder( Integer orderId, Integer orderDetailId ) throws Exception;

    /**
     * 获取退款方式
     *
     * @param order 订单
     *
     * @return 退款方式
     */
    List< PhoneReturnWayResult > getReturnWayList( MallOrder order );

    /**
     * 获取退款商品
     *
     * @param order  订单
     * @param detail 订单详情
     *
     * @return 商品信息
     */
    PhoneReturnProductResult getReturnProduct( MallOrder order, MallOrderDetail detail );

    /**
     * 查询退款信息
     *
     * @param orderDetailId 订单详情id
     * @param returnId      退款id
     *
     * @return 退款信息
     */
    PhoneReturnProductResult getReturn( Integer orderDetailId, Integer returnId );

    /**
     * 根据订单id和订单详情id查询退款信息
     * @param orderId 订单id
     * @param orderDetailId 订单详情id
     * @return 退款信息
     */
    MallOrderReturn selectByOrderDetailId(Integer orderId,Integer orderDetailId);
}
