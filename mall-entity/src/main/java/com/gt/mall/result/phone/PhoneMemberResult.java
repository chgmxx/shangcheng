package com.gt.mall.result.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 我的页面返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "PhoneMemberResult", description = "我的页面返回结果" )
@Getter
@Setter
public class PhoneMemberResult {
    @ApiModelProperty( name = "busId", value = "商家id" )
    private int busId;

    @ApiModelProperty( name = "memberName", value = "会员名称" )
    private String memberName;

    @ApiModelProperty( name = "memberImageUrl", value = "会员头像" )
    private String memberImageUrl;

    @ApiModelProperty( name = "isOpenPf", value = "是否开启批发 1开启" )
    private int isOpenPf = 0;

    @ApiModelProperty( name = "isOpenSeller", value = "是否开启销售员" )
    private int isOpenSeller = 0;

    @ApiModelProperty( name = "pfStatus", value = "批发商状态 1审核成功，成为批发上 0 审核中（不允许点击） -1审核不通过 -2 还未申请" )
    private int pfStatus = -2;

    @ApiModelProperty( name = "pfErrorMsg", value = "不允许点击我的批发提示" )
    private String pfErrorMsg;

    @ApiModelProperty( name = "sellerStatus", value = "销售员状态 1 审核成功，成为销售员  0 审核中 -1审核不通过 -2、-4还未申请销售员" )
    private int sellerStatus = -2;

    @ApiModelProperty( name = "sellerErrorMsg", value = "不允许点击超级销售员提示" )
    private String sellerErrorMsg = "";

    @ApiModelProperty( name = "minCosumeMoney", value = "成为销售员最低消费的金额" )
    private double minCosumeMoney = 0;

    @ApiModelProperty( name = "consumeMoney", value = "粉丝消费的金额" )
    private double consumeMoney = 0;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private int saleMemberId = 0;

    @ApiModelProperty( name = "isLogin", value = "是否登陆" )
    private int isLogin = 0;

}
