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
@TableName("purchase_language")
public class PurchaseLanguage extends Model<PurchaseLanguage> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 留言时间
     */
	@TableField("language_time")
	private Date languageTime;
    /**
     * 留言内容
     */
	@TableField("language_content")
	private String languageContent;
    /**
     * 订单id
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * 0未阅1已阅
     */
	@TableField("is_read")
	private String isRead;
    /**
     * 0用户回复1商家回复
     */
	@TableField("admin_content")
	private String adminContent;
    /**
     * 用户id
     */
	@TableField("member_id")
	private Integer memberId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseLanguage{" +
			"id=" + id +
			", languageTime=" + languageTime +
			", languageContent=" + languageContent +
			", orderId=" + orderId +
			", isRead=" + isRead +
			", adminContent=" + adminContent +
			", memberId=" + memberId +
			"}";
	}
}
