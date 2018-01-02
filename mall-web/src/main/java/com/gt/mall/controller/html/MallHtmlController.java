package com.gt.mall.controller.html;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseController;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.html.MallHtmlFromService;
import com.gt.mall.service.web.html.MallHtmlReportService;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商城里面的H5 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mallhtml" )
public class MallHtmlController extends BaseController {

    @Autowired
    private MallHtmlService       htmlService;
    @Autowired
    private MallHtmlFromService   htmlFromService;
    @Autowired
    private MallHtmlReportService htmlReportService;
    @Autowired
    private DictService           dictService;
    @Autowired
    private WxPublicUserService   wxPublicUserService;
    @Autowired
    private BusUserService        busUserService;
    @Autowired
    private CoreService           coreService;

    /**
     * h5商城手机
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     */
    @RequestMapping( value = "{id}/79B4DE7C/phoneHtml" )
    public String phoneHtml( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) {
	String jsp = "";
	MallHtml obj = htmlService.selectById( id );

	try {
	    coreService.payModel( obj.getBusUserId(), CommonUtil.getAddedStyle( "H5商城" ) );////判断活动是否已经过期
	} catch ( BusinessException be ) {
	    request.setAttribute( "guoqiError", 1 );
	}
	//举报关闭该页面
	if ( obj.getReportstate() == 1 ) {
	    jsp = "error/ban";
	} else {
	    Integer style = 1;//0代表是微信有公主号，1没有
	    String ua = ( (HttpServletRequest) request ).getHeader( "user-agent" ).toLowerCase();
	    if ( ua.indexOf( "micromessenger" ) > 0 ) {// 是否来自于微信浏览器打开
		//来自于商家这边
		if ( obj.getSourceType() == 2 ) {
		    WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( obj.getBusUserId() );
		    if ( wxPublicUsers != null ) {
			style = 0;
			//TODO CommonUtil.getWxParams
			//			CommonUtil.getWxParams( publicUsers, request );
		    }
		}
	    }
	    String http = PropertiesUtil.getResourceUrl();
	    request.setAttribute( "style", style );
	    request.setAttribute( "msg", obj );
	    request.setAttribute( "http", http );
	}
	return "/mall/htmlmall/phone/phonehtml";

    }

    /**
     * 商城举报
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/htmlReport" )
    public void htmlReport( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map map = new HashMap();
	try {
	    Integer style = Integer.valueOf( request.getParameter( "style" ).toString() );
	    Integer htmlid = Integer.valueOf( request.getParameter( "htmlid" ).toString() );
	    htmlReportService.htmlReport( htmlid, style );

	    map.put( "reTurn", "0" );
	    map.put( "message", "操作成功" );
	} catch ( Exception e ) {
	    map.put( "reTurn", "1" );
	    map.put( "message", "系统异常" );
	    logger.error( "h5 商城举报异常:" + e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 商城表单信息
     *
     * @param request
     * @param response
     * @param obj
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/htmlfrom" )
    public void htmlfrom( HttpServletRequest request, HttpServletResponse response, MallHtmlFrom obj ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    obj.setCreattime( DateTimeKit.getDateTime() );
	    htmlFromService.insert( obj );
	} catch ( Exception e ) {
	    logger.error( "h5 商城表单异常:" + e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /**
     * h5表单列表
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( "/htmlfromlist" )
    public String htmlfromlist( HttpServletRequest request, HttpServletResponse response ) {
	String jsp = "";
	try {
	    Map< String,Object > map = htmlFromService.htmlListfrom( request );
	    request.setAttribute( "map", map );
	    jsp = "mall/htmlmall/htmlfromlist";
	} catch ( Exception e ) {
	    logger.error( "h5 商城列表页异常:" + e.getMessage() );
	    jsp = "error/404Two";
	}
	return jsp;

    }

    /**
     * 查看详情
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( "/htmlfromview" )
    public String htmlfromview( HttpServletRequest request, HttpServletResponse response ) {
	String jsp = "";
	try {
	    Map< String,Object > map = htmlFromService.htmlfromview( request );
	    request.setAttribute( "map", map );
	    jsp = "mall/htmlmall/htmlfromview";
	} catch ( Exception e ) {
	    logger.error( "h5 商城列表页异常:" + e.getMessage() );
	    jsp = "error/404Two";
	}
	return jsp;
    }

    /**
     * 替换背景图
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "/updateimage" )
    public void updateimage( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );
	    String bakurl = request.getParameter( "imgurl" ).toString();
	    htmlService.updateimage( id, bakurl );
	    map.put( "error", 0 );
	} catch ( Exception e ) {
	    map.put( "error", 1 );
	    logger.error( "h5 商城替换图片失败:" + e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 模板商城h5 预览功能
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( "/ylmodel" )
    public String ylmodel( HttpServletRequest request, HttpServletResponse response ) {
	Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );//获取模板id
	Wrapper groupWrapper = new EntityWrapper();
	groupWrapper.setSqlSelect( "id,htmlname,state,introduce,codeUrl" );
	groupWrapper.where( "id = {0}", id );
	Map< String,Object > map = htmlService.selectMap( groupWrapper );
	request.setAttribute( "map", map );
	request.setAttribute( "image", PropertiesUtil.getResourceUrl() );
	return "mall/htmlmall/ylmodel";
    }

    /**
     * 添加H5商城选中模板
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "/SetmallHtml" )
    public void setmallHtml( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );//获取登录信息
	    Integer iscreat = 0;//是否还可以创建h5商城0 可以，1不可以
	    Integer ispid = 0;//是否是主账号，0是主账号，1不是
	    //pid==0 主账户,否则是子账户
	    if ( user.getPid() == 0 ) {
	    } else {
		Integer zhuid = busUserService.getMainBusId( user.getId() );//获取父类的id
		user = busUserService.selectById( zhuid );
		ispid = 1;
	    }
	    Integer maxcj = Integer.valueOf( dictService.getDiBserNum( user.getId(), 16, "1140" ) );
	    Integer ycj = htmlService.htmltotal( user.getId() );//主账户之下已创建的数量
	    if ( ycj >= maxcj ) {
		map.put( "error", "2" );
		map.put( "ispid", ispid );
		map.put( "message", "等级不够，不能创建h5商城" );
	    } else {
		user = MallSessionUtils.getLoginUser( request );//获取登录信息
		Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );//获取模板id
		Integer xid = htmlService.SetmallHtml( id, user );
		map.put( "error", "0" );
		map.put( "xid", xid );
	    }
	} catch ( Exception e ) {
	    map.put( "error", "1" );
	    logger.error( "添加H5商城选中模板失败:" + e.getMessage() );
	}
	CommonUtil.write( response, map );
    }
}
