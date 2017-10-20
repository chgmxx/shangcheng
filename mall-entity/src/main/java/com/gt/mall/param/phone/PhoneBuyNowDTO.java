package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分类验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneBuyNowDTO", description = "立即购买验证" )
@Data
public class PhoneBuyNowDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    @ApiModelProperty( name = "busId", value = "商品id，必传", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格值id" )
    private String productSpecificas = "";

    @ApiModelProperty( name = "invId", value = "库存id" )
    private int invId = 0;

    @ApiModelProperty( name = "productNum", value = "商品数量，必传", required = true )
    @NotNull( message = "商品数量不能为空" )
    @Min( value = 1, message = "商品数量不能小于1" )
    private Integer productNum = 1;

    @ApiModelProperty( name = "price", value = "商品价格，必传", required = true )
    @NotNull( message = "商品价格不能为空" )
    @DecimalMin( value = "0.01", message = "商品价格不能小于0.01" )
    private double price = 0;

    @ApiModelProperty( name = "price", value = "查看商品类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private int type = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private int activityId = 0;

    @ApiModelProperty( name = "joinActivityId", value = "参加活动id" )
    private int joinActivityId = 0;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private int saleMemberId = 0;

    @ApiModelProperty( name = "commission", value = "商品佣金" )
    private double commission = 0;

}
