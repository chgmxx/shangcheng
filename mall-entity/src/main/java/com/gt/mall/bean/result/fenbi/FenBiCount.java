package com.gt.mall.bean.result.fenbi;

import java.io.Serializable;


/**
 * 查询粉币数量返回业务参数
 * @author lfx
 *
 */
public class FenBiCount implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 粉币数
	 */
	private Double count;
	public Double getCount() {
		return count;
	}
	public void setCount(Double count) {
		this.count = count;
	}

}
