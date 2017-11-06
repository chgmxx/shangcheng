package com.gt.mall.param.phone.order.returns;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 评论
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneSubmitOrderReturnDTO", description = "评论" )
@Getter
@Setter
public class PhoneSubmitOrderReturnDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "退款id，如果修改退款必填" )
    private Integer id;

    @ApiModelProperty( name = "orderId", value = "订单id", required = true )
    private Integer orderId;

    @ApiModelProperty( name = "orderDetailId", value = "订单详情id", required = true )
    private Integer orderDetailId;

    @ApiModelProperty( name = "shopId", value = "店铺id", required = true )
    private Integer shopId;

    @ApiModelProperty( name = "cargoStatus", value = "货物状态 0 未收到货 1已收到货  -1 未填写" )
    private Integer cargoStatus = -1;

    @ApiModelProperty( name = "retHandlingWay", value = "处理方式", required = true )
    private Integer retHandlingWay;

    @ApiModelProperty( name = "retReasonId", value = "退款原因id", required = true )
    private Integer retReasonId;

    @ApiModelProperty( name = "retReason", value = "退款原因", required = true )
    private String retReason;

    @ApiModelProperty( name = "retRemark", value = "退款说明" )
    private String retRemark;

    @ApiModelProperty( name = "imageUrls", value = "图片地址,多张图片用逗号隔开" )
    private String imageUrls;

    @ApiModelProperty( name = "retMoney", value = "退款金额", required = true )
    private Double retMoney;

    @ApiModelProperty( name = "retTelephone", value = "退货手机号码" )
    private String retTelephone;

}
