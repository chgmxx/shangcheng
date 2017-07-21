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
 * 商品分组
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_group" )
public class MallGroup extends Model< MallGroup > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品分组标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 分组名称
     */
    @TableField( "group_name" )
    private String  groupName;
    /**
     * 父级分类
     */
    @TableField( "group_p_id" )
    private Integer groupPId;
    /**
     * 第一优先级
     */
    @TableField( "first_priority" )
    private Integer firstPriority;
    /**
     * 第二优先级
     */
    @TableField( "second_priority" )
    private Integer secondPriority;
    /**
     * 是否在页面显示分组名称 0 不显示  1 显示
     */
    @TableField( "is_show_page" )
    private Integer isShowPage;
    /**
     * 店铺id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 是否是一级父类
     * 0不是一级父类  1 是一级父类
     */
    @TableField( "is_first_parents" )
    private Integer isFirstParents;
    /**
     * 是否有子类 0没有子类  1有子类
     */
    @TableField( "is_child" )
    private Integer isChild;
    /**
     * 分组排序
     */
    private Integer sort;
    /**
     * 创建人id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 修改人id  关联bus_user表的id
     */
    @TableField( "edit_user_id" )
    private Integer editUserId;
    /**
     * 修改时间
     */
    @TableField( "edit_time" )
    private Date    editTime;
    /**
     * 是否已经删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallGroup{" +
			"id=" + id +
			", groupName=" + groupName +
			", groupPId=" + groupPId +
			", firstPriority=" + firstPriority +
			", secondPriority=" + secondPriority +
			", isShowPage=" + isShowPage +
			", shopId=" + shopId +
			", isFirstParents=" + isFirstParents +
			", isChild=" + isChild +
			", sort=" + sort +
			", userId=" + userId +
			", createTime=" + createTime +
			", editUserId=" + editUserId +
			", editTime=" + editTime +
			", isDelete=" + isDelete +
			"}";
    }
}
