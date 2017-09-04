package com.gt.mall.controller.order.phone;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.bean.WxPublicUsers;
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
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.basic.MallTakeTheirTimeService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private MallAuctionService       mallAuctionService;
    @Autowired
    private MallPresaleService       mallPresaleService;
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

    /**
     * 跳转至提交订单页面
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @RequestMapping( value = "/79B4DE7C/toOrder" )
    public String toOrder( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > data ) {
	logger.info( "进入手机订单页面" );
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    String key = "to_order";
	    Object orderObj = request.getSession().getAttribute( key );
	    Object payWayObj = request.getSession().getAttribute( "orderpayway" );
	    Object payWayNameObj = request.getSession().getAttribute( "orderpaywayname" );
	    int userid = 0;
	    if ( CommonUtil.isEmpty( data.get( "data" ) ) ) {
		if ( CommonUtil.isNotEmpty( orderObj ) ) {
		    Map< String,Object > maps = (Map< String,Object >) JSONObject.toBean( JSONObject.fromObject( orderObj ), Map.class );
		    data.putAll( maps );

		    if ( CommonUtil.isNotEmpty( maps ) ) {
			if ( CommonUtil.isNotEmpty( maps.get( "orderPayWay" ) ) ) {
			    request.setAttribute( "orderPayWays", maps.get( "orderPayWay" ) );
			}
			if ( CommonUtil.isNotEmpty( maps.get( "payWayName" ) ) ) {
			    request.setAttribute( "payWayName", maps.get( "payWayName" ) );
			}
			if ( CommonUtil.isNotEmpty( maps.get( "flowPhone" ) ) ) {
			    request.setAttribute( "flowPhone", maps.get( "flowPhone" ) );
			}
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( payWayObj ) ) {
		request.setAttribute( "orderPayWays", payWayObj );
	    }
	    if ( CommonUtil.isNotEmpty( payWayNameObj ) ) {
		request.setAttribute( "payWayName", payWayNameObj );
	    }
	    if ( CommonUtil.isNotEmpty( data.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( data.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    WxPublicUsers pbUser = null;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		pbUser = wxPublicUserService.selectByMemberId( member.getId() );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		request.getSession().setAttribute( key, JSONObject.fromObject( data ).toString() );
		return returnUrl;
	    }
	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( pbUser ) ) ) {
		isWxPay = 1;//可以微信支付
	    }else{
		isAliPay = 1;
	    }

	    //todo alipayUserService.findAlipayUserByBusId
	    /*AlipayUser alipayUser = alipayUserService.findAlipayUserByBusId( userid );
	    if ( CommonUtil.isNotEmpty( alipayUser ) && isWxPay == 0 ) {
		isAliPay = 1;//可以支付宝支付
	    }*/
	    request.setAttribute( "isWxPay", isWxPay );
	    request.setAttribute( "isAliPay", isAliPay );

	    List< Map< String,Object > > addressList = new ArrayList< Map< String,Object > >();
	    if ( CommonUtil.isNotEmpty( member ) ) {
		Map< String,Object > params = new HashMap< String,Object >();
		params = mallOrderService.getMemberParams( member, params );
		params.put( "memDefault", 1 );
		//获取粉丝的默认收货地址
		addressList = mallOrderService.selectShipAddress( params );
	    }
	    double mem_longitude = 0;//保存经度信息
	    double mem_latitude = 0;//保存纬度信息
	    String loginCity = "";
	    if ( addressList == null || addressList.size() == 0 ) {
		String ip = IPKit.getRemoteIP( request );
		loginCity = pageService.getProvince( ip );
	    } else {
		Map< String,Object > addressMap = addressList.get( 0 );

		loginCity = addressMap.get( "mem_province" ).toString();
		request.setAttribute( "address", addressMap );

		if ( CommonUtil.isNotEmpty( addressMap.get( "mem_longitude" ) ) ) {
		    mem_longitude = CommonUtil.toDouble( addressMap.get( "mem_longitude" ) );
		}
		if ( CommonUtil.isNotEmpty( addressMap.get( "mem_latitude" ) ) ) {
		    mem_latitude = CommonUtil.toDouble( addressMap.get( "mem_latitude" ) );
		}
		request.setAttribute( "loginCity", loginCity );
	    }

	    Object dataObj = request.getSession().getAttribute( "dataOrder" );
	    Object addTypeObj = request.getSession().getAttribute( "addressType" );
	    Object deliveryMethodObj = request.getSession().getAttribute( "deliveryMethod" );

	    Object address = data.get( "address" );
	    String type = "";
	    if ( null != address && !address.equals( "" ) ) {
		JSONObject obj = JSONObject.fromObject( dataObj );
		type = addTypeObj.toString();
		data.put( "data", obj );
	    } else if ( CommonUtil.isNotEmpty( data.get( "type" ) ) ) {
		type = data.get( "type" ).toString();
	    }

	    Object takeId = data.get( "takeId" );//到店自提的提货地址id
	    if ( CommonUtil.isEmpty( takeId ) ) {
		takeId = "0";
	    } else {
		JSONObject json = JSONObject.fromObject( dataObj );
		type = json.get( "type" ).toString();
		data.put( "data", json.get( "datas" ) );
		request.setAttribute( "deliveryMethod", deliveryMethodObj );
	    }
	    JSONObject obj = new JSONObject();
	    if ( CommonUtil.isNotEmpty( data.get( "data" ) ) ) {
		request.getSession().setAttribute( "cartToOrderData", data.get( "data" ) );
		String msg = data.get( "data" ).toString();
		obj = JSONObject.fromObject( msg );
	    }

	    List< Map< String,Object > > list = new ArrayList<>();

	    if ( type.equals( "1" ) ) {//从购物车进入订单
		if ( CommonUtil.isEmpty( data.get( "data" ) ) ) {
		    String msg = request.getSession().getAttribute( "cartToOrderData" ).toString();
		    obj = JSONObject.fromObject( msg );
		}

		String id = obj.get( "shop_ids" ).toString();
		String shopcards = id.substring( 0, id.length() );
		list = mallShopCartService.getProductByShopCart( shopcards, pbUser, member, userid );
	    } else {
		if ( CommonUtil.isNotEmpty( data.get( "detail" ) ) ) {
		    obj.put( "data", data.get( "detail" ) );
		    list = JSONArray.fromObject( data.get( "detail" ) );
		} else {
		    list = JSONArray.fromObject( obj );
		}

		JSONObject maps = JSONObject.fromObject( list.get( 0 ) );
		list = mallShopCartService.getProductByIds( maps, pbUser, member, userid );
	    }
	    Object appointName = data.get( "appointName" );//提货人姓名
	    Object appointTel = data.get( "appointTel" );//提货人手机
	    request.setAttribute( "appointName", appointName );
	    request.setAttribute( "appointTel", appointTel );

	    //获取参数
	    mallShopCartService.getOrdersParams( request, loginCity, userid, list, mem_longitude, mem_latitude, member );

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
	    if ( CommonUtil.isNotEmpty( member ) ) {
		//查询商家是否已经开启了商家联盟
		Map< String,Object > unionMap = new HashMap< String,Object >();
		unionMap.put( "memberId", member.getId() );
		unionMap.put( "busId", member.getBusid() );
		request.setAttribute( "unionMap", unionMap );
	    }

	    request.setAttribute( "orderDetail", JSONArray.fromObject( list ) );
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
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "访问提交订单页面花费：" + second + "秒" );
	}
	return "mall/order/phone/submitOrder";
    }

    @SuppressWarnings( { "unchecked", "unused" } )
    @RequestMapping( value = "/79B4DE7C/addOrder" )
    @SysLogAnnotation( op_function = "2", description = "添加订单" )
    public void addOrder( HttpServletRequest request,
		    @RequestParam Map< String,Object > param,
		    HttpServletResponse response ) {
	logger.info( "进入生成订单页面" );
	boolean flag = true;
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();

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
	    String data = param.get( "params" ).toString();
	    JSONObject obj = JSONObject.fromObject( data );
	    Map< String,Object > params = obj;

	    JSONArray arr = new JSONArray();
	    String data1 = params.get( "detail" ).toString();
	    String type = params.get( "type" ).toString();
	    JSONArray dobj = JSONArray.fromObject( data1 );
	    arr = JSONArray.fromObject( dobj );

	    if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
	    }

	    if ( arr != null && arr.size() > 0 && CommonUtil.isNotEmpty( member ) ) {
		JSONObject details = JSONObject.fromObject( arr.get( 0 ) );
		if ( CommonUtil.isNotEmpty( details.get( "pro_type_id" ) ) ) {
		    if ( details.get( "pro_type_id" ).toString().equals( "2" ) ) {//虚拟物品（会员卡）
			result.put( "result", false );
			flag = false;
			/*Map< String,Object > cardResult = memPayService.findBuyCard( member );//购买会员卡
						*//*cardResult.put("code", 1);*//*//测试用
			if ( cardResult.get( "code" ).equals( "-1" ) || cardResult.get( "code" ).equals( "-2" ) ) {//-1购买成功，跳转完善资料链接，-2已购买会员卡
			    result.put( "result", false );
			    result.put( "cardResult", cardResult );
			    flag = false;
			}*/
		    }
		}
	    }
	    int orderPayWay = 1;
	    double orderMoney = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "orderPayWay" ) ) ) {
		orderPayWay = CommonUtil.toInteger( params.get( "orderPayWay" ).toString() );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "orderMoney" ) ) ) {
		orderMoney = CommonUtil.toDouble( params.get( "orderMoney" ).toString() );
	    }
	    request.getSession().setAttribute( "deliveryMethod", params.get( "deliveryMethod" ) );
	    //判断是否用积分抵扣来兑换商品
	    /*if(flag){
		    if(orderPayWay == 4){//积分支付
			    Integer mIntergral = member.getIntegral();
			    if (mIntergral < orderMoney || mIntergral < 0) {
				    result.put("result", false);
				    result.put("msg", "您的积分不够，不能用积分来兑换这件商品");
			    }
		    }
		    if(orderPayWay == 8){//粉币支付
			    double fenbi = member.getFansCurrency();
			    if (fenbi < orderMoney || fenbi < 0) {
				    result.put("result", false);
				    result.put("msg", "您的粉币不够，不能用粉币来兑换这件商品");
			    }
		    }
	    }*/
	    int memberId = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		memberId = member.getId();
	    }
	    if ( flag ) {
		//int intergral = 0;
		for ( int i = 0; i < arr.size(); i++ ) {
		    JSONObject list = JSONObject.fromObject( arr.get( i ) );
		    JSONArray list1 = JSONArray.fromObject( list.get( "message" ) );
		    for ( int k = 0; k < list1.size(); k++ ) {
			JSONObject detail = JSONObject.fromObject( list1.get( k ) );
			result = judgeStock( detail, result, type, memberId );//判断商品库存，库存不够跳出循环
			if ( !result.get( "result" ).toString().equals( "true" ) ) {
			    flag = false;
			    break;
			}
		    }
		    /*if(type.equals("1")){//购物车判断库存
			    JSONArray list1 = JSONArray.fromObject(list.get("message"));
			    for (int k = 0; k < list1.size(); k++) {
				    JSONObject detail = JSONObject.fromObject(list1.get(k));
				    result = judgeStock(detail, result, type,memberId);//判断商品库存，库存不够跳出循环
				    if(!result.get("result").toString().equals("true")){
					    flag = false;
					    break;
				    }
			    }
		    }else{//立即购买判断库存
			    result = judgeStock(list, result, type,memberId);
			    if(!result.get("result").toString().equals("true")){
				    flag = false;
				    break;
			    }
		    }*/
		}
		//					int orderPayWay = Integer.parseInt(params.get("orderPayWay").toString());
		if ( result.get( "result" ).toString().equals( "true" ) ) {
		    //拍卖商品
		    if ( CommonUtil.isNotEmpty( params.get( "groupType" ) ) ) {
			if ( params.get( "groupType" ).toString().equals( "4" ) ) {
			    result = mallAuctionService.isMaxNum( params, member.getId().toString() );
			} else if ( params.get( "groupType" ).toString().equals( "6" ) ) {//商品预售
			    result = mallPresaleService.isMaxNum( params, member.getId().toString() );
			}
			if ( !result.get( "result" ).toString().equals( "true" ) ) {
			    flag = false;
			}
		    }
		    if ( result.get( "result" ).toString().equals( "true" ) ) {
			if ( CommonUtil.isNotEmpty( params.get( "flowPhone" ) ) ) {//流量充值，判断手机号码
			    Map< String,String > map = MobileLocationUtil.getMobileLocation( CommonUtil.toString( params.get( "flowPhone" ) ) );
			    if ( map.get( "code" ).toString().equals( "-1" ) ) {
				result.put( "result", false );
				result.put( "msg", map.get( "msg" ) );
				flag = false;
			    } else if ( map.get( "code" ).toString().equals( "1" ) ) {
				if ( map.get( "supplier" ).equals( "中国联通" ) && CommonUtil.toInteger( params.get( "prizeCount" ) ) == 10 ) {
				    result.put( "result", false );
				    result.put( "msg", "充值失败,联通号码至少30MB" );
				    flag = false;
				}
			    }
			}
		    }
		    if ( result.get( "result" ).toString().equals( "true" ) ) {
			Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
			if ( ( CommonUtil.judgeBrowser( request ) != 1 || CommonUtil.isEmpty( publicMap ) ) ) {
			    boolean isLogin = pageService.isLogin( member, userid, request );
			    if ( !isLogin ) {
				//JedisUtil.set(key, JSONObject.fromObject(param).toString(), 60*60);
								/*obj.put("data", JSONObject.fromObject(obj.get("detail")).toString());
								obj.remove("detail");*/
				request.getSession().setAttribute( "to_order", JSONObject.fromObject( data ).toString() );
				result.put( "result", false );
				result.put( "msg", "您还未登陆，请前往登陆页面登陆" );
				result.put( "isLogin", "1" );
				flag = false;
			    }
			}
		    }
		    if ( result.get( "result" ).toString().equals( "true" ) ) {
			result = mallOrderService.addSeckillOrder( params, member, result, request );
		    }
		}
		Object p = result.get( "payWays" );
		if ( CommonUtil.isNotEmpty( p ) ) {
		    orderPayWay = Integer.parseInt( p.toString() );
		}
		result.put( "payWay", orderPayWay );
	    }

	} catch ( Exception e ) {
	    logger.error( "生成订单异常：" + e );
	    e.printStackTrace();

	    result.put( "result", false );
	    result.put( "msg", "提交订单失败" );
	} finally {
	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
	}
    }

    @SuppressWarnings( { "unchecked", "unused" } )
    @RequestMapping( value = "/79B4DE7C/addSeckillOrder" )
    @SysLogAnnotation( op_function = "2", description = "添加订单-秒杀" )
    public void addSeckillOrder( HttpServletRequest request,
		    @RequestParam Map< String,Object > param,
		    HttpServletResponse response ) {
	logger.info( "进入生成订单页面-秒杀" );
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();
	    String data = param.get( "params" ).toString();
	    JSONObject obj = JSONObject.fromObject( data );
	    Map< String,Object > params = obj;
	    boolean flag = true;

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
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
	    if ( ( CommonUtil.judgeBrowser( request ) != 1 || CommonUtil.isEmpty( publicMap ) ) ) {
		boolean isLogin = pageService.isLogin( member, userid, request );
		if ( !isLogin ) {
		    JedisUtil.set( key, JSONObject.fromObject( param ).toString(), 60 * 60 );
		    result.put( "result", false );
		    result.put( "msg", "您还未登陆，请前往登陆页面登陆" );
		    result.put( "isLogin", "1" );
		    flag = false;
		}
	    }

	    if ( flag ) {
		JSONArray arr = new JSONArray();
		JSONArray dobj = JSONArray.fromObject( params.get( "detail" ).toString() );
		arr = JSONArray.fromObject( dobj );
		com.alibaba.fastjson.JSONObject detailObj = com.alibaba.fastjson.JSONObject.parseObject( arr.get( 0 ).toString() );
		int intergral = 0;
		//判断库存
		result = mallSeckillService.isInvNum( detailObj );

		int orderPayWay = Integer.parseInt( params.get( "orderPayWay" ).toString() );
		if ( result.get( "result" ).toString().equals( "true" ) ) {

		    result = mallOrderService.addSeckillOrder( params, member, result, request );
		    if ( result.get( "result" ).toString().equals( "true" ) ) {
			//修改库存
			mallSeckillService.invNum( detailObj, member.getId().toString(), result.get( "orderId" ).toString() );
		    }
		}
		result.put( "payWay", orderPayWay );
	    }

	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "提交订单失败，稍后请重新提交" );
	    logger.error( "生成秒杀订单异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "生成订单花费：" + second + "秒" );

	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
	}
    }

    /**
     * 订单支付方式
     */
    @RequestMapping( value = "/79B4DE7C/payWay" )
    @SysLogAnnotation( op_function = "2", description = "订单储蓄卡支付成功添加记录和修改" )
    @Transactional( rollbackFor = Exception.class )
    public String payWay( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	Member member = SessionUtils.getLoginMember( request );
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	    request.setAttribute( "userid", userid );
	}
	try {
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	} catch ( Exception e ) {
	    this.logger.error( "PhoneOrderController方法异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	String memberId = member.getId().toString();
	Integer payWay = CommonUtil.toInteger( params.get( "payWay" ) );
	String orderId = params.get( "orderId" ).toString();
	int shopId = 0;
	try {
	    Map< String,Object > payRresult = new HashMap<>();
	    double orderMoney = Double.parseDouble( params.get( "orderMoney" ).toString() );
	    if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
		shopId = CommonUtil.toInteger( params.get( "shopId" ) );
	    }

	    mallOrderService.paySuccessModified( params, member );//修改库存和订单状态
	    /*String result = payRresult.get( "result" ).toString();
	    if ( result.equals( "2" ) ) {
		MallOrder order = mallOrderService.selectById( Integer.parseInt( orderId ) );
		if ( userid == 0 ) {
		    userid = order.getBusUserId();
		}
		params.put( "status", 2 );
		params.put( "out_trade_no", order.getOrderNo() );
		mallOrderService.paySuccessModified( params, member );//修改库存和订单状态

	    } else if ( result.equals( "1" ) && payWay != 4 && payWay != 8 ) {//4 积分支付  8粉币支付
		return "redirect:/phoneMemberController/79B4DE7C/recharge.do?id=" + memberId;
	    }*/
	} catch ( Exception e ) {
	    logger.error( "储蓄卡支付异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "储蓄卡支付花费：" + second + "秒" );
	}
	if ( payWay == 7 ) {
	    return "redirect:/phoneOrder/" + orderId + "/79B4DE7C/getDaiFu.do?uId=" + userid;
	} else if ( payWay == 4 ) {
	    return "redirect:/phoneIntegral/" + shopId + "/79B4DE7C/recordList.do?uId=" + userid + "&&orderId=" + orderId;
	} else {
	    return "redirect:/phoneOrder/79B4DE7C/orderList.do?isPayGive=1&&orderId=" + orderId + "&&uId=" + userid;
	}
    }

    /**
     * 收货地址列表
     */
    @RequestMapping( value = "/79B4DE7C/addressList" )
    public String addressList( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params, HttpSession session ) {
	logger.info( "进入收货地址列表页面" );
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
	    Object addressManage = params.get( "addressManage" );//从我的页面，地址管理跳转至地址列表
	    if ( CommonUtil.isEmpty( addressManage ) ) {
		Object addType = params.get( "addType" );
		if ( CommonUtil.isEmpty( addType ) && CommonUtil.isNotEmpty( params ) ) {
		    if ( CommonUtil.isNotEmpty( params.get( "addressType" ) ) && CommonUtil.isNotEmpty( params.get( "payWay" ) ) && CommonUtil
				    .isNotEmpty( params.get( "payWayName" ) ) && CommonUtil.isNotEmpty( params.get( "data" ) ) ) {
			request.getSession().setAttribute( "addressType", params.get( "addressType" ).toString() );
			request.getSession().setAttribute( "orderpayway", params.get( "payWay" ).toString() );
			request.getSession().setAttribute( "orderpaywayname", params.get( "payWayName" ).toString() );
			request.getSession().setAttribute( "dataOrder", params.get( "data" ).toString() );
			params.remove( "payWayName" );
		    }
		    Object deliveryMethodObj = request.getSession().getAttribute( "deliveryMethod" );
		    if ( CommonUtil.isNotEmpty( deliveryMethodObj ) ) {
			request.getSession().removeAttribute( "deliveryMethod" );
		    }
		    request.setAttribute( "addType", addType );
		}
	    }
	    request.setAttribute( "addressManage", addressManage );

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    String memberId = member.getId().toString();

	    params = mallOrderService.getMemberParams( member, params );
	    params.put( "busUserId", member.getBusid() );
	    List< Map< String,Object > > addressList = mallOrderService.selectShipAddress( params );
	    request.setAttribute( "addressList", addressList );

	    BusUser user = pageService.selUserByMember( member );
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
		//todo 地址
		MemberAddress mem = null;//eatPhoneService.getMemberAddress( Integer.parseInt( id.toString() ) );
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

   /* *//**
     * 新增/修改收货地址
     *
     *//*
    @RequestMapping( value = "/79B4DE7C/addAddress" )
    @SysLogAnnotation( op_function = "2", description = "新增/修改收货地址" )
    public void addAddress( HttpServletRequest request, @RequestParam Map< String,Object > params, HttpServletResponse response ) {
	logger.info( "进入新增/修改收货地址页面" );
	int memberId = SessionUtils.getLoginMember( request ).getId();
	PrintWriter out = null;
	try {
	    out = response.getWriter();
	    MemberAddress memAddress = (MemberAddress) JSONObject.toBean( JSONObject.fromObject( params.get( "params" ) ), MemberAddress.class );
	    String memPhone = memAddress.getMemPhone();
	    Map< String,Object > msg = new HashMap< String,Object >();

	    MemberAddress address = memberAddressMapper.findDefault( memberId );
	    if ( CommonUtil.isEmpty( address ) ) {
		memAddress.setMemDefault( 1 );
	    }
	    if ( !GTUtils.isPhone( memPhone.trim() ) ) {
		msg.put( "result", false );
		msg.put( "message", "手机号码格式不正确" );
	    } else {
		memAddress.setDfMemberId( memberId );
		msg = eatPhoneService.saveOrderAddress( memAddress );
	    }
	    out.write( new Gson().toJson( msg ) );
	} catch ( Exception e ) {
	    logger.error( "新增/修改收货地址异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    out.flush();
	    out.close();
	}
    }*/

    /**
     * 查询城市数据
     *
     * @param pid
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
     *
     *//*
    @RequestMapping( value = "/79B4DE7C/updateDefault" )
    @SysLogAnnotation( op_function = "3", description = "修改默认收获地址" )
    public void updateDefault( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Integer id ) {
	try {
	    String memberId = SessionUtils.getLoginMember( request ).getId().toString();
			*//*String memberId= "200";*//*
	    String msg = "false";
	    int count = eatPhoneService.updateDefaultArea( id, Integer.parseInt( memberId ) );
	    if ( count > 0 ) {
		msg = "true";
	    }
	    PrintWriter p = response.getWriter();
	    p.write( msg );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    logger.error( "修改默认收货地址异常：" + e.getMessage() );
	    e.printStackTrace();
	}
    }*/

    /**
     * 支付成功跳转页面
     */
    @RequestMapping( value = "{orderId}/{busId}/{type}/79B4DE7C/orderPaySuccess" )
    public String orderPaySuccess( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params
		    , @PathVariable int orderId, @PathVariable int busId, @PathVariable int type ) {
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
    public String presalePaySuccess( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params
		    , @PathVariable int proId, @PathVariable int busId, @PathVariable int shopId ) {
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
	    Member sMember = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( sMember, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    Member member = memberService.findMemberById( sMember.getId(), sMember );
	    //查询的积分、粉币跟session里面的积分、粉币不同，则更新session
	    if ( CommonUtil.isNotEmpty( sMember ) && CommonUtil.isNotEmpty( member ) ) {
		boolean flag = false;
		if ( CommonUtil.isNotEmpty( member.getIntegral() ) && CommonUtil.isNotEmpty( sMember.getIntegral() ) ) {
		    if ( member.getIntegral() < sMember.getIntegral() ) {
			flag = true;
		    }
		}
		if ( CommonUtil.isNotEmpty( member.getFansCurrency() ) && CommonUtil.isNotEmpty( sMember.getFansCurrency() ) ) {
		    if ( member.getFansCurrency() < sMember.getFansCurrency() ) {
			flag = true;
		    }
		}
		//todo 更新session数据
		/*if ( flag ) {
		    CommonUtil.setLoginMember( request, member );
		}*/
	    }

	    String result = "0";
	    if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		Integer shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
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
		if ( !orderType.equals( "1" ) && !orderType.equals( "3" )
				&& !orderType.equals( "4" ) ) {
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
	    List< Map< String,Object > > orderList = mallOrderService.mobileOrderList( params, userid );

	    //关闭超过30分钟未付款订单
	    mallOrderService.updateByNoMoney( params );
	    request.setAttribute( "orderList", orderList );
	    request.setAttribute( "memberId", sMember.getId() );
	    request.setAttribute( "pageid", result );
	    request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );
	    pageService.getCustomer( request, 0 );
	    if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		request.setAttribute( "shopid", request.getSession().getAttribute( "shopId" ) );
	    }
	    MallPaySet set = mallPaySetService.selectByMember( member );
	    request.setAttribute( "mallPaySet", set );

	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, userid );
	    if ( saleMemberId > 0 ) {
		request.setAttribute( "saleMemberId", saleMemberId );
	    }
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );

	    //KeysUtil keysUtil = new KeysUtil();
	    //request.setAttribute("alipaySubject", keysUtil.getEncString("商城下单"));
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
     * 进入申请退款页面
     */
    @RequestMapping( value = "/79B4DE7C/toReturn" )
    public String toReturn( HttpSession session, HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
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
		/*Integer memberId = 200;*/
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    MallOrderReturn orderReturn = (MallOrderReturn) JSONObject.toBean(
			    JSONObject.fromObject( params ), MallOrderReturn.class );
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
    public void closeReturnOrder( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入撤销退款申请" );
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    MallOrderReturn orderReturn = (MallOrderReturn) JSONObject.toBean(
			    JSONObject.fromObject( params ), MallOrderReturn.class );
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
	    List orderList = mallOrderService.mobileOrderList( params, userid );
	    JSONObject orderObj = JSONObject.fromObject( orderList.get( 0 ) );

	    if ( orders.getReceiveId() != null ) {
		params.put( "id", orders.getReceiveId() );
		List< Map< String,Object > > addressList = mallOrderService.selectShipAddress( params );
		request.setAttribute( "addressList", addressList );
	    }
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
    public String getTakeTheir( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params, HttpSession session ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
			/*Map<String, Object> publicMap = pageService.memberMap(userid);
			if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = pageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
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
	    if ( CommonUtil.isNotEmpty( params.get( "deliveryType" ) ) && CommonUtil.isNotEmpty( params.get( "payWay" ) ) && CommonUtil.isNotEmpty( params.get( "payWayName" ) ) ) {
		request.getSession().setAttribute( "deliveryMethod", params.get( "deliveryType" ).toString() );
		request.getSession().setAttribute( "orderpayway", params.get( "payWay" ).toString() );
		request.getSession().setAttribute( "orderpaywayname", params.get( "payWayName" ).toString() );
		params.remove( "payWayName" );
	    }
	    request.getSession().setAttribute( "dataOrder", params.toString() );
	    Object addTypeObj = request.getSession().getAttribute( "addressType" );
	    if ( CommonUtil.isNotEmpty( addTypeObj ) ) {
		request.getSession().removeAttribute( "addressType" );
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
    public String shareDaiFu( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params, @PathVariable int orderId ) {
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
			if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
			    shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
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
    public String getDaiFu( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params, @PathVariable int orderId ) {
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
			if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
			    shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
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
    public void goPay( HttpServletRequest request,
		    @RequestParam Map< String,Object > param,
		    HttpServletResponse response ) {
	logger.info( "进入去支付controller" );
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	int code = 1;
	try {
	    out = response.getWriter();
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
				    result.put( "result", false );
				    result.put( "msg", "您购买的秒杀商品已经被删除，请重新下单" );
				} else if ( seckill.getIsUse().toString().equals( "-1" ) ) {
				    code = -1;
				    result.put( "result", false );
				    result.put( "msg", "您购买的秒杀商品已经被失效，请重新下单" );
				} else if ( seckill.getStatus() == 0 ) {
				    code = -1;
				    result.put( "result", false );
				    result.put( "msg", "您购买的秒杀商品还没开始，请耐心等待" );
				} else if ( seckill.getStatus() == -1 ) {
				    code = -1;
				    result.put( "result", false );
				    result.put( "msg", "您购买的秒杀商品已经结束，请重新下单" );
				}
			    } else {
				code = -1;
				result.put( "result", false );
				result.put( "msg", "您购买的秒杀商品已经被删除，请重新下单" );
			    }
			} else {
			    code = -1;
			    result.put( "result", false );
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
					result = mallProductService.calculateInventory( detail.getProductId(), proSpecificas, proNum, order.getBuyerUserId() );
					if ( !( result.get( "result" ) ).equals( "true" ) ) {
					    code = -1;
					    break;
					}
				    }
				} else {
				    result = mallProductService
						    .calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
				    if ( !( result.get( "result" ) ).equals( "ture" ) ) {
					code = -1;
					break;
				    }
				}

			    }
			}
		    }
		    if ( order.getOrderStatus() == 2 || order.getOrderStatus() == 3 || order.getOrderStatus() == 4 ) {
			result.put( "result", false );
			code = 0;
			result.put( "msg", "您已经支付成功，无需再次支付" );
		    }
		    result.put( "proTypeId", order.getMallOrderDetail().get( 0 ).getProTypeId() );
		    result.put( "out_trade_no", order.getOrderNo() );
		    result.put( "orderMoney", order.getOrderMoney() );
		    result.put( "busId", order.getBusUserId() );
		}
	    }

	} catch ( Exception e ) {
	    code = -1;
	    result.put( "result", false );
	    result.put( "msg", "去支付失败，稍后请重新支付" );
	    logger.error( "去支付异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    if ( CommonUtil.isEmpty( result.get( "result" ) ) ) {
		code = 1;
		result.put( "result", true );
	    }
	    result.put( "code", code );
	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
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
	    MallAllEntity mallAllEntity = mallOrderNewService.calculateOrder( params, member );

	    if ( CommonUtil.isNotEmpty( mallAllEntity ) ) {
		result =  mallOrderNewService.getCalculateData( mallAllEntity );
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

}
