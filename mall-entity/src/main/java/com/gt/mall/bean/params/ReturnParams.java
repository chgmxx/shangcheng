package com.gt.mall.bean.params;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 退款参数
 * User : yangqian
 * Date : 2017/8/17 0017
 * Time : 11:44
 */
@Getter
@Setter
public class ReturnParams implements Serializable {

    private static final long serialVersionUID = 186877643400581120L;

    /**
     * 商家id
     */
    private int busId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退款金额
     */
    private double money;

    /**
     * 退款粉币
     */
    private double fenbi = 0;

    /**
     * 退款积分
     */
    private double jifen = 0;
}
