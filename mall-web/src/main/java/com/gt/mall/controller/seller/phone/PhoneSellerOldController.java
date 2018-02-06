package com.gt.mall.controller.seller.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
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
 * 超级销售员 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/phoneSellers" )
public class PhoneSellerOldController extends AuthorizeOrLoginController {

    @Autowired
    private MallSellerMallsetService mallSellerMallSetService;

    /**
     * 进入我的商城页面
     */
    @RequestMapping( value = "{saleMemberId}/79B4DE7C/mallIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String mallIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        if ( userid <= 0 ) {
            MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
            userid = mallSet.getBusUserId();
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/seller/mallindex/" + userid + "/" + saleMemberId;
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @RequestMapping( "{saleMemberId}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params )
        throws Exception {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        if ( userid <= 0 ) {
            MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
            userid = mallSet.getBusUserId();
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/0/" + userid + "/8/k=k/";
    }

}
