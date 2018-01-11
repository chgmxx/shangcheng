package com.gt.mall.result.phone.shopcart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车页面店铺返回结果
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneShopResult", description = "购物车页面店铺返回结果" )
@Getter
@Setter
public class PhoneShopResult {

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "productResultList", value = "商品集合" )
    private List< PhoneProductResult > productResultList;

}
