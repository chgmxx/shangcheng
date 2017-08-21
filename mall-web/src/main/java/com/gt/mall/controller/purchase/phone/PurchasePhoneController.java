package com.gt.mall.controller.purchase.phone;

import com.gt.mall.bean.Member;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.purchase.*;
import com.gt.mall.entity.purchase.*;
import com.gt.mall.service.web.purchase.*;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.service.web.purchase.impl.PurchaseReceivablesServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 采购 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "purchasePhone" )
public class PurchasePhoneController extends AuthorizeOrLoginController {

    private final static Logger logger = Logger.getLogger( PurchasePhoneController.class );
    @Autowired
    PurchaseOrderService        orderService;
    @Autowired
    PurchaseLanguageDAO         languageDAO;
    @Autowired
    PurchaseOrderDetailsDAO     orderDetailsDAO;
    @Autowired
    PurchaseCompanyModeService  companyModeService;
    @Autowired
    PurchaseTermDAO             termDAO;
    @Autowired
    PurchaseCarouselDAO         carouselDAO;
    @Autowired
    PurchaseReceivablesDAO      receivablesDAO;
    @Autowired
    PurchaseOrderDetailsService detailsService;
    @Autowired
    PurchaseContractService     contractService;
    @Autowired
    PurchaseContractOrderDAO    contractOrderDAO;

    @Autowired
    private PurchaseReceivablesService     receivablesService;
    @Autowired
    private PurchaseReceivablesServiceImpl receivablesServiceImpl;
    @Autowired
    private PurchaseOrderStatisticsDAO     orderStatisticsDAO;

