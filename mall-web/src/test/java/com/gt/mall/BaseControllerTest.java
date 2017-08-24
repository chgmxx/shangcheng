package com.gt.mall;

import com.gt.mall.bean.wx.OldApiSms;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxAppletService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.store.MallStoreService;
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

    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private MemberService    memberService;
    @Autowired
    private DictService      dictService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private WxAppletService wxAppletService;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private WxShopService wxShopService;

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
	System.out.println( "json = " + photoList.get( 0 ).getLocalAddress() );*/

	//测试发送短信
	OldApiSms sms = new OldApiSms();
	sms.setBusId( 42 );
	sms.setContent( "hahah" );
	sms.setCompany( "5" );
	sms.setMobiles( "15017934717" );
	sms.setModel( 5 );
	boolean flag = smsService.sendSmsOld( sms );

	/*String result = HttpSignUtil.SignHttpSelect( sms, "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do", 1 );
	System.out.println( "result = " + result );*/

	/*WxPublicUsers user = wxPublicUserService.selectByUserId( 42 );

	System.out.println("wxPublicUser = " + JSONObject.toJSONString( user ));*/

	/*MemberAppletByMemIdAndStyle applet = new MemberAppletByMemIdAndStyle();
	applet.setMemberId( 1225570 );
	applet.setStyle( 4 );
	MemberAppletOpenid memberAppletOpenid = wxAppletService.memberAppletByMemIdAndStyle( applet );
	System.out.println("result = " + memberAppletOpenid.getOpenid());*/

	//测试，根据商家id查询商家信息
	/*BusUser user = busUserService.selectById( 42 );
	System.out.println("userName = " + user.getMerchant_name());*/

	//查询是否拥有  进销存
	/*busUserService.getIsErpCount( 8,42 );*/

	//查询是否是管理员
	/*busUserService.getIsAdmin( 42 );*/

	//查询管理员账号
	/*busUserService.getMainBusId( 42 );*/

	//获取数量
	/*dictService.getDiBserNum( 42, 5, "1094" );*/

	//获取模块视频url
	/*busUserService.getVoiceUrl( "77" );*/

	/*WsWxShopInfo shopInfo = wxShopService.getShopById( 17 );

        //查询所有省份
	wxShopService.queryWxShopByBusId( 42 );*/
	//查询子类的城市
	/*wxShopService.queryCityByParentId( 39 );*/

	/*BusUser user = new BusUser();
	user.setId( 42 );
	List< Map< String,Object > > storeList = mallStoreService.findAllStoByUser( user );

	System.out.println( "storeList = " + JSONObject.toJSONString( storeList ) );*/


	/*WsWxShopInfo shopInfo = wxShopService.getShopById( 17 );*/

    }
}


