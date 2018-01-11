package com.gt.mall.service.web.seller;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.seller.MallSellerOrder;
import com.gt.mall.utils.PageUtil;

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
    PageUtil selectOrderByClientId(Map<String, Object> params);

    /**
     * 查询销售员收益积分排行榜
     *
     *
     * @return 销售员收益积分排行榜
     */
    PageUtil selectSellerByBusUserId( Map< String,Object > params );
}
