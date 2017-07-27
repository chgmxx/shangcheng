package com.gt.mall.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/18 0018
 * Time : 16:22
 */
public class CommonUtil {

    /**
     * 判断对象是否为空
     *
     * @param obj
     *
     * @return
     */
    public static boolean isEmpty( Object obj ) {
	boolean b = false;
	try {
	    if ( obj == null || "".equals( obj ) ) {
		b = true;
	    } else {
		b = false;
	    }
	} catch ( Exception e ) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     *
     * @return
     */
    public static boolean isNotEmpty( Object obj ) {
	boolean b = false;
	try {
	    if ( obj == null || "".equals( obj ) ) {
		b = false;
	    } else {
		b = true;
	    }
	} catch ( Exception e ) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 转Integer
     *
     * @param obj
     */
    public static Integer toInteger( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return Integer.parseInt( obj.toString() );
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    public static Integer toIntegerByDouble(double obj) throws Exception {
	try {
	    return (int) obj;
	} catch ( Exception e ) {
	    throw new Exception( "double转int，转换失败！" );
//	    e.printStackTrace();
	}
    }

    /**
     * 转String
     *
     * @param obj
     */
    public static String toString( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return obj.toString();
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转Double
     *
     * @param obj
     */
    public static Double toDouble( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return Double.parseDouble( obj.toString() );
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 校验是否是double数据
     */
    public static boolean isDouble( Object obj ) {
	try {
	    Double.parseDouble( obj.toString() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 转BigDecimal
     * @param obj
     * @return
     */
    public static BigDecimal toBigDecimal(Object obj){
        try {
            if(isNotEmpty( obj )){
                if(isDouble( obj )){
                    return BigDecimal.valueOf( toDouble( obj ) );
		}else{
                    throw  new Exception( "对象不是double数据，不能转换成BigDecimal" );
		}
	    }else{
                throw new Exception( "对象为空，转换失败" );
	    }
	}catch ( Exception e ){
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 是否为正整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
	return pattern.matcher(str).matches();
    }

    public static String Blob2String(Object obj) {
	String string = null;
	try {
	    if (obj == null || obj.equals("")) {
		return "";
	    }
	    byte[] bytes = (byte[]) obj;
	    string = new String(bytes, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return string;
    }

    public static String getBytes(String str){
	try {
	    if(str.equals(new String(str.getBytes("iso8859-1"), "iso8859-1")))
	    {
		str=new String(str.getBytes("iso8859-1"),"utf-8");
	    }
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return str;
    }


    /**
     * url中文参数乱码
     * @param str
     * @return
     */
    public static String UrlEncode(String str){

	try {
	    return  URLEncoder.encode(str, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return "";
	}
    }


}
