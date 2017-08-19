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

    public static final String DICT_URL = "/8A5DA52E/dictApi/";//字典链接

    @Override
    public List< Map > getDict( String dictType ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "dictType", dictType );
	String data = HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDictApi.do" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONArray.parseArray( data, Map.class );
	}
	return null;
    }

    @Override
    public String getDictRuturnValue( String dictType, int key ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "dictType", dictType );
	params.put( "key", key );
	return HttpSignUtil.SignHttpSelect( params, DICT_URL + "getDictRuturnValue" );
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
