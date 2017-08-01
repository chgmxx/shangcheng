package com.gt.mall.cxf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.param.sms.OldApiSms;
import com.gt.mall.cxf.service.SmsService;
import com.gt.mall.util.CxfFactoryBeanUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.webservice.client.WxmpApiSerivce;
import com.gt.webservice.entity.param.BaseParam;
import com.gt.webservice.util.ParamsSignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 短信业务实现类（调用CXF接口）
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:19
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static Logger logger = LoggerFactory.getLogger( SmsServiceImpl.class );

    @Override
    public Boolean sendMsg( OldApiSms oldApiSms ) throws Exception {
//        System.out.println("PropertiesUtil.getShopUrl() = " + PropertiesUtil.getShopUrl());
	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) CxfFactoryBeanUtil.crateCxfFactoryBean( WxmpApiSerivce.class, PropertiesUtil.getShopUrl() );

	BaseParam<OldApiSms> baseParam = new BaseParam<>();
	baseParam.setAction( "sendSmsOld" );
	baseParam.setReqdata( oldApiSms );
	baseParam.setNonceStr(ParamsSignUtil.CreateNoncestr());
	baseParam.setTimestamp(ParamsSignUtil.create_timestamp());
	String sign = ParamsSignUtil.sign( baseParam,JSONObject.toJSONString( oldApiSms ) );
	baseParam.setRequestToken( sign );

	logger.info( "调用发送短信的接口传参 :"+JSONObject.toJSONString( baseParam ) );
	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	logger.info( "发送短信的接口 sendSmsOld 返回的接口：" + json );

	return CxfFactoryBeanUtil.getBooleanResultByJson( json );
    }
}
