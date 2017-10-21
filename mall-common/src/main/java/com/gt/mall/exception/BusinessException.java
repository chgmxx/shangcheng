package com.gt.mall.exception;

import org.apache.poi.ss.formula.functions.T;

/**
 * 业务异常
 */
public class BusinessException extends SystemException {

    public BusinessException( String message ) {
	super( message );
    }

    public BusinessException( int code, String desc, String message ) {
	super( message );
    }

    public BusinessException( int code, String message ) {
	super( code, message );
    }

    public BusinessException( int code, String message, T data ) {
	super( code, message, data );
    }
}
