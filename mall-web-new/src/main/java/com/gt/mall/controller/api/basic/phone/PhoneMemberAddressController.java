package com.gt.mall.controller.api.basic.phone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.PhoneMemberAddressDTO;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.common.MallMemberAddressService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 我的地址接口（手机端）
 *
 * @author yangqian
 */
@Api( value = "PhoneMemberAddressController", description = "我的地址接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneAddress/L6tgXlBFeK/" )
public class PhoneMemberAddressController extends AuthorizeOrUcLoginController {

    public static final Logger logger = Logger.getLogger( PhoneMemberAddressController.class );

    @Autowired
    private MemberAddressService     memberAddressService;//会员收货地址接口
    @Autowired
    private WxShopService            wxShopService;//门店接口
    @Autowired
    private MallMemberAddressService mallMemberAddressService;

    @ApiOperation( value = "手机端地址列表的接口", notes = "我的地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "addressList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< PhoneOrderMemberAddressDTO > > addressList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    //获取会员地址
	    List< PhoneOrderMemberAddressDTO > addressList = mallMemberAddressService.getMemberAddressList( member.getId() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), addressList, false );
	} catch ( BusinessException e ) {
	    logger.error( "手机端地址列表的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "手机端地址列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询我的地址失败" );
	}
    }

    @ApiOperation( value = "手机端根据地址id查询地址的接口", notes = "进入编辑地址地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "addressId", value = "地址id", paramType = "query", dataType = "int", required = true ) )
    @ResponseBody
    @PostMapping( value = "selectAddressById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< MemberAddress > selectAddressById( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer addressId ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    MemberAddress memberAddress = memberAddressService.addreSelectId( CommonUtil.toInteger( addressId ) );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), memberAddress, false );
	} catch ( BusinessException e ) {
	    logger.error( "手机端根据地址id查询地址的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "手机端根据地址id查询地址的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询地址信息失败" );
	}
    }

    @ApiOperation( value = "手机端查询省市区集合的接口", notes = "查询省市区集合", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "cityId", value = "城市id", paramType = "query", dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "getCityList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getCityList( HttpServletRequest request, HttpServletResponse response, Integer cityId ) {
	try {
	    List< Map > list;
	    if ( CommonUtil.isNotEmpty( cityId ) && cityId > 0 ) {
		//根据父类城市id查询城市集合
		list = wxShopService.queryCityByParentId( cityId );
	    } else {
		//查询省份集合
		list = wxShopService.queryCityByLevel( 2 );
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), list, false );
	} catch ( Exception e ) {
	    logger.error( "手机端查询省市区的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询查询省市区失败" );
	}
    }

    @ApiOperation( value = "手机端保存地址的接口", notes = "保存地址信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "saveAddress", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse saveAddress( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    @RequestBody @Valid @ModelAttribute PhoneMemberAddressDTO memberAddress ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    memberAddress.setDfMemberId( member.getId() );

	    MemberAddress address = JSONObject.parseObject( JSON.toJSONString( memberAddress ), MemberAddress.class );
	    boolean flag = memberAddressService.addOrUpdateAddre( address );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( "保存地址信息失败" );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "手机端保存地址的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "手机端保存地址的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "保存地址信息失败" );
	}
    }

    @ApiOperation( value = "设为默认地址的接口", notes = "设为默认地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "addressId", value = "地址id，必传", paramType = "query", dataType = "int", required = true ) )
    @ResponseBody
    @PostMapping( value = "defaultAddress", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse defaultAddress( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer addressId ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    boolean flag = memberAddressService.updateDefault( addressId );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( "设为默认地址失败" );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "设为默认地址的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "设为默认地址的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "设为默认地址失败" );
	}
    }

}
