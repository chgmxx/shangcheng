package com.gt.mall.bean.wx;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 消息模板实体类
 */
@Setter
@Getter
public class SendWxMsgTemplate implements Serializable {

    private static final long serialVersionUID = 5777690437400522303L;
    /**
     * ，模板表ID
     */
    private Integer id;

    /**
     * 跳转url
     */
    private String url;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 公众号ID
     */
    private Integer publicId;

    /**
     * 模板所需参数
     */
    private List< Object > objs;

}
