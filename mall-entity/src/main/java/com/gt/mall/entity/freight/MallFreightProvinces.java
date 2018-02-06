package com.gt.mall.entity.freight;

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
 * 物流配送区域
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_freight_provinces" )
public class MallFreightProvinces extends Model< MallFreightProvinces > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 物流id  关联t_mall_freight表的id
     */
    @TableField( "freight_id" )
    private Integer freightId;
    /**
     * 物流详情id，关联t_mall_freight_detail表的id字段
     */
    @TableField( "freight_detail_id" )
    private Integer freightDetailId;
    /**
     * 配送省份id
     */
    @TableField( "province_id" )
    private Integer provinceId;
    /**
     * 配送省份
     */
    @TableField( "province_name" )
    private String  provinceName;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallFreightProvinces{" +
            "id=" + id +
            ", freightId=" + freightId +
            ", freightDetailId=" + freightDetailId +
            ", provinceId=" + provinceId +
            ", provinceName=" + provinceName +
            ", isDelete=" + isDelete +
            "}";
    }
}
