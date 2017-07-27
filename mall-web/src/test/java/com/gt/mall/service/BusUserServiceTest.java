package com.gt.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.BasicTest;
import com.gt.mall.bean.param.BaseParam;
import com.gt.mall.bean.param.GetById;
import com.gt.webservice.service.WxmpApiSerivce;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zhangmz
 * @create 2017/7/9
 */
public class BusUserServiceTest extends BasicTest {

    @Value( "${project.shop.cxf-url}" )
    private String shopUrl;

    @Value( "${wxmp.token}" )
    private String wxmpToken;

    @Test
    public void testSelect() {

	JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	jaxWsProxyFactoryBean.setServiceClass( WxmpApiSerivce.class );
	jaxWsProxyFactoryBean.setAddress( shopUrl );
	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) jaxWsProxyFactoryBean.create();


        BaseParam baseParam = new BaseParam<>(  );


        baseParam.setAction( "getShopById" );;
        baseParam.setRequestToken( wxmpToken );
	GetById getById=new GetById();
	getById.setId( 21 );
        baseParam.setReqdata( getById);

        String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
        System.out.println("返回结果 = " + json);
    }

}