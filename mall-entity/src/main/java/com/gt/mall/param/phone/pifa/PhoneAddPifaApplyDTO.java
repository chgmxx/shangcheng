package com.gt.mall.param.phone.pifa;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 批发商申请参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddPifaApply", description = "批发商申请参数" )
@Getter
@Setter
public class PhoneAddPifaApplyDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "name", value = "姓名", required = true )
    @NotNull( message = "姓名不能为空" )
    private String name;

    @ApiModelProperty( name = "companyName", value = "公司名称", required = true )
    @NotNull( message = "公司名称ID不能为空" )
    private String companyName;

    @ApiModelProperty( name = "telephone", value = "手机号码", required = true )
    @NotNull( message = "手机号码不能为空" )
    private String telephone;

    @ApiModelProperty( name = "remark", value = "备注" )
    private String remark;

}
