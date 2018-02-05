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
 * 商城搜索标签表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_search_label" )
public class MallSearchLabel extends Model< MallSearchLabel > {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索标签标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 标签类型  1 推荐分类id    2自定义推荐
     */
    @TableField( "label_type" )
    private Integer labelType;
    /**
     * 店铺id（关联t_mall_store表的id）
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 分类id （关联t_mall_group表的id）
     */
    @TableField( "group_id" )
    private Integer groupId;
    /**
     * 用户id（关联bus_user表的id）
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 是否已经删除（0未删除    1已删除）
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSearchLabel{" +
            "id=" + id +
            ", labelType=" + labelType +
            ", shopId=" + shopId +
            ", groupId=" + groupId +
            ", userId=" + userId +
            ", isDelete=" + isDelete +
            ", createTime=" + createTime +
            "}";
    }
}
