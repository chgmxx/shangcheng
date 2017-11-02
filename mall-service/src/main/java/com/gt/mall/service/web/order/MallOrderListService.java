package com.gt.mall.service.web.order;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.order.PhoneOrderListDTO;
import com.gt.mall.result.phone.order.detail.PhoneOrderResult;
import com.gt.mall.result.phone.order.list.PhoneOrderListResult;

/**
 * <p>
 * 商城订单列表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderListService extends BaseService< MallOrder > {

    /**
     * 进入订单列表
     */
    PhoneOrderListResult orderList( PhoneOrderListDTO params, PhoneLoginDTO loginDTO, Member member );

    /**
     * 获取订单详情信息
     *
     * @param orderId 订单id
     * @param busId   商家id
     *
     * @return 订单详情信息
     */
    PhoneOrderResult getOrderDetail( Integer orderId, Integer busId );

}
