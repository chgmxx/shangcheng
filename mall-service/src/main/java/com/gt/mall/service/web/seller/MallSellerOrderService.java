package com.gt.mall.service.web.seller;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.seller.MallSellerOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员订单中间表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerOrderService extends BaseService< MallSellerOrder > {

    /**
     * 查询销售员的订单信息
     * @param params 参数
     * @return 订单信息
     */
    Map<String, Object> selectOrderByMemberId(Map<String, Object> params);

    /**
     * 查询客户的订单
     * @param params 参数
     * @return 订单信息
     */
    List<Map<String, Object> > selectOrderByClientId(Map<String, Object> params);
}
