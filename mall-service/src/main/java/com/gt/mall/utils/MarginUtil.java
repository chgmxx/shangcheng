package com.gt.mall.utils;

import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
import com.gt.util.entity.param.pay.PayWay;

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
     * @param memType     会员类型   3储值卡支付
     * @param payWay      支付方式
     * @param browserType 浏览器类型 1 微信浏览器   99 普通浏览器
     *
     * @return 支付方式集合
     */
    public static List< PhoneOrderWayDTO > getPayWay( int memType, PayWay payWay, int browserType ) {
	List< PhoneOrderWayDTO > payWayList = new ArrayList<>();
	if ( CommonUtil.isNotEmpty( payWay ) ) {
	    /*if ( payWay.getDfpay() == 0 ) {
		PhoneOrderWayDTO result = new PhoneOrderWayDTO( 4, "多粉钱包支付", "duofenlogoyuanwenjian-" );
		payWayList.add( result );
	    } else */if ( payWay.getWxpay() == 0 && browserType == 1 ) {
		PhoneOrderWayDTO result = new PhoneOrderWayDTO( 1, "微信支付", "weixinzhifu" );
		payWayList.add( result );
	    } else if ( payWay.getAlipay() == 0 && browserType != 1 ) {
		PhoneOrderWayDTO result = new PhoneOrderWayDTO( 3, "支付宝支付", "alipay" );
		payWayList.add( result );
	    }
	}
	if ( memType == 3 ) {
	    PhoneOrderWayDTO result = new PhoneOrderWayDTO( 2, "储值卡支付", "chuzhika" );
	    payWayList.add( result );
	}
	return payWayList;
    }
}
