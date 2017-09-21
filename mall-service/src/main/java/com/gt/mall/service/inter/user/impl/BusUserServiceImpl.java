package com.gt.mall.service.inter.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    private static final String VOICE_URL = "/8A5DA52E/videoCourceApi/";

    private static final String USER_GUOQ_URL = "/8A5DA52E/busUserApi/";

    private static final String USER_SHOP_URL = "/8A5DA52E/busCommonApi/";

    @Override
    public BusUser selectById( int busUserId ) {
	if ( busUserId == 0 ) {
	    return null;
	}
	String key = Constants.REDIS_KEY + "bus_user_" + busUserId;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), BusUser.class );
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	String result = HttpSignUtil.signHttpSelect( params, USER_URL + "getBusUserApi.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    BusUser user = JSONObject.toJavaObject( JSONObject.parseObject( result ), BusUser.class );
	    JedisUtil.set( key, JSONObject.toJSONString( user ), Constants.REDIS_SECONDS );
	    return user;
	}
	return null;
    }

    @Override
    public int getIsErpCount( int modelstyle, int busUserId ) {
	/*Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	params.put( "modelstyle", modelstyle > 0 ? modelstyle : 8 );//默认是8  为进销存
	String result = HttpSignUtil.signHttpSelect( params, USER_URL + "getIsErpCount.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    if ( resultObj.getInteger( "erpCount" ) == 1 ) {//已开通
		return 1;
	    }
	}*/
	return 0;
    }

    @Override
    public boolean getIsAdmin( int busUserId ) {
	String key = Constants.REDIS_KEY + "is_admin_" + busUserId;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return CommonUtil.toInteger( obj ) == 1;
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", busUserId );
	String result = HttpSignUtil.signHttpSelect( params, CHILD_USER_URL + "getIsAdmin.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    JedisUtil.set( key, resultObj.getString( "isadmin" ), Constants.REDIS_SECONDS );
	    if ( resultObj.getInteger( "isadmin" ) == 1 ) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public int getMainBusId( int userId ) {
	String key = Constants.REDIS_KEY + "user_is_main_" + userId;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return CommonUtil.toInteger( obj );
	    }
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", userId );
	String result = HttpSignUtil.signHttpSelect( params, CHILD_USER_URL + "getMainBusId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    int isMain = resultObj.getInteger( "mainBusId" );
	    JedisUtil.set( key, isMain + "", Constants.REDIS_SECONDS );
	    return isMain;
	}
	return -1;
    }

    @Override
    public String getVoiceUrl( String courceModel ) {
	String key = Constants.REDIS_KEY + "voice_url_" + courceModel;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return CommonUtil.toString( obj );
	    }
	    return null;
	}
	Map< String,Object > params = new HashMap<>();
	params.put( "courceModel", courceModel );
	String result = HttpSignUtil.signHttpSelect( params, VOICE_URL + "getVoiceUrl.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    JSONObject resultObj = JSONObject.parseObject( result );
	    JedisUtil.set( key, resultObj.get( "voiceUrl" ).toString(), Constants.REDIS_SECONDS );
	    if ( CommonUtil.isNotEmpty( resultObj.get( "voiceUrl" ) ) ) {
		return CommonUtil.toString( resultObj.get( "voiceUrl" ) );
	    }
	}
	return null;
    }

    @Override
    public JSONObject isUserGuoQi( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "busId", busUserId );
	//判断商家信息 1是否过期 2公众号是否变更过
	String result = HttpSignUtil.signHttpSelect( params, USER_GUOQ_URL + "getWxPulbicMsg.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.parseObject( result );
	}
	return null;
    }

    @Override
    public List< WsWxShopInfoExtend > getShopIdListByUserId( int userId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "userId", userId );
	String result = HttpSignUtil.signHttpSelect( params, USER_SHOP_URL + "getShopIdList.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, WsWxShopInfoExtend.class );
	}
	return null;
    }

}
