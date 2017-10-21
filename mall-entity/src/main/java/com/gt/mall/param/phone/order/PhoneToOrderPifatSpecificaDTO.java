package com.gt.mall.param.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 进入提交订单页面批发规格
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneToOrderPifatSpecificaDTO", description = "进入提交订单页面批发商品规格" )
@Getter
@Setter
public class PhoneToOrderPifatSpecificaDTO {

    @ApiModelProperty( name = "productId", value = "商品id" )
    private int productId;

    @ApiModelProperty( name = "specificaValueIds", value = "规格值id" )
    private String[] specificaValueIds;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

}
