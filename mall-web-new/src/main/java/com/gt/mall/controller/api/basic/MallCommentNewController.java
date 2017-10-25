package com.gt.mall.controller.api.basic;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城评论 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-26
 */

@Api( value = "mallComment", description = "评价管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallComment" )
public class MallCommentNewController extends BaseController {

    @Autowired
    private MallCommentService commentService;
    @Autowired
    private MallPaySetService  paySetService;
    @Autowired
    private MallStoreService   storeService;

    @ApiOperation( value = "评价列表(分页)", notes = "评价列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "queryName", value = "订单号或商品名称", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "feel", value = "评价状态 -1 差评 0 中评 1好评", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "startTime", value = "评价起始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "评价结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, String queryName, Integer shopId, Integer feel, String startTime,
		    String endTime ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isComment = false;
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = paySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsComment() ) ) {
		    if ( set.getIsComment() == 1 ) {
			isComment = true;
		    }
		}
	    }
	    result.put( "isComment", isComment );

	    if ( isComment ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺

		Map< String,Object > params = new HashMap<>();
		params.put( "curPage", curPage );
		params.put( "queryName", queryName );
		params.put( "shopId", shopId );
		params.put( "feel", feel );
		params.put( "startTime", startTime );
		params.put( "endTime", endTime );
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}

		// 查询会员下面的评论
		PageUtil page = commentService.selectCommentPage( params, shoplist );
		result.put( "page", page );

		//统计各状态的数量
		Map< String,Object > count = commentService.selectCommentCount( params );
		result.put( "count", count );
	    }

	} catch ( Exception e ) {
	    logger.error( "评价列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "评价列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 回复评论
     */
    @ApiOperation( value = "回复评论", notes = "回复评论" )
    @ResponseBody
    @SysLogAnnotation( description = "回复评论", op_function = "2" )
    @RequestMapping( value = "/reply", method = RequestMethod.POST )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "content", value = "回复内容", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = true, dataType = "int" ) } )
    public ServerResponse reply( HttpServletRequest request, HttpServletResponse response, String content, Integer id, Integer shopId ) {
	try {
	    //params:{"content":"32232","repPId":"25","shopId":"177"}
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallComment comment = new MallComment();
	    comment.setContent( CommonUtil.urlEncode( content ) );
	    comment.setRepPId( id );
	    comment.setShopId( shopId );
	    comment.setCreateTime( new Date() );
	    comment.setUserId( user.getId() );
	    comment.setUserType( 2 );
	    boolean count = commentService.insert( comment );
	    if ( count ) {
		MallComment pComment = new MallComment();
		pComment.setId( comment.getRepPId() );
		pComment.setIsRep( "1" );
		commentService.updateById( pComment );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "回复评论异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "回复评论异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 审核评论
     */
    @ApiOperation( value = "审核评论", notes = "审核评论" )
    @ResponseBody
    @SysLogAnnotation( description = "审核评论", op_function = "2" )
    @RequestMapping( value = "/checkComment", method = RequestMethod.POST )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "checkStatus", value = "审核状态 1通过 0不通过", paramType = "query", required = true, dataType = "int" ) } )
    public ServerResponse checkComment( HttpServletRequest request, HttpServletResponse response, Integer id, Integer checkStatus ) {
	try {
	    boolean result = false;
	    if ( CommonUtil.isNotEmpty( id ) ) {
		MallComment comment = new MallComment();
		comment.setId( id );
		comment.setCheckStatus( checkStatus );
		result = commentService.updateById( comment );
	    }
	    if ( !result ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "审核评论异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "审核评论异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
