package com.gt.mall.entity.presale;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品预售时间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale_time" )
public class MallPresaleTime extends Model< MallPresaleTime > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 预售id 关联t_mall_presale表的id
     */
    @TableField( "presale_id" )
    private Integer    presaleId;
    /**
     * 开始时间
     */
    @TableField( "start_time" )
    private String     startTime;
    /**
     * 结束时间
     */
    @TableField( "end_time" )
    private String     endTime;
    /**
     * 调整类型  0正常价格   1 上涨金额    2下调金额
     */
    @TableField( "sale_type" )
    private Integer    saleType;
    /**
     * 上涨下调金额  百分数    price/100
     */
    private BigDecimal price;
    /**
     * 是否删除  0 未删除   1 已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 预售价格类型  1 按百分比 2 按价格
     */
    @TableField( "price_type" )
    private Integer    priceType;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPresaleTime{" +
			"id=" + id +
			", presaleId=" + presaleId +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", saleType=" + saleType +
			", price=" + price +
			", isDelete=" + isDelete +
			", priceType=" + priceType +
			"}";
    }
}
