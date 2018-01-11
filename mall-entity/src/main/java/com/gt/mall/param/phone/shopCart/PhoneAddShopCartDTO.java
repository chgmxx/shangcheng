package com.gt.mall.param.phone.shopCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 购物车验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneAddShopCartDTO", description = "购物车验证" )
@Data
public class PhoneAddShopCartDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    @ApiModelProperty( name = "busId", value = "商品id，必传", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "busId", value = "商家id，必传", required = true )
    @NotNull( message = "商家id不能为空" )
    private int busId = 0;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格值id" )
    private String productSpecificas = "";

    @ApiModelProperty( name = "invId", value = "库存id" )
    private int invId = 0;

    @ApiModelProperty( name = "proSpecStr", value = "批发规格 以json的格式传输{规格值id:{num:数量,value:规格值,price:价格},规格id:{num:数量,value:规格值,price:价格}}" )
    private String proSpecStr;

    @ApiModelProperty( name = "productNum", value = "商品数量，必传", required = true )
    @NotNull( message = "商品数量不能为空" )
    @Min( value = 1, message = "商品数量不能小于1" )
    private Integer productNum = 1;

    @ApiModelProperty( name = "price", value = "商品价格，必传", required = true )
    @NotNull( message = "商品价格不能为空" )
    @DecimalMin( value = "0.01", message = "商品价格不能小于0.01" )
    private double price = 0;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private int saleMemberId = 0;

    @ApiModelProperty( name = "userType", value = "买家类型 1 微信端买家  2 PC端买家" )
    private int userType = 1;

    @ApiModelProperty( name = "proType", value = "商品类型 0普通商品  1 批发商品(手批) 2 批发商品(混批)" )
    private int proType = 0;

    @ApiModelProperty( name = "commission", value = "商品佣金" )
    private double commission = 0;

}
