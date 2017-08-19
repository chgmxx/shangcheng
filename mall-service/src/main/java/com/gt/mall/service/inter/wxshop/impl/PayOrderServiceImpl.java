package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wx.pay.WxPayOrder;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.WxHttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付接口实现类
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:02
 */
@Service
public class PayOrderServiceImpl implements PayOrderService {
    private static final String PAY_ORDER_URL = "/8A5DA52E/payorder/79B4DE7C/";

    @Override
    public WxPayOrder selectWxOrdByOutTradeNo( String orderNo ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", orderNo );
	String result = WxHttpSignUtil.SignHttpSelect( params, PAY_ORDER_URL + "selectWxOrdByOutTradeNo.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPayOrder.class );
	}
	return null;
    }
}
