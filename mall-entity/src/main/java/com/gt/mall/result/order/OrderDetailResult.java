package com.gt.mall.result.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情列表返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "OrderDetailResult", description = "订单详情列表返回结果" )
@Getter
@Setter
public class OrderDetailResult {

    @ApiModelProperty( name = "id", value = "订单详情ID" )
    private int id;

    @ApiModelProperty( name = "shopId", value = "店铺ID" )
    private int shopId;

    @ApiModelProperty( name = "orderId", value = "订单ID" )
    private int orderId;

    @ApiModelProperty( name = "productId", value = "商品ID" )
    private int productId;

    @ApiModelProperty( name = "productSpecificas", value = "商品规格" )
    private String productSpecificas;

    @ApiModelProperty( name = "productImageUrl", value = "产品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "detProNum", value = "产品数量" )
    private Integer detProNum;

    @ApiModelProperty( name = "detProPrice", value = "产品单价(实付商品单价)" )
    private BigDecimal detProPrice;

    @ApiModelProperty( name = "detProName", value = "产品名称" )
    private String detProName;

    @ApiModelProperty( name = "detProCode", value = "商家编码" )
    private String detProCode;

    @ApiModelProperty( name = "detPrivivilege", value = "产品原价" )
    private BigDecimal detPrivivilege;

    @ApiModelProperty( name = "detProMessage", value = "买家留言" )
    private String detProMessage;

    @ApiModelProperty( name = "productSpeciname", value = "产品规格,存多个规格，用分号隔开" )
    private String productSpeciname;

    @ApiModelProperty( name = "status", value = "退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款" )
    private Integer status;

    @ApiModelProperty( name = "statusName", value = "状态名称" )
    private String statusName;

    @ApiModelProperty( name = "returnDay", value = "完成订单后在有效天数内退款" )
    private Integer returnDay;

    @ApiModelProperty( name = "discount", value = "折扣数" )
    private Integer discount;

    @ApiModelProperty( name = "couponCode", value = "微信优惠券code" )
    private String couponCode;

    @ApiModelProperty( name = "discountedPrices", value = "优惠价格（产品价格-实付价格）" )
    private BigDecimal discountedPrices;

    @ApiModelProperty( name = "createTime", value = "创建时间" )
    private Date createTime;

    @ApiModelProperty( name = "proTypeId", value = "实否是实物商品（0实物 1虚拟商品非会员卡 2虚拟商品（会员卡） 3虚拟商品（卡券购买））" )
    private Integer proTypeId;

    @ApiModelProperty( name = "useFenbi", value = "使用粉币数量" )
    private Double useFenbi;

    @ApiModelProperty( name = "useJifen", value = "使用积分数量" )
    private Double useJifen;

    @ApiModelProperty( name = "totalPrice", value = "实付商品总价（数量乘以单价）" )
    private Double totalPrice;

    @ApiModelProperty( name = "appraiseStatus", value = "评论状态(0 待评价 1已评价)" )
    private Integer appraiseStatus;

    @ApiModelProperty( name = "proSpecStr", value = "商品的规格" )
    private String proSpecStr;

    @ApiModelProperty( name = "cardReceiveId", value = "购买卡券包id" )
    private Integer cardReceiveId;

    @ApiModelProperty( name = "duofenCoupon", value = "多粉优惠券" )
    private String duofenCoupon;

    @ApiModelProperty( name = "useCardId", value = "使用优惠券的id" )
    private Integer useCardId;

    @ApiModelProperty( name = "commission", value = "销售佣金" )
    private BigDecimal commission;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private Integer saleMemberId;

    @ApiModelProperty( name = "integralYouhui", value = "积分优惠的价格" )
    private BigDecimal integralYouhui;

    @ApiModelProperty( name = "fenbiYouhui", value = "粉币优惠的价格" )
    private BigDecimal fenbiYouhui;

    @ApiModelProperty( name = "flowId", value = "流量id" )
    private Integer flowId;

    @ApiModelProperty( name = "flowRecordId", value = "流量冻结id" )
    private Integer flowRecordId;

    @ApiModelProperty( name = "couponType", value = "优惠券类型" )
    private Integer couponType;

    @ApiModelProperty( name = "returnResult", value = "退款信息" )
    private OrderReturnResult returnResult;

}
