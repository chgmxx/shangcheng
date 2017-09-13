package com.gt.mall.controller.purchase;

import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Card;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.purchase.*;
import com.gt.mall.entity.purchase.*;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.utils.*;
import com.gt.mall.service.web.purchase.PurchaseCompanyModeService;
import com.gt.mall.service.web.purchase.PurchaseOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
 * 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "/purchaseOrder" )
public class PurchaseOrderController extends BaseController {

    @Autowired
    PurchaseOrderService       purchaseOrderService;
    @Autowired
    PurchaseCompanyModeService companyModeService;

    @Autowired
    PurchaseReceivablesDAO   receivablesDAO;
    @Autowired
    PurchaseLanguageDAO      languageDAO;
    @Autowired
    PurchaseCarouselDAO      carouselDAO;
    @Autowired
    PurchaseOrderDetailsDAO  purchaseOrderDetailsDAO;
    @Autowired
    PurchaseTermDAO          termDAO;
    @Autowired
    PurchaseContractDAO      contractDAO;
    @Autowired
    PurchaseContractOrderDAO contractOrderDAO;
    @Autowired
    PurchaseCompanyModeDAO   companyModeDAO;
    @Autowired
    private MallStoreService storeService;
    @Autowired
    MemberService memberService;

    /**
     * 订单首页
     *
     * @param request
     * @param parms
     *
     * @return
     */
    @RequestMapping( value = "/orderIndex" )
    public String orderIndex( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    parms.put( "busId", busUser.getId() );
	    PageUtil page = purchaseOrderService.findList( parms );
	    request.setAttribute( "page", page );
	    request.setAttribute( "parms", parms );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/index";
    }

