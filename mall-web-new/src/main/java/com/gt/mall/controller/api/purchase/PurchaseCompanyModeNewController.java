package com.gt.mall.controller.api.purchase;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dao.purchase.PurchaseCompanyModeDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.purchase.PurchaseCompanyMode;
import com.gt.mall.entity.purchase.PurchaseContract;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.purchase.PurchaseCompanyModeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对外报价-公司模板管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "purchaseCompany", description = "对外报价-公司模板管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "purchase/company" )
public class PurchaseCompanyModeNewController extends BaseController {

    @Autowired
    PurchaseCompanyModeService companyService;
    @Autowired
    private PurchaseCompanyModeDAO companyModeDAO;


    @ApiOperation( value = "公司模板列表(分页)", notes = "公司模板列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "keyWord", value = "公司名称/电话/官网", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, String keyWord ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "keyword", keyWord );
	    params.put( "busId", busUser.getId() );

	    PageUtil page = companyService.findList( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "获取公司模板列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取公司模板列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取公司模板信息
     */
    @ApiOperation( value = "获取公司模板信息", notes = "获取公司模板信息" )
    @ResponseBody
    @RequestMapping( value = "/companyInfo", method = RequestMethod.POST )
    public ServerResponse companyInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "公司模板ID", required = true ) @RequestParam Integer id ) {
	PurchaseCompanyMode company = null;
	try {
	    company = companyService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取公司模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取公司模板信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), company );
    }

    /**
     * 保存或修改公司模板信息
     */
    @ApiOperation( value = "保存公司模板信息", notes = "保存公司模板信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存公司模板信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    PurchaseCompanyMode company = new PurchaseCompanyMode();
	    company.setBusId( user.getId() );
	    company.setCompanyAddress( params.get( "companyAddress" ).toString() );
	    company.setCompanyInternet( params.get( "companyInternet" ).toString() );
	    company.setCompanyName( params.get( "companyName" ).toString() );
	    company.setLatitude( params.get( "latitude" ) != null ? params.get( "latitude" ).toString() : null );
	    company.setLongitude( params.get( "longitude" ) != null ? params.get( "longitude" ).toString() : null );
	    company.setCompanyTel( params.get( "companyTel" ).toString() );
	    if ( params.get( "id" ) != null && CommonUtil.isNotEmpty( params.get( "id" ).toString() ) ) {
		company.setId( Integer.parseInt( params.get( "id" ).toString() ) );
		companyService.updateById( company );
	    } else {
		companyService.insert( company );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存公司模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存公司模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除公司模板信息
     */
    @ApiOperation( value = "删除公司模板信息", notes = "删除公司模板信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除公司模板信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "公司模板Id", required = true ) @RequestParam Integer id ) {
	try {
	    companyService.deleteById( id );
	} catch ( BusinessException e ) {
	    logger.error( "删除公司模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除公司模板信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除公司模板信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 查询所有的公司模板
     */
    @ApiOperation( value = "查询所有的公司模板", notes = "查询所有的公司模板" )
    @ResponseBody
    @RequestMapping( value = "/companyModeList", method = RequestMethod.POST )
    public ServerResponse companyModeList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map< String,Object > > companyModeList = null;
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    companyModeList = companyModeDAO.findAllList( busUser.getId() );
	} catch ( Exception e ) {
	    logger.error( "查询所有的公司模板异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询所有的公司模板异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), companyModeList );
    }

}
