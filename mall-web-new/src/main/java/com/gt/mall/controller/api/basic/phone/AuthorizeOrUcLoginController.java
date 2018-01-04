package com.gt.mall.controller.api.basic.phone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mall.constant.Constants;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.utils.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
	Integer browser = CommonUtil.judgeBrowser( request );
	Integer uclogin = loginDTO.getUcLogin();
	Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	if ( CommonUtil.isNotEmpty( member ) ) {
	    //用户的所属商家和传进来的商家id相同不必登陆
	    if ( member.getBusid().toString().equals( CommonUtil.toString( busId ) ) ) {
		return null;
	    }
	}
	JSONObject guoqiJson = getBusGuoqi( busId );//获取过期json
	if ( CommonUtil.isEmpty( guoqiJson ) ) {
	    String wxmpSign = "WXMP2017";
	    Map< String,Object > getWxPublicMap = new HashMap<>();
	    getWxPublicMap.put( "busId", busId );
	    //判断商家信息 1是否过期 2公众号是否变更过
	    String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmpDomain() + GETWXPULICMSG, getWxPublicMap, wxmpSign );
	    guoqiJson = JSONObject.parseObject( wxpublic );
	    if ( CommonUtil.isEmpty( guoqiJson ) || guoqiJson.size() == 0 ) {
		return null;
	    }
	}
	Integer code = CommonUtil.toInteger( guoqiJson.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = guoqiJson.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		throw new BusinessException( ResponseEnums.BUS_GUOQI_ERROR.getCode(), ResponseEnums.BUS_GUOQI_ERROR.getDesc(), guoqiJson.get( "guoqiUrl" ).toString() );
	    }
	    setBusGuoqi( guoqiJson, busId );
	    Object remoteUcLogin = guoqiJson.get( "remoteUcLogin" );
	    if ( browser == 99 && ( ( CommonUtil.isNotEmpty( uclogin ) && uclogin == 1 ) || CommonUtil.isNotEmpty( remoteUcLogin ) ) ) {
		return null;
	    }
	}

	KeysUtil keysUtil = new KeysUtil();
	String requestUrl = keysUtil.getEncString( loginDTO.getUrl() );

	Map< String,Object > queryMap = new HashMap<>();
	queryMap.put( "returnUrl", requestUrl );
	queryMap.put( "browser", browser );
	queryMap.put( "busId", busId );
	if ( CommonUtil.isNotEmpty( uclogin ) && uclogin == 1 ) {
	    queryMap.put( "uclogin", uclogin );
	}
	logger.info( " ResponseEnums.NEED_LOGIN.getCode()" + ResponseEnums.NEED_LOGIN.getCode() );
	throw new BusinessException( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc(),
			PropertiesUtil.getWxmpDomain() + "remoteUserAuthoriPhoneController/79B4DE7C/authorizeMemberNew.do?queryBody=" + JSON.toJSONString( queryMap ) );
    }

    private void setBusGuoqi( JSONObject json, int busId ) {
	String key = Constants.REDIS_KEY + "_bus_guoqi_" + busId;
	Date date24 = DateTimeKit.parse( DateTimeKit.getDate() + " 24:00:00", DateTimeKit.DEFAULT_DATETIME_FORMAT );
	Date now = new Date();
	Long times = ( date24.getTime() - now.getTime() ) / 1000;
	JedisUtil.set( key, json.toJSONString(), times.intValue() );
    }

    private JSONObject getBusGuoqi( int busId ) {
	String key = Constants.REDIS_KEY + "_bus_guoqi_" + busId;
	if ( JedisUtil.exists( key ) ) {
	    if ( CommonUtil.isNotEmpty( JedisUtil.get( key ) ) ) {
		return JSONObject.parseObject( JedisUtil.get( key ) );
	    }
	}
	return null;
    }

}
