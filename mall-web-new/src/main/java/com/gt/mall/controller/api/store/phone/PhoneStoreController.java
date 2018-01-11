package com.gt.mall.controller.api.store.phone;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.util.entity.result.shop.WsShopPhoto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 店铺手机端接口
 * User : yangqian
 * Date : 2017/12/18 0018
 * Time : 18:00
 */
@Api( value = "phoneStore", description = "店铺相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@RestController
@CrossOrigin
@RequestMapping( value = "/phoneStore/L6tgXlBFeK/" )
public class PhoneStoreController {

    private static Logger logger = LoggerFactory.getLogger( PhoneStoreController.class );

    @Autowired
    private MallStoreService   mallStoreService;
    @Autowired
    private MallProductService mallProductService;
    @Autowired
    private WxShopService      wxShopService;

    @ApiOperation( value = "根据商家id获取粉币门店列表", notes = "根据商家id获取粉币门店" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/queryFenbiShopByBusId", method = RequestMethod.POST )
    public ServerResponse selectFenbiShopList( HttpServletRequest request, HttpServletResponse response, Integer busId ) {
	List< Map< String,Object > > fenbiShopList = null;
	try {
	    Wrapper< MallProduct > prowrapper = new EntityWrapper<>();
	    prowrapper.setSqlSelect( "DISTINCT shop_id AS shopId" );
	    prowrapper.where( "user_id={0} AND is_delete = 0 AND is_fenbi_change_pro=1 AND change_fenbi>0  AND is_publish=1 AND check_status=1 ", busId );
	    fenbiShopList = mallProductService.selectMaps( prowrapper );
	    if ( fenbiShopList == null || fenbiShopList.size() == 0 ) {
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), fenbiShopList, false );
	    }
	    List< Map< String,Object > > shopList = mallStoreService.findByUserId( busId );
	    if ( shopList != null && shopList.size() > 0 ) {
		for ( Map< String,Object > map : fenbiShopList ) {
		    for ( Map< String,Object > shopInfo : shopList ) {
			if ( CommonUtil.toString( map.get( "shopId" ) ).equals( CommonUtil.toString( shopInfo.get( "id" ) ) ) ) {
			    map.remove( "shopId" );
			    map.put( "id", shopInfo.get( "id" ) );
			    map.put( "shopName", shopInfo.get( "shopName" ) );
			    map.put( "address", shopInfo.get( "sto_address" ) );
			    //			    map.put( "url", PropertiesUtil.getPhoneWebHomeUrl() + "classify/" + shopInfo.get( "id" ) + "/" + busId + "/5/k=k" );
			    break;
			}
		    }
		}
	    }
	    for ( Map< String,Object > map : fenbiShopList ) {
		if ( !map.containsKey( "shopName" ) ) {
		    fenbiShopList.remove( map );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "根据商家id获取粉币门店异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据商家id获取粉币门店异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), fenbiShopList, false );
    }

    @ApiOperation( value = "获取商家的门店列表", notes = "获取商家的门店列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "shopList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse shopList( HttpServletRequest request, int busId ) {
	try {
	    BusUser user = new BusUser();
	    user.setId( busId );
	    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
	    if ( shopList != null && shopList.size() > 0 ) {

		for ( Map< String,Object > shopMap : shopList ) {
		    //获取门店图片
		    List< WsShopPhoto > imageList = wxShopService.getShopPhotoByShopId( CommonUtil.toInteger( shopMap.get( "wxShopId" ) ) );
		    if ( imageList != null && imageList.size() > 0 ) {
			shopMap.put( "stoPicture", imageList.get( 0 ).getLocalAddress() );
		    }
		    shopMap.remove( "wxShopId" );
		    shopMap.remove( "stoLongitude" );
		    shopMap.remove( "stoLatitude" );
		}
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), shopList, true );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商家的门店列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商家的门店列表失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "该商家没有门店列表" );
    }
}
