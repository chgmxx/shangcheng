package com.gt.mall.entity.seller;

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
 * 超级销售员选取商品
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_product" )
public class MallSellerProduct extends Model< MallSellerProduct > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 商城设置id
     */
    @TableField( "mallset_id" )
    private Integer mallsetId;
    /**
     * 销售员id   关联t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer saleMemberId;
    /**
     * 商家id   关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;
    /**
     * 商品id   关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 店铺id   关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 是否已删除  0未删除   1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSellerProduct{" +
			"id=" + id +
			", mallsetId=" + mallsetId +
			", saleMemberId=" + saleMemberId +
			", busUserId=" + busUserId +
			", productId=" + productId +
			", shopId=" + shopId +
			", isDelete=" + isDelete +
			"}";
    }
}
