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

import java.util.HashMap;
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
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", wxShopId );
	String result = HttpSignUtil.SignHttpSelect( wxShopId, WS_SHOP_URL + "getShopById.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
	}
	return null;
    }

    @Override
    public List< ShopPhoto > getShopPhotoByShopId( int wxShopId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", wxShopId );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "getShopPhotoByShopId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, ShopPhoto.class );
	}
	return null;
    }

    @Override
    public List< WsWxShopInfoExtend > queryWxShopByBusId( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", busUserId );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "queryWxShopByBusId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, WsWxShopInfoExtend.class );
	}
	return null;
    }

    @Override
    public boolean addShopSubShop( ShopSubsop shopSubsop ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", shopSubsop );
	Map< String,Object > resultMap = HttpSignUtil.SignHttpInsertOrUpdate( params, WS_SHOP_URL + "addShopSubShop.do", 1 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public List< Map > queryCityByParentId( int parentId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", parentId );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "queryCityByParentId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public List< Map > queryCityByLevel( int level ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", level );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "queryCityByLevel.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public List< Map > queryBasisCityIds( String cityIds ) {
        if(CommonUtil.isEmpty( cityIds )){
            return null;
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", cityIds.split( "," ) );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "queryBasisCityIds.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }
}
