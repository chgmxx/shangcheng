package com.gt.mall.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * CXF 服务端身份验证拦截器
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:53
 */
public class CXFServerAuthorInterceptor extends AbstractPhaseInterceptor< SoapMessage > {
    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger( CXFServerAuthorInterceptor.class );

    /**
     * 初始化拦截器配置
     */
    public CXFServerAuthorInterceptor() {
	super( Phase.PRE_INVOKE );
    }

    /**
     * 身份验证方法
     *
     * @param soapMessage Soap消息
     *
     * @throws Fault
     */
    @Override
    public void handleMessage( SoapMessage soapMessage ) throws Fault {
	List< Header > headers = soapMessage.getHeaders();
	if ( headers == null || headers.size() < 1 ) {
	    LOG.error( "认证不通过，客户端不存在认证信息。" );
	    throw new Fault( new IllegalArgumentException( "认证不通过，客户端不存在认证的header" ) );
	}
	// 获取Header携带是用户和密码信息
	Header firstHeader = headers.get( 0 );
	Element ele = (Element) firstHeader.getObject();
	NodeList userIdEle = ele.getElementsByTagName( "userId" );
	NodeList userPassEle = ele.getElementsByTagName( "userPass" );
	LOG.debug( " userid: {} ,password: {} ", userIdEle, userPassEle );
	if ( userIdEle.getLength() != 1 ) {
	    LOG.error( "认证参数缺失。" );
	    throw new Fault( new IllegalArgumentException( "WS接口账号不正确" ) );
	}
	if ( userPassEle.getLength() != 1 ) {
	    LOG.error( "认证参数缺失。" );
	    throw new Fault( new IllegalArgumentException( "WS接口密码不正确" ) );
	}
	String userId = userIdEle.item( 0 ).getFirstChild().getNodeValue();

	String userPass = userPassEle.item( 0 ).getFirstChild().getNodeValue();
	// 写死在这里
	if ( !userId.equals( "duofriend" ) || !userPass.equals( "123456" ) ) {
	    LOG.error( "认证失败，账号或密码不正确。" );
	    throw new Fault( new IllegalArgumentException( "WS接口账号或密码不正确" ) );
	}
    }
}
