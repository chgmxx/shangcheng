package com.gt.mall.controller.member;

import com.gt.mall.common.AuthorizeOrLoginController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 商城的个人中心 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mMember" )
public class MallMemberController extends AuthorizeOrLoginController {

    private Logger logger = Logger.getLogger( MallMemberController.class );

    /**
     * 跳转至个人中心的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( value = "/79B4DE7C/toUser" )
    public String toUser( HttpServletRequest request, HttpServletResponse response ) {
	logger.info( "进入个人中心页面页面" );
	try {

	} catch ( Exception e ) {
	    logger.error( "手机订单页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/member/index";
    }

    /**
     * 去评价页面
     *
     * @param request
     * @param params
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/orderAppraise" )
    public String orderAppraise( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入去评论页面的controller" );
	try {

	} catch ( Exception e ) {
	    logger.error( "进入评论页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/orderAppraise";
    }

    /**
     * 我的评价
     *
     * @param request
     * @param param
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/myAppraise" )
    public String appraiseList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > param ) {
	logger.info( "进入我的评论controller" );
	try {

	} catch ( Exception e ) {
	    logger.error( "查询我的评论有误！" + e.getMessage() );
	    e.printStackTrace();
	}
	return "/mall/order/phone/myAppraise";
    }

    /**
     * 我的收藏
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( "/79B4DE7C/collect" )
    public String collect( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws Exception {
	logger.info( "进入我的收藏controller" );
	try {

	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "我的收藏页面异常：" + e.getMessage() );
	}
	return "mall/member/collectAll";

    }

    /**
     * 打开腾讯地图
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/tencentMap" )
    public String tencentMap( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {

	return "mall/order/phone/tencentMap";
    }

}
