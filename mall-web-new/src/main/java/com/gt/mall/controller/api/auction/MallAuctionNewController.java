package com.gt.mall.controller.api.auction;

import com.gt.api.bean.session.BusUser;
import com.gt.api.util.KeysUtil;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "mallAuction", description = "拍卖管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallAuction/E9lM9uM4ct" )
public class MallAuctionNewController extends BaseController {

    @Autowired
    private MallAuctionService       auctionService;
    @Autowired
    private MallStoreService         storeService;
    @Autowired
    private MallAuctionMarginService auctionMarginService;
    @Autowired
    private BusUserService           busUserService;

    @ApiOperation( value = "拍卖管理列表(分页)", notes = "拍卖管理列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态 -2已失效 1进行中 -1 未开始  2已结束", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "type", type );
	    params.put( "shopId", shopId );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = auctionService.selectAuctionByShopId( params, shoplist );
		result.put( "page", page );
	    }
	    result.put( "videourl", busUserService.getVoiceUrl( "86" ) );
	} catch ( Exception e ) {
	    logger.error( "拍卖管理列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "拍卖管理列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取拍卖信息
     */
    @ApiOperation( value = "获取拍卖信息", notes = "获取拍卖信息" )
    @ResponseBody
    @RequestMapping( value = "/auctionInfo", method = RequestMethod.POST )
    public ServerResponse auctionInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "拍卖ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > groupMap = null;
	try {
	    groupMap = auctionService.selectAuctionById( id );
	} catch ( Exception e ) {
	    logger.error( "获取拍卖信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取拍卖信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), groupMap );
    }

    /**
     * 保存或修改拍卖信息
     */
    @ApiOperation( value = "保存拍卖信息", notes = "保存拍卖信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存拍卖信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int code = -1;// 编辑成功
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = auctionService.editAuction( params, userId );// 编辑商品
	    }
	    if ( code == -2 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "正在进行拍卖的商品不能修改" );
	    } else if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存拍卖失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存拍卖信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存拍卖信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除拍卖信息
     */
    @ApiOperation( value = "删除拍卖信息", notes = "删除拍卖信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除拍卖信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "拍卖Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效", required = true ) @RequestParam Integer type ) {
	try {
	    MallAuction auction = new MallAuction();
	    auction.setId( id );
	    if ( CommonUtil.isNotEmpty( type ) ) {
		if ( type == -1 ) {// 删除
		    auction.setIsDelete( 1 );
		} else if ( type == -2 ) {// 使失效拍卖
		    auction.setIsUse( -1 );
		}
	    }
	    boolean flag = auctionService.deleteAuction( auction );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), type == -1 ? "删除" : "使失效" + "拍卖记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除拍卖信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除拍卖信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除拍卖信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************************保证金管理************************************************/

    @ApiOperation( value = "保证金列表(分页)", notes = "保证金列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/margin/list", method = RequestMethod.POST )
    public ServerResponse marginList( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = auctionMarginService.selectMarginByShopId( params, user.getId() );
		result.put( "page", page );
	    }
	    result.put( "busId", user.getId() );
	    result.put( "alipayUrl", PropertiesUtil.getHomeUrl() + "alipay/79B4DE7C/refund.do" );
	} catch ( Exception e ) {
	    logger.error( "保证金列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保证金列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取保证金信息
     */
    @ApiOperation( value = "获取保证金信息", notes = "获取保证金信息" )
    @ResponseBody
    @RequestMapping( value = "/margin/marginInfo", method = RequestMethod.POST )
    public ServerResponse marginInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "保证金ID", required = true ) @RequestParam Integer id ) {
	MallAuctionMargin margin = null;
	try {
	    margin = auctionMarginService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取保证金信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取保证金信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), margin );
    }

    /**
     * 获取支付宝退款地址
     */
    @ApiOperation( value = "获取支付宝退款地址", notes = "获取支付宝退款地址" )
    @ResponseBody
    @RequestMapping( value = "/margin/refundUrl", method = RequestMethod.POST )
    public ServerResponse refundUrl( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "保证金ID", required = true ) @RequestParam Integer id ) {
	String url = "";
	try {
	    MallAuctionMargin margin = auctionMarginService.selectById( id );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    KeysUtil keysUtil = new KeysUtil();
	    String notifyUrl = keysUtil.getEncString( PropertiesUtil.getHomeUrl() + "mallAuction/mallAPI/returnSuccessBack.do" );

	    url = PropertiesUtil.getHomeUrl() + "alipay/79B4DE7C/refund.do?out_trade_no=" + margin.getAucNo() + "&busId=" + user.getId() + "&desc=退保证金&fee=" + margin
			    .getMarginMoney() + "&notifyUrl=" + notifyUrl;

	} catch ( Exception e ) {
	    logger.error( "获取支付宝退款地址异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取支付宝退款地址异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), url, false );
    }

    /**
     * 退保证金
     */
    @ApiOperation( value = "退保证金", notes = "退保证金" )
    @ResponseBody
    @RequestMapping( value = "/margin/agreedReturnMargin", method = RequestMethod.POST )
    public ServerResponse agreedReturnMargin( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "保证金ID", required = true ) @RequestParam Integer id ) {
	try {
	    MallAuctionMargin margin = auctionMarginService.selectById( id );

	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", margin.getId() );
	    map.put( "user_id", margin.getUserId() );
	    map.put( "pay_way", margin.getPayWay() );
	    map.put( "margin_money", margin.getMarginMoney() );
	    map.put( "auc_no", margin.getAucNo() );
	    Map< String,Object > result = auctionMarginService.returnEndMargin( map );

	    Boolean flag = (Boolean) result.get( "result" );
	    if ( CommonUtil.isEmpty( flag ) ) {
		flag = true;
	    }
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退保证金异常" );
	    }
	} catch ( BusinessException be ) {
	    return ServerResponse.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "退保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退保证金异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
