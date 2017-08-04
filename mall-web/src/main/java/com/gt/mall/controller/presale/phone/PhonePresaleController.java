package com.gt.mall.controller.presale.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.presale.MallPresaleDepositService;
import com.gt.mall.web.service.presale.MallPresaleService;
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商品预售表 前端控制器  (手机端)
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public class PhonePresaleController extends BaseController {
    @Autowired
    private MallPresaleService          mallPresaleService;
    @Autowired
    private MallPresaleDepositService   mallPresaleDepositService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private MallPresaleDepositDAO       mallPresaleDepositDAO;
    @Autowired
    private MallPaySetService           mallPaySetService;

    /**
     * 获取店铺下所有的预售（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/presaleall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String presaleall( HttpServletRequest request,
		    HttpServletResponse response, @PathVariable int shopid,
		    @RequestParam Map< String,Object > params ) throws Exception {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
			/*Map<String, Object> publicMap = pageService.memberMap(userid);
			if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				pageService.isLogin(member, userid, request);
			}*/
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    //todo userLogin
	    /*String returnUrl = userLogin(request, response, userid, loginMap);
	    if(CommonUtil.isNotEmpty(returnUrl)){
		return returnUrl;
	    }*/
			/*String memberId = "200";
			member = memberService.findById(Integer.valueOf(memberId));*/
	    boolean isShop = pageService.wxShopIsDelete( shopid );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();// 图片url链接前缀
	    List groupList = pageService.storeList( shopid, 1, 0 );// 获取分类
	    String pageid = "0";
	    List list1 = pageService.shoppage( shopid );// 获取商品主页
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";// 查询条件
	    String desc = "1";// 排序 默认倒序
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = params.get( "type" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    params.put( "shopId", shopid );
    /*if(CommonUtil.isNotEmpty(member)){
	    MallPaySet set = mallPaySetService.selectByMember(member);
	    boolean isOpenPresale = false;
	    if(CommonUtil.isNotEmpty(set)){
		    if(CommonUtil.isNotEmpty(set.getIsPresale())){
			    if(set.getIsPresale().toString().equals("1")){
				    isOpenPresale = true;
			    }
		    }
	    }
	    if(isOpenPresale){*/
	    List< Map< String,Object > > productList = mallPresaleService
			    .getPresaleAll( member, params );// 查询店铺下所有加入预售的商品
	    request.setAttribute( "productList", productList );
		    /*}
	    }*/
	    if ( userid > 0 ) {
		request.setAttribute( "userid", userid );
	    }
	    //查询搜索标签，历史记录
	    pageService.getSearchLabel( request, shopid, member, params );

	    request.setAttribute( "shopId", shopid );
	    request.setAttribute( "pageid", pageid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "groupList", groupList );
	    request.setAttribute( "imgHttp", http );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "proName", params.get( "proName" ) );

	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		request.getSession().setAttribute( "shopId", shopid );
	    } else {
		if ( !request.getSession().getAttribute( "shopId" ).toString().equals( CommonUtil.toString( shopid ) ) ) {
		    request.getSession().setAttribute( "shopId", shopid );
		}
	    }
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入预售商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/presale/phone/presaleall";
    }

    /**
     * 进入交纳预收定金
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{proId}/{invId}/{presaleId}/79B4DE7C/toAddDeposit" )
    public String toAddDeposit( HttpServletRequest request, HttpServletResponse response,
		    @PathVariable int proId, @PathVariable int invId, @PathVariable int presaleId, @RequestParam Map< String,Object > param ) throws Exception {
	logger.info( "进入交纳定金的页面...." );
	String jsp = "mall/presale/phone/toAddDeposit";
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( param.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map mapmessage = pageService.querySelct( proId );//获取商品信息
	    if ( CommonUtil.isNotEmpty( mapmessage ) ) {
		userid = CommonUtil.toInteger( mapmessage.get( "user_id" ) );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    //todo userLogin
	    /*String returnUrl = userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }*/
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = pageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    int shopid = 0;
	    if ( CommonUtil.isNotEmpty( mapmessage.get( "shop_id" ) ) ) {
		shopid = CommonUtil.toInteger( mapmessage.get( "shop_id" ).toString() );

		if ( CommonUtil.isEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		    request.getSession().setAttribute( "shopId", mapmessage.get( "shop_id" ) );
		} else {
		    if ( !request.getSession().getAttribute( "shopId" ).toString().equals( mapmessage.get( "shop_id" ).toString() ) ) {
			request.getSession().setAttribute( "shopId", mapmessage.get( "shop_id" ) );
		    }
		}
	    }
	    int proNum = 1;
	    if ( CommonUtil.isNotEmpty( param.get( "num" ) ) ) {
		proNum = CommonUtil.toInteger( param.get( "num" ) );
		request.setAttribute( "proNum", param.get( "num" ) );
	    }
	    //查询商品的预售信息
	    MallPresale presale = mallPresaleService.getPresaleByProId( proId, shopid, presaleId );
	    if ( CommonUtil.isNotEmpty( presale ) ) {
		DecimalFormat df = new DecimalFormat( "######0.00" );
		double money = CommonUtil.toDouble( presale.getDepositPercent() ) * proNum;
		presale.setDepositPercent( BigDecimal.valueOf( CommonUtil.toDouble( df.format( money ) ) ) );
	    }

	    request.setAttribute( "presale", presale );
	    request.setAttribute( "mapmessage", mapmessage );

	    List imagelist = pageService.imageProductList( proId, 1 );//获取轮播图列表
	    request.setAttribute( "imagelist", imagelist.get( 0 ) );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    request.setAttribute( "proId", proId );
	    String is_specifica = mapmessage.get( "is_specifica" ).toString();
	    Map guige = new HashMap();
	    if ( is_specifica.equals( "1" ) ) {
		guige = pageService.productSpecifications( proId, invId + "" );
	    }

	    int memType = 0;
	    //todo memPayService.isMemember
	   /* if ( memPayService.isMemember( member.getId() ) ) {//是否为会员
	   	//todo memPayService.isCardType
		memType = memPayService.isCardType( member.getId() );
	    }*/
	    request.setAttribute( "memType", memType );
	    request.setAttribute( "memberId", member.getId() );
	    request.setAttribute( "guige", guige );

	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) ) ) {
		isWxPay = 1;//可以微信支付
	    }
	    //todo alipayUserService.findAlipayUserByBusId
	    /*AlipayUser alipayUser = alipayUserService.findAlipayUserByBusId( member.getBusid() );
	    if ( CommonUtil.isNotEmpty( alipayUser ) && isWxPay == 0 ) {
		isAliPay = 1;//可以支付宝支付
	    }*/
	    request.setAttribute( "isWxPay", isWxPay );
	    request.setAttribute( "isAliPay", isAliPay );
	    if ( CommonUtil.isNotEmpty( param.get( "oMoney" ) ) ) {
		request.setAttribute( "orderMoney", param.get( "oMoney" ) );
	    }
	    if ( CommonUtil.isNotEmpty( param.get( "specId" ) ) ) {
		request.setAttribute( "specId", param.get( "specId" ) );
	    }
	    //KeysUtil keysUtil = new KeysUtil();
	    //request.setAttribute("alipaySubject", keysUtil.getEncString("商城预售缴纳定金"));
	    request.setAttribute( "alipaySubject", "商城预售缴纳定金" );
	} catch ( Exception e ) {
	    logger.error( "进入交纳预收定金的页面出错：" + e );
	    e.printStackTrace();
	}
	return jsp;

    }

    /**
     * 交纳定金
     *
     */
    @RequestMapping( value = "/79B4DE7C/addDeposit" )
    @SysLogAnnotation( op_function = "2", description = "预售交纳定金" )
    @Transactional( rollbackFor = Exception.class )
    public void addDeposit( HttpServletRequest request, @RequestParam Map< String,Object > param, HttpServletResponse response ) {
	logger.info( "进入生成订单页面-预售交纳定金" );
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();

	    Member member = SessionUtils.getLoginMember( request );
	    String memberId = member.getId().toString();
			/*String memberId = "200";*/
	    if ( CommonUtil.isNotEmpty( param.get( "userId" ) ) ) {
		memberId = param.get( "userId" ).toString();
	    }

	    if ( param != null ) {
		result = mallPresaleDepositService.addDeposit( param, memberId );
	    }
	    result.put( "busId", member.getBusid() );
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "交纳定金失败，稍后请重新提交" );
	    logger.error( "交纳预售定金异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "交纳定金花费：" + second + "秒" );

	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
	}
    }

    /**
     * 储值卡支付成功的回调
     *
     */
    @RequestMapping( value = "/79B4DE7C/payWay" )
    @SysLogAnnotation( op_function = "2", description = "定金储蓄卡支付成功添加记录和修改" )
    @Transactional( rollbackFor = Exception.class )
    public String payWay( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	   /* Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    //todo userLogin
	    String returnUrl = userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }*/
	    String memberId = member.getId().toString();
			/*String memberId = "200";*/

	    Integer payWay = CommonUtil.toInteger( params.get( "payWay" ) );

	    Map< String,Object > payRresult = new HashMap< String,Object >();
	    double orderMoney = Double.parseDouble( params.get( "orderMoney" ).toString() );

	    if ( payWay == 2 ) {//储蓄卡支付方式
		//todo memberpayService
		//payRresult = memberpayService.storePay( Integer.parseInt( memberId ), orderMoney );
		String result = payRresult.get( "result" ).toString();
		if ( result.equals( "2" ) ) {
		    params.put( "out_trade_no", params.get( "no" ) );

		    mallPresaleDepositService.paySuccessPresale( params );
		} else if ( result.equals( "1" ) && payWay != 4 ) {
		    return "redirect:/phoneMemberController/79B4DE7C/recharge.do?id=" + memberId;
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "储蓄卡支付定金异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "储蓄卡支付花费：" + second + "秒" );
	}
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( params.get( "no" ).toString() );

	return "redirect:/mallPage/" + deposit.getProductId() + "/" + deposit.getShopId() + "/79B4DE7C/phoneProduct.do";
    }

    /**
     * 消息提醒
     *
     */
    @RequestMapping( value = "/79B4DE7C/messageRemind" )
    @SysLogAnnotation( op_function = "2", description = "预售提醒" )
    @Transactional( rollbackFor = Exception.class )
    public void messageRemind( HttpServletRequest request, @RequestParam Map< String,Object > param, HttpServletResponse response ) {
	logger.info( "预售提醒controller" );
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();

	    String memberId = SessionUtils.getLoginMember( request ).getId().toString();
			/*String memberId = "200";*/
	    if ( CommonUtil.isNotEmpty( param.get( "userId" ) ) ) {
		memberId = param.get( "userId" ).toString();
	    }

	    if ( param != null ) {
		result = mallPresaleDepositService.addMessage( param, memberId );
	    }
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "预售提醒失败，稍后请重新提交" );
	    logger.error( "预售提醒异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
	}
    }

    /**
     * 进入我的定金
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/myDeposit" )
    public String myMargin( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    //todo userLogin
	    /*String returnUrl = userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }*/
	    String memberId = member.getId().toString();
			/*String memberId = "200";*/
	    if ( CommonUtil.isNotEmpty( params.get( "userId" ) ) ) {
		memberId = params.get( "userId" ).toString();
	    }

	    MallPresaleDeposit deposit = new MallPresaleDeposit();
	    if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
		List< String > lists = new ArrayList< String >();
		for ( String oldMemberId : member.getOldid().split( "," ) ) {
		    if ( CommonUtil.isNotEmpty( oldMemberId ) ) {
			lists.add( oldMemberId );
		    }
		}
		deposit.setOldUserIdList( lists );
	    } else {
		deposit.setUserId( Integer.parseInt( memberId ) );
	    }

	    List< Map< String,Object > > depositList = mallPresaleDepositService.getMyPresale( deposit );

	    request.setAttribute( "depositList", depositList );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "type", 2 );
	    pageService.getCustomer( request, 0 );
	} catch ( Exception e ) {
	    logger.error( "进入我的保证金页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/presale/phone/myDeposit";
    }
}
