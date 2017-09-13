package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderDAO extends BaseMapper< MallOrder > {

    /**
     * 获取拍卖商品的 订单信息
     */
    Map< String,Object > selectOrderByAuctId( Integer auctId );

    /**
     * 分页查询
     */
    List< MallOrder > findByPage( Map< String,Object > params );

    /**
     * 获取条数
     */
    int count( Map< String,Object > params );

    /**
     * 添加卖家备注、修改订单金额
     */
    int upOrderNoOrRemark( Map< String,Object > params );

    /**
     * 支付成功修改状态
     */
    int upOrderByorderNo( Map< String,Object > params );


    /**
     * 查询订单详细信息
     */
    List< Map< String,Object > > selectOrderDetail( Map< String,Object > params );

    /**
     * 关闭未付款订单
     */
    void updateByNoMoney( Map< String,Object > params );

    /**
     * 根据Id查询单个订单
     */
    MallOrder getOrderById( Integer orderId );

    /**
     * 根据父类id查询订单信息
     *
     * @param orderId 订单id
     *
     * @return 订单信息
     */
    List< MallOrder > getOrderByOrderPId( int orderId );

    /**
     * 手机端订单列表
     */
    List< MallOrder > mobileOrderList( Map< String,Object > params );

    /**
     * 手机端订单列表
     */
    List< MallOrder > mobileMyOrderList( Map< String,Object > params );

    /**
     * 统计我的订单
     */
    int countMobileOrderList( Map< String,Object > params );

    /**
     * 根据订单号品信息
     */
    MallOrder selectOrderByOrderNo( String orderNo );

    /**
     * 根据订单详情id查询订单信息
     */
    Map< String,Object > selectByDIdOrder( @Param( "detailId" ) Integer detailId );

    /**
     * 修改订单号
     */
    int upOrderNoById( MallOrder order );

    /**
     * 根据主订单id查询其他子订单
     */
    List< Map< String,Object > > selectOrderPid( Integer orderId );

    /**
     * 根据订单详情id查询商品规格
     */
    String selectSpecByDetailId( @Param( "detailId" ) Integer detailId );

    /**
     * 查出所有完成的订单
     */
    List< MallOrder > selectOrderByFinish();

    /**
     * 计算有多少人参团
     */
    Map< String,Object > groupJoinPeopleNum( Map< String,Object > params );

    /**
     * 查询所有未支付的订单
     */
    List< MallOrder > selectNoPayOrder( Map< String,Object > params );

    /**
     * 根据订单id查询订单详情
     */
    List< Map< String,Object > > selectDetailByOrderId( Integer orderId );

    /**
     * 查询订单的子订单是否还有没有支付成功的订单
     */
    int selectNoPayByPID( Integer orderId );

    /**
     * 根据订单表的id查询订单详情
     */
    List< MallOrderDetail > select_orderDetail( String orderIds );

    /**
     * 修改订单详情待评价状态
     */
    int uodAppraiseStatus( MallOrderDetail orderDetail );

    /**
     * 通过店铺id查询订单
     */
    List< Map< String,Object > > selectOrderByShopId( Map< String,Object > params );

    /**
     * 导出订单
     */
    List< MallOrder > explodOrder( Map< String,Object > params );

    /**
     * 用户购买商品的数量（用于限购）
     */
    int selectMemberBuyProNum( Map< String,Object > params );

    /**
     * 根据订单id查询订单信息
     */
    MallOrder getOrderByIdOrOne( Integer OrderId );

    /**
     * 修改订单的价格
     */
    int updateOrderMoney( Map< String,Object > params );

    /**
     * 查询会员的消费金额
     */
    Map< String,Object > selectSumSaleMoney( int buyerUserId );

    /**
     * 查询积分订单
     */
    List< Map< String,Object > > selectIntegralOrder( Map< String,Object > params );

    /**
     * 查询充值失败和未充值的订单
     */
    List< Map< String,Object > > selectOrderFlow();

    /**
     * 查询退款订单的条数
     */
    int countReturn( Map< String,Object > params );

    /**
     * 查询退款订单信息
     */
    List< MallOrder > findReturnByPage( Map< String,Object > params );

    /**
     * 根据订单id查询订单信息
     *
     * @param id 订单id
     *
     */
    Map< String,Object > selectMapById( int id );
}