package com.gt.mall.param.phone.sellers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 添加超级销售员参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddSellersResult", description = "添加超级销售员参数" )
@Getter
@Setter
public class PhoneAddSellersDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "userName", value = "姓名", required = true )
    private String userName;

    @ApiModelProperty( name = "companyName", value = "公司名称" )
    private String companyName;

    @ApiModelProperty( name = "telephone", value = "手机号码", required = true )
    private String telephone;

    @ApiModelProperty( name = "remark", value = "备注" )
    private String remark;

    @ApiModelProperty( name = "remark", value = "销售员id" )
    private Integer memberId;

}
