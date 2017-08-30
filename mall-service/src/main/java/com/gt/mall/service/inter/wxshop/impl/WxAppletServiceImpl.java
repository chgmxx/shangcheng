package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wx.applet.MemberAppletByMemIdAndStyle;
import com.gt.mall.bean.wx.applet.MemberAppletOpenid;
import com.gt.mall.service.inter.wxshop.WxAppletService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

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
	String result = HttpSignUtil.signHttpSelect( applet, APPLET_URL + "memberAppletByMemIdAndStyle.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), MemberAppletOpenid.class );
	}
	return null;
    }
}
