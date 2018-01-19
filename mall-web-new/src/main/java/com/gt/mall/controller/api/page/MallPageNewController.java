package com.gt.mall.controller.api.page;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面管理前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-19
 */
@Api( value = "mallPage", description = "页面管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallPage/E9lM9uM4ct" )
public class MallPageNewController extends BaseController {

    @Autowired
    private MallPageService  mallPageService;
    @Autowired
    private MallProductDAO   mallProductDAO;
    @Autowired
    private DictService      dictService;
    @Autowired
    private BusUserService   busUserService;
    @Autowired
    private MallStoreService mallStoreService;

    @ApiOperation( value = "商家的页面列表(分页)", notes = "商家的页面列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "curPage", curPage );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    PageUtil page = mallPageService.findByPage( params, user, request );

	    result.put( "page", page );
	    result.put( "videourl", Constants.VIDEO_URL + 78 );

	} catch ( Exception e ) {
	    logger.error( "获取商家的页面列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商家的页面列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取页面信息
     */
    @ApiOperation( value = "获取页面信息", notes = "获取页面信息" )
    @ResponseBody
    @RequestMapping( value = "/pageInfo", method = RequestMethod.POST )
    public ServerResponse pageInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "页面ID", required = true ) @RequestParam Integer id ) {
	MallPage page = null;
	try {
	    page = mallPageService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), page );
    }

    /**
     * 获取页面类型
     */
    @ApiOperation( value = "获取页面类型", notes = "获取页面类型" )
    @ResponseBody
    @RequestMapping( value = "/typeMap", method = RequestMethod.POST )
    public ServerResponse typeMap( HttpServletRequest request, HttpServletResponse response ) {
	List< DictBean > typeMap = null;
	try {
	    //获取页面类型
	    typeMap = dictService.getDict( "1073" );
	} catch ( Exception e ) {
	    logger.error( "获取页面类型异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面类型异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), typeMap );
    }

    /**
     * 保存或修改页面信息
     */
    @ApiOperation( value = "保存页面信息", notes = "保存页面信息" )
    @ResponseBody
    @SysLogAnnotation( description = "页面管理-保存页面信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallPage page = com.alibaba.fastjson.JSONObject.parseObject( params.get( "page" ).toString(), MallPage.class );

	    if ( CommonUtil.isEmpty( page.getPagStoId() ) ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "所属店铺不能为空" );
	    }
	    if ( CommonUtil.isEmpty( page.getPagName() ) ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "页面名称不能为空" );
	    }
	    if ( CommonUtil.isEmpty( page.getPagTypeId() ) ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "页面分类不能为空" );
	    }
	    page.setPagUserId( MallSessionUtils.getLoginUser( request ).getId() );
	    page.setPagCreateTime( new Date() );
	    mallPageService.saveOrUpdate( page, user );
	    result.put( "id", page.getId() );
	} catch ( BusinessException e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 删除页面信息
     */
    @ApiOperation( value = "删除页面", notes = "删除页面" )
    @ResponseBody
    @SysLogAnnotation( description = "页面管理-删除页面信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "页面ID集合,用逗号隔开", required = true ) @RequestParam String ids ) throws IOException {
	try {
	    String id[] = ids.toString().split( "," );
	    Map< String,Object > result = mallPageService.delete( id );
	    boolean flag = (boolean) result.get( "result" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "message" ).toString() );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除页面信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 弹出该店铺预售信息
     */
    @ApiOperation( value = "根据店铺id查询页面id", notes = "根据店铺id查询页面id" )
    @ApiImplicitParams( @ApiImplicitParam( name = "shopId", value = "店铺id", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/getPageIdByShopId", method = RequestMethod.GET )
    public ServerResponse< Integer > getPageIdByShopId( HttpServletRequest request, HttpServletResponse response, Integer shopId ) {
	try {
	    //获取店页面id
	    int pageId = mallPageService.getPageIdByShopId( shopId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), pageId );

	} catch ( Exception e ) {
	    logger.error( "获取页面链接异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面链接异常" );
	}

    }
}
