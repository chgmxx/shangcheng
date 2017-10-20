package com.gt.mall.result.phone.shopcart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车页面返回结果
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneShopCartResult", description = "购物车页面返回结果" )
@Getter
@Setter
public class PhoneShopCartResult {

    @ApiModelProperty( name = "shopCartList", value = "购物车" )
    private List< PhoneShopCartListResult > shopCartList;

    @ApiModelProperty( name = "sxShopCartList", value = "失效购物车" )
    private List< PhoneShopCartListResult > sxShopCartList;

    @ApiModelProperty( name = "hpMoney", value = "混批条件：混批最低金额" )
    private double hpMoney = 0;

    @ApiModelProperty( name = "hpNum", value = "混批条件：混批购买最低数量" )
    private int hpNum = 1;//混批件数

    @ApiModelProperty( name = "spHand", value = "手批条件：最少购买多少手" )
    private int spHand = 1;

}
