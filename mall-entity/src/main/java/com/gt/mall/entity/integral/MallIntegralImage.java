package com.gt.mall.entity.integral;

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
 * 积分商城图片循环
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_integral_image" )
public class MallIntegralImage extends Model< MallIntegralImage > {

    private static final long serialVersionUID = 1L;

    /**
     * 积分图片标示
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 图片所属店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 图片地址
     */
    @TableField( "image_url" )
    private String  imageUrl;
    /**
     * 跳转类型 0不跳转 1跳转链接
     */
    private Integer type;
    /**
     * 跳转链接
     */
    @TableField( "return_url" )
    private String  returnUrl;
    /**
     * 商家id  关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否删除   0不删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 是否显示   0不显示  1显示
     */
    @TableField( "is_show" )
    private Integer isShow;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallIntegralImage{" +
			"id=" + id +
			", shopId=" + shopId +
			", imageUrl=" + imageUrl +
			", type=" + type +
			", returnUrl=" + returnUrl +
			", busUserId=" + busUserId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", isShow=" + isShow +
			"}";
    }
}
