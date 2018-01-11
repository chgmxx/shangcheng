package com.gt.mall.param.phone.shopCart;

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
@ApiModel( value = "PhoneRemovePifatSpecificaDTO", description = "删除批发商品规格" )
@Getter
@Setter
public class PhoneRemovePifatSpecificaDTO {

    @ApiModelProperty( name = "id", value = "购物车id" )
    private int id;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private int productId;

    @ApiModelProperty( name = "specificaValueIds", value = "规格值id" )
    private String[] specificaValueIds;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

}
