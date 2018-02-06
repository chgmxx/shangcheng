package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.RequestUtils;
import com.gt.mall.constant.Constants;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.util.entity.param.shop.ShopSubsop;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
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
        if ( wxShopId == 0 ) {
            return null;
        }
        String key = Constants.REDIS_KEY + "wx_shop_" + wxShopId;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONObject.toJavaObject( JSONObject.parseObject( obj.toString() ), WsWxShopInfo.class );
            }
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( wxShopId );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "getShopById.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
        }
        return null;
    }

    @Override
    public List< WsShopPhoto > getShopPhotoByShopId( int wxShopId ) {
        if ( wxShopId == 0 ) {
            return null;
        }
        try {
            String key = Constants.REDIS_KEY + "wx_shop_photo_" + wxShopId;
            if ( JedisUtil.exists( key ) ) {
                Object obj = JedisUtil.get( key );
                if ( CommonUtil.isNotEmpty( obj ) ) {
                    return JSONArray.parseArray( obj.toString(), WsShopPhoto.class );
                }
            }
            RequestUtils< Integer > requestUtils = new RequestUtils<>();
            requestUtils.setReqdata( wxShopId );
            String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "getShopPhotoByShopId.do", 2 );
            if ( CommonUtil.isNotEmpty( result ) ) {
                JedisUtil.set( key, result, Constants.REDIS_SECONDS );
                return JSONArray.parseArray( result, WsShopPhoto.class );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List< WsWxShopInfoExtend > queryWxShopByBusId( int busUserId ) {
        if ( busUserId == 0 ) {
            return null;
        }
        String key = Constants.REDIS_KEY + "wx_shop_user_id_" + busUserId;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONArray.parseArray( obj.toString(), WsWxShopInfoExtend.class );
            }
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( busUserId );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "queryWxShopByBusId.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONArray.parseArray( result, WsWxShopInfoExtend.class );
        }
        return null;
    }

    @Override
    public WsWxShopInfo selectMainShopByBusId( int busUserId ) {
        if ( busUserId == 0 ) {
            return null;
        }
        String key = Constants.REDIS_KEY + "wx_shop_" + busUserId;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONObject.toJavaObject( JSONObject.parseObject( obj.toString() ), WsWxShopInfo.class );
            }
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( busUserId );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectMainShopByBusId.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsWxShopInfo.class );
        }
        return null;
    }

    @Override
    public boolean addShopSubShop( ShopSubsop shopSubsop ) {
        if ( CommonUtil.isEmpty( shopSubsop ) ) {
            throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "参数不完整" );
        }
        RequestUtils< ShopSubsop > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( shopSubsop );
        Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, WS_SHOP_URL + "addShopSubShop.do", 2 );
        return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public List< Map > queryCityByParentId( int parentId ) {
        if ( parentId == 0 ) {
            return null;
        }
        String key = Constants.REDIS_KEY + "area_parent_" + parentId;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONArray.parseArray( obj.toString(), Map.class );
            }
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( parentId );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "queryCityByParentId.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONArray.parseArray( result, Map.class );
        }
        return null;
    }

    @Override
    public List< Map > queryCityByLevel( int level ) {
        String key = Constants.REDIS_KEY + "area_" + level;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONArray.parseArray( obj.toString(), Map.class );
            }
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( level );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "queryCityByLevel.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONArray.parseArray( result, Map.class );
        }
        return null;
    }

    @Override
    public List< Map > queryBasisCityIds( String cityIds ) {
        if ( CommonUtil.isEmpty( cityIds ) ) {
            return null;
        }
        String key = Constants.REDIS_KEY + "area_id_" + cityIds;
        if ( JedisUtil.exists( key ) ) {
            Object obj = JedisUtil.get( key );
            if ( CommonUtil.isNotEmpty( obj ) ) {
                return JSONArray.parseArray( obj.toString(), Map.class );
            }
        }
        RequestUtils< String > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( cityIds );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "queryBasisCityIds.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            JedisUtil.set( key, result, Constants.REDIS_SECONDS );
            return JSONArray.parseArray( result, Map.class );
        }
        return null;
    }

    @Override
    public boolean updateBySubShop( ShopSubsop shopSubsop ) {
        RequestUtils< ShopSubsop > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( shopSubsop );
        Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, WS_SHOP_URL + "updateBySubShop.do", 2 );
        return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public Map queryBasisByName( String name ) {
        RequestUtils< String > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( name );
        String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "queryBasisByName.do", 2 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            List< Map > list = JSONArray.parseArray( result, Map.class );
            if ( CommonUtil.isNotEmpty( list ) ) {
                return list.get( 0 );
            }
        }
        return null;
    }

}
