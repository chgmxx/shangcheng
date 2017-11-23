package com.gt.mall.controller.applet.phone;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.applet.MallHomeAppletService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.applet.MallOrderAppletService;
import com.gt.mall.service.web.applet.MallProductAppletService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城小程序控制层
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallApplet" )
public class PhoneHomeAppletController extends BaseController {

    @Autowired
    private MallPageService           pageService;
    @Autowired
    private MallHomeAppletService     mallHomeAppletService;
    @Autowired
    private MallProductAppletService  mallProductAppletService;
    @Autowired
    private MallOrderAppletService    mallOrderAppletService;
    @Autowired
    private MallStoreService          storeService;
    @Autowired
    private MallOrderService          morderService;
    @Autowired
    private MallNewOrderAppletService mallNewOrderAppletService;
    @Autowired
    private MemberService             memberService;
    @Autowired
    private WxShopService             wxShopService;
    @Autowired
    private MemberAddressService      memberAddressService;
    @Autowired
    private MallCommentService        mallCommentService;

    /**
     * 进入店铺列表页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/shopList" )
    public void shopList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入店铺列表的参数：" + params );
	    //查询店铺列表
	    List< Map< String,Object > > shopList = mallHomeAppletService.getShopList( params );
	    resultMap.put( "shopList", shopList );

	} catch ( Exception e ) {
	    logger.error( "进入店铺列表异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 商城首页
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/pageIndex" )
    public void pageIndex( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入商城首页参数：" + params );
	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    List< Map< String,Object > > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "firstList", firstList );

	    //查询全部商品
	    PageUtil productPage = mallHomeAppletService.productAllList( params );
	    resultMap.put( "productPage", productPage );

	    //查询商品活动
	    Map< String,Object > activityMap = mallHomeAppletService.getActivityList( params );
	    resultMap.putAll( activityMap );
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
		if ( memberId > 0 ) {
		    Member member = memberService.findMemberById( memberId, null );
		    params.put( "userId", member.getBusid() );
		    //查询首页轮播图
		    List< Map< String,Object > > imageList = mallHomeAppletService.getAppletImageByBusUser( params );
		    resultMap.put( "imageList", imageList );
		}
	    }
	    resultMap.put( "imageHttp", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "商城小程序首页异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 分页查询商品
     *
     * @param request
     * @param response
     * @param params
     */
    @RequestMapping( "/79B4DE7C/productPage" )
    public void productPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    logger.info( "分页查询商品参数：" + params );
	    //分页查询商品
	    PageUtil productPage = mallHomeAppletService.productAllList( params );

