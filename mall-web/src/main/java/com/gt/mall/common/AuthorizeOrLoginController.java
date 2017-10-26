package com.gt.mall.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.api.bean.session.Member;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
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

    private static final String GETWXPULICMSG = "/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    @Autowired
    private BusUserService busUserService;

    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String userLogin( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );

	Member member = MallSessionUtils.getLoginMember( request, busId );
	if ( CommonUtil.isNotEmpty( member ) ) {
	    //用户的所属商家和传进来的商家id相同不必登陆
	    if ( member.getBusid().toString().equals( CommonUtil.toString( busId ) ) ) {
		return null;
	    }
	}

	String wxmpSign = "WXMP2017";
	Map< String,Object > getWxPublicMap = new HashMap<>();
	getWxPublicMap.put( "busId", busId );
	//判断商家信息 1是否过期 2公众号是否变更过
	System.out.println( "wxmpDomain = " + PropertiesUtil.getWxmpDomain() );
	String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + GETWXPULICMSG, getWxPublicMap, wxmpSign );
	JSONObject json = JSONObject.parseObject( wxpublic );
	if ( CommonUtil.isEmpty( json ) || json.size() == 0 ) {
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
	    if ( browser == 99 && ( CommonUtil.isNotEmpty( uclogin ) || CommonUtil.isNotEmpty( remoteUcLogin ) ) ) {
		return null;
	    }

	}

	String otherRedisKey = CommonUtil.getCode();
	String requestUrl = PropertiesUtil.getHomeUrl() + CommonUtil.toString( map.get( "requestUrl" ) );
	Map< String,Object > redisMap = new HashMap<>();
	redisMap.put( "redisKey", otherRedisKey );
	redisMap.put( "redisValue", requestUrl );
	redisMap.put( "setime", 5 * 60 );
	SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + "/8A5DA52E/redis/SetExApi.do", redisMap, PropertiesUtil.getWxmpSignKey() );


	/*JedisUtil.set( otherRedisKey, requestUrl, 5 * 60 );*/
	Map< String,Object > queryMap = new HashMap<>();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", PropertiesUtil.getHomeUrl() );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );
	String url = "redirect:" + PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + JSON.toJSONString( queryMap );
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
    public void clearMember( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    MallSessionUtils.setLoginMember( request, null );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
