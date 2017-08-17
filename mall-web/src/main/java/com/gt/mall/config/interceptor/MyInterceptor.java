package com.gt.mall.config.interceptor;

import com.gt.mall.bean.BusUser;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class MyInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger( MyInterceptor.class );

    //不需要登录就可访问的url
    private static final Map< String,String > urls = new HashMap< String,String >();

    //可通过的文件类型
    private static final List< String > suffixs = new ArrayList< String >();

    static {

	urls.put( "/jsp/error/404.jsp", "/jsp/error/404.jsp" );
	urls.put( "/jsp/error/error.jsp", "/jsp/error/error.jsp" );
	urls.put( "/", "/user/tologin.do" );
	urls.put( "/error/warning.jsp", "/error/warning.jsp" );

	urls.put( "/user/tologin.do", "/user/tologin.do" );
	urls.put( "/user/toregister.do", "/user/toregister.do" );
	urls.put( "/dxuser/login.do", "/dxuser/login.do" );
	urls.put( "/dxuser/login_success.do", "/dxuser/login_success.do" );

	suffixs.add( "js" );
	suffixs.add( "css" );
	suffixs.add( "gif" );
	suffixs.add( "png" );
	suffixs.add( "jpg" );
	suffixs.add( "ico" );
	suffixs.add( "html" );
	suffixs.add( "dwr" );
	suffixs.add( "mp3" );
	suffixs.add( "txt" );
	suffixs.add( "woff" );
	suffixs.add( "ttf" );
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		    throws Exception {
	System.out.println( ">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	System.out.println( "basePath = " + request.getRequestURL() );

	// 获得在下面代码中要用的request,response,session对象

	BusUser user = SessionUtils.getLoginUser( request );
	String url = request.getRequestURI();

	String urlwx = "";
	if ( url.length() > 0 ) {
	    //截取URL中的倒数第一个/和第二个/之间的数值
	    String tmp = url.substring( 0, url.lastIndexOf( "/" ) );
	    urlwx = tmp.substring( tmp.lastIndexOf( "/" ) + 1, tmp.length() );
	}
	//如果URL是登录页面或者是登录界面时或者是微信接口，继续
	/*if ( url.equals( "/" ) ) {
	    response.sendRedirect( "http://www." + PropertiesUtil.getDomain() + "" );
	    return false;
	} else */
	if ( urlwx.equals( "webservice" ) || urlwx.equals( "79B4DE7C" ) || url.contains( "79B4DE7C" ) ) {//移动端
	   /* Member member = SessionUtils.getLoginMember( request );
	    if ( CommonUtil.isNotEmpty( member ) && member.isPass() ) {//商家已过期，清空会员登录session
		request.getSession().removeAttribute( "member" );
		String upGradeUrl = request.getContextPath() + "/jsp/error/warning.jsp";
		response.sendRedirect( upGradeUrl );
		return false;
	    } else {
		return true;// 只有返回true才会继续向下执行，返回false取消当前请求
	    }*/
	    return true;
	} else if ( passSuffixs( url ) || passUrl( url ) ) {
	    return true;// 只有返回true才会继续向下执行，返回false取消当前请求
	} else if ( user == null ) {// 判断如果没有取到微信授权信息,就跳转到登陆页面
	    response.setCharacterEncoding( "UTF-8" );
	    String script = "<script type='text/javascript'>"
			    + "top.location.href='" + PropertiesUtil.getWxmpDomain() + "/user/tologin.do';"
			    + "</script>";
	    if ( isAjax( request ) ) {
		Map< String,Object > map = new HashMap<>();
		map.put( "timeout", "连接超时，请重新登录！" );
		response.getWriter().write( JSONObject.fromObject( map ).toString() );
	    } else {
		response.getWriter().write( script );
	    }
	    return false;
	}
	if ( CommonUtil.isNotEmpty( user ) ) {
	    request.setAttribute( "wxmpDomain", PropertiesUtil.getWxmpDomain() );//wxmp链接，前端调用js用的
	}
	request.setAttribute( "webUrl", PropertiesUtil.getHomeUrl() );//本项目的地址
	return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
     */
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler,
		    ModelAndView modelAndView ) throws Exception {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
		    throws Exception {
    }

    //判断是否是可通过的url
    private boolean passUrl( String url ) {
	return urls.containsKey( url );
    }

    private boolean passSuffixs( String url ) {
	boolean reuslt = false;
	for ( String suffix : suffixs ) {
	    if ( url.endsWith( suffix ) ) {
		reuslt = true;
		break;
	    }
	}
	return reuslt;
    }

    /**
     * 判断ajax请求
     */
    private boolean isAjax( HttpServletRequest request ) {
	return ( request.getHeader( "X-Requested-With" ) != null && "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) ) );
    }
}
