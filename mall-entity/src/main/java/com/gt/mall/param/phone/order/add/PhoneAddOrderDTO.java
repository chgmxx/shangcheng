package com.gt.mall.param.phone.order.add;

import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单参数
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddOrderResult", description = "提交订单参数" )
@Getter
@Setter
public class PhoneAddOrderDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "memberAddressDTO", value = "会员地址集合" )
    private PhoneOrderMemberAddressDTO memberAddressDTO;

    @ApiModelProperty( name = "totalMoney", value = "商品总额（优惠前的价格）" )
    private double totalMoney;

    @ApiModelProperty( name = "totalPayMoney", value = "合计（商品支付总价）" )
    private double totalPayMoney;

    @ApiModelProperty( name = "busResultList", value = "商家集合" )
    private List< PhoneAddOrderBusDTO > busResultList;

    @ApiModelProperty( name = "payWayList", value = "支付方式集合" )
    private List< PhoneOrderWayDTO > payWayList;

    @ApiModelProperty( name = "selectPayWayId", value = "选中的支付方式id" )
    private Integer selectPayWayId;

    @ApiModelProperty( name = "selectMemberAddressId", value = "会员地址id" )
    private Integer selectMemberAddressId;

}
