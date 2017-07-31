package com.gt.mall.entity.purchase;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("purchase_contract_order")
public class PurchaseContractOrder extends Model<PurchaseContractOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
	@TableField("contract_id")
	private Integer contractId;
    /**
     * 订单id
     */
	@TableField("order_id")
	private Integer orderId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseContractOrder{" +
			"contractId=" + contractId +
			", orderId=" + orderId +
			"}";
	}
}
