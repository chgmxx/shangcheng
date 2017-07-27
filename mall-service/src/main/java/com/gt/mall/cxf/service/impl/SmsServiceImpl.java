package com.gt.mall.cxf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.param.BaseParam;
import com.gt.mall.bean.param.sms.OldApiSms;
import com.gt.mall.cxf.service.SmsService;
import com.gt.mall.util.CxfFactoryBeanUtil;
import com.gt.mall.util.MyConfigUtil;
import com.gt.webservice.service.WxmpApiSerivce;
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
	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) CxfFactoryBeanUtil.crateCxfFactoryBean( "WxmpApiSerivce", MyConfigUtil.getShopUrl() );

	BaseParam baseParam = new BaseParam();
	baseParam.setAction( "sendSmsOld" );
	baseParam.setRequestToken( MyConfigUtil.getWxmpToken() );
	baseParam.setReqdata( oldApiSms );

	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	logger.info( "发送短信的接口 sendMsg 返回的接口：" + json );

	return CxfFactoryBeanUtil.getBooleanResultByJson( json );
    }
}
