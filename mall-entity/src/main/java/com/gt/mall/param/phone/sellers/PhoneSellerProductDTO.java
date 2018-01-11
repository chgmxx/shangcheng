package com.gt.mall.param.phone.sellers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 自选商品参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneSellerProduct", description = "自选商品参数" )
@Getter
@Setter
public class PhoneSellerProductDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "ID" )
    private Integer id;

    @ApiModelProperty( name = "mallsetId", value = "商城设置id" )
    private Integer mallsetId;

    /*@ApiModelProperty( name = "saleMemberId", value = "销售员id", required = true )
    private Integer saleMemberId;

    @ApiModelProperty( name = "busUserId", value = "商家id", required = true )
    private Integer busUserId;*/

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

}
