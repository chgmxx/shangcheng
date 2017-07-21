package com.gt.mall.entity.seller;

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
 * 超级销售员收益表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_income" )
public class MallSellerIncome extends Model< MallSellerIncome > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 销售员id 关联t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer    saleMemberId;
    /**
     * 收益佣金
     */
    @TableField( "income_commission" )
    private BigDecimal incomeCommission;
    /**
     * 收益积分
     */
    @TableField( "income_integral" )
    private BigDecimal incomeIntegral;
    /**
     * 收益金额
     */
    @TableField( "income_money" )
    private BigDecimal incomeMoney;
    /**
     * 收益时间
     */
    @TableField( "income_time" )
    private Date       incomeTime;
    /**
     * 购买人id 关联t_wx_bus_member表的id
     */
    @TableField( "buyer_user_id" )
    private Integer    buyerUserId;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 订单详情id 关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
    /**
     * 获得收益途径  1关注    2通过订单
     */
    @TableField( "income_type" )
    private Integer    incomeType;
    /**
     * 是否领取佣金 -1 还不能领取  0可以领取还未领  1已领取 -2无效
     */
    @TableField( "is_get" )
    private Integer    isGet;
    /**
     * 佣金的领取方式   1微信   2储值卡
     */
    @TableField( "commission_type" )
    private Integer    commissionType;
    /**
     * 未领取佣金
     */
    @TableField( "no_receive_commission" )
    private BigDecimal noReceiveCommission;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSellerIncome{" +
			"id=" + id +
			", saleMemberId=" + saleMemberId +
			", incomeCommission=" + incomeCommission +
			", incomeIntegral=" + incomeIntegral +
			", incomeMoney=" + incomeMoney +
			", incomeTime=" + incomeTime +
			", buyerUserId=" + buyerUserId +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", incomeType=" + incomeType +
			", isGet=" + isGet +
			", commissionType=" + commissionType +
			", noReceiveCommission=" + noReceiveCommission +
			"}";
    }
}
