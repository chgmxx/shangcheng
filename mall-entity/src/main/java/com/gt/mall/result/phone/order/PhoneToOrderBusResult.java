package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty( name = "memberDiscount", value = "会员折扣数" )
    private double memberDiscount = 0;

    @ApiModelProperty( name = "jifenNum", value = "会员拥有的积分数量" )
    private double jifenNum = 0;

    @ApiModelProperty( name = "jifenMoney", value = "积分数量抵扣的积分金额" )
    private double jifenMoney = 0;

    @ApiModelProperty( name = "fenbiNum", value = "粉币数量" )
    private double fenbiNum = 0;

    @ApiModelProperty( name = "fenbiMoney", value = "粉币数量抵扣的粉币金额" )
    private double fenbiMoney = 0;

    @ApiModelProperty( name = "memberPhone", value = "粉丝手机号码" )
    private String memberPhone;

    @ApiModelProperty( name = "duofenCardList", value = "多粉卡券集合" )
    private List< Map > duofenCardList;

    @ApiModelProperty( name = "wxCardList", value = "微信卡券集合" )
    private List< Map > wxCardList;

    @ApiModelProperty( name = "jifenFenbiRule", value = "积分粉币兑换规则" )
    private Map jifenFenbiRule;

}
