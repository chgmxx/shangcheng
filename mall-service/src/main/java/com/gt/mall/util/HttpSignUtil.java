package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用会员接口的工具类
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 15:50
 */
public class HttpSignUtil {

    private static Logger logger = LoggerFactory.getLogger( HttpSignUtil.class );

    private static JSONObject SignHttpJson( Object obj, String url, int... types ) {
	try {
	    String result = null;
	    if ( types[0] == 0 ) {//会员
		String signKey = PropertiesUtil.getMemberSignKey();
		url = PropertiesUtil.getMemberDomain() + url;
		result = SignHttpUtils.postByHttp( url, obj, signKey );
	    } else {
		//门店
		String signKey = PropertiesUtil.getWxmpSignKey();
		url = PropertiesUtil.getWxmpDomain() + url;
		result = SignHttpUtils.WxmppostByHttp( url, obj, signKey );
	    }
	    logger.info( "result:" + result );

	    if ( CommonUtil.isNotEmpty( result ) ) {
		return JSONObject.parseObject( result );
	    }
	} catch ( SignException e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 查询 接口
     *
     * @param params 参数
     * @param url    地址
     * @param type   请求类型  0 会员  1 门店  2商家
     *
     * @return 返回
     */
    public static String SignHttpSelect( Object params, String url, int... type ) {
	JSONObject resultObj = SignHttpJson( params, url, type );

	if ( resultObj.getInteger( "code" ) == 0 ) {
	    logger.info( "data = " + resultObj.getString( "data" ) );
	    return resultObj.getString( "data" );
	} else {
	    logger.info( "调用会员接口异常：" + resultObj.getString( "msg" ) );
	}
	return null;
    }

    /**
     * 新增和修改接口
     *
     * @param params 参数
     * @param url    地址
     * @param type   请求类型  0 会员  1 门店  2商家
     *
     * @return 返回
     */
    public static Map< String,Object > SignHttpInsertOrUpdate( Object params, String url, int... type ) {
	Map< String,Object > resultMap = new HashMap<>();
	JSONObject resultObj = SignHttpJson( params, url );
	logger.info( "调用接口返回值 = " + resultObj.getString( "data" ) );
	int code = resultObj.getInteger( "code" );
	if ( code == 0 ) {
	    resultMap.put( "code", 1 );//成功
	} else if ( code == 1 ) {//失败
	    resultMap.put( "code", -1 );
	} else {
	    resultMap.put( "code", code );
	}
	if ( resultObj.containsKey( "msg" ) ) {
	    resultMap.put( "errorMsg", resultObj.getString( "msg" ) );
	}
	return resultMap;
    }

}
