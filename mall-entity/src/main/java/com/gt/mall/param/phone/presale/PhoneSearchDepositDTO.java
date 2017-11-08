package com.gt.mall.param.phone.presale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取交纳预收定金参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneSearchDeposit", description = "获取交纳预收定金参数" )
@Getter
@Setter
public class PhoneSearchDepositDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "proId", value = "商品ID", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer proId;

    @ApiModelProperty( name = "presaleId", value = "预售ID", required = true )
    @NotNull( message = "预售ID不能为空" )
    @Min( value = 1, message = "预售ID不能小于1" )
    private Integer presaleId;

    @ApiModelProperty( name = "invId", value = "库存ID", required = true )
    @NotNull( message = "库存ID不能为空" )
    @Min( value = 1, message = "库存ID不能小于1" )
    private Integer invId;

    @ApiModelProperty( name = "specId", value = "商品规格ID集合" )
    private String specId;

    @ApiModelProperty( name = "num", value = "数量" , required = true )
    private Integer num;

    @ApiModelProperty( name = "oMoney", value = "价格")
    private Integer oMoney;

}
