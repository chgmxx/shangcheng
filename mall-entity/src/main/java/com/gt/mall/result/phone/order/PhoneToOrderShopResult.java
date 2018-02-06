package com.gt.mall.result.phone.order;

import com.gt.mall.bean.member.Coupons;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 店铺（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderShopResult", description = "店铺（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderShopResult {

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private int shopId;

    @ApiModelProperty( name = "wxShopId", value = "微信门店id" )
    private int wxShopId;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "商品总价" )
    private double totalMoney;

    @ApiModelProperty( name = "totalFreightMoney", value = "运费" )
    private double totalFreightMoney = 0;

    @ApiModelProperty( name = "productResultList", value = "商品集合名称" )
    private List< PhoneToOrderProductResult > productResultList;

    @ApiModelProperty( name = "couponList", value = "卡券集合" )
    private List< Coupons > couponList;

    @ApiModelProperty( name = "isCanUseYhqDiscount", value = "是否能使用优惠券抵扣 1 能" )
    private Integer isCanUseYhqDiscount = 0;

    @ApiModelProperty( name = "isSelectCoupons", value = "是否选中了优惠券  1选中" )
    private Boolean isSelectCoupons = false;

    @ApiModelProperty( name = "selectCoupon", value = "选中的优惠券对象" )
    private Coupons selectCoupon;

    @ApiModelProperty( name = "selectCouponId", value = "选中的优惠券id" )
    private Integer selectCouponId;

}
