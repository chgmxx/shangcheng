package com.gt.mall.result.phone.order.daifu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 获取代付订单的返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneGetDaiFuResult", description = "获取代付订单的返回结果" )
@Getter
@Setter
public class PhoneGetDaiFuResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "orderMoney", value = "代付金额" )
    private double orderMoney;

    @ApiModelProperty( name = "recevieUserName", value = "收货人姓名" )
    private String recevieUserName;

    @ApiModelProperty( name = "isShowTimes", value = "是否显示倒计时时间 1显示" )
    private Integer isShowTimes = 0;

    @ApiModelProperty( name = "times", value = "倒计时时间" )
    private long times;

    @ApiModelProperty( name = "isShowWxPay", value = "是否显示微信支付  1显示" )
    private Integer isShowWxPay = 0;

    @ApiModelProperty( name = "isShowAliPay", value = "是否显示支付宝支付 1显示" )
    private Integer isShowAliPay = 0;

    @ApiModelProperty( name = "productResultList", value = "商品集合" )
    private List< PhoneGetDaiFuProductResult > productResultList;

    @ApiModelProperty( name = "daifuId", value = "代付id" )
    private Integer daifuId;

}
