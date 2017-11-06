package com.gt.mall.result.phone.order.returns;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 退款方式
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneReturnWayResult", description = "退款方式" )
@Getter
@Setter
public class PhoneReturnWayResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "退款方式id" )
    private Integer id;

    @ApiModelProperty( name = "value", value = "方式名称" )
    private String value;

    public PhoneReturnWayResult( Integer id, String value ) {
	this.id = id;
	this.value = value;
    }
}
