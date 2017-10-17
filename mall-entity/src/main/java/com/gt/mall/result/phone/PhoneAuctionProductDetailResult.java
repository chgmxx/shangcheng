package com.gt.mall.result.phone;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 拍卖商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@Getter
@Setter
public class PhoneAuctionProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    /**
     * 报名人数
     */
    private int marginNumber = -1;

    /**
     * 抢拍次数
     */
    private int auctionNumber = -1;

    /**
     * 保证金
     */
    private double depositMoney = -1;

    /**
     * 拍卖类型 1 降价拍 2升价拍
     */
    private Integer aucType;

    /**
     * 拍卖方式
     */
    private String aucTypeVal;

    /**
     * 起拍价格
     */
    private double aucStartPrice = -1;

    /**
     * 最低价格
     */
    private double aucLowestPrice = -1;

    /**
     * 降价时间（每多少分钟）
     */
    private Integer aucLowerPriceTime;

    /**
     * 降价金额（每多少分钟降价多少元）
     */
    private double aucLowerPrice = -1;

    /**
     * 加价幅度
     */
    private double aucAddPrice = -1;

    /**
     * 是否显示立即拍下按钮 1 显示 0 不显示
     */
    private int isLijiPai = 0;

    /**
     * 是否显示出价按钮 1显示
     */
    private int isChujia = 0;

    /**
     * 是否胜出  1 胜出 0未胜出
     */
    private int isWin = -1;

    /**
     * 是否显示转订单按钮  1显示 0不显示
     */
    private int isReturnOrder = -1;

}
