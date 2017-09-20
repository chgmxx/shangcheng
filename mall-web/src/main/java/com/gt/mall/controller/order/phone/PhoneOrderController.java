package com.gt.mall.controller.order.phone;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.basic.MallTakeTheirTimeService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.union.api.entity.param.BindCardParam;
import com.gt.union.api.entity.param.UnionPhoneCodeParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/phoneOrder" )
public class PhoneOrderController extends AuthorizeOrLoginController {

    @Autowired
    private MallOrderService         mallOrderService;
    @Autowired
    private MallOrderDAO             mallOrderDAO;
    @Autowired
    private MallPageService          pageService;
    @Autowired
    private MallProductService       mallProductService;
    @Autowired
    private MallGroupBuyService      mallGroupBuyService;
    @Autowired
    private MallStoreService         mallStoreService;
    @Autowired
    private MallSeckillService       mallSeckillService;
    @Autowired
    private MallTakeTheirTimeService mallTakeTheirTimeService;
    @Autowired
    private MallTakeTheirService     mallTakeTheirService;
    @Autowired
    private MallSellerService        mallSellerService;
    @Autowired
    private MallPaySetService        mallPaySetService;
    @Autowired
    private MallGroupJoinDAO         mallGroupJoinDAO;
    @Autowired
    private MallShopCartService      mallShopCartService;
    @Autowired
    private MemberService            memberService;
    @Autowired
    private DictService              dictService;
    @Autowired
    private WxPublicUserService      wxPublicUserService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private MallPageService          mallPageService;
    @Autowired
    private MallOrderNewService      mallOrderNewService;
    @Autowired
    private UnionCardService         unionCardService;
    @Autowired
    private MemberAddressService     memberAddressService;
    @Autowired
    private BusUserService           busUserService;

    /**
     * 跳转至提交订单页面
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @RequestMapping( value = "/79B4DE7C/toOrder" )
    public String toOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > data ) {
	logger.info( "进入手机订单页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    String key = "to_order";
	    Object orderObj = SessionUtils.getSession( request, key );

	    int userid = 0;
	    String type = "";
	    if ( data.containsKey( "type" ) ) {
		type = CommonUtil.toString( data.get( "type" ) );
	    }
	    if ( CommonUtil.isEmpty( data.get( "data" ) ) ) {
		if ( CommonUtil.isNotEmpty( orderObj ) ) {
		    Map< String,Object > maps = (Map< String,Object >) JSONObject.toBean( JSONObject.fromObject( orderObj ), Map.class );
		    data.putAll( maps );

		    if ( CommonUtil.isNotEmpty( maps.get( "order" ) ) ) {
			List< MallOrder > orderList = com.alibaba.fastjson.JSONArray.parseArray( maps.get( "order" ).toString(), MallOrder.class );
			if ( orderList != null && orderList.size() > 0 ) {
			    MallOrder order = orderList.get( 0 );
			    request.setAttribute( "mallOrder", order );
			    request.setAttribute( "orderPayWays", order.getOrderPayWay() );
			    request.setAttribute( "deliveryMethod", order.getDeliveryMethod() );
			    if ( CommonUtil.isNotEmpty( order.getFlowPhone() ) ) {
				request.setAttribute( "flowPhone", order.getFlowPhone() );
			    }
			}
		    }
		    if ( CommonUtil.isNotEmpty( maps.get( "useFenbi" ) ) ) {
			request.setAttribute( "useFenbi", maps.get( "useFenbi" ) );
		    }
		    if ( CommonUtil.isNotEmpty( maps.get( "useJifen" ) ) ) {
			request.setAttribute( "useJifen", maps.get( "useJifen" ) );
		    }
		    if ( CommonUtil.isNotEmpty( maps.get( "couponArr" ) ) ) {
			request.setAttribute( "couponArr", com.alibaba.fastjson.JSONArray.parseArray( maps.get( "couponArr" ).toString() ) );
		    }
		    if ( maps.containsKey( "type" ) ) {
			type = CommonUtil.toString( maps.get( "type" ) );
		    }

		}
	    } else {
		SessionUtils.removeSession( request, "to_order" );
	    }
	    if ( CommonUtil.isNotEmpty( data.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( data.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    BusUser user = new BusUser();
	    user.setId( userid );
	    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );

	    if ( userid > 0 && CommonUtil.isEmpty( data.get( "data" ) ) && CommonUtil.isEmpty( orderObj ) ) {
		List< Map< String,Object > > pageId = mallPageService.selectPageIdByUserId( userid, shopList );
		if ( pageId.size() > 0 ) {//获取首页的pageId
		    return "redirect:/mallPage/" + pageId.get( 0 ).get( "id" ) + "/79B4DE7C/pageIndex.do";
		} else {
		    return "redirect:/mMember/79B4DE7C/toUser.do?uId=" + userid;
		}
	    }
	    WxPublicUsers pbUser = null;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		pbUser = wxPublicUserService.selectByMemberId( member.getId() );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		SessionUtils.setSession( JSONObject.fromObject( data ).toString(), request, key );
		return returnUrl;
	    }
	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( pbUser ) ) ) {
		isWxPay = 1;//可以微信支付
	    } else {
		isAliPay = 1;
	    }

	    //todo alipayUserService.findAlipayUserByBusId
	    /*AlipayUser alipayUser = alipayUserService.findAlipayUserByBusId( userid );
	    if ( CommonUtil.isNotEmpty( alipayUser ) && isWxPay == 0 ) {
		isAliPay = 1;//可以支付宝支付
	    }*/
	    request.setAttribute( "isWxPay", isWxPay );
	    request.setAttribute( "isAliPay", isAliPay );

