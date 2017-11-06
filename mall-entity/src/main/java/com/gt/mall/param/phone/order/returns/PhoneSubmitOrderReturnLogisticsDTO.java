package com.gt.mall.param.phone.order.returns;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 提交退货物流验证
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneSubmitOrderReturnLogisticsDTO", description = "提交退货物流验证" )
@Getter
@Setter
public class PhoneSubmitOrderReturnLogisticsDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "退款id ", required = true )
    private Integer id;

    @ApiModelProperty( name = "wlCompanyId", value = "物流公司id", required = true )
    private Integer wlCompanyId;

    @ApiModelProperty( name = "wlCompany", value = "物流公司", required = true )
    private String wlCompany;

    @ApiModelProperty( name = "wlNo", value = "物流单号", required = true )
    private String wlNo;

    @ApiModelProperty( name = "wlTelephone", value = "填写物流信息的备注" )
    private String wlTelephone;

    @ApiModelProperty( name = "wlRemark", value = "退货物流说明" )
    private String wlRemark;

    @ApiModelProperty( name = "wlImagesUrl", value = "图片" )
    private String wlImagesUrl;
}
