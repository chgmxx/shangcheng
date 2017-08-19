package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流量归属地的返回值
 */
@Getter
@Setter
public class GetMobileInfo implements Serializable {

    private static final long serialVersionUID = -1341826638015192406L;
    /**
     * 省份
     */
    private String province;

    /**
     * 运营商
     */
    private String supplier;

    /*
     * 城市
     */
    private String city;

}
