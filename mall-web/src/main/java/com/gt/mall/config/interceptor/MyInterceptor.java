package com.gt.mall.config.interceptor;

import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class MyInterceptor implements HandlerInterceptor {

    private Logger logger = Logger.getLogger( MyInterceptor.class );

    //不需要登录就可访问的url
    private static final Map< String,String > urls = new HashMap<>();

    //可通过的文件类型
    private static final List< String > suffixs = new ArrayList<>();

    private static final Map< String,Object > noIntercepor = new HashMap<>();

    static {

	urls.put( "/jsp/error/404.jsp", "/jsp/error/404.jsp" );
	urls.put( "/jsp/error/error.jsp", "/jsp/error/error.jsp" );
	urls.put( "/", "/user/tologin.do" );
	urls.put( "/error/warning.jsp", "/error/warning.jsp" );

	urls.put( "/user/tologin.do", "/user/tologin.do" );
	urls.put( "/user/toregister.do", "/user/toregister.do" );
	urls.put( "/dxuser/login.do", "/dxuser/login.do" );
	urls.put( "/dxuser/login_success.do", "/dxuser/login_success.do" );
	urls.put( "/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui" );

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

	noIntercepor.put( "swagger-resources", "swagger-resources" );
	noIntercepor.put( "api-docs", "api-docs" );
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
		    throws Exception {

	logger.error( ">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	logger.error( "basePath = " + request.getRequestURL() );

	long startTime = System.currentTimeMillis();
	request.setAttribute( "runStartTime", startTime );

	// 获得在下面代码中要用的request,response,session对象

	BusUser user = SessionUtils.getLoginUser( request );
	String url = request.getRequestURI();

	String urlwx = "";
	if ( url.length() > 0 ) {
	    //截取URL中的倒数第一个/和第二个/之间的数值
	    String tmp = url.substring( 0, url.lastIndexOf( "/" ) );
	    urlwx = tmp.substring( tmp.lastIndexOf( "/" ) + 1, tmp.length() );
	}
	if ( urlwx.equals( "webservice" ) || urlwx.equals( "79B4DE7C" ) || url.contains( "79B4DE7C" ) ) {//移动端
	    Member member = SessionUtils.getLoginMember( request );
	    request.setAttribute( "member", member );
	    return true;
	} else if ( passSuffixs( url ) || passUrl( url ) || passIntercepto( url ) ) {
	    return true;// 只有返回true才会继续向下执行，返回false取消当前请求
	} else if ( user == null && !url.contains( "error" ) ) {// 判断如果没有取到微信授权信息,就跳转到登陆页面
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

	long startTime = (Long) request.getAttribute( "runStartTime" );

	long endTime = System.currentTimeMillis();

	long executeTime = endTime - startTime;

	HandlerMethod handlerMethod = (HandlerMethod) handler;
	Method method = handlerMethod.getMethod();
	/*if ( logger.isDebugEnabled() ) {*/
	logger.debug( "方法:" + handlerMethod.getBean() + "." + method.getName() + "  ；  请求参数：" + handlerMethod.getMethodParameters() );
	logger.debug( "访问的执行时间 : " + executeTime + "ms" );
	/*}*/

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

    private boolean passIntercepto( String url ) {
	boolean reuslt = false;
	Iterator it = noIntercepor.entrySet().iterator();
	while ( it.hasNext() ) {
	    Map.Entry< String,Integer > entry = (Map.Entry< String,Integer >) it.next();
	    String key = entry.getKey();
	    if ( url.contains( key ) ) {
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
