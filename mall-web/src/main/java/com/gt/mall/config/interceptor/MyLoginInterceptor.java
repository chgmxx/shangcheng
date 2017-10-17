package com.gt.mall.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.sign.SignBean;
import com.gt.api.bean.sign.SignEnum;
import com.gt.api.util.sign.SignUtils;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.exception.ResponseEntityException;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
@WebFilter( filterName = "loginFilter", urlPatterns = "/*" )
public class MyLoginInterceptor implements Filter {

    private Logger logger = Logger.getLogger( MyLoginInterceptor.class );

    //不需要登录就可访问的url
    private static final Map< String,String > urls = new HashMap<>();

    //可通过的文件类型
    private static final List< String > suffixs = new ArrayList<>();

    private static final Map< String,Object > noIntercepor = new HashMap<>();

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
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
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

	HttpServletRequest req = (HttpServletRequest) request;
	//	HttpServletResponse res = (HttpServletResponse) response;

	logger.info( ">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）" );
	logger.info( "basePath = " + CommonUtil.getpath( req ) );

	long startTime = System.currentTimeMillis();
	request.setAttribute( "runStartTime", startTime );

	// 获得在下面代码中要用的request,response,session对象

	BusUser user = SessionUtils.getLoginUser( req );
	String url = req.getRequestURI();
	if ( CommonUtil.isNotEmpty( user ) ) {
	    request.setAttribute( "wxmpDomain", PropertiesUtil.getWxmpDomain() );//wxmp链接，前端调用js用的
	}
	request.setAttribute( "webUrl", PropertiesUtil.getHomeUrl() );//本项目的地址
	if ( url.contains( "mallAPI" ) ) {
	    validateSign( req );
	}

	String urlwx = "";
	if ( url.length() > 0 ) {
	    //截取URL中的倒数第一个/和第二个/之间的数值
	    String tmp = url.substring( 0, url.lastIndexOf( "/" ) );
	    urlwx = tmp.substring( tmp.lastIndexOf( "/" ) + 1, tmp.length() );
	}
	//商城登陆拦截
	if ( urlwx.equals( "webservice" ) || urlwx.equals( "79B4DE7C" ) || url.contains( "79B4DE7C" ) ) {//移动端
	    Member member = SessionUtils.getLoginMember( req );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "member", member );
	    } else {
		member = new Member();
		member.setId( 1225352 );
		//		member.setId( 562 );
		member.setBusid( 42 );
		member.setPublicId( 482 );
		member.setPhone( "15017934717" );
		member.setNickname( "杨倩" );
		member.setHeadimgurl( "http://wx.qlogo.cn/mmopen/SBjYnYMJXhekesFe18mYibHXhc0SsqXaxR31n8FXDK0TicZXsDjr0XFLdEtY0QgO7tdNt1w52L7aVBbke5ljuNiaoQbH1qGvXZa/0" );
		member.setOldid( "1225352,1225358,1225449" );
		SessionUtils.setLoginMember( req, member );
	    }
	    chain.doFilter( request, response );
	    return;
	} else if ( passSuffixs( url ) || passUrl( url ) || passIntercepto( url ) ) {
	    chain.doFilter( request, response );// 只有返回true才会继续向下执行，返回false取消当前请求
	    return;
	} else if ( user == null && !url.contains( "error" ) ) {// 判断如果没有取到微信授权信息,就跳转到登陆页面

	    user = new BusUser();
	    user.setId( 42 );
	    user.setName( "gt123456" );
	    user.setPid( 0 );
	    SessionUtils.setLoginUser( req, user );

	}
	chain.doFilter( request, response );// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    @Override
    public void destroy() {

    }

    private boolean validateSign( HttpServletRequest servletRequest ) {
	String signKey = "MALL2017"; // 定义到配置文件中
	String signStr = ( (HttpServletRequest) servletRequest ).getHeader( "sign" );
	// 解析签名
	SignBean signBean = JSON.parseObject( signStr, SignBean.class );
	// 获取签名信息
	String code = SignUtils.decSign( signKey, signBean, null );
	logger.debug( code );
	if ( SignEnum.TIME_OUT.getCode().equals( code ) ) {
	    // 超过指定时间
	    throw new ResponseEntityException( "请求超过指定时间" );
	} else if ( SignEnum.SIGN_ERROR.getCode().equals( code ) ) {
	    // 签名验证错误
	    throw new ResponseEntityException( "签名验证错误，请检查签名信息" );
	} else if ( SignEnum.SERVER_ERROR.getCode().equals( code ) ) {
	    // 签名系统错误
	    throw new ResponseEntityException( "系统错误，请检查传入参数" );

	}
	return true;
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
