package com.gt.mall.controller.presale;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.presale.MallPresaleGive;
import com.gt.mall.util.*;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.presale.MallPresaleDepositService;
import com.gt.mall.web.service.presale.MallPresaleService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mPresale" )
public class MallPresaleController extends BaseController {
    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallPresaleService        mallPresaleService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private MallPaySetService         mallPaySetService;

    /**
     * 预售管理列表页面
     *
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = true;//是管理员
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
		    PageUtil page = mallPresaleService.selectPresaleByShopId( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }

	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( user.getId() );
	    //通过商品id查询预售信息
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    boolean isOpenPresale = false;
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsPresale() ) ) {
		    if ( set.getIsPresale().toString().equals( "1" ) ) {
			isOpenPresale = true;
		    }
		}
	    }
	    request.setAttribute( "isOpenPresale", isOpenPresale );
	    //todo course.urlquery
//	    request.setAttribute( "videourl", course.urlquery( "83" ) );
	} catch ( Exception e ) {
	    logger.error( "预售列表异常：" + e );
	    e.printStackTrace();
	}

	return "mall/presale/presale_index";
    }

    /**
     * 进入预售编辑页面
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据预售id查询预售信息
		Map< String,Object > presaleMap = mallPresaleService.selectPresaleById( id );
		if ( presaleMap != null ) {
		    Object imageUrl = presaleMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = presaleMap.get( "imageUrl" );
		    }
		    presaleMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "presale", presaleMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "returnUrl", request.getHeader( "Referer" ) );
	} catch ( Exception e ) {
	    logger.error( "进入预售编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/presale/presale_edit";
    }

    /**
     * 编辑预售
     */
    @SysLogAnnotation( description = "预售管理-编辑预售", op_function = "2" )
    @RequestMapping( "edit_presale" )
    public void editPresale( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	logger.info( "进入编辑辑预售controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPresaleService.editPresale( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑预售：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	try {
	    obj.put( "code", code );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除预售
     */
    @SysLogAnnotation( description = "预售管理-删除预售", op_function = "4" )
    @RequestMapping( "presale_remove" )
    public void removeGroup( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除预售controller" );
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
		MallPresale presale = new MallPresale();
		presale.setId( id );
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    int type = CommonUtil.toInteger( params.get( "type" ) );
		    if ( type == -1 ) {// 删除
			presale.setIsDelete( 1 );
		    } else if ( type == -2 ) {// 使失效预售
			presale.setIsUse( -1 );
		    }
		}
		boolean flag = mallPresaleService.deletePresale( presale );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除预售：" + e );
	    e.printStackTrace();
	}finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response,obj );
	}
    }

    /**
     * 拍卖定金管理列表页面
     */
    @RequestMapping( "deposit" )
    public String deposit( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = mallPresaleDepositService.selectPresaleByShopId( params );
		request.setAttribute( "page", page );
		request.setAttribute( "shoplist", shoplist );
	    }
	    request.setAttribute( "type", params.get( "type" ) );
	} catch ( Exception e ) {
	    logger.error( "拍卖定金列表：" + e );
	    e.printStackTrace();
	}

	return "mall/presale/presale_deposit";
    }

    /**
     * 进入预售设置页面
     *
     * @return
     */
    @RequestMapping( "presale_set" )
    public String presale_set( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    List< MallPresaleGive > giveList = mallPresaleService.selectGiveByUserId( user );

	    //todo 陈丹字典接口 dictService.getDictList
	    //	    List<Map<String, Object>> list = dictService.getDictList("1143");
	    //	    request.setAttribute("dictList", list);

	    request.setAttribute( "giveList", giveList );
	    request.setAttribute( "shoplist", shoplist );

	} catch ( Exception e ) {
	    logger.error( "进入预售设置页面异常：" + e );
	    e.printStackTrace();
	}

	return "mall/presale/presale_set";
    }

    /**
     * 编辑预售设置
     */
    @SysLogAnnotation( description = "预售管理-编辑预售设置", op_function = "2" )
    @RequestMapping( "edit_presale_set" )
    public void editPresaleSet( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入编辑辑预售设置controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = mallPresaleService.editPresaleSet( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑预售设置异常：" + e );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "code", code );
	    CommonUtil.write( response, obj );
	}
    }

    @RequestMapping( "79B4DE7C/getPresaleByJedis" )
    public void getSeckillByJedis( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	PrintWriter p = null;
	Map< String,String > map = new HashMap<>();
	try {
	    String key = Constants.REDIS_KEY + "presale_num";
	    if ( JedisUtil.exists( key ) ) {
		map.put( "presale_num", JedisUtil.mapGetAll( key ).toString() );
	    } else {
		map.put( "msg", "没有获取到mall:presale_num的信息" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "map", map );
	    CommonUtil.write( response, obj );
	}

    }

    @RequestMapping( "79B4DE7C/loadPresale" )
    public void loadSeckill( HttpServletRequest request,
		    HttpServletResponse response ) throws Exception {
	Map< String,Object > map = new HashMap<>();
	try {
	    mallPresaleService.loadPresaleByJedis( null );
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

    /**
     * 进入退定金的页面
     */
    @RequestMapping( "returnPresalePopUp" )
    public String returnPresalePopUp( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	try {
	    int depositId = CommonUtil.toInteger( params.get( "depositId" ) );
	    BusUser user = SessionUtils.getLoginUser( request );

	    MallPresaleDeposit deposit = mallPresaleDepositService.selectByDeposit( depositId );
	    request.setAttribute( "deposit", deposit );

	    request.setAttribute( "busUserId", user.getId() );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );

	} catch ( Exception e ) {
	    logger.error( "进入退定金的页面异常：" + e );
	    e.printStackTrace();
	}

	return "mall/presale/returnDepositPopUp";
    }

    /**
     * 退保证金
     */
    @RequestMapping( value = "/agreedReturnDeposit" )
    @SysLogAnnotation( op_function = "3", description = "退定金" )
    public void agreedReturnDeposit( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	PrintWriter out = null;
	Map< String,Object > result = new HashMap<>();
	try {
	    out = response.getWriter();

	    int depositId = CommonUtil.toInteger( params.get( "depositId" ) );

	    MallPresaleDeposit deposit = mallPresaleDepositService.selectByDeposit( depositId );

	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", deposit.getId() );
	    map.put( "user_id", deposit.getUserId() );
	    map.put( "pay_way", deposit.getPayWay() );
	    map.put( "deposit_money", deposit.getDepositMoney() );
	    map.put( "deposit_no", deposit.getDepositNo() );
	    result = mallPresaleDepositService.returnEndPresale( map );
	    if ( !result.containsKey( "result" ) ) {
		result.put( "result", true );
	    }
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "退预售定金失败，稍后请重新提交" );
	    logger.error( "退预售定金异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	out.write( JSONObject.fromObject( result ).toString() );
	out.flush();
	out.close();
    }
}
