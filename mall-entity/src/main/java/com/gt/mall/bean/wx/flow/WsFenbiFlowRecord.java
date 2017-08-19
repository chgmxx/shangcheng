package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WsFenbiFlowRecord implements Serializable {

    private static final long serialVersionUID = 104865070999221300L;
    /**
     * ID
     */
    private Integer id;

    /**
     * 商家ID
     */
    private Integer busUserId;

    /**
     * 冻结类型
     */
    private Integer recType;

    /**
     * 冻结数量
     */
    private Double recCount;

    /**
     * 已使用数量
     */
    private Double recUseCount;

    /**
     * 创建时间
     */
    private String recCreatetime;

    /**
     * 描述
     */
    private String recDesc;

    /**
     * 活动类型
     */
    private Integer recFreezeType;

    /**
     * 外键ID
     */
    private Integer recFkId;

    /**
     * 是否回滚
     */
    private Integer rollStatus;

    /**
     * 流量类型
     */
    private Integer flowType = 0;

    /**
     * 流量记录ID
     */
    private Integer flowId = 0;

}