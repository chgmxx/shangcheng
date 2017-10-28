package com.gt.mall.controller.api.integral;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.integral.MallIntegralImageService;
import com.gt.mall.service.web.integral.MallIntegralService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-27
 */
@Api( value = "mallIntegral", description = "积分商城管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallIntegral/E9lM9uM4ct" )
public class MallIntegralNewController extends BaseController {

    @Autowired
    private MallIntegralService      integralService;
    @Autowired
    private MallIntegralImageService integralImageService;
    @Autowired
    private MallStoreService         storeService;
    @Autowired
    private BusUserService           busUserService;


    @ApiOperation( value = "积分列表(分页)", notes = "积分列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "type", type );
	    params.put( "shopId", shopId );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = integralService.selectIntegralByPage( params, user.getId(), shoplist );
		result.put( "page", page );
	    }
	    /*request.setAttribute("videourl", course.urlquery("86"));*/
	} catch ( Exception e ) {
	    logger.error( "获取积分列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取积分信息
     */
    @ApiOperation( value = "获取积分信息", notes = "获取积分信息" )
    @ResponseBody
    @RequestMapping( value = "/integralInfo", method = RequestMethod.POST )
    public ServerResponse integralInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "积分ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > integralMap = null;
	try {
	    integralMap = integralService.selectByIds( id );
	} catch ( Exception e ) {
	    logger.error( "获取积分信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), integralMap );
    }

    /**
     * 保存或修改积分信息
     */
    @ApiOperation( value = "保存积分信息", notes = "保存积分信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存积分信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > resultMap = integralService.saveIntegral( user.getId(), params );
	    boolean flag = (boolean) resultMap.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), resultMap.get( "msg" ).toString() );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存积分信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存积分信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 设置积分的状态（删除，失效 ，启用）
     */
    @ApiOperation( value = "设置积分的状态（删除，失效 ，启用）", notes = "设置积分的状态（删除，失效 ，启用）" )
    @ResponseBody
    @SysLogAnnotation( description = "设置积分的状态（删除，失效 ，启用）", op_function = "4" )
    @RequestMapping( value = "/setStatus", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "积分Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效 1启用失效商品", required = true ) @RequestParam Integer type ) {
	try {
	    MallIntegral integral = new MallIntegral();
	    integral.setId( id );
	    if ( type == -1 ) {//删除积分商品
		integral.setIsDelete( 1 );
	    } else if ( type == -2 ) {//使失效积分商品
		integral.setIsUse( -1 );
	    } else if ( type == 1 ) {//启用失效积分商品
		integral.setIsUse( 1 );
	    }
	    boolean flag = integralService.updateById( integral );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置积分的状态异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "设置积分的状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "设置积分的状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置积分的状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取链接
     */
    @ApiOperation( value = "获取链接", notes = "获取链接" )
    @ResponseBody
    @RequestMapping( value = "/link", method = RequestMethod.POST )
    public ServerResponse link( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "积分ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {

	    MallIntegral integral = integralService.selectById( id );
	    String url = PropertiesUtil.getHomeUrl() + "/phoneIntegral/79B4DE7C/integralProduct.do?id=" + integral.getId() + "&uId=" + integral.getUserId() + "&shopId=" + integral
			    .getShopId();
	    result.put( "link", url );//链接
	} catch ( Exception e ) {
	    logger.error( "获取链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }


    /***********************************积分商城图片***********************************************/

    @ApiOperation( value = "积分商城图片列表(分页)", notes = "积分商城图片列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/image/list", method = RequestMethod.POST )
    public ServerResponse imageList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "shopId", shopId );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "userId", user.getId() );
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = integralImageService.selectImageByShopId( params, user.getId(), shoplist );
		result.put( "page", page );
	    }
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "86" ) );
	} catch ( Exception e ) {
	    logger.error( "获取积分商城图片列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分商城图片列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取积分商城图片信息
     */
    @ApiOperation( value = "获取积分商城图片信息", notes = "获取积分商城图片信息" )
    @ResponseBody
    @RequestMapping( value = "/image/imageInfo", method = RequestMethod.POST )
    public ServerResponse integralImageInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "积分商城图片Id", required = true ) @RequestParam Integer id ) {
	MallIntegralImage imageMap = null;
	try {
	     imageMap = integralImageService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取积分商城图片信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分商城图片信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), imageMap );
    }

    /**
     * 保存积分商城图片
     */
    @ApiOperation( value = "保存积分商城图片", notes = "保存积分商城图片" )
    @ResponseBody
    @SysLogAnnotation( description = "保存积分商城图片", op_function = "2" )
    @RequestMapping( value = "/image/save", method = RequestMethod.POST )
    public ServerResponse imageSave( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    boolean flag = integralImageService.editImage( params, user.getId() );// 编辑商品
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存积分商城图片异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存积分商城图片异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存积分商城图片异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }


    /**
     * 设置积分商城图片的状态（删除，失效 ，启用）
     */
    @ApiOperation( value = "设置积分商城图片的状态（删除，不显示 ，显示）", notes = "设置积分商城图片的状态（删除，不显示 ，显示）" )
    @ResponseBody
    @SysLogAnnotation( description = "设置积分商城图片的状态（删除，不显示 ，显示）", op_function = "4" )
    @RequestMapping( value = "/image/setStatus", method = RequestMethod.POST )
    public ServerResponse setImageStatus( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "积分商城图片Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2不显示 1显示", required = true ) @RequestParam Integer type ) {
	try {
	    MallIntegralImage images = new MallIntegralImage();
	    images.setId( id );
	    if ( type == -1 ) {
		images.setIsDelete( 1 );
	    } else if ( type == -2 ) {
		images.setIsShow( 0 );
	    } else {
		images.setIsShow( 1 );
	    }
	    boolean flag = integralImageService.updateById( images );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置积分商城图片的状态异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "设置积分商城图片的状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "设置积分商城图片的状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置积分商城图片的状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
    /**
     * 获取链接
     */
    @ApiOperation( value = "获取积分商城图片链接", notes = "获取积分商城图片链接" )
    @ResponseBody
    @RequestMapping( value = "/image/link", method = RequestMethod.POST )
    public ServerResponse imageLink( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "shopId", value = "店铺ID", required = true ) @RequestParam Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    String url = PropertiesUtil.getHomeUrl() + "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + user.getId();
	    result.put( "link", url );//链接
	} catch ( Exception e ) {
	    logger.error( "获取积分商城图片链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
