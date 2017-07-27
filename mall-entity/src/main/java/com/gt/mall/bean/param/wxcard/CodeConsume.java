package com.gt.mall.bean.param.wxcard;

import java.io.Serializable;


/**
 * 卡券核销
 * @author Administrator
 *
 */
public class CodeConsume  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 优惠券微信卡ID
	 */
	private String card_id;
	
	
	/**
	 * 核销码
	 */
	private String code;
	
	/**
	 * 商家ID
	 */
	private Integer busId;
	
	


	public String getCard_id() {
		return card_id;
	}

	/**
	 * 优惠券微信卡ID
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}


	public String getCode() {
		return code;
	}

	/**
	 * 核销码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public Integer getBusId() {
		return busId;
	}

	/**
	 * 商家ID
	 */
	public void setBusId(Integer busId) {
		this.busId = busId;
	}
}
