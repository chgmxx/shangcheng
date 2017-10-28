package com.gt.mall.controller.api.set;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城设置 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Api( value = "mallPaySet", description = "商城设置(通用设置，消息模板)", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallPaySet/E9lM9uM4ct" )
public class MallPaySetController extends BaseController {

    @Autowired
    private MallPaySetService   mallPaySetService;
    @Autowired
    private WxPublicUserService wxPublicUserService;

    /**
     * 获取商城设置
     */
    @ApiOperation( value = "获取商城设置", notes = "获取商城设置" )
    @ResponseBody
    @RequestMapping( value = "/paySetInfo", method = RequestMethod.POST )
    public ServerResponse paySetInfo( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    result.put( "set", set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getSmsMessage() ) ) {
		    Map smsMsgObj = JSONObject.fromObject( set.getSmsMessage() );
		    result.put( "paySuccessText", smsMsgObj.get( "1" ) );//支付成功提醒内容
		}
		if ( CommonUtil.isNotEmpty( set.getFooterJson() ) ) {
		    JSONObject foorerObj = JSONObject.fromObject( set.getFooterJson() );
		    result.put( "foorerObj", foorerObj );//手机端底部菜单
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商城设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商城设置异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
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
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map > msgArr = null;
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
		    msgArr = JSONArray.fromObject( set.getMessageJson() );
		}
		if ( CommonUtil.isNotEmpty( set.getBusMessageJson() ) ) {
		    JSONArray busMsgArr = JSONArray.fromObject( set.getBusMessageJson() );
		    result.put( "busMsgArr", busMsgArr );
		}
	    }

	    List< Map > messageList = wxPublicUserService.selectTempObjByBusId( user.getId() );
	    if ( messageList != null && messageList.size() > 0 ) {
		for ( Map map : messageList ) {
		    for ( Map obj : msgArr ) {
			map.put( "selected", "0" );
			if ( CommonUtil.toInteger( obj.get( "id" ) ) == CommonUtil.toInteger( map.get( "id" ) ) ) {
			    map.put( "selected", "1" );
			    break;
			}
		    }
		}
	    }
	    result.put( "messageList", messageList );
	} catch ( Exception e ) {
	    logger.error( "获取消息模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取消息模板异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
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
	    BusUser user = MallSessionUtils.getLoginUser( request );
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
     * 消息模板设置
     */
    @ApiOperation( value = "设置消息模板", notes = "设置消息模板" )
    @ResponseBody
    @SysLogAnnotation( description = "设置消息模板", op_function = "2" )
    @RequestMapping( value = "/setSmsTemplate", method = RequestMethod.POST )
    @ApiImplicitParams( { @ApiImplicitParam( name = "template_json", value = "{\"title\":\"积分变动提醒\",\"id\":23} ", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "type", value = "商家0/粉丝1", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "operation", value = "操作 1开启 0关闭", paramType = "query", required = true, dataType = "int" ) } )
    public ServerResponse setSmsTemplate( HttpServletRequest request, HttpServletResponse response, String template_json, Integer type, Integer operation ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map > msgArr = null;
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( type == 1 ) {
		    if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
			msgArr = JSONArray.fromObject( set.getMessageJson() );
		    } else {
			msgArr = new ArrayList<>();
		    }
		} else {
		    if ( CommonUtil.isNotEmpty( set.getBusMessageJson() ) ) {
			msgArr = JSONArray.fromObject( set.getBusMessageJson() );
		    } else {
			msgArr = new ArrayList<>();
		    }
		}
	    }
	    Map temp = JSONObject.fromObject( template_json );

	    if ( operation == 1 ) {
		msgArr.add( temp );
		if ( type == 0 ) {//商家
		    set.setBusMessageJson( msgArr.toString() );
		} else {
		    set.setMessageJson( msgArr.toString() );
		}
	    } else {
		for ( Map map : msgArr ) {
		    if ( CommonUtil.toInteger( map.get( "id" ) ) == CommonUtil.toInteger( temp.get( "id" ) ) ) {
			msgArr.remove( map );
			break;
		    }
		}
		if ( type == 0 ) {//商家
		    set.setBusMessageJson( msgArr.toString() );
		} else {
		    set.setMessageJson( msgArr.toString() );
		}
	    }
	    mallPaySetService.updateById( set );

	} catch ( BusinessException e ) {
	    logger.error( "设置消息模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "设置消息模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 判断有无认证服务号
     */
    @ApiOperation( value = "判断有无认证服务号", notes = "判断有无认证服务号" )
    @ResponseBody
    @SysLogAnnotation( description = "保存商城设置信息", op_function = "2" )
    @RequestMapping( value = "/isAuthService", method = RequestMethod.POST )
    public ServerResponse isAuthService( HttpServletRequest request, HttpServletResponse response ) {
	boolean flag = true;
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( user.getId() );
	    if ( wxPublicUsers == null ) {flag = false;}
	} catch ( BusinessException e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flag );
    }

}
