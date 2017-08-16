package com.gt.mall.inter.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.inter.service.DictService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MemberInterUtil;
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
	String data = MemberInterUtil.SignHttpSelect( params, DICT_URL + "getdict" );
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
	return MemberInterUtil.SignHttpSelect( params, DICT_URL + "getDictRuturnValue" );
    }
}
