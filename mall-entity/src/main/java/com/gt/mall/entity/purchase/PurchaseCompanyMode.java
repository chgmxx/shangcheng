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
@TableName("purchase_company_mode")
public class PurchaseCompanyMode extends Model<PurchaseCompanyMode> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 公司名称
     */
	@TableField("company_name")
	private String companyName;
    /**
     * 公司地址
     */
	@TableField("company_address")
	private String companyAddress;
    /**
     * 公司联系电话
     */
	@TableField("company_tel")
	private String companyTel;
    /**
     * 公司官网
     */
	@TableField("company_internet")
	private String companyInternet;
    /**
     * 商家id
     */
	@TableField("bus_id")
	private Integer busId;
    /**
     * 经纬度
     */
	private String longitude;
    /**
     * 经纬度
     */
	private String latitude;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PurchaseCompanyMode{" +
			"id=" + id +
			", companyName=" + companyName +
			", companyAddress=" + companyAddress +
			", companyTel=" + companyTel +
			", companyInternet=" + companyInternet +
			", busId=" + busId +
			", longitude=" + longitude +
			", latitude=" + latitude +
			"}";
	}
}
