package com.gt.mall.controller.seckill;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.util.*;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.seckill.MallSeckillService;
import com.gt.mall.web.service.store.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 秒杀表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mSeckill" )
public class MallSeckillController extends BaseController {
    @Autowired
    private MallStoreService   mallStoreService;
    @Autowired
    private MallSeckillService mallSeckillService;

    @Autowired
    private MallPageService   mallPageService;
    @Autowired
    private MallPaySetService mallPaySetService;

    /**
     * 秒杀管理列表页面
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = true; //是管理员
	    if ( CommonUtil.isNotEmpty( user.getPid() ) && user.getPid() > 0 ) {
		isAdminFlag = mallStoreService.isAdminUser( user.getId() );//查询子账户是否是管理员

		if ( !isAdminFlag ) {
		    request.setAttribute( "isNoAdminFlag", 1 );
		}
	    }
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = mallSeckillService.selectSeckillByShopId( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    //todo 调用陈丹接口  视频教程   course.urlquery
	    //request.setAttribute("videourl", course.urlquery("82"));
	} catch ( Exception e ) {
	    logger.error( "秒杀列表异常：" + e );
	    e.printStackTrace();
	}

	return "mall/seckill/seckill_index";
    }

    /**
     * 进入秒杀编辑页面
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据秒杀id查询秒杀信息
		Map< String,Object > groupMap = mallSeckillService.selectSeckillById( id );
		if ( groupMap != null ) {
		    Object imageUrl = groupMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = groupMap.get( "imageUrl" );
		    }
		    groupMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "seckill", groupMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入秒杀编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/seckill/seckill_edit";
    }

    /**
     * 编辑秒杀
     */
    @SysLogAnnotation( description = "秒杀管理-编辑秒杀", op_function = "2" )
    @RequestMapping( "edit_seckill" )
    public void editSeckill( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑辑秒杀controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallSeckillService.editSeckill( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑秒杀：" + e );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}

    }

    /**
     * 删除秒杀
     */
    @SysLogAnnotation( description = "秒杀管理-删除秒杀", op_function = "4" )
    @RequestMapping( "group_remove" )
    public void removeGroup( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除秒杀controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) && !CommonUtil.isEmpty( params ) ) {
		int id = 0;
		if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		    id = CommonUtil.toInteger( params.get( "id" ) );
		}
		MallSeckill Seckill = new MallSeckill();
		Seckill.setId( id );
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    int type = CommonUtil.toInteger( params.get( "type" ) );
		    if ( type == -1 ) {// 删除
			Seckill.setIsDelete( 1 );
		    } else if ( type == -2 ) {// 使失效秒杀
			Seckill.setIsUse( -1 );
		    }
		}
		boolean flag = mallSeckillService.deleteSeckill( Seckill );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除秒杀：" + e );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}

    }

    /**
     * 获取店铺下所有的秒杀（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/seckillall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String seckillall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    Map< String,Object > mapuser = mallPageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = mallPageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    //todo 调用彭江丽接口   UC登陆  userLogin
	    /*String returnUrl = userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }*/

	    boolean isShop = mallPageService.wxShopIsDelete( shopid );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();// 图片url链接前缀
	    List groupList = mallPageService.storeList( shopid, 1, 0 );// 获取分类
	    String pageid = "0";
	    List list1 = mallPageService.shoppage( shopid );// 获取商品主页
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";// 查询条件
	    String desc = "1";// 排序 默认倒序
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = params.get( "type" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    params.put( "shopId", shopid );
	    List< Map< String,Object > > productList = mallSeckillService
			    .getSeckillAll( member, params );// 查询店铺下所有加入秒杀的商品

	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		request.getSession().setAttribute( "shopId", shopid );
	    } else {
		if ( !request.getSession().getAttribute( "shopId" ).toString().equals( String.valueOf( shopid ) ) ) {
		    request.getSession().setAttribute( "shopId", shopid );
		}
	    }
	    //查询搜索标签，历史记录
	    mallPageService.getSearchLabel( request, shopid, member, params );

	    request.setAttribute( "shopId", shopid );
	    request.setAttribute( "pageid", pageid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "groupList", groupList );
	    request.setAttribute( "imgHttp", http );
	    request.setAttribute( "productList", productList );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    mallPageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入秒杀商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/seckill/phone/seckillall";
    }

    @RequestMapping( "79B4DE7C/getSeckillByJedis" )
    public void getSeckillByJedis( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	Map< String,String > map = new HashMap<>();
	try {
	    String key = "hSeckill";
	    if ( JedisUtil.exists( key ) ) {
		map.put( "seckill", JedisUtil.mapGetAll( key ).toString() );
	    } else {
		map.put( "msg", "没有获取到seckill的信息" );
	    }
	    if ( JedisUtil.exists( "hSeckill_nopay" ) ) {
		map.put( "nopay", JedisUtil.mapGetAll( "hSeckill_nopay" ).toString() );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "map", map );
	    CommonUtil.write( response, obj );
	}

    }

    @RequestMapping( "79B4DE7C/loadSeckill" )
    public void loadSeckill( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	Map< String,Object > map = new HashMap<>();
	try {
	    mallSeckillService.loadSeckill();
	    map.put( "flag", true );
	    map.put( "msg", "初始化成功" );
	} catch ( Exception e ) {
	    map.put( "flag", false );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "map", map );
	    CommonUtil.write( response, obj );
	}

    }
}
