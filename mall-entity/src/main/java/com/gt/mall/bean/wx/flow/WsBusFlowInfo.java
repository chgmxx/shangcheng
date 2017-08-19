package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WsBusFlowInfo implements Serializable {

    private static final long serialVersionUID = -7105459882819766289L;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 商家Id
     */
    private Integer busId;

    /**
     * 运营商 1：电信  2 移动  3 联通
     */
    private Integer operateType;

    /**
     * 规格：10M 30M 100M 200M 500M
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer count;

}