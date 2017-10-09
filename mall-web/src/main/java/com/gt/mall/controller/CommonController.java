package com.gt.mall.controller;

/**
 * 公用Controller
 * User : yangqian
 * Date : 2017/8/26 0026
 * Time : 10:57
 */

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.QRcodeKit;
import com.gt.mall.utils.SessionUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping( "/common" )
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger( CommonController.class );

    /**
     * 下载提取链接二维码
     */
    @RequestMapping( "/downQr" )
    public void downQr( HttpServletRequest request, HttpServletResponse response, @RequestParam String url ) throws UnsupportedEncodingException {
	String filename = "二维码.jpg";
	response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	response.setContentType( "application/octet-stream" );
	QRcodeKit.buildQRcode( url, 450, 450, response );
    }

    /**
     * 跳转登陆
     */
    @RequestMapping( "/toLogin" )
    public String toLogin( HttpServletRequest request, HttpServletResponse response ) {
	request.setAttribute( "returnUrls", PropertiesUtil.getWxmpDomain() + "user/tologin.do" );
	return "common/userlogin";
    }

    /**
     * 文本编辑器上传图片
     */
    @RequestMapping( "/upload" )
    @SysLogAnnotation( description = "编辑器里面的图片上传方法", op_function = "2" )
    public void upload( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws Exception {
	response.setCharacterEncoding( "UTF-8" );
	Map< String,Object > msgMap = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    MultipartFile multipartFile = multipartRequest.getFile( "imgFile" );

	    Map< String,Object > returnMap = CommonUtil.fileUploadByBusUser( multipartFile, user.getId() );
	    if ( "1".equals( returnMap.get( "reTurn" ) ) ) {
		msgMap.put( "error", 0 );
		msgMap.put( "url", PropertiesUtil.getResourceUrl() + returnMap.get( "message" ) );

	    } else {
		logger.error( "该图片存在" );
		msgMap.put( "error", "1" );
		msgMap.put( "message", "该图片可能存在风险，微信拒绝上传" );
	    }

	} catch ( Exception e ) {
	    logger.error( "ex:" + e.getMessage() );
	    msgMap.put( "error", "1" );
	    msgMap.put( "message", "上传失败" );
	    e.printStackTrace();
	} finally {
	    PrintWriter p = response.getWriter();
	    p.write( JSONObject.fromObject( msgMap ).toString() );
	}
    }
}
