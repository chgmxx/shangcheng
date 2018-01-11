package com.gt.mall.result.phone.order.returns;

import com.gt.mall.bean.DictBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退款详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneReturnResult", description = "退款详情返回结果" )
@Getter
@Setter
public class PhoneReturnResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "status", value = "退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款" )
    private Integer status;

    @ApiModelProperty( name = "statusName", value = "退款状态名称" )
    private String statusName;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "returnId", value = "退款id" )
    private int returnId = 0;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "retHandlingWay", value = " 退款类型 1,我要退款，但不退货  2,我要退款退货" )
    private Integer retHandlingWay;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "returnPrice", value = "退款金额" )
    private double returnPrice = 0;

    @ApiModelProperty( name = "retReasonId", value = "退款原因id" )
    private int retReasonId;

    @ApiModelProperty( name = "retReasonName", value = "退款原因名称" )
    private String retReasonName;

    @ApiModelProperty( name = "retRemark", value = "退货说明" )
    private String retRemark;

/*    @ApiModelProperty( name = "returnReasonList", value = "退款原因集合" )
    private List< DictBean > returnReasonList;*/

    @ApiModelProperty( name = "createTime", value = "申请时间" )
    private String createTime;

    @ApiModelProperty( name = "returnTime", value = "退款时间" )
    private String returnTime;

}
