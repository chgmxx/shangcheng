package com.gt.mall.result.applet.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 首页商品参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "AppletIndexProductDTO", description = "首页商品参数" )
@Getter
@Setter
public class AppletIndexProductDTO {
    @ApiModelProperty( name = "id", value = "商品ID" )
    private Integer id;

    @ApiModelProperty( name = "image_url", value = "商品图片" )
    private String image_url;

    @ApiModelProperty( name = "pro_label", value = "商品标签" )
    private String pro_label;

    @ApiModelProperty( name = "pro_name", value = "商品名称" )
    private String pro_name;

    @ApiModelProperty( name = "price_next", value = "商品价格" )
    private String pro_price;

    @ApiModelProperty( name = "price_first", value = "商品价格的小数点前面" )
    private String price_first;

    @ApiModelProperty( name = "price_next", value = "商品价格的小数点后面" )
    private String price_next;

    @ApiModelProperty( name = "pro_type_id", value = "商品类型 0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）" )
    private Integer pro_type_id;

    @ApiModelProperty( name = "sale_total", value = "总销量" )
    private Integer sale_total;

    @ApiModelProperty( name = "shop_id", value = "店铺ID" )
    private Integer shop_id;

}
