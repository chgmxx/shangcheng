package com.gt.mall.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/24 0024
 * Time : 11:22
 */
public class AddressUtil {

    private static Logger logger = LoggerFactory.getLogger( AddressUtil.class );

    public static String provinceadd( String ip ) throws Exception {
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        AddressUtil addressUtil = new AddressUtil();
        return addressUtil.getProvince( "ip=" + ip, "utf-8" );
    }

    public String getProvince( String content, String encodingString )
        throws UnsupportedEncodingException {
        // 这里调用pconline的接口
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult( urlStr, content, encodingString );
        if ( returnStr != null ) {
            // 处理返回的省市区信息
            System.out.println( returnStr );
            String[] temp = returnStr.split( "," );
            if ( temp.length < 3 ) {
                return "0";//无效IP，局域网测试
            }
            String region = ( temp[5].split( ":" ) )[1].replaceAll( "\"", "" );
            region = decodeUnicode( region );// 省份

            String country = "";
            String area = "";
            // String region = "";
            String city = "";
            String county = "";
            String isp = "";
            for ( int i = 0; i < temp.length; i++ ) {
                switch ( i ) {
                    case 1:
                        country = ( temp[i].split( ":" ) )[2].replaceAll( "\"", "" );
                        country = decodeUnicode( country );// 国家
                        break;
                    case 3:
                        area = ( temp[i].split( ":" ) )[1].replaceAll( "\"", "" );
                        area = decodeUnicode( area );// 地区
                        break;
                    case 5:
                        region = ( temp[i].split( ":" ) )[1].replaceAll( "\"", "" );
                        region = decodeUnicode( region );// 省份
                        break;
                    case 7:
                        city = ( temp[i].split( ":" ) )[1].replaceAll( "\"", "" );
                        city = decodeUnicode( city );// 市区
                        break;
                    case 9:
                        county = ( temp[i].split( ":" ) )[1].replaceAll( "\"", "" );
                        county = decodeUnicode( county );// 地区
                        break;
                    case 11:
                        isp = ( temp[i].split( ":" ) )[1].replaceAll( "\"", "" );
                        isp = decodeUnicode( isp ); // ISP公司
                        break;
                    default:
                        isp = "";
                }
            }

            System.out.println( country + "=" + area + "=" + region + "=" + city + "=" + county + "=" + isp );
            return region;
        }
        return null;
    }

