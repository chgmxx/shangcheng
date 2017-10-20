package com.gt.mall.controller.api.presale;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.presale.MallPresaleGive;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-28
 */
@Api( value = "mallPresale", description = "预售管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallPresale" )
public class MallPresaleNewController extends BaseController {

    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallPresaleService        mallPresaleService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private MallPaySetService         mallPaySetService;
    @Autowired
    private DictService               dictService;
    @Autowired
    private BusUserService            busUserService;

    @ApiOperation( value = "预售列表(分页)", notes = "预售列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    //通过商品id查询预售信息
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    boolean isOpenPresale = false;
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsPresale() ) ) {
		    if ( set.getIsPresale() == 1 ) {
			isOpenPresale = true;
		    }
		}
	    }
	    result.put( "isOpenPresale", isOpenPresale );
	    if ( isOpenPresale ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "curPage", curPage );
		params.put( "type", type );
		params.put( "shopId", shopId );
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    if ( CommonUtil.isEmpty( shopId ) ) {
			params.put( "shoplist", shoplist );
		    }
		    PageUtil page = mallPresaleService.selectPresaleByShopId( params, user.getId(), shoplist );
		    result.put( "page", page );
		}
		result.put( "videourl", busUserService.getVoiceUrl( "83" ) );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取预售列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取预售列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取预售信息
     */
    @ApiOperation( value = "获取预售信息", notes = "获取预售信息" )
    @ResponseBody
    @RequestMapping( value = "/presaleInfo", method = RequestMethod.POST )
    public ServerResponse presaleInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "预售ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > presaleMap = null;
	try {
	    // 根据预售id查询预售信息
	    presaleMap = mallPresaleService.selectPresaleById( id );
	} catch ( Exception e ) {
	    logger.error( "获取预售信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取预售信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), presaleMap );
    }

    /**
     * 保存或修改预售信息
     */
    @ApiOperation( value = "保存预售信息", notes = "保存预售信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存预售信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	   /* presale:{"id":"","productId":"26771","isDeposit":1,"depositPercent":"0.1","saleStartTime":"2017-09-28 10:05:17","saleEndTime":"2017-10-07 10:05:17","orderNum":"1","shopId":"177"}
	      presaleTimes:[{"startTime":"2017-09-28 00:00:00","endTime":"2017-10-06 00:00:00","saleType":"1","price":"1","priceType":"1"}]
	    */
	    int code = -1;// 编辑成功
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPresaleService.newEditPresale( params, userId );// 编辑商品
	    }

	    if ( code == -2 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "正在进行预售的信息不能修改" );
	    } else if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存预售失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存预售信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存预售信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除预售信息
     */
    @ApiOperation( value = "删除预售信息", notes = "删除预售信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除预售信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "预售Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效", required = true ) @RequestParam Integer type ) {
	try {

	    MallPresale presale = new MallPresale();
	    presale.setId( id );
	    if ( CommonUtil.isNotEmpty( type ) ) {
		if ( type == -1 ) {// 删除
		    presale.setIsDelete( 1 );
		} else if ( type == -2 ) {// 使失效预售
		    presale.setIsUse( -1 );
		}
	    }
	    boolean flag = mallPresaleService.deletePresale( presale );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), type == -1 ? "删除" : "失效" + "预售记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除预售信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除预售信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除预售信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************************拍卖定金管理*******************************************/

    @ApiOperation( value = "拍卖定金管理列表(分页)", notes = "拍卖定金管理列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/deposit/list", method = RequestMethod.POST )
    public ServerResponse depositList( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = mallPresaleDepositService.selectPresaleByShopId( params, shoplist );
		result.put( "page", page );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取拍卖定金管理列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取拍卖定金管理列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取定金信息
     */
    @ApiOperation( value = "获取定金信息", notes = "获取定金信息" )
    @ResponseBody
    @RequestMapping( value = "/deposit/depositInfo", method = RequestMethod.POST )
    public ServerResponse depositInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "depositId", value = "定金ID", required = true ) @RequestParam Integer depositId ) {
	MallPresaleDeposit deposit = null;
	try {
	    deposit = mallPresaleDepositService.selectByDeposit( depositId );
	} catch ( Exception e ) {
	    logger.error( "获取定金信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取定金信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), deposit );
    }

    /**
     * 退保证金
     */
    @ApiOperation( value = "退保证金", notes = "退保证金" )
    @ResponseBody
    @SysLogAnnotation( description = "退保证金", op_function = "2" )
    @RequestMapping( value = "/deposit/agreedReturnDeposit", method = RequestMethod.POST )
    public ServerResponse agreedReturnDeposit( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "depositId", value = "定金ID", required = true ) @RequestParam Integer depositId ) {
	try {

	    Map< String,Object > result = new HashMap<>();
	    MallPresaleDeposit deposit = mallPresaleDepositService.selectByDeposit( depositId );

	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", deposit.getId() );
	    map.put( "user_id", deposit.getUserId() );
	    map.put( "pay_way", deposit.getPayWay() );
	    map.put( "deposit_money", deposit.getDepositMoney() );
	    map.put( "deposit_no", deposit.getDepositNo() );
	    result = mallPresaleDepositService.returnEndPresale( map );
	    if ( !result.containsKey( "result" ) ) {
		result.put( "result", true );
	    }
	    boolean flag = (boolean) result.get( "result" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退保证金异常" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "退保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "退保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************************预售送礼设置*******************************************/

    /**
     * 获取预售送礼设置信息
     */
    @ApiOperation( value = "获取预售送礼设置信息", notes = "获取预售送礼设置信息" )
    @ResponseBody
    @RequestMapping( value = "/give/giveInfo", method = RequestMethod.POST )
    public ServerResponse giveInfo( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< MallPresaleGive > giveList = mallPresaleService.selectGiveByUserId( user );
	    List< Map > list = dictService.getDict( "1143" );
	    result.put( "dictList", list );
	    result.put( "giveList", giveList );

	} catch ( Exception e ) {
	    logger.error( "获取预售送礼设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取预售送礼设置异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存或修改预售送礼设置
     */
    @ApiOperation( value = "保存预售送礼设置", notes = "保存预售送礼设置" )
    @ResponseBody
    @SysLogAnnotation( description = "保存预售送礼设置", op_function = "2" )
    @RequestMapping( value = "/give/save", method = RequestMethod.POST )
    public ServerResponse setSave( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	    int code = -1;// 编辑成功
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPresaleService.newEditPresaleSet( params, userId );// 编辑商品
	    }
	    if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存预售送礼设置失败" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存预售送礼设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存预售送礼设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
