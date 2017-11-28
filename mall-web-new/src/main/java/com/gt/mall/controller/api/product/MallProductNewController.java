package com.gt.mall.controller.api.product;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.result.product.ProductResult;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 商品分组前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-20
 */
@Api( value = "mallProduct", description = "商品管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallProduct/E9lM9uM4ct" )
public class MallProductNewController extends BaseController {

    @Autowired
    private MallGroupService            mallGroupService;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private CardService                 cardService;
    @Autowired
    private MallFreightService          mallFreightService;
    @Autowired
    private DictService                 dictService;

    @ApiOperation( value = "商品列表(分页)", notes = "商品列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "proType", value = "0全部  1上架商品   2未上架  3审核不通过", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "proName", value = "商品名称", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer proType, String proName, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //isPublish  checkStatus proName  proType(页面标识)  type(传1 售罄商品)
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "proName", proName );
	    params.put( "shopId", shopId );
	    if ( proType == 1 ) {//上架
		params.put( "isPublish", "1" );
		params.put( "checkStatus", "1" );
	    } else if ( proType == 2 ) {//未上架
		params.put( "isPublish", "0" );
	    } else if ( proType == 3 ) {//审核不通过
		params.put( "checkStatus", "-1" );
	    }

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    int userPId = MallSessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有(从session获取)
	    params.put( "isJxc", isJxc );
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = mallProductService.selectByUserId( params, shoplist );
		result.put( "page", page );
	    }
	    result.put( "proMaxNum", dictService.getDiBserNum( userPId, 5, "1094" ) );//可新增商品总数

	    if ( isJxc == 1 ) {
		mallProductService.syncErpPro( user.getId(), request );//把未同步的商品进行同步
	    }

	    result.put( "videourl", busUserService.getVoiceUrl( "77" ) );
	} catch ( Exception e ) {
	    logger.error( "商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "获取各状态下商品总数", notes = "获取各状态下商品总数" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/countStatus", method = RequestMethod.POST )
    public ServerResponse countStatus( HttpServletRequest request, HttpServletResponse response, Integer shopId ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	Map< String,Object > result = new HashMap<>();
	try {
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    if ( CommonUtil.isEmpty( shopId ) ) {
		params.put( "shoplist", shoplist );
	    }
	    //全部商品
	    Integer status_total = mallProductService.selectCountByUserId( params );
	    //已上架商品  （审核成功，上架）
	    params.put( "isPublish", "1" );
	    params.put( "checkStatus", "1" );
	    Integer status1 = mallProductService.selectCountByUserId( params );
	    //未上架商品
	    params.remove( "checkStatus" );
	    params.put( "isPublish", "0" );
	    Integer status2 = mallProductService.selectCountByUserId( params );
	    //审核不通过
	    params.remove( "isPublish" );
	    params.put( "checkStatus", "-1" );
	    Integer status3 = mallProductService.selectCountByUserId( params );

	    result.put( "status_total", status_total );
	    result.put( "status1", status1 );
	    result.put( "status2", status2 );
	    result.put( "status3", status3 );
	} catch ( Exception e )

	{
	    logger.error( "获取各状态下商品总数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取各状态下商品总数异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 批量处理商品(删除，送审，上架，下架)
     */
    @ApiOperation( value = "批量处理商品（删除，送审，上架，下架）", notes = "批量处理商品（删除，送审，上架，下架）" )
    @ResponseBody
    @SysLogAnnotation( description = "商品管理-商品列表：批量处理商品（删除，送审，上架，下架）", op_function = "3" )
    @RequestMapping( value = "/batchProduct", method = RequestMethod.POST )
    public ServerResponse batchProduct( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "商品Id集合，用逗号隔开", required = true ) @RequestParam( "ids" ) String ids,
		    @ApiParam( name = "type", value = "类型 1删除 2送审 3上架 4下架", required = true ) @RequestParam( "type" ) Integer type ) {

	logger.info( "进入批量处理controller" );
	response.setCharacterEncoding( "utf-8" );

	try {
	   /* String[] id = (String[]) net.sf.json.JSONArray.toArray( net.sf.json.JSONArray.fromObject( ids ), String.class );*/
	    Map< String,Object > params = new HashMap<>();
	    if ( type == 1 ) {//删除
		params.put( "isDelete", 1 );
	    } else if ( type == 2 ) {//送审
		params.put( "checkStatus", 0 );
	    } else if ( type == 3 ) {//上架
		params.put( "isPublish", 1 );
	    } else if ( type == 4 ) {//下架
		params.put( "isPublish", -1 );
	    }
	    String[] id = ids.split( "," );
	    boolean flag = mallProductService.batchUpdateProduct( params, id );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "操作失败！" );
	    }
	} catch ( Exception e ) {
	    logger.error( "批量处理商品信息：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取商品信息
     */
    @ApiOperation( value = "获取商品信息", notes = "获取商品信息" )
    @ResponseBody
    @RequestMapping( value = "/productInfo", method = RequestMethod.POST )
    public ServerResponse productInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "商品Id", required = true ) @RequestParam Integer id ) {
	MallProduct product = null;
	try {
	    product = mallProductService.selectById( id );
	    if ( product != null && product.getIsDelete() > 0 ) {
		product = null;
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商品分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), product );
    }

    /**
     * 获取编辑商品页面数据
     */
    @ApiOperation( value = "进入编辑商品", notes = "进入编辑商品" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "商品Id", paramType = "query", required = true, dataType = "int" ) )
    @RequestMapping( value = "/to_edit", method = RequestMethod.POST )
    public ServerResponse to_edit( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );

	    int userPId = MallSessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
	    int isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	    if ( isJxc == 1 ) {
		result.put( "noShowSt", 1 );//不显示实体物品
		result.put( "noUpSpec", 1 );//不修改规格 1 修改 0 不修改
	    }
	    result.put( "isJxc", isJxc );//是否有进销存 0没有 1有

	    // 查询商品信息
	    if ( CommonUtil.isNotEmpty( id ) ) {
		Map< String,Object > map = mallProductService.selectProductById( id, user, isJxc );
		if ( map != null && map.size() > 0 ) {
		    for ( Map.Entry< String,Object > e : map.entrySet() ) {
			result.put( e.getKey(), e.getValue() );
		    }
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "进入编辑商品功能异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入编辑商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 新增商品
     */
    @ApiOperation( value = "新增商品信息", notes = "新增商品信息" )
    @ResponseBody
    @SysLogAnnotation( description = "新增商品", op_function = "2" )
    @RequestMapping( value = "/add", method = RequestMethod.POST )
    public ServerResponse add( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int code = 1;
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > resultMap = mallProductService.addProduct( params, user, request );
	    if ( CommonUtil.isNotEmpty( resultMap.get( "code" ) ) ) {
		code = CommonUtil.toInteger( resultMap.get( "code" ) );
	    }
	    if ( code != 1 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存商品失败" );
	    }
	} catch ( Exception e ) {
	    logger.error( "新增商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 修改商品
     */
    @ApiOperation( value = "修改商品信息", notes = "修改商品信息" )
    @ResponseBody
    @SysLogAnnotation( description = "修改商品", op_function = "2" )
    @RequestMapping( value = "/updete", method = RequestMethod.POST )
    public ServerResponse updete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    boolean flag = mallProductService.newUpdateProduct( params, user, request );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改商品失败" );
	    }
	} catch ( Exception e ) {
	    logger.error( "修改商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
