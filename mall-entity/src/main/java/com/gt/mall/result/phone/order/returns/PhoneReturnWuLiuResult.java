package com.gt.mall.result.phone.order.returns;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 退货物流返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneReturnWuLiuResult", description = "退货物流返回结果" )
@Getter
@Setter
public class PhoneReturnWuLiuResult implements Serializable {

    private static final long serialVersionUID = -2250684076960876149L;

    @ApiModelProperty( name = "wlCompanyId", value = "物流公司id" )
    private Integer wlCompanyId;

    @ApiModelProperty( name = "wlCompany", value = "物流公司" )
    private String wlCompany;

    @ApiModelProperty( name = "wlNo", value = "物流单号" )
    private String wlNo;

    @ApiModelProperty( name = "wlTelephone", value = "物流手机号码" )
    private String wlTelephone;

    @ApiModelProperty( name = "wlRemark", value = "填写物流信息的备注" )
    private String wlRemark;

    @ApiModelProperty( name = "wlImagesUrl", value = "物流图片" )
    private String[] wlImagesUrl;

}
