package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品详情所需参数
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneSpecificaDTO", description = "商品规格验证" )
@Data
public class PhoneSpecificaDTO implements Serializable {

    private static final long serialVersionUID = 5115370109194912313L;

    @ApiModelProperty( name = "productId", value = "商品ID，必传", required = true )
    @NotNull( message = "商品ID不能为空" )
    @Min( value = 1, message = "商品ID不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "invId", value = "库存id，可不传" )
    private int invId = 0;

    @ApiModelProperty( name = "type", value = "查看商品类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private int type = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "isShowCommission", value = "是否显示佣金 1显示" )
    private int isShowCommission = 0;

}
