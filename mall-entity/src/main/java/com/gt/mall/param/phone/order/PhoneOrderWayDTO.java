package com.gt.mall.param.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 支付方式或配送方式
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneOrderWayDTO", description = "支付方式或配送方式（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneOrderWayDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "支付方式id" )
    private int id;

    @ApiModelProperty( name = "wayName", value = "支付方式名称" )
    private String wayName;

    @ApiModelProperty( name = "claName", value = "class名称" )
    private String claName;

    public PhoneOrderWayDTO( int id, String wayName ,String claName) {
	this.id = id;
	this.wayName = wayName;
	this.claName = claName;
    }

    public PhoneOrderWayDTO() {
    }
}
