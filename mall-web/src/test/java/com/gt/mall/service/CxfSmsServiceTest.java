package com.gt.mall.service;

import com.gt.mall.BasicTest;
import com.gt.mall.cxf.service.SmsService;
import com.gt.mall.util.MyConfigUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

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
    public void test() throws Exception {


//       OldApiSms oldApiSms = new OldApiSms();
//       oldApiSms.setModel( 0 );
//       oldApiSms.setMobiles( "15017934717" );
//       oldApiSms.setContent( "正在测试，哈哈哈" );
//       oldApiSms.setCompany( "谷通有限公司" );
//       oldApiSms.setBusId( 42 );
//       smsService.sendMsg( oldApiSms );

//      System.out.println("redisHost = " + JedisUtil.getRedisHost());
//      System.out.println("redisUser = " + JedisUtil.getRedisPort());
//      System.out.println("redisPass = " + JedisUtil.getRedisPassword());

       Map< String,Integer > params = new HashMap< String,Integer >();
       params.put( "total", 36002 );
       params.put( "daysCount", 211 );
//       JedisUtil.set( "busCount", JSONObject.toJSONString( params ));

//       System.out.println("redis = " + JedisUtil.get( "busCount" ));

      System.out.println("222 = " + MyConfigUtil.getHomeUrl());

   }


}
