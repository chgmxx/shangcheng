package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.wxshop.QrcodeCreateFinal;
import com.gt.mall.bean.wxshop.SendWxMsgTemplate;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号接口实现类
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 17:16
 */
@Service
public class WxPublicUserServiceImpl implements WxPublicUserService {

    private static final String WS_SHOP_URL = "/8A5DA52E/wxpublicapi/79B4DE7C/";

    @Override
    public WxPublicUsers selectByUserId( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", busUserId );

	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "selectByUserId", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectById( int id ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", id );

	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "selectById", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectByMemberId( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", memberId );

	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "selectByMemberId", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public String qrcodeCreateFinal( QrcodeCreateFinal createFinal ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", createFinal );

	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "qrcodeCreateFinal", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return result;
	}
	return null;
    }

    @Override
    public boolean sendWxMsgTemplate( SendWxMsgTemplate template ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", template );
	Map< String,Object > resultMap = HttpSignUtil.SignHttpInsertOrUpdate( params, WS_SHOP_URL + "sendWxMsgTemplate", 1 );
	if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == 1 ) {
	    return true;
	}
	return false;
    }

    @Override
    public List< Map > selectTempObjByBusId( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", busUserId );

	String result = HttpSignUtil.SignHttpSelect( params, WS_SHOP_URL + "selectTempObjByBusId", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }
}
