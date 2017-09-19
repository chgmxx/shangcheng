package com.gt.mall.controller.seller.phone;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.Member;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.seller.MallSellerSetDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.entity.seller.MallSellerWithdraw;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerOrderService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.seller.MallSellerWithdrawService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.sms.OldApiSms;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
public class PhoneSellerController extends AuthorizeOrLoginController {

    @Autowired
    private MallSellerService mallSellerService;

    @Autowired
    private MallPageService mallPageService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallSellerOrderService mallSellerOrderService;

    @Autowired
    private MallSellerWithdrawService mallSellerWithdrawService;

    @Autowired
    private MallSellerMallsetService mallSellerMallSetService;

    @Autowired
    private MallPifaApplyService mallPifaApplyService;

    @Autowired
    private MallSellerSetDAO mallSellerSetDAO;

    @Autowired
    private MallPaySetService mallPaySetService;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private SmsService smsService;

    /**
     * 进入申请超级销售员页面
     */
    @SysLogAnnotation( description = "销售员——申请超级销售员", op_function = "2" )
    @RequestMapping( value = "79B4DE7C/toApplySeller" )
    public String toApplySeller( HttpServletRequest request, HttpServletResponse response ) {
	logger.info( "进入申请超级销售员页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    WxPublicUsers wx = null;
	    if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
		userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
		wx = wxPublicUserService.selectByUserId( userid );
	    } else if ( CommonUtil.isNotEmpty( request.getParameter( "pub_id" ) ) ) {//在公众号直接放链接
		wx = wxPublicUserService.selectById( CommonUtil.toInteger( request.getParameter( "pub_id" ) ) );
	    }
	    if ( userid == 0 && CommonUtil.isNotEmpty( request.getParameter( "member_id" ) ) ) {
		int memberid = CommonUtil.toInteger( request.getParameter( "member_id" ) );
		Member members = memberService.findMemberById( memberid, member );
		userid = members.getBusid();
		wx = wxPublicUserService.selectByMemberId( members.getId() );
	    }
	    if ( CommonUtil.isNotEmpty( wx ) && userid == 0 ) {
		userid = wx.getBusUserId();
	    }
	    if ( userid > 0 ) {
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    boolean isError = false;
	    boolean isIndex = false;
	    String errorMsg = "";
	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( set.getIsSeller().toString().equals( "1" ) ) {//开启销售员
		    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

		    if ( set.getIsCheckSeller().toString().equals( "1" ) ) {//开启销售员审核
			if ( CommonUtil.isNotEmpty( seller ) ) {
			    if ( seller.getCheckStatus() == 1 && seller.getIsStartUse() == 1 ) {//审核通过并且已经开启
				isIndex = true;
			    } else if ( seller.getCheckStatus() == 0 ) {//审核中
				isError = true;
				errorMsg = "您的申请已经提交，请等候审核！";
			    } else if ( seller.getCheckStatus() == -1 ) {//审核中
				isError = true;
				errorMsg = "您的申请审核不通过！";
			    } else if ( seller.getIsStartUse() != 1 ) {
				isError = true;
				errorMsg = "您的销售员暂不能使用";
			    }
			}
		    } else {
			if ( CommonUtil.isNotEmpty( seller ) ) {
			    if ( seller.getIsStartUse() == 1 ) {//开启销售员
				isIndex = true;
			    } else {//暂停销售员
				isError = true;
				errorMsg = "您的销售员暂不能使用";
			    }
			    if ( seller.getCheckStatus() != 1 ) {
				MallSeller mallSeller = new MallSeller();
				mallSeller.setCheckStatus( 1 );
				mallSeller.setId( seller.getId() );
				mallSellerService.updateSeller( mallSeller );
			    }
			} else {
			    seller = new MallSeller();
			    seller.setMemberId( member.getId() );
			    seller.setBusUserId( userid );
			    seller.setApplyTime( new Date() );
			    seller.setAddTime( new Date() );
			    seller.setCheckStatus( 1 );
			    int count = mallSellerService.insertSelective( seller, member );//添加超级销售员
			    if ( count > 0 ) {
				isIndex = true;
			    }
			}

		    }
		} else {//没有开启销售员的页面
		    isError = true;
		    errorMsg = "商家还没开启销售员的功能，敬请期待";
		}
	    } else {//没有开启销售员的页面
		isError = true;
		errorMsg = "商家还没开启销售员的功能，敬请期待";
	    }
	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( wx ) && CommonUtil.isNotEmpty( member ) ) {
		//todo CommonUtil.getWxParams
		//CommonUtil.getWxParams( mallOrderService.getWpUser(CommonUtil.toInteger(member.getId())),request);
	    }
	    if ( isError ) {
		request.setAttribute( "errorMsg", errorMsg );
		int shopId = 0;
		if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {//获取shopId
		    shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
		}
		List< Map< String,Object > > list1 = mallPageService.shoppage( shopId );
		if ( list1.size() > 0 ) {
		    Map< String,Object > map1 = list1.get( 0 );
		    request.setAttribute( "pageId", map1.get( "id" ).toString() );
		}
		return "mall/seller/phone/applyTip";
	    }
	    if ( isIndex ) {
		String jsp = "redirect:/phoneSellers/79B4DE7C/sellerIndex.do?member_id=" + member.getId() + "&uId=" + member.getBusid();
		return jsp;
	    }

