package com.gt.mall.cxf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.param.BaseParam;
import com.gt.mall.bean.param.GetById;
import com.gt.mall.bean.result.shop.WsWxShopInfo;
import com.gt.mall.cxf.service.WxShopService;
import com.gt.mall.util.CommonUtil;
import com.gt.webservice.service.WxmpApiSerivce;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:19
 */
@Configuration
@Service
public class WxShopServiceImpl implements WxShopService {

    @Value( "${project.shop.cxf-url}" )
    private String shopUrl;

    @Value( "${wxmp.token}" )
    private String wxmpToken;

    private WxmpApiSerivce wxmpApiSerivce;

    public WxShopServiceImpl() {

	JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	jaxWsProxyFactoryBean.setServiceClass( WxmpApiSerivce.class );
	jaxWsProxyFactoryBean.setAddress( shopUrl );
	wxmpApiSerivce = (WxmpApiSerivce) jaxWsProxyFactoryBean.create();

    }

    @Override
    public WsWxShopInfo getShopById( int id ) {

	JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	jaxWsProxyFactoryBean.setServiceClass( WxmpApiSerivce.class );
	jaxWsProxyFactoryBean.setAddress( shopUrl );
	wxmpApiSerivce = (WxmpApiSerivce) jaxWsProxyFactoryBean.create();

	BaseParam baseParam = new BaseParam<>();

	baseParam.setAction( "getShopById" );

	baseParam.setRequestToken( wxmpToken );
	GetById getById = new GetById();
	getById.setId( id );
	baseParam.setReqdata( getById );

	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	if ( CommonUtil.isNotEmpty( json ) ) {
	    JSONObject resultObj = JSONObject.parseObject( json );
	    if ( resultObj.getInteger( "code" ) == 0 ) {//请求成功
		WsWxShopInfo wxShopInfo = JSONObject.parseObject( resultObj.getString( "data" ), WsWxShopInfo.class );
		return wxShopInfo;
	    }
	}

	return null;
    }

}
