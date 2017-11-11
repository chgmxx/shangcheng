package com.gt.mall.result.phone.order.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 商家集合（进入提交列表返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneOrderListBusResult", description = "商家集合（进入提交列表返回结果）" )
@Getter
@Setter
public class PhoneOrderListOrderResult implements Serializable {

    private static final long serialVersionUID = 6685362436611404285L;

    @ApiModelProperty( name = "busId", value = "商家集合" )
    private Integer busId;

    @ApiModelProperty( name = "busName", value = "商家名称" )
    private String busName;

    @ApiModelProperty( name = "busImageUrl", value = "商家图片" )
    private String busImageUrl;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "shopImageUrl", value = "店铺图片地址" )
    private String shopImageUrl;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "orderNo", value = "订单号" )
    private String orderNo;

    @ApiModelProperty( name = "orderStatus", value = "订单状态" )
    private Integer orderStatus;

    @ApiModelProperty( name = "orderStatusName", value = "订单状态码" )
    private String orderStatusName;

    @ApiModelProperty( name = "orderCreateTime", value = "下单时间" )
    private String orderCreateTime;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private Integer totalNum;

    @ApiModelProperty( name = "orderMoney", value = "订单金额" )
    private Double orderMoney;

    @ApiModelProperty( name = "isShowGoPayButton", value = "是否显示去支付按钮 1显示" )
    private Integer isShowGoPayButton = 0;

    @ApiModelProperty( name = "isShowReceiveGoodButton", value = "是否显示确认收货按钮  1显示" )
    private Integer isShowReceiveGoodButton = 0;

    @ApiModelProperty( name = "isShowDaifuButton", value = "是否显示代付详情 1显示" )
    private Integer isShowDaifuButton = 0;

    @ApiModelProperty( name = "detailResultList", value = "订单详情集合" )
    private List< PhoneOrderListOrderDetailResult > detailResultList;

    @ApiModelProperty( name = "orderType", value = "活动类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer orderType = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

}
