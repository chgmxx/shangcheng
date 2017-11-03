package com.gt.mall.param.phone.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 兑换商品参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddIntegral", description = "兑换商品参数" )
@Getter
@Setter
public class PhoneAddIntegralDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productId", value = "商品ID", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "integralId", value = "积分商品ID", required = true )
    @NotNull( message = "积分商品ID不能为空" )
    @Min( value = 1, message = "积分商品ID不能小于1" )
    private Integer integralId;

    @ApiModelProperty( name = "productNum", value = "兑换数量", required = true )
    @NotNull( message = "兑换数量不能为空" )
    @Min( value = 1, message = "兑换数量不能小于1" )
    private Integer productNum;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格ID集合" )
    private String productSpecificas;

    @ApiModelProperty( name = "flowPhone", value = "手机号码" )
    private Integer flowPhone;

}
