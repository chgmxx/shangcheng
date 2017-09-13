package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.RequestUtils;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.param.wx.QrcodeCreateFinal;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.result.wx.ApiWxApplet;
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

    private static final String SHARE_URL = "/8A5DA52E/wxphone/6F6D9AD2/79B4DE7C/";

    @Override
    public WxPublicUsers selectByUserId( int busUserId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busUserId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectByUserId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectById( int id ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( id );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public WxPublicUsers selectByMemberId( int memberId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( memberId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectByMemberId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WxPublicUsers.class );
	}
	return null;
    }

    @Override
    public String qrcodeCreateFinal( QrcodeCreateFinal createFinal ) {
	RequestUtils< QrcodeCreateFinal > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( createFinal );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "qrcodeCreateFinal.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return result;
	}
	return null;
    }

    @Override
    public boolean sendWxMsgTemplate( SendWxMsgTemplate template ) {
	RequestUtils< SendWxMsgTemplate > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( template );
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, WS_SHOP_URL + "sendWxMsgTemplate.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public List< Map > selectTempObjByBusId( int busUserId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busUserId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectTempObjByBusId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, Map.class );
	}
	return null;
    }

    @Override
    public ApiWxApplet selectBybusIdAndindustry( BusIdAndindustry busIdAndindustry ) {
	RequestUtils< BusIdAndindustry > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busIdAndindustry );
	String result = HttpSignUtil.signHttpSelect( requestUtils, WS_SHOP_URL + "selectBybusIdAndindustry.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), ApiWxApplet.class );
	}
	return null;
    }

}
