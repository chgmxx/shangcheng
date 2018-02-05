package com.gt.mall.entity.product;

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
 * <p>
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product_monthcount" )
public class MallProductMonthcount extends Model< MallProductMonthcount > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * t_wx_shop 门店id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * t_mall_product 门店id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 商品每月（微信支付）销售数量
     */
    @TableField( "sale_num" )
    private Integer    saleNum;
    /**
     * 商品每月（微信支付）销售总金额
     */
    @TableField( "sale_price" )
    private BigDecimal salePrice;
    /**
     * 商品每月（存储卡）销售金额
     */
    @TableField( "sale_miprice" )
    private BigDecimal saleMiprice;
    /**
     * 商品每月（存储卡）销售数量
     */
    @TableField( "sale_minum" )
    private Integer    saleMinum;
    /**
     * 商品每月销售总数量
     */
    @TableField( "sale_totalnum" )
    private Integer    saleTotalnum;
    /**
     * 商品每月销售总价钱
     */
    @TableField( "sale_totalprice" )
    private BigDecimal saleTotalprice;
    /**
     * 商品每月（微信支付）退款数量
     */
    @TableField( "refund_num" )
    private Integer    refundNum;
    /**
     * 商品每月（微信支付）退款总金额
     */
    @TableField( "refund_price" )
    private BigDecimal refundPrice;
    /**
     * 统计日期
     */
    @TableField( "count_date" )
    private String     countDate;
    /**
     * 商品每月（存储卡）退款数量
     */
    @TableField( "refund_minum" )
    private Integer    refundMinum;
    /**
     * 商品每月（存储卡）退款金额
     */
    @TableField( "refund_miprice" )
    private BigDecimal refundMiprice;
    /**
     * 商品每月退款总数量
     */
    @TableField( "refund_totalnum" )
    private Integer    refundTotalnum;
    /**
     * 商品每月退款总价钱
     */
    @TableField( "refund_totalprice" )
    private BigDecimal refundTotalprice;
    /**
     * 统计的年份
     */
    private Integer    year;
    /**
     * 统计的月份
     */
    private Integer    month;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallProductMonthcount{" +
            "id=" + id +
            ", shopId=" + shopId +
            ", productId=" + productId +
            ", saleNum=" + saleNum +
            ", salePrice=" + salePrice +
            ", saleMiprice=" + saleMiprice +
            ", saleMinum=" + saleMinum +
            ", saleTotalnum=" + saleTotalnum +
            ", saleTotalprice=" + saleTotalprice +
            ", refundNum=" + refundNum +
            ", refundPrice=" + refundPrice +
            ", countDate=" + countDate +
            ", refundMinum=" + refundMinum +
            ", refundMiprice=" + refundMiprice +
            ", refundTotalnum=" + refundTotalnum +
            ", refundTotalprice=" + refundTotalprice +
            ", year=" + year +
            ", month=" + month +
            "}";
    }
}
