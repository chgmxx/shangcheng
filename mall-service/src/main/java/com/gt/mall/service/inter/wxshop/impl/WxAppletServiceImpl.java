package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.wxshop.WxAppletService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.util.entity.param.member.MemberAppletByMemIdAndStyle;
import com.gt.util.entity.result.member.MemberAppletOpenid;
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
        RequestUtils< MemberAppletByMemIdAndStyle > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( applet );
        String result = HttpSignUtil.signHttpSelect( requestUtils, APPLET_URL + "memberAppletByMemIdAndStyle.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            return JSONObject.toJavaObject( JSONObject.parseObject( result ), MemberAppletOpenid.class );
        }
        return null;
    }
}
