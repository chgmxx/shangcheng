package com.gt.mall.entity.product;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商品分组中间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_group" )
public class MallProductGroup extends Model< MallProductGroup > {

    private static final long serialVersionUID = 1L;

    /**
     * 商品分组标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 商品id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 分组id
     */
    @TableField( "group_id" )
    private Integer groupId;
    /**
     * 店铺id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 是否删除  0没有删除  1删除了
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 父类id
     */
    @TableField( "group_p_id" )
    private Integer groupPId;
    /**
     * 排序 升序
     */
    private Integer sort;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductGroup{" +
			"id=" + id +
			", productId=" + productId +
			", groupId=" + groupId +
			", shopId=" + shopId +
			", isDelete=" + isDelete +
			", groupPId=" + groupPId +
			", sort=" + sort +
			"}";
    }
}
