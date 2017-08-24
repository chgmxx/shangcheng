package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.sign.SignBean;
import com.gt.api.exception.SignException;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.api.util.sign.SignUtils;
import com.gt.mall.bean.RequestUtils;
import com.gt.mall.bean.wx.OldApiSms;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:10
 */
public class WxTest {

    private static Map SignHttpByJSON( Object params, String url ) {
	url = "http://wxmp.yifriend.net:13882/" + url;
	String signKey = "WXMP2017";
	try {

	   /* String result = SignHttpUtils.WxmppostByHttp( url, params, signKey );
	    System.out.println("result = " + result);*/

	    RequestUtils requestUtils = new RequestUtils();
	    SignBean sign = SignUtils.sign( signKey, JSONObject.toJSONString( params ) );
	    requestUtils.setReqdata( JSONObject.toJSONString( params ) );
	   /* requestUtils.setSignKey( signKey );
	    requestUtils.setSign( JSONObject.toJSONString( sign ) );*/

	    System.out.println( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( requestUtils ) + "---签名key：" + signKey );
	    Map map = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, Map.class ,signKey);

	    System.out.println( "接口返回result:" + map );

	    if ( CommonUtil.isNotEmpty( map ) ) {
		return map;
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    private static JSONObject SignHttpJson( Object obj, String url, int... types ) {
	try {
	    String result = null;
	    //门店
	    url = "http://wxmp.yifriend.net:13882/" + url;
	    String signKey = "WXMP2017";
	    System.out.println( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( obj ) + "---签名key：" + signKey );
	    result = SignHttpUtils.WxmppostByHttp( url, obj, signKey );
	    System.out.println( "接口返回result:" + result );
	    if ( CommonUtil.isNotEmpty( result ) ) {
		return JSONObject.parseObject( result );
	    }
	} catch ( SignException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * post请求带请求参数
     */
    public static void main( String[] args ) {

	OldApiSms sms = new OldApiSms();
	sms.setBusId( 42 );
	sms.setContent( "hahah" );
	sms.setCompany( "5" );
	sms.setMobiles( "15017934717" );
	sms.setModel( 5 );
//	SignHttpByJSON( sms, "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do" );

	SignHttpJson( sms, "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do" );
    }

}
