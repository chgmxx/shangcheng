package com.gt.mall.bean.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 多粉优惠券
 * User : yangqian
 * Date : 2017/10/25 0025
 * Time : 11:05
 */
@Getter
@Setter
public class Coupons {

    @ApiModelProperty( name = "id", value = "优惠券id" )
    private Integer id;

    @ApiModelProperty( name = "couponsFrom", value = "优惠券来源（ 1 微信优惠券  2多粉优惠券 ）" )
    private int couponsFrom = 0;

    @ApiModelProperty( name = "cardType", value = "卡券类型 0折扣券 1减免券" )
    private int cardType;

    @ApiModelProperty( name = "couponsName", value = "优惠券名称" )
    private String couponsName;

    @ApiModelProperty( name = "image", value = "优惠券图片" )
    private String image;

    /************************************* 以下是满减券需要用到的参数 *************************************/
    @ApiModelProperty( name = "cashLeastCost", value = "起用金额（满多少）" )
    private double cashLeastCost = 0;

    @ApiModelProperty( name = "reduceCost", value = "减免金额（减多少）" )
    private double reduceCost = 0;

    @ApiModelProperty( name = "addUser", value = "是否允许叠加 0不允许 1已允许" )
    private int addUser = 0;

    @ApiModelProperty( name = "couponNum", value = "优惠券数量 允许叠加才显示" )
    private int couponNum = 1;

    /************************************* 以下是折扣券需要用到的参数 *************************************/
    @ApiModelProperty( name = "discount", value = "折扣券的折扣" )
    private double discount;

}
