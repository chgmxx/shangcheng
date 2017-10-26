package com.gt.mall.controller.purchase;

import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.entity.purchase.PurchaseCarousel;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.service.web.purchase.PurchaseCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
@RequestMapping( "purchaseCarousel" )
public class PurchaseCarouselController extends BaseController {

    @Autowired
    PurchaseCarouselService carouselService;

    /**
     * 轮播首页
     *
     * @param request
     * @param parms
     *
     * @return
     */
    @RequestMapping( value = "/carouselIndex" )
    public String orderIndex( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    parms.put( "busId", busUser.getId() );
	    PageUtil page = carouselService.findList( parms );
	    request.setAttribute( "page", page );
	    request.setAttribute( "parms", parms );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	if ( parms.get( "entrance" ) != null ) {
	    return "mall/purchase/carouselIframe";
	}
	return "mall/purchase/carouselIndex";
    }

    @RequestMapping( value = "/carouselForm" )
    public String form( HttpServletRequest request, Integer carouselId, PurchaseCarousel carousel ) {
	String title = "新增轮播";
	if ( carouselId != null && carouselId > 0 ) {
	    carousel = carouselService.selectById( carouselId );
	    if ( carousel != null ) {
		title = "修改轮播";
	    }
	}
	request.setAttribute( "title", title );
	request.setAttribute( "carousel", carousel );
	return "mall/purchase/carouselEdit";
    }

    @ResponseBody
    @RequestMapping( value = "/saveCarousel" )
    public Map< String,Object > saveContract( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    PurchaseCarousel carousel = new PurchaseCarousel();
	    carousel.setBusId( busUser.getId() );
	    carousel.setCarouselImg( parms.get( "carouselImg" ).toString() );
	    carousel.setCarouselUrl( parms.get( "carouselUrl" ).toString() );
	    if ( parms.get( "id" ) != null && CommonUtil.isNotEmpty( parms.get( "id" ).toString() ) ) {
		carousel.setId( Integer.parseInt( parms.get( "id" ).toString() ) );
		carouselService.updateById( carousel );
	    } else {
		carouselService.insert( carousel );
	    }
	    map.put( "result", true );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	}
	return map;
    }

}
