package com.gt.mall.result.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 批发规格（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderPfSpecResult", description = "批发规格（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderPfSpecResult implements Serializable {

    private static final long serialVersionUID = 3450211057951003430L;

    @ApiModelProperty( name = "specificaIds", value = "批发规格id" )
    private String specificaIds;

    @ApiModelProperty( name = "specificaValues", value = "批发规格值 " )
    private String specificaValues;

    @ApiModelProperty( name = "specificaName", value = "规格名称" )
    private String specificaName;

    @ApiModelProperty( name = "pfPrice", value = "批发价" )
    private double pfPrice;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

}
