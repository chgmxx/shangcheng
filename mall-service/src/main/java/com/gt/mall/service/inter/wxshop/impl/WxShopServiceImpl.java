package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.wxshop.ShopPhoto;
import com.gt.mall.bean.wxshop.WsWxShopInfo;
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

    private static final String WS_SHOP_URL = "/8A5DA52E/shopapi/79B4DE7C/";

    @Override
    public WsWxShopInfo getShopById( int wxShopId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", wxShopId );
	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "getShopById.do", 1 );
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
}
