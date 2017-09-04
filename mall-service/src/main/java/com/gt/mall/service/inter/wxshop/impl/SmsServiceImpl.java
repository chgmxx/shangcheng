package com.gt.mall.service.inter.wxshop.impl;

import com.gt.mall.constant.Constants;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 发送短信实现类
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 16:36
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final String SMS_URL = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/";

    @Override
    public boolean sendSmsOld( OldApiSms oldApiSms ) {
	oldApiSms.setCompany( Constants.doMainName );
	oldApiSms.setModel( CommonUtil.toInteger( Constants.SMS_MODEL ) );
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( oldApiSms, SMS_URL + "sendSmsOld.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }
}
