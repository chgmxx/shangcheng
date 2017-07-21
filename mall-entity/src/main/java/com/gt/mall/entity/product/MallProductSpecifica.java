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
 * 商品的规格
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_specifica" )
public class MallProductSpecifica extends Model< MallProductSpecifica > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品规格标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 产品id 关联t_wx_product表中的id字段
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 规格名称id
     */
    @TableField( "specifica_name_id" )
    private Integer specificaNameId;
    /**
     * 规格值id
     */
    @TableField( "specifica_value_id" )
    private Integer specificaValueId;
    /**
     * 规格名称
     */
    @TableField( "specifica_name" )
    private String  specificaName;
    /**
     * 规格值
     */
    @TableField( "specifica_value" )
    private String  specificaValue;
    /**
     * 图片地址
     */
    @TableField( "specifica_img_url" )
    private String  specificaImgUrl;
    /**
     * 是否已经删除
     * 0未删除   1删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 排序 正序
     */
    private Integer sort;
    /**
     * 关联erp规格值id（子类的规格id） 0为没有关联erp
     */
    @TableField( "erp_specvalue_id" )
    private Integer erpSpecvalueId;
    /**
     * 关联erp规格值id（父类的规格id） 0为没有关联erp
     */
    @TableField( "erp_specname_id" )
    private Integer erpSpecnameId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductSpecifica{" +
			"id=" + id +
			", productId=" + productId +
			", specificaNameId=" + specificaNameId +
			", specificaValueId=" + specificaValueId +
			", specificaName=" + specificaName +
			", specificaValue=" + specificaValue +
			", specificaImgUrl=" + specificaImgUrl +
			", isDelete=" + isDelete +
			", sort=" + sort +
			", erpSpecvalueId=" + erpSpecvalueId +
			", erpSpecnameId=" + erpSpecnameId +
			"}";
    }
}
