package com.gt.mall;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.bean.wxshop.ShopPhoto;
import com.gt.mall.util.HttpSignUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试
 * User : yangqian
 * Date : 2017/8/8 0008
 * Time : 10:23
 */
public class BaseControllerTest extends BasicTest {

   /* @Autowired
    private MallStoreDAO  mallStoreDAO;
    @Autowired
    private MemberService memberService;
    @Autowired
    private DictService dictService;*/

    @Test
    public void tests() {

	String url = "http://yifriend.net/8A5DA52E/shopapi/79B4DE7C/getShopById.do";


	String signKey = "WXMP2017";
	try {
	   /* RequestUtils< Integer > requestUtils = new RequestUtils<>();
	    requestUtils.setReqdata( 17 );
	    SignBean sign = SignUtils.sign( signKey, JSONObject.toJSONString( requestUtils.getReqdata() ) );

	    requestUtils.setSign( JSONObject.toJSONString( sign ) );
	    requestUtils.setSignKey( signKey );
	    Map result = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, Map.class );
	    System.out.println( "result = " + JSONObject.toJSONString( result ) );*/

	    /*Map< String,Object > params = new HashMap<>();
	    params.put( "reqdata", 17 );
	    String result = SignHttpUtils.WxmppostByHttp( url, params, signKey );
	    System.out.println("result = " + result);*/

	} catch ( Exception e ) {
	    e.printStackTrace();
	}



	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", 17 );


	/*String result = HttpSignUtil.SignHttpSelect( params, "/8A5DA52E/shopapi/79B4DE7C/getShopById.do", 1 );
	WsWxShopInfo shop = JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
	System.out.println("json = " + shop.getBusinessName());*/


	String result = HttpSignUtil.SignHttpSelect( params, "/8A5DA52E/shopapi/79B4DE7C/getShopPhotoByShopId.do", 1 );
	System.out.println("result = " + result);

	List<ShopPhoto> photoList =  JSONArray.parseArray( result, ShopPhoto.class );
	System.out.println("json = " + photoList.get( 0 ).getLocalAddress());

    }
}


