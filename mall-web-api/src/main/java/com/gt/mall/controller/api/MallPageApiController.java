package com.gt.mall.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dao.page.MallPageDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallPage/mallAPI" )
public class MallPageApiController {

    private static Logger logger = LoggerFactory.getLogger( MallStoreApiController.class );

    @Autowired
    private MallPageService  mallPageService;
    @Autowired
    private MallPageDAO      mallPageDAO;
    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private WxShopService    wxShopService;

    /**
     * 获取页面列表
     */
    @ApiOperation( value = "根据商家id获取页面列表", notes = "获取页面列表" )
    @ResponseBody
    @RequestMapping( value = "/pageList", method = RequestMethod.POST )
    public ServerResponse pageList( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	List< Map > pageList = null;
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );
	    if ( CommonUtil.isEmpty( params.get( "userId" ) ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	    }
	    Integer userId = CommonUtil.toInteger( params.get( "userId" ) );
	    List< Map< String,Object > > shoplist = mallStoreService.findShopByUserId( userId, request );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isEmpty( shoplist ) ) {
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), null, false );
	    }
	    List< Integer > shopIds = new ArrayList<>();
	    for ( Map< String,Object > map : shoplist ) {
		shopIds.add( CommonUtil.toInteger( map.get( "id" ) ) );
	    }

	    Wrapper< MallPage > wrapper = new EntityWrapper<>();
	    wrapper.in( "pag_sto_id", shopIds );
	    List< MallPage > list = mallPageService.selectList( wrapper );
	    if ( list != null && list.size() > 0 ) {
		pageList = new ArrayList<>();
		for ( MallPage page : list ) {
		    Map< String,Object > map = new HashMap<>();
		    map.put( "id", page.getId() );
		    map.put( "name", page.getPagName() );
		    map.put( "url", PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + page.getId() );
		    pageList.add( map );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取页面列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), pageList, false );
    }

    /**
     * 根据店铺id查询首页id
     */
    @ApiOperation( value = "根据门店id查询首页id", notes = "根据门店id查询首页id" )
    @ResponseBody
    @RequestMapping( value = "/getPageIdByShopId", method = RequestMethod.POST )
    public ServerResponse getPageIdByShopId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	Integer pageId = 0;
	try {
	    //pageId = mallPageService.getPageIdByShopId( shopId );
	  /*  Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	    storeWrapper.where( "is_delete = 0 and wx_shop_id = {0}", wxShopId );
	    MallStore store = mallStoreService.selectOne( storeWrapper );
	    if ( CommonUtil.isEmpty( store ) ) {
		pageId = 0;
	    }
	    WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
	    if ( shopInfo == null ) {pageId = 0;}
	    if ( shopInfo.getStatus() == -1 ) {pageId = 0;}
*/
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );
	    List< Map< String,Object > > pageList = mallPageDAO.selectPageByWxShopId( params );
	    if ( pageList != null && pageList.size() > 0 ) {
		pageId = CommonUtil.toInteger( pageList.get( 0 ).get( "id" ).toString() );
	    }
	} catch ( Exception e ) {
	    logger.error( "根据门店id查询首页id异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据门店id查询首页id异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), pageId, false );
    }

}
