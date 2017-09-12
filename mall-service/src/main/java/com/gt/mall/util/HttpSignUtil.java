package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
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

    /**
     * @param obj   参数
     * @param url   地址
     * @param types 0 会员  1 商家相关   2 微信相关 3 联盟
     *
     * @return 返回对象
     */
    private static JSONObject SignHttpJson( Object obj, String url, int... types ) {
	long startTime = System.currentTimeMillis();
	try {
	    String result = null;
	    int type = CommonUtil.isNotEmpty( types ) && types.length > 0 ? types[0] : 0;
	    String signKey = CommonUtil.getHttpSignKey( type );
	    String newUrl = CommonUtil.getHttpSignUrl( type ) + url;
	    if ( type == 0 ) {
		newUrl = "http://113.106.202.53:13885/" + url;
	    }
	    if ( type == 2 || type == 3 ) {//联盟和 微信相关接口需要封装一层
		RequestUtils requestUtils = new RequestUtils();
		requestUtils.setReqdata( obj );
		obj = requestUtils;
	    }
	    logger.info( "请求接口URL：" + newUrl + "---参数：" + JSONObject.toJSONString( obj ) + "---签名key：" + signKey );
	    if ( type == 1 ) {//商家
		result = SignHttpUtils.WxmppostByHttp( newUrl, obj, signKey );
	    } else if ( type == 2 ) {//微信、门店、支付
		Map map = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( obj ), newUrl, Map.class, signKey );
		result = JSONObject.toJSONString( map );
	    } else {//会员、联盟
		result = SignHttpUtils.postByHttp( newUrl, obj, signKey );
	    }
	    long endTime = System.currentTimeMillis();
	    long executeTime = endTime - startTime;

	    logger.info( "请求接口的执行时间 : " + executeTime + "ms" );
	    logger.info( "接口返回result:" + result );

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
    public static String signHttpSelect( Object params, String url, int... type ) {
	JSONObject resultObj = SignHttpJson( params, url, type );

	if ( resultObj.containsKey( "code" ) && resultObj.getInteger( "code" ) == 0 ) {
	    logger.info( "data = " + resultObj.getString( "data" ) );
	    return resultObj.getString( "data" );
	} else {
	    logger.info( "调用接口异常：" + resultObj.getString( "msg" ) );
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
    public static Map< String,Object > signHttpInsertOrUpdate( Object params, String url, int... type ) {
	Map< String,Object > resultMap = new HashMap<>();
	JSONObject resultObj = SignHttpJson( params, url, type );
	/*logger.info( "调用接口返回值 = " + resultObj.getString( "data" ) );*/
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
	    logger.info( "调用接口异常：" + resultObj.getString( "msg" ) );
	}
	if ( resultMap.containsKey( "data" ) ) {
	    resultMap.put( "data", resultMap.get( "data" ) );
	}
	return resultMap;
    }

}
