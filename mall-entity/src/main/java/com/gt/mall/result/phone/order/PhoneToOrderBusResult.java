package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 商家（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderBusResult", description = "商家（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderBusResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "busName", value = "商家名称" )
    private String busName;

    @ApiModelProperty( name = "publicId", value = "公众号id" )
    private Integer publicId;

    @ApiModelProperty( name = "busImageUrl", value = "商家头像" )
    private String busImageUrl;

    @ApiModelProperty( name = "productTotalMoney", value = "商品总价" )
    private double productTotalMoney;

    @ApiModelProperty( name = "productFreightMoney", value = "商品运费" )
    private double productFreightMoney;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "订单总价（包含运费）" )
    private double totalMoney;

    @ApiModelProperty( name = "payWay", value = "支付方式集合" )
    private List< PhoneToOrderWayResult > payWayList;

    @ApiModelProperty( name = "deliveryWayList", value = "配送方式集合" )
    private List< PhoneToOrderWayResult > deliveryWayList;

    @ApiModelProperty( name = "toOrderShopResultList", value = "店铺集合" )
    private List< PhoneToOrderShopResult > shopResultList;

    //积分数量

    //

    //粉币

}
