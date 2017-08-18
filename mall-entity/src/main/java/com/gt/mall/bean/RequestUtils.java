package com.gt.mall.bean;

import java.io.Serializable;

/**
 * 参数父级类
 *
 * @author Administrator
 */
public class RequestUtils< T > implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务请求参数
     */
    private T reqdata;

    /**
     * 签名
     */
    private String sign;

    private String signKey;

    public RequestUtils() {

    }

    /**
     * 插入请求参数
     */
    public RequestUtils( T reqdata, String action ) {
	this.reqdata = reqdata;
    }

    public T getReqdata() {
	return reqdata;
    }

    public void setReqdata( T reqdata ) {
	this.reqdata = reqdata;
    }

    public String getSignKey() {
	return signKey;
    }

    public void setSignKey( String signKey ) {
	this.signKey = signKey;
    }

    public String getSign() {
	return sign;
    }

    public void setSign( String sign ) {
	this.sign = sign;
    }

}
