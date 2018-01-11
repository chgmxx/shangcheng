package com.gt.mall.result.phone.shopcart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车页面商品返回结果
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneProductResult", description = "购物车页面商品返回结果" )
@Getter
@Setter
public class PhoneProductResult {

    @ApiModelProperty( name = "id", value = "购物车id" )
    private int id;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private int productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片地址" )
    private String productImageUrl;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private double productPrice = 0;

    @ApiModelProperty( name = "productOldPrice", value = "商品原价" )
    private double productOldPrice = 0;

    @ApiModelProperty( name = "productHyPrice", value = "商品会员价" )
    private double productHyPrice = 0;

    @ApiModelProperty( name = "productSpecifica", value = "商品规格值" )
    private String productSpecifica;

    @ApiModelProperty( name = "maxBuyNum", value = "限购 大于0判断" )
    private int maxBuyNum;

    @ApiModelProperty( name = "stockNum", value = "商品的库存数量 大于-1判断" )
    private int stockNum;

    @ApiModelProperty( name = "isError", value = "是否是失效商品 1失效 0 没失效" )
    private int isError = 0;

    @ApiModelProperty( name = "errorMsg", value = "失效原因" )
    private String errorMsg = "";

    @ApiModelProperty( name = "pifaSpecificaList", value = "批发规格集合" )
    private List< PhonePifaProductSpecificaResult > pifaSpecificaList;

    @ApiModelProperty( name = "pfType", value = "批发类型 1手批 2混批" )
    private int pfType = 0;
}
