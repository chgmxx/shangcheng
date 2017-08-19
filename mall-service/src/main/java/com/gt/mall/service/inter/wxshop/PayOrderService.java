package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.wx.pay.WxPayOrder;

/**
 * 微信订单接口
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:01
 */
public interface PayOrderService {
    /**
     * 根据订单号查询微信支付订单信息
     * @param orderNo 订单号
     * @return 支付订单信息
     */
    WxPayOrder selectWxOrdByOutTradeNo(String orderNo);
}
