package com.gt.mall.service.quartz;

public interface MallQuartzService {

    /**
     * 退款流量充值
     */
    /*void returnFlow();*/

    /**
     * 对已结束未成团的订单进行退款
     */
    void endGroupReturn();


    /**
     * 预售商品开售提醒买家
     */
    void presaleStar();

}
