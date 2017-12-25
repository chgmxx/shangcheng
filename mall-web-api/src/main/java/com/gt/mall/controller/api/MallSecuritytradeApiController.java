package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dao.basic.MallSecuritytradeQuitDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallSecuritytradeQuit;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallSecuritytradeQuitService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 退出担保交易后审接口
 * Date : 2017/9/21 0021
 * User : yangqian
 * Time : 15:30
 */
@Api( value = "/mallSecuritytrade/mallAPI", description = "担保交易相关API接口", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallSecuritytrade/mallAPI" )
public class MallSecuritytradeApiController {

    private static Logger logger = LoggerFactory.getLogger( MallSecuritytradeApiController.class );

    @Autowired
    private MallSecuritytradeQuitService mallSecuritytradeQuitService;
    @Autowired
    private MallSecuritytradeQuitDAO     mallSecuritytradeQuitDAO;
    @Autowired
    private MallPaySetService            mallPaySetService;

    @ApiOperation( value = "待审核退出担保交易的接口", notes = "获取所有待审核退出担保交易的列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/waitCheckList", method = RequestMethod.POST )
    public ServerResponse waitCheckList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {

	    List< Map< String,Object > > htmlList = null;
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    pageSize = CommonUtil.isEmpty( pageSize ) ? 15 : pageSize;

	    Map< String,Object > params = new HashMap<>();
	    params.put( "checkStatus","0");
	    if ( CommonUtil.isNotEmpty( userIds ) ) {
		params.put( "userIds", userIds.split( "," ) );
	    }
	    int count = mallSecuritytradeQuitDAO.selectAllCount( params );

	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	    params.put( "firstNum", firstNum );// 起始页
	    params.put( "maxNum", pageSize );// 每页显示商品的数量

	    if ( count > 0 ) {// 判断是否有数据
		htmlList = mallSecuritytradeQuitDAO.selectAllByPage( params );// 查询h5商城总数
	    }
	    page.setSubList( htmlList );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取所有待审核退出担保交易的列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取所有待审核退出担保交易的列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "担保交易审核", notes = "担保交易审核" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "评论ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "审核状态  1通过 -1不通过", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "reason", value = "不通过理由", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/securityCheck", method = RequestMethod.POST )
    public ServerResponse commentCheck( HttpServletRequest request, HttpServletResponse response, Integer id, Integer status, String reason ) {
	try {
	    MallSecuritytradeQuit quit = mallSecuritytradeQuitService.selectById( id );
	    quit.setCheckStatus( status );
	    quit.setCheckTime( new Date() );
	    quit.setRefuseReason( reason );
	    boolean flag = mallSecuritytradeQuitService.updateById( quit );

	    if ( status == 1 ) {
		MallPaySet set = new MallPaySet();
		set.setUserId( quit.getUserId() );
		set = mallPaySetService.selectByUserId( set );
		set.setIsSecuritytrade( 0 );
		mallPaySetService.updateById( set );
	    }
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改审核状态异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "修改审核状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "修改审核状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改审核状态异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
