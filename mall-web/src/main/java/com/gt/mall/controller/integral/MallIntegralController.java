package com.gt.mall.controller.integral;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.util.*;
import com.gt.mall.web.service.integral.MallIntegralImageService;
import com.gt.mall.web.service.integral.MallIntegralService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商品表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mApplet" )
public class MallIntegralController extends BaseController {

    @Autowired
    private MallIntegralService      integralService;
    @Autowired
    private MallIntegralImageService integralImageService;
    @Autowired
    private MallStoreService         storeService;

    /**
     * 进入积分商城
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "index" )
    public String toIndex( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		    request.setAttribute( "shop_id", params.get( "shopId" ) );
		} else {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = integralService.selectIntegralByPage( params );
		request.setAttribute( "page", page );
		request.setAttribute( "shoplist", shoplist );
	    }
	    request.setAttribute( "type", params.get( "type" ) );
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getArticleUrl() );
	    request.setAttribute( "userId", user.getId() );

			/*request.setAttribute("videourl", course.urlquery("86"));*/
	} catch ( Exception e ) {
	    logger.error( "进入积分商城异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/integral_index";
    }

    /**
     * 进入编辑积分商品
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "to_edit" )
    public String toEdit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );

		Map< String,Object > integralMap = integralService.selectByIds( id );
		request.setAttribute( "integralMap", integralMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入编辑积分商品异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/integral_edit";
    }

    /**
     * 保存积分商品信息
     *
     * @param request
     * @param response
     *
     * @return
     * @throws IOException
     */
    @RequestMapping( value = "save" )
    public void save( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    resultMap = integralService.saveIntegral( user.getId(), params );

	} catch ( Exception e ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "保存积分商品异常" );
	    logger.error( "保存积分商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 保存积分商品信息
     *
     * @param request
     * @param response
     *
     * @return
     * @throws IOException
     */
    @RequestMapping( value = "integral_remove" )
    public void integral_remove( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    boolean flag = integralService.removeIntegral( params );
	    if ( flag ) {
		resultMap.put( "code", 1 );
	    } else {
		resultMap.put( "code", -1 );
	    }
	} catch ( Exception e ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "msg", "删除积分商品异常" );
	    logger.error( "删除积分商品异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 获取二维码的图片
     *
     * @param params
     */
    @RequestMapping( value = "getTwoCode" )
    public void getTwoCode( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    String content = PropertiesUtil.getArticleUrl() + "/phoneIntegral/79B4DE7C/integralProduct.do?id=" + params.get( "id" ) + "&uId=" + params.get( "uId" ) + "&shopId="
			    + params.get( "shopId" );
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "获取积分商品二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 积分商城图片管理列表页面
     *
     * @return
     */
    @RequestMapping( "image_index" )
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
		    PageUtil page = integralImageService.selectImageByShopId( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    //TODO 需关连 视频  方法
	    //	    request.setAttribute("videourl", course.urlquery("86"));
	} catch ( Exception e ) {
	    logger.error( "积分商城图片列表：" + e );
	    e.printStackTrace();
	}

	return "mall/integral/integral_image_index";
    }

    /**
     * 进入积分商城图片编辑页面
     *
     * @return
     */
    @RequestMapping( "to_image_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据积分商城图片id查询积分商城图片信息
		MallIntegralImage imageMap = integralImageService.selectById( id );
		request.setAttribute( "imageMap", imageMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入积分商城图片编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/integral/integral_image_edit";
    }

    /**
     * 编辑积分商城图片
     *
     * @throws IOException
     * @Title: editGroup
     */
    @SysLogAnnotation( description = "积分商城图片管理-编辑积分商城图片", op_function = "2" )
    @RequestMapping( "image_edit" )
    public void edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑辑积分商城图片controller" );
	int code = -1;// 编辑成功
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		boolean flag = integralImageService.editImage( params, userId );// 编辑商品
		if ( flag ) {
		    code = 1;
		}
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑积分商城图片：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put( "code", code );
	CommonUtil.write( response, obj );
    }

    /**
     * 删除积分商城图片
     *
     * @throws IOException
     * @Title: editGroup
     */
    @SysLogAnnotation( description = "积分商城图片管理-删除积分商城图片", op_function = "4" )
    @RequestMapping( "image_remove" )
    public void applet_remove( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除积分商城图片controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	try {
	    if ( CommonUtil.isNotEmpty( params ) ) {
		boolean flag = integralImageService.deleteImage( params );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除积分商城图片：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put( "code", code );
	CommonUtil.write( response, obj );
    }

    /**
     * 获取二维码的图片
     *
     * @param params
     */
    @RequestMapping( value = "/integralMallTwoCode" )
    public void integralMallTwoCode( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String shopId = params.get( "shopId" ).toString();
	    String content = PropertiesUtil.getHomeUrl() + "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + user.getId();
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "获取积分商城二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 获取二维码的图片
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/integralMallCodeIframs" )
    public String integralMallCodeIframs( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    String shopId = params.get( "shopId" ).toString();
	    BusUser user = SessionUtils.getLoginUser( request );
	    String html = PropertiesUtil.getHomeUrl() + "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + user.getId();
	    request.setAttribute( "html", html );
	    request.setAttribute( "shopId", shopId );
	} catch ( Exception e ) {
	    logger.error( "获取积分商城二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/iframe/integral_mall";
    }

}
