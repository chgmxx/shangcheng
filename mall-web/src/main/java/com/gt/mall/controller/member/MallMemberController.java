package com.gt.mall.controller.member;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.constant.Constants;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.inter.service.MemberService;
import com.gt.mall.util.*;
import com.gt.mall.web.service.basic.MallCollectService;
import com.gt.mall.web.service.basic.MallCommentService;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.order.MallOrderService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.pifa.MallPifaApplyService;
import com.gt.mall.web.service.seller.MallSellerService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城的个人中心 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mMember" )
public class MallMemberController extends AuthorizeOrLoginController {

    private Logger logger = Logger.getLogger( MallMemberController.class );

    @Autowired
    private MallPageService      pageService;
    @Autowired
    private MallOrderService     morderService;
    @Autowired
    private MallCommentService   commentService;
    @Autowired
    private MallCollectService   collectService;
    @Autowired
    private MallPifaApplyService pifaApplayService;
    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private MallSellerService    mallSellerService;
    @Autowired
    private MemberService memberService;

    /**
     * 跳转至个人中心的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( value = "/79B4DE7C/toUser" )
    public String toUser( HttpServletRequest request, HttpServletResponse response ) {
	logger.info( "进入个人中心页面页面" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    WxPublicUsers wx = null;
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
		userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
		request.setAttribute( "userid", userid );
	    } else if ( CommonUtil.isNotEmpty( request.getParameter( "member_id" ) ) ) {
		request.setAttribute( "userid", member.getBusid() );
	    }
	    //TODO wxPublicUsersMapper.selectByUserId( userid );
	    //	    wx = wxPublicUsersMapper.selectByUserId( userid );
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //TODO 用户 busUserMapper.selectByPrimaryKey( userid );
	    BusUser user = null;
	    //			    busUserMapper.selectByPrimaryKey( userid );
	    if ( CommonUtil.isNotEmpty( user ) ) {
		if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		    if ( user.getAdvert() == 0 ) {
			request.setAttribute( "isAdvert", 1 );
		    }
		}
	    }
	    int shopId = 0;
	    if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {//获取shopId
		shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
		request.setAttribute( "shopid", request.getSession().getAttribute( "shopId" ) );

		List list1 = pageService.shoppage( shopId );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    request.setAttribute( "pageid", map1.get( "id" ).toString() );
		}
	    }
	    if ( shopId == 0 ) {
		List< Map< String,Object > > pageId = morderService.selectPageIdByUserId( userid );
		if ( pageId.size() > 0 ) {//获取首页的pageId
		    shopId = CommonUtil.toInteger( pageId.get( 0 ).get( "pag_sto_id" ) );
		    request.setAttribute( "pageid", pageId.get( 0 ).get( "id" ) );
		}
	    }
	    if ( CommonUtil.isNotEmpty( member ) ) {
		//TODO 会员卡方法 memberPay.findGradeType() findCardByMemberId()
		//		GradeType gradeType = memberPay.findGradeType( member.getId() );//会员卡名称
		//		Card card = memberPay.findCardByMemberId( member.getId() );//会员卡号
		//		request.setAttribute( "card", card );
		//		request.setAttribute( "gradeType", gradeType );
	    }
	    request.setAttribute( "member", member );

	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( set.getIsPf().toString().equals( "1" ) ) {
		    int status = pifaApplayService.getPifaApplay( member, set );
		    request.setAttribute( "status", status );
		    request.setAttribute( "isOpenPf", 1 );
		}
		if ( set.getIsSeller().toString().equals( "1" ) ) {
		    int status = mallSellerService.selectSellerStatusByMemberId( member, set );
		    request.setAttribute( "sellerStatus", status );
		    request.setAttribute( "isOpenSeller", 1 );
		}
	    }
	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( userid );
	    if ( CommonUtil.isNotEmpty( sellerSet ) ) {
		if ( CommonUtil.isNotEmpty( sellerSet.getConsumeMoney() ) ) {
		    double minCosumeMoney = CommonUtil.toDouble( sellerSet.getConsumeMoney() );
		    if ( minCosumeMoney > 0 ) {
			double consumeMoney = 0;
			if ( CommonUtil.isNotEmpty( member ) ) {
			    //判断消费金额
			    Map< String,Object > consumeMap = morderService.selectSumSaleMoney( member.getId() );
			    if ( CommonUtil.isNotEmpty( consumeMap ) ) {
				if ( CommonUtil.isNotEmpty( consumeMap.get( "orderMoney" ) ) ) {
				    consumeMoney = CommonUtil.toDouble( consumeMap.get( "orderMoney" ) );
				}
			    }
			}
			if ( minCosumeMoney > consumeMoney ) {
			    request.setAttribute( "minCosumeMoney", minCosumeMoney );
			    request.setAttribute( "consumeMoney", consumeMoney );
			}
		    }
		}
	    }

	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, userid );
	    if ( saleMemberId > 0 ) {
		request.setAttribute( "saleMemberId", saleMemberId );
	    }

	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( wx ) && CommonUtil.isNotEmpty( member ) ) {
		//TODO CommonUtil.getWxParams()
		//		CommonUtil.getWxParams( morderService.getWpUser( member.getId() ), request );
	    }
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	} catch ( Exception e ) {
	    logger.error( "手机订单页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/member/index";
    }

    /**
     * 去评价页面
     *
     * @param request
     * @param params
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/orderAppraise" )
    public String orderAppraise( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入去评论页面的controller" );
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

	    Object odId = params.get( "orDetailId" );
	    if ( null != odId && !odId.equals( "" ) ) {
		MallOrderDetail orderDetail = morderService.selectOrderDetailById( Integer.parseInt( odId.toString() ) );
		request.setAttribute( "orderDetail", orderDetail );
	    }
	    request.setAttribute( "imgHttp", PropertiesUtil.getResourceUrl() );

	    BusUser user = pageService.selUserByMember( member );
	    if ( CommonUtil.isNotEmpty( user ) ) {
		if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		    if ( user.getAdvert() == 0 ) {
			request.setAttribute( "isAdvert", 1 );
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "进入评论页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/phone/orderAppraise";
    }

    /**
     * 添加评价
     *
     * @param request
     * @param map
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/addAppraise" )
    @SysLogAnnotation( description = "添加评价", op_function = "2" )
    public void addAppraise( HttpServletRequest request, @RequestParam Map< String,Object > map, HttpServletResponse response ) {
	logger.info( "进入添加评论controller" );
	PrintWriter out = null;
	int count = 0;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();
	    int memberId = SessionUtils.getLoginMember( request ).getId();
			/*int memberId = 200;*/

