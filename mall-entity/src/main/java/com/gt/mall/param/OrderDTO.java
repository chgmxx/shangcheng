package com.gt.mall.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gt.mall.param.basic.ImageAssociativeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 订单列表查询
 * </p>
 *
 * @author maoyl
 * @since 2017-09-26
 */
@JsonIgnoreProperties( { "handler", "hibernateLazyInitializer" } )
@Data
@ApiModel( value = "order" ,description = "进入订单列表验证" )
public class OrderDTO {

    /**
     * 类型 (0 所有订单  -1维权订单)
     */
    @ApiModelProperty( value = "类型 (0 所有订单  -1维权订单)" )
    private Integer orderType = 0;
    /**
     * 页数
     */
    @ApiModelProperty( value = "页数" )
    private Integer curPage;

    /**
     * 订单号/姓名/联系电话
     */
    @ApiModelProperty( value = "订单号/姓名/联系电话" )
    private String queryName;

    /**
     * 下单起始时间
     */
    @ApiModelProperty( value = "下单起始时间" )
    private String startTime;
    /**
     * 下单结束时间
     */
    @ApiModelProperty( value = "下单结束时间" )
    private String endTime;
    /**
     * 订单类型
     */
    @ApiModelProperty( value = "订单类型（1.拼团 2积分 3.秒杀 4.拍卖 5 粉币 6预售 7批发）" )
    private Integer type;
    /**
     * 订单来源
     */
    @ApiModelProperty( value = "订单来源（0:pc端 1:微信 2:uc端 3:小程序 ）" )
    private Integer orderSource;
    /**
     * 订单状态
     */
    @ApiModelProperty( value = "订单状态（1,待付款 2,待发货 3,已发货 4,已完成 5,已关闭  6退款中 7全部 8退款处理中 9 退款结束）" )
    private Integer status;

    /**
     * 退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款
     */
    @ApiModelProperty( value = "维权状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款" )
    private Integer returnStatus;

    /**
     * 付款方式
     */
    @ApiModelProperty( value = "付款方式（1，微信支付 2，货到付款 3、储值卡支付 4、积分支付 5扫码支付 6到店支付 7 找人代付 8、粉币支付  9、支付宝支付 10 小程序微信支付）" )
    private Integer orderPayWay;

    /**
     * 配送方式（1, 快递配送  2,上门自提  3到店购买）
     */
    @ApiModelProperty( value = "配送方式（1, 快递配送  2,上门自提  3到店购买）" )
    private Integer deliveryMethod;

    /**
     * 店铺id
     */
    @ApiModelProperty( value = "店铺id" )
    private Integer shopId;

}
