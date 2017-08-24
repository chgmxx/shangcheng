package com.gt.mall;

import com.gt.mall.bean.wx.OldApiSms;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/24 0024
 * Time : 10:08
 */
public class WxControllerTest extends BasicTest {
    @Autowired
    private WxShopService wxShopService;
    @Autowired
    private SmsService    smsService;

    @Test
    public void shop() {

	System.out.println( "shop = " + wxShopService.getShopById( 17 ) );

	System.out.println( "shopPhoto = " + wxShopService.getShopPhotoByShopId( 17 ) );

	System.out.println( "shopList = " + wxShopService.queryWxShopByBusId( 42 ) );

	System.out.println( "parentCity = " + wxShopService.queryCityByParentId( 2136 ) );

	System.out.println( "province = " + wxShopService.queryCityByLevel( 2 ) );

	System.out.println( "city = " + wxShopService.queryBasisCityIds( "2136" ) );
    }

    @Test
    public void sendSms() {
	OldApiSms sms = new OldApiSms();
	sms.setBusId( 42 );
	sms.setContent( "hahah" );
	sms.setCompany( "5" );
	sms.setMobiles( "15017934717" );
	sms.setModel( 5 );

	smsService.sendSmsOld( sms );
    }
}
