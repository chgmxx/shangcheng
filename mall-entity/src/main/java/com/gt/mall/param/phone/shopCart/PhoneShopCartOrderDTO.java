package com.gt.mall.param.phone.shopCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 去结算验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneShopCartOrderDTO", description = "去结算验证" )
@Data
public class PhoneShopCartOrderDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    @ApiModelProperty( name = "ids", value = "购物车id ", required = true )
    @NotNull( message = "购物车id不能为空" )
    @Min( value = 1, message = "购物车id不能小于1" )
    private int id;

    @ApiModelProperty( name = "productNum", value = "购物车数量 ", required = true )
    @NotNull( message = "购物车数量不能为空" )
    @Min( value = 1, message = "购物车数量不能小于1" )
    private int productNum = 1;

    @ApiModelProperty( name = "pifatSpecificaDTOList", value = "批发规格 ", required = true )
    private List< PhoneShopCartOrderPifatSpecificaDTO > pifatSpecificaDTOList;
}
