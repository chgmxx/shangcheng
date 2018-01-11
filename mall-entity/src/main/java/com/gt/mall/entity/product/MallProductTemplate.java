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
 * 商品页模板表
 * </p>
 *
 * @author yangqian
 * @since 2017-09-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_template" )
public class MallProductTemplate extends Model< MallProductTemplate > {

    private static final long serialVersionUID = 1L;

    /**
     * 商品页模板标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 模板名称
     */
    private String  name;
    /**
     * 样式
     */
    @TableField( "template_css" )
    private String  templateCss;
    /**
     * 数据
     */
    @TableField( "template_data" )
    private String  templateData;
    /**
     * 用户id(bus_user中的id)
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 店铺ID
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否已经删除 0 未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
