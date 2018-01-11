package com.gt.mall.result.applet.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 购物车商品参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletShopCartProductDTO", description = "购物车商品参数" )
@Getter
@Setter
public class AppletShopCartProductDTO {
    @ApiModelProperty( name = "id", value = "购物车ID" )
    private Integer id;

    @ApiModelProperty( name = "product_id", value = "商品ID" )
    private Integer product_id;

    @ApiModelProperty( name = "image_url", value = "商品图片" )
    private String image_url;

    @ApiModelProperty( name = "product_speciname", value = "商品规格" )
    private String product_speciname;

    @ApiModelProperty( name = "product_name", value = "商品名称" )
    private String product_name;

    @ApiModelProperty( name = "pro_stock_num", value = "库存总数" )
    private Integer pro_stock_num;

    @ApiModelProperty( name = "product_num", value = "购买商品数量 单价" )
    private Integer product_num;

    @ApiModelProperty( name = "price", value = "价格" )
    private String price;

    @ApiModelProperty( name = "hyprice", value = "会员价格" )
    private String hyprice;

    @ApiModelProperty( name = "pro_type_id", value = "商品类型 0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）" )
    private Integer pro_type_id;

    @ApiModelProperty( name = "shop_id", value = "店铺ID" )
    private Integer shop_id;

    @ApiModelProperty( name = "sto_name", value = "店铺名称" )
    private String sto_name;

    @ApiModelProperty( name = "maxBuy", value = "最多还可购买数量" )
    private Integer maxBuy;

    @ApiModelProperty( name = "pfType", value = "是否批发商品  1是" )
    private Integer pfType;

    @ApiModelProperty( name = "msg", value = "说明" )
    private String msg;

    @ApiModelProperty( name = "code", value = "状态" )
    private Integer code;

}
