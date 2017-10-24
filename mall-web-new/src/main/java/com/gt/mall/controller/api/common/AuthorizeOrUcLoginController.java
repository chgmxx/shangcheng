package com.gt.mall.controller.api.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mall.bean.Member;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import org.apache.log4j.Logger;
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

    public String userLogin( HttpServletRequest request, HttpServletResponse response, PhoneLoginDTO loginDTO ) throws Exception {
	Integer busId = loginDTO.getBusId();
	Integer browser = loginDTO.getBrowerType();
	Integer uclogin = loginDTO.getUcLogin();

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
	String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + GETWXPULICMSG, getWxPublicMap, wxmpSign );
	JSONObject json = JSONObject.parseObject( wxpublic );
	if ( CommonUtil.isEmpty( json ) || json.size() == 0 ) {
	    return null;
	}
	Integer code = CommonUtil.toInteger( json.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = json.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		throw new BusinessException( ResponseEnums.BUS_GUOQI_ERROR.getCode(), ResponseEnums.BUS_GUOQI_ERROR.getDesc(), json.get( "guoqiUrl" ).toString() );
	    }
	    Object remoteUcLogin = json.get( "remoteUcLogin" );
	    if ( browser == 99 && ( ( CommonUtil.isNotEmpty( uclogin ) && uclogin == 1 ) || CommonUtil.isNotEmpty( remoteUcLogin ) ) ) {
		return null;
	    }

	}

	String otherRedisKey = CommonUtil.getCode();
	Map< String,Object > redisMap = new HashMap<>();
	redisMap.put( "redisKey", otherRedisKey );
	redisMap.put( "redisValue", loginDTO.getUrl().toString() );
	redisMap.put( "setime", 5 * 60 );
	SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + "/8A5DA52E/redis/SetExApi.do", redisMap, PropertiesUtil.getWxmpSignKey() );

	Map< String,Object > queryMap = new HashMap<>();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", PropertiesUtil.getHomeUrl() );
	queryMap.put( "busId", busId );
	if ( CommonUtil.isNotEmpty( uclogin ) && uclogin == 1 ) {
	    queryMap.put( "uclogin", uclogin );
	}
	logger.info( " ResponseEnums.NEED_LOGIN.getCode()" + ResponseEnums.NEED_LOGIN.getCode() );
	throw new BusinessException( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc(),
			PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + JSON.toJSONString( queryMap ) );
    }

}
