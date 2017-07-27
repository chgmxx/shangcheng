package com.gt.mall.service;

import com.gt.mall.BasicTest;
import com.gt.mall.cxf.service.WxShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/26 0026
 * Time : 17:47
 */
public class CxfWxShopServiceTest extends BasicTest {

    @Autowired
    private WxShopService wxShopService;

    @Test
    public void test(){
        try {
            wxShopService.getShopById( 21 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


   /* @Value( "${project.shop.cxf-url}" )
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
    }*/
}
