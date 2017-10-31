package com.gt.mall.controller.api.product.phone;

import com.alibaba.fastjson.JSONArray;
import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.common.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.shopCart.PhoneAddShopCartDTO;
import com.gt.mall.param.phone.shopCart.PhoneRemoveShopCartDTO;
import com.gt.mall.param.phone.shopCart.PhoneShopCartOrderDTO;
import com.gt.mall.result.phone.shopcart.PhoneShopCartResult;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 购物车页面相关接口（手机端）
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:35
 */
@Api( value = "phoneShopCart", description = "购物车页面相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneShopCart/L6tgXlBFeK/" )
public class PhoneShopCartController extends AuthorizeOrUcLoginController {

    private static Logger logger = LoggerFactory.getLogger( PhoneShopCartController.class );

    @Autowired
    private MallStoreService    mallStoreService;//店铺业务处理类
    @Autowired
    private MallShopCartService mallShopCartService;//商品库存业务处理类

    @ApiOperation( value = "加入购物车接口", notes = "用户加入购物车", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "addShopCart", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse addShopCart( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneAddShopCartDTO params ) {
	try {

	    Member member = MallSessionUtils.getLoginMember( request, params.getBusId() );

	    mallShopCartService.addShoppingCart( member, params, request, response );

	} catch ( BusinessException e ) {
	    logger.error( "加入购物车异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "加入购物车异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "加入购物车失败" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "查询购物车接口", notes = "查询购物车数据", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "Integer" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺id", paramType = "query", required = true, dataType = "Integer" ),
		    @ApiImplicitParam( name = "type", value = "购物车类型 1批发购物车可不传", paramType = "query", dataType = "Integer", defaultValue = "0" ),
		    @ApiImplicitParam( name = "loginDTO", value = "登陆参数", required = true, paramType = "query", dataType = "Object" )
    } )
    @ResponseBody
    @PostMapping( value = "getShopCart", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneShopCartResult > getShopCart( HttpServletRequest request, HttpServletResponse response, Integer busId, Integer shopId, Integer type,
		    PhoneLoginDTO loginDTO ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneShopCartResult result = mallShopCartService.getShopCart( member, busId, type, request, response );

	    MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "查询购物车异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询购物车异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询购物车失败" );
	}
    }

    @ApiOperation( value = "删除购物车接口", notes = "用户删除购物车", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "removeShopCart", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneRemoveShopCartDTO > removeShopCart( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneRemoveShopCartDTO params ) {
	try {

	    mallShopCartService.removeShopCart( params, request, response );

	} catch ( BusinessException e ) {
	    logger.error( "删除购物车异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除购物车异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "删除购物车失败" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "购物车去结算接口", notes = "购物车去结算", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams(
		    @ApiImplicitParam( name = "str", value = "参数", paramType = "query", required = true, dataType = "String" )
    )
    @ResponseBody
    @PostMapping( value = "shopCartOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse shopCartOrder( HttpServletRequest request, HttpServletResponse response, String str ) {
	try {

	    List< PhoneShopCartOrderDTO > params = JSONArray.parseArray( str, PhoneShopCartOrderDTO.class );
	    mallShopCartService.shopCartOrder( params );
	} catch ( BusinessException e ) {
	    logger.error( "购物车去结算异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "购物车去结算异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "购物车去结算失败" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