	    //查询申请超级销售员必填选填项
	    MallSellerSet sellerSet = mallSellerSetDAO.selectByBusUserId( member.getBusid() );

	    request.setAttribute( "memberId", member.getId() );
	    request.setAttribute( "sellerSet", sellerSet );
	    request.setAttribute( "userid", userid );
	} catch ( Exception e ) {
	    logger.error( "进入申请超级销售员失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/applySellers";
    }

    /**
     * 进入超级销售员首页
     */
    @RequestMapping( value = "79B4DE7C/sellerIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String sellerIndex( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入超级销售员首页页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    WxPublicUsers wx = null;
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
		userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
		wx = wxPublicUserService.selectByUserId( userid );
	    }
	    if ( userid == 0 && CommonUtil.isNotEmpty( request.getParameter( "member_id" ) ) ) {
		int memberid = CommonUtil.toInteger( request.getParameter( "member_id" ) );
		Member members = memberService.findMemberById( memberid, member );
		userid = members.getBusid();
		wx = wxPublicUserService.selectByMemberId( members.getId() );
	    }
	    request.setAttribute( "userid", userid );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    member = memberService.findMemberById( member.getId(), member );
	    //授权结束

	    boolean isSeller = mallSellerService.isSeller( member.getId() );//判断商户是否是销售员
	    if ( !isSeller ) {
		return "mall/product/phone/shopdelect";
	    }

	    //判断用户是否已经对商城进行设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		request.setAttribute( "mallSet", mallSet );
	    }

	    params.put( "refereesMemberId", member.getId() );

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    params.put( "isCheck", isCheck );

	    int sellerCount = mallSellerService.selectCountMyClient( params );//查询客户的个数
	    int sellerOrderCount = mallSellerService.selectCountClientOrder( params );//查询客户订单的个数

	    //查询销售员的信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.getSellerTwoCode( seller, member, CommonUtil.judgeBrowser( request ) );//获取二维码
	    seller = mallSellerService.mergeData( seller, member );
	    request.setAttribute( "seller", seller );

	    request.setAttribute( "sellerCount", sellerCount );//查询客户的个数
	    request.setAttribute( "sellerOrderCount", sellerOrderCount );//查询客户订单的个数

	    request.setAttribute( "member", member );
	    mallPageService.getCustomer( request, userid );
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( wx ) && CommonUtil.isNotEmpty( member ) ) {
	    	//todo CommonUtil.getWxParams
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入超级销售员首页失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/sellerIndex";
    }

    /**
     * 进入统计收益的页面
     *
     * @param type 1 统计销售佣金    2统计销售积分  3统计销售额
     */
    @RequestMapping( value = "{type}/79B4DE7C/totalIncome" )
    public String totalIncome( HttpServletRequest request, HttpServletResponse response, @PathVariable int type, @RequestParam Map< String,Object > param ) {
	logger.info( "进入统计收益页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( param.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "type", type );
	    params = mallOrderService.getMemberParams( member, params );
	    if ( params.containsKey( "memberId" ) ) {
		params.put( "saleMemeberId", params.get( "memberId" ) );
	    }
	    //查询佣金，积分和销售金额排行榜
	    List< Map< String,Object > > incomeList = mallSellerService.selectTotalIncome( params );

	    request.setAttribute( "incomeList", incomeList );

	    request.setAttribute( "type", type );
	    mallPageService.getCustomer( request, 0 );
	    request.setAttribute( "backUrl", request.getHeader( "Referer" ) );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入统计收益页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/totalIncome";
    }

    /**
     * 进入我的二维码的页面
     */
    @RequestMapping( value = "79B4DE7C/myTwoCode" )
    public String myTwoCode( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的二维码页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.getSellerTwoCode( seller, member, CommonUtil.judgeBrowser( request ) );//获取二维码
	    request.setAttribute( "seller", seller );//销售员信息

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    request.setAttribute( "mallSet", mallSet );
	    request.setAttribute( "member", member );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "url", PropertiesUtil.getHomeUrl() );
	    mallPageService.getCustomer( request, 0 );
	    // todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入我的二维码页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/myTwoCode";
    }

    /**
     * 进入推广页面
     */
    @RequestMapping( value = "79B4DE7C/promotion" )
    public String promotion( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入推广页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    request.setAttribute( "seller", seller );//销售员信息

	    //查询销售员信息
	    Map< String,Object > sellerMap = mallSellerService.selectSellerBySaleId( member.getId() );
	    request.setAttribute( "sellerMap", sellerMap );//销售员信息

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    request.setAttribute( "mallSet", mallSet );
	    request.setAttribute( "member", member );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "url", PropertiesUtil.getHomeUrl() );

	    mallPageService.getCustomer( request, 0 );

	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/

	    String imageUrl = mallSellerService.createTempImage( member, seller, CommonUtil.judgeBrowser( request ) );
	    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		imageUrl = PropertiesUtil.getResourceUrl() + imageUrl;
		imageUrl = URLConnectionDownloader.isConnect( imageUrl );
		if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		    request.setAttribute( "imageUrl", imageUrl );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "进入推广页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/promotion";
    }

    /**
     * 进入销售排行榜的页面
     */
    @RequestMapping( value = "{type}/79B4DE7C/saleRank" )
    public String saleRank( HttpServletRequest request, HttpServletResponse response, @PathVariable int type, @RequestParam Map< String,Object > params ) {
	logger.info( "进入销售排行榜页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

	    if ( CommonUtil.isNotEmpty( seller ) && CommonUtil.isNotEmpty( member ) ) {
		Map< String,Object > param = new HashMap< String,Object >();
		param.put( "busUserId", seller.getBusUserId() );
		param.put( "type", type );
		param.put( "url", "/phoneSellers" + type + "/79B4DE7C/saleRank.do" );

		MallPaySet set = new MallPaySet();
		set.setUserId( member.getBusid() );
		MallPaySet payset = mallPaySetService.selectByUserId( set );
		int isCheck = -1;
		if ( CommonUtil.isNotEmpty( payset ) ) {
		    if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
			if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			    isCheck = 1;
			}
		    }
		}
		if ( isCheck == -1 ) {
		    param.put( "fromType", 2 );
		}

		//通过商家id查询销售员排名
		Map< String,Object > resultMap = mallSellerService.selectSellerByBusUserId( param, 1, member );
		if ( CommonUtil.isNotEmpty( resultMap.get( "page" ) ) ) {
		    request.setAttribute( "page", resultMap.get( "page" ) );
		}
		if ( CommonUtil.isNotEmpty( resultMap.get( "rank" ) ) ) {
		    request.setAttribute( "myRank", resultMap.get( "rank" ) );//我的排名
		}

	    }
	    request.setAttribute( "seller", seller );//销售员信息

	    request.setAttribute( "member", member );
	    request.setAttribute( "type", type );
	    mallPageService.getCustomer( request, 0 );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入销售排行榜页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/saleRank";
    }

    /**
     * 加载销售排行榜信息
     */
    @RequestMapping( value = "{type}/79B4DE7C/loadSaleRank" )
    public void loadSaleRank( HttpServletRequest request, HttpServletResponse response, @PathVariable int type, @RequestParam Map< String,Object > params ) {
	logger.info( "进入加载销售排行榜页面" );
	PrintWriter pw = null;
	Map< String,Object > maps = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    Member member = SessionUtils.getLoginMember( request );

	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

	    if ( CommonUtil.isNotEmpty( seller ) && CommonUtil.isNotEmpty( member ) ) {
		params.put( "busUserId", seller.getBusUserId() );
		params.put( "type", type );
		params.put( "url", "/phoneSellers" + type + "/79B4DE7C/saleRank.do" );

		MallPaySet set = new MallPaySet();
		set.setUserId( member.getBusid() );
		MallPaySet payset = mallPaySetService.selectByUserId( set );
		int isCheck = -1;
		if ( CommonUtil.isNotEmpty( payset ) ) {
		    if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
			if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			    isCheck = 1;
			}
		    }
		}
		if ( isCheck == -1 ) {
		    params.put( "fromType", 2 );
		}

		//通过商家id查询销售员排名
		Map< String,Object > resultMap = mallSellerService.selectSellerByBusUserId( params, 1, member );
		if ( CommonUtil.isNotEmpty( resultMap.get( "page" ) ) ) {
		    maps.put( "data", resultMap.get( "page" ) );
		}

	    }
	    maps.put( "flag", true );
	} catch ( Exception e ) {
	    maps.put( "flag", false );
	    logger.error( "进入加载销售排行榜页面失败：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    pw.write( JSONObject.fromObject( maps ).toString() );
	    pw.flush();
	    pw.close();
	}

    }

    /**
     * 进入我的客户的页面
     */
    @RequestMapping( value = "79B4DE7C/clientList" )
    public String clientList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的客户页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    member = memberService.findMemberById( member.getId(), member );

	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

	    //查询我的客户列表
	    Map< String,Object > param = new HashMap< String,Object >();
	    param.put( "url", "/phoneSellers/79B4DE7C/clientList.do" );

	    param.put( "fromType", 1 );

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    param.put( "isCheck", isCheck );
	    param = mallOrderService.getMemberParams( member, param );
	    if ( params.containsKey( "memberId" ) ) {
		params.put( "refereesMemberId", params.get( "memberId" ) );
	    }
	    //通过商家id查询销售员排名
	    Map< String,Object > resultMap = mallSellerService.selectSellerByBusUserId( param, 2, member );
	    if ( CommonUtil.isNotEmpty( resultMap.get( "page" ) ) ) {
		request.setAttribute( "page", resultMap.get( "page" ) );
	    }

	    request.setAttribute( "seller", seller );//销售员信息
	    request.setAttribute( "member", member );
	    mallPageService.getCustomer( request, 0 );
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
	    	//todo CommonUtil.getWxParams
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入我的客户页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/clientList";
    }

    /**
     * 进入客户订单的页面
     */
    @RequestMapping( value = "79B4DE7C/clientOrder" )
    public String clientOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入客户订单页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    int customerId = 0;//客户id
	    if ( CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "custId" ) ) ) {
		    customerId = CommonUtil.toInteger( params.get( "custId" ) );
		}
	    }

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    params.put( "isCheck", isCheck );
	    params.put( "fromType", 1 );
	    params = mallOrderService.getMemberParams( member, params );
	    //查询客户订单
	    List< Map< String,Object > > sellerOrderList = mallSellerOrderService.selectOrderByClientId( params );
	    request.setAttribute( "sellerOrderList", sellerOrderList );

	    if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
		request.setAttribute( "status", params.get( "status" ) );
	    }
	    request.setAttribute( "custId", customerId );
	    mallPageService.getCustomer( request, 0 );
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
	    	// todo CommonUtil.getWxParams
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入客户订单页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/clientOrder";
    }

    /**
     * 进入我的提现的页面
     */
    @RequestMapping( value = "79B4DE7C/myWithdrawal" )
    public String myWithdrawal( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的提现页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.mergeData( seller, member );
	    request.setAttribute( "seller", seller );

	    //查询提现条件
	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( seller.getBusUserId() );
	    if ( CommonUtil.isNotEmpty( sellerSet ) ) {
		request.setAttribute( "sellerSet", sellerSet );
	    }

	    //查询销售员的提现记录
	    List< MallSellerWithdraw > withdrawList = mallSellerWithdrawService.selectBySaleMemberId( member );
	    request.setAttribute( "withdrawList", withdrawList );

	    mallPageService.getCustomer( request, 0 );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入我的提现页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/myWithdrawal";
    }

    /**
     * 我要提现
     */
    @SysLogAnnotation( description = "销售员——我要提现", op_function = "2" )
    @RequestMapping( value = "79B4DE7C/editWithdrawal" )
    public void editWithdrawal( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我要提现Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    Member member = SessionUtils.getLoginMember( request );

	    resultMap = mallSellerWithdrawService.saveWithdraw( member.getId(), params, 0 );

	} catch ( Exception e ) {
	    logger.error( "我要提现失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "我要提现失败，请稍后重试" );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 进入提现明细页面
     */
    @RequestMapping( value = "79B4DE7C/withdrawalDetail" )
    public String withdrawalDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入提现明细页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "getType" ) ) ) {
		int getType = CommonUtil.toInteger( params.get( "getType" ) );
		params.put( "types", getType );
		request.setAttribute( "getType", getType );
		params.put( "getType", null );
	    }

	    params.put( "type", 1 );
	    params = mallOrderService.getMemberParams( member, params );
	    if ( params.containsKey( "memberId" ) ) {
		params.put( "saleMemeberId", params.get( "memberId" ) );
	    }
	    //查询用户的提现明细
	    List< Map< String,Object > > incomeList = mallSellerService.selectTotalIncome( params );
	    request.setAttribute( "incomeList", incomeList );

	    mallPageService.getCustomer( request, 0 );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入提现明细页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/withdrawalDetail";
    }

    /**
     * 进入销售规则页面
     */
    @RequestMapping( value = "79B4DE7C/saleRules" )
    public String saleRules( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入销售规则页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    member = memberService.findMemberById( member.getId(), member );

	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    request.setAttribute( "seller", seller );

	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( seller.getBusUserId() );
	    request.setAttribute( "sellerSet", sellerSet );

	    request.setAttribute( "member", member );
	    mallPageService.getCustomer( request, 0 );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入销售规则页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/saleRules";
    }

    /**
     * 进入商城设置页面
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( value = "79B4DE7C/toMallSet" )
    public String toMallSet( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入商城设置页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
			/*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
				boolean isLogin = mallPageService.isLogin(member, userid, request);
				if(!isLogin){
					return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
				}
			}*/
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    int type = 1;
	    if ( CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    if ( params.get( "type" ).toString().equals( "2" ) ) {
			type = 2;
		    }
		}
	    }

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( type == 2 && CommonUtil.isEmpty( mallSet ) ) {
		return "redirect:/phoneSellers/79B4DE7C/toMallSet.do";
	    } else if ( type == 2 && CommonUtil.isNotEmpty( mallSet ) ) {
		params.put( "mallSetId", mallSet.getId() );
		params.put( "saleMemberId", mallSet.getSaleMemberId() );
		//查询销售员已选择的商品
		List< Map< String,Object > > sellerProList = mallSellerMallSetService.selectProductBySaleMember( params );
		request.setAttribute( "sellerProList", sellerProList );

		if ( CommonUtil.isNotEmpty( params.get( "findIds" ) ) ) {
		    String findIds = params.get( "findIds" ).toString();
		    request.setAttribute( "findIds", findIds );
		    String[] spli = findIds.split( "," );
		    request.setAttribute( "findIdArray", spli );
		    params.put( "findIds", spli );
		    //查询销售员当前选择的商品
		    List< Map< String,Object > > selectProList = mallSellerMallSetService.selectProductByBusUserId( params );
		    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		    if ( sellerProList != null && sellerProList.size() > 0 && selectProList != null && selectProList.size() > 0 ) {
			for ( Map< String,Object > selectMap : selectProList ) {
			    boolean isSave = true;
			    for ( Map< String,Object > sellerMap : sellerProList ) {
				if ( selectMap.get( "id" ).toString().equals( sellerMap.get( "id" ).toString() ) ) {
				    isSave = false;
				}
			    }
			    if ( isSave ) {
				proList.add( selectMap );
			    }
			}
			selectProList = proList;
		    }
		    request.setAttribute( "selectProList", selectProList );
		}
	    }
	    request.setAttribute( "userid", member.getBusid() );
	    request.setAttribute( "mallSet", mallSet );
	    request.setAttribute( "type", type );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    String backUrl = request.getHeader( "Referer" );
	    String key = "mall:mallSetBackUrl";
	    String backValue = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( backUrl ) ) {
		if ( backUrl.indexOf( "toMallSet.do" ) > 0 && backUrl.indexOf( "findProduct.do" ) > 0 ) {
		    JedisUtil.set( key, backUrl, 30 * 60 );
		}
	    }
	    if ( CommonUtil.isNotEmpty( backValue ) ) {
		backUrl = backValue;
	    } else {
		backUrl = "/phoneSellers/79B4DE7C/sellerIndex.do?uId=" + member.getBusid();
	    }
	    request.setAttribute( "backUrl", backUrl );
	    mallPageService.getCustomer( request, 0 );
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入商城设置失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/mallSet";
    }

    /**
     * 进入自选商品页面
     *
     * @param request
     *
     * @return
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( value = "79B4DE7C/findProduct" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String findProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入自选商品的页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( userid );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    userid = seller.getBusUserId();
	    if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
		String proName = new String( params.get( "proName" ).toString().getBytes( "iso8859-1" ), "utf-8" );
		params.put( "proName", proName );
		request.setAttribute( "proName", proName );
	    }

	    double discount = mallProductService.getMemberDiscount( "1", member );//商品折扣
	    request.setAttribute( "discount", discount );

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    boolean isPifa = mallPifaApplyService.isPifa( member );
	    params.put( "isFindSeller", 1 );
	    //查询已设置佣金的商品
	    PageUtil page = mallSellerMallSetService.selectProductBySaleMember( mallSet, params, "1", 0, discount, isPifa );
	    request.setAttribute( "page", page );
	    //获取店铺所有的分类
	    List classList = mallPageService.storeList( null, 1, seller.getBusUserId() );
	    request.setAttribute( "classList", classList );
	    /*params = mallSellerMallSetService.selectProductBySaleMember(mallSet, params);

	    if(CommonUtil.isNotEmpty(params)){
		    if(CommonUtil.isNotEmpty(params.get("sellerFindIds"))){
			    //查询商品信息
			    List<Map<String, Object>> productList = mallPageService.productAllList(null,"1",0,member,discount,params,isPifa);
			    request.setAttribute("productList", productList);
			    Page page = mallPageService.productAllList(null,"1",0,member,discount,params,isPifa);
			    request.setAttribute("page", page);

			    //获取店铺所有的分类
			    List classList  = mallPageService.storeList(null,1,seller.getBusUserId());
			    request.setAttribute("classList", classList);
		    }
	    }*/
	    if ( CommonUtil.isNotEmpty( params.get( "findIds" ) ) ) {
		request.setAttribute( "findIds", params.get( "findIds" ).toString().split( "," ) );
	    }

	    mallPageService.getSearchLabel( request, 0, member, params );

	    if ( CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "groupId" ) ) ) {
		    request.setAttribute( "groupId", params.get( "groupId" ) );
		}
	    }
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "backUrl", request.getHeader( "Referer" ) );
	    request.setAttribute( "member", member );
	    mallPageService.getCustomer( request, 0 );
	    // CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入自选商品的页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/findProduct";
    }

    /**
     * 保存商城设置
     */
    @SysLogAnnotation( description = "销售员——保存商城设置", op_function = "2" )
    @RequestMapping( value = "79B4DE7C/addMallSet" )
    public void addMallSet( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入保存商城设置Controller" );
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    Member member = SessionUtils.getLoginMember( request );

	    resultMap = mallSellerMallSetService.saveOrUpdateSeller( member, params );

	} catch ( Exception e ) {
	    logger.error( "保存商城设置失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "保存商城设置失败，请稍后重试" );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 删除已添加商品
     */
    @SysLogAnnotation( description = "销售员——删除已添加商品", op_function = "2" )
    @RequestMapping( value = "79B4DE7C/deleteMallPro" )
    public void deleteMallPro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入删除已添加商品Controller" );
	Map< String,Object > resultMap = new HashMap<>();
	try {

	    resultMap = mallSellerMallSetService.deleteSellerProduct( params );

	} catch ( Exception e ) {
	    logger.error( "删除已添加商品失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "删除已添加商品失败，请稍后重试" );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, resultMap );
	}
    }

    /**
     * 进入我的商城页面
     */
    @RequestMapping( value = "{saleMemberId}/79B4DE7C/mallIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String mallIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params ) {
	logger.info( "进入我的商城页面" );
	try {
	    //查询商城设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
	    request.setAttribute( "mallSet", mallSet );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    if ( userid == 0 && CommonUtil.isNotEmpty( mallSet ) ) {
		userid = mallSet.getBusUserId();
	    }
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > publicMap = mallPageService.publicMapByUserId( saleMemberId );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "share" ) ) || saleMemberId > 0 ) {//分享出来的链接，保存销售员在缓存
		mallSellerService.setSaleMemberIdByRedis( member, saleMemberId, request, userid );
	    }

	    saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, saleMemberId, request, userid );

	    //查询销售员信息
	    MallSeller mallSeller = mallSellerService.selectSellerByMemberId( saleMemberId );
	    if ( CommonUtil.isNotEmpty( mallSeller ) ) {
		request.setAttribute( "mallSeller", mallSeller );
	    }

	    if ( saleMemberId > 0 && CommonUtil.isNotEmpty( member ) ) {//分享的用户 判断是否是销售员
		mallSellerService.shareSellerIsSale( member, saleMemberId, mallSeller );
	    }

	    boolean isSeller = mallSellerService.isSeller( saleMemberId );
	    if ( !isSeller ) {
		return "mall/product/phone/shopdelect";
	    }

	    //查询销售员选择的商品
	    List< Map< String,Object > > productList = mallSellerMallSetService.selectProductByMallIndex( member, params, mallSet );
	    request.setAttribute( "productList", productList );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "saleMemberId", saleMemberId );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    request.setAttribute( "url", PropertiesUtil.getHomeUrl() );
	    mallPageService.getCustomer( request, userid );
	    //mallProductService.clearJifenByRedis(member, request,userid);
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( member.getId() ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入我的商城失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/mallIndex";
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @RequestMapping( "{saleMemberId}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @RequestParam Map< String,Object > params )
		    throws Exception {
	try {
	    //查询商城设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
	    request.setAttribute( "mallSet", mallSet );
			/*Map<String, Object> publicMap = mallPageService.memberMap(saleMemberId);*/
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = mallSet.getBusUserId();
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, mallSet.getBusUserId(), request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //授权结束
	    if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
		String proName = new String( params.get( "proName" ).toString().getBytes( "iso8859-1" ), "utf-8" );
		params.put( "proName", proName );
	    }

	    saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, saleMemberId, request, userid );
	    request.setAttribute( "saleMemberId", saleMemberId );

	    boolean isSeller = mallSellerService.isSeller( saleMemberId );//判断商户是否是销售员
	    if ( !isSeller ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();//图片url链接前缀
	    List groLs = mallPageService.storeList( 0, 1, mallSet.getBusUserId() );//获取分类
	    String type = "1";
	    //获取店铺下的商品
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = CommonUtil.toString( params.get( "type" ) );
	    }
	    String desc = "1";
	    //获取店铺下的商品
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = CommonUtil.toString( params.get( "desc" ) );
	    }
	    //条件类型
	    int rType = 0;//0 普通商品 1积分商品 2粉币商品
	    if ( CommonUtil.isNotEmpty( params.get( "rType" ) ) ) {
		rType = CommonUtil.toInteger( params.get( "rType" ).toString() );
	    }
	    params.put( "type", type );
	    params.put( "desc", desc );
	    params.put( "rType", rType );

	    double discount = 1;//商品折扣
	    if ( CommonUtil.isNotEmpty( member ) ) {
		discount = mallProductService.getMemberDiscount( "1", member );
	    }
	    request.setAttribute( "discount", discount );

	    boolean isPifa = mallPifaApplyService.isPifa( member );

	    PageUtil page = mallSellerMallSetService.selectProductBySaleMember( mallSet, params, type, rType, discount, isPifa );

	    mallPageService.getSearchLabel( request, 0, member, params );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );//主店铺id
	    }
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "page", page );
	    request.setAttribute( "groLs", groLs );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "http", http );
	    request.setAttribute( "rType", rType );
	    request.setAttribute( "member", member );
	    request.setAttribute( "isSeller", 1 );

	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    mallPageService.getCustomer( request, mallSet.getBusUserId() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "商品搜索页面异常：" + e.getMessage() );
	}
	return "mall/product/phone/shoppingall";
    }

    /**
     * 进入分享页面
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( value = "{saleMemberId}/{productId}/79B4DE7C/shareSeller" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shareSeller( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId, @PathVariable int productId,
		    @RequestParam Map< String,Object > params ) {
	logger.info( "进入分享页面" );
	try {
	    String share = "";
	    if ( CommonUtil.isNotEmpty( params.get( "share" ) ) ) {
		share = CommonUtil.toString( params.get( "share" ) );
	    }
	    MallProduct product = mallProductService.selectByPrimaryKey( productId );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
	    } else {
		userid = product.getUserId();
	    }
	    if ( userid > 0 ) {
		request.setAttribute( "userid", userid );
	    }
	    //查询商城设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
	    request.setAttribute( "mallSet", mallSet );
	    WxPublicUsers wp = wxPublicUserService.selectByUserId( mallSet.getBusUserId() );
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //授权结束
	    if ( CommonUtil.isNotEmpty( share ) ) {//分享出来的链接，保存销售员在缓存
		mallSellerService.setSaleMemberIdByRedis( member, saleMemberId, request, userid );
	    }

	    saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, saleMemberId, request, userid );

	    //查询销售员信息
	    MallSeller mallSeller = mallSellerService.selectSellerByMemberId( saleMemberId );
	    Member sellerMember = memberService.findMemberById( mallSeller.getMemberId(), member );//查询销售员的用户信息
	    request.setAttribute( "sellerMember", sellerMember );

	    if ( CommonUtil.isNotEmpty( share ) && saleMemberId > 0 && CommonUtil.isNotEmpty( mallSeller ) ) {//分享的用户 判断是否是销售员
		mallSellerService.shareSellerIsSale( member, saleMemberId, mallSeller );
	    }

	    boolean isSeller = mallSellerService.isSeller( saleMemberId );
	    if ( !isSeller ) {
		return "mall/product/phone/shopdelect";
	    }

	    //查询商品信息
	    Map< String,Object > proMap = mallSellerMallSetService.selectSellerByProId( productId, mallSet );
	    if ( CommonUtil.isNotEmpty( proMap ) ) {
		double price = CommonUtil.toDouble( proMap.get( "pro_price" ) );
		if ( CommonUtil.isNotEmpty( proMap.get( "inv_price" ) ) ) {
		    double invPrice = CommonUtil.toDouble( proMap.get( "inv_price" ) );
		    if ( invPrice > 0 ) {
			price = invPrice;
		    }
		}
		proMap.put( "price", price );
		proMap = mallSellerMallSetService.getSellerProductPrice( proMap );
	    }
	    request.setAttribute( "productMap", proMap );

	    request.setAttribute( "mallSeller", mallSeller );
	    request.setAttribute( "member", member );
	    request.setAttribute( "saleMemberId", saleMemberId );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    mallPageService.getCustomer( request, mallSet.getBusUserId() );
	    //todo CommonUtil.getWxParams
	    /*if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( wp ) && CommonUtil.isNotEmpty( member ) ) {
		CommonUtil.getWxParams( mallOrderService.getWpUser( CommonUtil.toInteger( member.getId() ) ), request );
	    }*/
	} catch ( Exception e ) {
	    logger.error( "进入分享页面失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/phone/shareSeller";
    }

    /**
     * 发送短信
     *
     * @param sType 验证类型: 0 短信验证, 1 语音验证
     * @param telNo 手机号码
     */
    @RequestMapping( value = "/79B4DE7C/sendMsg" )
    @ResponseBody
    public Map< String,Object > sendMsg( @RequestParam String telNo,
		    @RequestParam String sType, @RequestParam String mType, HttpServletResponse response,
		    HttpServletRequest request, HttpSession session ) {
	if ( logger.isDebugEnabled() ) {
	    // 验证类型
	    logger.error( "进入短信发送,手机号:" + telNo + "验证类型:" + sType );
	}

	Map< String,Object > map = null;

	Member member = SessionUtils.getLoginMember( request );

	String no = CommonUtil.getPhoneCode();
	JedisUtil.set( "mall:" + no, no, 5 * 60 );

	try {

	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( telNo );
	    oldApiSms.setBusId( member.getBusid() );
	    oldApiSms.setContent( "您申请成为超级销售员的验证码为:" + no + "，5分钟内有效。" );

	    boolean flag = smsService.sendSmsOld( oldApiSms );
	    if ( flag ) {
		map.put( "code", ResponseEnums.SUCCESS );
		map.put( "msg", "发送成功" );
	    }
	} catch ( Exception e ) {
	    map = new HashMap< String,Object >();
	    map.put( "msg", "获取短信验证码失败" );
	}
	return map;
    }

    /**
     * 检验验证码
     */
    @RequestMapping( value = "/79B4DE7C/checkCode" )
    @ResponseBody
    public void checkCode( HttpServletRequest request, HttpServletResponse response, @RequestParam String applyCode ) {
	Map< String,Object > map = new HashMap< String,Object >();
	PrintWriter out = null;
	try {
	    out = response.getWriter();
	    if ( CommonUtil.isEmpty( applyCode ) ) {
		map.put( "result", 0 );
		map.put( "message", "请输入验证码" );
	    } else {
		String code = JedisUtil.get( "mall:" + applyCode );
		if ( CommonUtil.isEmpty( code ) ) {
		    map.put( "result", 0 );
		    map.put( "message", "验证码错误或超时" );
		} else if ( code.equals( applyCode ) ) {
		    map.put( "result", 1 );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "验证验证码异常：" + e.getMessage() );
	} finally {
	    out.write( JSONObject.fromObject( map ).toString() );
	    out.flush();
	    out.close();
	}

    }

    /**
     * 添加批发商
     */
    @RequestMapping( value = "/79B4DE7C/addSellers" )
    public void addSellers( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > map ) {
	PrintWriter pw = null;
	Map< String,Object > maps = new HashMap< String,Object >();
	try {
	    String msg = "";
	    boolean result = false;
	    pw = response.getWriter();
	    int count = 0;
	    MallSeller seller = (MallSeller) JSONObject.toBean( JSONObject.fromObject( map.get( "obj" ) ), MallSeller.class );
	    Member member = SessionUtils.getLoginMember( request );
	    seller.setMemberId( member.getId() );
	    seller.setBusUserId( member.getBusid() );
	    seller.setApplyTime( new Date() );
	    seller.setAddTime( new Date() );

	    //查询是否已申请超级销售员
	    MallSeller isMallSeller = mallSellerService.selectMallSeller( seller );
	    if ( CommonUtil.isEmpty( isMallSeller ) ) {
		count = mallSellerService.insertSelective( seller, member );//添加超级销售员
	    } else {
		if ( !isMallSeller.getCheckStatus().toString().equals( "0" ) ) {
		    seller.setId( isMallSeller.getId() );
		    seller.setCheckStatus( 0 );
		    count = mallSellerService.updateSeller( seller );
		} else {
		    msg = "您申请的超级销售员正在审核中，无需再次提交申请";
		}
	    }
	    if ( count > 0 ) {
		result = true;
		JSONObject obj = JSONObject.fromObject( map.get( "obj" ) );
		JedisUtil.del( obj.get( "code" ).toString() );//申请超级销售员成功，删除验证码
	    }
	    maps.put( "msg", msg );
	    maps.put( "result", result );
	} catch ( Exception e ) {
	    maps.put( "result", false );
	    maps.put( "msg", "超级销售员申请异常，请稍后再申请" );
	    e.printStackTrace();
	} finally {
	    pw.write( JSONObject.fromObject( maps ).toString() );
	    pw.flush();
	    pw.close();
	}
    }
}
