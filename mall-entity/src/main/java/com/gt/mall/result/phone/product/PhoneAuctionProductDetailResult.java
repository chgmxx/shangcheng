package com.gt.mall.result.phone.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 拍卖商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAuctionProductDetailResult", description = "拍卖商品详情返回结果" )
@Getter
@Setter
public class PhoneAuctionProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    /**
     * 报名人数
     */
    @ApiModelProperty( name = "marginNumber", value = "报名人数" )
    private int marginNumber = -1;

    /**
     * 抢拍次数
     */
    @ApiModelProperty( name = "auctionNumber", value = "抢拍次数" )
    private int auctionNumber = -1;

    /**
     * 保证金
     */
    @ApiModelProperty( name = "depositMoney", value = "保证金" )
    private double depositMoney = -1;

    /**
     * 拍卖类型 1 降价拍 2升价拍
     */
    @ApiModelProperty( name = "aucType", value = "拍卖类型 1 降价拍 2升价拍" )
    private Integer aucType;

    /**
     * 拍卖方式
     */
    @ApiModelProperty( name = "aucTypeVal", value = "拍卖方式" )
    private String aucTypeVal;

    /**
     * 起拍价格
     */
    @ApiModelProperty( name = "aucStartPrice", value = "起拍价格" )
    private double aucStartPrice = -1;

    /**
     * 最低价格
     */
    @ApiModelProperty( name = "aucLowestPrice", value = "最低价格" )
    private double aucLowestPrice = -1;

    /**
     * 降价时间（每多少分钟）
     */
    @ApiModelProperty( name = "aucLowerPriceTime", value = "降价时间（每多少分钟）" )
    private Integer aucLowerPriceTime;

    /**
     * 降价金额（每多少分钟降价多少元）
     */
    @ApiModelProperty( name = "aucLowerPrice", value = "降价金额（每多少分钟降价多少元）" )
    private double aucLowerPrice = -1;

    /**
     * 加价幅度
     */
    @ApiModelProperty( name = "aucAddPrice", value = "加价幅度" )
    private double aucAddPrice = -1;

    /**
     * 是否显示立即拍下按钮 1 显示 0 不显示
     */
    @ApiModelProperty( name = "isLijiPai", value = "是否显示立即拍下按钮 1 显示 0 不显示" )
    private int isLijiPai = 0;

    /**
     * 是否显示出价按钮 1显示
     */
    @ApiModelProperty( name = "isChujia", value = "是否显示出价按钮 1显示" )
    private int isChujia = 0;

    /**
     * 是否胜出  1 胜出 0未胜出
     */
    @ApiModelProperty( name = "isWin", value = "是否胜出  1 胜出 0未胜出" )
    private int isWin = -1;

    /**
     * 是否显示转订单按钮  1显示 0不显示
     */
    @ApiModelProperty( name = "isReturnOrder", value = "是否显示转订单按钮  1显示 0不显示" )
    private int isReturnOrder = -1;

    /**
     * 是否显示缴纳保证金的按钮  1显示
     */
    @ApiModelProperty( name = "isShowDeposit", value = "是否显示缴纳保证金的按钮  1显示" )
    private int isShowDeposit = -1;

}
