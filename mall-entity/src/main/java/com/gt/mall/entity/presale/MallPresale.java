package com.gt.mall.entity.presale;

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
import java.util.Map;

/**
 * <p>
 * 商品预售表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale" )
public class MallPresale extends Model< MallPresale > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 预售开始时间
     */
    @TableField( "sale_start_time" )
    private String     saleStartTime;
    /**
     * 预售结束时间
     */
    @TableField( "sale_end_time" )
    private String     saleEndTime;
    /**
     * 是否缴纳定金 0 不用缴纳     1缴纳
     */
    @TableField( "is_deposit" )
    private Integer    isDeposit;
    /**
     * 定金   商品原价的百分比   从1%到100%
     */
    @TableField( "deposit_percent" )
    private BigDecimal depositPercent;
    /**
     * 创建人  关联bus_user表的id
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
     * 是否生效  1已生效 -1 已失效
     */
    @TableField( "is_use" )
    private Integer    isUse;
    /**
     * 预定数量（作弊）
     */
    @TableField( "order_num" )
    private Integer    orderNum;

    @TableField(exist = false)
    private String shopName;//店铺名称

    @TableField(exist = false)
    private String proName;//商品名称

    @TableField(exist = false)
    private int status;//预售状态

    @TableField(exist = false)
    private int joinId;//参加用户id

    @TableField(exist = false)
    private long times;

    @TableField(exist = false)
    private long startTimes;

    @TableField(exist = false)
    private String twoCodePath;

    @TableField(exist = false)
    private int invNum;

    @TableField(exist = false)
    private List<Map<String, Object> > list;

    @TableField(exist = false)
    private int wx_shop_id; //门店ID 关联wx_shop表的id

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPresale{" +
			"id=" + id +
			", productId=" + productId +
			", shopId=" + shopId +
			", saleStartTime=" + saleStartTime +
			", saleEndTime=" + saleEndTime +
			", isDeposit=" + isDeposit +
			", depositPercent=" + depositPercent +
			", userId=" + userId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", isUse=" + isUse +
			", orderNum=" + orderNum +
			"}";
    }
}
