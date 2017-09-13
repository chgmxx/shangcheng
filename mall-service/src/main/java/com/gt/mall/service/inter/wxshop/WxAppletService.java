package com.gt.mall.service.inter.wxshop;

import com.gt.util.entity.param.member.MemberAppletByMemIdAndStyle;
import com.gt.util.entity.result.member.MemberAppletOpenid;

/**
 * 微信小程序相关接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 17:44
 */
public interface WxAppletService {

    /**
     * 获取小程序用户信息
     * @param applet 参数
     * @return 小程序用户信息
     */
    MemberAppletOpenid memberAppletByMemIdAndStyle(MemberAppletByMemIdAndStyle applet);
}
