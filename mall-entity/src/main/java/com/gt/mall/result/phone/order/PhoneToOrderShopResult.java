package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderShopResult", description = "店铺（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderShopResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;
    ;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "商品总价" )
    private double totalMoney;

    @ApiModelProperty( name = "productResultList", value = "商品集合名称" )
    private List< PhoneToOrderProductResult > productResultList;

    //优惠券集合

}
