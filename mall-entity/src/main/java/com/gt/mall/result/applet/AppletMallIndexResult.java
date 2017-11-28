package com.gt.mall.result.applet;

import com.gt.mall.result.applet.param.AppletGroupDTO;
import com.gt.mall.result.applet.param.AppletImageDTO;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商城首页数据列表
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletMallIndexResult", description = "商城首页数据列表" )
@Getter
@Setter
public class AppletMallIndexResult {
    @ApiModelProperty( name = "firstList", value = "商品一级分类列表" )
    private List< AppletGroupDTO > firstList;

    @ApiModelProperty( name = "imageList", value = "轮播图列表" )
    private List< AppletImageDTO > imageList;

    @ApiModelProperty( name = "productPage", value = "商品分页列表" )
    private PageUtil productPage;

    @ApiModelProperty( name = "imageHttp", value = "访问资源URL" )
    private String imageHttp;

    @ApiModelProperty( name = "isShowActivity", value = "是否有活动  0没有活动  1有活动" )
    private Integer isShowActivity;

    @ApiModelProperty( name = "is_group", value = "是否有正在进行的团购商品 1有" )
    private Integer is_group;

    @ApiModelProperty( name = "is_seckill", value = "是否有正在进行的秒杀商品 1有" )
    private Integer is_seckill;

    @ApiModelProperty( name = "is_auction", value = "是否有正在进行的拍卖商品 1有" )
    private Integer is_auction;

    @ApiModelProperty( name = "is_presale", value = "是否有正在进行的预售商品 1有" )
    private Integer is_presale;

}
