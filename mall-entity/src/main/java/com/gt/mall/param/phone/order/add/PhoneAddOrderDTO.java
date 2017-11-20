package com.gt.mall.param.phone.order.add;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty( name = "totalMoney", value = "商品总额（优惠前的价格）" )
    private double totalMoney;

    @ApiModelProperty( name = "totalPayMoney", value = "合计（商品支付总价）" )
    private double totalPayMoney;

    @ApiModelProperty( name = "busResultList", value = "商家集合" )
    private List< PhoneAddOrderBusDTO > busResultList;

    @ApiModelProperty( name = "selectPayWayId", value = "选中的支付方式id" )
    private Integer selectPayWayId;

    @ApiModelProperty( name = "selectMemberAddressId", value = "选中会员地址id" )
    private Integer selectMemberAddressId;

    @ApiModelProperty( name = "orderType", value = "订单类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer orderType;

    @ApiModelProperty( name = "flowPhone", value = "流量充值需要传的手机号码" )
    private String flowPhone;

    //    @ApiModelProperty( name = "unionCardId", value = "联盟卡id" )
    //    private Integer unionCardId;

    @ApiModelProperty( name = "shopCartIds", value = "购物车id，多个用逗号隔开" )
    private String shopCartIds;

    /******************************** 以下参数 不需要前端传值，用作后台计算和生成订单 ********************************/

    @ApiModelProperty( name = "wxShopIds", value = "门店id ，多个以逗号分割" )
    private String wxShopIds = "";

    @ApiModelProperty( name = "busIds", value = "商家id，多个以逗号分割" )
    private String busIds = "";

    @ApiModelProperty( name = "productMap", value = "商品集合", hidden = true )
    private Map< Integer,Object > productMap = new HashMap<>();

    @ApiModelProperty( name = "isCalculation", value = "是否计算", hidden = true )
    private boolean isCalculation = false;

}