	    CommonUtil.write( response, JSONObject.fromObject( productPage ) );

	} catch ( Exception e ) {
	    logger.error( "分页查询商品异常。。。" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 全部分类
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/classAll" )
    public void classAll( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入全部分类页面参数：" + params );
	    //查询子分类
	    List< Map< String,Object > > classList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "classList", classList );

	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    params.remove( "classId" );
	    List< Map< String,Object > > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "firstList", firstList );

	} catch ( Exception e ) {
	    logger.error( "进入全部分类页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 全部商品
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/productAll" )
    public void productAll( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入全部商品页面参数：" + params );
	    //查询商品信息
	    PageUtil productPage = mallHomeAppletService.productAllList( params );
	    resultMap.put( "productPage", productPage );
	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    List< Map< String,Object > > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "firstList", firstList );
	} catch ( Exception e ) {
	    logger.error( "进入全部商品页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 查询商品的规格
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/getProductSpecifica" )
    public void getProductSpecifica( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "查询商品的规格参数：" + params );

	    resultMap = mallProductAppletService.getProductSpecifica( params );

	} catch ( Exception e ) {
	    logger.error( "查询商品的规格异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 商品详细页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/phoneProduct" )
    public void phoneProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入商品详细页面参数：" + params );
	    //查询商品信息
	    resultMap = mallHomeAppletService.selectProductDetailById( params );

	} catch ( Exception e ) {
	    logger.error( "进入商品详细页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 查询商品的评价
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/getProductComment" )
    public void getProductComment( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "查询商品评价的参数：" + params );
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) && CommonUtil.isNotEmpty( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) ) {
		Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
		String feel = "";
		if ( CommonUtil.isNotEmpty( params.get( "feel" ) ) ) {
		    feel = CommonUtil.toString( params.get( "feel" ) );
		}
		resultMap = mallCommentService.getProductComment( member.getBusid(), CommonUtil.toInteger( params.get( "proId" ) ), feel );
		resultMap.put( "imageHttp", PropertiesUtil.getResourceUrl() );
	    }

	} catch ( Exception e ) {
	    logger.error( "查询商品评价出错:" + e );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 商品加入到购物车里
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "商城小程序-商品购物车保存", op_function = "2" )
    @RequestMapping( "/79B4DE7C/addshopping" )
    public void addshopping( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "商品加入购物车参数：" + params );

	    resultMap = mallHomeAppletService.addshopping( params );
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "message", "加入购物车失败！" );
	    logger.error( "商品加入购物车异常:" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 进入购物车页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/shopCart" )
    public void shopCart( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    logger.info( "进入购物车页面的参数：" + params );

	    resultMap = mallProductAppletService.shoppingcare( params, request );

	} catch ( Exception e ) {
	    logger.error( "进入购物车页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 清空购物车信息
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SuppressWarnings( { "deprecation", "unchecked" } )
    @SysLogAnnotation( description = "小程序购物车页面-删除购物车内的商品", op_function = "3" )
    @RequestMapping( "79B4DE7C/shoppingdelete" )
    public void shoppingdelect( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "删除购物车内的商品参数：" + params );
	    List< Integer > ids = new ArrayList< Integer >();
	    if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {
		ids = JSONArray.toList( JSONArray.fromObject( params.get( "ids" ) ) );
	    }
	    mallProductAppletService.shoppingdelete( ids );
	    resultMap.put( "code", "1" );
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "删除商品失败" );
	    logger.error( "删除购物车内商品失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 去结算购物车信息
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "小程序购物车页面-结算购物车内的商品", op_function = "3" )
    @RequestMapping( "79B4DE7C/shoppingorder" )
    public void shoppingOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "购物车去结算的参数：" + params );

	    mallProductAppletService.shoppingorder( params );
	    resultMap.put( "code", "1" );
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "结算购物车失败" );
	    logger.error( "结算购物车失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 我的页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/memberIndex" )
    public void memberIndex( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入我的页面的参数：" + params );
	    resultMap = mallHomeAppletService.getMemberPage( params );
	} catch ( Exception e ) {
	    logger.error( "我的页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 立即购买
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/productBuyNow" )
    public void productBuyNow( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "立即购买的参数：" + params );
	    resultMap = mallOrderAppletService.productBuyNow( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "立即失败" );
	    logger.error( "立即购买异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 进入提交订单的页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/toSubmitOrder" )
    public void toSubmitOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入提交订单页面的参数：" + params );
	    String version = "";//保存版本号
	    if ( CommonUtil.isNotEmpty( params.get( "version" ) ) ) {
		version = CommonUtil.toString( params.get( "version" ) );
	    }
	    if ( version.equals( "1.1.0" ) ) {
		resultMap = mallNewOrderAppletService.toSubmitOrder( params );
	    } else {
		resultMap = mallOrderAppletService.toSubmitOrder( params );
	    }

	} catch ( Exception e ) {
	    logger.error( "进入提交订单的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 提交订单
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/submitOrder" )
    public void submitOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "提交订单的参数：" + params );
	    String version = "";//保存版本号
	    if ( CommonUtil.isNotEmpty( params.get( "version" ) ) ) {
		version = CommonUtil.toString( params.get( "version" ) );
	    }
	    if ( version.equals( "1.1.0" ) ) {
		resultMap = mallNewOrderAppletService.submitOrder( params );
	    } else {
		resultMap = mallOrderAppletService.submitOrder( params );
	    }

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "提交订单失败" );
	    logger.error( "提交订单异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    logger.info( "提交订单返回参数：" + resultMap );
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 订单列表页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/orderList" )
    public void orderList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入订单列表页面的参数：" + params );

	    PageUtil page = mallOrderAppletService.getOrderList( params );
	    resultMap.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "订单列表页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 订单详情 页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/orderDetail" )
    public void orderDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入订单详细页面的参数：" + params );
	    resultMap = mallOrderAppletService.getOrderDetail( params );

	} catch ( Exception e ) {
	    logger.error( "订单列表页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 微信订单支付
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/appletWxOrder" )

    public void appletWxOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "微信订单支付的参数：" + params );
	    String obj = mallOrderAppletService.appletWxOrder( params );
	    resultMap.put( "params", obj );
	} catch ( Exception e ) {
	    logger.error( "微信订单支付异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 订单去支付
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "小程序我的订单页面-去支付", op_function = "3" )
    @RequestMapping( "79B4DE7C/orderGoPay" )
    public void orderGoPay( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< Object,Object > resultMap = new HashMap< Object,Object >();
	try {
	    logger.info( "去支付的参数：" + params );
	    resultMap = mallOrderAppletService.orderGoPay( params, CommonUtil.getpath( request ) );
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "订单去支付失败" );
	    logger.error( "订单去支付失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 确认收货
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "小程序我的订单页面-确认收货", op_function = "3" )
    @RequestMapping( "79B4DE7C/confirmReceipt" )
    public void confirmReceipt( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "确认收货的参数：" + params );
	    resultMap = mallOrderAppletService.confirmReceipt( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "确认收货失败" );
	    logger.error( "确认收货失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 进入退款页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/toReturnOrder" )
    public void toReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入退款页面的参数：" + params );

	    resultMap = mallOrderAppletService.toReturnOrder( params );

	} catch ( Exception e ) {
	    logger.error( "进入退款页面异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 提交退款信息
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "小程序我的订单页面-提交退款信息", op_function = "3" )
    @RequestMapping( "79B4DE7C/submitReturnOrder" )
    public void submitReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "提交退款信息的参数：" + params );
	    resultMap = mallOrderAppletService.submitReturnOrder( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "提交退款失败" );
	    logger.error( "提交退款失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 撤销退款
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @SysLogAnnotation( description = "小程序我的订单页面-撤销退款", op_function = "3" )
    @RequestMapping( "79B4DE7C/closeReturnOrder" )
    public void closeReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "撤销退款的参数：" + params );
	    resultMap = mallOrderAppletService.closeReturnOrder( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "撤销退款失败" );
	    logger.error( "撤销退款失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 进入我的地址页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/addressList" )
    public void addressList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入我的地址页面的参数：" + params );
	    resultMap = mallOrderAppletService.addressList( params );

	} catch ( Exception e ) {
	    logger.error( "进入我的地址页面失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 设置默认地址
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/addressDefault" )
    public void addressDefault( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "设置默认地址的参数：" + params );

	    int id = CommonUtil.toInteger( params.get( "id" ) );
	    int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	    boolean reuslt = memberAddressService.updateDefault( id, memberId );
	    if ( reuslt ) {
		resultMap.put( "code", 1 );
	    } else {
		resultMap.put( "code", -1 );
		resultMap.put( "errorMsg", "设置默认地址失败" );
	    }

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "设置默认地址失败" );
	    logger.error( "设置默认地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 删除地址
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/addressDelete" )
    public void addressDelete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "删除地址的参数：" + params );
	    //	    resultMap = mallOrderAppletService.addressDefault( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "设置默认地址失败" );
	    logger.error( "设置默认地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 进入修改地址的页面
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/toAddress" )
    public void toAddress( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入修改地址页面的参数：" + params );
	    //查询省份列表
	    List< Map > provinceList = wxShopService.queryCityByLevel( 2 );

	    resultMap.put( "provinceList", provinceList );

	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		MemberAddress address = memberAddressService.addreSelectId( CommonUtil.toInteger( params.get( "id" ) ) );
		resultMap.put( "address", address );
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		boolean isJuli = morderService.isJuliByFreight( params.get( "shopId" ).toString() );
		if ( isJuli ) {
		    resultMap.put( "isJuliFreight", 1 );
		}
	    }

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "提交地址失败" );
	    logger.error( "提交地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 提交地址信息
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/addressSubmit" )
    public void addressSubmit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "提交地址信息的参数：" + params );

	    MemberAddress memAddress = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( params ), MemberAddress.class );
	    boolean msg = memberAddressService.addOrUpdateAddre( memAddress );

	    if ( msg ) {
		resultMap.put( "code", 1 );
	    } else {
		resultMap.put( "code", -1 );
		resultMap.put( "errorMsg", "保存地址信息失败" );
	    }

	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "保存地址失败" );
	    logger.error( "保存地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( resultMap ) );
	}
    }

    /**
     * 查询城市数据
     *
     * @param params
     */
    @RequestMapping( value = "79B4DE7C/queryCity" )
    public void queryCity( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int id = CommonUtil.toInteger( params.get( "id" ) );
	    List< Map > maps = wxShopService.queryCityByParentId( id );
	    CommonUtil.write( response, maps );
	} catch ( IOException e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 获取短信验证码
     *
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/getValCode" )
    public void getValCode( HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = null;
	try {
	    logger.info( "商城小程序获取短信验证码参数：" + params );
	    resultMap = mallHomeAppletService.getValCode( params );

	} catch ( Exception e ) {
	    resultMap = new HashMap< String,Object >();
	    resultMap.put( "result", false );
	    resultMap.put( "message", "获取短信验证码失败" );
	    logger.error( "商城小程序获取短信验证码失败" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 绑定手机号码
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/bindPhone" )
    public void bindPhone( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = null;
	logger.info( "商城小程序绑定手机号码参数：" + params );
	try {
	    resultMap = mallHomeAppletService.bindPhones( params );

	} catch ( Exception e ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "rsult", false );
	    resultMap.put( "message", "绑定手机号码！" );
	    logger.error( "商城小程序绑定手机号码失败" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    logger.info( "商城小程序绑定手机号码返回值：" + resultMap );
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 计算订单的优惠信息  1.1版新增接口
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/calculationPreferential" )
    public void calculationPreferential( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = null;
	logger.info( "商城小程序计算订单的优惠信息：" + params );
	try {
	   /* resultMap = mallNewOrderAppletService.calculationPreferential( params );*/
	    resultMap = mallNewOrderAppletService.newCalculationPreferential( params );
	} catch ( Exception e ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "计算订单的优惠信息失败" );
	    logger.error( "商城小程序计算订单的优惠信息失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {

	    logger.info( "商城小程序计算订单的优惠返回结果：" + resultMap );

	    CommonUtil.write( response, resultMap );
	}
    }

}
