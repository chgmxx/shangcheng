package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 登陆授权参数
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneLoginDTO", description = "登陆授权参数验证" )
@Data
public class PhoneLoginDTO implements Serializable {

    private static final long serialVersionUID = 5115370109194912313L;

    @ApiModelProperty( name = "busId", value = "商家ID，必传", required = true )
    @NotNull( message = "商家ID不能为空" )
    @Min( value = 1, message = "商家ID不能小于1" )
    private Integer busId;

    @ApiModelProperty( name = "url", value = "当前页面地址，必传", required = true )
    @NotNull( message = "当前页面地址不能为空" )
    private String url;

    @ApiModelProperty( name = "browerType", value = "浏览器类型 1 微信 ， 99 浏览器", required = true )
    @NotNull( message = "浏览器类型不为空" )
    private int browerType;

    @ApiModelProperty( name = "ucLogin", value = "是否需要登陆 1不需要登陆 0 需要登陆", hidden = true )
    private Integer ucLogin = null;

}
