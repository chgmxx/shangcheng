package com.gt.mall.result.phone.order;

import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty( name = "memberAddressDTO", value = "会员地址集合" )
    private PhoneOrderMemberAddressDTO memberAddressDTO;

    @ApiModelProperty( name = "totalMoney", value = "商品总额（优惠前的价格）" )
    private double totalMoney;

    @ApiModelProperty( name = "totalPayMoney", value = "合计（商品支付总价）" )
    private double totalPayMoney;

    @ApiModelProperty( name = "busResultList", value = "商家集合" )
    private List< PhoneToOrderBusResult > busResultList;

    @ApiModelProperty( name = "payWayList", value = "支付方式集合" )
    private List< PhoneOrderWayDTO > payWayList;

    @ApiModelProperty( name = "mallShopList", value = "店铺集合", hidden = true )
    private List< Map< String,Object > > mallShopList;

    @ApiModelProperty( name = "proTypeId", value = "商品类型id", hidden = true )
    private int proTypeId;

}
