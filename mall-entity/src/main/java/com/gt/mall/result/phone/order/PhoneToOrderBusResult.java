package com.gt.mall.result.phone.order;

import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
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

    @ApiModelProperty( name = "deliveryWayList", value = "配送方式集合" )
    private List< PhoneOrderWayDTO > deliveryWayList;

    @ApiModelProperty( name = "toOrderShopResultList", value = "店铺集合" )
    private List< PhoneToOrderShopResult > shopResultList;

    @ApiModelProperty( name = "takeId", value = "上门自提id" )
    private Integer takeId;

    @ApiModelProperty( name = "takeAddress", value = "上门自提地址" )
    private String takeAddress;

    @ApiModelProperty( name = "memberDiscount", value = "会员折扣数" )
    private double memberDiscount = 0;

    @ApiModelProperty( name = "jifenNum", value = "会员拥有的积分数量" )
    private Double jifenNum;

    @ApiModelProperty( name = "jifenMoney", value = "积分数量抵扣的积分金额" )
    private Double jifenMoney;

    @ApiModelProperty( name = "fenbiNum", value = "粉币数量" )
    private Double fenbiNum;

    @ApiModelProperty( name = "fenbiMoney", value = "粉币数量抵扣的粉币金额" )
    private Double fenbiMoney;

    @ApiModelProperty( name = "jifenFenbiRule", value = "积分粉币兑换规则" )
    private JifenAndFenbiRule jifenFenbiRule;

    @ApiModelProperty( name = "memberPhone", value = "粉丝手机号码" )
    private String memberPhone;


    /*********************************联盟卡返回的接口***************************************/

    @ApiModelProperty( name = "unionStatus", value = "联盟状态 -1：没有联盟信息，不显示 0：没有关联联盟卡，需绑定联盟卡 1：返回联盟折扣" )
    private Integer unionStatus;

    @ApiModelProperty( name = "unionDiscount", value = "联盟折扣" )
    private Double unionDiscount;

    @ApiModelProperty( name = "unionId", value = "联盟id" )
    private Integer unionId;

    @ApiModelProperty( name = "unionCardId", value = "联盟卡id" )
    private Integer unionCardId;

}
