package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:10
 */
public class Test {

    private static String SignHttpByJSON( Map< String,Object > params, String url ) {
	url = "http://113.106.202.53:13885/" + url;
	String signKey = "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM";
	try {
	    String result = SignHttpUtils.postByHttp( url, params, signKey );
	    System.out.println( "result:" + result );

	    if ( CommonUtil.isNotEmpty( result ) ) {
		JSONObject resultObj = JSONObject.parseObject( result );
		if ( resultObj.getInteger( "code" ) == 0 ) {
		    System.out.println( "data = " + resultObj.getString( "data" ) );
		    return resultObj.getString( "data" );
		} else {
		    System.out.println( "调用会员接口异常：" + resultObj.getString( "msg" ) );
		}
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
	System.out.println( "1111 = " );
	//	String url = "http://113.106.202.53:13885/memberAPI/member/findByMemberId";
	Map< String,Object > map = new HashMap<>();
	map.put( "memberId", 1225352 );
	map.put( "shopId", 17);
	String result = SignHttpByJSON( map, "/memberAPI/member/findMemberCardByMemberId" );

//	map.put( "memberId", 1225352 );
//	String result = SignHttpByJSON( map, "/memberAPI/member/findByMemberId" );

    }
}
