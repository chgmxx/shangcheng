//package com.gt.mall.config;
//
//import com.gt.mall.interceptor.CXFServerAuthorInterceptor;
//import com.gt.mall.ws.TestService;
//import org.apache.cxf.Bus;
//import org.apache.cxf.jaxws.EndpointImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//
//import javax.xml.ws.Endpoint;
//
///**
// * CXF 配置类  ( 配置对外发布的CXF接口)
// * User : yangqian
// * Date : 2017/7/27 0027
// * Time : 10:54
// */
//public class CXFServiceConfig {
//    @Autowired
//    private Bus bus;
//
//    // 注入CXF服务端身份验证拦截器
//    @Autowired
//    private CXFServerAuthorInterceptor cxfServerAuthorInterceptor;
//
//
//    @Autowired
//    private TestService testService;
//
//    @Bean
//    public Endpoint userServiceWSendpoint() {
//	EndpointImpl endpoint = new EndpointImpl( bus, testService );
//	endpoint.publish( "/TestService" );
//	endpoint.getInInterceptors().add( cxfServerAuthorInterceptor );
//	return endpoint;
//    }
//}
