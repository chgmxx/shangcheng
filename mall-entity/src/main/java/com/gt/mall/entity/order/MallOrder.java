package com.gt.mall.entity.order;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商城订单
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_order" )
public class MallOrder extends Model< MallOrder > implements Serializable, Cloneable {

    private static final long serialVersionUID = 4083441605166952158L;
    /**
     * 订单标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 订单编号
     */
    @TableField( "order_no" )
    private String     orderNo;
    /**
     * 微信订单编号
     */
    @TableField( "wx_order_no" )
    private String     wxOrderNo;
    /**
     * 收货id
     */
    @TableField( "receive_id" )
    private Integer    receiveId;
    /**
     * 订单金额(包含运费)
     */
    @TableField( "order_money" )
    private BigDecimal orderMoney;
    /**
     * 运费
     */
    @TableField( "order_freight_money" )
    private BigDecimal orderFreightMoney;
    /**
     * 订单原价(不包含运费)
     */
    @TableField( "order_old_money" )
    private BigDecimal orderOldMoney;
    /**
     * 买家留言
     */
    @TableField( "order_buyer_message" )
    private String     orderBuyerMessage;
    /**
     * 卖家备注
     */
    @TableField( "order_seller_remark" )
    private String     orderSellerRemark;
    /**
     * 加星
     */
    @TableField( "order_start" )
    private Integer    orderStart;
    /**
     * 付款方式（1，微信支付 2，货到付款 3、储值卡支付 4、积分支付 5扫码支付 6到店支付 7 找人代付 8、粉币支付  9、支付宝支付 10 小程序微信支付）
     */
    @TableField( "order_pay_way" )
    private Integer    orderPayWay;
    /**
     * 配送方式（1, 快递配送  2,上门自提  3到店购买）
     */
    @TableField( "delivery_method" )
    private Integer    deliveryMethod;
    /**
     * 预约人姓名
     */
    @TableField( "appointment_name" )
    private String     appointmentName;
    /**
     * 预约人联系方式
     */
    @TableField( "appointment_telephone" )
    private String     appointmentTelephone;
    /**
     * 预约时间
     */
    @TableField( "appointment_time" )
    private Date       appointmentTime;
    /**
     * 发票抬头
     */
    @TableField( "invoice_title" )
    private String     invoiceTitle;
    /**
     * 自提点id
     */
    @TableField( "take_their_id" )
    private Integer    takeTheirId;
    /**
     * 是否短信通知
     */
    @TableField( "is_message_notice" )
    private Integer    isMessageNotice;
    /**
     * 买家用户id 关联t_wx_bus_member表的id
     */
    @TableField( "buyer_user_id" )
    private Integer    buyerUserId;
    /**
     * 商家公众号id 关联表t_wx_public_users表的id
     */
    @TableField( "seller_user_id" )
    private Integer    sellerUserId;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 订单状态（1,待付款 2,待发货 3,已发货 4,已完成 5,已关闭 ）
     */
    @TableField( "order_status" )
    private Integer    orderStatus;
    /**
     * 创建订单时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 支付流水号
     */
    @TableField( "order_pay_no" )
    private String     orderPayNo;
    /**
     * 付款时间
     */
    @TableField( "pay_time" )
    private Date       payTime;
    /**
     * 发货方式
     * 0 无需物流   1 需要物流
     */
    @TableField( "express_way" )
    private Integer    expressWay;
    /**
     * 快递公司id
     */
    @TableField( "express_id" )
    private Integer    expressId;
    /**
     * 快递单号
     */
    @TableField( "express_number" )
    private String     expressNumber;
    /**
     * 发货时间
     */
    @TableField( "express_time" )
    private Date       expressTime;
    /**
     * 买家取消订单理由（从字典表取）
     */
    @TableField( "buyer_cancel_reason" )
    private Integer    buyerCancelReason;
    /**
     * 卖家取消订单理由（从字典表取）
     */
    @TableField( "seller_cancel_reason" )
    private Integer    sellerCancelReason;
    /**
     * 订单修改的时间
     */
    @TableField( "update_time" )
    private Date       updateTime;
    /**
     * 买家 数据来源 0:pc端 1:微信 2:uc端 3:小程序
     */
    @TableField( "buyer_user_type" )
    private Integer    buyerUserType;
    /**
     * 订单的父Id
     */
    @TableField( "order_pid" )
    private Integer    orderPid;
    /**
     * 会员卡类型
     */
    @TableField( "mem_card_type" )
    private Integer    memCardType;
    /**
     * 赠送状态 0没有赠送 1赠送
     */
    @TableField( "give_status" )
    private Integer    giveStatus;
    /**
     * 团购表id(order_type=1 关联t_mall_group_buy的id ；order_type = 3 关联t_mall_seckill 表的id ;order_type=4 关联t_mall_auction表的id;order_type = 6 关联t_mall_presale表的id;order_type = 7 关联t_mall_pifa表的id)
     */
    @TableField( "group_buy_id" )
    private Integer    groupBuyId;
    /**
     * 订单类型（1.拼团 2积分 3.秒杀 4.拍卖 5 粉币 6预售 7批发）
     */
    @TableField( "order_type" )
    private Integer orderType = 0;
    /**
     * 参团表的父id  根据订单类型 来决定关联表
     */
    @TableField( "p_join_id" )
    private Integer pJoinId;
    /**
     * 优惠券code
     */
    @TableField( "coupon_code" )
    private String  couponCode;
    /**
     * 预约开始时间 小时:分钟的格式
     */
    @TableField( "appointment_start_time" )
    private String  appointmentStartTime;
    /**
     * 预约结束时间    小时:分钟的格式
     */
    @TableField( "appointment_end_time" )
    private String  appointmentEndTime;
    /**
     * 是否使用钱包支付   1已使用  0未使用 -1正在支付
     */
    @TableField( "is_wallet" )
    private Integer isWallet;
    /**
     * 商家id  关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;
    /**
     * 买家的收货地址
     */
    @TableField( "receive_address" )
    private String  receiveAddress;
    /**
     * 收货人姓名
     */
    @TableField( "receive_name" )
    private String  receiveName;
    /**
     * 收货人联系方式
     */
    @TableField( "receive_phone" )
    private String  receivePhone;
    /**
     * 流量充值手机号
     */
    @TableField( "flow_phone" )
    private String  flowPhone;
    /**
     * 流量充值的状态 -1不用充值 0未充值 1已充值 2充值失败且需要退款 3退款成功
     */
    @TableField( "flow_recharge_status" )
    private Integer flowRechargeStatus;

