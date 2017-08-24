package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wx.applet.MemberAppletByMemIdAndStyle;
import com.gt.mall.bean.wx.applet.MemberAppletOpenid;
import com.gt.mall.service.inter.wxshop.WxAppletService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序实现类
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 17:44
 */
@Service
public class WxAppletServiceImpl implements WxAppletService {

    private static final String APPLET_URL = "/8A5DA52E/memapi/6F6D9AD2/79B4DE7C/";

    @Override
    public MemberAppletOpenid memberAppletByMemIdAndStyle( MemberAppletByMemIdAndStyle applet ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", applet );
	String result = HttpSignUtil.SignHttpSelect( params, APPLET_URL + "memberAppletByMemIdAndStyle.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), MemberAppletOpenid.class );
	}
	return null;
    }
}
