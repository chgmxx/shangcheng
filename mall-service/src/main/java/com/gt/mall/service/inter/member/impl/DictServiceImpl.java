package com.gt.mall.service.inter.member.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.service.inter.member.DictService;
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

    public static final String DICT_URL = "/memberAPI/dict/";//字典链接

    @Override
    public List< Map > getDict( String dictType ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "dictType", dictType );
	String data = HttpSignUtil.SignHttpSelect( params, DICT_URL + "getdict" );
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
}
