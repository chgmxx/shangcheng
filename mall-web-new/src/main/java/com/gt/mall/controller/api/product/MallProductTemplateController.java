package com.gt.mall.controller.api.product;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallProductTemplate;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.product.MallProductTemplateService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.MallSessionUtils;
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
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商品页模板表 前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-20
 */
@Api( value = "mallProductTemplate", description = "商品页模板", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallProduct/E9lM9uM4ct/template" )
public class MallProductTemplateController extends BaseController {

    @Autowired
    private MallProductTemplateService mallProductTemplateService;

    @ApiOperation( value = "商品页模板列表(分页)", notes = "商品页模板列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( user != null ) {
		params.put( "userId", user.getId() );
		PageUtil page = mallProductTemplateService.findTemplateByPage( params );// 获取商品页模板集合
		result.put( "page", page );
	    }
	} catch ( Exception e ) {
	    logger.error( "商品页模板列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品页模板列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取商品页模板信息
     */
    @ApiOperation( value = "获取商品页模板信息", notes = "获取商品页模板信息" )
    @ResponseBody
    @RequestMapping( value = "/templateInfo", method = RequestMethod.POST )
    public ServerResponse templateInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "模板ID", required = true ) @RequestParam Integer id ) {
	MallProductTemplate template = null;
	try {
	    template = mallProductTemplateService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品页模板信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), template );
    }

    /**
     * 保存或修改页面信息
     */
    @ApiOperation( value = "保存商品页模板信息", notes = "保存商品页模板信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存商品页模板信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallProductTemplate template = JSONObject.parseObject( params.get( "template" ).toString(), MallProductTemplate.class );
	    if ( CommonUtil.isEmpty( template.getName() ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "模板名称不能为空" );
	    }
	    if ( CommonUtil.isEmpty( template.getTemplateCss() ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "模板样式不能为空" );
	    }
	    if ( CommonUtil.isEmpty( template.getTemplateData() ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "模板数据不能为空" );
	    }
	    if ( template != null ) {
		template.setName( CommonUtil.urlEncode( template.getName() ) );
		template.setTemplateData( CommonUtil.urlEncode( template.getTemplateData() ) );
		template.setTemplateCss( CommonUtil.urlEncode( template.getTemplateCss() ) );
		if ( CommonUtil.isNotEmpty( template.getId() ) ) {
		    mallProductTemplateService.updateById( template );
		} else {
		    template.setUserId( user.getId() );
		    template.setCreateTime( new Date() );
		    template.setIsDelete( 0 );
		    mallProductTemplateService.insert( template );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "参数不能为空" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存商品页模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存商品页模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除商品页模板
     */
    @ApiOperation( value = "删除商品页模板", notes = "删除商品页模板" )
    @ResponseBody
    @SysLogAnnotation( description = "删除商品页模板信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "模板ID集合,用逗号隔开", required = true ) @RequestParam String ids ) throws IOException {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( ids ) ) {
		String id[] = ids.toString().split( "," );
		boolean result = mallProductTemplateService.batchDelTemplate( id, user.getId() );
		if ( !result ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.INTER_ERROR.getCode(), ResponseEnums.INTER_ERROR.getDesc() );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "参数不能为空" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "删除商品页模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除商品页模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除商品页模板异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
