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
@TableName("purchase_contract")
public class PurchaseContract extends Model<PurchaseContract> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 合同标题
     */
	@TableField("contract_title")
	private String contractTitle;
    /**
     * 合同内容
     */
	@TableField("contract_content")
	private String contractContent;
    /**
     * 商家id
     */
	@TableField("bus_id")
	private Integer busId;
    /**
     * 创建时间
     */
	@TableField("create_date")
	private Date createDate;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseContract{" +
			"id=" + id +
			", contractTitle=" + contractTitle +
			", contractContent=" + contractContent +
			", busId=" + busId +
			", createDate=" + createDate +
			"}";
	}
}
