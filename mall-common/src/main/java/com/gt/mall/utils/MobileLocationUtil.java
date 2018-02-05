package com.gt.mall.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 */
public class MobileLocationUtil {

    /**
     * 获取URL返回的字符串
     *
     * @param callurl
     * @param charset
     *
     * @return
     */
    private static String callUrlByGet( String callurl, String charset ) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL( callurl );
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), charset ) );
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                result.append( line );
                result.append( "\n" );
            }
            reader.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            return "";
        }
        return result.toString();
    }

    /**
     * 手机号码归属地
     *
     * @param tel 手机号码
     *
     * @return 135XXXXXXXX, 联通/移动/电信,湖北武汉
     * @throws Exception
     * @author
     */
    public static Map< String,String > getMobileLocation( String tel ) {
        Map< String,String > resultMap = new HashMap< String,String >();
        resultMap.put( "province", "未知" );
        resultMap.put( "city", "未知" );
        resultMap.put( "supplier", "未知" );
        try {
            Pattern pattern = Pattern.compile( "1\\d{10}" );
            Matcher matcher = pattern.matcher( tel );
            if ( matcher.matches() ) {
                String url = "http://api.k780.com:88/?app=phone.get&phone=" + tel + "&appkey=29824&sign=60be97ade8de1c69193e3ddae965b73b&format=json";
                String result = callUrlByGet( url, "utf-8" );
                Map< String,Object > s = (Map< String,Object >) JSONObject.parse( result );
                if ( s.get( "success" ).equals( "1" ) ) {
                    resultMap.put( "code", "1" );

                    Map< String,String > map1 = (Map< String,String >) s.get( "result" );
                    String[] arr = map1.get( "att" ).split( "," );
                    resultMap.put( "city", arr[2] );
                    resultMap.put( "province", arr[1] );
                    resultMap.put( "supplier", map1.get( "operators" ) );
                    return resultMap;
                } else {
                    resultMap.put( "code", "-1" );
                    resultMap.put( "msg", "系统繁忙，请稍后" );
                    return resultMap;
                }
            } else {
                resultMap.put( "code", "-1" );
                resultMap.put( "msg", "手机格式错误" );
                return resultMap;

            }
        } catch ( Exception e ) {
            resultMap.put( "code", "-1" );
            resultMap.put( "msg", "系统繁忙，请稍后" );
            return resultMap;
        }

    }

}
