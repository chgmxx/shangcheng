package com.gt.mall.param.phone.presale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户缴纳定金参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddDeposit", description = "用户缴纳定金参数" )
@Getter
@Setter
public class PhoneAddDepositDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productId", value = "商品ID", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "presaleId", value = "预售ID", required = true )
    @NotNull( message = "预售ID不能为空" )
    @Min( value = 1, message = "预售ID不能小于1" )
    private Integer presaleId;

    @ApiModelProperty( name = "depositMoney", value = "定金金额", required = true )
    @NotNull( message = "定金金额不能为空" )
    @Min( value = 1, message = "定金金额不能小于1" )
    private BigDecimal depositMoney;

    @ApiModelProperty( name = "proName", value = "商品名称", required = true )
    private String proName;

    @ApiModelProperty( name = "proImgUrl", value = "商品图片", required = true )
    private String proImgUrl;

    @ApiModelProperty( name = "proSpecificaIds", value = "商品规格id  存放多个规格，用逗号(,)分隔", required = true )
    private String proSpecificaIds;

    @ApiModelProperty( name = "payWay", value = "支付方式 1 微信支付 2 储值卡支付  3 支付宝支付", required = true )
    private Integer payWay;

    @ApiModelProperty( name = "orderMoney", value = "订购价 预定时的价格", required = true )
    private BigDecimal orderMoney;

    @ApiModelProperty( name = "proNum", value = "商品数量", required = true )
    private Integer proNum;
}
