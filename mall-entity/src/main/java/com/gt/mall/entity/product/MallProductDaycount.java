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
@TableName( "t_mall_product_daycount" )
public class MallProductDaycount extends Model< MallProductDaycount > {

    private static final long serialVersionUID = 1L;

    /**
     * 主id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * t_wx_shop 门店id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * t_mall_product 商品id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 单商品每天销售(微信支付的)数量
     */
    @TableField( "sale_num" )
    private Integer    saleNum;
    /**
     * 单商品每天销售（微信支付线上）价钱
     */
    @TableField( "sale_price" )
    private BigDecimal salePrice;
    /**
     * 单商品每天销售（会员）价钱
     */
    @TableField( "sale_miprice" )
    private BigDecimal saleMiprice;
    /**
     * 单商品每天销售（会员）支付数量
     */
    @TableField( "sale_minum" )
    private Integer    saleMinum;
    /**
     * 单商品每天销售数量
     */
    @TableField( "sale_totalnum" )
    private Integer    saleTotalnum;
    /**
     * 单商品每天销售总价钱
     */
    @TableField( "sale_totalprice" )
    private BigDecimal saleTotalprice;
    /**
     * 单商品每天退款（微信支付）数量
     */
    @TableField( "refund_num" )
    private Integer    refundNum;
    /**
     * 单商品每天退款（微信支付线上）总价钱
     */
    @TableField( "refund_price" )
    private BigDecimal refundPrice;
    /**
     * 统计日期
     */
    @TableField( "count_date" )
    private String     countDate;
    /**
     * 单商品每天退款（会员）数量
     */
    @TableField( "refund_minum" )
    private Integer    refundMinum;
    /**
     * 单商品每天退款（会员）金额
     */
    @TableField( "refund_miprice" )
    private BigDecimal refundMiprice;
    /**
     * 单商品每天退款数量
     */
    @TableField( "refund_totalnum" )
    private Integer    refundTotalnum;
    /**
     * 单商品每天退款总金额
     */
    @TableField( "refund_totalprice" )
    private BigDecimal refundTotalprice;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallProductDaycount{" +
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
			"}";
    }
}
