package com.gt.mall.controller.store;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallCommentGive;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.*;
import com.gt.mall.web.service.basic.MallCommentGiveService;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.catalina.Store;
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

    @RequestMapping( "/index" )
    public String res_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入店铺管理啦啦啦" );
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    int pid = 0;
	    boolean isAdminFlag = true;
	    //如果是子账户按照分店查询
	    if ( CommonUtil.isNotEmpty( user.getPid() ) && user.getPid() > 0 ) {
		//params.put("branchids", userService.findBranchIdByUser(user.getId()));
		pid = user.getPid();
		isAdminFlag = mallStoreService.isAdminUser( user.getId() );//查询子账户是否是管理员
		if ( !isAdminFlag ) {
		    request.setAttribute( "isNoAdminFlag", 1 );
		}
	    }

	    if ( isAdminFlag ) {
		params.put( "userId", user.getId() );
		params.put( "pid", pid );
		PageUtil page = mallStoreService.findByPage( params );
		if ( !CommonUtil.isEmpty( params.get( "stoName" ) ) ) {
		    request.setAttribute( "stoName", params.get( "stoName" ) );
		}
		int branchCount = mallStoreService.countBranch( user.getId() );
		int shopcount = branchCount + 1;
		int store = mallStoreService.countStroe( user.getId() );
		int countnum = 0;//创建店铺多余本身店铺就有问题，返回1 不能添加主店铺，只能添加子店铺
		if ( store >= shopcount ) {
		    countnum = 1;
		}
		request.setAttribute( "countnum", countnum );
		request.setAttribute( "page", page );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    //todo 调用陈丹接口 视频教程接口
	    /*request.setAttribute("videourl", course.urlquery("8"));*/
	} catch ( Exception e ) {
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
	    Integer userId = user.getId();
	    if ( CommonUtil.isEmpty( params.get( "id" ) ) ) {
		//todo 调用陈丹信息  userService.findUserDefArea
		Map< String,Object > defArea = null;//userService.findUserDefArea(userId);
		if ( defArea.size() > 0 ) {
		    MallStore sto = new MallStore();
		    sto.setStoAddress( defArea.get( "detail_address" ).toString() );
		    sto.setStoLatitude( defArea.get( "latitude" ).toString() );
		    sto.setStoLongitude( defArea.get( "longitude" ).toString() );
		    sto.setStoProvince( CommonUtil.toInteger( defArea.get( "province_id" ) ) );
		    if ( CommonUtil.isEmpty( defArea.get( "city_id" ) ) ) {
			defArea.put( "city_id", defArea.get( "district_id" ) );
		    }
		    sto.setStoCity( CommonUtil.toInteger( defArea.get( "city_id" ) ) );
		    if ( CommonUtil.isNotEmpty( defArea.get( "district_id" ) ) ) {
			sto.setStoArea( CommonUtil.toInteger( defArea.get( "district_id" ) ) );
		    }
		    request.setAttribute( "sto", sto );
		}
		request.setAttribute( "defArea", defArea );
	    } else {
		request.setAttribute( "pageTitle", "编辑信息" );
		//				Store sto = storeMapper.selectByPrimaryKey(CommonUtil.toInteger(params.get("id")));
		Map< String,Object > sto = mallStoreService.findShopByStoreId( CommonUtil.toInteger( params.get( "id" ) ) );
		/*if(sto != null){
		    if(CommonUtil.isNotEmpty(sto.get("wxShopId"))){
			shopId = CommonUtil.toInteger(sto.get("wxShopId").toString());
		    }
		    if(CommonUtil.isEmpty(sto.get("stoPicture")) && CommonUtil.isNotEmpty(sto.get("sto_picture"))){
			sto.put("stoPicture", sto.get("sto_picture"));
		    }
		}*/
		request.setAttribute( "sto", sto );
	    }
	    String http = PropertiesUtil.getResourceUrl();
	    request.setAttribute( "http", http );
	    //获取用户店铺集合
	    List< Map< String,Object > > allSto = mallStoreService.findAllStore( userId );
	    request.setAttribute( "allSto", allSto );

	    List< Map< String,Object > > list = mallStoreService.findShop( userId, shopId );
	    request.setAttribute( "shopList", list );

	    //todo 调用陈丹接口，根据地区id查询地区信息
	    //request.setAttribute("areaLs", restaurantService.findCityByPid(restaurantService.getAreaIds()));
	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    request.setAttribute( "pageTitle", "添加信息" );
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

	    MallStore sto = (MallStore) JSONObject.toBean( JSONObject.fromObject( params.get( "obj" ) ), Store.class );
	    sto.setStoUserId( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreatePerson( SessionUtils.getLoginUser( request ).getId() );
	    sto.setStoCreateTime( new Date() );
	    msg = mallStoreService.saveOrUpdate( sto, user );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
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
	    String ids[] = params.get( "ids" ).toString().split( "," );
	    msg = mallStoreService.deleteShop( ids );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 查看二维码信息
     */
    @RequestMapping( "/viewQR" )
    public String viewQR( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	MallStore sto = mallStoreService.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	request.setAttribute( "qrCode", sto.getStoQrCode() );
	return "mall/store/viewQR";
    }

    /**
     * 进入商城设置列表页面
     */
    @RequestMapping( "setindex" )
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
		//todo 调用小屁孩接口
		List< Map< String,Object > > messageList = null;//msgTemplateService.selectListByBusId( user.getId() );

		request.setAttribute( "messageList", messageList );
		request.setAttribute( "giveList", giveList );
	    }
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
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
	    logger.debug( "编辑设置：" + e.getMessage() );
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
