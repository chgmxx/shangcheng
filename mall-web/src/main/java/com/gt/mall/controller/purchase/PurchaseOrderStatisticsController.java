package com.gt.mall.controller.purchase;

import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.purchase.PurchaseOrderStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "/purchaseStatistics" )
public class PurchaseOrderStatisticsController extends BaseController {

    @Autowired
    PurchaseOrderStatisticsService orderStatisticsService;

    @RequestMapping( value = "/statisticsIndex" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > parms, @RequestParam Integer orderId ) {
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    parms.put( "busId", busUser.getId() );
	    parms.put( "orderId", orderId );
	    PageUtil page = orderStatisticsService.findList( parms );
	    request.setAttribute( "page", page );
	    request.setAttribute( "parms", parms );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/statisticsIndex";
    }
}
