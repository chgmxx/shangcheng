package com.gt.mall;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
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

    @Autowired
    private SmsService smsService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Test
    public void tests() {

	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", 17 );


	/*String result = HttpSignUtil.SignHttpSelect( params, "/8A5DA52E/shopapi/79B4DE7C/getShopById.do", 1 );
	WsWxShopInfo shop = JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
	System.out.println("json = " + shop.getBusinessName());*/

	/*String result = HttpSignUtil.SignHttpSelect( params, "/8A5DA52E/shopapi/79B4DE7C/getShopPhotoByShopId.do", 1 );
	System.out.println( "result = " + result );

	List< ShopPhoto > photoList = JSONArray.parseArray( result, ShopPhoto.class );
	System.out.println( "json = " + photoList.get( 0 ).getLocalAddress() );

	OldApiSms sms = new OldApiSms();
	sms.setBusId( 42 );
	sms.setContent( "hahah" );
	sms.setCompany( "5" );
	sms.setMobiles( "15017934717" );
	sms.setModel( 5 );
	boolean flag = smsService.sendSmsOld( sms );*/


	WxPublicUsers user = wxPublicUserService.selectByUserId( 42 );

	System.out.println("wxPublicUser = " + JSONObject.toJSONString( user ));

    }
}


