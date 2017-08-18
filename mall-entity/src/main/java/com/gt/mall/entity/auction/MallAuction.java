package com.gt.mall.entity.auction;

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
 * 拍卖
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_auction" )
public class MallAuction extends Model< MallAuction > {

    private static final long serialVersionUID = 1L;

    /**
     * 拍卖标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 拍卖类型 1 降价拍 2升价拍
     */
    @TableField( "auc_type" )
    private Integer    aucType;
    /**
     * 起拍价格
     */
    @TableField( "auc_start_price" )
    private BigDecimal aucStartPrice;
    /**
     * 最低价格
     */
    @TableField( "auc_lowest_price" )
    private BigDecimal aucLowestPrice;
    /**
     * 开始时间
     */
    @TableField( "auc_start_time" )
    private String     aucStartTime;
    /**
     * 降价时间（每多少分钟）
     */
    @TableField( "auc_lower_price_time" )
    private Integer    aucLowerPriceTime;
    /**
     * 降价金额（每多少分钟降价多少元）
     */
    @TableField( "auc_lower_price" )
    private BigDecimal aucLowerPrice;
    /**
     * 结束时间
     */
    @TableField( "auc_end_time" )
    private String     aucEndTime;
    /**
     * 拍卖数量
     */
    @TableField( "auc_num" )
    private Integer    aucNum;
    /**
     * 限购数量
     */
    @TableField( "auc_restriction_num" )
    private Integer    aucRestrictionNum;
    /**
     * 创建人id 关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
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
     * 是否生效 0 未生效  1已生效
     */
    @TableField( "is_use" )
    private Integer    isUse;
    /**
     * 是否需要加纳保证金 0不需要 1需要
     */
    @TableField( "is_margin" )
    private Integer    isMargin;
    /**
     * 保证金
     */
    @TableField( "auc_margin" )
    private BigDecimal aucMargin;
    /**
     * 加价幅度
     */
    @TableField( "auc_add_price" )
    private BigDecimal aucAddPrice;


    @TableField(exist = false)
    private int joinId;

    @TableField(exist = false)
    private int status;//状态 0未开始 1进行中 -1已结束 -2已失效

    @TableField(exist = false)
    private Long times;// 剩余时间

    @TableField(exist = false)
    private Long startTimes;// 活动开始剩余时间

    @TableField(exist = false)
    private double nowPrice; //当前价格

    @TableField(exist = false)
    private int wx_shop_id; //门店ID 关联wx_shop表的id

    @TableField(exist = false)
    private String shopName;

    @TableField(exist = false)
    private String proName;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallAuction{" +
			"id=" + id +
			", productId=" + productId +
			", shopId=" + shopId +
			", aucType=" + aucType +
			", aucStartPrice=" + aucStartPrice +
			", aucLowestPrice=" + aucLowestPrice +
			", aucStartTime=" + aucStartTime +
			", aucLowerPriceTime=" + aucLowerPriceTime +
			", aucLowerPrice=" + aucLowerPrice +
			", aucEndTime=" + aucEndTime +
			", aucNum=" + aucNum +
			", aucRestrictionNum=" + aucRestrictionNum +
			", userId=" + userId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", isUse=" + isUse +
			", isMargin=" + isMargin +
			", aucMargin=" + aucMargin +
			", aucAddPrice=" + aucAddPrice +
			"}";
    }
}
