package com.gt.mall.result.phone.shopcart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 购物车页面批发商品规格返回结果
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhonePifaProductSpecificaResult", description = "购物车页面批发商品规格" )
@Getter
@Setter
public class PhonePifaProductSpecificaResult {

    @ApiModelProperty( name = "id", value = "购物车id" )
    private int id;

    @ApiModelProperty( name = "specificaValueIds", value = "规格值id" )
    private String specificaValueIds;

    @ApiModelProperty( name = "specificaValues", value = "规格值" )
    private String specificaValues;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

    @ApiModelProperty( name = "productPrice", value = "规格价格" )
    private double specificaPrice = 0;

    @ApiModelProperty( name = "stockNum", value = "库存数量" )
    private double stockNum = 0;

    @ApiModelProperty( name = "specificaName", value = "规格名称" )
    private String specificaName;

}
