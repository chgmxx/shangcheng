package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.wxshop.OldApiSms;

/**
 * 发送短信接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 16:35
 */
public interface SmsService {

    /**
     * 发送短信的九街口
     * @param oldApiSms 发送短信实体类
     * @return true 成功 false 失败
     */
    boolean sendSmsOld(OldApiSms oldApiSms);
}
