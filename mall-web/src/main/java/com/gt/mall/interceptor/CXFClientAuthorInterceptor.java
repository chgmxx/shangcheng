package com.gt.mall.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * cxf 客户端 身份验证
 * User : yangqian
 * Date : 2017/7/26 0026
 * Time : 17:39
 */
public class CXFClientAuthorInterceptor extends AbstractPhaseInterceptor< SoapMessage > {

    private String uid;
    private String pwd;

    public CXFClientAuthorInterceptor( String uid, String pwd ) {
	// 设置拦截器的时机，在发送请求到服务端之前进行拦截
	super( Phase.PREPARE_SEND );
	this.uid = uid;
	this.pwd = pwd;
    }

    @Override
    public void handleMessage( SoapMessage soapMessage ) throws Fault {

	// 在soap消息中添加认证头信息
	List< Header > headers = soapMessage.getHeaders();
	Document doc = DOMUtils.createDocument();
	Element ele = doc.createElement( "authHeader" );
	Element eleId = doc.createElement( "userId" );
	eleId.setTextContent( this.uid );
	Element elePass = doc.createElement( "userPass" );
	elePass.setTextContent( this.pwd );
	ele.appendChild( eleId );
	ele.appendChild( elePass );
	headers.add( new Header( new QName( "" ), ele ) );

    }
}
