package com.gt.mall.entity.groupbuy;

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
 * 参团表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_group_join" )
public class MallGroupJoin extends Model< MallGroupJoin > {

    private static final long serialVersionUID = 1L;

    /**
     * 参团标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 团购id  关联t_mall_group_buy表的id
     */
    @TableField( "group_buy_id" )
    private Integer    groupBuyId;
    /**
     * 商品规格id 关联t_mall_product_specifica表的specifica_value_id 存放多个规格，用逗号(,)分隔
     */
    @TableField( "specifica_ids" )
    private String     specificaIds;
    /**
     * 参团价格
     */
    @TableField( "join_price" )
    private BigDecimal joinPrice;
    /**
     * 参团人id  关联t_wx_bus_member表的id
     */
    @TableField( "join_user_id" )
    private Integer    joinUserId;
    /**
     * 参团类型 0 参团  1开团
     */
    @TableField( "join_type" )
    private Integer    joinType;
    /**
     * 关联订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 关联订单id 关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
    /**
     * 参团时间
     */
    @TableField( "join_time" )
    private Date       joinTime;
    /**
     * 参团状态 0 参团中 1参/开团成功 -1参/开团失败
     */
    @TableField( "join_status" )
    private Integer    joinStatus;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 参团id  关联t_mall_group_join表的id
     */
    @TableField( "p_join_id" )
    private Integer    pJoinId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallGroupJoin{" +
			"id=" + id +
			", groupBuyId=" + groupBuyId +
			", specificaIds=" + specificaIds +
			", joinPrice=" + joinPrice +
			", joinUserId=" + joinUserId +
			", joinType=" + joinType +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", joinTime=" + joinTime +
			", joinStatus=" + joinStatus +
			", productId=" + productId +
			", pJoinId=" + pJoinId +
			"}";
    }
}
