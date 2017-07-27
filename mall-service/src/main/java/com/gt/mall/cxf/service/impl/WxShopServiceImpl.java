package com.gt.mall.cxf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.param.BaseParam;
import com.gt.mall.bean.param.GetById;
import com.gt.mall.bean.result.shop.WsWxShopInfo;
import com.gt.mall.cxf.service.WxShopService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.CxfFactoryBeanUtil;
import com.gt.mall.util.MyConfigUtil;
import com.gt.webservice.service.WxmpApiSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信门店调用的业务实现  调用CXF接口
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:19
 */
@Service
public class WxShopServiceImpl implements WxShopService {

    private static Logger logger = LoggerFactory.getLogger( WxShopServiceImpl.class );

    @Autowired
    private MyConfigUtil myConfigUtil;

    @Override
    public WsWxShopInfo getShopById( int id ) throws Exception {
	GetById getById = new GetById();
	getById.setId( id );

	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) CxfFactoryBeanUtil.crateCxfFactoryBean( "WxmpApiSerivce" , MyConfigUtil.getShopUrl() );

	BaseParam baseParam = new BaseParam<>();
	baseParam.setAction( "getShopById" );
	baseParam.setRequestToken( MyConfigUtil.getWxmpToken() );
	baseParam.setReqdata( getById );

	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	logger.info( "根据门店id查询门店信息的接口 getShopById 返回的结果：" + json );

	String data = CxfFactoryBeanUtil.getStringResultByJson( json );
	if(CommonUtil.isNotEmpty( data )){
	    WsWxShopInfo wxShopInfo = JSONObject.parseObject( data, WsWxShopInfo.class );
	    return wxShopInfo;
	}
	return null;
    }

}
