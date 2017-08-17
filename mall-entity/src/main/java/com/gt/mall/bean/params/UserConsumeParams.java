package com.gt.mall.bean.params;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 订单支付记录
 * User : yangqian
 * Date : 2017/8/17 0017
 * Time : 9:36
 */
@Getter
@Setter
public class UserConsumeParams implements Serializable {

    private static final long serialVersionUID = -334515189424341832L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付方式
     */
    private int payType;

    /**
     * 支付状态
     */
    private int payStatus;
}
