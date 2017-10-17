//package com.gt.mall.config.interceptor;
//
//import org.apache.log4j.Logger;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//
///**
// * Created by Administrator on 2017/7/24 0024.
// */
//public class MyInterceptor implements HandlerInterceptor {
//
//    private Logger logger = Logger.getLogger( MyInterceptor.class );
//
//    /**
//     * 在请求处理之前进行调用（Controller方法调用之前）
//     */
//    @Override
//    public boolean preHandle( HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler ) throws Exception {
//
//	boolean isSuccess = true;
//	String url = servletRequest.getRequestURI();
//	//请求商城接口拦截
//	if ( !url.contains( "mallAPI" ) ) {
//	    return true;
//	}
//
//
//	return isSuccess;// 只有返回true才会继续向下执行，返回false取消当前请求
//    }
//
//    /**
//     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
//     */
//    @Override
//    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception {
//
//	long startTime = (Long) request.getAttribute( "runStartTime" );
//
//	long endTime = System.currentTimeMillis();
//
//	long executeTime = endTime - startTime;
//
//	HandlerMethod handlerMethod = (HandlerMethod) handler;
//	Method method = handlerMethod.getMethod();
//	/*if ( logger.isDebugEnabled() ) {*/
//	logger.error( "方法:" + handlerMethod.getBean() + "." + method.getName() + "  ；  请求参数：" + handlerMethod.getMethodParameters() );
//	logger.error( "访问的执行时间 : " + executeTime + "ms" );
//	/*}*/
//
//    }
//
//    /**
//     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
//     */
//    @Override
//    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
//    }
//
//}
