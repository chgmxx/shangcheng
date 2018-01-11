package com.gt.mall.result.applet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 店铺列表
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletShopResult", description = "店铺列表" )
@Getter
@Setter
public class AppletShopResult {
    @ApiModelProperty( name = "raill", value = "距离 -1不显示" )
    private String raill;

    @ApiModelProperty( name = "shopImage", value = "店铺图片" )
    private String shopImage;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "shopAddress", value = "店铺地址" )
    private String shopAddress;

    @ApiModelProperty( name = "telephone", value = "店铺联系电话" )
    private String telephone;

    @ApiModelProperty( name = "id", value = "店铺ID" )
    private Integer id;

}
