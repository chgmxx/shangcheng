package com.gt.mall.entity.purchase;

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
 * 
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Data
@Accessors(chain = true)
@TableName("purchase_term")
public class PurchaseTerm extends Model<PurchaseTerm> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单id
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * 分期序号
     */
	@TableField("term_index")
	private Integer termIndex;
    /**
     * 分期时间
     */
	@TableField("term_time")
	private Date termTime;
    /**
     * 分期金额
     */
	@TableField("term_money")
	private Double termMoney;
    /**
     * 0未付款1已付款
     */
	@TableField("term_buy")
	private String termBuy;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseTerm{" +
			"id=" + id +
			", orderId=" + orderId +
			", termIndex=" + termIndex +
			", termTime=" + termTime +
			", termMoney=" + termMoney +
			", termBuy=" + termBuy +
			"}";
	}
}
