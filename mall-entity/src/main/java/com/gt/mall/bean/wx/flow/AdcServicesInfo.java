package com.gt.mall.bean.wx.flow;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流量充值
 *
 * @author Administrator
 */
@Setter
@Getter
public class AdcServicesInfo implements Serializable {

    private static final long serialVersionUID = 7582779831484200211L;
    /**
     * 模块ID
     */
    private Integer model;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 流量数
     */
    private Integer prizeCount;

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 公众号ID
     */
    private Integer publicId;

    /**
     * 商家ID
     */
    private Integer busId;

    /**
     * 订单id
     */
    private Integer id;

}
