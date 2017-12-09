package com.gt.mall.result.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单退款返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "OrderReturnResult", description = "订单退款返回结果" )
@Getter
@Setter
public class OrderReturnResult {

    @ApiModelProperty( name = "id", value = "退款ID" )
    private int id;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "orderDetailId", value = "订单详情id" )
    private Integer orderDetailId;

    @ApiModelProperty( name = "returnNo", value = "退款单号" )
    private String returnNo;

    @ApiModelProperty( name = "retHandlingWay", value = "处理方式 1,我要退款，但不退货 2,我要退款退货" )
    private Integer retHandlingWay;

    @ApiModelProperty( name = "retReasonId", value = "申请退款原因id" )
    private Integer retReasonId;

    @ApiModelProperty( name = "retReason", value = "申请退款原因" )
    private String retReason;

    @ApiModelProperty( name = "retMoney", value = "退款金额" )
    private BigDecimal retMoney;

    @ApiModelProperty( name = "retTelephone", value = "手机号码" )
    private String retTelephone;

    @ApiModelProperty( name = "retRemark", value = "备注信息" )
    private String retRemark;

    @ApiModelProperty( name = "imagesUrl", value = " 多张图片，用逗号隔开" )
    private String imagesUrl;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "createTime", value = "创建时间" )
    private Date createTime;

    @ApiModelProperty( name = "updateTime", value = "修改时间" )
    private Date updateTime;

    @ApiModelProperty( name = "status", value = "退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款" )
    private Integer status;

    @ApiModelProperty( name = "userId", value = "申请人id" )
    private Integer userId;

    @ApiModelProperty( name = "noReturnReason", value = "拒绝退款理由" )
    private String noReturnReason;

    @ApiModelProperty( name = "returnAddress", value = "退货地址" )
    private String returnAddress;

    @ApiModelProperty( name = "wlCompanyId", value = "物流公司id" )
    private Integer wlCompanyId;

    @ApiModelProperty( name = "wlCompany", value = "物流公司" )
    private String wlCompany;

    @ApiModelProperty( name = "wlNo", value = "物流单号" )
    private String wlNo;

    @ApiModelProperty( name = "wlTelephone", value = "填写物流信息时用到的手机号码" )
    private String wlTelephone;

    @ApiModelProperty( name = "wlRemark", value = "填写物流信息的备注" )
    private String wlRemark;

    @ApiModelProperty( name = "returnFenbi", value = "退还粉币" )
    private Double returnFenbi;

    @ApiModelProperty( name = "returnJifen", value = "退还积分" )
    private Double returnJifen;

    @ApiModelProperty( name = "cargoStatus", value = "0 未收到货 1已收到货  -1 未填写" )
    private Integer cargoStatus;

    @ApiModelProperty( name = "wlImagesUrl", value = "退货物流时上传的图片 用逗号隔开" )
    private String wlImagesUrl;

    @ApiModelProperty( name = "orderReturnLogList", value = "退款维权日志列表" )
    private List< OrderReturnLogResult > orderReturnLogList;

    @ApiModelProperty( name = "isShowRefuseApplyButton", value = "是否显示拒绝退款申请按钮 1显示" )
    private Integer isShowRefuseApplyButton = 0;

    @ApiModelProperty( name = "isShowAgreeApplyButton", value = "是否显示同意退款申请按钮 1显示" )
    private Integer isShowAgreeApplyButton = 0;

    @ApiModelProperty( name = "isShowAgreeRetGoodsApplyButton", value = "是否显示同意退货退款按钮 1显示" )
    private Integer isShowAgreeRetGoodsApplyButton = 0;

    @ApiModelProperty( name = "isShowConfirmTakeButton", value = "是否显示确认收货并退款按钮 1显示" )
    private Integer isShowConfirmTakeButton = 0;

    @ApiModelProperty( name = "isShowRefuseConfirmTakeButton", value = "是否显示拒绝确认收货按钮 1显示" )
    private Integer isShowRefuseConfirmTakeButton = 0;

    @ApiModelProperty( name = "isShowUpdateAddressButton", value = "是否显示修改退货地址按钮 1显示" )
    private Integer isShowUpdateAddressButton = 0;

    @ApiModelProperty( name = "applyTimes", value = "退款申请倒计时" )
    private long applyTimes;

    @ApiModelProperty( name = "takeTimes", value = "自动确认收货倒计时" )
    private long takeTimes;

    @ApiModelProperty( name = "refundUrl", value = "封装支付宝退款地址" )
    private String refundUrl;
}
