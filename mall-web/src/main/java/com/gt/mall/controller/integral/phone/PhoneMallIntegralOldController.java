package com.gt.mall.controller.integral.phone;

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
 * 积分商品表 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "phoneIntegral" )
public class PhoneMallIntegralOldController extends AuthorizeOrLoginController {

    @Autowired
    private MallStoreService mallStoreService;

    /**
     * 进入积分商城
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "{shopId}/79B4DE7C/toIndex" )
    public String toIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopId, @RequestParam Map< String,Object > params ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        if ( userid <= 0 ) {
            MallStore mallStore = mallStoreService.selectById( shopId );
            userid = mallStore.getStoUserId();
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/integral/index/" + userid;
    }

    /**
     * 进入兑换记录的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/recordList" )
    public String recordList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/integral/record/" + userid;
    }

    /**
     * 进入积分明细的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/integralDetail" )
    public String integralDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/integral/detail/" + userid;
    }

    /**
     * 进入积分商品明细的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/integralProduct" )
    public String integralProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        int productId = 0;
        if ( CommonUtil.isNotEmpty( CommonUtil.toInteger( params.get( "id" ) ) ) ) {
            productId = CommonUtil.toInteger( params.get( "id" ) );
        }
        int shopId = 0;
        if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
            shopId = CommonUtil.toInteger( params.get( "shopId" ) );
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/integral/product/" + userid + "/" + productId + "/" + shopId;
    }

}
