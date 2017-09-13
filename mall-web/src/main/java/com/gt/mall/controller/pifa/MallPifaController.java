package com.gt.mall.controller.pifa;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.store.MallStoreService;
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
 * 商品批发表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallWholesalers" )
public class MallPifaController extends BaseController {

    @Autowired
    private MallPifaService   mallPifaService;
    @Autowired
    private MallStoreService  mallStoreService;
    @Autowired
    private MallPageService   mallPageService;
    @Autowired
    private MallPaySetService mallPaySetService;
    @Autowired
    private BusUserService    busUserService;

    /**
     * 批发商列表
     */
    @RequestMapping( value = "/wholesaleList" )
    public String wholesaleList( @RequestParam Map< String,Object > params, HttpServletRequest request ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    PageUtil page = mallPifaService.wholesalerList( params );
	    request.setAttribute( "page", page );
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "84" ) );
	} catch ( Exception e ) {
	    this.logger.error( "MallPifaController.wholesaleList方法异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/wholesalers/wholesalerList";
    }

    /**
     * 批发商设置
     */
    @RequestMapping( value = "/toSetWholesale" )
    public String toSetWholesale( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = SessionUtils.getLoginUser( request );
	MallPaySet set = new MallPaySet();
	set.setUserId( user.getId() );
	MallPaySet paySet = mallPaySetService.selectByUserId( set );
	request.setAttribute( "paySet", paySet );
	if ( CommonUtil.isNotEmpty( paySet ) ) {
	    if ( CommonUtil.isNotEmpty( paySet.getPfSet() ) ) {
		JSONObject obj = JSONObject.fromObject( paySet.getPfSet() );
		request.setAttribute( "pfSet", obj );
	    }
	}
	return "mall/wholesalers/setWholesaler";
    }

    /*
     * 设置批发商
     * @param map
     * @param request
     * @param response
     */
	/*@RequestMapping(value="/setWholesale")
	public void setWholesale(@RequestParam Map<String,Object> params,HttpServletRequest request
			,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			BusUser user = SessionUtils.getLoginUser(request);
			params.put("userId", user.getId());

			int count = mallPifaService.updateSetWholesaler(params);
			if(count == 1){
				map.put("result", 1);//设置批发商成功
			}else{
				map.put("result", 0);//设置批发商失败
			}
			out.write(net.sf.json.JSONObject.fromObject(map).toString());
		} catch (Exception e) {
			log.error("设置批发商异常："+e.getMessage());
		} finally {
			out.flush();
			out.close();
		}
	}*/

    /**
     * 保存批发商设置
     */
    @RequestMapping( value = "/updateSetWholesaler" )
    @SysLogAnnotation( op_function = "3", description = "添加修改批发商设置" )
    public void updateSetWholesaler( @RequestParam Map< String,Object > params, HttpServletRequest request
		    , HttpServletResponse response ) {
	Map< String,Object > map = new HashMap< String,Object >();
	PrintWriter out = null;
	try {
	    out = response.getWriter();
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPaySet mallPaySet = (MallPaySet) JSONObject.toBean( JSONObject.fromObject( params ), MallPaySet.class );

	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    MallPaySet paySet = mallPaySetService.selectByUserId( set );//查询登录者是否有设置商城
	    int count = 0;
	    if ( CommonUtil.isEmpty( paySet ) ) {//如果没有设置商城,则添加批发商设置,有就修改
		mallPaySet.setUserId( user.getId() );
		mallPaySet.setCreateTime( new Date() );
		boolean flag = mallPaySetService.insert( mallPaySet );//添加批发商城设置
		if ( flag ) {
		    count = 1;
		}
	    } else {
		params.put( "userId", user.getId() );
		count = mallPifaService.updateSetWholesaler( params );//修改批发商城设置
	    }

	    if ( count > 0 ) {
		map.put( "result", 1 );
		map.put( "msg", "设置成功" );
	    } else {
		map.put( "result", 0 );
		map.put( "msg", "设置失败，请稍后再试" );
	    }
	    out.write( net.sf.json.JSONObject.fromObject( map ).toString() );
	} catch ( Exception e ) {
	    logger.error( "保存批发设置异常：" + e.getMessage() );
	} finally {
	    out.flush();
	    out.close();
	}
    }

    /**
     * 设置批发商审核通不通过、启不启用
     */
    @RequestMapping( value = "/updateStatus" )
    @SysLogAnnotation( op_function = "3", description = "修改批发商审核通不通过、启不启用" )
    public void updateStatus( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > map = new HashMap< String,Object >();
	PrintWriter out = null;
	try {
	    out = response.getWriter();
	    Object type = params.get( "type" );
	    String status = "";
	    if ( type.equals( "1" ) ) {
		status = "1";//审核通过
	    } else if ( type.equals( "2" ) ) {
		status = "-1";//审核不通过
	    }
	    params.put( "checkTime", new Date() );//审核时间
	    if ( !CommonUtil.isEmpty( params.get( "status" ) ) ) {
		params.remove( "isUse" );//启不启用（如果是审核将移除这个参数）
	    }
	    params.put( "status", status );
	    Object idsObj = params.get( "ids" );
	    int count = 0;
	    if ( !CommonUtil.isEmpty( idsObj ) ) {
		String[] ids = ( idsObj.toString() ).split( "," );
		for ( int i = 0; i < ids.length; i++ ) {
		    params.put( "id", ids[i] );
		    count = mallPifaService.updateStatus( params );
		}
	    }
	    if ( count > 0 ) {
		map.put( "result", 1 );//批发商审核成功
	    } else {
		map.put( "result", 0 );//批发商审核失败
	    }
	    out.write( net.sf.json.JSONObject.fromObject( map ).toString() );
	} catch ( Exception e ) {
	    logger.error( "设置批发商审核通不通过异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    out.flush();
	    out.close();
	}
    }

    /**
     * 批发商列表
     */
    @RequestMapping( value = "/index" )
    public String pifaIndex( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//是否是管理员
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = mallPifaService.pifaProductList( params, shoplist );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    } else {
		request.setAttribute( "isNoAdminFlag", 1 );
	    }
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsPresale() ) ) {
		    if ( set.getIsPf().toString().equals( "1" ) ) {
			request.setAttribute( "isOpenPifa", true );
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "批发管理列表异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/wholesalers/wholesalerIndex";
    }

    /**
     * 进入批发编辑页面
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据批发id查询批发信息
		Map< String,Object > groupMap = mallPifaService.selectPifaById( id );
		if ( groupMap != null ) {
		    Object imageUrl = groupMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = groupMap.get( "imageUrl" );
		    }
		    groupMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "pifa", groupMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入批发编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/wholesalers/wholesalerEdit";
    }

    /**
     * 编辑批发
     */
    @SysLogAnnotation( description = "批发管理-编辑批发", op_function = "2" )
    @RequestMapping( "edit_pifa" )
    public void editPifa( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑辑批发controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPifaService.editPifa( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑批发：" + e );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 删除批发
     */
    @SysLogAnnotation( description = "批发管理-删除批发", op_function = "4" )
    @RequestMapping( "pifa_remove" )
    public void removePifa( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除批发controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) && !CommonUtil.isEmpty( params ) ) {
		int id = 0;
		if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		    id = CommonUtil.toInteger( params.get( "id" ) );
		}
		MallPifa pifa = new MallPifa();
		pifa.setId( id );
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    int type = CommonUtil.toInteger( params.get( "type" ) );
		    if ( type == -1 ) {// 删除
			pifa.setIsDelete( 1 );
		    } else if ( type == -2 ) {// 使失效批发
			pifa.setIsUse( -1 );
		    }
		}
		boolean flag = mallPifaService.deletePifa( pifa );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除批发：" + e );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}

    }

}
