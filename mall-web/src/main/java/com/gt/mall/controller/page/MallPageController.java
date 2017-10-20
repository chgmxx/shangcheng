package com.gt.mall.controller.page;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 页面表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallPage" )
public class MallPageController extends AuthorizeOrLoginController {

    @RequestMapping( "{id}/79B4DE7C/viewHomepage" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String viewHomepage( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws Exception {

	return "redirect:/mallPage/" + id + "/79B4DE7C/pageIndex.do";
    }

    /**
     * 手机访问商家主页面接口
     */
    @RequestMapping( "{id}/79B4DE7C/pageIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String pageIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws IOException {

	return "/mall/phonepage/phoneHomepage";
    }

    /**
     * 显示手机商品页
     */
    @RequestMapping( "{id}/{shopid}/79B4DE7C/phoneProduct" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String phoneProduct( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @RequestParam Map< String,Object > param )
		    throws Exception {
	logger.info( "进入商城商品详细页面。。。" );
	String jsp = "/mall/product/phone/phone_product_detail";
	try {

	} catch ( Exception e ) {
	    logger.error( "访问商城商品详细页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return jsp;
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @RequestMapping( "{shopid}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "商品搜索页面异常：" + e.getMessage() );
	}
	return "mall/product/phone/shoppingall";

    }

}