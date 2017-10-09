package com.gt.mall.controller.api.html;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    private MallPaySetService     mallPaySetService;
    @Autowired
    private WxPublicUserService   wxPublicUserService;
    @Autowired
    private MallHtmlService       htmlService;
    @Autowired
    private MallHtmlFromService   htmlFromService;
    @Autowired
    private MallHtmlReportService htmlReportService;
    @Autowired
    private DictService           dictService;
    @Autowired
    private BusUserService        busUserService;

    @ApiOperation( value = "h5商城列表(分页)", notes = "h5商城列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "pageNum", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer pageNum ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > map = htmlService.htmlList( request );
	    result.put( "map", map );
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
    @ApiImplicitParams( { @ApiImplicitParam( name = "pageNum", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/modelList", method = RequestMethod.POST )
    public ServerResponse modelList( HttpServletRequest request, HttpServletResponse response, Integer pageNum ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    map = htmlService.modelList( request );
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
    public ServerResponse setMallHtml( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );

	    params.put( "userId", user.getId() );
	    int num = mallPaySetService.editPaySet( params );
	    if ( num <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存修改信息异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
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

	    params.put( "userId", user.getId() );
	    int num = mallPaySetService.editPaySet( params );
	    if ( num <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存修改信息异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }




    /**
     * 通用设置保存
     */
    @ApiOperation( value = "保存商城设置信息", notes = "保存商城设置信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存商城设置信息", op_function = "2" )
    @RequestMapping( value = "/setSave", method = RequestMethod.POST )
    public ServerResponse setSave( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    //	    set.setUserId( user.getId() );
	    //	    com.alibaba.fastjson.JSONObject set2 = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( set ) );
	    params.put( "userId", user.getId() );
	    int num = mallPaySetService.editPaySet( params );
	    if ( num <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存商城设置信息异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }


    /**
     * 获取消息模板
     */
    @ApiOperation( value = "获取消息模板", notes = "获取消息模板" )
    @ResponseBody
    @RequestMapping( value = "/getTemplate", method = RequestMethod.POST )
    public ServerResponse getTemplate( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
		    JSONArray msgArr = JSONArray.fromObject( set.getMessageJson() );
		    result.put( "msgArr", msgArr ); //选中消息模板
		}
	    }

	    List< Map > messageList = wxPublicUserService.selectTempObjByBusId( user.getId() );
	    result.put( "messageList", messageList );
	} catch ( Exception e ) {
	    logger.error( "获取消息模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取消息模板异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
