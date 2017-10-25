package com.gt.mall.controller.api.html;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.html.MallHtmlFromService;
import com.gt.mall.service.web.html.MallHtmlReportService;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * h5商城 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "mallHtml", description = "h5商城", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallHtml" )
public class MallHtmlNewController extends BaseController {

    @Autowired
    private MallHtmlService     htmlService;
    @Autowired
    private MallHtmlFromService htmlFromService;
    @Autowired
    private DictService         dictService;
    @Autowired
    private BusUserService      busUserService;

    @ApiOperation( value = "h5商城列表(分页)", notes = "h5商城列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "user_id", user.getId() );
	    PageUtil page = htmlService.newHtmlList( request, params );
	    result.put( "page", page );
	    Integer iscreat = 0;//是否还可以创建h5商城0 可以，1不可以
	    Integer ispid = 0;//是否是主账号，0是主账号，1是子账号管理，2是子账号用户
	    //pid==0 主账户,否则是子账户
	    if ( user.getPid() == 0 ) {
	    } else {
		boolean isadmin = busUserService.getIsAdmin( user.getId() );
		if ( isadmin ) {
		    Integer zhuid = SessionUtils.getAdminUserId( user.getId(), request );//获取父类的id
		    user = busUserService.selectById( zhuid );
		    ispid = 1;
		} else {
		    ispid = 2;
		}
	    }
	    if ( ispid != 2 ) {
		Integer maxcj = Integer.valueOf( dictService.getDiBserNum( user.getId(), 16, "1140" ) );
		Integer ycj = htmlService.htmltotal( user.getId() );//主账户之下已创建的数量
		if ( ycj >= maxcj ) {
		    iscreat = 1;
		}
		result.put( "iscreat", iscreat );
	    }
	    result.put( "ispid", ispid );
	    result.put( "videourl", busUserService.getVoiceUrl( "87" ) );
	} catch ( Exception e ) {
	    logger.error( "h5商城列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取模板列表
     */
    @ApiOperation( value = "获取模板列表", notes = "获取模板列表" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/modelList", method = RequestMethod.POST )
    public ServerResponse modelList( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    /*map = htmlService.modelList( request );*/
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    PageUtil page = htmlService.newModelList( request, params );
	    map.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取模板列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取模板列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), map );
    }

    /**
     * 添加H5商城选中模板
     */
    @ApiOperation( value = "添加H5商城选中模板", notes = "添加H5商城选中模板" )
    @ResponseBody
    @RequestMapping( value = "/setMallHtml", method = RequestMethod.POST )
    public ServerResponse setMallHtml( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "模板id", required = true ) @RequestParam Integer id ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );//获取登录信息
	    Integer maxcj = Integer.valueOf( dictService.getDiBserNum( user.getId(), 16, "1140" ) );
	    Integer ycj = htmlService.htmltotal( user.getId() );//主账户之下已创建的数量
	    if ( ycj >= maxcj ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "等级不够，不能创建h5商城" );
	    } else {
		Integer xid = htmlService.SetmallHtml( id, user );
	    }
	} catch ( Exception e ) {
	    logger.error( "添加H5商城选中模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取h5商城信息
     */
    @ApiOperation( value = "获取h5商城信息", notes = "获取h5商城信息" )
    @ResponseBody
    @RequestMapping( value = "/htmlInfo", method = RequestMethod.POST )
    public ServerResponse htmlInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "h5ID", required = true ) @RequestParam Integer id ) {
	MallHtml obj = null;
	try {
	    obj = htmlService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取h5商城信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取h5商城信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), obj );
    }

    /**
     * 保存修改信息
     */
    @ApiOperation( value = "保存修改信息", notes = "保存修改信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存修改信息", op_function = "2" )
    @RequestMapping( value = "/update", method = RequestMethod.POST )
    public ServerResponse update( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallHtml obj = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( params ), MallHtml.class );
	    obj.setHtmlname( CommonUtil.urlEncode( obj.getHtmlname() ) );
	    obj.setIntroduce( CommonUtil.urlEncode( obj.getIntroduce() ) );
	    htmlService.addorUpdateSave( obj, user );
	} catch ( BusinessException e ) {
	    logger.error( "保存修改信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存修改信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 保存背景图
     */
    @ApiOperation( value = "保存背景图", notes = "保存背景图" )
    @ResponseBody
    @SysLogAnnotation( description = "保存背景图", op_function = "2" )
    @RequestMapping( value = "/updateImage", method = RequestMethod.POST )
    public ServerResponse updateImage( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "h5Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "imgUrl", value = "背景图URL", required = true ) @RequestParam String imgUrl ) {
	try {
	    htmlService.updateimage( id, imgUrl );
	} catch ( BusinessException e ) {
	    logger.error( "保存背景图异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存背景图异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除h5商城
     */
    @ApiOperation( value = "删除h5商城", notes = "删除h5商城" )
    @ResponseBody
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "h5ID", required = true ) @RequestParam Integer id ) {
	try {
	    htmlService.deleteById( id );
	} catch ( BusinessException e ) {
	    logger.error( "删除h5商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除h5商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取h5表单列表
     */
    @ApiOperation( value = "获取h5表单列表", notes = "获取h5表单列表" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "id", value = "h5Id", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/htmlFromList", method = RequestMethod.POST )
    public ServerResponse htmlFromList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer id ) {
	Map< String,Object > map = new HashMap<>();
	try {
	  /*  map = htmlFromService.htmlListfrom( request );*/
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "htmlId", id );
	    PageUtil page = htmlFromService.newHtmlListfrom( request, params );
	    map.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取h5表单列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取h5表单列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), map );
    }

    /**
     * 查看h5表单详情
     */
    @ApiOperation( value = "查看h5表单详情", notes = "查看h5表单详情" )
    @ResponseBody
    @RequestMapping( value = "/htmlFromView", method = RequestMethod.POST )
    public ServerResponse htmlFromView( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "h5表单id", required = true ) @RequestParam Integer id ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    map = htmlFromService.htmlfromview( request );
	} catch ( Exception e ) {
	    logger.error( "查看h5表单详情异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查看h5表单详情异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), map );
    }

    /**
     * 获取播放器样式列表
     */
    @ApiOperation( value = "获取播放器样式列表", notes = "获取播放器样式列表" )
    @ResponseBody
    @RequestMapping( value = "/playList", method = RequestMethod.POST )
    public ServerResponse playList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map > playList = null;
	try {
	    playList = dictService.getDict( "1048" );//获取播放器样式
	} catch ( Exception e ) {
	    logger.error( "获取播放器样式列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取播放器样式列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), playList );
    }

    /**
     * 表单页面设计保存
     */
    @ApiOperation( value = "表单页面设计保存", notes = "表单页面设计保存" )
    @ResponseBody
    @SysLogAnnotation( description = "表单页面设计保存", op_function = "2" )
    @RequestMapping( value = "/htmlSave", method = RequestMethod.POST )
    public ServerResponse htmlSave( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallHtml obj = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( params ), MallHtml.class );
	    htmlService.htmlSave( obj, user );
	} catch ( BusinessException e ) {
	    logger.error( "表单页面设计保存异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "表单页面设计保存异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
