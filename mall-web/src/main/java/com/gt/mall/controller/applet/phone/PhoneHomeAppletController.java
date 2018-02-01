package com.gt.mall.controller.applet.phone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.applet.AppletAddReturnOrderDTO;
import com.gt.mall.param.applet.AppletSubmitOrderDTO;
import com.gt.mall.param.applet.AppletToSubmitOrderDTO;
import com.gt.mall.param.applet.ProductSearchDTO;
import com.gt.mall.result.applet.AppletMallIndexResult;
import com.gt.mall.result.applet.AppletProductDetailResult;
import com.gt.mall.result.applet.AppletShopResult;
import com.gt.mall.result.applet.param.AppletGroupDTO;
import com.gt.mall.result.applet.param.AppletImageDTO;
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
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping( "/mallApplet/79B4DE7C" )
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
     */
    @RequestMapping( value = "shopList", method = RequestMethod.GET )
    public void shopList( HttpServletRequest request, HttpServletResponse response, Integer userId, String longitude, String latitude ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "userId", userId );
	    params.put( "longitude", longitude );
	    params.put( "latitude", latitude );
	    logger.info( "进入店铺列表的参数：" + params );
	    //查询店铺列表
	    List< AppletShopResult > shopList = mallHomeAppletService.getShopList( params );
	    resultMap.put( "shopList", shopList );

	} catch ( Exception e ) {
	    logger.error( "进入店铺列表异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 商城首页
     *
     * @param shopId   店铺ID
     * @param memberId 会员ID
     */
    @RequestMapping( value = "pageIndex", method = RequestMethod.GET )
    public void pageIndex( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer memberId ) throws IOException {
	AppletMallIndexResult indexResult = new AppletMallIndexResult();
	try {
	    //	    Member member = MallSessionUtils.getLoginMember( request );
	    //	    member.getPublicId()
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "memberId", memberId );
	    EntityDtoConverter converter = new EntityDtoConverter();
	    logger.info( "进入商城首页参数：" + params );
	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    List< AppletGroupDTO > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    indexResult.setFirstList( firstList );

	    //查询全部商品
	    PageUtil productPage = mallHomeAppletService.productAllList( params );
	    indexResult.setProductPage( productPage );
	    //查询商品活动
	    Map< String,Object > activityMap = mallHomeAppletService.getActivityList( params );
	    converter.mapToBean( activityMap, indexResult );
	    int busUserId = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		if ( memberId > 0 ) {
		    Member member = memberService.findMemberById( memberId, null );
		    if ( CommonUtil.isNotEmpty( member ) ) {
			busUserId = member.getBusid();
		    }
		}
	    }
	    if ( busUserId == 0 && shopId > 0 ) {
		MallStore store = storeService.selectById( shopId );
		if ( CommonUtil.isNotEmpty( store ) ) {
		    busUserId = store.getStoUserId();
		}
	    }
	    if ( busUserId > 0 ) {
		params.put( "userId", busUserId );
		//查询首页轮播图
		List< AppletImageDTO > imageList = mallHomeAppletService.getAppletImageByBusUser( params );
		indexResult.setImageList( imageList );
	    }
	    indexResult.setImageHttp( PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "商城小程序首页异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( indexResult ) ) );
	}
    }

    /**
     * 分页查询商品
     */
    @RequestMapping( value = "productPage", method = RequestMethod.GET )
    public void productPage( HttpServletRequest request, HttpServletResponse response, ProductSearchDTO searchDTO ) {
	try {
	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = converter.beanToMap( searchDTO );
	    logger.info( "分页查询商品参数：" + params );
	    //分页查询商品
	    PageUtil productPage = mallHomeAppletService.productAllList( params );

	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( productPage ) ) );

	} catch ( Exception e ) {
	    logger.error( "分页查询商品异常。。。" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 全部分类
     *
     * @param shopId   店铺ID
     * @param memberId 会员ID
     * @param classId  父级分类ID
     */
    @RequestMapping( value = "classAll", method = RequestMethod.GET )
    public void classAll( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer memberId, Integer classId ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "memberId", memberId );
	    params.put( "classId", classId );
	    logger.info( "进入全部分类页面参数：" + params );
	    //查询子分类
	    List< AppletGroupDTO > classList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "classList", classList );

	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    params.remove( "classId" );
	    List< AppletGroupDTO > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "firstList", firstList );

	} catch ( Exception e ) {
	    logger.error( "进入全部分类页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 全部商品
     */
    @RequestMapping( value = "productAll", method = RequestMethod.GET )
    public void productAll( HttpServletRequest request, HttpServletResponse response, ProductSearchDTO searchDTO ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = converter.beanToMap( searchDTO );
	    logger.info( "进入全部商品页面参数：" + params );
	    //查询商品信息
	    PageUtil productPage = mallHomeAppletService.productAllList( params );
	    resultMap.put( "productPage", productPage );
	    //查询商品一级分类
	    params.put( "isFrist", 1 );
	    List< AppletGroupDTO > firstList = mallHomeAppletService.selectGroupsByShopId( params );
	    resultMap.put( "firstList", firstList );
	} catch ( Exception e ) {
	    logger.error( "进入全部商品页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 查询商品的规格
     * 无效接口
     */
    @RequestMapping( value = "getProductSpecifica", method = RequestMethod.GET )
    public void getProductSpecifica( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "查询商品的规格参数：" + params );

	    resultMap = mallProductAppletService.getProductSpecifica( params );

	} catch ( Exception e ) {
	    logger.error( "查询商品的规格异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 商品详细页面
     *
     * @param shopId    店铺ID
     * @param memberId  会员ID
     * @param productId 商品ID
     * @param province  默认为0
     */
    @RequestMapping( value = "phoneProduct", method = RequestMethod.GET )
    public void phoneProduct( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer memberId, Integer productId, Integer province ) throws IOException {
	AppletProductDetailResult productDetailResult = new AppletProductDetailResult();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "memberId", memberId );
	    params.put( "productId", productId );
	    params.put( "province", province );
	    logger.info( "进入商品详细页面参数：" + params );
	    //查询商品信息
	    productDetailResult = mallHomeAppletService.selectProductDetailById( params );

	} catch ( Exception e ) {
	    logger.error( "进入商品详细页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( productDetailResult ) ) );
	}
    }

    /**
     * 查询商品的评价
     *
     * @param memberId 会员ID
     * @param proId    商品ID
     * @param feel     评论状态
     */
    @RequestMapping( value = "getProductComment", method = RequestMethod.GET )
    public void getProductComment( HttpServletRequest request, HttpServletResponse response, Integer memberId, Integer proId, String feel ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "查询商品评价的参数：memberId=" + memberId + "proId=" + proId + "feel=" + feel );
	    if ( CommonUtil.isNotEmpty( memberId ) && CommonUtil.isNotEmpty( CommonUtil.isNotEmpty( proId ) ) ) {
		Member member = memberService.findMemberById( memberId, null );
		resultMap = mallCommentService.getProductComment( member.getBusid(), proId, feel );
		resultMap.put( "imageHttp", PropertiesUtil.getResourceUrl() );
	    }
	} catch ( Exception e ) {
	    logger.error( "查询商品评价出错:" + e );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 商品加入到购物车里
     */
    @SysLogAnnotation( description = "商城小程序-商品购物车保存", op_function = "2" )
    @RequestMapping( value = "addshopping", method = RequestMethod.GET )
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
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 进入购物车页面
     *
     * @param shopId    店铺ID
     * @param memberId  会员ID
     * @param busUserId 商家ID
     */
    @RequestMapping( value = "shopCart", method = RequestMethod.GET )
    public void shopCart( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer memberId, Integer busUserId ) throws IOException {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "memberId", memberId );
	    params.put( "busUserId", busUserId );
	    logger.info( "进入购物车页面的参数：" + params );

	    resultMap = mallProductAppletService.shoppingcare( params, request );

	} catch ( Exception e ) {
	    logger.error( "进入购物车页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 清空购物车信息
     *
     * @param memberId 会员ID
     * @param ids      购物车ID集合
     */
    @SuppressWarnings( { "deprecation", "unchecked" } )
    @SysLogAnnotation( description = "小程序购物车页面-删除购物车内的商品", op_function = "3" )
    @RequestMapping( value = "shoppingdelete", method = RequestMethod.POST )
    public void shoppingdelect( HttpServletRequest request, HttpServletResponse response, Integer memberId, String ids ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "删除购物车内的商品参数：" + ids );
	    List< Integer > idList = new ArrayList< Integer >();
	    if ( CommonUtil.isNotEmpty( ids ) ) {
		idList = JSONArray.toList( JSONArray.fromObject( ids ) );
	    }
	    mallProductAppletService.shoppingdelete( idList );
	    resultMap.put( "code", "1" );
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "删除商品失败" );
	    logger.error( "删除购物车内商品失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 去结算购物车信息
     */
    @SysLogAnnotation( description = "小程序购物车页面-结算购物车内的商品", op_function = "3" )
    @RequestMapping( value = "shoppingorder", method = RequestMethod.GET )
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
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 我的页面
     *
     * @param memberId 会员ID
     */
    @RequestMapping( value = "memberIndex", method = RequestMethod.GET )
    public void memberIndex( HttpServletRequest request, HttpServletResponse response, Integer memberId ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入我的页面的参数：" + memberId );
	    resultMap = mallHomeAppletService.getMemberPage( memberId );
	} catch ( Exception e ) {
	    logger.error( "我的页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 立即购买 无效接口
     */
    @RequestMapping( value = "productBuyNow", method = RequestMethod.GET )
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
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 进入提交订单的页面
     */
    @RequestMapping( value = "toSubmitOrder", method = RequestMethod.GET )
    public void toSubmitOrder( HttpServletRequest request, HttpServletResponse response, AppletToSubmitOrderDTO toSubmitOrderDTO ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入提交订单页面的参数：" + toSubmitOrderDTO );
	    String version = "";//保存版本号
	    if ( CommonUtil.isNotEmpty( toSubmitOrderDTO.getVersion() ) ) {
		version = toSubmitOrderDTO.getVersion();
	    }

	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = converter.beanToMap( toSubmitOrderDTO );
	    if ( version.equals( "1.1.0" ) ) {
		resultMap = mallNewOrderAppletService.toSubmitOrder( params );
	    } else {
		resultMap = mallOrderAppletService.toSubmitOrder( params );
	    }

	} catch ( Exception e ) {
	    logger.error( "进入提交订单的页面异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 提交订单
     */
    @RequestMapping( "submitOrder" )
    public void submitOrder( HttpServletRequest request, HttpServletResponse response, AppletSubmitOrderDTO submitOrderDTO ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = converter.beanToMap( submitOrderDTO );
	    logger.info( "提交订单的参数：" + params );
	    String version = "";//保存版本号
	    if ( CommonUtil.isNotEmpty( submitOrderDTO.getVersion() ) ) {
		version = submitOrderDTO.getVersion();
	    }
	    if ( version.equals( "1.1.1" ) ) {
		resultMap = mallNewOrderAppletService.submitOrder( submitOrderDTO );
	    } else {
		resultMap = mallOrderAppletService.submitOrder( params );
	    }

	} catch ( BusinessException be ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", be.getMessage() );
	    be.printStackTrace();
	} catch ( Exception e ) {
	    resultMap.put( "code", "-1" );
	    resultMap.put( "errorMsg", "提交订单失败" );
	    logger.error( "提交订单异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    logger.info( "提交订单返回参数：" + resultMap );
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 订单列表页面
     *
     * @param memberId  会员ID
     * @param curPage   页数
     * @param busUserId 商家ID
     * @param type      0 全部 1待支付 2待发货 3待收货  4已完成
     */
    @RequestMapping( "orderList" )
    public void orderList( HttpServletRequest request, HttpServletResponse response, Integer memberId, Integer curPage, Integer busUserId, Integer type ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "memberId", memberId );
	    params.put( "curPage", curPage );
	    params.put( "busUserId", busUserId );
	    params.put( "type", type );
	    logger.info( "进入订单列表页面的参数：" + params );

	    PageUtil page = mallOrderAppletService.getOrderList( params );
	    resultMap.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "订单列表页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 订单详情 页面
     */
    @RequestMapping( "orderDetail" )
    public void orderDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入订单详细页面的参数：" + params );
	    resultMap = mallOrderAppletService.getOrderDetail( params );

	} catch ( Exception e ) {
	    logger.error( "订单列表页面异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 微信订单支付
     *
     * @param orderNo  订单号
     * @param memberId 会员ID
     * @param appid    appid
     */
    @RequestMapping( "appletWxOrder" )
    public void appletWxOrder( HttpServletRequest request, HttpServletResponse response, String orderNo, Integer memberId, String appid ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderNo", orderNo );//订单号
	    params.put( "memberId", memberId );//会员ID
	    params.put( "appid", appid );//appid
	    logger.info( "微信订单支付的参数：" + params );
	    resultMap = mallOrderAppletService.appletWxOrder( params );
	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "获取支付信息失败！" );
	    logger.error( "微信订单支付异常。。。" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 订单去支付
     *
     * @param order_id 订单ID
     * @param memberId 会员ID
     * @param appid    appid
     */
    @SysLogAnnotation( description = "小程序我的订单页面-去支付", op_function = "3" )
    @RequestMapping( "orderGoPay" )
    public void orderGoPay( HttpServletRequest request, HttpServletResponse response, Integer order_id, Integer memberId, String appid ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "order_id", order_id );//订单ID
	    params.put( "memberId", memberId );//会员ID
	    params.put( "appid", appid );//appid
	    logger.info( "去支付的参数：" + params );
	    resultMap = mallOrderAppletService.orderGoPay( params, CommonUtil.getpath( request ) );
	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "订单去支付失败" );
	    logger.error( "订单去支付失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 确认收货
     */
    @SysLogAnnotation( description = "小程序我的订单页面-确认收货", op_function = "3" )
    @RequestMapping( "confirmReceipt" )
    public void confirmReceipt( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "确认收货的参数：" + params );
	    resultMap = mallOrderAppletService.confirmReceipt( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "确认收货失败" );
	    logger.error( "确认收货失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 进入退款页面
     */
    @RequestMapping( "toReturnOrder" )
    public void toReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入退款页面的参数：" + params );

	    resultMap = mallOrderAppletService.toReturnOrder( params );

	} catch ( Exception e ) {
	    logger.error( "进入退款页面异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 提交退款信息
     */
    @SysLogAnnotation( description = "小程序我的订单页面-提交退款信息", op_function = "3" )
    @RequestMapping( "submitReturnOrder" )
    public void submitReturnOrder( HttpServletRequest request, HttpServletResponse response, AppletAddReturnOrderDTO addReturnOrderDTO ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = converter.beanToMap( addReturnOrderDTO );
	    logger.info( "提交退款信息的参数：" + addReturnOrderDTO );
	    resultMap = mallOrderAppletService.submitReturnOrder( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "提交退款失败" );
	    logger.error( "提交退款失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 撤销退款
     */
    @SysLogAnnotation( description = "小程序我的订单页面-撤销退款", op_function = "3" )
    @RequestMapping( "closeReturnOrder" )
    public void closeReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "撤销退款的参数：" + params );
	    resultMap = mallOrderAppletService.closeReturnOrder( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "撤销退款失败" );
	    logger.error( "撤销退款失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 进入我的地址页面
     */
    @RequestMapping( "addressList" )
    public void addressList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "进入我的地址页面的参数：" + params );
	    resultMap = mallOrderAppletService.addressList( params );

	} catch ( Exception e ) {
	    logger.error( "进入我的地址页面失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 设置默认地址
     */
    @RequestMapping( "addressDefault" )
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
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "设置默认地址失败" );
	    logger.error( "设置默认地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 删除地址
     */
    @RequestMapping( "addressDelete" )
    public void addressDelete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    logger.info( "删除地址的参数：" + params );
	    //	    resultMap = mallOrderAppletService.addressDefault( params );

	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "设置默认地址失败" );
	    logger.error( "设置默认地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 进入修改地址的页面
     */
    @RequestMapping( "toAddress" )
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
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "提交地址失败" );
	    logger.error( "提交地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 提交地址信息
     */
    @RequestMapping( "addressSubmit" )
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
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "保存地址失败" );
	    logger.error( "保存地址失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.parseObject( JSON.toJSONString( resultMap ) ) );
	}
    }

    /**
     * 查询城市数据
     */
    @RequestMapping( value = "queryCity" )
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
     */
    @RequestMapping( "getValCode" )
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
     */
    @RequestMapping( "bindPhone" )
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
     */
    @RequestMapping( "calculationPreferential" )
    public void calculationPreferential( HttpServletRequest request, HttpServletResponse response, @RequestParam AppletSubmitOrderDTO submitOrderDTO ) throws IOException {
	Map< String,Object > resultMap = new HashMap<>();
	logger.info( "商城小程序计算订单的优惠信息：" + submitOrderDTO );
	try {
	    resultMap = mallNewOrderAppletService.newCalculationPreferential( submitOrderDTO );
	} catch ( BusinessException be ) {
	    resultMap.put( "code", be.getCode() );
	    resultMap.put( "errorMsg", be.getMessage() );
	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.APPLET_ERROR.getCode() );
	    resultMap.put( "errorMsg", "计算订单的优惠信息失败" );
	    logger.error( "商城小程序计算订单的优惠信息失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {

	    logger.info( "商城小程序计算订单的优惠返回结果：" + resultMap );

	    CommonUtil.write( response, resultMap );
	}
    }

    @ApiOperation( value = "获取所有国家区号列表", notes = "获取所有国家区号列表" )
    @ResponseBody
    @RequestMapping( value = "/areaPhoneList", method = RequestMethod.GET )
    public ServerResponse areaPhoneList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map > list = null;
	try {
	    list = memberAddressService.areaPhoneList();
	} catch ( Exception e ) {
	    logger.error( "获取所有国家区号列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取所有国家区号列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), list, false );
    }

}
