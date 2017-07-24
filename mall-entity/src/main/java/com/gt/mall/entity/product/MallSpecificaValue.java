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
 * 用户添加规格值
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_specifica_value" )
public class MallSpecificaValue extends Model< MallSpecificaValue > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品规格标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 规格名id 关联t_mall_specifica表的id
     */
    @TableField( "spec_id" )
    private Integer specId;
    /**
     * 规格值
     */
    @TableField( "spec_value" )
    private String  specValue;
    /**
     * 用户id 关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 是否已经删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 类型  1商品规格值   2 商品参数值
     */
    private Integer type;
    /**
     * 关联erp规格值id（父类的规格id） 0为没有关联erp
     */
    @TableField( "erp_value_id" )
    private Integer erpValueId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSpecificaValue{" +
			"id=" + id +
			", specId=" + specId +
			", specValue=" + specValue +
			", userId=" + userId +
			", isDelete=" + isDelete +
			", type=" + type +
			", erpValueId=" + erpValueId +
			"}";
    }
}
