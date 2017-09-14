package com.gt.mall.controller.api.store;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallCommentGive;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Controller
@RequestMapping( "/mallStore" )
public class MallStoreNewController extends BaseController {

    @Autowired
    private MallStoreService mallStoreService;

    @Autowired
    private MallCommentGiveService mallCommentGiveService;

    @Autowired
    private MallPaySetService mallPaySetService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private WxShopService wxShopService;

    @ApiOperation( value = "进入店铺管理接口", notes = "获取商家的所有店铺列表" )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/index", method = RequestMethod.GET )
    public ServerResponse index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    WxPublicUsers wxPublicUsers = SessionUtils.getLoginPbUser( request );
	    if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		if ( CommonUtil.isNotEmpty( wxPublicUsers.getMchId() ) && CommonUtil.isNotEmpty( wxPublicUsers.getApiKey() ) ) {
		    result.put( "isShowUpd", 1 );
		}
	    }
//	    result.put( "wxPublicUsers", wxPublicUsers );
	    int pid = SessionUtils.getAdminUserId( user.getId(), request );//查询总账号id
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//true 是管理员

	    if ( isAdminFlag ) {
		params.put( "userId", user.getId() );
		params.put( "pid", pid );
		List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
		if ( shopList != null && shopList.size() > 0 ) {
		    PageUtil page = mallStoreService.findByPage( params, shopList );
		    if ( CommonUtil.isNotEmpty( params.get( "stoName" ) ) ) {
			result.put( "stoName", params.get( "stoName" ) );
		    }
		    int branchCount = SessionUtils.getWxShopNumBySession( user.getId(), request );
		    int store = mallStoreService.countStroe( user.getId() );
		    int countnum = 0;//创建店铺多余本身店铺就有问题，返回1 不能添加主店铺，只能添加子店铺
		    if ( store >= branchCount ) {
			countnum = 1;
			result.put( "isShopAdd", countnum );
		    }

		    result.put( "page", page );
		}
		result.put( "imgUrl", PropertiesUtil.getResourceUrl() );
		result.put( "path", PropertiesUtil.getHomeUrl() );
	    } else {
		result.put( "isNoAdminFlag", 1 );
	    }
//	    result.put( "wxPublicUsers", wxPublicUsers );
	    result.put( "videourl", busUserService.getVoiceUrl( "8" ) );
	    result.put( "payUrl", PropertiesUtil.getWxmpDomain() );

	} catch ( Exception e ) {
	    logger.error( "商城店铺管理异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商城店铺管理异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 进入编辑页面
     */
    @RequestMapping( value = "/to_edit", method = RequestMethod.GET )
    public ServerResponse to_edit( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	BusUser user = SessionUtils.getLoginUser( request );
	int shopId = 0;
	try {
	    Integer userId = user.getId();
	    int wxShopId = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Map< String,Object > sto = mallStoreService.findShopByStoreId( CommonUtil.toInteger( params.get( "id" ) ) );
		result.put( "sto", sto );
		if ( CommonUtil.isNotEmpty( sto ) ) {
		    if ( CommonUtil.isNotEmpty( sto.get( "wxShopId" ) ) ) {
			wxShopId = CommonUtil.toInteger( sto.get( "wxShopId" ) );
		    }
		}
	    }
	    String http = PropertiesUtil.getResourceUrl();
	    result.put( "http", http );
	    //获取用户店铺集合
	    List< Map< String,Object > > allSto = mallStoreService.findAllStore( userId );

	    List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( userId );
	    if ( allSto != null && allSto.size() > 0 && shopInfoList != null && shopInfoList.size() > 0 ) {
		for ( Map< String,Object > shopMap : allSto ) {
		    if ( CommonUtil.isEmpty( shopMap.get( "wx_shop_id" ) ) ) {
			continue;
		    }
		    for ( WsWxShopInfoExtend wxShop : shopInfoList ) {
			if ( CommonUtil.toString( shopMap.get( "wx_shop_id" ) ).equals( wxShop.getId().toString() ) ) {
			    if ( wxShopId > 0 && wxShopId == wxShop.getId() ) {
				break;
			    }
			    shopInfoList.remove( wxShop );
			    break;
			}
		    }
		}
	    }
	    result.put( "shopList", shopInfoList );

	} catch ( Exception e ) {
	    logger.error( "进入修改店铺页面异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入修改店铺页面异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存或修改店铺信息
     */
    @SysLogAnnotation( description = "店铺管理-保存店铺信息", op_function = "2" )
    @RequestMapping( value = "/saveOrUpdate", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );

	    MallStore sto = com.alibaba.fastjson.JSONObject.parseObject( params.get( "obj" ).toString(), MallStore.class );
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
    @SysLogAnnotation( description = "店铺管理-删除店铺信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.DELETE )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String ids[] = params.get( "ids" ).toString().split( "," );
	    boolean flag = mallStoreService.deleteShop( ids );
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
     * 进入商城设置列表页面
     */
    @RequestMapping( value = "/setindex", method = RequestMethod.GET )
    public ServerResponse setindex( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    if ( user != null ) {
		MallPaySet paySet = new MallPaySet();
		paySet.setUserId( user.getId() );
		MallPaySet set = mallPaySetService.selectByUserId( paySet );
		result.put( "set", set );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
			JSONArray msgArr = JSONArray.fromObject( set.getMessageJson() );
			result.put( "msgArr", msgArr );
		    }
		    if ( CommonUtil.isNotEmpty( set.getSmsMessage() ) ) {
			JSONObject smsMsgObj = JSONObject.fromObject( set.getSmsMessage() );
			result.put( "smsMsgObj", smsMsgObj );
		    }
		    if ( CommonUtil.isNotEmpty( set.getFooterJson() ) ) {
			JSONObject foorerObj = JSONObject.fromObject( set.getFooterJson() );
			result.put( "foorerObj", foorerObj );
		    }
		}

		List< MallCommentGive > giveList = mallCommentGiveService.getGiveByUserId( user.getId() );

		//获取消息模板的内容
		List< Map > messageList = wxPublicUserService.selectTempObjByBusId( user.getId() );

		result.put( "messageList", messageList );
		result.put( "giveList", giveList );
	    }
	} catch ( Exception e ) {
	    logger.error( "进入商城设置页面异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入修改店铺页面异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 编辑设置
     */
    @SysLogAnnotation( description = "商城设置-编辑设置", op_function = "2" )
    @RequestMapping( value = "edit_set", method = RequestMethod.POST )
    public ServerResponse editSet( HttpServletRequest request, @RequestBody Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑设置的controller" );
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		if ( params != null ) {
		    params.put( "userId", user.getId() );
		    mallPaySetService.editPaySet( params );
		} else {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc() );
	    }
	} catch ( Exception e ) {
	    logger.error( "编辑商城设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入修改店铺页面异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );

    }

    /**
     * 自动生成二维码
     */
    @RequestMapping( value = "/79B4DE7C/getTwoCode", method = RequestMethod.GET )
    public void getTwoCode( @RequestBody Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    String url = params.get( "url" ).toString();
	    String content = PropertiesUtil.getHomeUrl() + url;
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "自动生成二维码异常：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

}
