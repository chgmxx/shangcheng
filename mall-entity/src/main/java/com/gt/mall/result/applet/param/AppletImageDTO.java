package com.gt.mall.result.applet.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序图片参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletGroupDTO", description = "小程序图片参数" )
@Getter
@Setter
public class AppletImageDTO {
    @ApiModelProperty( name = "proId", value = "商品ID" )
    private Integer proId;

    @ApiModelProperty( name = "imageUrl", value = "图片地址" )
    private String imageUrl;

    @ApiModelProperty( name = "type", value = "跳转类型 0不跳转 1商品详情页面 2团购 3秒杀 4拍卖 5预售列表" )
    private Integer type;

    @ApiModelProperty( name = "shopId", value = "店铺ID" )
    private Integer shopId;

}
