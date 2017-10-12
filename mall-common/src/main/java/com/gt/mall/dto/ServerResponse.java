package com.gt.mall.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;

import java.io.Serializable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.DEFAULT_TYPING;

/**
 * 服务响应类
 * <pre>
 *     统一响应返回数据格式
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/16
 */
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize( typing = DEFAULT_TYPING )
public class ServerResponse< T > implements Serializable {

    /*状态码*/
    private int code;

    /*返回消息*/
    private String msg;

    /*图片域名*/
    private String imgUrl = PropertiesUtil.getResourceUrl();

    /*本地域名*/
    private String path = PropertiesUtil.getHomeUrl();

    /*前端web域名*/
    private String webPath = PropertiesUtil.getHomeUrl();

    /*泛型数据*/
    private T data;

    protected ServerResponse( int code, Boolean... isShowPath ) {
	this.code = code;
	isSowPath( isShowPath );
    }

    protected ServerResponse( int code, T data, Boolean... isShowPath ) {
	this.code = code;
	this.data = data;
	isSowPath( isShowPath );
    }

    protected ServerResponse( int code, String msg, Boolean... isShowPath ) {
	this.code = code;
	this.msg = msg;
	isSowPath( isShowPath );
    }

    protected ServerResponse( int code, String msg, T data, Boolean... isShowPath ) {
	this.code = code;
	this.msg = msg;
	this.data = data;
	isSowPath( isShowPath );
    }

    private void isSowPath( Boolean... isShowPath ) {
	boolean isShow = CommonUtil.isEmpty( isShowPath ) || isShowPath.length == 0 ? true : isShowPath[0];
	if ( isShow ) {
	    this.imgUrl = PropertiesUtil.getResourceUrl();
	    this.path = PropertiesUtil.getHomeUrl();
	    this.webPath = PropertiesUtil.getHomeUrl();
	} else {
	    this.imgUrl = null;
	    this.path = null;
	    this.webPath = null;
	}

    }

    /**
     * 创建响应成功
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess() {
	return createBySuccessMessage( ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 创建响应成功
     *
     * @param data 数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess( T data ) {
	return createBySuccess( null, data );
    }

    /**
     * 创建响应成功
     *
     * @param msg 返回消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessMessage( String msg ) {
	return createBySuccess( msg, null );
    }

    /**
     * 创建响应成功
     *
     * @param msg  消息
     * @param data 数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess( String msg, T data ) {
	return createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), msg, data );
    }

    /**
     * 创建响应成功
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessCodeMessage( int code, String msg, T data, Boolean... isShowPath ) {
	return new ServerResponse<>( code, msg, data, isShowPath );
    }

    /**
     * 创建响应成功
     *
     * @param code 状态码
     * @param msg  消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessCodeMessage( int code, String msg, Boolean... isShowPath ) {
	return new ServerResponse<>( code, msg, isShowPath );
    }

    /**
     * 创建响应成功
     *
     * @param code 状态吗
     * @param data 数据
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessCodeData( int code, T data, Boolean... isShowPath ) {
	return new ServerResponse< T >( code, data, isShowPath );
    }

    /**
     * 创建响应失败
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByError() {
	return createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
    }

    /**
     * 创建响应失败
     *
     * @param errorMessage 消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByErrorMessage( String errorMessage ) {
	return createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), errorMessage );
    }

    /**
     * 创建响应失败
     *
     * @param errorCode    状态码
     * @param errorMessage 消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByErrorCodeMessage( int errorCode, String errorMessage ) {
	return new ServerResponse<>( errorCode, errorMessage, false );
    }

    //使之不在json序列化结果当中，作用用于判断
    @JsonIgnore
    public boolean isSuccess() {
	return this.code == ResponseEnums.SUCCESS.getCode();
    }

    public int getCode() {
	return code;
    }

    public T getData() {
	return data;
    }

    public String getMsg() {
	return msg;
    }

    public String getImgUrl() {
	return imgUrl;
    }

    public String getPath() {
	return path;
    }

    public String getWebPath() {
	return webPath;
    }
}
