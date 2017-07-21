package com.gt.mall.entity.pifa;

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
 * 商品批发表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_pifa" )
public class MallPifa extends Model< MallPifa > {

    private static final long serialVersionUID = 1L;

    /**
     * 批发标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 批发价
     */
    @TableField( "pf_price" )
    private BigDecimal pfPrice;
    /**
     * 批发开始时间
     */
    @TableField( "pf_start_time" )
    private String     pfStartTime;
    /**
     * 批发结束时间
     */
    @TableField( "pf_end_time" )
    private String     pfEndTime;
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
     * 是否生效 0 未生效  1已生效
     */
    @TableField( "is_use" )
    private Integer    isUse;
    /**
     * 批发类型 1手批 2混批
     */
    @TableField( "pf_type" )
    private Integer    pfType;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPifa{" +
			"id=" + id +
			", productId=" + productId +
			", pfPrice=" + pfPrice +
			", pfStartTime=" + pfStartTime +
			", pfEndTime=" + pfEndTime +
			", userId=" + userId +
			", shopId=" + shopId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", isUse=" + isUse +
			", pfType=" + pfType +
			"}";
    }
}
