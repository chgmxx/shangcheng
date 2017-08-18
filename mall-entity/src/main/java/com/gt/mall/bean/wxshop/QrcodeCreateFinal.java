package com.gt.mall.bean.wxshop;

import java.io.Serializable;

public class QrcodeCreateFinal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 场景值
     */
    private String scene_id;

    /**
     * 公众号ID
     */
    private Integer publicId;

    public String getScene_id() {
	return scene_id;
    }

    public void setScene_id( String scene_id ) {
	this.scene_id = scene_id;
    }

    public Integer getPublicId() {
	return publicId;
    }

    public void setPublicId( Integer publicId ) {
	this.publicId = publicId;
    }

}
