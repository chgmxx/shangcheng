package com.gt.mall.param.phone.order;

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

    @ApiModelProperty( name = "busId", value = "来源 0 立即购买进入 1 购物车结算进入 2 从订单列表去购买，必传", required = true )
    @NotNull( message = "来源不能为空" )
    @Min( value = 0, message = "商品id不能小于0" )
    private Integer from = 0;

    @ApiModelProperty( name = "type", value = "活动类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer type = 0;

    @ApiModelProperty( name = "busId", value = "购物车id，多个以逗号隔开（从购物车结算进入，必须传此参数）" )
    private String cartIds;

    @ApiModelProperty( name = "productId", value = "商品id（当立即购买进入，必传此参数）" )
    private Integer productId;

    @ApiModelProperty( name = "productNum", value = "商品数量（当立即购买进入，必传此参数）" )
    private Integer productNum;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格值id" )
    private String productSpecificas;

    @ApiModelProperty( name = "productPrice", value = "商品价（当立即购买进入，必传此参数）" )
    private double productPrice;

    @ApiModelProperty( name = "pifaSpecificaDTOList", value = "批发规格（当立即购买进入 且批发商品，必传此参数）" )
    private List< PhoneToOrderPifatSpecificaDTO > pifaSpecificaDTOList;

    @ApiModelProperty( name = "toShop", value = "是否是到店购买 1到店购买 0 线上" )
    private Integer toShop = 0;

    @ApiModelProperty( name = "ip", value = "粉丝的ip" )
    private String ip;

    @ApiModelProperty( name = "longitude", value = "经度" )
    private double longitude = 0;

    @ApiModelProperty( name = "langitude", value = "纬度" )
    private double langitude = 0;

    @ApiModelProperty( name = "memberAddressId", value = "会员地址id" )
    private int memberAddressId;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId;
    
    @ApiModelProperty( name = "groupJoinId", value = "参团id" )
    private Integer groupJoinId;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

}
