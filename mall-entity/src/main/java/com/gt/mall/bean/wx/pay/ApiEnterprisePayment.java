package com.gt.mall.bean.wx.pay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商家提现参数实体类
 */
@Getter
@Setter
public class ApiEnterprisePayment implements Serializable {

    private static final long serialVersionUID = -2401634885585066416L;
    /**
     * 公众号appid
     */
    private String appid;

    /**
     * 系统订单号
     */
    private String partner_trade_no;

    /**
     * openid
     */
    private String openid;

    /**
     * 描述
     */
    private String desc;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 商家ID
     */
    private Integer busId;

    /**
     * 模块ID
     */
    private Integer model;

    /**
     * 来源 0--公众号  1--小程序，默认0
     */
    private int paySource = 0;

}
