package com.gt.mall.web.service.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrderReturn;

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
    public boolean returnEndOrder(Integer orderId,Integer orderDetailId) throws Exception;
}
