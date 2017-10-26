package com.gt.mall.controller.groupbuy;

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
 * 团购表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mGroupBuy" )
public class MallGroupBuyController extends AuthorizeOrLoginController {

    /**
     * 获取店铺下所有的团购（手机）
     *
     * @param request
     * @param response
     * @param shopid
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/groupbuyall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupbuyall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入团购商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/phone/groupbuyall";
    }

    /**
     * 我要参团，团购详情（手机）
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{id}/{joinId}/79B4DE7C/groupBuyDetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupBuyDetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int joinId,
		    @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入我要参团/团购详情的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/phone/groupBuyDetail";
    }

}
