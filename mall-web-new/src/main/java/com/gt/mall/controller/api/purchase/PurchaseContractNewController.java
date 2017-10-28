package com.gt.mall.controller.api.purchase;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dao.purchase.PurchaseContractDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.purchase.PurchaseContract;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.purchase.PurchaseContractService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对外报价-合同管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "purchaseContract", description = "对外报价-合同管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "purchase/E9lM9uM4ct/contract" )
public class PurchaseContractNewController extends BaseController {

    @Autowired
    PurchaseContractService contractService;
    @Autowired
    private PurchaseContractDAO contractDAO;

    @ApiOperation( value = "合同列表(分页)", notes = "合同列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "contractTitle", value = "合同标题", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, String contractTitle ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "contractTitle", contractTitle );
	    params.put( "busId", busUser.getId() );

	    PageUtil page = contractService.findList( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "获取合同列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取合同列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取合同信息
     */
    @ApiOperation( value = "获取合同信息", notes = "获取合同信息" )
    @ResponseBody
    @RequestMapping( value = "/contractInfo", method = RequestMethod.POST )
    public ServerResponse contractInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "合同ID", required = true ) @RequestParam Integer id ) {
	PurchaseContract contract = null;
	try {
	    contract = contractService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取合同信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取合同信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), contract );
    }

    /**
     * 保存或修改合同信息
     */
    @ApiOperation( value = "保存合同信息", notes = "保存合同信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存合同信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    PurchaseContract contract = new PurchaseContract();
	    contract.setBusId( busUser.getId() );
	    contract.setContractContent( CommonUtil.urlEncode( params.get( "contractContent" ).toString() ) );
	    contract.setContractTitle( CommonUtil.urlEncode( params.get( "contractTitle" ).toString() ) );
	    if ( params.get( "id" ) != null && CommonUtil.isNotEmpty( params.get( "id" ).toString() ) ) {
		contract.setId( Integer.parseInt( params.get( "id" ).toString() ) );
		contractService.updateById( contract );
	    } else {
		contract.setCreateDate( new Date() );
		contractService.insert( contract );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存合同信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存合同信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除合同信息
     */
    @ApiOperation( value = "删除合同信息", notes = "删除合同信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除合同信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "合同Id", required = true ) @RequestParam Integer id ) {
	try {
	    contractService.deleteById( id );
	} catch ( BusinessException e ) {
	    logger.error( "删除合同信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除合同信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除合同信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 查询所有的合同
     */
    @ApiOperation( value = "查询所有的合同", notes = "查询所有的合同" )
    @ResponseBody
    @RequestMapping( value = "/contractList", method = RequestMethod.POST )
    public ServerResponse contractList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map< String,Object > > contractList = null;
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    contractList = contractDAO.findAllList( busUser.getId() );
	} catch ( Exception e ) {
	    logger.error( "查询所有的合同异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询所有的合同异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), contractList );
    }
}
