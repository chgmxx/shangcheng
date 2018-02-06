package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员订单中间表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerOrderDAO extends BaseMapper< MallSellerOrder > {

    /**
     * 查询客户订单的数量
     *
     * @param params
     *
     * @return
     */
    int selectCountClientOrder( Map< String,Object > params );

    /**
     * 查询销售员的订单信息
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > selectOrderByMemberId( Map< String,Object > params );

    /**
     * 统计客户的订单
     *
     * @param params
     *
     * @return
     */
    int countOrderByClient( Map< String,Object > params );

    /**
     * 查询客户的订单
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectOrderByClientId( Map< String,Object > params );

    /**
     * 查询销售订单信息
     *
     * @param order
     *
     * @return
     */
    MallSellerOrder selectBySellerOrder( MallSellerOrder order );

    /**
     * 查询商家销售员排名
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectSalePricerByUserId( Map< String,Object > params );
}
