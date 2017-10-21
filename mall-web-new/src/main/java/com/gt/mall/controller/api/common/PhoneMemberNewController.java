package com.gt.mall.controller.api.common;

import com.gt.mall.dto.ServerResponse;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.result.phone.order.PhoneToOrderResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 我的页面
 *
 * @author Administrator
 */
@Controller
@RequestMapping( "phoneMember" )
public class PhoneMemberNewController extends AuthorizeOrUcLoginController {

    public static final Logger logger = Logger.getLogger( PhoneMemberNewController.class );

    @ApiOperation( value = "进入我的页面的接口", notes = "查询我的页面信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "busId", value = "商家id，必传", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "url", value = "商家id，必传", paramType = "query", required = true, dataType = "String" )
    } )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/toUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneToOrderResult > toUser( HttpServletRequest request, HttpServletResponse response, PhoneLoginDTO loginDTO ) {
	try {
	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "进入我的页面的异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "进入我的页面的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "是否需要登陆接口", notes = "是否需要登陆订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "Integer" ),
		    @ApiImplicitParam( name = "pageUrl", value = "当前页面地址", paramType = "query", required = true, dataType = "string" )
    } )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/isLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse isLogin( HttpServletRequest request, HttpServletResponse response, PhoneLoginDTO loginDTO ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "是否需要登陆异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	} catch ( Exception e ) {
	    logger.error( "是否需要登陆异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "是否需要登陆失败" );
	}
    }

}
