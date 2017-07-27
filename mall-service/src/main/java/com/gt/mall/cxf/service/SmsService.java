package com.gt.mall.cxf.service;

import com.gt.mall.bean.param.sms.OldApiSms;

/**
 * 短信业务接口（实现类会调用CXF接口）
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:14
 */
public interface SmsService {

    /**
     * 发送短信
     * @param oldApiSms 参数
     * @return true  发送成功   false  发送失败
     */
    public Boolean sendMsg( OldApiSms oldApiSms) throws Exception;

}
