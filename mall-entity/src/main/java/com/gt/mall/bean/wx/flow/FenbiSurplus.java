package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 查询粉币数量
 *
 * @author lfx
 */
@Getter
@Setter
public class FenbiSurplus implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户
     */
    private Integer busId;

    /**
     * 粉币-1
     */
    private Integer rec_type = 1;

    private Integer fre_type;

    /**
     * 外键Id
     */
    private Integer fkId;

}
