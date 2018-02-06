package com.gt.mall.service.quartz;

public interface MallQuartzService {

    /**
     * 对已结束未成团的订单进行退款
     */
    void activityRefund();

    /**
     * 预售商品开售提醒买家
     */
    void presaleStar();

}
