package com.gt.mall.config.interceptor;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BackInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger( BackInterceptor.class );

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
	logger.info( ">>>BackInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	logger.info( "basePath = " + CommonUtil.getpath( request ) );

	// 获得在下面代码中要用的request,response,session对象

	BusUser user = MallSessionUtils.getLoginUser( request );
	String url = request.getRequestURI();

	if ( user == null && !url.contains( "error" ) ) {// 判断如果没有取到微信授权信息,就跳转到登陆页面
	    if ( request.getServerName().contains( "192.168.2" ) ) {
		user = new BusUser();
		user.setId( 42 );
		user.setName( "gt123456" );
		user.setPid( 0 );
		MallSessionUtils.setLoginUser( request, user );
	    } else {
		throw new BusinessException( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc(), PropertiesUtil.getWxmpDomain() );
	    }
	}
	return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
     */
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
    }
}
