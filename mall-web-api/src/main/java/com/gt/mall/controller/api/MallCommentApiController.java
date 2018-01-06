package com.gt.mall.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单评论后审接口
 * Date : 2017/9/21 0021
 * User : yangqian
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallComment/mallAPI" )
public class MallCommentApiController {

    private static Logger logger = LoggerFactory.getLogger( MallCommentApiController.class );

    @Autowired
    private MallCommentService mallCommentService;
    @Autowired
    private MallStoreService   storeService;

    @ApiOperation( value = "商品评论列表的接口", notes = "获取所有订单评论列表" )
    //    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
    //		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认10条", paramType = "query", required = false, dataType = "int" ),
    //		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );
	    //	    params.put( "curPage", curPage );
	    //	    params.put( "pageSize", pageSize );
	    //	    params.put( "shoplist", shoplist );
	    //	    params.put( "userIds", userIds );
	    //	    params.put( "checkStatus", "0" );
	    // 查询会员下面的评论
	    PageUtil page = mallCommentService.selectCommentPage( params, null );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "商品评论列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品评论列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    @ApiOperation( value = "评论审核", notes = "评论审核" )
    //    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
    //		    @ApiImplicitParam( name = "status", value = "审核状态 -1审核失败 1审核成功", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/commentCheck", method = RequestMethod.POST )
    public ServerResponse commentCheck( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );
	    boolean result = false;
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		MallComment comment = new MallComment();
		comment.setId( CommonUtil.toInteger( params.get( "id" ) ) );
		comment.setCheckStatus( CommonUtil.toInteger( params.get( "status" ) ) );
		result = mallCommentService.updateById( comment );
	    }
	    if ( !result ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "审核评论异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "评论审核异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "评论审核异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
