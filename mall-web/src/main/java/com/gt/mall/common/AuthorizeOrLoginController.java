package com.gt.mall.common;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权 或 登录 统一调用接口
 *
 * @author Administrator
 */
@Controller
@RequestMapping( "authorizeOrLoginController" )
public class AuthorizeOrLoginController {

    public static final Logger logger = Logger.getLogger( AuthorizeOrLoginController.class );

    @Autowired
    private BusUserService busUserService;

    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String userLogin( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );

	JSONObject json = busUserService.isUserGuoQi( busId );
	if ( CommonUtil.isEmpty( json ) ) {
	    return null;
	}
	Integer code = CommonUtil.toInteger( json.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = json.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		//商家已过期
		Object guoqiUrl = json.get( "guoqiUrl" );
		return "redirect:" + guoqiUrl;
	    }
	    Object remoteUcLogin = json.get( "remoteUcLogin" );
	    if ( CommonUtil.isNotEmpty( uclogin ) || CommonUtil.isNotEmpty( remoteUcLogin ) ) {
		return null;
	    }

	}

	String requestUrl = CommonUtil.toString( map.get( "requestUrl" ) );
	String otherRedisKey = CommonUtil.getCode();
	JedisUtil.set( otherRedisKey, requestUrl, 5 * 60 );
	Map< String,Object > queryMap = new HashMap< String,Object >();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", PropertiesUtil.getHomeUrl() );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );
	String url = "redirect:" + PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryParam=" + queryMap;
	return url;
    }

    /**
     * 返回跳转到之前的页面
     */
    @RequestMapping( value = "/{redisKey}/79B4DE7C/returnJump" )
    public String returnJump( HttpServletRequest request, HttpServletResponse response, @PathVariable( "redisKey" ) String redisKey, Map< String,Object > params ) {
	try {
	    Object url = JedisUtil.get( redisKey );
	    return "redirect:" + url;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    @RequestMapping( value = "/79B4DE7C/clearMember" )
    public void clearMember( HttpServletRequest request, HttpServletResponse response) {
	try {
	    SessionUtils.setLoginMember( request, null );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
