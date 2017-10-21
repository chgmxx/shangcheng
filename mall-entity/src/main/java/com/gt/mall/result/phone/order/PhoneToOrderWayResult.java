package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付方式或配送方式（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderWayResult", description = "支付方式或配送方式（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderWayResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "way", value = "支付方式id" )
    private int way;

    @ApiModelProperty( name = "wayName", value = "支付方式名称" )
    private String wayName;

    @ApiModelProperty( name = "isDefault", value = "是否是默认" )
    private int isDefault;

}
