package com.gt.mall.controller.api;

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
 *  订单评论后审接口
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

    @ApiOperation( value = "待审核评论的接口", notes = "获取所有商家待审核的评论" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认10条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/waitCheckList", method = RequestMethod.POST )
    public ServerResponse waitCheckList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //	    BusUser user = MallSessionUtils.getLoginUser( request );
	    //	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺

	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "pageSize", pageSize );
	    //	    params.put( "shoplist", shoplist );
	    params.put( "userIds", userIds );
	    params.put( "checkStatus", "0" );
	    // 查询会员下面的评论
	    PageUtil page = mallCommentService.selectCommentPage( params, null );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "待审核评论的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取待审核评论列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result ,false);
    }

    @ApiOperation( value = "评论审核", notes = "评论审核" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "审核状态 -1审核失败 1审核成功", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/commentCheck", method = RequestMethod.POST )
    public ServerResponse commentCheck( HttpServletRequest request, HttpServletResponse response, Integer id, Integer status ) {
	try {
	    boolean result = false;
	    if ( CommonUtil.isNotEmpty( id ) ) {
		MallComment comment = new MallComment();
		comment.setId( id );
		comment.setCheckStatus( status );
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
