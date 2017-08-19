package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 查询粉币数量返回业务参数
 *
 * @author lfx
 */
@Getter
@Setter
public class FenBiCount implements Serializable {

    private static final long serialVersionUID = -3141138780121063643L;
    /**
     * 粉币数
     */
    private Double count;


}
