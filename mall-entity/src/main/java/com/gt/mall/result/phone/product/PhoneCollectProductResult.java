package com.gt.mall.result.phone.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 收藏商品返回结果
 * User : yangqian
 * Date : 2017/11/02
 * Time : 18:11
 */
@ApiModel( value = "PhoneCollectProductResult", description = "收藏商品返回结果" )
@Getter
@Setter
public class PhoneCollectProductResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    @ApiModelProperty( name = "id", value = "收藏id" )
    private Integer id;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private double productPrice = 0;

    @ApiModelProperty( name = "productMemberPrice", value = "会员价格" )
    private double productMemberPrice = 0;

    @ApiModelProperty( name = "labelName", value = "标签名称" )
    private String labelName;
}
