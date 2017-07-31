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
@TableName("purchase_carousel")
public class PurchaseCarousel extends Model<PurchaseCarousel> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 轮播图图片地址
     */
	@TableField("carousel_img")
	private String carouselImg;
    /**
     * 轮播图跳转链接地址
     */
	@TableField("carousel_url")
	private String carouselUrl;
    /**
     * 商家id
     */
	@TableField("bus_id")
	private Integer busId;
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
		return "PurchaseCarousel{" +
			"id=" + id +
			", carouselImg=" + carouselImg +
			", carouselUrl=" + carouselUrl +
			", busId=" + busId +
			", orderId=" + orderId +
			"}";
	}
}
