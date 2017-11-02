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

/**
 * <p>
 * 商城订单详情表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_order_detail" )
public class MallOrderDetail extends Model< MallOrderDetail > {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 产品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 店铺id
     */
    @TableField( "shop_id" )
    private Integer shopId;

    /**
     * 商品谷歌
     */
    @TableField( "product_specificas" )
    private String     productSpecificas;
    /**
     * 产品图片
     */
    @TableField( "product_image_url" )
    private String     productImageUrl;
    /**
     * 产品数量
     */
    @TableField( "det_pro_num" )
    private Integer    detProNum;
    /**
     * 产品单价(实付商品单价)
     */
    @TableField( "det_pro_price" )
    private BigDecimal detProPrice;
    /**
     * 产品名称
     */
    @TableField( "det_pro_name" )
    private String     detProName;
    /**
     * 商家编码
     */
    @TableField( "det_pro_code" )
    private String     detProCode;
    /**
     * 产品原价
     */
    @TableField( "det_privivilege" )
    private BigDecimal detPrivivilege;
    /**
     * 买家留言
     */
    @TableField( "det_pro_message" )
    private String     detProMessage;
    /**
     * 产品规格,存多个规格，用分号隔开
     */
    @TableField( "product_speciname" )
    private String     productSpeciname;
    /**
     * 退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款
     */
    @TableField( "status" )
    private Integer    status;
    /**
     * 完成订单后在有效天数内退款
     */
    @TableField( "return_day" )
    private Integer    returnDay;
    /**
     * 折扣数
     */
    @TableField( "discount" )
    private Integer    discount;
    /**
     * 微信优惠券code
     */
    @TableField( "coupon_code" )
    private String     couponCode;
    /**
     * 优惠价格（产品价格-实付价格）
     */
    @TableField( "discounted_prices" )
    private BigDecimal discountedPrices;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 实否是实物商品（0实物 1虚拟商品非会员卡 2虚拟商品（会员卡） 3虚拟商品（卡券购买））
     */
    @TableField( "pro_type_id" )
    private Integer    proTypeId;
    /**
     * 使用粉币数量
     */
    @TableField( "use_fenbi" )
    private Double     useFenbi;
    /**
     * 使用积分数量
     */
    @TableField( "use_jifen" )
    private Double     useJifen;
    /**
     * 实付商品总价（数量乘以单价）
     */
    @TableField( "total_price" )
    private Double     totalPrice;
    /**
     * 评论状态(0 待评价 1已评价)
     */
    @TableField( "appraise_status" )
    private Integer    appraiseStatus;
    /**
     * 商品的规格（批发商品），以json方式存储{规格id:{num:数量,value:规格值,price:价格},规格id:{num:数量,value:规格值,price:价格}}
     */
    @TableField( "pro_spec_str" )
    private String     proSpecStr;
    /**
     * 购买卡券包id  关联 t_duofen_card_receive 表的id
     */
    @TableField( "card_receive_id" )
    private Integer    cardReceiveId;
    /**
     * 多粉优惠券{couponCode:优惠券code,fullCoupon:抵扣的money,discountCoupon:折扣,shopId:店铺Id,proDisAll:优惠商品的总价格,
     */
    @TableField( "duofen_coupon" )
    private String     duofenCoupon;
    /**
     * 使用优惠券的id
     */
    @TableField( "use_card_id" )
    private Integer    useCardId;
    /**
     * 销售佣金
     */
    @TableField( "commission" )
    private BigDecimal commission;

    /**
     * 销售员id  关联t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer    saleMemberId;
    /**
     * 积分优惠的价格
     */
    @TableField( "integral_youhui" )
    private BigDecimal integralYouhui;
    /**
     * 粉币优惠的价格
     */
    @TableField( "fenbi_youhui" )
    private BigDecimal fenbiYouhui;
    /**
     * 流量id，用于流量购买
     */
    @TableField( "flow_id" )
    private Integer    flowId;
    /**
     * 流量冻结id，用于流量购买
     */
    @TableField( "flow_record_id" )
    private Integer    flowRecordId;
    /**
     * 优惠券类型  1 微信优惠券  2多粉优惠券
     */
    @TableField( "coupon_type" )
    private Integer    couponType;

    @TableField( exist = false )
    private MallOrderReturn orderReturn;//退款

    @TableField( exist = false )
    private Byte isReturn;//是否允许退款

    @TableField( exist = false )
    private double newTotalPrice;//优惠后的价格（用作存储）

    @TableField( exist = false )
    private double fenbiBeforeTotalPrice;//粉币优惠前的价格

    @TableField( exist = false )
    private double jifenBeforeTotalPrice;//积分优惠前的价格

    @TableField( exist = false )
    private Integer userCard = 0;  //是否允许会员卡打折  0不参与 1参与 （用于计算）

    @TableField( exist = false )
    private Integer useCoupon = 0;  //是否允许使用优惠券   0不参与 1参与 （用于计算）

    @TableField( exist = false )
    private Integer userJifen = 0;  //是否允许使用积分  0不参与 1参与 （用于计算）

    @TableField( exist = false )
    private Integer userFenbi = 0;//是否允许使用粉币  0不允许  1允许（用于计算）

    @TableField( exist = false )
    private Integer index = 0;// 生成的index 用于计算

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallOrderDetail{" +
			"id=" + id +
			", orderId=" + orderId +
			", productId=" + productId +
			", shopId=" + shopId +
			", productSpecificas=" + productSpecificas +
			", productImageUrl=" + productImageUrl +
			", detProNum=" + detProNum +
			", detProPrice=" + detProPrice +
			", detProName=" + detProName +
			", detProCode=" + detProCode +
			", detPrivivilege=" + detPrivivilege +
			", detProMessage=" + detProMessage +
			", productSpeciname=" + productSpeciname +
			", status=" + status +
			", returnDay=" + returnDay +
			", discount=" + discount +
			", couponCode=" + couponCode +
			", discountedPrices=" + discountedPrices +
			", createTime=" + createTime +
			", proTypeId=" + proTypeId +
			", useFenbi=" + useFenbi +
			", useJifen=" + useJifen +
			", totalPrice=" + totalPrice +
			", appraiseStatus=" + appraiseStatus +
			", proSpecStr=" + proSpecStr +
			", cardReceiveId=" + cardReceiveId +
			", duofenCoupon=" + duofenCoupon +
			", useCardId=" + useCardId +
			", commission=" + commission +
			", saleMemberId=" + saleMemberId +
			", integralYouhui=" + integralYouhui +
			", fenbiYouhui=" + fenbiYouhui +
			", flowId=" + flowId +
			", flowRecordId=" + flowRecordId +
			"}";
    }
}
