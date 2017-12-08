package com.gt.mall.result.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.order.MallDaifu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单列表返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "OrderResult", description = "订单列表返回结果" )
@Getter
@Setter
public class OrderResult {

    @ApiModelProperty( name = "id", value = "订单ID" )
    private int id;

    @ApiModelProperty( name = "shopId", value = "店铺ID" )
    private int shopId;

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "memberName", value = "买家名称" )
    private String memberName;

    @ApiModelProperty( name = "nickName", value = "会员昵称" )
    private String nickName;

    @ApiModelProperty( name = "buyerUserId", value = "买家用户ID" )
    private int buyerUserId;

    @ApiModelProperty( name = "orderNo", value = "订单号" )
    private String orderNo;

    @ApiModelProperty( name = "orderPNo", value = "父类的订单编号" )
    private String orderPNo;

    @ApiModelProperty( name = "orderMoney", value = "订单金额(包含运费)" )
    private BigDecimal orderMoney;

    @ApiModelProperty( name = "orderFreightMoney", value = "运费" )
    private BigDecimal orderFreightMoney;

    @ApiModelProperty( name = "orderPayWay", value = "付款方式（1，微信支付 2，货到付款 3、储值卡支付 4、积分支付 5扫码支付 6到店支付 7 找人代付 8、粉币支付  9、支付宝支付 10 小程序微信支付）" )
    private int orderPayWay;

    @ApiModelProperty( name = "deliveryMethod", value = "配送方式（1, 快递配送  2,上门自提  3到店购买）" )
    private int deliveryMethod;

    @ApiModelProperty( name = "orderStatus", value = "订单状态（1,待付款 2,待发货 3,已发货 4,已完成 5,已关闭 ）" )
    private int orderStatus;

    @ApiModelProperty( name = "orderStatusName", value = "订单状态名称" )
    private String orderStatusName;

    @ApiModelProperty( name = "orderSellerRemark", value = "卖家备注" )
    private String orderSellerRemark;

    @ApiModelProperty( name = "orderBuyerMessage", value = "买家留言" )
    private String orderBuyerMessage;

    @ApiModelProperty( name = "buyerUserType", value = "买家 数据来源 0:pc端 1:微信 2:uc端 3:小程序" )
    private Integer buyerUserType;
    @ApiModelProperty( name = "typeName", value = "订单来源" )
    private String  typeName;

    @ApiModelProperty( name = "createTime", value = "创建订单时间" )
    private Date createTime;

    @ApiModelProperty( name = "updateTime", value = "修改时间" )
    private Date updateTime;

    @ApiModelProperty( name = "orderPayNo", value = "支付流水号" )
    private String orderPayNo;

    @ApiModelProperty( name = "payTime", value = "付款时间" )
    private Date payTime;

    @ApiModelProperty( name = "orderType", value = "订单类型（1.拼团 2积分 3.秒杀 4.拍卖 5 粉币 6预售 7批发）" )
    private int orderType = 0;
    @ApiModelProperty( name = "groupBuyId", value = "营销关联表id" )
    private int groupBuyId;

    @ApiModelProperty( name = "isWallet", value = "是否使用钱包支付   1已使用  0未使用 -1正在支付" )
    private int isWallet;

    @ApiModelProperty( name = "receiveAddress", value = "买家的收货地址" )
    private String receiveAddress;
    @ApiModelProperty( name = "receiveName", value = "收货人姓名" )
    private String receiveName;
    @ApiModelProperty( name = "receivePhone", value = "收货人联系方式" )
    private String receivePhone;

    @ApiModelProperty( name = "expressId", value = "快递公司id" )
    private Integer expressId;
    @ApiModelProperty( name = "expressName", value = "快递公司名称" )
    private String expressName;
    @ApiModelProperty( name = "expressNumber", value = "快递单号" )
    private String expressNumber;
    @ApiModelProperty( name = "expressTime", value = "发货时间" )
    private Date   expressTime;
    @ApiModelProperty( name = "otherExpressName", value = "其它快递公司的快递名称" )
    private String otherExpressName;

    @ApiModelProperty( name = "mallOrderDetail", value = "订单商品详情列表" )
    private List< OrderDetailResult > mallOrderDetail;

    @ApiModelProperty( name = "mallDaifu", value = "找人代付信息" )
    private MallDaifu     mallDaifu;
    @ApiModelProperty( name = "takeTheir", value = "上门自提信息" )
    private MallTakeTheir takeTheir;

/*    @ApiModelProperty( name = "isShowRemarkButton", value = "是否显示提交备注按钮  1显示" )
    private Integer isShowRemarkButton = 1;*/

    @ApiModelProperty( name = "isShowUpdatePriceButton", value = "是否显示修改价格按钮 1显示" )
    private Integer isShowUpdatePriceButton = 0;

    @ApiModelProperty( name = "isShowCancelOrderButton", value = "是否显示取消订单按钮  1显示" )
    private Integer isShowCancelOrderButton = 0;

    @ApiModelProperty( name = "isShowDeliveryButton", value = "是否显示发货按钮  1显示" )
    private Integer isShowDeliveryButton = 0;

    @ApiModelProperty( name = "isShowPickUpGoodsButton", value = "是否显示确认已提货按钮  1显示" )
    private Integer isShowPickUpGoodsButton = 0;
}