    /**
     * 手机端订单详情首页
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/findOrder" )
    public String findOrder( HttpServletRequest request, @RequestParam Integer orderId ) {
	try {
	    String stage = ""; //期数
	    Double nowMoney = 0.0; //如果是订单类型为分期 该属性为本期需要支付的金额
	    Double nextMoney = 0.0; //如果是订单类型为分期 该属性为下期需要支付的金额
	    Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
	    //查询订单详情
	    PurchaseOrder order = orderService.selectById( orderId );
	    if ( order != null && order.getId() != null ) {
		//查询订单的商品详情
		List< Map< String,Object > > orderdetails = orderDetailsDAO.findOrderDetails( order.getId() );
		//查询留言
		List< Map< String,Object > > languageList = languageDAO.findAllList( order.getId() );
		for ( int i = 0; i < languageList.size(); i++ ) {
		    if ( languageList.get( i ).containsKey( "nickname" ) ) {
			try {
			    byte[] bytes = (byte[]) languageList.get( i ).get( "nickname" );
			    languageList.get( i ).put( "nickname", new String( bytes, "UTF-8" ) );
			} catch ( Exception e ) {
			    languageList.get( i ).put( "nickname", null );
			}
		    }
		}
		//查询轮播图
		List< Map< String,Object > > carouselList = carouselDAO.findByOrderId( order.getId() );
		PurchaseCompanyMode companyMode = companyModeService.selectById( order.getCompanyId() );
		if ( order.getOrderType().equals( "1" ) ) {
		    //查询分期
		    List< Map< String,Object > > termList = termDAO.findTermList( order.getId() );
		    for ( int i = 0; i < termList.size(); i++ ) {
			PurchaseTerm term = (PurchaseTerm) termList.get( i );
			if ( term.getTermBuy().equals( "0" ) ) {
			    stage = i + 1 + "/" + termList.size();
			    nowMoney = term.getTermMoney();
			    if ( i + 1 < termList.size() && ( (PurchaseTerm) termList.get( i + 1 ) ).getTermBuy().toString().equals( "0" ) ) {
				nextMoney = ( (PurchaseTerm) termList.get( i + 1 ) ).getTermMoney();
			    }
			    break;
			}
		    }
		    retainage = order.getAllMoney();
		    for ( int i = 0; i < termList.size(); i++ ) {
			if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
			    retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
			}
		    }
		    if ( retainage > 0 ) {
			retainage = getDiscountMoney( retainage, 10 );
		    } else {
			retainage = 0.0;
		    }
		}
		//查询收款
		List< Map< String,Object > > receivablesList = receivablesDAO.findReceivablesList( order.getId() );
		request.setAttribute( "order", order );
		request.setAttribute( "stage", stage );
		request.setAttribute( "nowMoney", nowMoney );
		request.setAttribute( "retainage", retainage );
		request.setAttribute( "nextMoney", nextMoney );
		request.setAttribute( "orderdetails", orderdetails );
		request.setAttribute( "languageList", languageList );
		request.setAttribute( "carouselList", carouselList );
		request.setAttribute( "companyMode", companyMode );
		request.setAttribute( "receivablesList", receivablesList );
	    }
	} catch ( Exception e ) {
	    logger.error( "手机端订单详情首页异常:" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/purchase/phone/index";
    }

    /**
     * 查询订单详情商品信息
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/findDetails" )
    public String findDetails( HttpServletRequest request ) {
	try {
	    PurchaseOrderDetails orderDetails = orderDetailsDAO.selectById( Integer.parseInt( request.getParameter( "id" ).toString() ) );
	    if ( orderDetails != null && orderDetails.getId() != null && orderDetails.getProductId() != null ) {
		List< Map< String,Object > > imgList = detailsService.productImg( orderDetails.getProductId() );
		if ( imgList != null ) {
		    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
		    request.setAttribute( "imgList", imgList );
		}
		request.setAttribute( "orderDetails", orderDetails );
	    }
	} catch ( Exception e ) {
	    logger.error( "查询订单详情商品信息异常:" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/purchase/phone/detail";
    }

    /**
     * 付款详情
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/buy" )
    public String buy( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Double nowMoney = 0.0; //如果是订单类型为分期 该属性为本期需要支付的金额
	    Double nextMoney = 0.0; //如果是订单类型为分期 该属性为下期需要支付的金额
	    Double actualMoney = 0.0; //改属性为本次需要付款的金额
	    Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
	    Integer grDiscount = 0; //如果是折扣卡这个值代表折扣数
	    Integer termId = 0; //本期分期的id
	    String orderId = request.getParameter( "orderId" ).toString(); //订单id
	    String busId = request.getParameter( "busId" ).toString(); //订单的商户id
	    String haveContract = request.getParameter( "haveContract" ).toString(); //是否需要合同
	    //授权部分
	    String redisKey = Constants.REDIS_KEY + CommonUtil.getCode();
	    JedisUtil.set( redisKey, "/purchasePhone/79B4DE7C/buy.do?orderId=" + orderId + "&busId=" + busId + "&haveContract=" + haveContract, 300 );
	    Map< String,Object > mapParam = new HashMap< String,Object >();
	    mapParam.put( "redisKey", redisKey );
	    mapParam.put( "busId", busId );
	    String returnStr = userLogin( request, response, mapParam );
	    if ( CommonUtil.isNotEmpty( returnStr ) ) {return returnStr;}
	    //判断是否存在合同 ,如果有合同跳转合同页面待用户确认
	    if ( request.getParameter( "haveContract" ) != null && request.getParameter( "haveContract" ).toString().equals( "0" ) ) {
		List< Map< String,Object > > contractListMap = contractOrderDAO.findContractOrderList( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
		if ( contractListMap.size() > 0 ) {
		    PurchaseContract contract = contractService.selectById( Integer.parseInt( contractListMap.get( 0 ).get( "contract_id" ).toString() ) );
		    request.setAttribute( "contract", contract );
		}
		request.setAttribute( "orderId", request.getParameter( "orderId" ).toString() );
		request.setAttribute( "busId", request.getParameter( "busId" ).toString() );
		return "mall/purchase/phone/hetong";
	    } else { // 如果不存在合同则直接跳转到报价单详情页进行付款操作
		//浏览器类型判断
		if ( CommonUtil.judgeBrowser( request ) != 1 ) {request.setAttribute( "payType", 0 );} else {request.setAttribute( "payType", 1 );}
		//查询订单详情
		PurchaseOrder order = orderService.selectById( Integer.parseInt( request.getParameter( "orderId" ) ) );
		if ( order.getOrderType().equals( "1" ) ) { //如果订单的类型是分期
		    List< Map< String,Object > > termList = termDAO.findTermList( order.getId() );//查询分期
		    for ( int i = 0; i < termList.size(); i++ ) {
			PurchaseTerm term = (PurchaseTerm) termList.get( i ); //获得一条分期数据
			if ( term.getTermBuy().equals( "0" ) ) { //如果出现未付款的数据
			    nowMoney = term.getTermMoney(); // 保存本期应该的付款
			    actualMoney = term.getTermMoney();
			    termId = term.getId();
			    if ( i + 1 < termList.size() && ( (PurchaseTerm) termList.get( i + 1 ) ).getTermBuy().toString().equals( "0" ) ) {
				nextMoney = ( (PurchaseTerm) termList.get( i + 1 ) ).getTermMoney();
			    }
			    break;
			}
		    }
		    retainage = order.getAllMoney();
		    for ( int i = 0; i < termList.size(); i++ ) {
			if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
			    retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
			}
		    }
		    if ( retainage > 0 ) {
			retainage = getDiscountMoney( retainage, 10 );
		    } else {
			retainage = 0.0;
		    }

		} else { //如果订单的类型是全款
		    //查询收款
		    List< Map< String,Object > > receivablesList = receivablesDAO.findReceivablesList( order.getId() );
		    if ( receivablesList == null || receivablesList.size() == 0 ) {
			nowMoney = order.getAllMoney();
			actualMoney = order.getAllMoney();
		    }
		}
		Member member = SessionUtils.getLoginMember( request );
		if ( member.getMcId() != null && member.getMcId() > 0 ) {
		    //TODO 会员卡信息 Card
		    //		    Card card = cardMapper.selectByPrimaryKey( member.getMcId() );
		    //		    if ( card.getCtId() == 2 ) {
		    //TODO 赠送规则 giveRuleMapper.findBybusIdAndGtIdAndCtId
		    //			GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( member.getBusid(), card.getGtId(), card.getCtId() );
		    //			if ( giveRule != null ) {
		    //			    grDiscount = giveRule.getGrDiscount() / 10;
		    //			    actualMoney = getDiscountMoney( nowMoney, giveRule.getGrDiscount() / 10 );
		    //			}
		    //		    }
		    //		    request.setAttribute( "card", card );
		}
		request.setAttribute( "nowMoney", nowMoney );
		request.setAttribute( "nextMoney", nextMoney );
		request.setAttribute( "actualMoney", actualMoney );
		request.setAttribute( "retainage", retainage );
		request.setAttribute( "grDiscount", grDiscount );
		request.setAttribute( "termId", termId );
		request.setAttribute( "member", member );
		request.setAttribute( "order", order );
	    }
	} catch ( Exception e ) {
	    logger.error( "付款详情异常:" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/purchase/phone/order";
    }

    /**
     * 还能用多少粉币
     *
     * @param response
     * @param actualMoney
     * @param fansCurrency
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/currencyCount" )
    public void currencyCount( HttpServletResponse response, Double actualMoney, Double fansCurrency ) throws IOException {
	//TODO 粉币方法 memberPayService.currencyCount()
	//	Double satisfy_fenbi = memberPayService.currencyCount( actualMoney, fansCurrency );
	//	response.getWriter().print( satisfy_fenbi );
    }

    /**
     * 还能用多少积分
     *
     * @param response
     * @param actualMoney
     * @param integral
     * @param busId
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/integralCount" )
    public void integralCount( HttpServletResponse response, Double actualMoney, Double integral, Integer busId ) throws IOException {
	//TODO 积分方法 memberPayService.integralCount()
	//	Double satisfy_jifen = memberPayService.integralCount( actualMoney, integral, busId );
	//	response.getWriter().print( satisfy_jifen );
    }

    public static double getDiscountMoney( double money, Integer discount ) {
	money = money / 10 * discount;
	String str = String.format( "%.2f", money );
	if ( Double.parseDouble( str ) < 0.01 ) { return 0.01; }
	return Double.parseDouble( str );
    }

    /**
     * 去往写留言的页面
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/languagePage" )
    public String languagePage( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	String redisKey = Constants.REDIS_KEY + CommonUtil.getCode();
	JedisUtil.set( redisKey,
			"/purchasePhone/79B4DE7C/languagePage.do?orderId=" + request.getParameter( "orderId" ).toString() + "&busId=" + request.getParameter( "busId" ).toString(),
			300 );
	Map< String,Object > mapParam = new HashMap< String,Object >();
	mapParam.put( "redisKey", redisKey );
	mapParam.put( "busId", Integer.parseInt( request.getParameter( "busId" ).toString() ) );
	String returnStr = userLogin( request, response, mapParam );
	if ( CommonUtil.isNotEmpty( returnStr ) ) { return returnStr; }
	PurchaseOrder order = orderService.selectById( Integer.parseInt( request.getParameter( "orderId" ) ) );
	request.setAttribute( "orderId", order.getId() );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    request.setAttribute( "member", member );
	    request.setAttribute( "orderTitle", order.getOrderTitle() );
	} catch ( Exception e ) {
	    logger.error( "去往写留言的页面异常:" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/purchase/phone/msg";
    }

    /**
     * 写留言
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/writeLanguage" )
    public void writeLanguage( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	try {
	    Integer memberId = SessionUtils.getLoginMember( request ).getId();
	    PurchaseLanguage language = new PurchaseLanguage();
	    language.setIsRead( "0" );
	    language.setLanguageTime( new Date() );
	    language.setMemberId( memberId );
	    language.setLanguageContent( request.getParameter( "languageContent" ).toString() );
	    language.setOrderId( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
	    languageDAO.insert( language );
	    response.getWriter().print( "true" );
	} catch ( Exception e ) {
	    logger.error( "写留言异常:" + e.getMessage() );
	    e.printStackTrace();
	    response.getWriter().print( "false" );
	}
    }

    /**
     * 新增收款记录
     *
     * @param request
     * @param memberId
     * @param orderId
     * @param busId
     * @param buyMode
     * @param money
     * @param fansCurrency
     * @param integral
     * @param coupon
     * @param termId
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/addReceivables" )
    @ResponseBody
    public String addReceivables( HttpServletRequest request, @RequestParam Integer memberId, @RequestParam Integer orderId, @RequestParam Integer busId,
		    @RequestParam String buyMode, @RequestParam Double money, Double fansCurrency, Double integral, String coupon, String termId, Integer discount ) {
	try {
	    receivablesServiceImpl.addReceivables( request, memberId, orderId, busId, buyMode, money, fansCurrency, integral, coupon, termId, discount );
	    return "true";
	} catch ( Exception e ) {
	    logger.error( "新增收款记录异常:" + e.getMessage() );
	    e.printStackTrace();
	    return "false";
	}

    }

    /**
     * 授权保存统计
     *
     * @param response
     * @param request
     */
    @RequestMapping( value = "/79B4DE7C/getMemberPower" )
    public String getMemberPower( HttpServletResponse response, HttpServletRequest request, @RequestParam Integer busId, @RequestParam Integer orderId ) {
	try {
	    //浏览器类型判断
	    if ( CommonUtil.judgeBrowser( request ) != 1 ) {
		request.setAttribute( "payType", 0 );
	    } else {
		String redisKey = Constants.REDIS_KEY + CommonUtil.getCode();
		JedisUtil.set( redisKey, "/purchasePhone/79B4DE7C/getMemberPower.do?orderId=" + orderId + "&busId=" + busId, 300 );
		Map< String,Object > mapParam = new HashMap< String,Object >();
		mapParam.put( "redisKey", redisKey );
		mapParam.put( "busId", Integer.parseInt( request.getParameter( "busId" ).toString() ) );
		String returnStr = userLogin( request, response, mapParam );
		if ( CommonUtil.isNotEmpty( returnStr ) ) { return returnStr; }
		request.setAttribute( "payType", 1 );
	    }
	    //记录统计
	    PurchaseOrderStatistics orderStatistics = new PurchaseOrderStatistics();
	    Member member = SessionUtils.getLoginMember( request );
	    if ( member != null ) {
		orderStatistics.setMemberId( member.getId() );
	    }
	    orderStatistics.setLookDate( new Date() );
	    orderStatistics.setLookIp( CommonUtil.getIpAddr( request ) );
	    orderStatistics.setBusId( busId );
	    orderStatistics.setOrderId( orderId );
	    orderStatisticsDAO.insert( orderStatistics );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "redirect:/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId;
    }

    /**
     * 支付宝支付订单
     *
     * @param request
     * @param response
     * @param memberId
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/aliCgPay" )
    public void cgPay( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer memberId, @RequestParam Integer busId, String termId,
		    @RequestParam double money, @RequestParam double discountmoney, Double fenbi, Integer jifen, Integer discount, @RequestParam String paymentType,
		    @RequestParam Integer orderId, Integer dvId, String disCountdepict ) throws IOException {
	Map< String,Object > map = null;
	try {
	    map = receivablesService.aliCgPay( memberId, busId, termId, money, discountmoney, fenbi, jifen, discount, paymentType, orderId, dvId, disCountdepict );
	} catch ( Exception e ) {
	    map = new HashMap< String,Object >();
	    map.put( "result", false );
	    map.put( "message", "下单失败" );
	    logger.error( "支付宝支付订单异常:" + e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 分期详情
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/termDetails" )
    public String termDetails( HttpServletRequest request, @RequestParam Integer orderId ) {
	Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
	PurchaseOrder order = orderService.selectById( orderId );//查询订单
	int index = 0; //已还期数
	//查询分期
	retainage = order.getAllMoney();
	List< Map< String,Object > > termList = termDAO.findTermList( orderId );
	for ( int i = 0; i < termList.size(); i++ ) {
	    if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
		index++;
		retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
	    }
	}
	if ( retainage > 0 ) {
	    retainage = getDiscountMoney( retainage, 10 );
	} else {
	    retainage = 0.0;
	}
	request.setAttribute( "order", order );
	request.setAttribute( "index", index );
	request.setAttribute( "retainage", retainage );
	request.setAttribute( "termList", termList );
	return "mall/purchase/phone/xiangqing";
    }
}
