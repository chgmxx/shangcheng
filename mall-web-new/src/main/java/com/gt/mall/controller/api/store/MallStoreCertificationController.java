package com.gt.mall.controller.api.store;

import com.alibaba.fastjson.JSONArray;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.basic.ImageAssociativeDTO;
import com.gt.mall.param.phone.PhoneBuyNowDTO;
import com.gt.mall.param.store.StoreCertDTO;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺认证前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-18
 */
@Api( value = "mallStoreCert", description = "店铺认证", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallStore/E9lM9uM4ct/cert" )
public class MallStoreCertificationController extends BaseController {

    @Autowired
    private MallStoreCertificationService mallStoreCertService;
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;
    @Autowired
    private DictService                   dictService;
    @Autowired
    private MallImageAssociativeService   imageAssociativeService;
    @Autowired
    private MallCommonService             mallCommonService;
    @Autowired
    private WxPublicUserService           wxPublicUserService;

    /**
     * 保存店铺认证信息
     */
    @ApiOperation( value = "保存店铺认证信息", notes = "保存店铺认证信息" )
    @ApiImplicitParams( @ApiImplicitParam( name = "code", value = "验证码", paramType = "query", required = true, dataType = "String" ) )
    @ResponseBody
    @SysLogAnnotation( description = "保存店铺认证信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse save( HttpServletRequest request, HttpServletResponse response, String code, @RequestBody @Valid @ModelAttribute StoreCertDTO storeCertDTO ) {
	try {
	    if ( CommonUtil.isEmpty( code ) ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "验证码不能为空" );
	    }
	    if ( CommonUtil.isEmpty( JedisUtil.get( Constants.REDIS_KEY + code ) ) ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "验证码不正确" );
	    }
	    MallStoreCertification storeCert = new MallStoreCertification();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( storeCertDTO, storeCert );

	    if ( CommonUtil.isEmpty( storeCert.getId() ) ) {
		storeCert.setCreateTime( new Date() );
		mallStoreCertService.insert( storeCert );

		if ( storeCertDTO.getImageList() != null && storeCertDTO.getImageList().size() > 0 ) {
		    for ( int i = 0; i < storeCertDTO.getImageList().size(); i++ ) {
			MallImageAssociative associative = new MallImageAssociative();
			converter.entityConvertDto( storeCertDTO.getImageList().get( i ), associative );
			associative.setAssId( storeCert.getId() );
			associative.setAssType( 6 );
			associative.setAssSort( i );
			mallImageAssociativeService.insert( associative );
		    }
		}
	    } else {
		mallStoreCertService.updateById( storeCert );

		imageAssociativeService.newSaveImage( storeCertDTO.getImageList(), storeCert.getId(), 6 );
	    }
	    JedisUtil.del( Constants.REDIS_KEY + code );//保存成功，删除验证码
	} catch ( Exception e ) {
	    logger.error( "保存店铺认证信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取认证的店铺类型
     */
    @ApiOperation( value = "获取认证的店铺类型", notes = "获取认证的店铺类型" )
    @ResponseBody
    @RequestMapping( value = "/categoryMap", method = RequestMethod.POST )
    public ServerResponse categoryMap( HttpServletRequest request, HttpServletResponse response ) {
	List< DictBean > categoryMap = null;
	try {
	    //获取认证的店铺类型
	    categoryMap = dictService.getDict( "K002" );
	    for ( DictBean map : categoryMap ) {
		String value = map.getItem_value();
		JSONObject foorerObj = JSONObject.fromObject( value );
		map.setValue( foorerObj.get( "title" ).toString() );//名称
		if ( CommonUtil.isNotEmpty( foorerObj.get( "array" ) ) ) {
		    map.setChildList( foorerObj.getJSONArray( "array" ) );//子级
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取认证的店铺类型异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取认证的店铺类型异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), categoryMap );
    }

    /**
     * 获取店铺认证信息
     */
    @ApiOperation( value = "获取店铺认证信息", notes = "获取店铺认证信息" )
    @ResponseBody
    @RequestMapping( value = "/certInfo", method = RequestMethod.POST )
    public ServerResponse certInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "认证ID", required = true ) @RequestParam Integer id ) {
	MallStoreCertification storeCert = null;
	try {
	    storeCert = mallStoreCertService.selectById( id );
	    if ( storeCert != null && storeCert.getIsCertDoc() == 1 ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "assType", 6 );
		params.put( "assId", storeCert.getId() );
		List< MallImageAssociative > imageAssociativeList = mallImageAssociativeService.selectByAssId( params );
		storeCert.setImageList( imageAssociativeList );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取店铺认证信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取店铺认证信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), storeCert );
    }

    /**
     * 认证信息失效
     */
    @ApiOperation( value = "认证信息设置失效", notes = "认证信息设置失效" )
    @ResponseBody
    @RequestMapping( value = "/setInvalid", method = RequestMethod.POST )
    public ServerResponse certInvalid( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "认证ID", required = true ) @RequestParam Integer id ) throws IOException {
	try {
	    MallStoreCertification storeCert = mallStoreCertService.selectById( id );
	    if ( storeCert != null ) {
		storeCert.setIsDelete( 1 );
		mallStoreCertService.updateById( storeCert );
	    }
	} catch ( Exception e ) {
	    logger.error( "认证信息设置失效异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "认证信息设置失效异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 修改审核状态
     */
    @ApiOperation( value = "修改审核状态", notes = "修改审核状态" )
    @ResponseBody
    @RequestMapping( value = "/updateStatus", method = RequestMethod.POST )
    public ServerResponse updateStatus( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "认证ID", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "status", value = "类型 1通过 -1不通过", required = true ) @RequestParam Integer status ) {
	try {
	    MallStoreCertification certification = mallStoreCertService.selectById( id );
	    certification.setCheckStatus( status );
	    boolean flag = mallStoreCertService.updateById( certification );

	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改审核状态异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "修改审核状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改审核状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取短信验证码
     */
    @ApiOperation( value = "获取短信验证码", notes = "获取短信验证码" )
    @ResponseBody
    @RequestMapping( value = "/getValCode", method = RequestMethod.POST )
    public ServerResponse getValCode( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "mobile", value = "手机号码", required = true ) @RequestParam String mobile ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    WxPublicUsers pbUser = wxPublicUserService.selectByUserId( user.getId() );
	    String no = CommonUtil.getPhoneCode();
	    JedisUtil.set( Constants.REDIS_KEY + no, no, 10 * 60 );
	    System.out.println( "店铺认证短信验证码：" + no );
	    String content = pbUser.getAuthorizerInfo() + "  提醒您，您的验证码为：(" + no + ")" + "，验证码10分钟内有效，请尽快完成验证。";

	    boolean result = mallCommonService.getValCode( mobile, user.getId(), content, pbUser.getAuthorizerInfo() );
	    if ( !result ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "发送短信验证码异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取短信验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取短信验证码异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

 /*   *//**
     * 校验验证码
     *//*
    @ApiOperation( value = "校验验证码", notes = "校验验证码" )
    @ResponseBody
    @RequestMapping( value = "/checkCode", method = RequestMethod.POST )
    public ServerResponse checkCode( HttpServletRequest request, HttpServletResponse response, @RequestParam String code ) {
	try {
	    if ( CommonUtil.isEmpty( JedisUtil.get( Constants.REDIS_KEY + code ) ) ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "验证码不正确" );
	    } else {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	    }
	} catch ( Exception e ) {
	    logger.error( "校验验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "校验验证码异常" );
	}
    }*/
}
