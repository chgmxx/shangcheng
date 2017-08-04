package com.gt.mall.controller.purchase;

import com.gt.mall.annotation.CommAnno;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 采购初始页
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "purchase" )
public class PurchaseController {

    @CommAnno( menu_url = "purchase/index.do" )
    @RequestMapping( value = "/index" )
    public String index( HttpServletRequest request, HttpServletResponse response ) {
	request.setAttribute( "iframe_url", "/purchaseOrder/orderIndex.do" );
	return "iframe";
    }
}
