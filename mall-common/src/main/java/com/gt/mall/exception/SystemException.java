package com.gt.mall.exception;

/**
 * 系统统一异常类
 * <pre>
 *     所有自定义的异常，都继承此类。
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/16
 */
public class SystemException extends RuntimeException {

    private int code;//状态码

    private String message;//错误消息

    private String data;//异常内容

    public SystemException( String message ) {
	super( message );
	this.message = message;
    }

    public SystemException( int code, String message ) {
	super( message );
	this.message = message;
	this.code = code;
    }

    public SystemException( int code, String message, String data ) {
	super( message );
	this.message = message;
	this.code = code;
	this.data = data;
    }

    public int getCode() {
	return code;
    }

    @Override
    public String getMessage() {
	return message;
    }

    public String getData() {
	return data;
    }

}
