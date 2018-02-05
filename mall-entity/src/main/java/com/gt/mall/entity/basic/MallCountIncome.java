package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 每日收入统计表
 * </p>
 *
 * @author yangqian
 * @since 2017-10-16
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_count_income" )
public class MallCountIncome extends Model< MallCountIncome > {

    private static final long serialVersionUID = 1L;

    /**
     * 每日收入统计标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 统计时间
     */
    @TableField( "count_date" )
    private Date       countDate;
    /**
     * 交易金额
     */
    @TableField( "trade_price" )
    private BigDecimal tradePrice;
    /**
     * 退款金额
     */
    @TableField( "refund_price" )
    private BigDecimal refundPrice;
    /**
     * 收入金额（订单完成7天后，计入收入金额）
     */
    @TableField( "income_price" )
    private BigDecimal incomePrice;
    /**
     * 营业额（交易-退款）
     */
    @TableField( "turnover" )
    private BigDecimal turnover;
    /**
     * 店铺ID
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 商家ID
     */
    @TableField( "bus_id" )
    private Integer    busId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
