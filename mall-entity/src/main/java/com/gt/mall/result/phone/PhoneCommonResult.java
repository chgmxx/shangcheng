package com.gt.mall.result.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 技术支持和QQ
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "PhoneCommonResult", description = "技术支持和QQ" )
@Getter
@Setter
public class PhoneCommonResult {
    @ApiModelProperty( name = "isAdvert", value = "是否显示技术支持 1显示" )
    private int isAdvert = 0;

    @ApiModelProperty( name = "qq", value = "qq客服" )
    private String qq;

}
