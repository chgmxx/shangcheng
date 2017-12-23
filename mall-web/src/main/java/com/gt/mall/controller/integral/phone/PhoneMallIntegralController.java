package com.gt.mall.controller.integral.phone;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.integral.MallIntegralImageService;
import com.gt.mall.service.web.integral.MallIntegralService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
public class PhoneMallIntegralController extends AuthorizeOrLoginController {

    @Autowired
    private MallPageService          pageService;
    @Autowired
    private MallIntegralService      integralService;
    @Autowired
    private MallIntegralImageService integralImageService;
    @Autowired
    private MallOrderService         orderService;
    @Autowired
    private MallStoreService         storeService;
    @Autowired
    private MemberService            memberService;
    @Autowired
    private WxPublicUserService      wxPublicUserService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private MemberAddressService     memberAddressService;

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
	return "mall/integral/phone/integral_index";
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
	return "mall/integral/phone/integral_record";
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
	return "mall/integral/phone/integral_detail";
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
	try {
	} catch ( Exception e ) {
	    logger.error( "进入积分商品明细的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/integral_product";
    }

    /**
     * 兑换积分
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/recordIntegral" )
    @SysLogAnnotation( op_function = "2", description = "积分兑换商品" )
    public void recordIntegral( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	} catch ( Exception e ) {
	    resultMap.put( "code", -1 );

	    logger.error( "兑换积分异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

}
