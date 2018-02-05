package com.gt.mall.entity.applet;

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
 * 小程序图片表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_applet_image" )
public class MallAppletImage extends Model< MallAppletImage > {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序图片标示
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 图片地址
     */
    @TableField( "image_url" )
    private String  imageUrl;
    /**
     * 跳转类型 0不跳转 1商品详情页面 2团购 3秒杀 4拍卖 5预售列表
     */
    private Integer type;
    /**
     * 商品id  关联t_mall_product表的id
     */
    @TableField( "pro_id" )
    private Integer proId;
    /**
     * 商家id 关联bus_user表的id
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
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallAppletImage{" +
            "id=" + id +
            ", imageUrl=" + imageUrl +
            ", type=" + type +
            ", proId=" + proId +
            ", busUserId=" + busUserId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            ", isShow=" + isShow +
            ", shopId=" + shopId +
            "}";
    }
}
