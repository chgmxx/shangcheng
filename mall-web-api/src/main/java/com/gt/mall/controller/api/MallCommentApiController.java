package com.gt.mall.controller.api;

import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallCommonService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO 订单评论后审接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallComment/mallAPI" )
public class MallCommentApiController {

    private static Logger logger = LoggerFactory.getLogger( MallCommentApiController.class );

    @Autowired
    private MallCommonService mallCommonService;
    @Autowired
    private MallPaySetService mallPaySetService;

    @ApiOperation( value = "待审核评论的接口", notes = "获取所有商家待审核的评论" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/waitCheckList", method = RequestMethod.GET )
    public ServerResponse waitCheckList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {

	} catch ( Exception e ) {
	    logger.error( "获取待审核商品的异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取待审核评论的异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "查看评论明细", notes = "查看评论明细" )
    @ResponseBody
    @RequestMapping( value = "/commentDeatil", method = RequestMethod.GET )
    public ServerResponse commentDeatil( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "评论ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {

	} catch ( Exception e ) {
	    logger.error( "查看评论明细异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查看评论明细异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "评论审核", notes = "评论审核" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "审核状态 -1审核失败 1审核成功", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "checkReason", value = "审核不通过的原因", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/commentCheck", method = RequestMethod.GET )
    public ServerResponse commentCheck( HttpServletRequest request, HttpServletResponse response, Integer id, Integer status, String checkReason ) {
	try {

	} catch ( Exception e ) {
	    logger.error( "评论审核异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "评论审核异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
