package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import org.springframework.stereotype.Service;

/**
 * 微信支付接口实现类
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:02
 */
@Service
public class PayOrderServiceImpl implements PayOrderService {
    private static final String PAY_ORDER_URL = "/8A5DA52E/payorder/6F6D9AD2/79B4DE7C/";

    @Override
    public WxPayOrder selectWxOrdByOutTradeNo( String orderNo ) {
	String result = HttpSignUtil.signHttpSelect( orderNo, PAY_ORDER_URL + "selectWxOrdByOutTradeNo.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPayOrder.class );
	}
	return null;
    }
}
