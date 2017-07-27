package com.gt.mall.bean.param.sms;

import java.io.Serializable;


/**
 * 旧版本短信发送接口
 * @author Administrator
 *
 */
public class OldApiSms  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 手机号码，多个手机号码以逗号隔开
	 */
	private String mobiles;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 公司名称
	 */
	private String company;
	
	/**
	 * 商家ID
	 */
	private Integer busId; 
	
	/**
	 * 短信模块ID(字典1071)
	 */
	private Integer model;

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
