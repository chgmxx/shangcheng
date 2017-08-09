package com.gt.mall.controller.integral.phone;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.constant.Constants;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.integral.MallIntegralImageService;
import com.gt.mall.web.service.integral.MallIntegralService;
import com.gt.mall.web.service.order.MallOrderService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
public class PhoneMallIntegralController extends BaseController {

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
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    // TODO 登录地址
	    String returnUrl ="";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    if ( shopId > 0 ) {
		boolean isShop = pageService.wxShopIsDelete( shopId );
		if ( !isShop ) {
		    return "mall/product/phone/shopdelect";
		}
		params.put( "shopId", shopId );
	    }
	    //查询积分商品
	    PageUtil page = integralService.selectIntegralByUserId( params );
	    request.setAttribute( "curPage", page.getCurPage() );
	    request.setAttribute( "pageCount", page.getPageCount() );
	    request.setAttribute( "productList", page.getSubList() );

	    //查询我的积分
	    if ( CommonUtil.isNotEmpty( member ) ) {
		//TODO 需关连 Member 会员信息
		//		member = memberService.findById(member.getId());
		if ( CommonUtil.isNotEmpty( member.getIntegral() ) ) {
		    request.setAttribute( "memberIntegral", member.getIntegral() );
		} else {
		    request.setAttribute( "memberIntegral", 0 );
		}
		request.setAttribute( "memberId", member.getId() );
	    }

	    //查询轮播图片
	    params.put( "userId", userid );
	    List< MallIntegralImage > imageList = integralImageService.getIntegralImageByUser( params );
	    request.setAttribute( "imageList", imageList );
	    request.setAttribute( "imageHttp", PropertiesUtil.getResourceUrl() );

