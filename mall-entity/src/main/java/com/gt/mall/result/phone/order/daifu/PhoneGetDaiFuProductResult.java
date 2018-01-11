package com.gt.mall.result.phone.order.daifu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * (商品)获取代付订单的返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneGetDaiFuProductResult", description = "(商品)获取代付订单的返回结果" )
@Getter
@Setter
public class PhoneGetDaiFuProductResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片地址" )
    private String productImageUrl;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private Integer productNum;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private Double productPrice;

}
