package com.gt.mall.param.phone.order.add;

import com.gt.mall.bean.member.Coupons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 店铺（提交订单参数）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddOrderShopResult", description = "店铺（提交订单参数）" )
@Getter
@Setter
public class PhoneAddOrderShopDTO {

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "wxShopId", value = "微信门店id" )
    private int wxShopId;

    //    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    //    private String shopName;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "商品总价（优惠前的价格，没包含运费）" )
    private double totalMoney;

    @ApiModelProperty( name = "totalFreightMoney", value = "运费" )
    private double totalFreightMoney = 0;

    @ApiModelProperty( name = "productResultList", value = "商品集合" )
    private List< PhoneAddOrderProductDTO > productResultList;

    @ApiModelProperty( name = "couponList", value = "卡券集合", hidden = true )
    private List< Coupons > couponList;

    /*********************************提交订单接口，传值***************************************/
    @ApiModelProperty( name = "selectCouponsId", value = "选中优惠券的id" )
    private Integer selectCouponsId = 0;

    @ApiModelProperty( name = "buyerMessage", value = "买家留言" )
    private String buyerMessage;

    @ApiModelProperty( name = "selectCouponsNum", value = "选中优惠券的数量", hidden = true )
    private Integer selectCouponsNum = 0;

    @ApiModelProperty( name = "selectCouponsType", value = "选中优惠券的类型 1 微信优惠券  2多粉优惠券", hidden = true )
    private Integer selectCouponsType = 0;

    @ApiModelProperty( name = "useFenbi", value = "订单使用粉币的数量", hidden = true )
    private double useFenbi = 0;

    @ApiModelProperty( name = "useJifen", value = "订单使用积分的数量", hidden = true )
    private double useJifen = 0;

    @ApiModelProperty( name = "totalNewMoney", value = "商品优惠后的总价（没包含运费）", hidden = true )
    private double totalNewMoney = 0;

    @ApiModelProperty( name = "fenbiYouhuiMoney", value = "粉币优惠金额", hidden = true )
    private double fenbiYouhuiMoney = 0;

    @ApiModelProperty( name = "jifenYouhuiMoney", value = "积分优惠金额", hidden = true )
    private double jifenYouhuiMoney = 0;

    @ApiModelProperty( name = "totalYouhuiMoney", value = "订单总共优惠的金额", hidden = true )
    private double totalYouhuiMoney = 0;

}
