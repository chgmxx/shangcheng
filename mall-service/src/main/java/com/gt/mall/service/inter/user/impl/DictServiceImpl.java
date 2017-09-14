package com.gt.mall.service.inter.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.constant.Constants;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.mall.utils.JedisUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典接口实现类
 * User : yangqian
 * Date : 2017/8/16 0016
 * Time : 10:30
 */
@Service
public class DictServiceImpl implements DictService {

    private static final String DICT_URL = "/8A5DA52E/dictApi/";//字典链接

    @Override
    public List< Map > getDict( String dictType ) {
	String key = Constants.REDIS_KEY + "dict_type_" + dictType;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return JSONArray.parseArray( obj.toString(), Map.class );
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "style", dictType );
	String data = HttpSignUtil.signHttpSelect( params, DICT_URL + "getDictApi.do", 1 );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject dataJson = JSONObject.parseObject( data );
	    if ( CommonUtil.isNotEmpty( dataJson.get( "dictJSON" ) ) ) {
		JedisUtil.set( key, dataJson.get( "dictJSON" ).toString(), Constants.REDIS_SECONDS );
		return JSONArray.parseArray( dataJson.get( "dictJSON" ).toString(), Map.class );
	    }
	}
	return null;
    }

    @Override
    public String getDictRuturnValue( String dictType, int key ) {
	String keys = Constants.REDIS_KEY + "dict_type_key_" + dictType + "_" + key;
	if ( JedisUtil.exists( keys ) ) {
	    Object obj = JedisUtil.get( keys );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return obj.toString();
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "dictType", dictType );
	params.put( "key", key );
	String data = HttpSignUtil.signHttpSelect( params, DICT_URL + "getDictApi.do", 1 );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject dataJson = JSONObject.parseObject( data );
	    if ( CommonUtil.isNotEmpty( dataJson.get( "dictJSON" ) ) ) {
		List< Map > list = JSONArray.parseArray( dataJson.get( "dictJSON" ).toString(), Map.class );
		if ( list != null && list.size() > 0 ) {
		    String value = list.get( 0 ).get( "item_value" ).toString();
		    JedisUtil.set( keys, value, Constants.REDIS_SECONDS );
		    return value;
		}
	    }
	}
	return HttpSignUtil.signHttpSelect( params, DICT_URL + "getDictApi.dood", 1 );
    }

    @Override
    public int getDiBserNum( int userId, int modelStyle, String dictstyle ) {
	String key = Constants.REDIS_KEY + "dict_num_" + userId + "_" + modelStyle + "_" + dictstyle;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return CommonUtil.toInteger( obj );
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", userId );
	params.put( "dictstyle", dictstyle );
	params.put( "modelStyle", modelStyle );
	String result = HttpSignUtil.signHttpSelect( params, DICT_URL + "getDiBserNum.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( CommonUtil.isNotEmpty( resultObj.get( "dictBusUserNum" ) ) ) {
		JedisUtil.set( key, resultObj.get( "dictBusUserNum" ).toString(), Constants.REDIS_SECONDS );
		return CommonUtil.toInteger( resultObj.get( "dictBusUserNum" ) );
	    }
	}
	return 0;
    }
}
