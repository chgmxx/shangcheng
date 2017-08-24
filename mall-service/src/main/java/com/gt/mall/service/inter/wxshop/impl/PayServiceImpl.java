package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wx.pay.ApiEnterprisePayment;
import com.gt.mall.bean.wx.pay.EnterprisePaymentResult;
import com.gt.mall.bean.wx.pay.SubQrPayParams;
import com.gt.mall.bean.wx.pay.WxmemberPayRefund;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import com.gt.mall.util.WxHttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付接口（微信支付，小程序支付和钱包支付 以及支付宝）
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:06
 */
@Service
public class PayServiceImpl implements PayService {

    private static final String PAY_URL = "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/";

    @Override
    public Map< String,Object > payapi( SubQrPayParams payParams ) {
	return HttpSignUtil.SignHttpInsertOrUpdate( payParams, PAY_URL + "payapi.do", 2 );
    }

    @Override
    public Map< String,Object > wxmemberPayRefund( WxmemberPayRefund refund ) {
	return HttpSignUtil.SignHttpInsertOrUpdate( refund, PAY_URL + "wxmemberPayRefund.do", 2 );
    }

    @Override
    public EnterprisePaymentResult enterprisePayment( ApiEnterprisePayment payment ) {
	Map resultMap = WxHttpSignUtil.SignHttpInsertOrUpdate( payment, PAY_URL + "wxmemberPayRefund.do", 2 );
	if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == 1 ) {
	    if ( CommonUtil.isEmpty( resultMap.get( "data" ) ) ) {return null;}
	    return JSONObject.toJavaObject( JSONObject.parseObject( resultMap.get( "data" ).toString() ), EnterprisePaymentResult.class );
	}
	return null;
    }

}
