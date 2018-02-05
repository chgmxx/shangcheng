package com.gt.mall.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nan 2015-11
 */
public class MallHttpClientUtil {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8     = "UTF-8";

    private static void init() {
        if ( cm == null ) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal( 50 );// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute( 5 );// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager( cm ).build();
    }

    /**
     * @param url
     *
     * @return
     */
    public static String httpGetRequest( String url ) {
        HttpGet httpGet = new HttpGet( url );
        return getResult( httpGet );
    }

    public static String httpGetRequest( String url, Map< String,Object > params ) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath( url );

        ArrayList< NameValuePair > pairs = covertParams2NVPS( params );
        ub.setParameters( pairs );

        HttpGet httpGet = new HttpGet( ub.build() );
        return getResult( httpGet );
    }

    public static String httpGetRequest( String url, Map< String,Object > headers, Map< String,Object > params )
        throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath( url );

        ArrayList< NameValuePair > pairs = covertParams2NVPS( params );
        ub.setParameters( pairs );

        HttpGet httpGet = new HttpGet( ub.build() );
        for ( Map.Entry< String,Object > param : headers.entrySet() ) {
            httpGet.addHeader( param.getKey(), String.valueOf( param.getValue() ) );
        }
        return getResult( httpGet );
    }

    public static String httpPostRequest( String url ) {
        HttpPost httpPost = new HttpPost( url );
        return getResult( httpPost );
    }

    public static String httpPostRequest( String url, Map< String,Object > params, String token ) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost( url );
        ArrayList< NameValuePair > pairs = covertParams2NVPS( params );
        httpPost.setHeader( "token", token );
        httpPost.setEntity( new UrlEncodedFormEntity( pairs, UTF_8 ) );
        return getResult( httpPost );
    }

    public static String httpPostRequest( String url, Map< String,Object > params ) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost( url );
        ArrayList< NameValuePair > pairs = covertParams2NVPS( params );
        httpPost.setEntity( new UrlEncodedFormEntity( pairs, UTF_8 ) );
        return getResult( httpPost );
    }

    public static String httpPostRequest( String url, Map< String,Object > headers, Map< String,Object > params )
        throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost( url );

        for ( Map.Entry< String,Object > param : headers.entrySet() ) {
            httpPost.addHeader( param.getKey(), String.valueOf( param.getValue() ) );
        }

        ArrayList< NameValuePair > pairs = covertParams2NVPS( params );
        httpPost.setEntity( new UrlEncodedFormEntity( pairs, UTF_8 ) );

        return getResult( httpPost );
    }

    private static ArrayList< NameValuePair > covertParams2NVPS( Map< String,Object > params ) {
        ArrayList< NameValuePair > pairs = new ArrayList< NameValuePair >();
        for ( Map.Entry< String,Object > param : params.entrySet() ) {
            pairs.add( new BasicNameValuePair( param.getKey(), String.valueOf( param.getValue() ) ) );
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     *
     * @return
     */
    private static String getResult( HttpRequestBase request ) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute( request );
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if ( entity != null ) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString( entity );
                response.close();
                // httpClient.close();
                return result;
            }
        } catch ( ClientProtocolException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {

        }

        return EMPTY_STR;
    }

    public static void login() {

        String url = "http://jxc.deeptel.com.cn/erp/b/login";
        Map< String,Object > params = new HashMap< String,Object >();
        params.put( "account", "e_shop" );
        params.put( "pwd", "shop12345" );

        //
        // String url = "http://deeptel.com.cn/ErpMenus/79B4DE7C/getMenus.do";
        // Map<String, Object> params = new HashMap<String, Object>();
        // JSONObject json = new JSONObject();
        // json.put("style", 1);
        // json.put("userId", 33);
        // json.put("loginuc", 0);
        // json.put("erpstyle", 8);
        // ; params.put("str ", json.toJSONString());

        // System.out.println(json.toJSONString());
        try {
            // String result = httpPostRequest(url, params,
            // "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0ODc5ODkzMDAsInN1YiI6IntcInJvbGVJZFwiOjEsXCJhY2NvdW50XCI6XCJtYWxhbmdcIn0iLCJleHAiOjE0ODc5OTI5MDB9.45Mx76JnQ97XYXFmiZ_lVQfdqiooe0ESy1MBMUSp9q4");

            JSONObject jsonObject = JSONObject.parseObject( httpPostRequest( url, params ) );
            System.out.println( jsonObject );
            JSONObject tokens = jsonObject.getJSONObject( "data" );

            System.out.println( tokens );

            url = "http://jxc.deeptel.com.cn/erp/query/allProducts";
            params = new HashMap<>();
            params.put( "shopIds", 33 );
            System.out.println( httpPostRequest( url, params, tokens.getString( "token" ) ) );

        } catch ( Exception e ) {

            e.printStackTrace();
        }

    }

    public static void main( String[] args ) {
        login();
    }
}
