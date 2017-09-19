package com.gt.mall.controller.store;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/store" )
public class MallStoreController extends BaseController {

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

    @RequestMapping( "/index" )
    public String res_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入店铺管理啦啦啦" );
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    WxPublicUsers wxPublicUsers = SessionUtils.getLoginPbUser( request );
	    request.setAttribute( "wxPublicUsers", wxPublicUsers );
	    int pid = SessionUtils.getAdminUserId( user.getId(), request );//查询总账号id
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//true 是管理员

	    if ( isAdminFlag ) {
		params.put( "userId", user.getId() );
		params.put( "pid", pid );
		List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
		if ( shopList != null && shopList.size() > 0 ) {
		    PageUtil page = mallStoreService.findByPage( params, shopList );
		    if ( CommonUtil.isNotEmpty( params.get( "stoName" ) ) ) {
			request.setAttribute( "stoName", params.get( "stoName" ) );
		    }
		    int branchCount = SessionUtils.getWxShopNumBySession( user.getId(), request );
		    int store = mallStoreService.countStroe( user.getId() );
		    int countnum = 0;//创建店铺多余本身店铺就有问题，返回1 不能添加主店铺，只能添加子店铺
		    if ( store >= branchCount ) {
			countnum = 1;
		    }
		    request.setAttribute( "countnum", countnum );
		    request.setAttribute( "page", page );
		}
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    } else {
		request.setAttribute( "isNoAdminFlag", 1 );
	    }
	    request.setAttribute( "wxPublicUsers", wxPublicUsers );
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "8" ) );
	    request.setAttribute( "payUrl", PropertiesUtil.getWxmpDomain() );

	} catch ( Exception e ) {
	    logger.error( "商城店铺管理异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/store/index";
    }

    /**
     * 进入编辑页面
     */
    @RequestMapping( "/to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	int shopId = 0;
	try {
	    request.setAttribute( "pageTitle", "添加信息" );
	    Integer userId = user.getId();
	    int wxShopId = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		request.setAttribute( "pageTitle", "编辑信息" );
		Map< String,Object > sto = mallStoreService.findShopByStoreId( CommonUtil.toInteger( params.get( "id" ) ) );
		request.setAttribute( "sto", sto );
		if ( CommonUtil.isNotEmpty( sto ) ) {
		    if ( CommonUtil.isNotEmpty( sto.get( "wxShopId" ) ) ) {
			wxShopId = CommonUtil.toInteger( sto.get( "wxShopId" ) );
		    }
		}
	    }
	    String http = PropertiesUtil.getResourceUrl();
	    request.setAttribute( "http", http );
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
	    request.setAttribute( "shopList", shopInfoList );

	} catch ( Exception e ) {
	    logger.error( "进入修改店铺页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/store/edit";
    }

    /**
     * 保存或修改店铺信息
     */
    @SysLogAnnotation( description = "店铺管理-保存店铺信息", op_function = "2" )
    @RequestMapping( "/saveOrUpdate" )
    public void saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > msg = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );

	    MallStore sto = com.alibaba.fastjson.JSONObject.parseObject( params.get( "obj" ).toString(), MallStore.class );
	    sto.setStoUserId( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreatePerson( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreateTime( new Date() );
	    boolean flag = mallStoreService.saveOrUpdate( sto, user );
	    if ( flag ) {
		msg.put( "code", ResponseEnums.SUCCESS.getCode() );
		SessionUtils.setShopListBySession( user.getId(), null, request );
		mallStoreService.findAllStoByUser( user, request );
	    }
	} catch ( BusinessException e ) {
	    msg.put( "code", e.getCode() );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	    logger.error( "保存店铺信息异常：" + e.getMessage() );
	} catch ( Exception e ) {
	    msg.put( "code", ResponseEnums.ERROR.getCode() );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	    logger.error( "保存店铺信息异常：" + e.getMessage() );
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 删除店铺信息
     */
    @SysLogAnnotation( description = "店铺管理-删除店铺信息", op_function = "4" )
    @RequestMapping( "/delete" )
    public void delete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > msg = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String ids[] = params.get( "ids" ).toString().split( "," );
	    boolean flag = mallStoreService.deleteShop( ids );
	    if ( flag ) {
		msg.put( "result", true );
		SessionUtils.setShopListBySession( user.getId(), null, request );
		mallStoreService.findAllStoByUser( user, request );
	    }
	} catch ( BusinessException e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	    logger.error( "删除店铺信息异常：" + e.getMessage() );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    logger.error( "删除店铺信息异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 查看二维码信息
     */
    /*@RequestMapping( "/viewQR" )
    public String viewQR( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	MallStore sto = mallStoreService.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	request.setAttribute( "qrCode", sto.getStoQrCode() );
	return "mall/store/viewQR";
    }*/

    /**
     * 进入商城设置列表页面
     */
    @RequestMapping( "/setindex" )
    public String setindex( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    if ( user != null ) {
		MallPaySet paySet = new MallPaySet();
		paySet.setUserId( user.getId() );
		MallPaySet set = mallPaySetService.selectByUserId( paySet );
		request.setAttribute( "set", set );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
			JSONArray msgArr = JSONArray.fromObject( set.getMessageJson() );
			request.setAttribute( "msgArr", msgArr );
		    }
		    if ( CommonUtil.isNotEmpty( set.getSmsMessage() ) ) {
			JSONObject smsMsgObj = JSONObject.fromObject( set.getSmsMessage() );
			request.setAttribute( "smsMsgObj", smsMsgObj );
		    }
		    if ( CommonUtil.isNotEmpty( set.getFooterJson() ) ) {
			JSONObject foorerObj = JSONObject.fromObject( set.getFooterJson() );
			request.setAttribute( "foorerObj", foorerObj );
		    }
		}

		List< MallCommentGive > giveList = mallCommentGiveService.getGiveByUserId( user.getId() );

		//获取消息模板的内容
		List< Map > messageList = wxPublicUserService.selectTempObjByBusId( user.getId() );

		request.setAttribute( "messageList", messageList );
		request.setAttribute( "giveList", giveList );
	    }
	} catch ( Exception e ) {
	    logger.error( "进入商城设置页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/set/set_index";
    }

    /**
     * 编辑设置
     */
    @SysLogAnnotation( description = "商城设置-编辑设置", op_function = "2" )
    @RequestMapping( "edit_set" )
    public void editSet( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑设置的controller" );
	boolean flag = false;
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		if ( params != null ) {
		    params.put( "userId", user.getId() );
		    int num = mallPaySetService.editPaySet( params );
		    if ( num > 0 ) {
			flag = true;
		    }
		}
	    }
	} catch ( Exception e ) {
	    flag = false;
	    logger.error( "编辑商城设置异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "flag", flag );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 自动生成二维码
     */
    @RequestMapping( value = "/79B4DE7C/getTwoCode" )
    public void getTwoCode( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
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
