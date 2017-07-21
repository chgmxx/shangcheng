package com.gt.mall.entity.product;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品库存
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_inventory" )
public class MallProductInventory extends Model< MallProductInventory > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品库存标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 产品id 关联t_wx_product表中的id字段
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 产品规格id  多个id用逗号隔开 例如：1,2,3   关联wx_mall_product_specifica表的id
     */
    @TableField( "specifica_ids" )
    private String     specificaIds;
    /**
     * 产品价格
     */
    @TableField( "inv_price" )
    private BigDecimal invPrice;
    /**
     * 库存
     */
    @TableField( "inv_num" )
    private Integer    invNum;
    /**
     * 产品编码
     */
    @TableField( "inv_code" )
    private String     invCode;
    /**
     * 销量
     */
    @TableField( "inv_sale_num" )
    private Integer    invSaleNum;
    /**
     * 是否已经删除  0没有删除  1已经删除了
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 是否默认  0 没有默认  1已经默认
     */
    @TableField( "is_default" )
    private Integer    isDefault;
    /**
     * 有图片的规格id
     */
    @TableField( "specifica_img_id" )
    private Integer    specificaImgId;
    /**
     * 关联erp库存id（规格详情id）
     */
    @TableField( "erp_inv_id" )
    private Integer    erpInvId;
    /**
     * 关联erp规格值id（规格id组）
     */
    @TableField( "erp_specvalue_id" )
    private String     erpSpecvalueId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductInventory{" +
			"id=" + id +
			", productId=" + productId +
			", specificaIds=" + specificaIds +
			", invPrice=" + invPrice +
			", invNum=" + invNum +
			", invCode=" + invCode +
			", invSaleNum=" + invSaleNum +
			", isDelete=" + isDelete +
			", isDefault=" + isDefault +
			", specificaImgId=" + specificaImgId +
			", erpInvId=" + erpInvId +
			", erpSpecvalueId=" + erpSpecvalueId +
			"}";
    }
}
