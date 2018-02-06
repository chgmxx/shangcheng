package com.gt.mall.entity.integral;

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
 * 积分商品表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_integral" )
public class MallIntegral extends Model< MallIntegral > {

    private static final long serialVersionUID = 1L;

    /**
     * 积分商品标示
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 积分或粉币金额
     */
    private BigDecimal money;
    /**
     * 开始时间
     */
    @TableField( "start_time" )
    private String     startTime;
    /**
     * 结束时间
     */
    @TableField( "end_time" )
    private String     endTime;
    /**
     * 1 积分商品   2 粉币商品
     */
    private Integer    type;
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
     * 是否生效 1已生效 -1 已失效
     */
    @TableField( "is_use" )
    private Integer    isUse;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallIntegral{" +
            "id=" + id +
            ", productId=" + productId +
            ", money=" + money +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", type=" + type +
            ", userId=" + userId +
            ", shopId=" + shopId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            ", isUse=" + isUse +
            "}";
    }
}
