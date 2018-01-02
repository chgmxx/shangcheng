package com.gt.mall.controller.auction;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MallAuctionOldController extends AuthorizeOrLoginController {

    @Autowired
    private MallStoreService mallStoreService;

    /**
     * 获取店铺下所有的拍卖（手机端）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/auctionall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctionall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	}
	if ( userid <= 0 ) {
	    MallStore mallStore = mallStoreService.selectById( shopid );
	    userid = mallStore.getStoUserId();
	}
	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/" + shopid + "/" + userid + "/4/k=k";
    }

    /**
     * 拍卖详情
     */
    @RequestMapping( "{id}/{shopid}/{aId}/79B4DE7C/auctiondetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctiondetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @PathVariable int aId,
		    @RequestParam Map< String,Object > params ) throws Exception {
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	}
	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/goods/details/" + shopid + "/" + userid + "/4/" + id + "/" + aId;

    }

}
