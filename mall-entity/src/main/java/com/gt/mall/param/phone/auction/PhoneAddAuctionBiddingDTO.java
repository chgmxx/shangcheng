package com.gt.mall.param.phone.auction;

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
@ApiModel( value = "PhoneAddAuctionBidding", description = "竞拍参数" )
@Getter
@Setter
public class PhoneAddAuctionBiddingDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "proId", value = "商品ID", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer proId;

    @ApiModelProperty( name = "aucId", value = "拍卖ID", required = true )
    @NotNull( message = "拍卖ID不能为空" )
    @Min( value = 1, message = "拍卖ID不能小于1" )
    private Integer aucId;

    @ApiModelProperty( name = "aucPrice", value = "中拍价格", required = true )
    @NotNull( message = "中拍价格不能为空" )
    @Min( value = 1, message = "中拍价格不能小于1" )
    private Integer aucPrice;

    @ApiModelProperty( name = "proName", value = "商品名称", required = true )
    private String proName;

    @ApiModelProperty( name = "proImgUrl", value = "商品图片", required = true )
    private String proImgUrl;

    @ApiModelProperty( name = "proSpecificaIds", value = "商品规格id  存放多个规格，用逗号(,)分隔", required = true )
    private String proSpecificaIds;

}
