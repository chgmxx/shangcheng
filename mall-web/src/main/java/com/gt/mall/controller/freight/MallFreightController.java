package com.gt.mall.controller.freight;

import com.gt.mall.annotation.CommAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.basic.MallTakeTheirService;
import com.gt.mall.web.service.freight.MallFreightService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
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
 * 物流表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallFreight/" )
public class MallFreightController extends BaseController {

    @Autowired
    private MallStoreService     storeService;
    @Autowired
    private MallFreightService   freightService;
    @Autowired
    private MallTakeTheirService takeTheirService;
    @Autowired
    private MallPaySetService    paySetService;

    @CommAnno( menu_url = "mFreight/start.do" )
    @RequestMapping( "start" )
    public String start( HttpServletRequest request, HttpServletResponse response ) {

	request.setAttribute( "iframe_url", "mFreight/index.do" );
	request.setAttribute( "title", "物流管理" );
	return "iframe";
    }

    /**
     * 进入物流管理列表页面
     *
     * @Title: index
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    if ( user != null ) {
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
			params.put( "shoplist", shoplist );
			PageUtil freightPage = freightService.selectFreightByShopId( shoplist, params );
			request.setAttribute( "page", freightPage );
		    }

		    request.setAttribute( "shoplist", shoplist );
		}
	    }
	    //TODO 需关连VoiceCourseService 方法
	    //	    request.setAttribute("videourl", course.urlquery("80"));
	} catch ( Exception e ) {
	    logger.error( "物流管理列表: " + e );
	    e.printStackTrace();
	}
	return "mall/freight/freight_index";
    }

    @CommAnno( menu_url = "mFreight/start.do" )
    @RequestMapping( "edit" )
    public String freightEdit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	String url = "mFreight/to_edit.do";
	if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
	    url += "?id=" + params.get( "id" ).toString();
	}
	request.setAttribute( "iframe_url", url );
	request.setAttribute( "title", "物流管理-编辑物流" );
	return "iframe";
    }

    /**
     * 进入修改物流信息页面
     *
     * @Title: toEditFreight
     */
    @RequestMapping( "to_edit" )
    public String toEditFreight( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shopList = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    int id = 0;
	    // 查询物流信息
	    if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
		id = CommonUtil.toInteger( params.get( "id" ) );
		MallFreight freight = freightService.selectFreightById( id );
		if ( freight != null ) {
		    request.setAttribute( "freight", freight );
		}
	    }
	    // 查询会员下面的所有店铺
	    if ( shopList != null && shopList.size() > 0 ) {
		if ( id == 0 ) {
		    List< MallFreight > freightList = freightService.getByShopId( shopList );
		    if ( freightList != null && freightList.size() > 0 ) {
			for ( MallFreight mallFreight : freightList ) {
			    for ( int i = 0; i < shopList.size(); i++ ) {
				Map< String,Object > map = shopList.get( i );
				if ( mallFreight.getShopId().toString().equals( map.get( "id" ).toString() ) ) {
				    shopList.remove( i );
				}
			    }
			}
		    }
		}
		request.setAttribute( "shopList", shopList );
	    }

