package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /**
     * 向指定URL发送GET方法的请求
     * @param url
     * @return
     * @throws IOException
     */
    public static StringBuilder getUrlConnection(String url) throws IOException {
	StringBuilder json = new StringBuilder();

	URL oracle = new URL(url);
	URLConnection conn = oracle.openConnection(); // 打开和URL之间的连接
	// 设置通用的请求属性
	conn.setRequestProperty("accept", "*/*");
	conn.setRequestProperty("Accept-Charset", "UTF-8");
	conn.setRequestProperty("contentType", "UTF-8");
	conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
	conn.setRequestProperty("Accept-Language", Locale.getDefault().toString());
	// 定义BufferedReader输入流来读取URL的响应
	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	String inputLine = null;
	while ( (inputLine = in.readLine()) != null) {
	    json.append(inputLine);
	}
	in.close();

	return json;
    }

    /**
     * 通过经纬度来获取地址信息
     * @param lat  纬度
     * @param lng  经度
     * @return province 省份
     * @return city 	城市
     * @return area	区县
     * @throws Exception
     */
    public static Map<String, Object> getAddressBylnglat(String lat,String lng) throws  Exception{
	try {
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    //解析经纬度获取地址
	    String url = "http://api.map.baidu.com/geocoder/v2/?location="+lat+","+lng+"&output=json&ak=82d7136146878d664def8c56de8255f5";

	    StringBuilder json = getUrlConnection(url);

	    //解析经纬度获取地址
	    if(CommonUtil.isNotEmpty(json)){
		JSONObject obj = JSONObject.parseObject( json.toString() );
		if(obj.getInteger("status") == 0 && CommonUtil.isNotEmpty(obj.get("result"))){
		    JSONObject resultObj = JSONObject.parseObject(obj.get("result").toString());
		    if(CommonUtil.isNotEmpty(resultObj.get("addressComponent"))){
			JSONObject addressObj = JSONObject.parseObject(resultObj.get("addressComponent").toString());
			if(CommonUtil.isNotEmpty(addressObj.get("province"))){//详细地址
			    resultMap.put("addresss", resultObj.get("formatted_address"));
			}
			if(CommonUtil.isNotEmpty(addressObj.get("province"))){//省份
			    resultMap.put("province", addressObj.get("province"));
			}
			if(CommonUtil.isNotEmpty(addressObj.get("city"))){//城市
			    resultMap.put("city", addressObj.get("city"));
			}
			if(CommonUtil.isNotEmpty(addressObj.get("district"))){//区县
			    resultMap.put("area", addressObj.get("district"));
			}
			return resultMap;
		    }
		}
	    }
	} catch (Exception e) {
	    throw new Exception( "通过经纬度获取地址异常："+e.getMessage() );
	}
	return null;
    }

    /**
     * 通过地址来获取经纬度
     * @param address 	详细地址
     * @param city	地址所在的城市名
     * @return lat	纬度
     * @return lng	经度
     */
    public static Map<String, Object> getlnglatByAddress(String address,String city) throws Exception {
	try {
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    //解析地址获取经纬度
	    String url ="http://api.map.baidu.com/geocoder/v2/?address="+address+"&city="+city+"&output=json&ak=82d7136146878d664def8c56de8255f5";

	    StringBuilder json = getUrlConnection(url);

	    //解析地址获取经纬度
	    if(CommonUtil.isNotEmpty(json)){
		JSONObject obj = JSONObject.parseObject(json.toString());
		if(obj.getInteger("status") == 0){
		    double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
		    double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
		    resultMap.put("lng", lng);
		    resultMap.put("lat", lat);
		    return resultMap;
		}
	    }

	} catch (Exception e) {
	    throw new Exception( "通过地址获取经纬度异常："+e.getMessage());
	}
	return null;
    }

    public static void main( String[] args ) throws Exception {
	Map<String, Object> resultMap = getlnglatByAddress( "兰光科技园","深圳" );
	System.out.println("sss:"+resultMap);
	resultMap = getAddressBylnglat( "22.559778383131384","113.94828144248515" );
	System.out.println("sss:"+resultMap);
    }
}
