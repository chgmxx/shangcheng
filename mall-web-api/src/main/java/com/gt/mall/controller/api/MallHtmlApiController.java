package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dao.html.MallHtmlDAO;
import com.gt.mall.dao.html.MallHtmlReportDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.html.MallHtmlReport;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.utils.CommonUtil;
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
 * h5商城相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallHtml/mallAPI" )
public class MallHtmlApiController {

    private static Logger logger = LoggerFactory.getLogger( MallHtmlApiController.class );

    @Autowired
    private MallHtmlService   mallHtmlService;
    @Autowired
    private MallHtmlDAO       mallHtmlDAO;
    @Autowired
    private MallHtmlReportDAO mallHtmlReportDAO;

    @ApiOperation( value = "h5商城列表", notes = "h5商城列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "htmlName", value = "商城名称", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "userIds", value = "商家用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/htmlList", method = RequestMethod.POST )
    public ServerResponse htmlList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String htmlName, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    List< Map< String,Object > > htmlList = null;
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    pageSize = CommonUtil.isEmpty( pageSize ) ? 15 : pageSize;

	    Map< String,Object > params = new HashMap<>();
	    params.put( "htmlName", htmlName );
	    if ( CommonUtil.isNotEmpty( userIds ) ) {
		params.put( "userIds", userIds.split( "," ) );
	    }
	    int count = mallHtmlDAO.selectAllCount( params );

	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	    params.put( "firstNum", firstNum );// 起始页
	    params.put( "maxNum", pageSize );// 每页显示商品的数量

	    if ( count > 0 ) {// 判断是否有数据
		htmlList = mallHtmlDAO.selectAllByPage( params );// 查询h5商城总数
	    }
	    page.setSubList( htmlList );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取h5商城列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取h5商城列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "设置为模板", notes = "设置为模板" )
    @ResponseBody
    @RequestMapping( value = "/setModel", method = RequestMethod.POST )
    public ServerResponse setModel( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "模板Id", required = true ) @RequestParam Integer id ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( id );
	    if ( mallHtml != null ) {
		mallHtml.setId( null );
		mallHtml.setState( 1 );
		mallHtml.setBusUserId( 1 );
		mallHtml.setSourceType( 1 );
		mallHtmlService.insert( mallHtml );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "设置为模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置为模板异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "发布模板", notes = "发布模板" )
    @ResponseBody
    @RequestMapping( value = "/publishModel", method = RequestMethod.POST )
    public ServerResponse publishModel( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "模板Id", required = true ) @RequestParam Integer id ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( id );
	    if ( mallHtml != null ) {
		mallHtml.setState( 0 );
		mallHtmlService.updateById( mallHtml );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "发布模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "发布模板异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "修改h5商城", notes = "修改h5商城" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "模板ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "introduce", value = "商城介绍", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "htmlName", value = "商城名称", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "bakurl", value = "背景图路径", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/updateHtml", method = RequestMethod.POST )
    public ServerResponse updateHtml( HttpServletRequest request, HttpServletResponse response, Integer id, String htmlName, String introduce, String bakurl ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( id );
	    if ( mallHtml != null ) {
		mallHtml.setHtmlname( htmlName );
		mallHtml.setIntroduce( introduce );
		mallHtml.setBakurl( bakurl );
		mallHtmlService.updateById( mallHtml );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "修改h5商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改h5商城异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "删除H5商城", notes = "删除H5商城" )
    @ResponseBody
    @RequestMapping( value = "/delHtml", method = RequestMethod.POST )
    public ServerResponse delHtml( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "模板Id", required = true ) @RequestParam Integer id ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( id );
	    if ( mallHtml != null ) {
		Wrapper< MallHtml > wrapper = new EntityWrapper<>();
		wrapper.where( "id= {0}", id );
		mallHtmlDAO.delete( wrapper );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "删除H5商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除H5商城异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "h5商城举报列表", notes = "h5商城举报列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "htmlName", value = "商城名称", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "userIds", value = "商家用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/htmlReport", method = RequestMethod.POST )
    public ServerResponse htmlReport( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String htmlName, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    List< Map< String,Object > > htmlList = null;
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    pageSize = CommonUtil.isEmpty( pageSize ) ? 15 : pageSize;

	    Map< String,Object > params = new HashMap<>();
	    params.put( "htmlName", htmlName );
	    if ( CommonUtil.isNotEmpty( userIds ) ) {
		params.put( "userIds", userIds.split( "," ) );
	    }
	    int count = mallHtmlReportDAO.selectAllCount( params );

	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	    params.put( "firstNum", firstNum );// 起始页
	    params.put( "maxNum", pageSize );// 每页显示商品的数量

	    if ( count > 0 ) {// 判断是否有数据
		htmlList = mallHtmlReportDAO.selectAllByPage( params );// 查询h5商城总数
	    }
	    page.setSubList( htmlList );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取h5商城举报列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取h5商城举报列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "禁用H5商城", notes = "禁用H5商城" )
    @ResponseBody
    @RequestMapping( value = "/disableHtml", method = RequestMethod.POST )
    public ServerResponse disableHtml( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "htmlId", value = "模板Id", required = true ) @RequestParam Integer htmlId ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( htmlId );
	    if ( mallHtml != null ) {
		mallHtml.setReportstate( 1 );
		mallHtmlDAO.updateById( mallHtml );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "禁用H5商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "禁用H5商城异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "删除举报信息", notes = "删除举报信息" )
    @ResponseBody
    @RequestMapping( value = "/disableReport", method = RequestMethod.POST )
    public ServerResponse disableReport( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "htmlId", value = "模板Id", required = true ) @RequestParam Integer htmlId ) {
	try {
	    MallHtml mallHtml = mallHtmlService.selectById( htmlId );
	    if ( mallHtml != null ) {
		mallHtml.setReportstate( 1 );
		mallHtmlDAO.updateById( mallHtml );

		Wrapper< MallHtmlReport > wrapper = new EntityWrapper<>();
		wrapper.where( "html_id = {0}", htmlId );
		mallHtmlReportDAO.delete( wrapper );
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "h5商城不存在" );
	    }
	} catch ( Exception e ) {
	    logger.error( "删除举报信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除举报信息异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "获取举报明细", notes = "获取举报明细" )
    @ResponseBody
    @RequestMapping( value = "/reportInfo", method = RequestMethod.POST )
    public ServerResponse reportInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "htmlId", value = "模板Id", required = true ) @RequestParam Integer htmlId ) {
	List< Map< String,Object > > reportList = null;
	try {
	    Wrapper< MallHtmlReport > wrapper = new EntityWrapper<>();
	    wrapper.setSqlSelect( "style,SUM(report_num) reportNum " );
	    wrapper.where( "html_id = {0}", htmlId );
	    wrapper.groupBy( "style" );
	    reportList = mallHtmlReportDAO.selectMaps( wrapper );
	    if ( reportList != null && reportList.size() > 0 ) {
		for ( Map< String,Object > report : reportList ) {
		    Integer style = CommonUtil.toInteger( report.get( "style" ) );
		    String styleName = "";
		    switch ( style ) {
			case 1:
			    styleName = "诈骗、反社会、谣言";
			    break;
			case 2:
			    styleName = "色情、赌博、毒品";
			    break;
			case 3:
			    styleName = "传销、邪教、非法集会";
			    break;
			case 4:
			    styleName = "侵权、抄袭";
			    break;
			case 5:
			    styleName = "恶意营销、侵犯隐私、诱导分享";
			    break;
			case 6:
			    styleName = "虚假广告";
			    break;
			case 7:
			    styleName = "其他原因";
			    break;
		    }
		    report.put( "styleName", styleName );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取举报明细异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取举报明细异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), reportList );
    }
}