	    shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
	    request.setAttribute( "shopId", shopId );

	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入积分商城异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/integral_index";
    }

    /**
     * 分页积分商品
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/integerProductPage" )
    public void integerProductPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    //查询积分商品
	    PageUtil page = integralService.selectIntegralByUserId( params );
	    resultMap.put( "curPage", page.getCurPage() );
	    resultMap.put( "pageCount", page.getPageCount() );
	    resultMap.put( "productList", page.getSubList() );
	} catch ( Exception e ) {
	    logger.error( "进入积分商城异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
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
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    // TODO 登录地址
	    String returnUrl ="";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    params.put( "buyerUserId", member.getId() );
	    params.put( "busUserId", userid );
	    //查询兑换记录
	    List< Map< String,Object > > orderList = orderService.selectIntegralOrder( params );
	    request.setAttribute( "orderList", orderList );

	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		int shopId = CommonUtil.toInteger( params.get( "shopId" ) );
		shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
		request.setAttribute( "shopId", shopId );
	    }
	    pageService.getCustomer( request, userid );

	    String urls = request.getHeader( "Referer" );
	    if ( CommonUtil.isNotEmpty( urls ) ) {
		if ( urls.indexOf( "79B4DE7C/integralProduct.do" ) > 0 ) {
		    request.setAttribute( "urls", urls );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "进入兑换记录的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
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
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    // TODO 登录地址
	    String returnUrl ="";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询兑换记录
	    PageUtil page = integralService.selectIntegralDetail( member, params );
	    request.setAttribute( "curPage", page.getCurPage() );
	    request.setAttribute( "pageCount", page.getPageCount() );
	    request.setAttribute( "integralList", page.getSubList() );
	    //查询我的积分
	    if ( CommonUtil.isNotEmpty( member ) ) {
		//TODO 需关连 Member 会员信息
		//		member = memberService.findById(member.getId());
		request.setAttribute( "memberIntegral", member.getIntegral() );
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		int shopId = CommonUtil.toInteger( params.get( "shopId" ) );
		shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
		request.setAttribute( "shopId", shopId );
	    }
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入积分明细的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/integral_detail";
    }

    /**
     * 分页积分明细
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/integerDetailPage" )
    public void integerDetailPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    //查询积分商品
	    PageUtil page = integralService.selectIntegralDetail( member, params );
	    resultMap.put( "curPage", page.getCurPage() );
	    resultMap.put( "pageCount", page.getPageCount() );
	    resultMap.put( "integerList", page.getSubList() );
	} catch ( Exception e ) {
	    logger.error( "分页积分明细异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
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
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    Map< String,Object > resultMap = integralService.selectProductDetail( member, params );
	    if ( resultMap != null && resultMap.size() > 0 ) {
		for ( String str : resultMap.keySet() ) {
		    request.setAttribute( str, resultMap.get( str ) );
		}
	    }
	    /*request.setAttribute("product", resultMap.get("product"));
	    request.setAttribute("detail", resultMap.get("detail"));
	    request.setAttribute("imageList", resultMap.get("imageList"));
	    request.setAttribute("recordNum", resultMap.get("recordNum"));
	    if(CommonUtil.isNotEmpty(resultMap.get("integral"))){
		    request.setAttribute("integral", resultMap.get("integral"));
	    }*/
	    if ( CommonUtil.isNotEmpty( member ) ) {
		//TODO 需关连 Member 会员信息
		//		member = memberService.findById(member.getId());
		//TODO 需关连 Member 是否会员 方法isMemember()
		//		boolean isMember = memberPayService.isMemember(member.getId());
		//		if(isMember){
		//		    request.setAttribute("isMember", 1);
		//		}
	    }
	    request.setAttribute( "member", member );
	    request.setAttribute( "imageHttp", PropertiesUtil.getResourceUrl() );

	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		int shopId = CommonUtil.toInteger( params.get( "shopId" ) );
		shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
		request.setAttribute( "shopId", shopId );
	    }
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入积分商品明细的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/integral_product";
    }

    /**
     * 收货地址列表
     *
     * @param session
     * @param request
     * @param params
     *
     * @return
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( value = "/79B4DE7C/addressList" )
    public String addressList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, HttpSession session ) {
	logger.info( "进入收货地址列表页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    params = orderService.getMemberParams( member, params );
	    List addressList = orderService.selectShipAddress( params );
	    request.setAttribute( "addressList", addressList );


	    /*BusUser user = pageService.selUserByMember(member);
	    if(CommonUtil.isNotEmpty(user)){
		    if(CommonUtil.isNotEmpty(user.getAdvert())){
			    if(user.getAdvert() == 0){
				    request.setAttribute("isAdvert", 1);
			    }
		    }
	    }*/
	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		int shopId = CommonUtil.toInteger( params.get( "shopId" ) );
		shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
		request.setAttribute( "shopId", shopId );
	    }
	    pageService.getCustomer( request, userid );
	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( member ) ) {
		//TODO 需关连 t_wx_public_users 用户信息
		//		Map<String, Object> publicMap = pageService.publicUserid(userid);
		//		if(CommonUtil.isNotEmpty(publicMap)){
		//		    CommonUtil.getWxParams(morderService.getWpUser(member.getId()),request);
		//		}
	    }
	    Object obj = request.getSession().getAttribute( Constants.SESSION_KEY + "integral_order" );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		request.setAttribute( "orders", obj );
	    } else if ( CommonUtil.isNotEmpty( params.get( "orders" ) ) ) {
		request.setAttribute( "orders", params.get( "orders" ) );
	    }
	} catch ( Exception e ) {
	    logger.error( "收货地址列表页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/address_list";
    }

    /**
     * 跳转至新增/修改收货地址页面
     *
     * @param request
     * @param params
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/toAddress" )
    public String toAddress( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入跳转至新增/修改收货地址页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询省份数据
	    //TODO 需关连 省份 方法
	    //	    List<Map<String, Object>> maps=wxShopService.queryCityByLevel(2);
	    //	    request.setAttribute("maps", maps);
	    Object id = params.get( "id" );
	    if ( null != id && !id.equals( "" ) ) {//修改地址查询
		//TODO 需关连地址 方法
		//		MemberAddress mem = eatPhoneService.getMemberAddress(Integer.parseInt(id.toString()));
		//		request.setAttribute("mem", mem);
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		int shopId = CommonUtil.toInteger( params.get( "shopId" ) );
		shopId = storeService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
		request.setAttribute( "shopId", shopId );

		boolean isJuli = orderService.isJuliByFreight( shopId + "" );
		if ( isJuli ) {
		    request.setAttribute( "isJuliFreight", 1 );
		}
	    }
	    Object sessionObjs = request.getSession().getAttribute( Constants.SESSION_KEY + "mall_isnoLogin" );
	    if ( CommonUtil.isNotEmpty( sessionObjs ) ) {
		MemberAddress mem = (MemberAddress) JSONObject.toBean( JSONObject.fromObject( sessionObjs ), MemberAddress.class );
		request.setAttribute( "mem", mem );
	    }
	    pageService.getCustomer( request, userid );
	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( member ) ) {
		//TODO 需关连 t_wx_public_users 用户信息
		//		Map<String, Object> publicMap = pageService.publicUserid(userid);
		//		if(CommonUtil.isNotEmpty(publicMap)){
		//		    CommonUtil.getWxParams(morderService.getWpUser(member.getId()),request);
		//		}
	    }
	    Object obj = request.getSession().getAttribute( Constants.SESSION_KEY + "integral_order" );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		request.setAttribute( "orders", obj );
	    } else if ( CommonUtil.isNotEmpty( params.get( "orders" ) ) ) {
		request.setAttribute( "orders", params.get( "orders" ) );
	    }
	} catch ( Exception e ) {
	    logger.error( "跳转至新增/修改收货地址页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/integral/phone/address_edit";
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
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		resultMap.put( "code", -2 );
		request.getSession().setAttribute( Constants.SESSION_KEY + "integral_order", params );
	    } else {
		//TODO 需关连 Member 会员信息
		//		member = memberService.findById(member.getId());
		//TODO 判断浏览器
		Integer browser = 1;
//		CommonUtil.judgeBrowser( request, member.getBusid() );
		if ( browser != 1 ) {
		    browser = 2;
		}
		resultMap = integralService.recordIntegral( params, member, browser, request );
		if ( resultMap.get( "code" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( resultMap.get( "proTypeId" ) ) ) {
		    request.getSession().setAttribute( Constants.SESSION_KEY + "integral_order", params );
		}
	    }
	} catch ( Exception e ) {
	    resultMap.put( "code", -1 );

	    logger.error( "兑换积分异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

}
