package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 12:04
 */
public class CxfFactoryBeanUtil {

    /**
     * 创建cxf
     * @param className 类名
     * @param url 请求地址
     * @return Object 对象
     * @throws Exception  异常
     */
    public static Object crateCxfFactoryBean( String className, String url ) throws Exception {
	Class c = Class.forName( className );
	JaxWsProxyFactoryBean jaxWsProxyFactoryBean;
	jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	jaxWsProxyFactoryBean.setServiceClass( c );
	jaxWsProxyFactoryBean.setAddress( url );
	return jaxWsProxyFactoryBean.create();
    }

    /**
     * 解析json，获取最终数据
     * @param json  json
     * @return 数据
     */
    public static String getStringResultByJson(String json){
	if ( CommonUtil.isNotEmpty( json ) ) {
	    JSONObject resultObj = JSONObject.parseObject( json );
	    if ( resultObj.getInteger( "code" ) == 0 ) {//请求成功
		return resultObj.getString( "data" );
	    }
	}
	return null;
    }

    /**
     * 解析json，获取操作是否成功
     * @param json json
     * @return  true 成功  false 失败
     */
    public static boolean getBooleanResultByJson(String json){
	if ( CommonUtil.isNotEmpty( json ) ) {
	    JSONObject resultObj = JSONObject.parseObject( json );
	    if ( resultObj.getInteger( "code" ) == 0 ) {//请求成功
		return true;
	    }
	}
	return false;
    }
}
