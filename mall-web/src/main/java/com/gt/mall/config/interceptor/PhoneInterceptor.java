package com.gt.mall.config.interceptor;

import com.gt.api.bean.session.Member;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class PhoneInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger( PhoneInterceptor.class );

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
	logger.info( ">>>PhoneInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	logger.info( "basePath = " + CommonUtil.getpath( request ) );

	long startTime = System.currentTimeMillis();
	request.setAttribute( "runStartTime", startTime );

	// 获得在下面代码中要用的request,response,session对象

	Member member = null;//MallSessionUtils.getLoginMember( request, MallSessionUtils.getUserId( request ) );
	if ( CommonUtil.isNotEmpty( member ) ) {
	    request.setAttribute( "member", member );
	} else {
	    if ( request.getServerName().contains( "192.168.2" ) ) {
		member = new Member();
		//	    member.setId( 366 );//销售员测试
		//	    	    	    member.setId( 1225509 );
		member.setId( 1225352 );//折扣卡
		//			    	    member.setId( 1225542 );//储值卡
		member.setBusid( 42 );
		member.setPublicId( 482 );
		member.setPhone( "15017934717" );
		member.setNickname( "杨倩" );
		member.setHeadimgurl( "http://wx.qlogo.cn/mmopen/SBjYnYMJXhekesFe18mYibHXhc0SsqXaxR31n8FXDK0TicZXsDjr0XFLdEtY0QgO7tdNt1w52L7aVBbke5ljuNiaoQbH1qGvXZa/0" );
		//	    	    member.setOldid( "1225352,1225358,1225449" );
		MallSessionUtils.setLoginMember( request, member );
	    }

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
	logger.error( "访问的执行时间 : " + executeTime + "ms" );
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
    }

}
