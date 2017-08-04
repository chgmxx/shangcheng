package com.gt.mall.controller.applet;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.cxf.service.WxShopService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.applet.MallAppletImageService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallAppletImage" )
public class MallAppletImageController extends BaseController {

    @Autowired
    private MallAppletImageService appletImageService;
    @Autowired
    private WxShopService          wxShopService;
    @Autowired
    private MallStoreService       storeService;

    /**
     * 小程序管理列表页面
     *
     * @return
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = true;//是管理员
	    if ( CommonUtil.isNotEmpty( user.getPid() ) && user.getPid() > 0 ) {
		isAdminFlag = storeService.isAdminUser( user.getId() );//查询子账户是否是管理员

		if ( !isAdminFlag ) {
		    request.setAttribute( "isNoAdminFlag", 1 );
		}
	    }
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "userId", user.getId() );
		    PageUtil page = appletImageService.selectImageByShopId( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    /*request.setAttribute("videourl", course.urlquery("86"));*/
	} catch ( Exception e ) {
	    logger.error( "小程序列表：" + e );
	    e.printStackTrace();
	}

	return "mall/applet/applet_index";
    }

    /**
     * 进入小程序编辑页面
     *
     * @return
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据小程序id查询小程序信息
		Map< String,Object > imageMap = appletImageService.selectImageById( id );
		request.setAttribute( "imageMap", imageMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入小程序编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/applet/applet_edit";
    }

    /**
     * 编辑小程序
     *
     * @param params
     * @throws IOException
     *
     */
    @SysLogAnnotation( description = "小程序管理-编辑小程序", op_function = "2" )
    @RequestMapping( "edit" )
    public void edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑辑小程序controller" );
	int code = -1;// 编辑成功
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		boolean flag = appletImageService.editImage( params, userId );// 编辑商品
		if ( flag ) { code = 1;}
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑小程序：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put( "code", code );
	CommonUtil.write( response, obj );
    }

    /**
     * 删除小程序
     *
     * @throws IOException
     *
     */
    @SysLogAnnotation( description = "小程序管理-删除小程序", op_function = "4" )
    @RequestMapping( "applet_remove" )
    public void applet_remove( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除小程序controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	try {
	    if ( CommonUtil.isNotEmpty( params ) ) {
		boolean flag = appletImageService.deleteImage( params );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除小程序：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put( "code", code );
	CommonUtil.write( response, obj );
    }

}
