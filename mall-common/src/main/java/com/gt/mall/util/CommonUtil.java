package com.gt.mall.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
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

    public static Integer toIntegerByDouble( double obj ) throws Exception {
	try {
	    return (int) obj;
	} catch ( Exception e ) {
	    throw new Exception( "double转int，转换失败！" );
	    //	    e.printStackTrace();
	}
    }

    /**
     * 转String
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
     */
    public static BigDecimal toBigDecimal( Object obj ) {
	try {
	    if ( isNotEmpty( obj ) ) {
		if ( isDouble( obj ) ) {
		    return BigDecimal.valueOf( toDouble( obj ) );
		} else {
		    throw new Exception( "对象不是double数据，不能转换成BigDecimal" );
		}
	    } else {
		throw new Exception( "对象为空，转换失败" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 是否为正整数
     */
    public static boolean isInteger( String str ) {
	Pattern pattern = Pattern.compile( "^[-\\+]?[\\d]*$" );
	return pattern.matcher( str ).matches();
    }

    public static String Blob2String( Object obj ) {
	String string = null;
	try {
	    if ( obj == null || obj.equals( "" ) ) {
		return "";
	    }
	    byte[] bytes = (byte[]) obj;
	    string = new String( bytes, "UTF-8" );
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return string;
    }

    public static String getBytes( String str ) {
	try {
	    if ( str.equals( new String( str.getBytes( "iso8859-1" ), "iso8859-1" ) ) ) {
		str = new String( str.getBytes( "iso8859-1" ), "utf-8" );
	    }
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return str;
    }

    /**
     * url中文参数乱码
     */
    public static String UrlEncode( String str ) {

	try {
	    return URLEncoder.encode( str, "UTF-8" );
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	    return "";
	}
    }

    /**
     * 获取卡号 截取一位 是生成条形码13位
     */
    public static String getCode() {
	Long date = new Date().getTime();
	return date.toString().substring( 1 );
    }

    public static String getpath(HttpServletRequest request) {
	String url = "http://"
			+ request.getServerName() // 服务器地址
			+ request.getContextPath() // 项目名称
			+ request.getServletPath() // 请求页面或其他地址
			+ (CommonUtil.isEmpty(request.getQueryString()) ? "" : "?"
			+ request.getQueryString()); // 参数
	return url;
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
	String str = null;
	str = String.format(format, args);
	return str;
    }

    /**
     * 精确的减法运算
     * @param v1 v1
     * @param  v2 v2
     * @return v1-v2
     */
    public static double subtract( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.subtract( b2 ).doubleValue();
    }

    /**
     * 精确的乘法运算
     * @param v1 v1
     * @param v2 v2
     * @return v1+v2
     */
    public static double multiply( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.multiply( b2 ).doubleValue();
    }

    /**
     * 精确的加法运算
     * @param v1 v1
     * @param v2 v2
     * @return v1*v2
     */
    public static double add( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.add( b2 ).doubleValue();
    }

    /**
     * 除法
     * @param v1 v1
     * @param v2 v2
     * @param scale 保留小数
     * @return v1/v2
     */
    public static double div( double v1, double v2, int scale ) {
	if ( scale < 0 ) {
	    return 0;
	}
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.divide( b2, scale, BigDecimal.ROUND_HALF_UP ).doubleValue();
    }


	private final static double PI = 3.14159265358979323; // 圆周率
	private final static double R = 6371229; // 地球的半径

	/**
	 * 获取两点间的距离
	 *
	 * @param longt1
	 *            经度1
	 * @param lat1
	 *            纬度1
	 * @param longt2
	 *            经度2
	 * @param lat2
	 *            纬度2
	 * @return
	 */
	public static double getDistance(double longt1, double lat1, double longt2,
									 double lat2) {
		double x, y, distance;
		x = (longt2 - longt1) * PI * R
				* Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		if (distance > 0) {
			return distance;
		} else {
			return 0.0;
		}
	}

	/**
	 * 获取推荐码 6位
	 *
	 * @return
	 */
	public static String getPhoneCode() {
		StringBuffer buf = new StringBuffer("1,2,3,4,5,6,7,8,9,0");
		String[] arr = buf.toString().split(",");
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			Integer count = arr.length;
			int a = random.nextInt(count);
			sb.append(arr[a]);
		}
		return sb.toString();
	}
}
