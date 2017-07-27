package com.gt.mall.bean.result.wacard;

import java.io.Serializable;


/**
 * 核销返回业务参数
 * @author Administrator
 *
 */
public class CodeConsumeRetrun  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 优惠卡券ID
	 */
	private String card;
	
	/**
	 * 微信用户ID
	 */
	private String openid;

	/**
	 * 优惠卡券ID
	 */
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	/**
	 * 微信用户ID
	 */
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
