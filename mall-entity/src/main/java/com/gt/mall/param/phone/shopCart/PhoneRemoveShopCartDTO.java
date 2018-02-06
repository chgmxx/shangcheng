package com.gt.mall.param.phone.shopCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 删除购物车验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneRemoveShopCartDTO", description = "删除购物车验证" )
@Data
public class PhoneRemoveShopCartDTO {

    @ApiModelProperty( name = "ids", value = "购物车id,多个用逗号隔开", required = true )
    @NotNull( message = "删除购物车id不能为空" )
    private String ids;

    @ApiModelProperty( name = "pifaSpecificaList", value = "批发规格集合", required = true )
    private List< PhoneRemovePifatSpecificaDTO > pifaSpecificaList;

}