    /**
     * 订单详情页面
     *
     * @param request
     * @param orderId
     *
     * @return
     */
    @RequestMapping( value = "/orderIndexDetail" )
    public String orderIndexDetail( HttpServletRequest request, int orderId ) {
	try {
	    //查询订单详情
	    PurchaseOrder order = purchaseOrderService.selectById( orderId );
	    //查询订单的商品详情
	    List< Map< String,Object > > orderdetails = purchaseOrderDetailsDAO.findOrderDetails( orderId );
	    if ( order.getOrderType().equals( "1" ) ) {
		//查询分期
		List< Map< String,Object > > termList = termDAO.findTermList( orderId );
		request.setAttribute( "termList", termList );
	    }
	    PurchaseCompanyMode company = companyModeService.selectById( order.getCompanyId() );
	    request.setAttribute( "company", company );
	    request.setAttribute( "order", order );
	    request.setAttribute( "orderdetails", orderdetails );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/chakanxiangqing";
    }

    /**
     * 进入订单新增页面
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( value = "/orderAdd" )
    public String orderAdd( HttpServletRequest request ) {
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    //查询所有的合同
	    List< Map< String,Object > > contractList = contractDAO.findAllList( busUser.getId() );
	    //查询所有的公司模板
	    List< Map< String,Object > > companyModeList = companyModeDAO.findAllList( busUser.getId() );
	    if ( CommonUtil.isNotEmpty( request.getParameter( "orderId" ) ) ) {
		Integer orderId = Integer.parseInt( request.getParameter( "orderId" ).toString() );
		PurchaseOrder order = purchaseOrderService.selectById( orderId );
		if ( !"1".equals( order.getOrderStatus() ) ) {
		    return "redirect:/purchaseOrder/orderIndex.do";
		}
		request.setAttribute( "order", order );
		if ( order.getOrderType().equals( "1" ) ) {
		    //查询分期
		    List< Map< String,Object > > termList = termDAO.findTermList( orderId );
		    request.setAttribute( "termList", termList );
		}
		List< Map< String,Object > > contractListMap = contractOrderDAO.findContractOrderList( orderId );
		if ( contractListMap.size() > 0 ) {
		    request.setAttribute( "orderContract", contractListMap.get( 0 ) );
		}
		//查询轮播图
		List< Map< String,Object > > carouselList = carouselDAO.findByOrderId( order.getId() );
		request.setAttribute( "carouselList", carouselList );
		//查询订单的商品详情
		List< Map< String,Object > > orderdetails = purchaseOrderDetailsDAO.findOrderDetails( orderId );
		request.setAttribute( "orderdetails", orderdetails );
	    }
	    request.setAttribute( "companyModeList", companyModeList );
	    request.setAttribute( "contractList", contractList );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/index-add";
    }

    /**
     * 根据店铺id查询商品
     *
     * @Title: editGroup
     */
    @RequestMapping( "/getProductByGroup" )
    public String getProductByGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	response.setCharacterEncoding( "utf-8" );
	try {
	    String shopIds = "";
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = purchaseOrderService.productList( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "map", params );
		}
	    }
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/choosePro";
    }

    /**
     * 进入商家回复页面
     *
     * @param request
     * @param languageId
     * @param orderId
     *
     * @return
     */
    @RequestMapping( value = "/orderIndexRemarks" )
    public String orderIndexRemarks( HttpServletRequest request, Integer languageId, Integer orderId ) {
	request.setAttribute( "languageId", languageId );
	request.setAttribute( "orderId", orderId );
	return "mall/purchase/orderRemark";
    }

    /**
     * 商家回复买家的留言
     *
     * @param orderId
     * @param languageId
     * @param languageContent
     *
     * @return
     */
    @RequestMapping( value = "/replyLanguage" )
    @ResponseBody
    public String replyLanguage( Integer orderId, Integer languageId, String languageContent ) {
	try {
	    PurchaseLanguage language = new PurchaseLanguage();
	    language.setId( languageId );
	    language.setIsRead( "1" );
	    language.setAdminContent( languageContent );
	    languageDAO.updateById( language );
	    return "true";
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return "false";
	}

    }

    /**
     * 保存订单
     *
     * @param request
     * @param termTime
     * @param termMoney
     * @param allMoney
     * @param discountMoney
     * @param count
     * @param orderDetailsId
     *
     * @return
     */
    @RequestMapping( value = "/saveOrder" )
    @ResponseBody
    public Map< String,Object > saveOrder( HttpServletRequest request, String[] termId, String[] carouselId, String[] termTime, String[] termMoney, String[] allMoney,
		    String[] discountMoney, String[] count, String[] orderDetailsId, String[] freight, String[] money, String[] productDetails, String[] productId,
		    String[] laborCost, String[] installationFee, String[] productImg, String[] productName, String[] carouselImg, String[] carouselUrl ) {
	BusUser user = SessionUtils.getLoginUser( request );
	Map< String,Object > map = new HashMap< String,Object >();
	PurchaseOrder order = new PurchaseOrder();
	List< PurchaseOrderDetails > orderDetailsList = new ArrayList< PurchaseOrderDetails >();
	List< PurchaseTerm > termList = new ArrayList< PurchaseTerm >();
	List< PurchaseCarousel > carouselList = new ArrayList< PurchaseCarousel >();
	try {
	    //填充订单数据
	    if ( CommonUtil.isNotEmpty( request.getParameter( "orderId" ) ) ) {
		order.setId( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
	    }
	    order.setBusId( user.getId() );
	    order.setOrderTitle( request.getParameter( "orderTitle" ).toString() );
	    order.setOrderQrcode( request.getParameter( "orderQrcode" ).toString() );
	    order.setOrderType( request.getParameter( "orderType" ).toString() );
	    order.setHaveContract( request.getParameter( "havaContract" ).toString() );
	    if ( request.getParameter( "havaContract" ).toString().equals( "0" ) ) {
		order.setContractId( request.getParameter( "contractId" ).toString() );
	    }
	    order.setHaveTax( request.getParameter( "haveTax" ).toString() );
	    order.setCompanyId( Integer.parseInt( request.getParameter( "companyId" ).toString() ) );
	    order.setOrderDescribe( request.getParameter( "orderDescribe" ).toString() );
	    order.setOrderExplain( request.getParameter( "orderExplain" ).toString() );
	    order.setOrderRemarks( request.getParameter( "orderRemarks" ).toString() );
	    order.setAllMoney( Double.parseDouble( request.getParameter( "orderAllMoney" ).toString() ) );
	    order.setFreight( Double.parseDouble( request.getParameter( "orderFreight" ).toString() ) );

	    //填充订单详情数据
	    for ( int i = 0; i < productId.length; i++ ) {
		PurchaseOrderDetails orderDetails = new PurchaseOrderDetails();
		if ( orderDetailsId != null && orderDetailsId.length > 0 && orderDetailsId.length > i ) {
		    orderDetails.setId( Integer.parseInt( orderDetailsId[i] ) );
		}
		if ( money != null && money.length > 0 && money.length > i ) {
		    orderDetails.setMoney( Double.parseDouble( money[i] ) );
		}
		if ( productDetails != null && productDetails.length > 0 && productDetails.length > i ) {
		    orderDetails.setProductDetails( productDetails[i] );
		}
		if ( productId != null && productId.length > 0 && productId.length > i ) {
		    orderDetails.setProductId( Integer.parseInt( productId[i] ) );
		}
		if ( productImg != null && productImg.length > 0 && productImg.length > i ) {
		    orderDetails.setProductImg( productImg[i] );
		}
		if ( productName != null && productName.length > 0 && productName.length > i ) {
		    orderDetails.setProductName( productName[i] );
		}
		orderDetails.setCount( Integer.parseInt( count[i] ) );
		orderDetails.setDiscountMoney( Double.parseDouble( discountMoney[i] ) );
		orderDetails.setFreight( Double.parseDouble( freight[i] ) );
		orderDetails.setLaborCost( Double.parseDouble( laborCost[i] ) );
		orderDetails.setInstallationFee( Double.parseDouble( installationFee[i] ) );
		orderDetails.setAllMoney( Double.parseDouble( allMoney[i] ) );
		orderDetailsList.add( orderDetails );
	    }
	    //填充分期数据
	    if ( order.getOrderType().equals( "1" ) ) {
		for ( int a = 0; a < termTime.length; a++ ) {
		    PurchaseTerm term = new PurchaseTerm();
		    if ( termId != null && termId.length > a && CommonUtil.isNotEmpty( termId[a] ) ) {
			term.setId( Integer.parseInt( termId[a] ) );
		    }
		    term.setTermIndex( a + 1 );
		    term.setTermMoney( Double.parseDouble( termMoney[a] ) );
		    term.setTermTime( DateTimeKit.parseDate( termTime[a] ) );
		    term.setTermBuy( "0" );
		    if ( CommonUtil.isNotEmpty( request.getParameter( "orderId" ) ) ) {
			term.setOrderId( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
		    }

		    termList.add( term );
		}
	    }
	    for ( int i = 0; i < carouselImg.length; i++ ) {
		PurchaseCarousel carousel = new PurchaseCarousel();
		if ( carouselId != null && carouselId.length > i && CommonUtil.isNotEmpty( carouselId[i] ) ) {
		    carousel.setId( Integer.parseInt( carouselId[i] ) );
		}
		carousel.setBusId( SessionUtils.getLoginUser( request ).getId() );
		carousel.setCarouselImg( carouselImg[i] );
		carousel.setCarouselUrl( carouselUrl[i] );
		if ( CommonUtil.isNotEmpty( request.getParameter( "orderId" ) ) ) {
		    carousel.setOrderId( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
		}
		carouselList.add( carousel );
	    }
	    return purchaseOrderService.saveOrder( order, orderDetailsList, termList, carouselList );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", "false" );
	    return map;
	}

    }

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param status
     */
    @RequestMapping( value = "/editStatus" )
    @ResponseBody
    public String editStatus( Integer orderId, Integer status ) {
	PurchaseOrder order = new PurchaseOrder();
	try {
	    order.setId( orderId );
	    order.setOrderStatus( status.toString() );
	    purchaseOrderService.updateById( order );
	    if ( status.equals( "2" ) || status == 2 ) {
		PurchaseOrder orderEntity = purchaseOrderService.selectById( orderId );
		// 添加会员记录
		/*UserConsume uc = new UserConsume();
		uc.setRecordtype( (byte) 2 );
		uc.setTotalmoney( orderEntity.getAllMoney() );
		uc.setOrderid( orderEntity.getId() );
		uc.setUctable( "purchase_order" );
		uc.setCreatedate( new Date() );
		uc.setPaystatus( (byte) 0 );
		uc.setOrdercode( orderEntity.getOrderNumber() );
		uc.setFreightmoney( orderEntity.getFreight() );*/
		//TODO 添加会员记录  UserConsume
		//		userConsumeMapper.insertSelective(uc);
	    }
	    return "1";
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return "0";
	}

    }

    /**
     * 订单二维码下载
     *
     * @param request
     * @param response
     * @param orderId
     *
     * @throws IOException
     */
    @RequestMapping( "/downQrcode" )
    public void downQrcode( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer orderId ) throws IOException {
	BusUser user = SessionUtils.getLoginUser( request );
	String url = PropertiesUtil.getHomeUrl() + "/" + "purchasePhone" + "/79B4DE7C/getMemberPower.do?orderId=" + orderId + "&busId=" + user.getId();
	String filename = "订单二维码.jpg";
	response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	response.setContentType( "application/octet-stream" );
	QRcodeKit.buildQRcode( url, 450, 450, response );
    }

    /**
     * 查看收款详情
     *
     * @param request
     * @param orderId
     *
     * @return
     */
    @RequestMapping( "/receivablesDetails" )
    public String receivablesDetails( HttpServletRequest request, @RequestParam Integer orderId, @RequestParam Integer memberId ) {
	try {
	    Member member = memberService.findMemberById( memberId, null );//查询用户信息
	    if ( member != null && member.getMcId() != null ) { //如果用户存在会员卡
		//TODO 会员卡信息 Card
		Card card = null;
		//		cardMapper.selectByPrimaryKey(member.getMcId()); // 查询会员卡信息
		request.setAttribute( "card", card );
	    }
	    request.setAttribute( "member", member );
	    PurchaseOrder order = purchaseOrderService.selectById( orderId );
	    request.setAttribute( "order", order );
	    List< Map< String,Object > > receivablesList = receivablesDAO.findReceivablesList( orderId );//查询报价单收款记录
	    request.setAttribute( "receivablesList", receivablesList );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/shoukuanxinxi";
    }

    /**
     * 查看留言列表并将所有留言设为已读
     *
     * @param request
     * @param orderId
     *
     * @return
     */
    @RequestMapping( "/languageList" )
    public String languageList( HttpServletRequest request, @RequestParam Integer orderId ) {
	//查询留言
	List< Map< String,Object > > languageList = languageDAO.findLanguangeList( orderId );
	for ( int i = 0; i < languageList.size(); i++ ) {
	  Member member=  memberService.findMemberById( CommonUtil.toInteger( languageList.get( i ).get( "member_id" ) ) ,null);
	    languageList.get( i ).put( "headimgurl", member.getHeadimgurl() );
	  if ( CommonUtil.isNotEmpty( member.getNickname()) ) {
	      try {
		  String bytes = member.getNickname();
		  languageList.get( i ).put( "nickname", new String( bytes.getBytes(), "UTF-8" ) );
	      } catch ( Exception e ) {
		  languageList.get( i ).put( "nickname", null );
	      }
	  }

	}
	//设置订单的留言为已阅状态
	languageDAO.updateLanguangeByOrderId( orderId );
	request.setAttribute( "languageList", languageList );
	return "mall/purchase/liuyanguanli";
    }

    /**
     * 查看留言详情
     *
     * @param request
     * @param orderId
     *
     * @return
     */
    @RequestMapping( "/languageDetails" )
    public String languageDetails( HttpServletRequest request, @RequestParam Integer orderId, @RequestParam Integer memberId ) {
	PurchaseLanguage language = new PurchaseLanguage();
	language.setOrderId( orderId );
	language.setMemberId( memberId );
	//查询留言
	List< Map< String,Object > > languageList = languageDAO.findLanguangeDetails( language );
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
	request.setAttribute( "languageList", languageList );
	return "mall/purchase/detail";
    }

}
