package com.gt.mall.bean.param.fenbiFlow;

import java.io.Serializable;

/**
 * 查询粉币数量
 * @author lfx
 *
 */
public class FenbiSurplus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户
	 */
	private Integer busId ;
	
	/**
	 * 粉币-1
	 */
	private Integer rec_type=1;
	
	private Integer fre_type;
	
	
	/**
	 * 外键Id
	 */
	private Integer fkId;


	public Integer getBusId() {
		return busId;
	}


	public void setBusId(Integer busId) {
		this.busId = busId;
	}


	public Integer getRec_type() {
		return rec_type;
	}


	public void setRec_type(Integer rec_type) {
		this.rec_type = rec_type;
	}


	public Integer getFkId() {
		return fkId;
	}


	public void setFkId(Integer fkId) {
		this.fkId = fkId;
	}


	public Integer getFre_type() {
		return fre_type;
	}


	public void setFre_type(Integer fre_type) {
		this.fre_type = fre_type;
	}

}
