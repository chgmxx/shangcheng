package com.gt.mall.exception;

import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.enums.ResponseEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常统一处理
 * <pre>
 *
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/21
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    // 全局默认错误页
    private static final String DEFAULT_ERROR_VIEW = "/error/404Two";

    private static Logger logger = LoggerFactory.getLogger( GlobalDefaultExceptionHandler.class );

    // 页面
    // 统一异常处理 页面跳转
    @ExceptionHandler( value = Exception.class )
    public ModelAndView defaultErrorHandlerModel( HttpServletRequest request, Exception e ) {
	ModelAndView modelAndView = new ModelAndView();
	modelAndView.addObject( "ex", e );
	modelAndView.addObject( "url", request.getRequestURL() );
	modelAndView.setViewName( DEFAULT_ERROR_VIEW );
	logger.error( e.getMessage() );
	e.printStackTrace();
	return modelAndView;
    }

    // 统一异常处理 Ajax请求
    @ResponseBody
    @ExceptionHandler( value = ResponseEntityException.class )
    public ErrorInfo< String > defaultErrorHandler( HttpServletRequest request, ResponseEntityException e ) {
	return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage(), request.getRequestURL().toString() );
    }
}