    @TableField( "member_name" )
    private String  memberName;
    /**
     * 其它快递公司的物流名称
     */
    @TableField( "other_express_name" )
    private String  otherExpressName;
    /**
     * 订单使用积分的数量
     */
    @TableField( "use_jifen" )
    private Double  useJifen;
    /**
     * 订单使用粉币的数量
     */
    @TableField( "use_fenbi" )
    private Double  useFenbi;
    /**
     * 联盟卡id
     */
    @TableField( "union_card_id" )
    private Integer unionCardId;
    /**
     * 使用卡券的id
     */
    @TableField( "coupon_id" )
    private Integer couponId;

    /**
     * 粉币优惠的金额
     */
    @TableField( "fenbi_discount_money" )
    private Double  fenbiDiscountMoney;
    /**
     * 积分优惠的金额
     */
    @TableField( "jifen_discount_money" )
    private Double  jifenDiscountMoney;
    /**
     * 总共优惠的金额
     */
    @TableField( "discount_money" )
    private Double  discountMoney;
    /**
     * 优惠券类型  1 微信优惠券  2多粉优惠券
     */
    @TableField( "coupon_type" )
    private Integer couponType;
    /**
     * 优惠券使用数量
     */
    @TableField( "coupon_use_num" )
    private Integer couponUseNum;
    /**
     * 会员是否已删除
     */
    @TableField( "member_is_delete" )
    private Integer memberIsDelete;
    /**
     * 自提点地址
     */
    @TableField("appointment_address")
    private String appointmentAddress;

    @TableField( exist = false )
    private Integer updateDay;//修改订单的天数

    @TableField( exist = false )
    private String orderPNo;//父类的订单编号

    @TableField( exist = false )
    private String shopName;//店铺名称

    @TableField( exist = false )
    private List< MallOrderDetail > mallOrderDetail;        //商品详情

    @TableField( exist = false )
    double yhqDiscountMoney = 0;//使用优惠券总共优惠的金额

    @TableField( exist = false )
    double unionDiscountMoney = 0;//使用商家联盟总共优惠的金额

    @TableField( exist = false )
    double productAllMoney = 0;//订单商品总金额

    @TableField( exist = false )
    private Integer wxShopId;//微信门店

    @TableField( exist = false )
    private Integer useCoupon;//是否使用优惠券  用于计算


    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public MallOrder clone() {
	MallOrder mallOrder = null;
	try {
	    mallOrder = (MallOrder) super.clone();
	} catch ( CloneNotSupportedException e ) {
	    e.printStackTrace();
	}
	return mallOrder;
    }

}