	    //查询物流公司
	    //TODO 字典  dictService.getDict("1092");
	    //	    SortedMap<String, Object> comMap = dictService.getDict("1092");
	    //	    request.setAttribute("comMap", comMap);

	} catch ( Exception e ) {
	    logger.error( "修改物流信息:" + e );
	    e.printStackTrace();
	}
	return "mall/freight/freight_edit";
    }

    /**
     * 编辑物流信息
     *
     * @Title: editFreight
     */
    @SysLogAnnotation( description = "物流管理-添加物流", op_function = "2" )
    @RequestMapping( "editFreight" )
    public void editFreight( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入编辑物流信息的controller" );
	boolean flag = false;
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		// 编辑物流信息
		flag = freightService.editFreight( params, user.getId() );
	    }
	} catch ( Exception e ) {
	    flag = false;
	    logger.debug( "编辑物流信息：" + e.getMessage() );
	    e.printStackTrace();
	}

	JSONObject obj = new JSONObject();
	try {
	    obj.put( "flag", flag );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( JSONException e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除物流信息
     *
     * @Title: deleteFreight
     */
    @SysLogAnnotation( description = "物流管理-删除物流", op_function = "4" )
    @RequestMapping( "deleteFreight" )
    public void deleteFreight( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除物流信息的controller" );
	boolean flag = false;
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    String[] id = (String[]) JSONArray.toArray( JSONArray.fromObject( params.get( "id" ).toString() ), String.class );
	    params.put( "ids", id );
	    // 删除物流信息
	    flag = freightService.deleteFreight( params );
	} catch ( Exception e ) {
	    flag = false;
	    logger.debug( "删除物流信息：" + e.getMessage() );
	    e.printStackTrace();
	}finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "flag", flag );

	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 查询退款信息
     *
     * @param request
     * @param map
     *
     * @return
     */
    @RequestMapping( value = "/provincePopUp" )
    public String provincePopUp( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > map ) {
	//TODO 地区 areaService.getAllProvince();
	List< Map< Integer,String > > provinceList = null;
	//			areaService.getAllProvince();
	request.setAttribute( "provinceList", provinceList );
	request.setAttribute( "index", map.get( "index" ) );
	request.setAttribute( "selectPro", map.get( "selectPro" ) );
	request.setAttribute( "hidePro", map.get( "hidePro" ) );
	return "mall/freight/provincePopUp";
    }

    /**
     * 通过省份id来获取运费
     *
     * @Title: deleteFreight
     */
    @RequestMapping( "79B4DE7C/getFreightByProvinceId" )
    public void getFreightByProvinceId( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入通过省份id来获取运费的controller" );
	Map< Integer,Object > map = new HashMap< Integer,Object >();
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    if ( CommonUtil.isNotEmpty( params.get( "province" ) ) ) {
		map = freightService.getFreightMoney( params );

		request.getSession().setAttribute( Constants.SESSION_KEY + "province", params.get( "province" ) );
	    }
	} catch ( Exception e ) {
	    logger.debug( "通过省份id来获取运费：" + e.getMessage() );
	    e.printStackTrace();
	}

	JSONObject obj = new JSONObject();
	try {
	    obj.put( "maps", map );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( JSONException e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 进入上门自提列表页面
     *
     * @Title: takeindex
     */
    @RequestMapping( "takeindex" )
    public String takeindex( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    if ( user != null ) {
		params.put( "userId", user.getId() );
		PageUtil page = takeTheirService.selectByUserId( params );
		request.setAttribute( "page", page );

		MallPaySet paySet = new MallPaySet();
		paySet.setUserId( user.getId() );
		MallPaySet set = paySetService.selectByUserId( paySet );
		request.setAttribute( "set", set );
	    }
	} catch ( Exception e ) {
	    logger.error( "上门自提列表:" + e );
	    e.printStackTrace();
	}
	return "mall/take/take_index";
    }

    /**
     * 进入修改上门自提
     *
     * @Title: take_edit
     */
    @RequestMapping( "take_edit" )
    public String take_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    // 查询物流信息
	    if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
		params.put( "userId", user.getId() );
		MallTakeTheir take = takeTheirService.selectById( params );
		if ( take != null ) {
		    request.setAttribute( "take", take );
		}
	    } else {
		//TODO 默认地址 userService.findUserDefArea
		Map< String,Object > defArea = null;
		//				userService.findUserDefArea(user.getId());
		if ( defArea.size() > 0 ) {
		    MallTakeTheir take = new MallTakeTheir();
		    take.setVisitAddress( defArea.get( "detail_address" ).toString() );
		    take.setVisitLatitude( defArea.get( "latitude" ).toString() );
		    take.setVisitLongitude( defArea.get( "longitude" ).toString() );
		    take.setVisitProvinceId( CommonUtil.toInteger( defArea.get( "province_id" ) ) );
		    if ( CommonUtil.isEmpty( defArea.get( "city_id" ) ) ) {
			defArea.put( "city_id", defArea.get( "district_id" ) );
		    }
		    take.setVisitCityId( CommonUtil.toInteger( defArea.get( "city_id" ) ) );
		    if ( CommonUtil.isNotEmpty( defArea.get( "district_id" ) ) ) {
			take.setVisitAreaId( CommonUtil.toInteger( defArea.get( "district_id" ) ) );
		    }
		    request.setAttribute( "take", take );
		}
		request.setAttribute( "defArea", defArea );
	    }

	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    //TODO 下级城市集合  restaurantService.findCityByPid
	    //	    request.setAttribute("areaLs", restaurantService.findCityByPid(restaurantService.getAreaIds()));
	} catch ( Exception e ) {
	    logger.error( "进入编辑上门自提失败：" + e );
	    e.printStackTrace();
	}
	return "mall/take/take_edit";
    }

    /**
     * 编辑上门自提
     *
     * @Title: editTake
     */
    @SysLogAnnotation( description = "上门自提-编辑上门自提", op_function = "2" )
    @RequestMapping( "editTake" )
    public void editTake( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入编辑上门自提的controller" );
	boolean flag = false;
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		// 编辑上门自提
		flag = takeTheirService.editTake( params, user );
	    }
	} catch ( Exception e ) {
	    flag = false;
	    logger.debug( "编辑上门自提：" + e.getMessage() );
	    e.printStackTrace();
	}

	JSONObject obj = new JSONObject();
	try {
	    obj.put( "flag", flag );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( JSONException e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除上门自提
     *
     * @Title: deleteTake
     */
    //    @SysLogAnnotation(description = "上门自提-删除上门自提", op_function = "4")
    @RequestMapping( "deleteTake" )
    public void deleteTake( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入删除上门自提的controller" );
	boolean flag = false;
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    // 删除上门自提
	    flag = takeTheirService.deleteTake( params );
	} catch ( Exception e ) {
	    flag = false;
	    logger.debug( "删除上门自提：" + e.getMessage() );
	    e.printStackTrace();
	}

	JSONObject obj = new JSONObject();
	try {
	    obj.put( "flag", flag );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( JSONException e ) {
	    e.printStackTrace();
	}
    }
}
