package com.gt.mall.util;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.constant.Constants;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/18 0018
 * Time : 16:22
 */
public class CommonUtil {

    private static final Logger log = Logger.getLogger( CommonUtil.class );

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

    public static Integer toIntegerByDouble( double obj ) {
	try {
	    return (int) obj;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
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

    public static String blob2String( Object obj ) {
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
    public static String urlEncode( String str ) {

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

    public static String getpath( HttpServletRequest request ) {
	String url = request.getScheme() + "://" + request.getServerName() // 服务器地址
			+ request.getContextPath() // 项目名称
			+ request.getServletPath() // 请求页面或其他地址
			+ ( CommonUtil.isEmpty( request.getQueryString() ) ? "" : "?" + request.getQueryString() ); // 参数
	return url;
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     *
     * @return
     */
    public static String format( String format, Object... args ) {
	String str = null;
	str = String.format( format, args );
	return str;
    }

    /**
     * 精确的减法运算
     *
     * @param v1 v1
     * @param v2 v2
     *
     * @return v1-v2
     */
    public static double subtract( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.subtract( b2 ).doubleValue();
    }

    /**
     * 精确的乘法运算
     *
     * @param v1 v1
     * @param v2 v2
     *
     * @return v1+v2
     */
    public static double multiply( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.multiply( b2 ).doubleValue();
    }

    /**
     * 精确的加法运算
     *
     * @param v1 v1
     * @param v2 v2
     *
     * @return v1*v2
     */
    public static double add( double v1, double v2 ) {
	BigDecimal b1 = new BigDecimal( Double.toString( v1 ) );
	BigDecimal b2 = new BigDecimal( Double.toString( v2 ) );
	return b1.add( b2 ).doubleValue();
    }

    /**
     * 除法
     *
     * @param v1    v1
     * @param v2    v2
     * @param scale 保留小数
     *
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
    private final static double R  = 6371229; // 地球的半径

    /**
     * 获取两点间的距离
     *
     * @param longt1 经度1
     * @param lat1   纬度1
     * @param longt2 经度2
     * @param lat2   纬度2
     *
     * @return
     */
    public static double getDistance( double longt1, double lat1, double longt2, double lat2 ) {
	double x, y, distance;
	x = ( longt2 - longt1 ) * PI * R * Math.cos( ( ( lat1 + lat2 ) / 2 ) * PI / 180 ) / 180;
	y = ( lat2 - lat1 ) * PI * R / 180;
	distance = Math.hypot( x, y );
	if ( distance > 0 ) {
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
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 6; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return sb.toString();
    }

    /**
     * 判断浏览器
     *
     * @return 1:微信浏览器,99:其他浏览器
     */
    public static Integer judgeBrowser( HttpServletRequest request ) {
	Integer result = null;
	String ua = request.getHeader( "user-agent" ).toLowerCase();
	if ( ua.indexOf( "micromessenger" ) > 0 ) {// 微信-1
	    result = 1;
	} else {//其他 -99
	    result = 99;
	}
	return result;
    }

    /**
     * 返回json数据到客户端
     *
     * @param response
     * @param obj
     *
     * @throws IOException
     */
    public static void write( HttpServletResponse response, Object obj ) throws IOException {
	if ( obj instanceof List || obj instanceof Object[] ) {
	    response.getWriter().print( JSONArray.toJSON( obj ) );
	} else if ( obj instanceof Boolean || obj instanceof Integer || obj instanceof String || obj instanceof Double ) {
	    Map< String,Object > result = new HashMap< String,Object >();
	    result.put( "status", obj );
	    response.getWriter().print( JSONObject.fromObject( result ) );
	} else {
	    response.getWriter().print( JSONObject.fromObject( obj ) );
	}
	response.getWriter().flush();
	response.getWriter().close();
    }

    /**
     * 获取IP
     *
     * @param request
     *
     * @return
     */
    public static String getIpAddr( HttpServletRequest request ) {
	String ip = request.getHeader( "x-forwarded-for" );
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getHeader( "Proxy-Client-IP" );
	}
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getHeader( "WL-Proxy-Client-IP" );
	}
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getRemoteAddr();
	}
	if ( "0:0:0:0:0:0:0:1".equals( ip ) ) {
	    ip = "127.0.0.1";
	}
	return ip;
    }

    /**
     * 图文保存，返回信息
     *
     * @param multipartFile
     * @param userId
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    public static Map< String,Object > fileUploadByBusUser( MultipartFile multipartFile, Integer userId ) {
	Map< String,Object > map = new HashMap<>();
	String originalFilename = multipartFile.getOriginalFilename();
	// 后缀
	String suffix = originalFilename.substring( originalFilename.lastIndexOf( "." ) );
	String phonejsp = originalFilename.substring( originalFilename.lastIndexOf( "." ) + 1 );
	// 文件大小
	Integer Size = Integer.parseInt( String.valueOf( multipartFile.getSize() ) );
	// 判断上传图片是否是支持的格式

	String path = PropertiesUtil.getResImagePath() + "/2/" + userId + "/" + Constants.IMAGE_FOLDER_TYPE_4 + "/" + DateTimeKit
			.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD ) + "/jietu/";

	File file = new File( path );
	if ( !file.exists() && !file.isDirectory() ) {
	    boolean flag = file.mkdirs();
	    if ( !flag ) {
		System.out.println( "创建文件失败 " );
	    }
	}
	Long time = System.currentTimeMillis();
	path += MD5Util.getMD5( time + originalFilename.substring( 0, originalFilename.lastIndexOf( "." ) ) ) + suffix;
	byte[] bytes;
	try {
	    bytes = multipartFile.getBytes();
	    InputStream is = new ByteArrayInputStream( bytes );
	    BufferedImage bufimg = ImageIO.read( is );
	    ImageIO.write( bufimg, phonejsp, new File( path ) );
	    ContinueFTP myFtp = new ContinueFTP();
	    try {
		myFtp.upload( path );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    is.close();
	    String url = "/image/2/" + userId + "/" + Constants.IMAGE_FOLDER_TYPE_4 + "/" + DateTimeKit.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD )
			    + "/jietu/" + MD5Util.getMD5( time + originalFilename.substring( 0, originalFilename.lastIndexOf( "." ) ) ) + suffix;
	    map.put( "reTurn", "1" );
	    map.put( "message", url );// 保存路径

	} catch ( IOException e ) {
	    e.printStackTrace();
	    map.put( "reTurn", "0" );
	    map.put( "message", "抛出异常" );
	}
	return map;

    }

    /**
     * 获取会员的支付方式
     *
     * @param payWay   支付方式  传 order.getOrderPayWay
     * @param isWallet 是否使用钱包支付   1已使用  0未使用 -1正在支付  传 order.getIsWallet
     *
     * @return @return 支付方式（调用 memberPayService.paySuccess用的，不适用与其他的接口）
     */
    public static int getMemberPayType( int payWay, int isWallet ) {
	int payType = 0;//现金支付
	switch ( payWay ) {
	    case 1://微信支付
		payType = 1;
		break;
	    case 3://储值卡支付
		payType = 5;
		break;
	    case 4://积分支付
		payType = 11;
		break;
	    case 5://扫码支付
		payType = 1;
		break;
	    case 7://找人代付
		payType = 1;
		break;
	    case 8://粉币支付
		payType = 12;
		break;
	    default:
		payType = 10;//现金支付
		break;
	}
	if ( isWallet == 1 ) {
	    payType = 1;
	}
	return payType;
    }

    /**
     * 获取会员的消费方式
     *
     * @return 消费方式 （调用 memberPayService.paySuccess用的，不适用与其他的接口）
     */
    public static int getMemberUcType( int orderType ) {
	int uctype = 104;//商城下单

	switch ( orderType ) {
	    case 1:
		uctype = 105;//团购
		break;
	    case 2:
		uctype = 110;//积分
		break;
	    case 3:
		uctype = 106;//秒杀
		break;
	    case 4:
		uctype = 107;//拍卖
		break;
	    case 5:
		uctype = 111;//粉币
		break;
	    case 6:
		uctype = 108;//预售
		break;
	    case 7:
		uctype = 109;//批发
		break;
	    default:
		uctype = 104;//商城下单
		break;
	}
	return uctype;
    }

    /**
     * 获取接口的域名
     *
     * @param type 0 会员  1 商家相关   2 微信相关 3 联盟
     *
     * @return 域名
     */
    public static String getHttpSignUrl( int type ) {
	if ( type == 1 || type == 2 ) {
	    return PropertiesUtil.getWxmpDomain();
	} else if ( type == 3 ) {
	    return PropertiesUtil.getUnionDomain();
	}
	return PropertiesUtil.getMemberDomain();
    }

    /**
     * 获取接口的签名key
     *
     * @param type 0 会员  1 商家相关   2 微信相关 3 联盟
     *
     * @return 签名key
     */
    public static String getHttpSignKey( int type ) {
	if ( type == 1 || type == 2 ) {
	    return PropertiesUtil.getWxmpSignKey();
	} else if ( type == 3 ) {
	    return PropertiesUtil.getUnionSignKey();
	}
	return PropertiesUtil.getMemberSignKey();
    }

    /**
     * 把用户集合改成用逗号隔开的字符串
     */
    public static String getMememberIds( List< Integer > memberList, int memberId ) {
	StringBuilder memberIds = new StringBuilder();
	if ( memberList != null && memberList.size() > 0 ) {
	    for ( Integer id : memberList ) {
		memberIds.append( id ).append( "," );
	    }
	    memberIds = new StringBuilder( memberIds.substring( 0, memberIds.length() - 1 ) );
	} else {
	    memberIds = new StringBuilder( CommonUtil.toString( memberId ) );
	}
	return memberIds.toString();
    }

    /**
     * 手机号验证
     */
    public static boolean isPhone( String phone ) {
	if ( StringUtils.isEmpty( phone ) ) {
	    return false;
	}
	Pattern pattern = Pattern.compile( "(^(13[0123456789][0-9]{8}|15[0123456789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$)" );
	Matcher matcher = pattern.matcher( phone );
	return matcher.matches();
    }
}
