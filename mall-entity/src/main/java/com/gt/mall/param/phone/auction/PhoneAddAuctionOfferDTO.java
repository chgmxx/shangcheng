package com.gt.mall.param.phone.auction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 拍卖出价参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddAuctionOffer", description = "拍卖出价参数" )
@Getter
@Setter
public class PhoneAddAuctionOfferDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "proId", value = "商品ID", required = true )
    @NotNull( message = "商品id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer proId;

    @ApiModelProperty( name = "aucId", value = "拍卖ID", required = true )
    @NotNull( message = "拍卖ID不能为空" )
    @Min( value = 1, message = "拍卖ID不能小于1" )
    private Integer aucId;

    @ApiModelProperty( name = "offerMoney", value = "出价金额", required = true )
    @NotNull( message = "出价金额不能为空" )
    @Min( value = 1, message = "出价金额不能小于1" )
    private Integer offerMoney;

}
