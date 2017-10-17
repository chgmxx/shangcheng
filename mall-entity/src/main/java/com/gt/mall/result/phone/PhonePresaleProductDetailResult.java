package com.gt.mall.result.phone;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 预售商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@Getter
@Setter
public class PhonePresaleProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    /**
     * 订购量
     */
    private int buyCount = 0;

    /**
     * 是否显示预定按钮 1显示
     */
    private int isShowPresaleButton = 0;

    /**
     * 是否显示支付尾款按钮 1显示
     */
    private int isShowWeiMoneyButton = 0;

    /**
     * 是否显示即将开售按钮 1显示
     */
    private int isShowStartButton = 0;

    /**
     * 定金
     */
    private double dingMoney = 0;

    /**
     * 尾款
     */
    private double weiMoney = 0;

    /**
     * 缴纳定金状态 1已缴纳 0未缴纳
     */
    private int payDespositStatus = 0;

}
