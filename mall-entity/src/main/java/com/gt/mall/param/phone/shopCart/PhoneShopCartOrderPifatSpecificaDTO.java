package com.gt.mall.param.phone.shopCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 结算接口批发规格
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneShopCartOrderPifatSpecificaDTO", description = "删除批发商品规格" )
@Getter
@Setter
public class PhoneShopCartOrderPifatSpecificaDTO {

    @ApiModelProperty( name = "specificaValueIds", value = "规格值id" )
    private String[] specificaValueIds;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

}
