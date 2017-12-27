package com.gt.mall.service.quartz;

/**
 * 定时任务公共接口
 */
public interface MallQuartzNewService {

    /**
     * 每天定时晚上一点统计商城信息
     */
    void mallcount();

    /**
     * 订单完成赠送物品  每天早上8点扫描
     */
    void orderFinish();

    /**
     * 关闭30分钟内未支付的订单
     */
    void closeOrderNoPay();

    /**
     * 扫描已结束秒杀
     */
    void endSeckill();

    /******************************************/

   /*统计每天营业额*/
    void countIncomeNum();

    /*统计每天页面访问数量*/
    void countPageVisitorNum();

    /**
     * 关闭未付款认单 (拍下未付款订单规定时间内买家不付款，自动取消订单)
     */
    void closeNoPayOrder();

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

    /*
    * 支付成功，但回调失败的订单修改
    * */
    void orderCallback();

    /**
     * 调用会员退款接口
     */
    void memberRefund();
}