	    MallComment mallComment = (MallComment) JSONObject.toBean( JSONObject.fromObject( map.get( "obj" ) ), MallComment.class );
	    mallComment.setUserId( memberId );
	    //判断用户是否已经评论了，防止重复评论
	    MallComment isComment = commentService.selectComment( mallComment );
	    if ( CommonUtil.isEmpty( isComment ) && CommonUtil.isEmpty( mallComment.getId() ) ) {
		MallComment comment = commentService.addAppraise( map, mallComment, request );
		if ( CommonUtil.isNotEmpty( comment ) ) {
		    count = 1;
		}
	    } else {
		count = -1;
	    }
	    result.put( "orderDetailId", mallComment.getOrderDetailId() );
	} catch ( Exception e ) {
	    logger.error( "添加评论异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    result.put( "count", count );
	    JSONObject obj = new JSONObject();
	    obj.put( "result", result );
	    out.write( obj.get( "result" ).toString() );
	    out.flush();
	    out.close();
	}
    }

    /**
     * 我的评价
     *
     * @param request
     * @param param
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/myAppraise" )
    public String appraiseList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > param ) {
	logger.info( "进入我的评论controller" );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( param.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    param = morderService.getMemberParams( member, param );
	    PageUtil appraise = commentService.myAppraise( param );
	    request.setAttribute( "page", appraise );
	    request.setAttribute( "type", param.get( "type" ) );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "查询我的评论有误！" + e.getMessage() );
	    e.printStackTrace();
	}
	return "/mall/order/phone/myAppraise";
    }

    /**
     * 我的收藏
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( "/79B4DE7C/collect" )
    public String collect( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws Exception {
	logger.info( "进入我的收藏controller" );
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

	    String http = PropertiesUtil.getResourceUrl();//图片url链接前缀

	    double discount = 1;//商品折扣
	    //TODO 折扣 memberpayService.findCardType( member.getId() );
	    Map< String,Object > map = null;
	    //	    memberpayService.findCardType( member.getId() );
	    String result = map.get( "result" ).toString();
	    if ( result == "true" || result.equals( "true" ) ) {
		discount = Double.parseDouble( map.get( "discount" ).toString() );
	    }
	    request.setAttribute( "discount", discount );
	    List< Map< String,Object > > xlist = pageService.getProductCollectByMemberId( member, discount );

	    request.setAttribute( "memberId", member.getId() );//主店铺id
	    request.setAttribute( "xlist", xlist );
	    request.setAttribute( "http", http );
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "我的收藏页面异常：" + e.getMessage() );
	}
	return "mall/member/collectAll";

    }

    @SysLogAnnotation( description = "添加评价-上传图片", op_function = "2" )
    @SuppressWarnings( "unchecked" )
    @RequestMapping( "/79B4DE7C/updateImage" )
    public void updateImage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > parma ) throws IOException {
	logger.info( "进入评论上传图片controller" );
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Integer memberId = SessionUtils.getLoginMember( request ).getId();

	    StringBuffer imageUrl = new StringBuffer();
	    boolean flag = false;
	    Member member = memberService.findMemberById( memberId ,null);
	    //TODO 用户 busUserMapper.selectByPrimaryKey( member.getBusid() );
	    BusUser user = null;
	    //	    nullbusUserMapper.selectByPrimaryKey( member.getBusid() );
	    if ( request instanceof MultipartHttpServletRequest ) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List< MultipartFile > userfile = multipartRequest.getFiles( "file" );
		if ( CommonUtil.isNotEmpty( userfile ) && userfile.size() != 0 ) {
		    for ( int i = 0; i < userfile.size(); i++ ) {
			MultipartFile file = userfile.get( i );
			Map< String,Object > returnMap = CommonUtil.fileUploadByBusUser( file, user.getId() );
			if ( "1".equals( returnMap.get( "reTurn" ) ) ) {
			    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
				imageUrl.append( ";" );
			    }
			    imageUrl.append( returnMap.get( "message" ) );
			    flag = true;

			} else {
			    flag = false;
			    break;
			}
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		map.put( "imagePath", imageUrl );
	    }
	    map.put( "path", PropertiesUtil.getResourceUrl() );
	    map.put( "result", flag );
	} catch ( Exception e ) {
	    logger.error( "商城评论上传图片异常:", e );
	    map.put( "result", false );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 删除收藏
     *
     * @Title: checkComment
     */
    @SysLogAnnotation( description = "我的收藏——删除收藏", op_function = "3" )
    @RequestMapping( "/79B4DE7C/deleteCollect" )
    public void deleteCollect( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws JSONException {
	logger.info( "进入删除收藏的controller" );
	PrintWriter p = null;
	boolean result = false;
	String msg = "";
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    p = response.getWriter();

	    result = collectService.deleteCollect( params );

	} catch ( Exception e ) {
	    result = false;
	    logger.debug( "删除收藏失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	if ( !result ) {
	    map.put( "msg", "删除收藏失败，请稍后重试" );
	}
	map.put( "result", result );
	map.put( "msg", msg );
	p.write( JSONObject.fromObject( map ).toString() );
	p.flush();
	p.close();
    }

    /**
     * 跳转到登陆页面
     *
     * @param request
     * @param params
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/isLogin" )
    public void isLogin( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "判断用户是否已经登陆" );
	PrintWriter out;
	try {
	    out = response.getWriter();
	    Map< String,Object > result = new HashMap< String,Object >();

	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "data" ) ) ) {
		String keys = "mall_isnoLogin";
		if ( CommonUtil.isNotEmpty( params.get( "keys" ) ) ) {
		    keys = CommonUtil.toString( params.get( "keys" ) );
		}
		request.getSession().setAttribute( keys, params.get( "data" ) );//清空缓存
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
	    if ( ( CommonUtil.judgeBrowser( request ) != 1 || CommonUtil.isEmpty( publicMap ) ) ) {
		boolean isLogin = true;
		if ( CommonUtil.isNotEmpty( member ) ) {
		    if ( !member.getBusid().toString().equals( CommonUtil.toString( userid ) ) ) {
			request.getSession().setAttribute( "member", null );//清空缓存
			member = null;
			isLogin = false;
		    }
		} else {
		    isLogin = false;
		}
		if ( !isLogin ) {
		    String redisKey = Constants.REDIS_KEY + CommonUtil.getCode();
		    JedisUtil.set( redisKey, params.get( "urls" ).toString(), 5 * 60 );
		    result.put( "returnUrl", redisKey );
		}
	    }
	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) ) {
		result.put( "isWx", 1 );
	    }
	    out.write( JSONObject.fromObject( result ).toString() );
	    out.flush();
	    out.close();
	} catch ( Exception e ) {
	    logger.error( "判断用户是否已经登陆异常：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 打开腾讯地图
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/tencentMap" )
    public String tencentMap( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	request.setAttribute( "address", params.get( "params" ) );
	request.setAttribute( "uId", params.get( "uId" ) );
	request.setAttribute( "id", params.get( "id" ) );
	if ( CommonUtil.isNotEmpty( params.get( "addressManage" ) ) ) {
	    request.setAttribute( "addressManage", params.get( "addressManage" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "addType" ) ) ) {
	    request.setAttribute( "addType", params.get( "addType" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
	    request.setAttribute( "type", params.get( "type" ) );
	}
	return "mall/order/phone/tencentMap";
    }
}
