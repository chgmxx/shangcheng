package com.gt.mall.controller.api.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallSecuritytradeQuit;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.presale.MallPresaleTime;
import com.gt.mall.entity.purchase.PurchaseCarousel;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.SessionUtils;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
@RequestMapping( "/mallStore/cert" )
public class MallStoreCertificationController extends BaseController {

    @Autowired
    private MallStoreCertificationService mallStoreCertService;
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;
    @Autowired
    private DictService                   dictService;
    @Autowired
    private MallImageAssociativeService   imageAssociativeService;

    /**
     * 保存店铺认证信息
     */
    @ApiOperation( value = "保存店铺认证信息", notes = "保存店铺认证信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存店铺认证信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse save( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	    String code = params.get( "code" ).toString();
	    if ( CommonUtil.isEmpty( JedisUtil.get( Constants.REDIS_KEY + code ) ) ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "验证码不正确" );
	    }

	    MallStoreCertification storeCert = com.alibaba.fastjson.JSONObject.parseObject( params.get( "storeCert" ).toString(), MallStoreCertification.class );
	    if ( CommonUtil.isEmpty( storeCert.getId() ) ) {
		String[] docImg = params.get( "docImg" ).toString().split( "," );

		if ( docImg != null && docImg.length > 0 ) {
		    storeCert.setIsCertDoc( 1 );
		}
		storeCert.setCreateTime( new Date() );
		mallStoreCertService.insert( storeCert );

		for ( int i = 0; i < docImg.length; i++ ) {
		    MallImageAssociative associative = new MallImageAssociative();
		    associative.setAssId( storeCert.getId() );
		    associative.setAssType( 6 );
		    associative.setAssSort( i );
		    associative.setImageUrl( docImg[i] );
		    mallImageAssociativeService.insert( associative );
		}
	    } else {
		mallStoreCertService.updateById( storeCert );
		/*List< MallImageAssociative > imageList = JSONArray.parseArray( params.get( "imageList" ).toString(), MallImageAssociative.class );*/
		imageAssociativeService.newInsertUpdBatchImage( params, storeCert.getId(), 6 );

	    }

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
	List< Map > categoryMap = null;
	try {
	    //获取认证的店铺类型
	    categoryMap = dictService.getDict( "K002" );
	    for ( Map map : categoryMap ) {
		String value = (String) map.get( "item_value" );
		JSONObject foorerObj = JSONObject.fromObject( value );
		map.put( "value", foorerObj.get( "title" ) );//名称
		map.put( "childList", foorerObj.get( "array" ) );//子级
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
	    Map< String,Object > params = new HashMap<>();
	    params.put( "assType", 6 );
	    params.put( "assId", storeCert.getId() );
	    List< MallImageAssociative > imageAssociativeList = mallImageAssociativeService.selectByAssId( params );
	    storeCert.setImageList( imageAssociativeList );
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
		MallStoreCertification cert = new MallStoreCertification();
		cert.setId( id );
		cert.setIsDelete( 1 );
		mallStoreCertService.updateById( cert );
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
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean result = mallStoreCertService.getValCode( mobile, user.getId() );
	    if ( result ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	    } else {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "发送短信验证码异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取短信验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取短信验证码异常" );
	}
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
