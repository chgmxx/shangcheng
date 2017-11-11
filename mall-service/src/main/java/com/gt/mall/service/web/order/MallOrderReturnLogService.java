package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrderReturnLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单维权日志表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
public interface MallOrderReturnLogService extends BaseService< MallOrderReturnLog > {

    /**
     * 查询维权日志信息
     */
    List< Map< String,Object > > selectReturnLogList( Integer returnId );

    /**
     * 1买家发起退款申请
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     * @param way      1,我要退款，但不退货  2,我要退款退货
     *
     * @return
     */
    boolean addBuyerRetutnApply( Integer returnId, Integer userId, Integer way );

    boolean againRetutnApply( Integer returnId, Integer userId, Integer way );

    /**
     * 2等待卖家处理  系统消息
     *
     * @param returnId     维权订单ID
     * @param deadlineTime 截止时间
     *
     * @return
     */
    boolean waitSellerDispose( Integer returnId, Date deadlineTime );

    /**
     * 3卖家同意申请
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean sellerAgreeApply( Integer returnId, Integer userId );

    /**
     * 4买家已退货
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean buyerReturnGoods( Integer returnId, Integer userId );

    /**
     * 5卖家已收到货，并退款
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean sellerRefund( Integer returnId, Integer userId );

    /**
     * 6退款成功 系统消息
     *
     * @param returnId    维权订单ID
     * @param returnPrice 退款金额
     *
     * @return
     */
    boolean refundSuccess( Integer returnId, String payWay, String returnPrice );

    /**
     * 7卖家拒绝退款申请
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean sellerRefuseRefund( Integer returnId, Integer userId );

    /**
     * 8申请维权介入
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     * @param type     谁申请的 0买家 1卖家
     *
     * @return
     */
    boolean platformIntervention( Integer returnId, Integer userId, Integer type );

    /**
     * 买家撤销退款
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean buyerRevokeRefund( Integer returnId, Integer userId );

    /**
     * 买家修改物流信息
     *
     * @param returnId 维权订单ID
     * @param userId   用户或商家ID
     *
     * @return
     */
    boolean buyerUpdateLogistics( Integer returnId, Integer userId );

}
