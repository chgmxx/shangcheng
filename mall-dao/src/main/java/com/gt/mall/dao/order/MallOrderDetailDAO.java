package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单详情表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderDetailDAO extends BaseMapper< MallOrderDetail > {

    /**
     * 统计购买商品的数量
     *
     * @param params 商品id
     *
     * @return 商品的数量
     */
    int countDetailByProductId( Map< String,Object > params );

    /**
     * 根据订单id查询订单详情
     */
    List< MallOrderDetail > selectByOrderId( Integer orderId );

    /**
     * 统计订单下购买了几件商品
     */
    int countByOrderId( Map< String,Object > params );

    /**
     * 分页订单详情
     */
    List< MallOrderDetail > selectPageByOrderId( Map< String,Object > params );

    /**
     * 根据订单id查询订单详情
     */
    List< MallOrderDetail > selectDetailByOrderIds( Map< String,Object > params );

    /**
     * 添加订单详情
     */
    public int insertOrderDetail( List< MallOrderDetail > list );

}