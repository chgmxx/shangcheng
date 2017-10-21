package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 进入提交订单页面返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderResult", description = "进入提交订单页面返回结果" )
@Getter
@Setter
public class PhoneToOrderResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "memberAddressResult", value = "会员地址集合" )
    private PhoneToOrderMemberAddressResult memberAddressResult;

    @ApiModelProperty( name = "totalMoney", value = "商品总额（优惠前的价格）" )
    private double totalMoney;

    @ApiModelProperty( name = "totalDiscountMoney", value = "商品优惠总价" )
    private double totalDiscountMoney;

    @ApiModelProperty( name = "totalPayMoney", value = "合计（商品支付总价）" )
    private double totalPayMoney;

    @ApiModelProperty( name = "memberAddressResult", value = "商家集合" )
    private List< PhoneToOrderBusResult > busResultList;

}
