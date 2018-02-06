package com.gt.mall.service.quartz;

/**
 * 订单定时任务公共接口
 */
public interface MallQuartzOrderTaskService {

    /**
     * 关闭未支付订单
     */
    void newCloseOrderNoPay();

    /**
     * 订单完成赠送物品  每天早上8点扫描
     */
    void orderFinish();

    /*统计每天的收入金额*/
    void countIncomeNum();

    /**
     * 自动确认收货(物流签收后超过7天未确认收货，系统自动确认收货;)
     */
    void autoConfirmTakeDelivery();

    /**
     * 取消维权(维权 买家在卖家拒绝后7天内没有回应，则系统自动默认取消维权 )
     */
    void cancelReturn();

    /**
     * 自动退款给买家 (买家申请退款，卖家没有响应，7天后系统自动退款给买家)
     */
    void autoRefund();

    /**
     * 自动确认收货并退款至买家  (买家退货物流,若卖家超出10天不做操作，系统自动确认卖家
     * 收货并结算至买家账户)
     */
    void returnGoodsByRefund();

}
