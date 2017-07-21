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

/**
 * <p>
 * 团购价格表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_group_buy_price" )
public class MallGroupBuyPrice extends Model< MallGroupBuyPrice > {

    private static final long serialVersionUID = 1L;

    /**
     * 团购价格标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 团购id  关联t_mall_group_buy表的id
     */
    @TableField( "group_buy_id" )
    private Integer    groupBuyId;
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
     * 团购价格
     */
    @TableField( "group_price" )
    private BigDecimal groupPrice;
    /**
     * 是否参团 0 未参团 1参团
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
	return "MallGroupBuyPrice{" +
			"id=" + id +
			", groupBuyId=" + groupBuyId +
			", invenId=" + invenId +
			", specificaIds=" + specificaIds +
			", groupPrice=" + groupPrice +
			", isJoinGroup=" + isJoinGroup +
			", isDelete=" + isDelete +
			"}";
    }
}
