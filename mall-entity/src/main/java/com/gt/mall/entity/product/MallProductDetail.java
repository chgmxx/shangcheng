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
 * 商品详情表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_detail" )
public class MallProductDetail extends Model< MallProductDetail > {

    private static final long serialVersionUID = 1L;

    /**
     * 商品详情标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 商品id 关联t_wx_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 商品详情
     */
    @TableField( "product_detail" )
    private String  productDetail;
    /**
     * 商品简介
     */
    @TableField( "product_introdu" )
    private String  productIntrodu;
    /**
     * 商品信息栏
     */
    @TableField( "product_message" )
    private String  productMessage;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductDetail{" +
			"id=" + id +
			", productId=" + productId +
			", productDetail=" + productDetail +
			", productIntrodu=" + productIntrodu +
			", productMessage=" + productMessage +
			"}";
    }
}
