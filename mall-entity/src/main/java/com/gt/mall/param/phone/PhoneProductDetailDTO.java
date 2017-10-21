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
@ApiModel( value = "PhoneProductDetailDTO", description = "商品详情验证" )
@Data
public class PhoneProductDetailDTO implements Serializable {

    private static final long serialVersionUID = 5115370109194912313L;

    @ApiModelProperty( name = "productId", value = "商品ID，必传", required = true )
    @NotNull( message = "商品ID不能为空" )
    @Min( value = 1, message = "商品ID不能小于1" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺ID，必传", required = true )
    @NotNull( message = "店铺ID不能为空" )
    @Min( value = 1, message = "店铺ID不能小于1" )
    private Integer shopId;

    @ApiModelProperty( name = "busId", value = "商家id，必传", required = true )
    @NotNull( message = "商家id不能为空" )
    @Min( value = 1, message = "商家id不能小于1" )
    private Integer busId;

    @ApiModelProperty( name = "loginDTO", value = "登陆参数 ", required = true )
    @NotNull( message = "登陆参数不为空" )
    private PhoneLoginDTO loginDTO;

    @ApiModelProperty( name = "toShop", value = "是否是到店购买 1到店" )
    private Integer toShop = 0;

    @ApiModelProperty( name = "type", value = "查看商品类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer type = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "longitude", value = "经度" )
    private double longitude = 0;

    @ApiModelProperty( name = "latitude", value = "纬度" )
    private double latitude = 0;

    @ApiModelProperty( name = "ip", value = "粉丝ip" )
    private String ip;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private int saleMemberId = 0;

    @ApiModelProperty( name = "view", value = "展示方式 show：预览" )
    private String view = "";

}
