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

/**
 * <p>
 * 秒杀价格表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seckill_price" )
public class MallSeckillPrice extends Model< MallSeckillPrice > {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀价格标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 秒杀id 关联t_mall_seckill 表的id
     */
    @TableField( "seckill_id" )
    private Integer    seckillId;
    /**
     * 商品库存id 关联t_mall_product_inventory表的id
     */
    @TableField( "inven_id" )
    private Integer    invenId;
    /**
     * 商品规格id 关联t_mall_product_specifica表的specifica_value_id 存放多个规格，用逗号(,)分隔
     */
    @TableField( "specifica_ids" )
    private String     specificaIds;
    /**
     * 秒杀价格
     */
    @TableField( "seckill_price" )
    private BigDecimal seckillPrice;
    /**
     * 是否参与秒杀  0 未参与 1参与
     */
    @TableField( "is_join_group" )
    private Integer    isJoinGroup;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSeckillPrice{" +
			"id=" + id +
			", seckillId=" + seckillId +
			", invenId=" + invenId +
			", specificaIds=" + specificaIds +
			", seckillPrice=" + seckillPrice +
			", isJoinGroup=" + isJoinGroup +
			", isDelete=" + isDelete +
			"}";
    }
}
