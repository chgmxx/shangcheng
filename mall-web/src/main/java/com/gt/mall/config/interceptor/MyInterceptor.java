package com.gt.mall.config.interceptor;

import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
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

        urls.put( "/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui" );
        urls.put( "/common/toLogin", "/common/toLogin" );

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
        noIntercepor.put( "html/phone", "html/phone" );
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        //	logger.info( ">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
        logger.info( ">>>MyInterceptor1>>basePath = " + CommonUtil.getpath( request ) );

        long startTime = System.currentTimeMillis();
        request.setAttribute( "runStartTime", startTime );

        // 获得在下面代码中要用的request,response,session对象

        BusUser user = MallSessionUtils.getLoginUser( request );
        String url = request.getRequestURI();
        if ( CommonUtil.isNotEmpty( user ) ) {
            request.setAttribute( "wxmpDomain", PropertiesUtil.getWxmpDomain() );//wxmp链接，前端调用js用的
        }
        request.setAttribute( "webUrl", PropertiesUtil.getHomeUrl() );//本项目的地址

	String urlwx = "";
	if ( url.length() > 0 ) {
	    //截取URL中的倒数第一个/和第二个/之间的数值
	    String tmp = url.substring( 0, url.lastIndexOf( "/" ) );
	    urlwx = tmp.substring( tmp.lastIndexOf( "/" ) + 1, tmp.length() );
	}
	//商城登陆拦截
	if ( urlwx.equals( "webservice" ) || urlwx.equals( "79B4DE7C" ) || url.contains( "79B4DE7C" ) ) {//移动端
	    Member member = null;//MallSessionUtils.getLoginMember( request, MallSessionUtils.getUserId( request ) );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "member", member );
	    }
	    return true;
	} else if ( passSuffixs( url ) || passUrl( url ) || passIntercepto( url ) ) {
	    return true;// 只有返回true才会继续向下执行，返回false取消当前请求
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
        logger.error( "访问的执行时间 : " + executeTime + "ms----页面：" + CommonUtil.getpath( request ) );
	/*}*/

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception {
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
