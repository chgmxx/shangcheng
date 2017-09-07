package com.gt.mall.service.inter.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 粉丝收货地址的接口的实现类
 * User : yangqian
 * Date : 2017/9/7 0007
 * Time : 10:46
 */
@Service
public class MemberAddressServiceImpl implements MemberAddressService {

    public static final String url = "/8A5DA52E/fanAddress/";

    @Override
    public Map addressDefault( String memberIds ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberids", memberIds );
	String result = HttpSignUtil.signHttpSelect( params, url + "addressDefault.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), Map.class );
	}
	return null;
    }

    @Override
    public Map addreSelectId( String addresssId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "addid", addresssId );
	String result = HttpSignUtil.signHttpSelect( params, url + "addreSelectId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), Map.class );
	}
	return null;
    }
}
