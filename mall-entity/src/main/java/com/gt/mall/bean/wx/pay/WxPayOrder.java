package com.gt.mall.bean.wx.pay;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class WxPayOrder implements Serializable {

    private static final long serialVersionUID = 5082645885674818717L;

    private Integer id;

    private String outTradeNo;

    private String transactionId;

    private String feeType;

    private Double totalFee;

    private Double refundFee;

    private String spbillCreateIp;

    private Date timeStart;

    private Date timeExpire;

    private String tradeType;

    private String productId;

    private String prepayId;

    private String codeUrl;

    private String errCodeDes;

    private String openid;

    private String tradeState;

    private String bankType;

    private Double cashFee;

    private String cashFeeType;

    private Double couponFee;

    private Integer couponCount;

    private Date timeEnd;

    private String outRefundNo;

    private String refundFeeType;

    private String opUserId;

    private String refundId;

    private String refundChannel;

    private Double cashRefundFee;

    private Double couponRefundFee;

    private Integer couponRefundCount;

    private String couponRefundId;

    private String refundStatus;

    private String refundRecvAccout;

    private Integer model;

    private Integer pay_type;

    private Integer bus_id;

    private String businessName;

    private Integer paySource;

    private String submitNum;

}