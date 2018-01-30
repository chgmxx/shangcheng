package com.gt.mall.config.interceptor;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BackInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger( BackInterceptor.class );

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
	//	logger.info( ">>>BackInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	logger.info( ">>>BackInterceptor>>basePath = " + CommonUtil.getpath( request ) );

	long startTime = System.currentTimeMillis();
	request.setAttribute( "runStartTime", startTime );

	// 获得在下面代码中要用的request,response,session对象

	BusUser user = MallSessionUtils.getLoginUser( request );
	String url = request.getRequestURI();
	if ( user == null && !url.contains( "error" ) ) {// 判断如果没有取到微信授权信息,就跳转到登陆页面
	    throw new BusinessException( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc(), PropertiesUtil.getWxmpDomain() );
	}
	return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
     */
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception {

	long startTime = (Long) request.getAttribute( "runStartTime" );

	long endTime = System.currentTimeMillis();

	long executeTime = endTime - startTime;

	HandlerMethod handlerMethod = (HandlerMethod) handler;
	Method method = handlerMethod.getMethod();
	/*if ( logger.isDebugEnabled() ) {*/
	logger.error( "方法:" + handlerMethod.getBean() + "." + method.getName() + "  ；  请求参数：" + handlerMethod.getMethodParameters() );
	logger.error( "访问的执行时间 : " + executeTime + "ms----页面："+ CommonUtil.getpath( request ) );
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
    }
}
