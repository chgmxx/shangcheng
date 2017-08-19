package com.gt.mall.bean.wx.pay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商家提现返回参数
 */
@Getter
@Setter
public class EnterprisePaymentResult implements Serializable {

    private static final long serialVersionUID = 6774366845907248081L;
    /**
     * 微信订单号
     */
    private String wxorderNo;

    /**
     * 企业支付订单表主键
     */
    private Integer id;

}
