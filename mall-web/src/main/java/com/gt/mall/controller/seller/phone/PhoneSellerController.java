package com.gt.mall.controller.seller.phone;

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
 * 超级销售员 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/phoneSellers" )
public class PhoneSellerController extends AuthorizeOrLoginController {

    /**
     * 进入我的商城页面
     */
    @RequestMapping( value = "{saleMemberId}/79B4DE7C/mallIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String mallIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的商城页面" );
	try {
	} catch ( Exception e ) {
	    logger.error( "进入我的商城失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/mallIndex";
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @RequestMapping( "{saleMemberId}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params )
		    throws Exception {
	try {
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "商品搜索页面异常：" + e.getMessage() );
	}
	return "mall/product/phone/shoppingall";
    }

}
