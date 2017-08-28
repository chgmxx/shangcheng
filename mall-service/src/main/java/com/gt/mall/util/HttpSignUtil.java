package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.sign.SignHttpUtils;
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
public class HttpSignUtil {

    private static Logger logger = LoggerFactory.getLogger( HttpSignUtil.class );

    /**
     *
     * @param obj 参数
     * @param url 地址
     * @param types 0 会员  1 商家相关   2 微信相关
     * @return 返回对象
     */
    private static JSONObject SignHttpJson( Object obj, String url, int... types ) {
	try {
	    String result = null;
	    int type = CommonUtil.isNotEmpty( types ) && types.length > 0 ? types[0] : 0;
	    if ( type == 0 ) {//会员
		String signKey = PropertiesUtil.getMemberSignKey();
		url = PropertiesUtil.getMemberDomain() + url;
		logger.info( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( obj ) + "---签名key：" + signKey );
		result = SignHttpUtils.postByHttp( url, obj, signKey );
	    } else {
		//门店
		String signKey = PropertiesUtil.getWxmpSignKey();
		url = PropertiesUtil.getWxmpDomain() + url;
		if(type == 1){
		    logger.info( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( obj ) + "---签名key：" + signKey );
		    result = SignHttpUtils.WxmppostByHttp( url, obj, signKey );
		}else if(type == 2){
		    RequestUtils requestUtils = new RequestUtils();
		    requestUtils.setReqdata( obj );

		    logger.info( "请求接口URL：" + url + "---参数：" + JSONObject.toJSONString( requestUtils ) + "---签名key：" + signKey );
		    Map map = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, Map.class, signKey );

		    result = JSONObject.toJSONString( map );
		}
	    }
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
    public static String SignHttpSelect( Object params, String url, int... type ) {
	JSONObject resultObj = SignHttpJson( params, url, type );

	if ( resultObj.getInteger( "code" ) == 0 ) {
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
    public static Map< String,Object > SignHttpInsertOrUpdate( Object params, String url, int... type ) {
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
