package com.gt.mall.bean.wxshop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 旧版本短信发送接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 16:37
 */
@Getter
@Setter
public class OldApiSms implements Serializable {

    private static final long serialVersionUID = 4186690892268475133L;
    /**
     * 手机号码，多个手机号码以逗号隔开
     */
    private String mobiles;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 商家ID
     */
    private Integer busId;

    /**
     * 短信模块ID(字典1071)
     */
    private Integer model;
}
