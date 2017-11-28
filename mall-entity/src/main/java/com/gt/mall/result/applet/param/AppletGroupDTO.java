package com.gt.mall.result.applet.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分类参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletGroupDTO", description = "分类参数" )
@Getter
@Setter
public class AppletGroupDTO {
    @ApiModelProperty( name = "group_id", value = "分类ID" )
    private Integer group_id;

    @ApiModelProperty( name = "group_name", value = "分类名称" )
    private String group_name;

    @ApiModelProperty( name = "is_child", value = "是否有子类  1有 0无" )
    private Integer is_child;

    @ApiModelProperty( name = "shop_id", value = "店铺ID" )
    private Integer shop_id;

    @ApiModelProperty( name = "image_url", value = "图片" )
    private String image_url;

}
