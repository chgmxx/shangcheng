package com.gt.mall.bean.wx.shop;

public class WsWxShopInfoExtend extends WsWxShopInfo {

    private static final long serialVersionUID = -4076066116670785438L;
    /**
     * 地址
     */
    private String adder;

    /**
     * 门店图片
     */
    private String imageUrl;

    public String getAdder() {
	return adder;
    }

    public void setAdder( String adder ) {
	this.adder = adder;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public void setImageUrl( String imageUrl ) {
	this.imageUrl = imageUrl;
    }

}
