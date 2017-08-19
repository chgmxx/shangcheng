package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.wx.pay.ApiEnterprisePayment;
import com.gt.mall.bean.wx.pay.EnterprisePaymentResult;
import com.gt.mall.bean.wx.pay.SubQrPayParams;
import com.gt.mall.bean.wx.pay.WxmemberPayRefund;

import java.util.Map;

/**
 * 支付接口（微信支付，小程序支付和钱包支付 以及支付宝）
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:05
 */
public interface PayService {

    /**
     * 支付接口（微信，小程序，钱包和支付宝  支付接口）
     * @param payParams 支付接口参数
     * @return 是否成功
     */
    Map<String,Object> payapi(SubQrPayParams payParams);

    /**
     * 退款接口（微信，小程序，钱包和支付宝退款接口）
     * @param refund 退款参数
     * @return 是否成功
     */
    Map<String,Object> wxmemberPayRefund(WxmemberPayRefund  refund);

    /**
     * 商家提现
     * @param payment 提现参数
     * @return 返回值
     */
    EnterprisePaymentResult enterprisePayment(ApiEnterprisePayment payment);


}
