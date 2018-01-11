package com.gt.mall.result.applet;

import com.gt.mall.result.applet.param.AppletShopCartProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletShopCartResult", description = "购物车参数" )
@Getter
@Setter
public class AppletShopCartResult {

    @ApiModelProperty( name = "pro_type", value = " 商品类型 0普通商品  1 批发商品(手批) 2 批发商品(混批)" )
    private Integer pro_type;

    @ApiModelProperty( name = "sto_image", value = "店铺图片" )
    private String sto_image;

    @ApiModelProperty( name = "sto_name", value = "店铺名称" )
    private String sto_name;

    @ApiModelProperty( name = "pageid", value = "页面ID" )
    private Integer pageid;

    @ApiModelProperty( name = "shop_id", value = "店铺ID" )
    private Integer shop_id;

    @ApiModelProperty( name = "proList", value = "商品列表" )
    private List< AppletShopCartProductDTO > proList;

}
