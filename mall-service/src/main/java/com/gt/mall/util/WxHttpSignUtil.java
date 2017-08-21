package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.sign.SignBean;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.sign.SignUtils;
import com.gt.mall.bean.RequestUtils;
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
public class WxHttpSignUtil {

    private static Logger logger = LoggerFactory.getLogger( WxHttpSignUtil.class );

    private static Map SignHttpJson( Object obj, String url ) {
	try {
	    String signKey = PropertiesUtil.getWxmpSignKey();
	    url = PropertiesUtil.getWxmpDomain() + url;
	    //	    String result = SignHttpUtils.postByHttp( url, obj, signKey );

	    RequestUtils requestUtils = new RequestUtils();
	    SignBean sign = SignUtils.sign( signKey, JSONObject.toJSONString( obj ) );
	    requestUtils.setReqdata( obj );
	    requestUtils.setSignKey( signKey );
	    requestUtils.setSign( JSONObject.toJSONString( sign ) );

	    logger.info( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( requestUtils ) + "---签名key：" + signKey );
	    Map map = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, Map.class );

	    logger.info( "接口返回result:" + map );

	    if ( CommonUtil.isNotEmpty( map ) ) {
		return map;
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 查询 接口
     *
     * @param params 参数
     * @param url    地址
     *
     * @return 返回
     */
    public static String SignHttpSelect( Object params, String url, int type ) {

	Map resultMap = SignHttpJson( params, url );

	if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == 0 ) {
	    logger.info( "data = " + resultMap.get( "data" ) );
	    return resultMap.get( "data" ).toString();
	} else {
	    logger.info( "调用接口异常：" + resultMap.get( "msg" ) );
	}
	return null;
    }

    /**
     * 新增和修改接口
     *
     * @param params 参数
     * @param url    地址
     *
     * @return 返回
     */
    public static Map< String,Object > SignHttpInsertOrUpdate( Object params, String url, int type ) {
	Map< String,Object > result = new HashMap<>();

	Map resultMap = SignHttpJson( params, url );

	int code = CommonUtil.toInteger( resultMap.get( "code" ) );
	if ( code == 0 ) {
	    result.put( "code", 1 );//成功
	} else {//失败
	    result.put( "code", -1 );
	}
	if ( resultMap.containsKey( "msg" ) ) {
	    result.put( "errorMsg", resultMap.get( "msg" ) );
	    logger.info( "调用接口异常：" + resultMap.get( "msg" ) );
	}
	if ( resultMap.containsKey( "data" ) ) {
	    result.put( "data", resultMap.get( "data" ) );
	}
	return result;
    }

}
