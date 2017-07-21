package com.gt.mall.entity.product;

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
 * 商品浏览表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_browse" )
public class MallProductBrowse extends Model< MallProductBrowse > {

    private static final long serialVersionUID = 1L;

    /**
     * 商品浏览标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 商品id  关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 访问时间
     */
    @TableField( "access_time" )
    private Date    accessTime;
    /**
     * 分组id 用逗号隔开
     */
    @TableField( "group_value_id" )
    private String  groupValueId;
    /**
     * 买家id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 未登陆时身份标识
     */
    @TableField( "session_id" )
    private String  sessionId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductBrowse{" +
			"id=" + id +
			", shopId=" + shopId +
			", productId=" + productId +
			", accessTime=" + accessTime +
			", groupValueId=" + groupValueId +
			", userId=" + userId +
			", sessionId=" + sessionId +
			"}";
    }
}
