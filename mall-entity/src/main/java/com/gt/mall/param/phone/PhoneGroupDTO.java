package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分类验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneGroupDTO", description = "分类验证" )
@Data
public class PhoneGroupDTO {

    @ApiModelProperty( name = "shopId", value = "店铺ID，必传", required = true )
    @NotNull( message = "店铺ID不能为空" )
    private Integer shopId;

    @ApiModelProperty( name = "busId", value = "商家id，必传", required = true )
    @NotNull( message = "商家id不能为空" )
    @Min( value = 1, message = "商家id不能小于1" )
    private Integer busId;

    @ApiModelProperty( name = "groupId", value = "分类id" )
    private Integer groupId = 0;

}
