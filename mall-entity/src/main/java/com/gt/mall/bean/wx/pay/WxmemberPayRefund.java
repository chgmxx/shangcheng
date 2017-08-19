package com.gt.mall.bean.wx.pay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信退款
 *
 * @author Administrator
 */
@Getter
@Setter
public class WxmemberPayRefund implements Serializable {

    private static final long serialVersionUID = -5693130263715888421L;

    /**
     * appid
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 订单号
     */
    private String sysOrderNo;

    /**
     * 退款金额
     */
    private Double refundFee;

    /**
     * 总金额
     */
    private Double totalFee;

}
