package com.gt.mall.utils;

import com.gt.mall.param.phone.order.PhoneOrderWayDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/11/24 0024
 * Time : 19:07
 */
public class MarginUtil {
    /**
     * 获取商家的支付方式
     *
     * @param isWxPay  是否可以使用微信支付  1可以
     * @param isAliPay 是否可以使用支付宝支付  1可以
     * @param memType  会员类型   3储值卡支付
     *
     * @return 支付方式集合
     */
    public static List< PhoneOrderWayDTO > getPayWay( int isWxPay, int isAliPay, int memType ) {
	List< PhoneOrderWayDTO > payWayList = new ArrayList<>();

	if ( isWxPay == 1 ) {
	    PhoneOrderWayDTO result = new PhoneOrderWayDTO( 1, "微信支付", "weixinzhifu" );
	    payWayList.add( result );
	}
	if ( isAliPay == 1 ) {
	    PhoneOrderWayDTO result = new PhoneOrderWayDTO( 3, "支付宝支付", "alipay" );
	    payWayList.add( result );
	}
	if ( memType == 3 ) {
	    PhoneOrderWayDTO result = new PhoneOrderWayDTO( 2, "储值卡支付", "chuzhika" );
	    payWayList.add( result );
	}
	return payWayList;
    }
}
