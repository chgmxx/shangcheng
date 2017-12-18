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
@TableName("purchase_order_statistics")
public class PurchaseOrderStatistics extends Model<PurchaseOrderStatistics> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单id
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * 用户id
     */
	@TableField("member_id")
	private Integer memberId;

	/**
	 * 用户名称
	 */
	@TableField("member_name")
	private String memberName;

	/**
	 * 用户头像
	 */
	@TableField("member_headimgurl")
	private String memberHeadimgurl;
    /**
     * 浏览时间
     */
	@TableField("look_date")
	private Date lookDate;
    /**
     * 浏览的ip
     */
	@TableField("look_ip")
	private String lookIp;
    /**
     * 商家id
     */
	@TableField("bus_id")
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseOrderStatistics{" +
			"id=" + id +
			", orderId=" + orderId +
			", memberId=" + memberId +
			", lookDate=" + lookDate +
			", lookIp=" + lookIp +
			", busId=" + busId +
			"}";
	}
}
