package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 查询手机归属地参数
 */
@Getter
@Setter
public class ReqGetMobileInfo implements Serializable {

    private static final long serialVersionUID = -4385830891177290373L;
    /**
     * 手机号码
     */
    private String phone;

}
