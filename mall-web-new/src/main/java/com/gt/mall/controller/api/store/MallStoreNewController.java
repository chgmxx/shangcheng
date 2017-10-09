package com.gt.mall.controller.api.store;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺管理前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Api( value = "mallStore", description = "店铺管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallStore" )
public class MallStoreNewController extends BaseController {

    @Autowired
    private MallStoreService  mallStoreService;
    @Autowired
    private BusUserService    busUserService;
    @Autowired
    private WxShopService     wxShopService;
    @Autowired
    private DictService       dictService;
    @Autowired
    private MallPaySetService mallPaySetService;

    @ApiOperation( value = "商家的店铺列表(分页)", notes = "商家的店铺列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    result.put( "userName", user.getName() );//商家名称
	    result.put( "userLogo", "" );//商家头像
	    int pid = SessionUtils.getAdminUserId( user.getId(), request );//查询总账号id
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "userId", user.getId() );
	    params.put( "pid", pid );
	    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
	    if ( shopList != null && shopList.size() > 0 ) {
		PageUtil page = mallStoreService.findByPage( params, shopList );

		int branchCount = SessionUtils.getWxShopNumBySession( user.getId(), request );
		int store = mallStoreService.countStroe( user.getId() );
		int countnum = 0;//创建店铺多余本身店铺就有问题，返回1 不能添加主店铺，只能添加子店铺
		if ( store >= branchCount ) {
		    countnum = 1;
		    result.put( "isShopAdd", countnum );//是否新增店铺
		}
		result.put( "page", page );
	    }

