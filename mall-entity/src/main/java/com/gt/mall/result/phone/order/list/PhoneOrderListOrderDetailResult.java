package com.gt.mall.result.phone.order.list;

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
@ApiModel( value = "PhoneOrderListOrderDetailResult", description = "订单详情（进入提交列表返回结果）" )
@Getter
@Setter
public class PhoneOrderListOrderDetailResult implements Serializable {

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

    @ApiModelProperty( name = "returnId", value = "退款id" )
    private Integer returnId = 0;

    /******************  退款售后页面用到的参数  ************************/

    @ApiModelProperty( name = "returnMoney", value = "退款金额" )
    private double returnMoney = 0;

    @ApiModelProperty( name = "returnType", value = "退款类型 1退款  2退款退货" )
    private int returnType = -1;

    @ApiModelProperty( name = "returnTypeDesc", value = "退款类型说明" )
    private String returnTypeDesc;

    @ApiModelProperty( name = "returnStatusDesc", value = "退款状态说明" )
    private String returnStatusDesc;

    @ApiModelProperty( name = "isShowReturnWuliuButton", value = "是否显示退货物流消息 1显示" )
    private Integer isShowReturnWuliuButton = 0;

    @ApiModelProperty( name = "isShowUpdateReturnButton", value = "是否显示修改退款的按钮 1显示" )
    private Integer isShowUpdateReturnButton = 0;

    @ApiModelProperty( name = "isShowCloseReturnButton", value = "是否显示撤销退款的按钮 1显示" )
    private Integer isShowCloseReturnButton = 0;

    @ApiModelProperty( name = "isShowApplySaleButton", value = "是否显示售后的按钮 1显示" )
    private Integer isShowApplySaleButton = 0;

    @ApiModelProperty( name = "isShowKanJinduButton", value = "是否显示查看进度按钮  1显示" )
    private Integer isShowKanJinduButton = 0;

    @ApiModelProperty( name = "isShowReturnDetailButton", value = "是否可以查看退款详情  1显示" )
    private Integer isShowReturnDetailButton = 0;

}
