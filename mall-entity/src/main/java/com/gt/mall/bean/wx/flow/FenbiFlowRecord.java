package com.gt.mall.bean.wx.flow;

import java.util.Date;

public class FenbiFlowRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.bus_user_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Integer busUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Integer recType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Double recCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_use_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Double recUseCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_createTime
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Date recCreatetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_desc
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private String recDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_freeze_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Integer recFreezeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_wx_fenbi_flow_record.rec_fk_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    private Integer recFkId;
    
    private Integer rollStatus;
    
    private Integer flowType=0;
    
    private Integer flowId=0;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.id
     *
     * @return the value of t_wx_fenbi_flow_record.id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.id
     *
     * @param id the value for t_wx_fenbi_flow_record.id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.bus_user_id
     *
     * @return the value of t_wx_fenbi_flow_record.bus_user_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Integer getBusUserId() {
        return busUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.bus_user_id
     *
     * @param busUserId the value for t_wx_fenbi_flow_record.bus_user_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setBusUserId(Integer busUserId) {
        this.busUserId = busUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_type
     *
     * @return the value of t_wx_fenbi_flow_record.rec_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Integer getRecType() {
        return recType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_type
     *
     * @param recType the value for t_wx_fenbi_flow_record.rec_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecType(Integer recType) {
        this.recType = recType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_count
     *
     * @return the value of t_wx_fenbi_flow_record.rec_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Double getRecCount() {
        return recCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_count
     *
     * @param recCount the value for t_wx_fenbi_flow_record.rec_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecCount(Double recCount) {
        this.recCount = recCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_use_count
     *
     * @return the value of t_wx_fenbi_flow_record.rec_use_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Double getRecUseCount() {
        return recUseCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_use_count
     *
     * @param recUseCount the value for t_wx_fenbi_flow_record.rec_use_count
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecUseCount(Double recUseCount) {
        this.recUseCount = recUseCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_createTime
     *
     * @return the value of t_wx_fenbi_flow_record.rec_createTime
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Date getRecCreatetime() {
        return recCreatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_createTime
     *
     * @param recCreatetime the value for t_wx_fenbi_flow_record.rec_createTime
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecCreatetime(Date recCreatetime) {
        this.recCreatetime = recCreatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_desc
     *
     * @return the value of t_wx_fenbi_flow_record.rec_desc
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public String getRecDesc() {
        return recDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_desc
     *
     * @param recDesc the value for t_wx_fenbi_flow_record.rec_desc
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecDesc(String recDesc) {
        this.recDesc = recDesc == null ? null : recDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_freeze_type
     *
     * @return the value of t_wx_fenbi_flow_record.rec_freeze_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Integer getRecFreezeType() {
        return recFreezeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_freeze_type
     *
     * @param recFreezeType the value for t_wx_fenbi_flow_record.rec_freeze_type
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecFreezeType(Integer recFreezeType) {
        this.recFreezeType = recFreezeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_wx_fenbi_flow_record.rec_fk_id
     *
     * @return the value of t_wx_fenbi_flow_record.rec_fk_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public Integer getRecFkId() {
        return recFkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_wx_fenbi_flow_record.rec_fk_id
     *
     * @param recFkId the value for t_wx_fenbi_flow_record.rec_fk_id
     *
     * @mbggenerated Wed Jan 06 10:13:01 CST 2016
     */
    public void setRecFkId(Integer recFkId) {
        this.recFkId = recFkId;
    }

	public Integer getRollStatus() {
		return rollStatus;
	}

	public void setRollStatus(Integer rollStatus) {
		this.rollStatus = rollStatus;
	}

	public Integer getFlowType() {
		return flowType;
	}

	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}

	public Integer getFlowId() {
		return flowId;
	}

	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}

	public FenbiFlowRecord(Integer id, Integer busUserId, Integer recType,
			Double recCount, Double recUseCount, Date recCreatetime,
			String recDesc, Integer recFreezeType, Integer recFkId,
			Integer rollStatus, Integer flowType, Integer flowId) {
		super();
		this.id = id;
		this.busUserId = busUserId;
		this.recType = recType;
		this.recCount = recCount;
		this.recUseCount = recUseCount;
		this.recCreatetime = recCreatetime;
		this.recDesc = recDesc;
		this.recFreezeType = recFreezeType;
		this.recFkId = recFkId;
		this.rollStatus = rollStatus;
		this.flowType = flowType;
		this.flowId = flowId;
	}

	public FenbiFlowRecord() {
		super();
	}
	
	
	
}