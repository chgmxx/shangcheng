package com.gt.mall.result.phone;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 批发商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@Getter
@Setter
public class PhonePifaProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    /**
     * 批发设置
     */
    private JSONObject pfSetObj;

    /**
     * 批发状态
     */
    private int pfStatus = -2;

    /**
     * 批发错误提醒
     */
    private String pfErrorMsg = "";

    /**
     * 批发类型 1手批 2混批
     */
    private int pfType;

}
