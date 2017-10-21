package com.gt.mall.param.phone.order;

import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.shopCart.PhoneShopCartOrderPifatSpecificaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 进入提交订单验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneToOrderDTO", description = "进入提交订单证" )
@Data
public class PhoneToOrderDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;

    @ApiModelProperty( name = "busId", value = "来源 1 购物车结算进入 0 立即购买进入，必传", required = true )
    @NotNull( message = "来源不能为空" )
    @Min( value = 0, message = "商品id不能小于0" )
    private Integer from = 0;

    @ApiModelProperty( name = "type", value = "商品类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer type = 0;

    @ApiModelProperty( name = "busId", value = "购物车id，多个以逗号隔开（从购物车结算进入，必须传此参数）" )
    private String cartIds;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private Integer productNum;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格值id" )
    private String productSpecificas;

    @ApiModelProperty( name = "productPrice", value = "商品价" )
    private double productPrice;

    @ApiModelProperty( name = "pufaSpecificaDTOList", value = "批发规格" )
    private List< PhoneShopCartOrderPifatSpecificaDTO > pufaSpecificaDTOList;

    @ApiModelProperty( name = "loginDTO", value = "登陆参数 ", required = true )
    @NotNull( message = "登陆参数不为空" )
    private PhoneLoginDTO loginDTO;

}