	    result.put( "videourl", busUserService.getVoiceUrl( "8" ) );
	} catch ( Exception e ) {
	    logger.error( "商城店铺管理异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商城店铺管理异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取未新增店铺的门店列表
     */
    @ApiOperation( value = "获取未新增店铺的门店列表", notes = "获取未新增店铺的门店列表" )
    @ResponseBody
    @RequestMapping( value = "/getShopList", method = RequestMethod.POST )
    public ServerResponse getShopList( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = SessionUtils.getLoginUser( request );
	List< WsWxShopInfoExtend > shopInfoList = null;
	try {
	    //获取用户店铺集合
	    List< Map< String,Object > > allSto = mallStoreService.findAllStore( user.getId() );

	    shopInfoList = wxShopService.queryWxShopByBusId( user.getId() );
	    if ( allSto != null && allSto.size() > 0 && shopInfoList != null && shopInfoList.size() > 0 ) {
		for ( Map< String,Object > shopMap : allSto ) {
		    if ( CommonUtil.isEmpty( shopMap.get( "wx_shop_id" ) ) ) {
			continue;
		    }
		    for ( WsWxShopInfoExtend wxShop : shopInfoList ) {
			if ( CommonUtil.toString( shopMap.get( "wx_shop_id" ) ).equals( wxShop.getId().toString() ) ) {
			    shopInfoList.remove( wxShop );
			    break;
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取未新增店铺的门店列表：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取未新增店铺的门店列表" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), shopInfoList );
    }

    /**
     * 获取商家店铺信息
     */
    @ApiOperation( value = "获取商家店铺信息", notes = "获取商家店铺信息" )
    @ResponseBody
    @RequestMapping( value = "/storeInfo", method = RequestMethod.POST )
    public ServerResponse storeInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "店铺ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > sto = new HashMap<>();
	try {
	    sto = mallStoreService.findShopByStoreId( id );
	} catch ( Exception e ) {
	    logger.error( "获取商家店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商家店铺信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), sto );
    }

    /**
     * 获取商家店铺列表
     */
    @ApiOperation( value = "获取商家店铺列表", notes = "获取商家店铺列表" )
    @ResponseBody
    @RequestMapping( value = "/storeList", method = RequestMethod.POST )
    public ServerResponse storeList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map< String,Object > > storeList = null;
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    storeList = mallStoreService.findAllStoByUser( user, request );
	} catch ( Exception e ) {
	    logger.error( "获取商家店铺列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商家店铺列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), storeList );
    }

    /**
     * 保存或修改店铺信息
     */
    @ApiOperation( value = "保存店铺信息", notes = "保存店铺信息" )
    @ResponseBody
    @SysLogAnnotation( description = "店铺管理-保存店铺信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );

	    MallStore sto = com.alibaba.fastjson.JSONObject.parseObject( params.get( "obj" ).toString(), MallStore.class );
	    if(CommonUtil.isNotEmpty( sto.getWxShopId() )){
		WsWxShopInfo info=wxShopService.getShopById( sto.getWxShopId() );
		sto.setStoLongitude(info.getLongitude()  );
		sto.setStoLatitude( info.getLatitude() );
		sto.setStoHouseMember(  info.getDetail());
	    }
	    sto.setStoUserId( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreatePerson( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreateTime( new Date() );
	    boolean flag = mallStoreService.saveOrUpdate( sto, user );
	    if ( flag ) {
		SessionUtils.setShopListBySession( user.getId(), null, request );
		mallStoreService.findAllStoByUser( user, request );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除店铺信息
     */
    @ApiOperation( value = "删除店铺", notes = "删除店铺" )
    @ResponseBody
    @SysLogAnnotation( description = "店铺管理-删除店铺信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "店铺ID集合,用逗号隔开", required = true ) @RequestParam String ids ) throws IOException {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String id[] = ids.toString().split( "," );
	    boolean flag = mallStoreService.deleteShop( id );
	    if ( flag ) {
		SessionUtils.setShopListBySession( user.getId(), null, request );
		mallStoreService.findAllStoByUser( user, request );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入修改店铺页面异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取店铺链接
     */
    @ApiOperation( value = "获取店铺链接", notes = "获取店铺链接" )
    @ResponseBody
    @RequestMapping( value = "/link", method = RequestMethod.POST )
    public ServerResponse link( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "pageId", value = "页面ID", required = true ) @RequestParam Integer pageId ) throws IOException {
	Map< String,Object > result = new HashMap<>();
	try {
	    String url = PropertiesUtil.getHomeUrl() + "/mallPage/" + pageId + "/79B4DE7C/viewHomepage.do";
	    result.put( "storeLink", url );//店铺链接
	    result.put( "smsLink", url );//短信链接
	} catch ( Exception e ) {
	    logger.error( "获取店铺链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 自动生成二维码
     */
    @ApiOperation( value = "生成二维码", notes = "生成二维码" )
    @RequestMapping( value = "/generateQRCode", method = RequestMethod.GET )
    public void generateQRCode( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "url", value = "地址", required = true ) @RequestParam String url ) {
	try {
	    String content = PropertiesUtil.getHomeUrl() + url;
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "自动生成二维码异常：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /******************************全店风格*************************************/
    /**
     * 获取配色风格列表
     */
    @ApiOperation( value = "获取配色风格列表", notes = "获取配色风格列表" )
    @ResponseBody
    @RequestMapping( value = "/getStyleList", method = RequestMethod.POST )
    public ServerResponse getStyleList( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map< String,Object > result = new HashMap<>();
	try {
	    List< Map > styleList = dictService.getDict( "k001" );
	    for ( Map map : styleList ) {
		String[] style = map.get( "item_value" ).toString().split( "," );
		map.put( "style", style );
	    }
	    result.put( "styleList", styleList );
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		result.put( "styleKey", set.getStyleKey() );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取店铺配色风格列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存配色风格
     */
    @ApiOperation( value = "保存配色风格", notes = "保存配色风格" )
    @ResponseBody
    @SysLogAnnotation( description = "保存配色风格", op_function = "2" )
    @RequestMapping( value = "/saveStyle", method = RequestMethod.POST )
    public ServerResponse saveStyle( HttpServletRequest request, HttpServletResponse response, @ApiParam( value = "配色key", required = true ) @RequestParam String styleKey )
		    throws IOException {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    if ( set != null ) {
		MallPaySet mallPaySet = new MallPaySet();
		mallPaySet.setId( set.getId() );
		mallPaySet.setStyleKey( styleKey );
		mallPaySetService.updateById( mallPaySet );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存配色风格异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存配色风格异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
