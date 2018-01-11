package com.gt.mall.param.phone.order.add;

import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.param.phone.order.PhoneOrderPifaSpecDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 商品（提交订单参数）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddOrderProductResult", description = "商品（提交订单参数）" )
@Getter
@Setter
public class PhoneAddOrderProductDTO implements Serializable {

    private static final long serialVersionUID = 3450211057951003430L;


    @ApiModelProperty( name = "orderDetailId", value = "订单详情id" )
    private Integer orderDetailId = 0;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private int productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productSpecificaValueId", value = "规格值id" )
    private String productSpecificaValueId;

    @ApiModelProperty( name = "productSpecificaValue", value = "商品规格值" )
    private String productSpecificaValue;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private double productPrice;

    @ApiModelProperty( name = "productOldPrice", value = "商品原价" )
    private double productOldPrice;

    @ApiModelProperty( name = "totalPrice", value = "商品总价（商品价格*数量）" )
    private double totalPrice;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private int productNum;

    @ApiModelProperty( name = "pfSpecResultList", value = "批发规格集合（批发商品  且 有规格则必传）" )
    private List< PhoneOrderPifaSpecDTO > pfSpecResultList;

    @ApiModelProperty( name = "activityId", value = "活动id(如果是活动商品，必传)" )
    private Integer activityId;

    @ApiModelProperty( name = "activityType", value = "活动类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer activityType;

    @ApiModelProperty( name = "groupJoinId", value = "参团id" )
    private Integer groupJoinId = 0;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private Integer saleMemberId;

    @ApiModelProperty( name = "commission", value = "销售佣金" )
    private Double commission;

    /******************************** 以下参数 不需要前端传值，用作后台计算和生成订单 ********************************/
    @ApiModelProperty( name = "productWeight", value = "商品重量", hidden = true )
    private double productWeight = 0;

    @ApiModelProperty( name = "cardReceiveId", value = "卡券id", hidden = true )
    private Integer cardReceiveId;

    //是否能使用积分
    @ApiModelProperty( name = "isCanUseJifen", value = "是否能使用积分1 能使用 0不能使用", hidden = true )
    private Integer isCanUseJifen = 0;

    //是否能使用粉币
    @ApiModelProperty( name = "isCanUseFenbi", value = "是否能使用粉币 1能使用", hidden = true )
    private Integer isCanUseFenbi = 0;

    //是否能使用优惠券
    @ApiModelProperty( name = "isCanUseYhq", value = "是否能使用优惠券 1能使用", hidden = true )
    private Integer isCanUseYhq = 0;

    //是否能使用会员折扣
    @ApiModelProperty( name = "isCanUseDiscount", value = "是否能使用会员折扣 1能使用", hidden = true )
    private Integer isCanUseDiscount = 0;

    @ApiModelProperty( name = "proTypeId", value = "商品类型  0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）", hidden = true )
    private Integer proTypeId = 0;

    @ApiModelProperty( name = "productYouhuiPrice", value = "商品优惠的价格", hidden = true )
    private Double productYouhuiPrice;

    @ApiModelProperty( name = "productNewTotalPrice", value = "商品最新的总价", hidden = true )
    private Double productNewTotalPrice;

    //以下参数 是计算优惠的参数
    @ApiModelProperty( name = "useUnionDiscountYouhuiPrice", value = "使用联盟卡优惠的价格", hidden = true )
    private double useUnionDiscountYouhuiPrice = 0;

    @ApiModelProperty( name = "useDiscountYouhuiPrice", value = "使用会员卡优惠的价格", hidden = true )
    private double useDiscountYouhuiPrice = 0;

    @ApiModelProperty( name = "useCouponYouhuiPrice", value = "使用优惠券优惠的价格", hidden = true )
    private double useCouponYouhuiPrice = 0;

    @ApiModelProperty( name = "useFenbiNum", value = "使用粉币的数量", hidden = true )
    private double useFenbiNum = 0;

    @ApiModelProperty( name = "useFenbiYouhuiPrice", value = "使用粉币优惠的价格", hidden = true )
    private double useFenbiYouhuiPrice = 0;

    @ApiModelProperty( name = "useJifenNum", value = "使用积分的数量", hidden = true )
    private double useJifenNum = 0;

    @ApiModelProperty( name = "useJifenYouhuiPrice", value = "使用积分优惠的价格", hidden = true )
    private double useJifenYouhuiPrice = 0;

    @ApiModelProperty( name = "product", value = "商品对象", hidden = true )
    private MallProduct product;

    @ApiModelProperty( name = "useUnionDiscount", value = "是否已经使用了联盟折扣", hidden = true )
    private boolean useUnionDiscount = false;

    @ApiModelProperty( name = "selectCouponsType", value = "选中优惠券的类型 1 微信优惠券  2多粉优惠券", hidden = true )
    private Integer selectCouponsType = 0;

    @ApiModelProperty( name = "selectCouponsId", value = "选中优惠券的id" )
    private Integer selectCouponsId = 0;

}
