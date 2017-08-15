package com.gt.mall.common;

import com.gt.mall.base.BaseController;
import com.gt.mall.bean.Member;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PropertiesUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class AuthorizeOrLoginController extends BaseController {

    private static final Logger LOG = Logger.getLogger( AuthorizeOrLoginController.class );

    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String userLogin( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	//获取token
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );

	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );
	if ( browser == 99 && CommonUtil.isNotEmpty( uclogin ) ) {
	    return null;
	}

	String requestUrl = request.getRequestURL().toString();
	String otherRedisKey = CommonUtil.getCode();
	JedisUtil.set( otherRedisKey, requestUrl, 5 * 60 );
	Map< String,Object > queryMap = new HashMap< String,Object >();

	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", PropertiesUtil.getHomeUrl() );
	queryMap.put( "busId", busId );
	String json = JSONObject.fromObject( queryMap ).toString();
	String url = "redirect:"+PropertiesUtil.getHomeUrl()+"/remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryParam=" + json;
	return url;
    }

    /**
     * 返回跳转到之前的页面
     */
    @RequestMapping( value = "/{redisKey}/79B4DE7C/returnJump" )
    public String returnJump( HttpServletRequest request, HttpServletResponse response, @PathVariable( "redisKey" ) String redisKey,
		    @RequestParam( "memberJson" ) String memberJson ) {
	try {

	    if ( !"1".equals( memberJson ) ) {
		memberJson = JedisUtil.get( memberJson );
		Member member = (Member) JSONObject.toBean( JSONObject.fromObject( memberJson ), Member.class );
//		CommonUtil.setLoginMember( request, member );
	    }
	    String url = JedisUtil.get( redisKey );
	    return "redirect:" + url;
	} catch ( Exception e ) {
	    LOG.error( "授权或登录回调异常", e );
	}
	return null;
    }
}
