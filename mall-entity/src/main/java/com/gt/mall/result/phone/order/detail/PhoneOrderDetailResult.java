package com.gt.mall.result.phone.order.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 订单详情（进入提交列表返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneOrderDetailResult", description = "订单详情（进入提交列表返回结果）" )
@Getter
@Setter
public class PhoneOrderDetailResult implements Serializable {

    private static final long serialVersionUID = 6685362436611404285L;

    @ApiModelProperty( name = "orderDetailId", value = "订单详情id" )
    private Integer orderDetailId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productNum", value = "商品数量" )
    private Integer productNum;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private Double productPrice;

    @ApiModelProperty( name = "productSpecificaValue", value = "商品规格值" )
    private String productSpecificaValue;

    @ApiModelProperty( name = "detailStauts", value = "订单详情状态" )
    private Integer detailStauts;

    @ApiModelProperty( name = "isShowApplyReturnButton", value = "是否显示申请退款按钮 1显示" )
    private Integer isShowApplyReturnButton = 0;

    @ApiModelProperty( name = "isShowCommentButton", value = "是否显示评论按钮  1显示" )
    private Integer isShowCommentButton = 0;

    @ApiModelProperty( name = "isShowReturnWuLiuButton", value = "是否显示退货物流的按钮 1显示" )
    private Integer isShowReturnWuLiuButton = 0;

    @ApiModelProperty( name = "isShowUpdateReturnButton", value = "是否显示修改退款的按钮 1显示" )
    private Integer isShowUpdateReturnButton = 0;

    @ApiModelProperty( name = "isShowCloseReturnButton", value = "是否显示撤销退款的按钮 1显示" )
    private Integer isShowCloseReturnButton = 0;

    @ApiModelProperty( name = "isShowApplySaleButton", value = "是否显示售后的按钮 1显示" )
    private Integer isShowApplySaleButton = 0;

    @ApiModelProperty( name = "returnId", value = "退款id" )
    private Integer returnId = 0;

}
