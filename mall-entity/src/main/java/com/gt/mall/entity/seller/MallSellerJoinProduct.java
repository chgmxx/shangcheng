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
 * 加入超级销售员的商品表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_join_product" )
public class MallSellerJoinProduct extends Model< MallSellerJoinProduct > {

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
     * 佣金比例
     */
    @TableField( "commission_rate" )
    private BigDecimal commissionRate;
    /**
     * 佣金类型 1按百分比  2按固定金额
     */
    @TableField( "commission_type" )
    private Integer    commissionType;
    /**
     * 创建人 关联bus_user表的id
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSellerJoinProduct{" +
            "id=" + id +
            ", productId=" + productId +
            ", commissionRate=" + commissionRate +
            ", commissionType=" + commissionType +
            ", userId=" + userId +
            ", shopId=" + shopId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            ", isUse=" + isUse +
            "}";
    }
}
