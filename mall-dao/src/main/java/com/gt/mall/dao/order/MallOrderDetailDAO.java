package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrderDetail;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取当天所有的商品id
     *
     * @param startTime
     * @param endTime
     *
     * @return
     */
    List< Map< String,Object > > selectTodayProduct( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime );

    /**
     * 获取单商品微信支付的数量和金额
     *
     * @param startTime
     * @param endTime
     *
     * @return
     */
    List< Map< String,Object > > selectProductMoneyByWay( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime, @Param( "productId" ) Integer productId,
        @Param( "payWay" ) Integer payWay );

}
