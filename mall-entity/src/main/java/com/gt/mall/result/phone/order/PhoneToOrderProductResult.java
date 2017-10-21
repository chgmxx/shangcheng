package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderProductResult", description = "商品（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderProductResult implements Serializable {

    private static final long serialVersionUID = 3450211057951003430L;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private int productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productSpecificaValue", value = "商品规格值" )
    private String productSpecificaValue;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private double productPrice;

    @ApiModelProperty( name = "productOldPrice", value = "商品原价" )
    private double productOldPrice;

    @ApiModelProperty( name = "productHyPrice", value = "商品会员价" )
    private double productHyPrice;
    
    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

    //是否使用积分

    //是否使用粉币

    //是否使用优惠券

    //是否使用

}