	    Map addressMap = null;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
		addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, member.getId() ) );
	    }
	    double mem_longitude = 0;//保存经度信息
	    double mem_latitude = 0;//保存纬度信息
	    String loginCity = "";
	    if ( CommonUtil.isEmpty( addressMap ) || addressMap.size() == 0 ) {
		String ip = IPKit.getRemoteIP( request );
		loginCity = pageService.getProvince( ip );
	    } else {
		loginCity = addressMap.get( "memProvince" ).toString();

		request.setAttribute( "address", addressMap );

		if ( CommonUtil.isNotEmpty( addressMap.get( "memLongitude" ) ) ) {
		    mem_longitude = CommonUtil.toDouble( addressMap.get( "memLongitude" ) );
		}
		if ( CommonUtil.isNotEmpty( addressMap.get( "memLatitude" ) ) ) {
		    mem_latitude = CommonUtil.toDouble( addressMap.get( "memLatitude" ) );
		}
		request.setAttribute( "loginCity", loginCity );
	    }

	    Object takeId = data.get( "takeId" );//到店自提的提货地址id
	    if ( CommonUtil.isEmpty( takeId ) ) {
		takeId = "0";
	    }
	    JSONObject obj = new JSONObject();
	    if ( CommonUtil.isNotEmpty( data.get( "data" ) ) ) {
		SessionUtils.setSession( data.get( "data" ), request, "cartToOrderData" );
		String msg = data.get( "data" ).toString();
		obj = JSONObject.fromObject( msg );
	    }

	    List< Map< String,Object > > list = new ArrayList<>();
	    if ( type.equals( "1" ) ) {//从购物车进入订单
		if ( CommonUtil.isEmpty( data.get( "data" ) ) ) {
		    String msg = SessionUtils.getSession( request, "cartToOrderData" ).toString();
		    obj = JSONObject.fromObject( msg );
		}
		String id = CommonUtil.isNotEmpty( data.get( "shopcards" ) ) ? data.get( "shopcards" ).toString() : obj.getString( "shopcards" );
		String shopcards = id.substring( 0, id.length() );
		request.setAttribute( "shopcards", shopcards );
		list = mallShopCartService.getProductByShopCart( shopcards, pbUser, member, userid, shopList );
	    } else {
		if ( CommonUtil.isNotEmpty( data.get( "order" ) ) ) {
		    list = JSONArray.fromObject( data.get( "order" ) );

		    list = JSONArray.fromObject( list.get( 0 ).get( "mallOrderDetail" ) );
		} else {
		    list = JSONArray.fromObject( obj );
		}

		JSONObject maps = JSONObject.fromObject( list.get( 0 ) );
		list = mallShopCartService.getProductByIds( maps, pbUser, member, userid, shopList );
	    }
	    Object appointName = data.get( "appointName" );//提货人姓名
	    Object appointTel = data.get( "appointTel" );//提货人手机
	    request.setAttribute( "appointName", appointName );
	    request.setAttribute( "appointTel", appointTel );

	    //获取参数
	    mallShopCartService.getOrdersParams( request, loginCity, userid, list, mem_longitude, mem_latitude, member, shopList );

	    //商户是否允许买家货到付款,找人代付（true 允许  false 不允许）
	    mallPaySetService.isHuoDaoByUserId( userid, request );

	    //商户是否允许使用上门自提（true 允许 false不允许）
	    boolean takeTheir = mallTakeTheirService.isTakeTheirByUserId( userid );
	    String isTakeTheir = "0";//不允许到店自提

	    if ( takeTheir ) {
		isTakeTheir = "1";//允许到店自提
		MallTakeTheir mallTakeTheir = mallTakeTheirTimeService
				.selectDefaultTakeByUserId( userid, CommonUtil.toInteger( loginCity ), CommonUtil.toInteger( takeId ) );//查询到店自提的默认地址
		request.setAttribute( "mallTakeTheir", mallTakeTheir );
	    }

	    request.setAttribute( "orderList", JSONArray.fromObject( list ) );
	    request.setAttribute( "data", obj );
	    request.setAttribute( "type", type );
	    request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "isTakeTheir", isTakeTheir );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );

	    int shopId = SessionUtils.getMallShopId( request );
	    if ( shopId > 0 ) {
		List list1 = pageService.shoppage( shopId );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    String pageid = map1.get( "id" ).toString();
		    request.setAttribute( "pageId", pageid );
		}
	    }
	    // todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "手机订单页面异常：" + e );
	    e.printStackTrace();
	}
	return "mall/order/phone/submitOrder";
    }

    /**
     * 收货地址列表
     */
    @RequestMapping( value = "/79B4DE7C/addressList" )
    public String addressList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入收货地址列表页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    if ( params.containsKey( "data" ) ) {
		SessionUtils.setSession( params.get( "data" ), request, "to_order" );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
	    List< MemberAddress > addressList = memberAddressService.addressList( CommonUtil.getMememberIds( memberList, member.getId() ) );
	    request.setAttribute( "addressList", addressList );

	    BusUser user = busUserService.selectById( member.getBusid() );
	    if ( CommonUtil.isNotEmpty( user ) ) {
		if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		    if ( user.getAdvert() == 0 ) {
			request.setAttribute( "isAdvert", 1 );
		    }
		}
	    }
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( memberId ) ) {
	    	//todo CommonUtil.getWxParams
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "收货地址列表页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/addressList";
    }

    /**
     * 跳转至新增/修改收货地址页面
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
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询省份数据
	    List< Map > maps = wxShopService.queryCityByLevel( 2 );
	    request.setAttribute( "maps", maps );
	    Object id = params.get( "id" );
	    if ( null != id && !id.equals( "" ) ) {//修改地址查询
		MemberAddress mem = memberAddressService.addreSelectId( CommonUtil.toInteger( id ) );
		request.setAttribute( "mem", mem );
		request.setAttribute( "updateAddress", "修改地址" );
	    } else {
		request.setAttribute( "updateAddress", "新增地址" );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "params" ) ) ) {
		MemberAddress mem = (MemberAddress) JSONObject.toBean( JSONObject.fromObject( params.get( "params" ) ), MemberAddress.class );
		request.setAttribute( "mem", mem );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "addType" ) ) ) {
		request.setAttribute( "addType", params.get( "addType" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "addressManage" ) ) ) {
		request.setAttribute( "addressManage", params.get( "addressManage" ) );
	    }
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( member ) ) {
		//todo CommonUtil.getWxParams
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/

	    Object shopObj = SessionUtils.getSession( request, "isJuliFreight" );
	    if ( CommonUtil.isNotEmpty( shopObj ) ) {
		request.setAttribute( "isJuliFreight", shopObj );
	    }
	} catch ( Exception e ) {
	    logger.error( "跳转至新增/修改收货地址页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/addAddress";
    }

    /*
      * 新增/修改收货地址
      *
      */
    @RequestMapping( value = "/79B4DE7C/addAddress" )
    @SysLogAnnotation( op_function = "2", description = "新增/修改收货地址" )
    public void addAddress( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) throws IOException {
	logger.info( "进入新增/修改收货地址页面" );
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    if ( CommonUtil.isEmpty( member ) ) {
		resultMap.put( "code", ResponseEnums.SUCCESS.getCode() );
	    } else {
		MemberAddress memAddress = (MemberAddress) JSONObject.toBean( JSONObject.fromObject( params.get( "params" ) ), MemberAddress.class );

		memAddress.setDfMemberId( member.getId() );
		boolean flag = memberAddressService.addOrUpdateAddre( memAddress );
		if ( flag ) {
		    resultMap.put( "code", ResponseEnums.SUCCESS.getCode() );
		} else {
		    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		}
	    }

	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
	    logger.error( "新增/修改收货地址异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 查询城市数据
     */
    @RequestMapping( value = "/{pid}/79B4DE7C/queryCity" )
    public void queryCity( HttpServletRequest request, HttpServletResponse response, @PathVariable( "pid" ) Integer pid ) {
	try {
	    List< Map > maps = wxShopService.queryCityByParentId( pid );
	    CommonUtil.write( response, maps );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 修改默认收获地址
     */
    @RequestMapping( value = "/79B4DE7C/updateDefault" )
    @SysLogAnnotation( op_function = "3", description = "修改默认收获地址" )
    public void updateDefault( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) throws IOException {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    boolean flag = memberAddressService.updateDefault( id );
	    if ( flag ) {
		resultMap.put( "code", ResponseEnums.SUCCESS.getCode() );
	    } else {
		resultMap.put( "code", ResponseEnums.ERROR.getCode() );
	    }
	} catch ( Exception e ) {
	    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
	    logger.error( "修改默认收货地址异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 支付成功跳转页面
     */
    @RequestMapping( value = "{orderId}/{busId}/{type}/79B4DE7C/orderPaySuccess" )
    public String orderPaySuccess( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, @PathVariable int orderId,
		    @PathVariable int busId, @PathVariable int type ) {
	logger.info( "进入支付成功跳转页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, busId, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	} catch ( Exception e ) {
	    logger.error( "支付成功跳转页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	if ( type == 2 ) {
	    return "redirect:/phoneOrder/" + orderId + "/79B4DE7C/getDaiFu.do?isPayGive=1&&orderId=" + orderId + "&&uId=" + busId;
	} else if ( type == 3 ) {
	    return "redirect:/mAuction/79B4DE7C/myMargin.do?uId=" + busId;
	}
	return "redirect:/phoneOrder/79B4DE7C/orderList.do?isPayGive=1&&orderId=" + orderId + "&&uId=" + busId;
    }

    /**
     * 预售定金跳转页面
     */
    @RequestMapping( value = "{proId}/{busId}/{shopId}/79B4DE7C/presalePaySuccess" )
    public String presalePaySuccess( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, @PathVariable int proId,
		    @PathVariable int busId, @PathVariable int shopId ) {
	logger.info( "进入支付成功跳转页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, busId, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	} catch ( Exception e ) {
	    logger.error( "支付成功跳转页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "redirect:/mallPage/" + proId + "/" + shopId + "/79B4DE7C/phoneProduct.do?uId=" + busId;
    }

    /**
     * 我的订单
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( value = "/79B4DE7C/orderList" )
    public String orderList( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的订单列表页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "busId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "busId" ) );
	    }
	    request.setAttribute( "userid", userid );
	    mallOrderService.clearSession( request );

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );

	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    String result = "0";
	    Integer shopId = SessionUtils.getMallShopId( request );
	    if ( shopId > 0 ) {
		List list1 = pageService.shoppage( shopId );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    result = map1.get( "id" ).toString();
		}
	    }
	    if ( result.equals( "0" ) ) {
		List< Map< String,Object > > shopList = mallStoreService.findByUserId( member.getId() );
		List< Map< String,Object > > pageId = mallPageService.selectPageIdByUserId( member.getBusid(), shopList );
		if ( pageId.size() > 0 ) {
		    result = pageId.get( 0 ).get( "id" ).toString();
		}
	    }
	    boolean isPayGive = true;
	    if ( CommonUtil.isNotEmpty( params.get( "orderId" ) ) ) {
		String key = "mall:card_receive_num";
		String field = params.get( "orderId" ).toString();
		boolean isCard = JedisUtil.hExists( key, params.get( "orderId" ).toString() );
		if ( isCard ) {
		    //购买了卡券包
		    isPayGive = false;
		    request.setAttribute( "cardReceiveId", JedisUtil.maoget( key, field ) );
		    JedisUtil.hdel( key, field );
		}
	    }
	    if ( isPayGive ) {
		String url = mallOrderService.payGive( result, params, member );//支付有礼
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return "redirect:" + url;//跳转到支付有礼的页面
		}
	    }
	    Object orderType = params.get( "orderType" );//订单类型
	    if ( ( null != orderType && !orderType.equals( "" ) ) ) {
		if ( !orderType.equals( "1" ) && !orderType.equals( "3" ) && !orderType.equals( "4" ) ) {
		    orderType = "";
		}
	    }
	    Object type = params.get( "type" );//订单状态

	    request.setAttribute( "orderType", orderType );
	    request.setAttribute( "type", type );
	    request.setAttribute( "appraiseStatus", params.get( "appraiseStatus" ) );

	    params.put( "orderType", orderType );
	    params.put( "type", type );
	    params = mallOrderService.getMemberParams( member, params );
	    PageUtil page = mallOrderService.mobileOrderList( params, userid );
	    if ( page.getSubList() != null && page.getSubList().size() > 0 ) {
		request.setAttribute( "orderList", page.getSubList() );
	    }
	    request.setAttribute( "page", page );

	    //关闭超过30分钟未付款订单
	    mallOrderService.updateByNoMoney( params );
	    request.setAttribute( "memberId", member.getId() );
	    request.setAttribute( "pageid", result );
	    request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );
	    pageService.getCustomer( request, 0 );
	    if ( shopId > 0 ) {
		request.setAttribute( "shopid", shopId );
	    }
	    MallPaySet set = mallPaySetService.selectByMember( member );
	    request.setAttribute( "mallPaySet", set );

	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, userid );
	    if ( saleMemberId > 0 ) {
		request.setAttribute( "saleMemberId", saleMemberId );
	    }
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );

	    String subject = URLEncoder.encode( "商城下单", "UTF-8" );
	    request.setAttribute( "alipaySubject", subject );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "我的订单列表页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/myOrder";
    }

    /**
     * 分页查询订单
     *
     * @param request
     * @param params
     *
     * @return
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/mobileOrderListPage" )
    public void mobileOrderListPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入分页查询订单" );
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    int userid = CommonUtil.toInteger( params.get( "busUserId" ) );
	    PageUtil page = mallOrderService.mobileOrderList( params, userid );
	    map.put( "page", page );
	    map.put( "path", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "用户申请退款异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, map );
	}
    }

    /**
     * 进入申请退款页面
     */
    @RequestMapping( value = "/79B4DE7C/toReturn" )
    public String toReturn( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入申请退款页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = pageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    if ( !CommonUtil.isEmpty( params.get( "dId" ) ) ) {
		Integer detailId = CommonUtil.toInteger( params.get( "dId" ) );
		Map< String,Object > map = mallOrderService.selectByDIdOrder( detailId );
		request.setAttribute( "map", map );
	    }
	    if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		MallOrderReturn orderReturn = mallOrderService.selectByDId( id );
		request.setAttribute( "orderReturn", orderReturn );
	    }
	    if ( !CommonUtil.isEmpty( params.get( "type" ) ) ) {
		Integer type = CommonUtil.toInteger( params.get( "type" ) );
		if ( type == 0 ) {
		    // 查询退款原因
		    List< Map > dictMap = dictService.getDict( "1091" );
		    request.setAttribute( "dictMap", dictMap );
		} else if ( type == 1 ) {
		    //查询物流公司
		    List< Map > comMap = dictService.getDict( "1092" );
		    request.setAttribute( "comMap", comMap );
		}
	    }
	    request.setAttribute( "type", params.get( "type" ) );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入申请退款页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/requestRefund";
    }

    /**
     * 用户申请退款
     */
    @RequestMapping( value = "/79B4DE7C/addReturnOrder" )
    @SysLogAnnotation( op_function = "3", description = "用户申请退款" )
    public void addReturnOrder( HttpServletRequest request,

		    HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入用户申请退款" );
	Integer memberId = SessionUtils.getLoginMember( request ).getId();
	Map< String,Object > map = new HashMap<>();

	try {
	    MallOrderReturn orderReturn = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( params ), MallOrderReturn.class );
	    orderReturn.setUserId( memberId );
	    // 申请退款
	    boolean flag = mallOrderService.addOrderReturn( orderReturn );

	    map.put( "flag", flag );
	} catch ( Exception e ) {
	    logger.error( "用户申请退款异常：" + e.getMessage() );
	    map.put( "flag", false );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, map );
	}

    }

    /**
     * 撤销退款申请
     */
    @SysLogAnnotation( op_function = "3", description = "撤销申请退款" )
    @RequestMapping( value = "/79B4DE7C/closeReturnOrder" )
    public void closeReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入撤销退款申请" );
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    MallOrderReturn orderReturn = (MallOrderReturn) JSONObject.toBean( JSONObject.fromObject( params ), MallOrderReturn.class );
	    // 撤销退款申请
	    map = mallOrderService.updateOrderReturn( orderReturn, null, null );

	} catch ( Exception e ) {
	    logger.error( "撤销退款申请异常：" + e.getMessage() );
	    map.put( "flag", false );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, map );
	}
    }

    /**
     * 确认收货
     */
    @RequestMapping( value = "/79B4DE7C/confirmReceipt" )
    @SysLogAnnotation( op_function = "3", description = "确认收货（修改订单状态）" )
    public void confirmReceipt( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) {
	try {
	    String data = params.get( "params" ).toString();
	    JSONObject obj = JSONObject.fromObject( data );
	    params.put( "status", 4 );//已完成订单
	    params.put( "orderId", obj.get( "orderId" ) );
	    int count = mallOrderService.upOrderNoOrRemark( params );
	    CommonUtil.write( response, count );
	} catch ( IOException e ) {
	    logger.error( "确认收货异常：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 查看订单详情
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @RequestMapping( value = "/79B4DE7C/orderDetail" )
    public String orderDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );

	    int orderId = Integer.parseInt( params.get( "orderId" ).toString() );
	    MallOrder orders = mallOrderService.selectById( orderId );
	    int userid = orders.getBusUserId();
	    request.setAttribute( "userid", userid );

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    String memberId = member.getId().toString();
	    params.put( "memberId", memberId );
	    params.put( "id", orderId );
	    PageUtil page = mallOrderService.mobileOrderList( params, userid );
	    JSONObject orderObj = JSONObject.fromObject( page.getSubList().get( 0 ) );

	    if ( CommonUtil.isNotEmpty( orders.getExpressId() ) ) {
		String expressName = dictService.getDictRuturnValue( "1092", orders.getExpressId() );
		if ( CommonUtil.isNotEmpty( expressName ) ) {
		    request.setAttribute( "expressName", expressName );
		}
	    }
	    if ( orders.getDeliveryMethod() == 2 && orders.getTakeTheirId() > 0 ) {//配送方式是到店自提
		MallTakeTheir take = mallTakeTheirService.selectById( orders.getTakeTheirId() );
		request.setAttribute( "take", take );
	    }
	    if ( orders.getOrderType() == 1 && orders.getGroupBuyId() > 0 ) {
		Map< String,Object > groupBuy = mallGroupBuyService.selectGroupBuyById( orders.getGroupBuyId() );
		String endTime = groupBuy.get( "gEndTime" ).toString();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		long e = sdf.parse( endTime ).getTime() / 1000;
		long time = e - System.currentTimeMillis() / 1000;
		request.setAttribute( "endTime", time );

		request.setAttribute( "groupBuy", groupBuy );

		params.put( "groupBuyId", orders.getGroupBuyId() );
		params.put( "orderId", orders.getId() );
		Map< String,Object > count = mallOrderDAO.groupJoinPeopleNum( params );
		request.setAttribute( "count", count );
	    }
	    int openGroupUserId = member.getId();
	    if ( CommonUtil.isNotEmpty( orders ) ) {
		Map< String,Object > orderMap = new HashMap< String,Object >();
		orderMap.put( "joinId", orders.getPJoinId() );
		if ( orders.getGroupBuyId() > 0 ) {
		    orderMap.put( "groupBuyId", orders.getGroupBuyId() );
		    List< MallGroupJoin > joinList = mallGroupJoinDAO.selectByPJoinId( orderMap );
		    if ( joinList != null && joinList.size() > 0 ) {
			openGroupUserId = joinList.get( 0 ).getJoinUserId();
		    }
		}
	    }
	    request.setAttribute( "openGroupUserId", openGroupUserId );
	    request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "order", orderObj );
	    request.setAttribute( "orders", orders );
	    request.setAttribute( "memberId", memberId );
	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, userid );
	    if ( saleMemberId > 0 ) {
		request.setAttribute( "saleMemberId", saleMemberId );
	    }
	} catch ( Exception e ) {
	    logger.error( "查询订单详情异常：" + e.getMessage() );
	    e.printStackTrace();
	}

	return "mall/order/phone/orderDetail";
    }

    /**
     * 根据公众号id查询到店自提的信息
     */
    @RequestMapping( "/79B4DE7C/getTakeTheir" )
    public String getTakeTheir( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, HttpSession session ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    if ( params.containsKey( "data" ) ) {
		SessionUtils.setSession( params.get( "data" ), request, "to_order" );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    Map< String,Object > map = new HashMap< String,Object >();
	    map.put( "userId", userid );
	    //根据公众号id查询提取信息
	    List< MallTakeTheir > takeList = mallTakeTheirService.selectByBusUserId( map );
	    request.setAttribute( "takeList", takeList );
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		request.setAttribute( "checkId", params.get( "id" ) );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/take/phone/takeall";
    }

    /**
     * 代付分享页面
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "/{orderId}/79B4DE7C/shareDaiFu" )
    public String shareDaiFu( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, @PathVariable int orderId ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = pageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    if ( orderId > 0 ) {
		MallOrder order = mallOrderService.selectById( orderId );
		List< Map< String,Object > > detailList = mallOrderDAO.selectDetailByOrderId( orderId );
		if ( detailList != null && detailList.size() > 0 ) {
		    Map< String,Object > map = detailList.get( 0 );
		    if ( CommonUtil.isNotEmpty( map.get( "product_image_url" ) ) ) {
			request.setAttribute( "proImgUrl", map.get( "product_image_url" ) );
		    }
		    if ( CommonUtil.isNotEmpty( map.get( "shop_id" ) ) ) {
			int shopId = CommonUtil.toInteger( map.get( "shop_id" ) );
			if ( SessionUtils.getMallShopId( request ) > 0 ) {
			    shopId = SessionUtils.getMallShopId( request );
			}
			List list1 = pageService.shoppage( shopId );
			if ( list1.size() > 0 ) {
			    Map map1 = (Map) list1.get( 0 );
			    request.setAttribute( "pageId", map1.get( "id" ).toString() );
			}
		    }
		}
		request.setAttribute( "order", order );
	    }
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getHomeUrl() );

	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/order/phone/daifuShare";
    }

    /**
     * 求代付
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "/{orderId}/79B4DE7C/getDaiFu" )
    public String getDaiFu( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params, @PathVariable int orderId ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    MallOrder order = mallOrderService.selectById( orderId );
	    if ( userid == 0 ) {
		userid = order.getBusUserId();
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = pageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) ) ) {
		isWxPay = 1;//可以微信支付
	    }
	    //todo alipayUserService.findAlipayUserByBusId
	   /* AlipayUser alipayUser = alipayUserService.findAlipayUserByBusId( member.getBusid() );
	    if ( CommonUtil.isNotEmpty( alipayUser ) && isWxPay == 0 ) {
		isAliPay = 1;//可以支付宝支付
	    }*/
	    request.setAttribute( "isWxPay", isWxPay );
	    request.setAttribute( "isAliPay", isAliPay );
	    if ( orderId > 0 ) {
		List< Map< String,Object > > detailList = mallOrderDAO.selectDetailByOrderId( orderId );
		request.setAttribute( "order", order );
		request.setAttribute( "detailList", detailList );
		if ( detailList != null && detailList.size() > 0 ) {
		    Map< String,Object > map = detailList.get( 0 );
		    if ( CommonUtil.isNotEmpty( map.get( "shop_id" ) ) ) {
			int shopId = CommonUtil.toInteger( map.get( "shop_id" ) );
			if ( SessionUtils.getMallShopId( request ) > 0 ) {
			    shopId = SessionUtils.getMallShopId( request );
			}
			List list1 = pageService.shoppage( shopId );
			String pageId = "0";
			if ( list1.size() > 0 ) {
			    Map map1 = (Map) list1.get( 0 );
			    pageId = map1.get( "id" ).toString();
			    request.setAttribute( "pageId", pageId );
			}

			String url = mallOrderService.payGive( pageId, params, member );//支付有礼
			if ( CommonUtil.isNotEmpty( url ) ) {
			    return "redirect:" + url;//跳转到支付有礼的页面
			}
		    }
		}
		Integer memberId = SessionUtils.getLoginMember( request ).getId();
				/*Integer memberId = 200;*/
		mallOrderService.getDaiFu( order, orderId, memberId, request );
	    }
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/order/phone/daifu";
    }

    /**
     * 好友代付
     */
    @RequestMapping( "/79B4DE7C/freindDaifu" )
    @SysLogAnnotation( op_function = "2", description = "好友代付" )
    public void freindDaifu( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	int memberId = SessionUtils.getLoginMember( request ).getId();
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isNotEmpty( params ) ) {
		MallDaifu daifu = (MallDaifu) JSONObject.toBean( JSONObject.fromObject( params.get( "daifu" ) ), MallDaifu.class );
		daifu.setDfUserId( memberId );
		resultMap = mallOrderService.addMallDaifu( daifu );
	    }

	} catch ( Exception e ) {
	    resultMap.put( "result", false );
	    e.printStackTrace();
	    logger.error( e );
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 储值卡支付成功的回调
     */
    @RequestMapping( value = "/79B4DE7C/success" )
    @Transactional( rollbackFor = Exception.class )
    public String success( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	String id = params.get( "id" ).toString();
	Member member = SessionUtils.getLoginMember( request );
	try {
	    params.put( "out_trade_no", params.get( "no" ) );
	    mallOrderService.paySuccessDaifu( params );

	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "redirect:/phoneOrder/" + id + "/79B4DE7C/getDaiFu.do?uId=" + member.getBusid();
    }

    /**
     * 订单判断库存
     */
    private Map judgeStock( JSONObject detail, Map< String,Object > result, String type, int memberId ) {
	int proId = CommonUtil.toInteger( detail.get( "product_id" ) );
	int proNum = 1;//商品数量
	Object proSpecificas = null;//规格Id
	Object groupType = detail.get( "groupType" );
	Object proSpecStr = detail.get( "pro_spec_str" );
	if ( null != groupType && groupType.equals( "7" ) && CommonUtil.isNotEmpty( proSpecStr ) ) {//批发商品判断库存
	    Map< String,Object > map = JSONObject.fromObject( proSpecStr );
	    for ( Map.Entry< String,Object > e : map.entrySet() ) {
		proSpecificas = e.getKey();
		Map p = com.alibaba.fastjson.JSONObject.parseObject( e.getValue().toString(), Map.class );
		proNum = CommonUtil.toInteger( p.get( "num" ) );
		result = mallProductService.calculateInventory( proId, proSpecificas, proNum, memberId );
		if ( !( result.get( "result" ) ).equals( "true" ) ) {
		    break;
		}
	    }
	} else {
	    proNum = CommonUtil.toInteger( detail.get( "product_num" ) );
			/*if(CommonUtil.isNotEmpty(detail.get("totalnum"))){//点立即购买时，购买商品的数量
				proNum = CommonUtil.toInteger( detail.get("totalnum"));
			}else if(type.equals("1")){//点购物车时，购买商品的数量
				proNum = CommonUtil.toInteger(detail.get("product_num"));
			}*/
	    proSpecificas = detail.get( "product_specificas" );
	    result = mallProductService.calculateInventory( proId, proSpecificas, proNum, memberId );
	}
	return result;
    }

    /**
     * 去支付
     */
    @RequestMapping( value = "/79B4DE7C/goPay" )
    @Transactional( rollbackFor = Exception.class )
    public void goPay( HttpServletRequest request, @RequestParam Map< String,Object > param, HttpServletResponse response ) throws IOException {
	logger.info( "进入去支付controller" );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    if ( CommonUtil.isNotEmpty( param.get( "id" ) ) ) {

		int orderId = CommonUtil.toInteger( param.get( "id" ) );

		MallOrder order = mallOrderService.getOrderById( orderId );
		if ( CommonUtil.isNotEmpty( order ) ) {
					/*Order newOrder = new Order();
					newOrder.setId(order.getId());
					String orderNo="SC"+System.currentTimeMillis();
					newOrder.setOrderNo(orderNo);
					morderService.upOrderNoById(newOrder);
					order.setOrderNo(orderNo);*/

		    if ( order.getOrderType() == 3 ) {//秒杀订单
			JSONObject detailObj = new JSONObject();
			String key = "hSeckill_nopay";//秒杀用户(用于没有支付，恢复库存用)
			if ( JedisUtil.hExists( key, order.getId().toString() ) ) {
			    detailObj.put( "groupBuyId", order.getGroupBuyId() );
			    //判断秒杀订单是否正在进行
			    MallSeckill seckill = mallSeckillService.selectSeckillBySeckillId( order.getGroupBuyId() );
			    if ( CommonUtil.isNotEmpty( seckill ) ) {
				if ( seckill.getIsDelete().toString().equals( "1" ) ) {
				    code = -1;
				    result.put( "code", ResponseEnums.ERROR.getCode() );
				    result.put( "msg", "您购买的秒杀商品已经被删除，请重新下单" );
				} else if ( seckill.getIsUse().toString().equals( "-1" ) ) {
				    code = -1;
				    result.put( "code", ResponseEnums.ERROR.getCode() );
				    result.put( "msg", "您购买的秒杀商品已经被失效，请重新下单" );
				} else if ( seckill.getStatus() == 0 ) {
				    code = -1;
				    result.put( "code", ResponseEnums.ERROR.getCode() );
				    result.put( "msg", "您购买的秒杀商品还没开始，请耐心等待" );
				} else if ( seckill.getStatus() == -1 ) {
				    code = -1;
				    result.put( "code", ResponseEnums.ERROR.getCode() );
				    result.put( "msg", "您购买的秒杀商品已经结束，请重新下单" );
				}
			    } else {
				code = -1;
				result.put( "code", ResponseEnums.ERROR.getCode() );
				result.put( "msg", "您购买的秒杀商品已经被删除，请重新下单" );
			    }
			} else {
			    code = -1;
			    result.put( "code", ResponseEnums.ERROR.getCode() );
			    result.put( "msg", "您的订单已关闭，请重新下单" );
			}

		    } else {
			if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) ) {
			    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
				if ( CommonUtil.isNotEmpty( order.getOrderType() ) && order.getOrderType().toString().equals( "7" ) && CommonUtil
						.isNotEmpty( detail.getProSpecStr() ) ) {//批发商品判断库存
				    JSONObject map = JSONObject.fromObject( detail.getProSpecStr() );
				    for ( Object key : map.keySet() ) {
					String proSpecificas = key.toString();
					JSONObject p = JSONObject.fromObject( map.get( key ) );
					int proNum = CommonUtil.toInteger( p.get( "num" ) );
					Map< String,Object > resultMap = mallProductService
							.calculateInventory( detail.getProductId(), proSpecificas, proNum, order.getBuyerUserId() );
					if ( !( resultMap.get( "result" ) ).equals( "true" ) ) {
					    code = -1;
					    break;
					}
				    }
				} else {
				    Map< String,Object > resultMap = mallProductService
						    .calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
				    if ( !( resultMap.get( "result" ) ).equals( "ture" ) ) {
					code = -1;
					break;
				    }
				}

			    }
			}
		    }
		    if ( order.getOrderStatus() == 2 || order.getOrderStatus() == 3 || order.getOrderStatus() == 4 ) {
			result.put( "code", ResponseEnums.REFRESH_PAGE.getCode() );
			result.put( "msg", "您已经支付成功，无需再次支付" );
		    }

		    String url = mallOrderNewService.wxPayWay( CommonUtil.toDouble( order.getOrderMoney() ), order.getOrderNo(), order );
		    result.put( "url", url );
		}
	    }

	} catch ( Exception e ) {
	    result.put( "code", ResponseEnums.ERROR.getCode() );
	    result.put( "msg", "去支付失败，稍后请重新支付" );
	    logger.error( "去支付异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    if ( CommonUtil.isEmpty( result.get( "code" ) ) ) {
		result.put( "code", code );
	    }
	    CommonUtil.write( response, result );
	}
    }

    /**
     * 计算订单价格
     */
    @RequestMapping( value = "/79B4DE7C/calculateOrder" )
    @Transactional( rollbackFor = Exception.class )
    public void calculateOrder( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) throws IOException {
	logger.info( "进入计算controller" );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    List< MallOrder > orderList = com.alibaba.fastjson.JSONArray.parseArray( params.get( "order" ).toString(), MallOrder.class );
	    MallAllEntity mallAllEntity = mallOrderNewService.calculateOrder( params, member, orderList );

	    if ( CommonUtil.isNotEmpty( mallAllEntity ) ) {
		result = mallOrderNewService.getCalculateData( mallAllEntity );
	    } else {
		code = ResponseEnums.NULL_ERROR.getCode();
	    }
	} catch ( Exception e ) {
	    code = ResponseEnums.ERROR.getCode();
	    logger.error( "计算异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "code", code );
	    CommonUtil.write( response, result );
	}
    }

    @RequestMapping( value = "/79B4DE7C/submitOrder" )
    @SysLogAnnotation( op_function = "2", description = "提交订单" )
    public void submitOrder( HttpServletRequest request, @RequestParam Map< String,Object > param, HttpServletResponse response ) throws IOException {
	logger.info( "提交订单" );
	Map< String,Object > result = new HashMap<>();
	int code = 1;
	try {
	    String ip = IPKit.getRemoteIP( request );
	    String key = "mall:add_order_" + ip;
	    if ( CommonUtil.isEmpty( param.get( "params" ) ) ) {
		if ( JedisUtil.exists( key ) ) {
		    param = (Map< String,Object >) JSONObject.toBean( JSONObject.fromObject( JedisUtil.get( key ) ), Map.class );
		}
	    }

	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( param.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }

	    if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
		userid = member.getBusid();
		request.setAttribute( "userid", member.getBusid() );
	    }
	    SessionUtils.setSession( param.get( "deliveryMethod" ), request, "deliveryMethod" );

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		SessionUtils.setSession( JSONObject.fromObject( param ).toString(), request, "to_order" );
		result.put( "code", ResponseEnums.ERROR.getCode() );
		result.put( "errorMsg", "您还未登陆，请前往登陆页面登陆" );
		result.put( "isLogin", "1" );
		code = -1;
	    }
	    if ( code == 1 ) {
		Integer browser = CommonUtil.judgeBrowser( request );
		if ( browser != 1 ) {
		    browser = 2;
		}
		result = mallOrderNewService.submitOrder( param, member, browser );
	    }

	} catch ( BusinessException be ) {
	    result.put( "code", be.getCode() );
	    result.put( "errorMsg", be.getMessage() );
	    logger.error( "生成订单异常：" + be.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "生成订单异常：" + e );
	    e.printStackTrace();
	    result.put( "code", ResponseEnums.ERROR.getCode() );
	    result.put( "errorMsg", "提交订单失败" );
	} finally {
	    CommonUtil.write( response, result );
	}
    }

    /**
     * 支付成功回调
     */
    @RequestMapping( value = "/79B4DE7C/paySuccessModified" )
    public void paySuccessModified( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	logger.info( " 支付成功回调controller" + JSONObject.fromObject( params ) );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    mallOrderService.paySuccessModified( params, SessionUtils.getLoginMember( request ) );
	} catch ( BusinessException be ) {
	    code = be.getCode();
	    result.put( "errorMsg", be.getMessage() );
	    logger.error( "支付成功回调异常：" + be.getMessage() );
	} catch ( Exception e ) {
	    code = ResponseEnums.ERROR.getCode();
	    logger.error( "支付成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "code", code );
	    CommonUtil.write( response, result );
	}
    }

    /**
     * 联盟绑定手机号码
     */
    @RequestMapping( value = "/79B4DE7C/bindUnionCard" )
    public void bindUnionCard( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) throws IOException {
	logger.info( "进入联盟绑定controller" );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    BindCardParam bindCardParam = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( params ), BindCardParam.class );
	    Map resultMap = unionCardService.uionCardBind( bindCardParam );
	    if ( !resultMap.get( "code" ).toString().equals( "1" ) ) {
		code = ResponseEnums.ERROR.getCode();
	    }
	    if ( CommonUtil.isNotEmpty( resultMap.get( "errorMsg" ) ) ) {
		result.put( "errorMsg", resultMap.get( "errorMsg" ) );
	    }

	} catch ( Exception e ) {
	    code = ResponseEnums.ERROR.getCode();
	    logger.error( "联盟绑定异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "code", code );
	    CommonUtil.write( response, result );
	}
    }

    /**
     * 联盟发送手机验证码
     */
    @RequestMapping( value = "/79B4DE7C/getPhoneCode" )
    public void getPhoneCode( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) throws IOException {
	logger.info( "进入联盟发送手机验证码controller" );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    Member member = SessionUtils.getLoginMember( request );

	    UnionPhoneCodeParam phoneCodeParam = new UnionPhoneCodeParam();
	    phoneCodeParam.setBusId( member.getBusid() );
	    phoneCodeParam.setMemberId( member.getId() );
	    phoneCodeParam.setPhone( params.get( "phone" ).toString() );

	    Map resultMap = unionCardService.phoneCode( phoneCodeParam );

	    if ( !resultMap.get( "code" ).toString().equals( "1" ) ) {
		code = ResponseEnums.ERROR.getCode();
	    }
	    if ( CommonUtil.isNotEmpty( resultMap.get( "errorMsg" ) ) ) {
		result.put( "errorMsg", resultMap.get( "errorMsg" ) );
	    }
	} catch ( Exception e ) {
	    code = ResponseEnums.ERROR.getCode();
	    logger.error( "联盟发送手机验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "code", code );
	    CommonUtil.write( response, result );
	}
    }

    /**
     * 钱包支付
     */
    @RequestMapping( value = "/79B4DE7C/walletPayWay" )
    public void walletPayWay( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) throws IOException {
	logger.info( " 钱包支付controller" );
	Map< String,Object > result = new HashMap<>();
	int code = ResponseEnums.SUCCESS.getCode();
	try {
	    MallOrder mallOrder = mallOrderService.selectById( params.get( "id" ).toString() );
	    String returnUrl = mallOrderNewService.wxPayWay( 0, "", mallOrder );
	    if ( CommonUtil.isEmpty( returnUrl ) ) {
		code = ResponseEnums.ERROR.getCode();
	    } else {
		result.put( "returnUrl", returnUrl );
	    }
	} catch ( BusinessException be ) {
	    code = be.getCode();
	    result.put( "errorMsg", be.getMessage() );
	    logger.error( "钱包支付异常：" + be.getMessage() );
	} catch ( Exception e ) {
	    code = ResponseEnums.ERROR.getCode();
	    logger.error( "钱包支付异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "code", code );
	    CommonUtil.write( response, result );
	}
    }

}
