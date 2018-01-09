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
    private double totalMoney = 0;

    @ApiModelProperty( name = "totalPayMoney", value = "合计（商品支付总价）" )
    private double totalPayMoney = 0;

    @ApiModelProperty( name = "totalYouHuiMoney", value = "总优惠" )
    private double totalYouHuiMoney = 0;

    @ApiModelProperty( name = "busResultList", value = "商家集合" )
    private List< PhoneToOrderBusResult > busResultList;

    @ApiModelProperty( name = "payWayList", value = "支付方式集合" )
    private List< PhoneOrderWayDTO > payWayList;

    @ApiModelProperty( name = "mallShopList", value = "店铺集合", hidden = true )
    private List< Map< String,Object > > mallShopList;

    @ApiModelProperty( name = "proTypeId", value = "商品类型id", hidden = true )
    private int proTypeId;

    @ApiModelProperty( name = "type", value = "活动类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer type = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "joinActivityId", value = "参加活动id" )
    private int joinActivityId = 0;

    @ApiModelProperty( name = "selectPayWayId", value = "选中的支付方式id" )
    private Integer selectPayWayId;

    @ApiModelProperty( name = "selectPayWay", value = "默认选中的支付方式" )
    private PhoneOrderWayDTO selectPayWay;

    @ApiModelProperty( name = "memberPhone", value = "会员手机号码" )
    private String memberPhone;

}
