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
 * 商城搜索表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_search_keyword" )
public class MallSearchKeyword extends Model< MallSearchKeyword > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 关键字
     */
    private String  keyword;
    /**
     * 搜索次数
     */
    @TableField( "search_num" )
    private Integer searchNum;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 搜索用户id 关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 编辑时间
     */
    @TableField( "edit_time" )
    private Date    editTime;
    /**
     * 是否删除 0未删除   1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSearchKeyword{" +
            "id=" + id +
            ", keyword=" + keyword +
            ", searchNum=" + searchNum +
            ", shopId=" + shopId +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", editTime=" + editTime +
            ", isDelete=" + isDelete +
            "}";
    }
}
