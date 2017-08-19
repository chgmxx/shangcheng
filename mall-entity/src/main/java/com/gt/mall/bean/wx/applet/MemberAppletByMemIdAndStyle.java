package com.gt.mall.bean.wx.applet;

import java.io.Serializable;

public class MemberAppletByMemIdAndStyle implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 小程序类型
     */
    private Integer style;

    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public Integer getStyle() {
	return style;
    }

    public void setStyle( Integer style ) {
	this.style = style;
    }
}
