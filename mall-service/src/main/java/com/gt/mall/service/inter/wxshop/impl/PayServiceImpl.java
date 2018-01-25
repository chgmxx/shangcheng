package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.util.entity.param.pay.ApiEnterprisePayment;
import com.gt.util.entity.param.pay.PayWay;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.result.pay.EnterprisePaymentResult;
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
    public Map< String,Object > payapi( SubQrPayParams payParams ) throws Exception {
	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( payParams ) );
	return HttpSignUtil.signHttpInsertOrUpdate( params, PAY_URL + "payapi.do", 2 );
    }

    @Override
    public Map< String,Object > wxmemberPayRefund( WxmemberPayRefund refund ) {
	RequestUtils< WxmemberPayRefund > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( refund );
	return HttpSignUtil.signHttpInsertOrUpdate( requestUtils, PAY_URL + "wxmemberPayRefund.do", 2,1 );
    }

    @Override
    public EnterprisePaymentResult enterprisePayment( ApiEnterprisePayment payment ) {
	RequestUtils< ApiEnterprisePayment > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( payment );
	Map resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, PAY_URL + "enterprisePayment.do", 2 );
	if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == 1 ) {
	    if ( CommonUtil.isEmpty( resultMap.get( "data" ) ) ) {return null;}
	    return JSONObject.toJavaObject( JSONObject.parseObject( resultMap.get( "data" ).toString() ), EnterprisePaymentResult.class );
	}
	return null;
    }

    @Override
    public PayWay getPayWay( Integer busId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busId );
	Map resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, PAY_URL + "getPayWay.do", 2 );
	if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == 1 ) {
	    if ( CommonUtil.isEmpty( resultMap.get( "data" ) ) ) {return null;}
	    return JSONObject.toJavaObject( JSONObject.parseObject( resultMap.get( "data" ).toString() ), PayWay.class );
	}
	return null;
    }

}
