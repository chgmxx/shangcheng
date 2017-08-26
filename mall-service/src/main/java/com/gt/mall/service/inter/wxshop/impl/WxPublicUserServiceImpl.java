package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.wx.QrcodeCreateFinal;
import com.gt.mall.bean.wx.SendWxMsgTemplate;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

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

    private static final String WS_SHOP_URL = "/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/";

    @Override
    public WxPublicUsers selectByUserId( int busUserId ) {
	String result = HttpSignUtil.SignHttpSelect( busUserId, WS_SHOP_URL + "selectByUserId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectById( int id ) {
	String result = HttpSignUtil.SignHttpSelect( id, WS_SHOP_URL + "selectById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectByMemberId( int memberId ) {
	String result = HttpSignUtil.SignHttpSelect( memberId, WS_SHOP_URL + "selectByMemberId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public String qrcodeCreateFinal( QrcodeCreateFinal createFinal ) {
	String result = HttpSignUtil.SignHttpSelect( createFinal, WS_SHOP_URL + "qrcodeCreateFinal.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return result;
	}
	return null;
    }

    @Override
    public boolean sendWxMsgTemplate( SendWxMsgTemplate template ) {
	Map< String,Object > resultMap = HttpSignUtil.SignHttpInsertOrUpdate( template, WS_SHOP_URL + "sendWxMsgTemplate.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public List< Map > selectTempObjByBusId( int busUserId ) {
	String result = HttpSignUtil.SignHttpSelect( busUserId, WS_SHOP_URL + "selectTempObjByBusId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }
}
