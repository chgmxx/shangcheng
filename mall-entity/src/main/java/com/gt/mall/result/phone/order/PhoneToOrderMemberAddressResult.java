package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 会员地址
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderMemberAddressResult", description = "会员地址" )
@Getter
@Setter
public class PhoneToOrderMemberAddressResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "地址id" )
    private int id;

    @ApiModelProperty( name = "memberName", value = "收货人名称" )
    private String memberName;

    @ApiModelProperty( name = "memberPhone", value = "收货人手机号码" )
    private String memberPhone;

    @ApiModelProperty( name = "memberAddress", value = "收货人地址" )
    private String memberAddress;

}
