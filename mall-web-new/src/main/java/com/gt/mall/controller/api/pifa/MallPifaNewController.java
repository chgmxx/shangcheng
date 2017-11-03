package com.gt.mall.controller.api.pifa;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
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
 * 商品批发管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-29
 */
@Api( value = "mallWholesale", description = "批发管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallWholesale/E9lM9uM4ct" )
public class MallPifaNewController extends BaseController {

    @Autowired
    private MallPifaService      mallPifaService;
    @Autowired
    private MallPifaApplyService mallPifaApplyService;
    @Autowired
    private MallStoreService     mallStoreService;
    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private BusUserService       busUserService;
    @Autowired
    private MallOrderService     mallOrderService;

    @ApiOperation( value = "批发列表(分页)", notes = "批发列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    boolean isOpenPifa = false;
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsPresale() ) ) {
		    if ( set.getIsPf() == 1 ) {
			isOpenPifa = true;
		    }
		}
	    }
	    result.put( "isOpenPifa", true );
	    if ( isOpenPifa ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "curPage", curPage );
		params.put( "type", type );
		params.put( "shopId", shopId );
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    if ( CommonUtil.isEmpty( shopId ) ) {
			params.put( "shoplist", shoplist );
		    }
		    PageUtil page = mallPifaService.pifaProductList( params, shoplist );
		    result.put( "page", page );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取批发列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取批发列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取批发信息
     */
    @ApiOperation( value = "获取批发信息", notes = "获取批发信息" )
    @ResponseBody
    @RequestMapping( value = "/pifaInfo", method = RequestMethod.POST )
    public ServerResponse pifaInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "批发ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > groupMap = null;
	try {
	    // 根据批发id查询批发信息
	    groupMap = mallPifaService.selectPifaById( id );
	} catch ( Exception e ) {
	    logger.error( "获取批发信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取批发信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), groupMap );
    }

    /**
     * 保存或修改批发信息
     */
    @ApiOperation( value = "保存批发信息", notes = "保存批发信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存批发信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int code = -1;// 编辑成功
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPifaService.editPifa( params, userId );// 编辑商品
	    }
	    if ( code == -2 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "正在进行批发的商品不能修改" );
	    } else if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存批发失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存批发信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存批发信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除批发信息
     */
    @ApiOperation( value = "删除批发信息", notes = "删除批发信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除批发信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "批发Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效", required = true ) @RequestParam Integer type ) {
	try {
	    MallPifa pifa = new MallPifa();
	    pifa.setId( id );
	    if ( type == -1 ) {// 删除
		pifa.setIsDelete( 1 );
	    } else if ( type == -2 ) {// 使失效批发
		pifa.setIsUse( -1 );
	    }

	    boolean flag = mallPifaService.deletePifa( pifa );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), type == -1 ? "删除" : "失效" + "批发记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除批发信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除批发信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除批发信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /*****************************批发商管理****************************************/

    @ApiOperation( value = "批发商列表(分页)", notes = "批发商列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "keyword", value = "关键词", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/wholesalers/list", method = RequestMethod.POST )
    public ServerResponse wholesalersList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String keyword ) {
	Map< String,Object > result = new HashMap<>();
	try {

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "keyword", keyword );
	    params.put( "userId", user.getId() );
	    PageUtil page = mallPifaService.wholesalerList( params );
	    result.put( "page", page );
	    result.put( "videourl", busUserService.getVoiceUrl( "84" ) );

	} catch ( Exception e ) {
	    logger.error( "获取批发商列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取批发商列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 设置批发商审核通不通过、启不启用
     */
    @ApiOperation( value = "设置批发商审核通不通过、启不启用", notes = "设置批发商审核通不通过、启不启用" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "ids", value = "批发Id", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "status", value = "审核  1通过 -1不通过", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "isUse", value = "是否启用 1启用 -1禁用", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @SysLogAnnotation( description = "设置批发商审核通不通过、启不启用", op_function = "4" )
    @RequestMapping( value = "/wholesalers/updateStatus", method = RequestMethod.POST )
    public ServerResponse updateStatus( HttpServletRequest request, HttpServletResponse response, String ids, Integer status, Integer isUse ) {
	try {
	    if ( !CommonUtil.isEmpty( ids ) ) {
		String[] str = ( ids.toString() ).split( "," );
		for ( int i = 0; i < str.length; i++ ) {
		    MallPifaApply pifaApply = new MallPifaApply();
		    pifaApply.setId( CommonUtil.toInteger( str[i] ) );
		    if ( CommonUtil.isNotEmpty( status ) ) {
			pifaApply.setCheckTime( new Date() );
			pifaApply.setStatus( status );
		    } else {
			pifaApply.setIsUse( isUse );
		    }
		    mallPifaApplyService.updateById( pifaApply );
		}
	    }
	} catch ( BusinessException e ) {
	    logger.error( "设置批发商审核、启用异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "设置批发商审核、启用异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "设置批发商审核、启用异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 同步订单成交数
     */
    @ApiOperation( value = "同步订单成交数", notes = "同步订单成交数" )
    @ResponseBody
    @SysLogAnnotation( description = "同步订单成交数", op_function = "4" )
    @RequestMapping( value = "/wholesalers/syncOrderPifa", method = RequestMethod.POST )
    public ServerResponse syncOrderPifa( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    Map< String,Object > map = mallOrderService.syncOrderbyPifa( params );
	    boolean flag = (boolean) map.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "同步订单成交数异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "同步订单成交数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "同步订单成交数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "同步订单成交数异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************************批发设置********************************/

    /**
     * 批发设置
     */
    @ApiOperation( value = "获取批发设置", notes = "获取批发设置" )
    @ResponseBody
    @RequestMapping( value = "/setWholesale", method = RequestMethod.POST )
    public ServerResponse setWholesale( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    MallPaySet paySet = mallPaySetService.selectByUserId( set );
	    result.put( "paySet", paySet );
	    if ( CommonUtil.isNotEmpty( paySet ) ) {
		if ( CommonUtil.isNotEmpty( paySet.getPfSet() ) ) {
		    JSONObject obj = JSONObject.fromObject( paySet.getPfSet() );
		    result.put( "pfSet", obj );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取批发设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取批发设置异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存或修改批发信息
     */
    @ApiOperation( value = "保存批发设置", notes = "保存批发设置" )
    @ResponseBody
    @SysLogAnnotation( description = "保存批发设置", op_function = "2" )
    @RequestMapping( value = "/saveSet", method = RequestMethod.POST )
    public ServerResponse saveSetWholesaler( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallPaySet mallPaySet = (MallPaySet) JSONObject.toBean( JSONObject.fromObject( params ), MallPaySet.class );
	    mallPaySet.setPfRemark( CommonUtil.urlEncode( mallPaySet.getPfRemark() ) );
	    mallPaySet.setPfApplyRemark( CommonUtil.urlEncode( mallPaySet.getPfApplyRemark() ) );
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    MallPaySet paySet = mallPaySetService.selectByUserId( set );//查询登录者是否有设置商城
	    int count = 0;
	    if ( CommonUtil.isEmpty( paySet ) ) {//如果没有设置商城,则添加批发商设置,有就修改
		mallPaySet.setUserId( user.getId() );
		mallPaySet.setCreateTime( new Date() );
		boolean flag = mallPaySetService.insert( mallPaySet );//添加批发商城设置
		if ( flag ) {
		    count = 1;
		}
	    } else {
		params.put( "userId", user.getId() );
		params.put( "pfRemark", CommonUtil.urlEncode( params.get( "pfRemark" ).toString() ) );
		params.put( "pfApplyRemark", CommonUtil.urlEncode( params.get( "pfApplyRemark" ).toString() ) );
		count = mallPifaService.updateSetWholesaler( params );//修改批发商城设置
	    }

	    if ( count <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存批发设置失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存批发设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存批发设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
