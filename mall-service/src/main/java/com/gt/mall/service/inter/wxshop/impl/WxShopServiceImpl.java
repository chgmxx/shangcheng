package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wx.shop.ShopPhoto;
import com.gt.mall.bean.wx.shop.ShopSubsop;
import com.gt.mall.bean.wx.shop.WsWxShopInfo;
import com.gt.mall.bean.wx.shop.WsWxShopInfoExtend;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 门店接口的实现类
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 10:18
 */
@Service
public class WxShopServiceImpl implements WxShopService {

    private static final String WS_SHOP_URL = "/8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/";

    @Override
    public WsWxShopInfo getShopById( int wxShopId ) {
	String result = HttpSignUtil.signHttpSelect( wxShopId, WS_SHOP_URL + "getShopById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
	}
	return null;
    }

    @Override
    public List< ShopPhoto > getShopPhotoByShopId( int wxShopId ) {
	String result = HttpSignUtil.signHttpSelect( wxShopId, WS_SHOP_URL + "getShopPhotoByShopId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, ShopPhoto.class );
	}
	return null;
    }

    @Override
    public List< WsWxShopInfoExtend > queryWxShopByBusId( int busUserId ) {
	String result = HttpSignUtil.signHttpSelect( busUserId, WS_SHOP_URL + "queryWxShopByBusId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, WsWxShopInfoExtend.class );
	}
	return null;
    }

    @Override
    public boolean addShopSubShop( ShopSubsop shopSubsop ) {
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( shopSubsop, WS_SHOP_URL + "addShopSubShop.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public List< Map > queryCityByParentId( int parentId ) {
	String result = HttpSignUtil.signHttpSelect( parentId, WS_SHOP_URL + "queryCityByParentId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public List< Map > queryCityByLevel( int level ) {
	String result = HttpSignUtil.signHttpSelect( level, WS_SHOP_URL + "queryCityByLevel.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public List< Map > queryBasisCityIds( String cityIds ) {
	if ( CommonUtil.isEmpty( cityIds ) ) {
	    return null;
	}
	String result = HttpSignUtil.signHttpSelect( cityIds, WS_SHOP_URL + "queryBasisCityIds.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public boolean updateBySubShop( ShopSubsop shopSubsop ) {
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( shopSubsop, WS_SHOP_URL + "updateBySubShop.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

}
