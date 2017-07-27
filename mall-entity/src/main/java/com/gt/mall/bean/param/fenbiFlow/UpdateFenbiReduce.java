package com.gt.mall.bean.param.fenbiFlow;

import java.io.Serializable;


/**
 * 冻结商家粉币业务参数
 * @author Administrator
 *
 */
public class UpdateFenbiReduce implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 商家ID
	 */
	private Integer busId;
	
	/**
	 * 冻结数量
	 */
	private Double count;
	
	/**
	 *  外键Id
	 */
	private Integer fkId;
	
	/**
	 * 活动类型
	 */
	private Integer freType;

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public Integer getFkId() {
		return fkId;
	}

	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}

	public Integer getFreType() {
		return freType;
	}

	public void setFreType(Integer freType) {
		this.freType = freType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
