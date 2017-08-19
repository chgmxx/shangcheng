package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateUseCountById implements Serializable {

    private static final long serialVersionUID = 483863269775342271L;

    /**
     * 流量记录表ID
     */
    private Integer id;

    /**
     * 使用数量
     */
    private Integer useCount;

}
