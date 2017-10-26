package com.gt.mall.controller;

/**
 * 公用Controller
 * User : yangqian
 * Date : 2017/8/26 0026
 * Time : 10:57
 */

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.utils.*;
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
import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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
	String originalFilename = "";
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    MultipartFile multipartFile = multipartRequest.getFile( "imgFile" );
	    String userName = user.getName();
	    // 文件名
	    originalFilename = multipartFile.getOriginalFilename();
	    // 后缀
	    String suffix = originalFilename.substring( originalFilename.lastIndexOf( "." ) );
	    String phonejsp = originalFilename.substring( originalFilename.lastIndexOf( "." ) + 1 );
	    // 判断上传图片是否是支持的格式
	   /* int num = dictService.dictJsp( suffix );
	    if ( num == 0 ) {
		msgMap.put( "error", "1" );
		String message = "上传失败，该图片格式不支持";
		msgMap.put( "message", message );
	    } else {*/
	    // 文件大小
	    Long fileSize = multipartFile.getSize();
	    // 加上时间戳，名字就可以一样
	    Long time = System.currentTimeMillis();
	    // 获取1007字典类型
	    String path = PropertiesUtil.getResImagePath() + "/2/" + userName + "/" + Constants.IMAGE_FOLDER_TYPE_4 + "/"
			    + DateTimeKit.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD )
			    + "/"; // request.getSession().getServletContext().getRealPath("/")+"upload/editor/"+userName+"/";
	    logger.error( "path:" + path );
	    path += MD5Util.getMD5( time + originalFilename.substring( 0, originalFilename.lastIndexOf( "." ) ) )
			    + suffix;
	    File file = new File( path );
	    if ( !file.exists() && !file.isDirectory() ) {
		file.mkdirs();
	    }
	    /*multipartFile.transferTo( file );*/
	    ContinueFTP myFtp = new ContinueFTP();
	    myFtp.upload( file.getPath() );
	    logger.info( "ftp上传图片路径:" + path );
	    Map< String,Object > ftpResult = myFtp.upload( path );
	    logger.info( "ftp上传结果：" + com.alibaba.fastjson.JSONObject.toJSONString( ftpResult ) );
	    String url = PropertiesUtil.getResourceUrl() + path.split( "upload" )[1];

	    /*}*/
	    msgMap.put( "error", 0 );
	    msgMap.put( "url", url );

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
