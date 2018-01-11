package com.gt.mall.controller.api;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.QRcodeKit;
import com.gt.util.entity.param.fenbiFlow.BusFlow;
import com.gt.util.entity.param.pay.PayWay;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城概况前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-18
 */
@Api( value = "mall", description = "商城概况", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mall/E9lM9uM4ct" )
public class MallIndexController extends BaseController {

    @Autowired
    private MallOrderService       mallOrderService;
    @Autowired
    private MallOrderReturnService mallOrderReturnService;
    @Autowired
    private MallStoreService       mallStoreService;
    @Autowired
    private WxShopService          wxShopService;
    @Autowired
    private CardService            cardService;
    @Autowired
    private MallProductService     mallProductService;
    @Autowired
    private PayService             payService;

    @ApiOperation( value = "获取商城的统计概况", notes = "获取商城的统计概况" )
    @ResponseBody
    @RequestMapping( value = "/count", method = RequestMethod.POST )
    public ServerResponse count( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	Map< String,Object > result = new HashMap<>();
	//待发货订单数量
	int unfilled_orders_num = 0;
	//维权订单数量
	int bad_orders_num = 0;
	//昨日订单数量
	int yesterday_orders_num = 0;
	//商家当前可提现金额
	int withdraw_money = 0;
	try {
	    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "userId", user.getId() );
	    params.put( "shoplist", shopList );
	    params.put( "status", "2" );
	    unfilled_orders_num = mallOrderService.count( params );
	    params.put( "status", "6" );
	    bad_orders_num = mallOrderService.count( params );

	    Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
	    calendar.add( Calendar.DATE, -1 );    //得到前一天
	    String yestedayDate = new SimpleDateFormat( "yyyy-MM-dd" ).format( calendar.getTime() );

	    params.put( "status", "0" );
	    params.put( "startTime", yestedayDate );
	    params.put( "endTime", mallOrderService + "23:59:59" );
	    yesterday_orders_num = mallOrderService.count( params );

	    result.put( "unfilled_orders_num", unfilled_orders_num );
	    result.put( "bad_orders_num", bad_orders_num );
	    result.put( "yesterday_orders_num", yesterday_orders_num );
	    result.put( "withdraw_money", withdraw_money );
	    result.put( "domain", PropertiesUtil.getWxmpDomain() );
	} catch ( Exception e ) {
	    logger.error( "商城店铺管理异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商城的统计概况异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "判断是否是管理员", notes = "判断是否是管理员" )
    @ResponseBody
    @RequestMapping( value = "/isAdminUser", method = RequestMethod.POST )
    public ServerResponse isAdminUser( HttpServletRequest request, HttpServletResponse response ) {
	boolean isAdminFlag = false;
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//是管理员
	} catch ( Exception e ) {
	    logger.error( "判断是否是管理员异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "判断是否是管理员异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), isAdminFlag, false );
    }

    @ApiOperation( value = "获取商城营销地址", notes = "获取商城营销地址" )
    @ResponseBody
    @RequestMapping( value = "/getMarketingUrl", method = RequestMethod.POST )
    public ServerResponse getMarketingUrl( HttpServletRequest request, HttpServletResponse response ) {
	String url = "";
	try {
	    url = PropertiesUtil.getMarketingUrl();
	} catch ( Exception e ) {
	    logger.error( "获取商城营销的地址异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商城营销的地址异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), url, false );
    }

    @ApiOperation( value = "获取素材库地址", notes = "获取素材库地址" )
    @ResponseBody
    @RequestMapping( value = "/getMaterialUrl", method = RequestMethod.POST )
    public ServerResponse getMaterialUrl( HttpServletRequest request, HttpServletResponse response ) {
	String url = "";
	try {
	    url = PropertiesUtil.getMaterialUrl();
	} catch ( Exception e ) {
	    logger.error( "获取素材库地址异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取素材库地址异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), url, false );
    }

    /**
     * 判断商家是否有商户支付平台
     */
    @ApiOperation( value = "判断商家是否有商户支付平台", notes = "判断商家是否有商户支付平台" )
    @ResponseBody
    @RequestMapping( value = "/isWxPayUser", method = RequestMethod.POST )
    public ServerResponse isWxPayUser( HttpServletRequest request, HttpServletResponse response ) {
	Boolean flag = false;
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    PayWay payWay = payService.getPayWay( user.getId() );
	    System.out.print( payWay.getWxpay() );
	    if ( payWay != null && payWay.getWxpay() == 0 && payWay.getAlipay() == 0 ) {
		flag = true;
	    } else {
		flag = false;
	    }
	} catch ( Exception e ) {
	    logger.error( "判断商家是否有商户支付平台异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flag );
    }

    @ApiOperation( value = "获取门店列表", notes = "获取门店列表" )
    @ResponseBody
    @RequestMapping( value = "/shopList", method = RequestMethod.POST )
    public ServerResponse shopList( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	List< WsWxShopInfoExtend > shopList = null;
	try {
	    shopList = wxShopService.queryWxShopByBusId( user.getId() );
	} catch ( Exception e ) {
	    logger.error( "获取门店列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取门店列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), shopList );
    }

    @ApiOperation( value = "获取门店信息", notes = "获取门店信息" )
    @ResponseBody
    @RequestMapping( value = "/shopInfo", method = RequestMethod.POST )
    public ServerResponse shopInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "wxShopId", value = "门店ID", required = true ) @RequestParam Integer wxShopId ) {
	WsWxShopInfoExtend wxShopInfo = null;
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< WsWxShopInfoExtend > shopList = wxShopService.queryWxShopByBusId( user.getId() );
	    for ( WsWxShopInfoExtend info : shopList ) {
		if ( info.getId() == wxShopId ) {
		    wxShopInfo = info;
		    break;
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取门店信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取门店信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), wxShopInfo );
    }

    @ApiOperation( value = "获取主门店信息", notes = "获取主门店信息" )
    @ResponseBody
    @RequestMapping( value = "/selectMainShop", method = RequestMethod.POST )
    public ServerResponse selectMainShop( HttpServletRequest request, HttpServletResponse response ) {
	WsWxShopInfo wxShopInfo = null;
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    wxShopInfo = wxShopService.selectMainShopByBusId( user.getId() );
	} catch ( Exception e ) {
	    logger.error( "获取主门店信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取主门店信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), wxShopInfo );
    }

    /**
     * 下载提取链接二维码
     *
     * @param request
     * @param response
     * @param url
     */
    @ApiOperation( value = "下载提取链接二维码", notes = "下载提取链接二维码" )
    @ResponseBody
    @RequestMapping( value = "/downQr", method = RequestMethod.GET )
    public void downQr( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "url", value = "地址", required = true ) @RequestParam String url ) {
	try {
	    String filename = "二维码.jpg";
	    response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	    response.setContentType( "application/octet-stream" );
	    QRcodeKit.buildQRcode( url, 450, 450, response );
	} catch ( UnsupportedEncodingException e ) {
	    logger.error( "下载提取链接二维码异常：" + e.getMessage() );
	    e.printStackTrace();

	}
    }

    @ApiOperation( value = "获取卡券包列表", notes = "获取卡券包列表" )
    @ResponseBody
    @RequestMapping( value = "/cardReceiveList", method = RequestMethod.POST )
    public ServerResponse cardReceiveList( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	List< Map > cardReceiveList = null;
	try {
	    cardReceiveList = cardService.findReceiveByBusUserId( user.getId() );
	    if ( cardReceiveList != null && cardReceiveList.size() > 0 ) {
		for ( Map map2 : cardReceiveList ) {

		    if ( CommonUtil.isNotEmpty( map2.get( "cardMessage" ) ) ) {
			List< Map > messageList = com.alibaba.fastjson.JSONArray.parseArray( map2.get( "cardMessage" ).toString(), Map.class );
			map2.put( "messageList", messageList );
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取卡券包列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取卡券包列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), cardReceiveList );
    }

    @ApiOperation( value = "获取商家流量列表", notes = "获取商家流量列表" )
    @ResponseBody
    @RequestMapping( value = "/flowList", method = RequestMethod.POST )
    public ServerResponse flowList( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	List< BusFlow > flowList = null;
	try {
	    flowList = mallProductService.selectCountByFlowIds( user.getId() );
	} catch ( Exception e ) {
	    logger.error( "获取商家流量列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商家流量列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flowList );
    }

    @ApiOperation( value = "获取会员卡列表", notes = "获取会员卡列表" )
    @ResponseBody
    @RequestMapping( value = "/cardList", method = RequestMethod.POST )
    public ServerResponse cardList( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	List< Map > cardList = null;
	try {
	    cardList = mallProductService.selectMemberType( user.getId() );
	} catch ( Exception e ) {
	    logger.error( "获取会员卡列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取会员卡列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), cardList );
    }

    @ApiOperation( value = "获取地区列表", notes = "获取地区列表" )
    @ResponseBody
    @RequestMapping( value = "/getArea", method = RequestMethod.POST )
    @ApiImplicitParams( @ApiImplicitParam( name = "pid", value = "上级地区ID", paramType = "query", required = false, dataType = "int" ) )
    public ServerResponse getArea( HttpServletRequest request, HttpServletResponse response, Integer pid ) {
	List< Map > areaList = null;
	try {
	    if ( CommonUtil.isNotEmpty( pid ) ) {
		areaList = wxShopService.queryCityByParentId( pid );
	    } else {
		areaList = wxShopService.queryCityByLevel( 2 );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取地区列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取地区列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), areaList );
    }
}
