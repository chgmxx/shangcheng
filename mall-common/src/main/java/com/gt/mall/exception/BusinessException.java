package com.gt.mall.exception;

/**
 * 业务异常
 */
public class BusinessException extends SystemException {

    public BusinessException( String message ) {
	super( message );
    }

    public BusinessException( int code, String message ) {
	super( code, message );
    }

    public BusinessException( int code, String message, String data ) {
	super( code, message, data );
    }

}
