package com.gt.mall.service;

import com.gt.mall.BasicTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/7/26 0026
 * Time : 17:47
 */
public class CxfServiceTest extends BasicTest {

    @Value( "${project.member.cxf-url}" )
    private String MemberUrl;

//    @Autowired
//    private MemberWSService memberWSService;

    // 用例一，使用JaxWsProxyFactoryBean 发起调用
    @Test
    public void findById() {
//	JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//	// 1. 如果存在身份验证，必须添加cxf安全验证拦截器 账号密码为 duofriend/123456 账号校验
//	jaxWsProxyFactoryBean.getOutInterceptors().add( new CXFClientAuthorInterceptor( "duofriend", "123456" ) );
//	jaxWsProxyFactoryBean.setServiceClass( MemberWSService.class );
//	jaxWsProxyFactoryBean.setAddress( MemberUrl );
//	MemberWSService memberWSService = (MemberWSService) jaxWsProxyFactoryBean.create();
//	Member member = memberWSService.findById( 197 );
//	this.logger.info( "member {}", JSONObject.toJSON( member ) );
    }
    // 用例二（推荐），使用wsdl2java 生成的代码调用方式
    @Test
    public void findByIdProxy(){
	// 使用动态代理方式处理 header 并调用cxf 校验
//	MemberWSService memberWSService = new MemberWS().getMemberWSServiceImplPort();
//	org.apache.cxf.endpoint.Client client1 = ClientProxy.getClient( memberWSService );
//	client1.getOutInterceptors().add( new CXFClientAuthorInterceptor("duofriend", "123456") );
//	System.out.println(" member : "+ JSONObject.toJSONString( memberWSService.findById( 197 ) ));
    }

}
