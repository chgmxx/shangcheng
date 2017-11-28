package com.gt.mall.result.applet;

import com.gt.mall.entity.product.MallProductParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 商品详细信息参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletProductDetailResult", description = "商品详细信息参数" )
@Getter
@Setter
public class AppletProductDetailResult {
    @ApiModelProperty( name = "product_name", value = "商品名称" )
    private String product_name;

    @ApiModelProperty( name = "product_sale", value = "商品销量" )
    private Integer product_sale;

    @ApiModelProperty( name = "product_code", value = "商家编码" )
    private String product_code;

    @ApiModelProperty( name = "product_label", value = "商品标签" )
    private String product_label;

    @ApiModelProperty( name = "product_type_id", value = " 商品类型 0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）" )
    private Integer product_type_id;

    @ApiModelProperty( name = "isNoShow", value = "是否显示商品详细页面 1不显示" )
    private Integer isNoShow;

    @ApiModelProperty( name = "product_price", value = "商品价格" )
    private Double product_price;

    @ApiModelProperty( name = "product_cost_price", value = "商品原价" )
    private Double product_cost_price;

    @ApiModelProperty( name = "member_price", value = "会员价" )
    private Double member_price;

    @ApiModelProperty( name = "wholesale_price", value = "批发价" )
    private Double wholesale_price;

    @ApiModelProperty( name = "freightPrice", value = "运费" )
    private Double freightPrice;

    @ApiModelProperty( name = "address", value = "收货地址" )
    private String address;

    @ApiModelProperty( name = "address_id", value = "收货地址ID" )
    private Integer address_id;

    @ApiModelProperty( name = "shopImage", value = "店铺图片" )
    private String shopImage;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "product_detail", value = "商品详情" )
    private String product_detail;

    @ApiModelProperty( name = "product_message", value = "商品信息" )
    private String product_message;

    @ApiModelProperty( name = "product_message_sub", value = "商品信息 缩略信息" )
    private String product_message_sub;

    @ApiModelProperty( name = "shopCartNum", value = "购物车数量" )
    private Integer shopCartNum;

    @ApiModelProperty( name = "specificaList", value = "商品规格值" )
    private List< Map< String,Object > > specificaList;

    @ApiModelProperty( name = "guigePriceList", value = "所有规格信息" )
    private List< Map< String,Object > > guigePriceList;

    @ApiModelProperty( name = "defaultInvMap", value = "商品默认的规格价" )
    private Map< String,Object > defaultInvMap;

    @ApiModelProperty( name = "imagelist", value = "商品图片列表" )
    private List< Map< String,Object > > imagelist;

    @ApiModelProperty( name = "paramList", value = "商品参数列表" )
    private List< MallProductParam > paramList;

    @ApiModelProperty( name = "memberList", value = "用户列表" )
    private Integer memberList;

}
