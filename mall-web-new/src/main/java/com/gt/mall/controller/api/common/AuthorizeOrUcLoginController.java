package com.gt.mall.controller.api.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mall.bean.Member;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping( "authorizeOrUcLoginController" )
public class AuthorizeOrUcLoginController {

    public static final Logger logger = Logger.getLogger( AuthorizeOrUcLoginController.class );

    private static final String GETWXPULICMSG = "/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    @Autowired
    private BusUserService busUserService;

    //    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String userLogin( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );

	Member member = SessionUtils.getLoginMember( request );
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
	//	System.out.println( "wxmpDomain = " + PropertiesUtil.getWxmpDomain() );
	String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + GETWXPULICMSG, getWxPublicMap, wxmpSign );
	JSONObject json = JSONObject.parseObject( wxpublic );
	if ( CommonUtil.isEmpty( json ) || json.size() == 0 ) {
	    return null;
	}
	Integer code = CommonUtil.toInteger( json.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = json.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		throw new BusinessException( ResponseEnums.BUS_GUOQI_ERROR.getCode(), json.get( "guoqiUrl" ).toString() );
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

	Map< String,Object > queryMap = new HashMap<>();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", PropertiesUtil.getHomeUrl() );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );
	throw new BusinessException( ResponseEnums.NEED_LOGIN.getCode(),
			PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + JSON.toJSONString( queryMap ) );
	//	String url = "redirect:" + PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + JSON.toJSONString( queryMap );
	//	return url;
    }

}
