package com.gt.mall.controller.auction;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 拍卖 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mAuction" )
public class MallAuctionController extends AuthorizeOrLoginController {

    /**
     * 获取店铺下所有的拍卖（手机端）
     *
     * @param request
     * @param response
     * @param shopid
     * @param params
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/auctionall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctionall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入拍卖商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/auction/phone/auctionall";
    }

    /**
     * 拍卖详情
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( "{id}/{shopid}/{aId}/79B4DE7C/auctiondetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctiondetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @PathVariable int aId,
		    @RequestParam Map< String,Object > param ) throws Exception {
	String jsp = "mall/auction/phone/auctiondetail";
	try {
	} catch ( Exception e ) {
	    logger.error( "进入拍卖商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return jsp;

    }

    /**
     * 商品详情
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( "{id}/{shopid}/{auctionId}/79B4DE7C/shopdetails" )
    public String shopdetails( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @PathVariable int auctionId,
		    @RequestParam Map< String,Object > param ) {
	try {
	} catch ( Exception e ) {
	    logger.error( "MallAuctionController方法异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/auction/phone/productdetail";
    }

}
