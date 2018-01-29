package com.gt.mall.controller.api.seckill;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
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
 * 秒杀管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-27
 */
@Api( value = "mallSeckill", description = "秒杀管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallSeckill/E9lM9uM4ct" )
public class MallSeckillNewController extends BaseController {

    @Autowired
    private BusUserService     busUserService;
    @Autowired
    private MallProductService mallProductService;
    @Autowired
    private MallStoreService   mallStoreService;
    @Autowired
    private MallSeckillService mallSeckillService;

    @ApiOperation( value = "秒杀列表(分页)", notes = "秒杀列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态  -2已失效 1进行中 -1 未开始  2已结束", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "type", type );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = mallSeckillService.selectSeckillByShopId( params, user.getId(), shoplist );
		result.put( "page", page );
	    }
	    result.put( "videourl", Constants.VIDEO_URL + 82 );
	} catch ( Exception e ) {
	    logger.error( "获取秒杀列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取秒杀列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取秒杀信息
     */
    @ApiOperation( value = "获取秒杀信息", notes = "获取秒杀信息" )
    @ResponseBody
    @RequestMapping( value = "/seckillInfo", method = RequestMethod.POST )
    public ServerResponse seckillInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "秒杀ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > groupMap = null;
	try {
	    groupMap = mallSeckillService.selectSeckillById( id );
	} catch ( Exception e ) {
	    logger.error( "获取秒杀信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取秒杀信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), groupMap );
    }

    /**
     * 保存或修改秒杀信息
     */
    @ApiOperation( value = "保存秒杀信息", notes = "保存秒杀信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存秒杀信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    /*
	    *seckill:{"productId":"273","sNums":"597","id":"","sName":"232","sStartTime":"2017-09-20 00:00:00","sEndTime":"2017-09-28 00:00:00","sMaxBuyNum":"233","sPrice":"1","shopId":"29","sNum":597}
		specArr:[{"seckillPrice":"1","invenId":"789","specificaIds":"147","isJoinGroup":1,"seckillNum":"199"},{"seckillPrice":"2","invenId":"790","specificaIds":"153","isJoinGroup":1,"seckillNum":"199"},
		{"seckillPrice":"4","invenId":"791","specificaIds":"154","isJoinGroup":0,"seckillNum":"199"}]
	    * */
	    int code = -1;// 编辑成功
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( busUser ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallSeckillService.editSeckill( params, busUser, request );// 编辑商品
	    }

	    if ( code == -2 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "正在进行秒杀的商品不能修改" );
	    } else if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存秒杀失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存秒杀信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存秒杀信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
    
    /**
     * 删除秒杀信息
     */
    @ApiOperation( value = "删除秒杀信息", notes = "删除秒杀信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除秒杀信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "秒杀Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效", required = true ) @RequestParam Integer type ) {
	try {

	    MallSeckill seckill = new MallSeckill();
	    seckill.setId( id );
	    if ( type == -1 ) {// 删除
		seckill.setIsDelete( 1 );
	    } else if ( type == -2 ) {// 使失效秒杀
		seckill.setIsUse( -1 );
	    }
	    boolean flag = mallSeckillService.deleteSeckill( seckill );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), type == -1 ? "删除" : "失效" + "秒杀记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除秒杀信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除秒杀信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除秒杀信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
