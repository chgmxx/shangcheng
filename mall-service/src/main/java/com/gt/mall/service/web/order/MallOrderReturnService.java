package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.result.phone.order.returns.PhoneReturnProductResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnResult;
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
     * 查询退款信息
     *
     * @param orderDetailId 订单详情id
     * @param returnId      退款id
     *
     * @return 退款信息
     */
    PhoneReturnProductResult getReturn( Integer orderDetailId, Integer returnId );

    /**
     * 查询退款详情
     *
     * @param returnId 退款id
     *
     * @return
     */
    PhoneReturnResult returnDetail( Integer returnId );

}
