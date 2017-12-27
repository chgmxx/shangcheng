package com.gt.mall.controller.api;

import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.store.MallStoreService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商城店铺相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallStore/mallAPI" )
public class MallStoreApiController {

    private static Logger logger = LoggerFactory.getLogger( MallStoreApiController.class );

    @Autowired
    private MallStoreService mallStoreService;

    @ApiOperation( value = "查询门店是否开通商城并拥有页面", notes = "查询门店是否开通商城并拥有页面" )
    @ResponseBody
    @RequestMapping( value = "/shopIsOpenMall", method = RequestMethod.POST )
    public ServerResponse shopIsOpenMall( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "wxShopId", value = "门店id", required = true ) @RequestParam Integer wxShopId ) {
	Boolean flag = true;
	try {
	    flag = mallStoreService.shopIsOpenMall( wxShopId );
	} catch ( Exception e ) {
	    logger.error( "查询门店是否开通商城异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询门店是否开通商城异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flag, false );
    }

    //
    //    @ApiOperation( value = "根据商家id获取粉币门店列表", notes = "根据商家id获取粉币门店" )
    //    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家ID", paramType = "query", required = true, dataType = "int" ) } )
    //    @ResponseBody
    //    @RequestMapping( value = "/queryFenbiShopByBusId", method = RequestMethod.POST )
    //    public ServerResponse selectFenbiShopList( HttpServletRequest request, HttpServletResponse response, Integer busId ) {
    //	List< Map< String,Object > > fenbiShopList = null;
    //	try {
    //	    Wrapper< MallProduct > prowrapper = new EntityWrapper<>();
    //	    prowrapper.setSqlSelect( "DISTINCT shop_id AS shopId" );
    //	    prowrapper.where( "user_id={0} AND is_delete = 0 AND is_fenbi_change_pro=1 AND change_fenbi>0  AND is_publish=1 AND check_status=1 ", busId );
    //	    fenbiShopList = mallProductDAO.selectMaps( prowrapper );
    //	    if ( fenbiShopList == null || fenbiShopList.size() == 0 ) {
    //		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), fenbiShopList, false );
    //	    }
    //	    List< Map< String,Object > > shopList = mallStoreService.findByUserId( busId );
    //	    if ( shopList != null && shopList.size() > 0 ) {
    //		for ( Map< String,Object > map : fenbiShopList ) {
    //		    for ( Map< String,Object > shopInfo : shopList ) {
    //			if ( CommonUtil.toInteger( map.get( "shopId" ) ) == CommonUtil.toInteger( shopInfo.get( "id" ) ) ) {
    //			    map.remove( "shopId" );
    //			    map.put( "id", shopInfo.get( "id" ) );
    //			    map.put( "shopName", shopInfo.get( "shopName" ) );
    //			    map.put( "stoAddress", shopInfo.get( "sto_address" ) );
    //			    map.put( "url", PropertiesUtil.getPhoneWebHomeUrl() + "classify/" + shopInfo.get( "id" ) + "/" + busId + "/5/k=k" );
    //			    break;
    //			}
    //		    }
    //		}
    //	    }
    //	    for ( Map< String,Object > map : fenbiShopList ) {
    //		if ( !map.containsKey( "shopName" ) ) {
    //		    fenbiShopList.remove( map );
    //		}
    //	    }
    //	} catch ( Exception e ) {
    //	    logger.error( "根据商家id获取粉币门店异常：" + e.getMessage() );
    //	    e.printStackTrace();
    //	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据商家id获取粉币门店异常" );
    //	}
    //	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), fenbiShopList, false );
    //    }

}
