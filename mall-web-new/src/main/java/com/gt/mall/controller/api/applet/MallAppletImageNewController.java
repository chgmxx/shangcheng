package com.gt.mall.controller.api.applet;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.applet.MallAppletImageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-26
 */
@Api( value = "mallApplet", description = "小程序图片设置", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallApplet/E9lM9uM4ct" )
public class MallAppletImageNewController extends BaseController {

    @Autowired
    private MallAppletImageService appletImageService;

    @Autowired
    private MallStoreService storeService;

    @ApiOperation( value = "小程序图片管理列表(分页)", notes = "小程序图片管理列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "userId", user.getId() );
		PageUtil page = appletImageService.selectImageByShopId( params );
		result.put( "page", page );

	    }
	} catch ( Exception e ) {
	    logger.error( "小程序图片管理列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "小程序图片管理列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取小程序图片信息
     */
    @ApiOperation( value = "获取小程序图片信息", notes = "获取小程序图片信息" )
    @ResponseBody
    @RequestMapping( value = "/appletInfo", method = RequestMethod.POST )
    public ServerResponse appletInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "小程序图片ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > imageMap = null;
	try {
	    imageMap = appletImageService.selectImageById( id );
	} catch ( Exception e ) {
	    logger.error( "获取小程序图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取小程序图片信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), imageMap );
    }

    /**
     * 保存或修改小程序图片信息
     */
    @ApiOperation( value = "保存小程序图片信息", notes = "保存小程序图片信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存小程序图片信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int code = -1;// 编辑成功
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		boolean flag = appletImageService.editImage( params, userId );// 编辑商品
		if ( flag ) { code = 1;}
	    }
	    if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存小程序图片失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存小程序图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存小程序图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除小程序图片信息
     */
    @ApiOperation( value = "删除小程序图片信息", notes = "删除小程序图片信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除小程序图片信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "小程序图片Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2不显示 1显示", required = true ) @RequestParam Integer type ) {
	try {
	    MallAppletImage images = new MallAppletImage();
	    images.setId( id );
	    if ( type == -1 ) {
		images.setIsDelete( 1 );
	    } else if ( type == -2 ) {
		images.setIsShow( 0 );
	    } else {
		images.setIsShow( 1 );
	    }
	    boolean flag = appletImageService.updateById( images );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除小程序图片记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除小程序图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除小程序图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除小程序图片信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
