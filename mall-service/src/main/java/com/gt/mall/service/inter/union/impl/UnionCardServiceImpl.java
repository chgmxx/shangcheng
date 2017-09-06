package com.gt.mall.service.inter.union.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用联盟卡实现类
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:34
 */
@Service
public class UnionCardServiceImpl implements UnionCardService {

    private static final String url = "/api/card/8A5DA52E/";

    @Override
    public Map consumeUnionDiscount( int busUserId ) {
	RequestUtils requestUtils = new RequestUtils();
	requestUtils.setReqdata( busUserId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, url + "consumeUnionDiscount" );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), Map.class );
	}
	return null;
    }

    @Override
    public String phoneCode( String phone ) {
	RequestUtils requestUtils = new RequestUtils();
	requestUtils.setReqdata( phone );
	String result = HttpSignUtil.signHttpSelect( requestUtils, url + "uionCardBind" );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return result;
	}
	return null;
    }

    @Override
    public boolean uionCardBind( String phone, String code ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "phone", phone );
	params.put( "code", code );
	RequestUtils requestUtils = new RequestUtils();
	requestUtils.setReqdata( params );
	Map< String,Object > result = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, url + "uionCardBind" );
	return result.get( "code" ).toString().equals( "1" );
    }

}
