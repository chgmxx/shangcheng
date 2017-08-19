package com.gt.mall.service.inter.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusUser;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家实现类
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 11:45
 */
@Service
public class BusUserServiceImpl implements BusUserService {

    private static final String USER_URL = "/8A5DA52E/busUserApi/";

    private static final String CHILD_USER_URL = "/8A5DA52E/childBusUserApi/";

    public static final String VOICE_URL = "/8A5DA52E/videoCourceApi/";

    @Override
    public BusUser selectById( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	String result = HttpSignUtil.SignHttpSelect( params, USER_URL + "getBusUserApi.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), BusUser.class );
	}
	return null;
    }

    @Override
    public int getIsErpCount( int modelstyle, int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	params.put( "modelstyle", modelstyle > 0 ? modelstyle : 8 );//默认是8  为进销存
	String result = HttpSignUtil.SignHttpSelect( params, USER_URL + "getIsErpCount.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( resultObj.getInteger( "erpCount" ) == 1 ) {//已开通
		return 1;
	    }
	}
	return 0;
    }

    @Override
    public boolean getIsAdmin( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	String result = HttpSignUtil.SignHttpSelect( params, CHILD_USER_URL + "getIsAdmin.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( resultObj.getInteger( "isadmin" ) == 1 ) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public int getMainBusId( int userId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", userId );
	String result = HttpSignUtil.SignHttpSelect( params, CHILD_USER_URL + "getMainBusId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    return resultObj.getInteger( "mainBusId" );
	}
	return -1;
    }

    @Override
    public String getVoiceUrl( String courceModel ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "courceModel", courceModel );
	String result = HttpSignUtil.SignHttpSelect( params, VOICE_URL + "getVoiceUrl.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( CommonUtil.isNotEmpty( resultObj.get( "voiceUrl" ) ) ) {
		return CommonUtil.toString( resultObj.get( "voiceUrl" ) );
	    }
	}
	return null;
    }


}
