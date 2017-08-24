package com.gt.mall.controller.product;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.entity.product.MallSearchLabel;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.store.MallStoreService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分组 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mPro/group" )
public class MallGroupController extends BaseController {

    @Autowired
    private MallGroupService mallGroupService;
    @Autowired
    MallStoreService            mallStoreService;
    @Autowired
    MallImageAssociativeService mallImageAssociativeService;

    /**
     * 商品分组主页面
     */
    @RequestMapping( "/group_index" )
    public String group_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String urls = request.getHeader( "Referer" );
	    if ( user != null ) {
		Integer userId = user.getId();// 获取登陆人id
		params.put( "userId", userId );

		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		request.setAttribute( "shoplist", shoplist );
		if ( shoplist != null && shoplist.size() > 0 ) {
		    int groupPId = 0;
		    if ( CommonUtil.isNotEmpty( params.get( "pId" ) ) ) {
			groupPId = CommonUtil.toInteger( params.get( "pId" ) );
			request.setAttribute( "pId", params.get( "pId" ) );
		    }
		    params.put( "groupPId", groupPId );
		    params.put( "isLabel", 0 );
		    PageUtil page = mallGroupService.findGroupByPage( params, shoplist, userId );// 获取分组集合
		    request.setAttribute( "page", page );
		    if ( CommonUtil.isNotEmpty( page ) ) {
			if ( page.getCurPage() > 1 ) {
			    urls = "/mPro/group/group_index.do";
			}
		    }
		}
	    }
	    request.setAttribute( "urls", urls );
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	}

	return "mall/product/group_index";
    }

    /**
     * 进入商品分组编辑页面
     */
    @RequestMapping( "/to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );// 获取登陆人id
	    List< Map< String,Object > > list = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( list != null && list.size() > 0 ) {
		MallGroup parentGroup = null;
		int groupId = 0;
		if ( !CommonUtil.isEmpty( params.get( "pId" ) ) ) {
		    groupId = CommonUtil.toInteger( params.get( "pId" ) );
		}
		if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
		    MallGroup group = mallGroupService.findGroupById( CommonUtil
				    .toInteger( params.get( "id" ) ) );
		    if ( group != null ) {
			if ( group.getGroupPId() != null && group.getGroupPId() > 0 )
			    groupId = group.getGroupPId();
		    }
		    request.setAttribute( "group", group );

		    params = new HashMap< String,Object >();
		    params.put( "assId", group.getId() );
		    params.put( "assType", 2 );
		    List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( params );// 根据分组id查询图片
		    request.setAttribute( "imageList", imageList );
		} else {
		    //					params = new HashMap<String, Object>();
		    //					if(CommonUtil.isNotEmpty(list.get(0))){
		    //						params.put("shopId", list.get(0).get("id"));
		    //					}
		    //					//查询所有父类的分组
		    //					List<Map<String, Object>> pGroupList = mallGroupService.selectPGroupByShopId(params);
		    //					request.setAttribute("pGroupList", pGroupList);
		}

		if ( CommonUtil.isNotEmpty( params.get( "pId" ) ) ) {
		    parentGroup = mallGroupService.findGroupById( groupId );// 根据id查询父类id
		    request.setAttribute( "parentGroup", parentGroup );
		    request.setAttribute( "pId", params.get( "pId" ) );
		}

		request.setAttribute( "shopList", list );
	    }
	    request.setAttribute( "urls", request.getHeader( "Referer" ) );
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/group_edit";
    }

    /**
     * 编辑商品分组
     */
    @SuppressWarnings( { "unchecked", "deprecation" } )
    @SysLogAnnotation( description = "商品管理-商品分组：编辑商品分组", op_function = "2" )
    @RequestMapping( "group_edit" )
    public void editGroup( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑分组controller" );
	response.setCharacterEncoding( "utf-8" );
	List< MallImageAssociative > imageList = new ArrayList< MallImageAssociative >();
	int code = 1;// 编辑成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) ) {

		MallGroup group = (MallGroup) JSONObject.toBean(
				JSONObject.fromObject( params.get( "group" ) ),
				MallGroup.class );
		if ( !CommonUtil.isEmpty( params.get( "imageArr" ) ) ) {
		    imageList = (List< MallImageAssociative >) JSONArray.toList( JSONArray.fromObject( params.get( "imageArr" ) ), MallImageAssociative.class );
		}

		boolean flag = mallGroupService.saveOrUpdateGroup( group, imageList, userId );
		if ( !flag ) {
		    code = -1;// 编辑失败
		}
	    } else {
		code = 0;// 未登录
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}

    }

    /**
     * 删除商品分组
     */
    @SysLogAnnotation( description = "商品管理-商品分组：删除商品分组", op_function = "4" )
    @RequestMapping( "group_remove" )
    public void deleteGroup( HttpServletRequest request,
		    HttpServletResponse response, Integer id ) throws IOException {
	logger.info( "进入删除分组controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) ) {
		boolean flag = mallGroupService.deleteGroup( id );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    } else {
		code = 0;// 未登录
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 获取店铺下的商品分组
     */
    @RequestMapping( "refreshGroup" )
    public void refreshGroup( HttpServletRequest request,
		    HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除分组controller" );
	response.setCharacterEncoding( "utf-8" );
	List< Map< String,Object > > groupList = new ArrayList< Map< String,Object > >();
	PrintWriter p = null;
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    // 查询会员下面的所有分组
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isEmpty( params ) ) {
		    params = new HashMap< String,Object >();
		} else {
		    if ( CommonUtil.isEmpty( params.get( "groupPId" ) ) ) {
			params.put( "groupPId", 0 );
		    }
		}

		params.put( "shoplist", shoplist );
		groupList = mallGroupService.findGroupByShopId( params );
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "groupList", groupList );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 通过店铺id查询分组
     */
    @RequestMapping( "getGroupByShopId" )
    public void getGroupByShopId( HttpServletRequest request,
		    HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑分组controller" );
	response.setCharacterEncoding( "utf-8" );
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) ) {
		list = mallGroupService.selectGroupByParent( params );
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "list", list );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 获取商品分组
     */
    @RequestMapping( "/getGroups" )
    public String getGroups( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	int type = 0;
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( !CommonUtil.isEmpty( user ) ) {
		params.put( "userId", user.getId() );
		if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
		    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		    if ( shoplist != null && shoplist.size() > 0 ) {
			request.setAttribute( "shoplist", shoplist );
			params.put( "shopId", shoplist.get( 0 ).get( "id" ) );
		    }
		}
		//查询所有分组
		List< Map< String,Object > > list = mallGroupService.selectGroupByParent( params );
		request.setAttribute( "list", list );
	    }
	    request.setAttribute( "shopId", params.get( "shopId" ) );
	    if ( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) {
		request.setAttribute( "proId", params.get( "proId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = CommonUtil.toInteger( params.get( "type" ) );
	    }
	    request.setAttribute( "type", type );
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/groupPopUp";
    }

    /**
     * 获取商品分组
     */
    @RequestMapping( "/syncProduct" )
    public String syncProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( !CommonUtil.isEmpty( user ) ) {
		params.put( "userId", user.getId() );

		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    request.setAttribute( "shoplist", shoplist );
		    params.put( "shopId", shoplist.get( 0 ).get( "id" ) );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/groupSyncPopUp";
    }

    /**
     * 商品搜索标签的主页面
     */
    @RequestMapping( "/label_index" )
    public String label_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {

	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    String urls = request.getHeader( "Referer" );
	    if ( user != null ) {
		Integer userId = user.getId();// 获取登陆人id
		params.put( "userId", userId );

		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		request.setAttribute( "shoplist", shoplist );
		if ( shoplist != null && shoplist.size() > 0 ) {
		    int groupPId = 0;
		    if ( CommonUtil.isNotEmpty( params.get( "pId" ) ) ) {
			groupPId = CommonUtil.toInteger( params.get( "pId" ) );
			request.setAttribute( "pId", params.get( "pId" ) );
		    }
		    params.put( "groupPId", groupPId );
		    params.put( "type", 1 );
		    params.put( "isLabel", 1 );
		    PageUtil page = mallGroupService.findGroupByPage( params, shoplist, userId );// 获取分组集合
		    request.setAttribute( "page", page );
		    if ( CommonUtil.isNotEmpty( page ) ) {
			if ( page.getCurPage() > 1 ) {
			    urls = "/mPro/group/label_index.do";
			}
		    }
		}
	    }
	    request.setAttribute( "urls", urls );
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/label_index";
    }

    /**
     * 推荐分组
     */
    @SuppressWarnings( { "unchecked", "deprecation" } )
    @SysLogAnnotation( description = "商品管理-搜索推荐管理：推荐分组", op_function = "2" )
    @RequestMapping( "labelEdit" )
    public void labelEdit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入推荐分组controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 编辑成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) ) {

		List< MallSearchLabel > labelList = (List< MallSearchLabel >) JSONArray.toList( JSONArray.fromObject( params.get( "param" ) ), MallSearchLabel.class );
		boolean flag = mallGroupService.saveOrUpdateGroupLabel( labelList, userId );
		if ( !flag ) {
		    code = -1;// 编辑失败
		}
	    } else {
		code = 0;// 未登录
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 根据店铺id获取商品分组
     */
    @RequestMapping( "/getGroupsByShopid" )
    public void getGroupsByShopid( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入推荐分组controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 编辑成功
	PrintWriter p = null;
	JSONObject obj = new JSONObject();
	try {
	    p = response.getWriter();
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( !CommonUtil.isEmpty( user ) ) {
		//查询所有分组
		List< Map< String,Object > > list = mallGroupService.selectGroupByParent( params );
		obj.put( "list", list );
	    }
	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    code = -1;
	    e.printStackTrace();
	} finally {
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}
    }
}
