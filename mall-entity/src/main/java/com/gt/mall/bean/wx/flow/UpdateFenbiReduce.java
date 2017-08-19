package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 冻结商家粉币业务参数
 *
 * @author Administrator
 */
@Getter
@Setter
public class UpdateFenbiReduce implements Serializable {

    private static final long serialVersionUID = -5502864188291162231L;
    /**
     * 商家ID
     */
    private Integer busId;

    /**
     * 冻结数量
     */
    private Double count;

    /**
     * 外键Id
     */
    private Integer fkId;

    /**
     * 活动类型
     */
    private Integer freType;

}
