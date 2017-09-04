package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.entity.order.MallOrder;

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
    MallAllEntity calculateOrder( Map< String,Object > params, Member member );

    /**
     * 计算值
     */
    Map< String,Object > getCalculateData( MallAllEntity allEntity );
}
