package com.gt.mall.bean.result.common;

import java.io.Serializable;

public class GetMobileInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 省份
	 */
	private String province;
	
	

	/**
	 * 运营商
	 */
	private String supplier;
	
	
	/*
	 * 城市
	 */
	private String city;


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getSupplier() {
		return supplier;
	}


	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}

}
