package com.gt.mall.result.phone.order.detail;

import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 进入订单详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneOrderResult", description = "进入订单详情返回结果" )
@Getter
@Setter
public class PhoneOrderResult implements Serializable {

    private static final long serialVersionUID = 585402078094218855L;

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

    @ApiModelProperty( name = "orderStatusMsg", value = "订单状态倒计时说明" )
    private String orderStatusMsg;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private Integer totalNum;

    @ApiModelProperty( name = "orderMoney", value = "订单金额（实付金额）" )
    private Double orderMoney;

    @ApiModelProperty( name = "productTotalMoney", value = "商品金额" )
    private Double productTotalMoney;

    @ApiModelProperty( name = "orderFreightMoney", value = "订单运费" )
    private Double orderFreightMoney;

    @ApiModelProperty( name = "orderYouhuiMoney", value = "订单优惠金额" )
    private Double orderYouhuiMoney;

    @ApiModelProperty( name = "orderJifenYouhuiMoney", value = "订单积分优惠金额" )
    private Double orderJifenYouhuiMoney;

    @ApiModelProperty( name = "orderFenbiYouhuiMoney", value = "订单粉币优惠金额" )
    private Double orderFenbiYouhuiMoney;

    @ApiModelProperty( name = "memberAddressDTO", value = "收货地址对象" )
    private PhoneOrderMemberAddressDTO memberAddressDTO;

    @ApiModelProperty( name = "appointmentId", value = "提货id" )
    private Integer appointmentId;

    @ApiModelProperty( name = "appointmentUserName", value = "提货人姓名" )
    private String appointmentUserName;

    @ApiModelProperty( name = "appointmentUserPhone", value = "提货人手机号码" )
    private String appointmentUserPhone;

    @ApiModelProperty( name = "appointmentDate", value = "提货日期" )
    private String appointmentDate;

    @ApiModelProperty( name = "appointmentStartTime", value = "提货开始时间" )
    private String appointmentStartTime;

    @ApiModelProperty( name = "appointmentEndTime", value = "提货结束时间" )
    private String appointmentEndTime;

    @ApiModelProperty( name = "appointmentAddress", value = "提货地址" )
    private String appointmentAddress;

    @ApiModelProperty( name = "isShowGoPayButton", value = "是否显示去支付按钮 1显示" )
    private Integer isShowGoPayButton = 0;

    @ApiModelProperty( name = "isShowReceiveGoodButton", value = "是否显示确认收货按钮  1显示" )
    private Integer isShowReceiveGoodButton = 0;

    @ApiModelProperty( name = "isShowDeleteButton", value = "是否显示删除订单按钮 1显示" )
    private Integer isShowDeleteButton = 0;

    @ApiModelProperty( name = "isShowKanWuLiuButton", value = "是否查看物流按钮 1显示" )
    private Integer isShowKanWuLiuButton = 0;

    @ApiModelProperty( name = "detailResultList", value = "订单详情集合" )
    private List< PhoneOrderDetailResult > detailResultList;

    @ApiModelProperty( name = "buyerMessage", value = "买家留言" )
    private String buyerMessage;

    @ApiModelProperty( name = "busMessage", value = "商家留言" )
    private String busMessage;

    @ApiModelProperty( name = "orderType", value = "活动类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer orderType = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "joinId", value = "参团id" )
    private Integer joinId = 0;

    @ApiModelProperty( name = "joinNum", value = "参团数量" )
    private Integer joinNum = 0;

    @ApiModelProperty( name = "buyerUserId", value = "买家" )
    private Integer buyerUserId = 0;

    @ApiModelProperty( name = "orderPayWay", value = "支付方式" )
    private Integer orderPayWay = 0;

}
