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
public class PhoneUrlResult {

    @ApiModelProperty( name = "memberCenterUrl", value = "会员中心地址" )
    private String memberCenterUrl;

    @ApiModelProperty( name = "memberCouponUrl", value = "会员优惠券地址" )
    private String memberCouponUrl;

}
