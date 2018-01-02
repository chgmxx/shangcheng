package com.gt.mall.result.phone.shopcart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车页面返回结果
 * User : yangqian
 * Date : 2017/10/18 0018
 * Time : 21:13
 */
@ApiModel( value = "PhoneShopCartListResult", description = "购物车页面返回结果" )
@Getter
@Setter
public class PhoneShopCartListResult {

    @ApiModelProperty( name = "userName", value = "商家名称（如果有公众号取公众号的名称，没有公众号直接去商家登陆名）" )
    private String userName;

    @ApiModelProperty( name = "userImageUrl", value = "商家头像（如果有公众号取公众号的头像，没有公众号给默认头像）" )
    private String userImageUrl;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "shopResultList", value = "购物车店铺集合" )
    private List< PhoneShopResult > shopResultList;

}
