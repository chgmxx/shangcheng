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
     *
     * @param auctId 拍卖d
     *
     * @return
     */
    Map< String,Object > selectOrderByAuctId( Integer auctId );

    /**
     * 分页查询
     *
     * @param params
     *
     * @return
     */
    public List< MallOrder > findByPage( Map< String,Object > params );

    /**
     * 获取条数
     *
     * @param params
     *
     * @return
     */
    public int count( Map< String,Object > params );

    /**
     * 添加卖家备注、修改订单金额
     *
     * @param params
     *
     * @return
     */
    public int upOrderNoOrRemark( Map< String,Object > params );

    /**
     * 支付成功修改状态
     *
     * @param params
     *
     * @return
     */
    public int upOrderByorderNo( Map< String,Object > params );

    /**
     * 查询订单信息
     *
     * @param params
     *
     * @return
     */
    public List< Map< String,Object > > selectOrder( Map< String,Object > params );

    /**
     * 查询订单详细信息
     *
     * @param params
     *
     * @return
     */
    public List< Map< String,Object > > selectOrderDetail( Map< String,Object > params );

    /**
     * 关闭未付款订单
     *
     * @Title: updateByNoMoney
     */
    public void updateByNoMoney( Map< String,Object > params );

    /**
     * 根据Id查询单个订单
     *
     * @param orderId
     *
     * @return
     */
    public MallOrder getOrderById( Integer orderId );

    /**
     * 手机端订单列表
     *
     * @param params
     *
     * @return
     */
    public List< MallOrder > mobileOrderList( Map< String,Object > params );

    /**
     * 统计我的订单
     *
     * @param params
     *
     * @return
     */
    int countMobileOrderList( Map< String,Object > params );

    /**
     * 根据订单号品信息
     *
     * @param orderNo
     *
     * @return
     */
    public MallOrder selectOrderByOrderNo( String orderNo );

    /**
     * 修改订单详情的状态
     *
     * @Title: updateOrderDetailStatus
     */
    public int updateOrderDetailStatus( MallOrderDetail orderDetail );

    /**
     * 根据订单详情id查询订单信息
     *
     * @Title: selectByDIdOrder
     */
    public Map< String,Object > selectByDIdOrder(
		    @Param( "detailId" ) Integer detailId );

    /**
     * 根据memberId查询pageId
     *
     * @param userId
     *
     * @return
     */
    public List< Map< String,Object > > selectPageIdByUserId( @Param( "userId" ) Integer userId );

    /**
     * 根据规格值Id查询规格Id
     *
     * @param params
     *
     * @return
     */
//    public Integer selectSpeBySpeValueId( Map< String,Object > params );

    /**
     * 修改订单号
     *
     * @param order
     *
     * @return
     */
    public int upOrderNoById( MallOrder order );

    /**
     * 根据主订单id查询其他子订单
     *
     * @param orderId
     *
     * @return
     */
    public List< Map< String,Object > > selectOrderPid( Integer orderId );

    /**
     * 根据订单详情id查询商品规格
     *
     * @Title: selectSpecByDetailId
     */
    public String selectSpecByDetailId( @Param( "detailId" ) Integer detailId );

    /**
     * 查出所有完成的订单
     *
     * @return
     */
    public List< MallOrder > selectOrderByFinish();

    /**
     * 计算有多少人参团
     *
     * @param params
     *
     * @return
     */
    public Map< String,Object > groupJoinPeopleNum( Map< String,Object > params );

    /**
     * 查询所有未支付的订单
     *
     * @param params
     *
     * @return
     */
    List< MallOrder > selectNoPayOrder( Map< String,Object > params );

    /**
     * 根据订单id查询订单详情
     *
     * @param orderId
     *
     * @return
     */
    List< Map< String,Object > > selectDetailByOrderId( Integer orderId );

    /**
     * 查询订单的子订单是否还有没有支付成功的订单
     *
     * @param orderId
     *
     * @return
     */
    int selectNoPayByPID( Integer orderId );

    /**
     * 根据订单表的id查询订单详情
     *
     * @param orderIds
     *
     * @return
     */
    List< MallOrderDetail > select_orderDetail( String orderIds );

    /**
     * 修改订单详情待评价状态
     */
    int uodAppraiseStatus( MallOrderDetail orderDetail );

    /**
     * 通过店铺id查询订单
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectOrderByShopId( Map< String,Object > params );

    /**
     * 导出订单
     *
     * @param params
     *
     * @return
     */
    public List< MallOrder > explodOrder( Map< String,Object > params );

    /**
     * 用户购买商品的数量（用于限购）
     *
     * @param params
     *
     * @return
     */
    public int selectMemberBuyProNum( Map< String,Object > params );

    /**
     * 根据订单id查询订单信息
     *
     * @param OrderId
     *
     * @return
     */
    public MallOrder getOrderByIdOrOne( Integer OrderId );

    /**
     * 修改订单的价格
     *
     * @param params
     *
     * @return
     */
    public int updateOrderMoney( Map< String,Object > params );

    /**
     * 查询会员的消费金额
     *
     * @param buyerUserId
     *
     * @return
     */
    Map< String,Object > selectSumSaleMoney( int buyerUserId );

    /**
     * 查询积分订单
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectIntegralOrder( Map< String,Object > params );

    /**
     * 查询充值失败和未充值的订单
     *
     * @return
     */
    List< Map< String,Object > > selectOrderFlow();

    /**
     * 查询退款订单的条数
     *
     * @param params
     *
     * @return
     */
    int countReturn( Map< String,Object > params );

    /**
     * 查询退款订单信息
     *
     * @param params
     *
     * @return
     */
    List< MallOrder > findReturnByPage( Map< String,Object > params );
}