package com.gt.mall.entity.purchase;

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
 * 
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Data
@Accessors(chain = true)
@TableName("purchase_order_details")
public class PurchaseOrderDetails extends Model<PurchaseOrderDetails> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单id
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * 商品id
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 商品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 商品图片
     */
	@TableField("product_img")
	private String productImg;
    /**
     * 商品详情
     */
	@TableField("product_details")
	private String productDetails;
    /**
     * 人工费
     */
	@TableField("labor_cost")
	private Double laborCost;
    /**
     * 安装费
     */
	@TableField("installation_fee")
	private Double installationFee;
    /**
     * 运费
     */
	private Double freight;
    /**
     * 原价
     */
	private Double money;
    /**
     * 优惠价
     */
	@TableField("discount_money")
	private Double discountMoney;
    /**
     * 数量
     */
	private Integer count;
    /**
     * 总额
     */
	@TableField("all_money")
	private Double allMoney;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseOrderDetails{" +
			"id=" + id +
			", orderId=" + orderId +
			", productId=" + productId +
			", productName=" + productName +
			", productImg=" + productImg +
			", productDetails=" + productDetails +
			", laborCost=" + laborCost +
			", installationFee=" + installationFee +
			", freight=" + freight +
			", money=" + money +
			", discountMoney=" + discountMoney +
			", count=" + count +
			", allMoney=" + allMoney +
			"}";
	}
}
