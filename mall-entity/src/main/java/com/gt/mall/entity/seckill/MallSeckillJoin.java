package com.gt.mall.entity.seckill;

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
 * 秒杀参与表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seckill_join" )
public class MallSeckillJoin extends Model< MallSeckillJoin > {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀参与标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 秒杀id 关联t_mall_seckill 表的id
     */
    @TableField( "seckill_id" )
    private Integer    seckillId;
    /**
     * 秒杀人id 关联t_wx_bus_member表的id
     */
    @TableField( "seckill_user_id" )
    private Integer    seckillUserId;
    /**
     * 秒杀价
     */
    @TableField( "seckill_price" )
    private BigDecimal seckillPrice;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 关联订单id 关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 秒杀时间
     */
    @TableField( "seckill_time" )
    private Date       seckillTime;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSeckillJoin{" +
			"id=" + id +
			", seckillId=" + seckillId +
			", seckillUserId=" + seckillUserId +
			", seckillPrice=" + seckillPrice +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", productId=" + productId +
			", seckillTime=" + seckillTime +
			"}";
    }
}
