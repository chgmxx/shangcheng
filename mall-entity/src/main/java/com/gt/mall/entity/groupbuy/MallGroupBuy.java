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
 * 团购表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_group_buy" )
public class MallGroupBuy extends Model< MallGroupBuy > {

    private static final long serialVersionUID = 1L;

    /**
     * 团购标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 团购活动名称
     */
    @TableField( "g_name" )
    private String     gName;
    /**
     * 团购价
     */
    @TableField( "g_price" )
    private BigDecimal gPrice;
    /**
     * 团购开始时间
     */
    @TableField( "g_start_time" )
    private String     gStartTime;
    /**
     * 团购结束时间
     */
    @TableField( "g_end_time" )
    private String     gEndTime;
    /**
     * 参团人数
     */
    @TableField( "g_people_num" )
    private Integer    gPeopleNum;
    /**
     * 商品限购数量
     */
    @TableField( "g_max_buy_num" )
    private Integer    gMaxBuyNum;
    /**
     * 创建人  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 商品所属店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 是否生效  1已生效 -1 已失效
     */
    @TableField( "is_use" )
    private Integer    isUse;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallGroupBuy{" +
			"id=" + id +
			", productId=" + productId +
			", gName=" + gName +
			", gPrice=" + gPrice +
			", gStartTime=" + gStartTime +
			", gEndTime=" + gEndTime +
			", gPeopleNum=" + gPeopleNum +
			", gMaxBuyNum=" + gMaxBuyNum +
			", userId=" + userId +
			", shopId=" + shopId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", isUse=" + isUse +
			"}";
    }
}