    /**
     * @param urlStr   请求的地址
     * @param content  请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     *
     * @return
     */
    private String getResult( String urlStr, String content, String encoding ) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL( urlStr );
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout( 5000 );// 设置连接超时时间，单位毫秒
            connection.setReadTimeout( 5000 );// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput( true );// 是否打开输出流 true|false
            connection.setDoInput( true );// 是否打开输入流true|false
            connection.setRequestMethod( "POST" );// 提交方法POST|GET
            connection.setUseCaches( false );// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream( connection
                .getOutputStream() );// 打开输出流往对端服务器写数据
            out.writeBytes( content );// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), encoding ) );// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ( ( line = reader.readLine() ) != null ) {
                buffer.append( line );
            }
            reader.close();
            return buffer.toString();
        } catch ( IOException e ) {
            logger.error( "获取买家当前地址异常" + e.getMessage() );
            //	    e.printStackTrace();
        } finally {
            if ( connection != null ) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     *
     * @return
     * @author fanhui 2007-3-15
     */
    public static String decodeUnicode( String theString ) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer( len );
        for ( int x = 0; x < len; ) {
            aChar = theString.charAt( x++ );
            if ( aChar == '\\' ) {
                aChar = theString.charAt( x++ );
                if ( aChar == 'u' ) {
                    int value = 0;
                    for ( int i = 0; i < 4; i++ ) {
                        aChar = theString.charAt( x++ );
                        switch ( aChar ) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = ( value << 4 ) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = ( value << 4 ) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = ( value << 4 ) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                    "Malformed      encoding." );
                        }
                    }
                    outBuffer.append( (char) value );
                } else {
                    if ( aChar == 't' ) {
                        aChar = '\t';
                    } else if ( aChar == 'r' ) {
                        aChar = '\r';
                    } else if ( aChar == 'n' ) {
                        aChar = '\n';
                    } else if ( aChar == 'f' ) {
                        aChar = '\f';
                    }
                    outBuffer.append( aChar );
                }
            } else {
                outBuffer.append( aChar );
            }
        }
        return outBuffer.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *
     * @return
     * @throws IOException
     */
    public static StringBuilder getUrlConnection( String url ) throws IOException {
        StringBuilder json = new StringBuilder();

        URL oracle = new URL( url );
        URLConnection conn = oracle.openConnection(); // 打开和URL之间的连接
        // 设置通用的请求属性
        conn.setRequestProperty( "accept", "*/*" );
        conn.setRequestProperty( "Accept-Charset", "UTF-8" );
        conn.setRequestProperty( "contentType", "UTF-8" );
        conn.setRequestProperty( "Content-type", "application/x-www-form-urlencoded;charset=UTF-8" );
        conn.setRequestProperty( "Accept-Language", Locale.getDefault().toString() );
        // 定义BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader( new InputStreamReader( conn.getInputStream(), "utf-8" ) );
        String inputLine = null;
        while ( ( inputLine = in.readLine() ) != null ) {
            json.append( inputLine );
        }
        in.close();

        return json;
    }

    /**
     * 通过经纬度来获取地址信息
     *
     * @param lat 纬度
     * @param lng 经度
     *
     * @return area        区县
     * @throws Exception
     */
    public static Map< String,Object > getAddressBylnglat( String lat, String lng ) throws Exception {
        try {
            Map< String,Object > resultMap = new HashMap< String,Object >();
            //解析经纬度获取地址
            String url = "http://api.map.baidu.com/geocoder/v2/?location=" + lat + "," + lng + "&output=json&ak=82d7136146878d664def8c56de8255f5";

            StringBuilder json = getUrlConnection( url );

            //解析经纬度获取地址
            if ( CommonUtil.isNotEmpty( json ) ) {
                JSONObject obj = JSONObject.parseObject( json.toString() );
                if ( obj.getInteger( "status" ) == 0 && CommonUtil.isNotEmpty( obj.get( "result" ) ) ) {
                    JSONObject resultObj = JSONObject.parseObject( obj.get( "result" ).toString() );
                    if ( CommonUtil.isNotEmpty( resultObj.get( "addressComponent" ) ) ) {
                        JSONObject addressObj = JSONObject.parseObject( resultObj.get( "addressComponent" ).toString() );
                        if ( CommonUtil.isNotEmpty( addressObj.get( "province" ) ) ) {//详细地址
                            resultMap.put( "addresss", resultObj.get( "formatted_address" ) );
                        }
                        if ( CommonUtil.isNotEmpty( addressObj.get( "province" ) ) ) {//省份
                            resultMap.put( "province", addressObj.get( "province" ) );
                        }
                        if ( CommonUtil.isNotEmpty( addressObj.get( "city" ) ) ) {//城市
                            resultMap.put( "city", addressObj.get( "city" ) );
                        }
                        if ( CommonUtil.isNotEmpty( addressObj.get( "district" ) ) ) {//区县
                            resultMap.put( "area", addressObj.get( "district" ) );
                        }
                        return resultMap;
                    }
                }
            }
        } catch ( Exception e ) {
            throw new Exception( "通过经纬度获取地址异常：" + e.getMessage() );
        }
        return null;
    }

    /**
     * 通过地址来获取经纬度
     *
     * @param address 详细地址
     * @param city    地址所在的城市名
     *
     * @return lng        经度
     */
    public static Map< String,Object > getlnglatByAddress( String address, String city ) throws Exception {
        try {
            Map< String,Object > resultMap = new HashMap< String,Object >();
            //解析地址获取经纬度
            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&city=" + city + "&output=json&ak=82d7136146878d664def8c56de8255f5";

            StringBuilder json = getUrlConnection( url );

            //解析地址获取经纬度
            if ( CommonUtil.isNotEmpty( json ) ) {
                JSONObject obj = JSONObject.parseObject( json.toString() );
                if ( obj.getInteger( "status" ) == 0 ) {
                    double lng = obj.getJSONObject( "result" ).getJSONObject( "location" ).getDouble( "lng" );
                    double lat = obj.getJSONObject( "result" ).getJSONObject( "location" ).getDouble( "lat" );
                    resultMap.put( "lng", lng );
                    resultMap.put( "lat", lat );
                    return resultMap;
                }
            }

        } catch ( Exception e ) {
            throw new Exception( "通过地址获取经纬度异常：" + e.getMessage() );
        }
        return null;
    }

    public static void main( String[] args ) throws Exception {
        Map< String,Object > resultMap = getlnglatByAddress( "兰光科技园", "深圳" );
        System.out.println( "sss:" + resultMap );
        resultMap = getAddressBylnglat( "22.559778383131384", "113.94828144248515" );
        System.out.println( "sss:" + resultMap );
    }
}
