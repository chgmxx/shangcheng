package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_collect" )
public class MallCollect extends Model< MallCollect > {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 商品id（关联t_mall_product表的id）
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 收藏人（关联t_wx_bus_member表的id）
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 收藏时间
     */
    @TableField( "create_time" )
    public  Date    createTime;
    /**
     * 是否收藏（0未收藏  1已收藏）
     */
    @TableField( "is_collect" )
    private Integer isCollect;
    /**
     * 是否删除（0未删除   1已删除）
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallCollect{" +
			"id=" + id +
			", productId=" + productId +
			", userId=" + userId +
			", createTime=" + createTime +
			", isCollect=" + isCollect +
			", isDelete=" + isDelete +
			"}";
    }
}
