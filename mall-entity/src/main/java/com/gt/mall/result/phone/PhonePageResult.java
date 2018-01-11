package com.gt.mall.result.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 商城首页返回值
 * User : yangqian
 * Date : 2017/12/14 0014
 * Time : 17:01
 */
@ApiModel( value = "PhonePageResult", description = "我的页面返回结果" )
@Getter
@Setter
public class PhonePageResult {

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "stoPicture", value = "店铺图片" )
    private String stoPicture;

    @ApiModelProperty( name = "stoName", value = "店铺名称" )
    private String stoName;

    @ApiModelProperty( name = "sharePicture", value = "分享需要的图片" )
    private String sharePicture;

    @ApiModelProperty( name = "shareTitle", value = "分享标题" )
    private String shareTitle;

    @ApiModelProperty( name = "pageName", value = "首页名称" )
    private String pageName;

    @ApiModelProperty( name = "dataJson", value = "样式json" )
    private String dataJson;

    @ApiModelProperty( name = "picJson", value = "图片数据json" )
    private String picJson;

    @ApiModelProperty( name = "countProduct", value = "商品总数" )
    private Integer countProduct;
}
