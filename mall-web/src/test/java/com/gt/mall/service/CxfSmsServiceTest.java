package com.gt.mall.service;

import com.gt.mall.BasicTest;
import com.gt.mall.bean.param.sms.OldApiSms;
import com.gt.mall.cxf.service.SmsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/26 0026
 * Time : 17:47
 */
public class CxfSmsServiceTest extends BasicTest {


   @Autowired
    private SmsService smsService;

   @Test
    public void test(){
       OldApiSms oldApiSms = new OldApiSms();
       oldApiSms.setModel( 0 );
       oldApiSms.setMobiles( "15017934717" );
       oldApiSms.setContent( "正在测试，哈哈哈" );
       oldApiSms.setCompany( "谷通有限公司" );
       oldApiSms.setBusId( 42 );
       smsService.sendMsg( oldApiSms );
   }
}
