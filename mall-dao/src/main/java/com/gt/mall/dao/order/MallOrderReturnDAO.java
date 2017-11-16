package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrderReturn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品退货 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderReturnDAO extends BaseMapper< MallOrderReturn > {

    /**
     * 根据订单id和订单详情id查询退款信息
     * @param orderReturn
     * @return
     */
    MallOrderReturn selectByOrderDetailId(MallOrderReturn orderReturn);

    /**
     * 根据退单号，查询退单信息
     * @param returnNo
     * @return
     */
    MallOrderReturn selectByReturnNo(String returnNo);

    /**
     * 根据订单id查询退单信息
     * @param params
     * @return
     */
    List<MallOrderReturn> selectByDetailIds(Map<String, Object> params);

    /**
     * 退款的所有商品
     *
     * @param startTime
     * @param endTime
     *
     * @return
     */
    List< Map< String,Object > > selectAllReturnProduct( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime );

    /**
     * 获取单商品微信支付退款的数量和金额
     *
     * @param startTime
     * @param endTime
     * @param productId
     * @param payWay
     *
     * @return
     */
    List< Map< String,Object > > selectProductMoneyByWay( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime, @Param( "productId" ) Integer productId,
                    @Param( "payWay" ) Integer payWay );
}