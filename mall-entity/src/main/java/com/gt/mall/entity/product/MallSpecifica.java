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
 * 用户添加规格
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_specifica" )
public class MallSpecifica extends Model< MallSpecifica > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品规格标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 规格名称
     */
    @TableField( "spec_name" )
    private String  specName;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 用户id 关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否已经删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 是否是后台添加 0不是后台添加 1是后台添加
     */
    @TableField( "is_back_end" )
    private Integer isBackEnd;
    /**
     * 类型  1商品规格名称   2 商品参数名称
     */
    private Integer type;
    /**
     * 关联erp规格名称id（父类的规格id） 0为没有关联erp
     */
    @TableField( "erp_name_id" )
    private Integer erpNameId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSpecifica{" +
            "id=" + id +
            ", specName=" + specName +
            ", shopId=" + shopId +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            ", isBackEnd=" + isBackEnd +
            ", type=" + type +
            ", erpNameId=" + erpNameId +
            "}";
    }
}
