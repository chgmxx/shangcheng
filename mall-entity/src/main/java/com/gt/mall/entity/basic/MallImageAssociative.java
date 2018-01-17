package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 图片中间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_image_associative" )
public class MallImageAssociative extends Model< MallImageAssociative > {

    private static final long serialVersionUID = 1L;

    /**
     * 图片标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 图片地址
     */
    @ApiModelProperty( name = "imageUrl", value = "商品名称" )
    @TableField( "image_url" )
    private String  imageUrl;
    /**
     * 关联id
     */
    @TableField( "ass_id" )
    private Integer assId;
    /**
     * 是否是主图 0不是主图  1是主图
     */
    @ApiModelProperty( name = "isMainImages", value = "是否是主图  1 主图 0不是" )
    @TableField( "is_main_images" )
    private Integer isMainImages;
    /**
     * 关联表类型 1关联商品表 t_mall_product表的id  2关联商品分组表t_mall_group的id  3关联上门自提表 t_mall_take_their表的id   4关联评价表t_mall_comment表的id
     *         5关联店铺表t_mall_store的id 6关联店铺认证表t_mall_store_certification的id
     */
    @TableField( "ass_type" )
    private Integer assType;
    /**
     * 排序
     */
    @TableField( "ass_sort" )
    private Integer assSort;
    /**
     * 是否已经删除 0没有删除  1已经删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallImageAssociative{" +
			"id=" + id +
			", imageUrl=" + imageUrl +
			", assId=" + assId +
			", isMainImages=" + isMainImages +
			", assType=" + assType +
			", assSort=" + assSort +
			", isDelete=" + isDelete +
			"}";
    }
}
