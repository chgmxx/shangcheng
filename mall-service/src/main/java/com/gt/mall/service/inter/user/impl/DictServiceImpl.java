package com.gt.mall.service.inter.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
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
	Map< String,Object > params = new HashMap<>();
	params.put( "style", dictType );
	String data = HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDictApi.do", 1 );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject dataJson = JSONObject.parseObject( data );
	    if ( CommonUtil.isNotEmpty( dataJson.get( "dictJSON" ) ) ) {
		return JSONArray.parseArray( dataJson.get( "dictJSON" ).toString(), Map.class );
	    }
	}
	return null;
    }

    @Override
    public String getDictRuturnValue( String dictType, int key ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "dictType", dictType );
	params.put( "key", key );
	String data = HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDictApi.do", 1 );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject dataJson = JSONObject.parseObject( data );
	    if ( CommonUtil.isNotEmpty( dataJson.get( "dictJSON" ) ) ) {
		List< Map > list = JSONArray.parseArray( dataJson.get( "dictJSON" ).toString(), Map.class );
		if ( list != null && list.size() > 0 ) {
		    return list.get( 0 ).get( "item_value" ).toString();
		}
	    }
	}
	return HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDictApi.dood", 1 );
    }

    @Override
    public int getDiBserNum( int userId, int modelStyle, String dictstyle ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", userId );
	params.put( "dictstyle", dictstyle );
	params.put( "modelStyle", modelStyle );
	String result = HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDiBserNum.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( CommonUtil.isNotEmpty( resultObj.get( "dictBusUserNum" ) ) ) {
		return CommonUtil.toInteger( resultObj.get( "dictBusUserNum" ) );
	    }
	}
	return 0;
    }
}
