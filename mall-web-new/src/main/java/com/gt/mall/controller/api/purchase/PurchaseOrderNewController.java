package com.gt.mall.controller.api.purchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.member.MemberCard;
import com.gt.mall.dao.purchase.*;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.purchase.*;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.purchase.PurchaseCompanyModeService;
import com.gt.mall.service.web.purchase.PurchaseOrderService;
import com.gt.mall.service.web.purchase.PurchaseOrderStatisticsService;
import com.gt.mall.service.web.purchase.PurchaseReceivablesService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对外报价-报价单管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "purchaseOrder", description = "对外报价-报价单管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/purchase/E9lM9uM4ct/order" )
public class PurchaseOrderNewController extends BaseController {

    @Autowired
    PurchaseOrderService       purchaseOrderService;
    @Autowired
    PurchaseCompanyModeService companyModeService;
    @Autowired
    PurchaseReceivablesDAO     receivablesDAO;
    @Autowired
    PurchaseReceivablesService purchaseReceivablesService;
    @Autowired
    PurchaseLanguageDAO        languageDAO;
    @Autowired
    PurchaseCarouselDAO        carouselDAO;
    @Autowired
    PurchaseOrderDetailsDAO    purchaseOrderDetailsDAO;
    @Autowired
    PurchaseTermDAO            termDAO;
    @Autowired
    PurchaseContractOrderDAO   contractOrderDAO;
    @Autowired
    private MallStoreService storeService;
    @Autowired
    PurchaseOrderStatisticsService orderStatisticsService;
    @Autowired
    MemberService                  memberService;

    @ApiOperation( value = "报价单列表(分页)", notes = "报价单列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "状态", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "search", value = "报价单号", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "创建开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "创建结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, String status, String search, String startTime, String endTime ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "status", status );
	    params.put( "search", search );
	    params.put( "startTime", startTime );
	    params.put( "endTime", endTime );
	    params.put( "busId", busUser.getId() );
	    PageUtil page = purchaseOrderService.findList( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "获取报价单列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取报价单列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取报价单信息
     */
    @ApiOperation( value = "获取报价单信息", notes = "获取报价单信息" )
    @ResponseBody
    @RequestMapping( value = "/orderInfo", method = RequestMethod.POST )
    public ServerResponse orderInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "报价单ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //查询订单详情
	    PurchaseOrder order = purchaseOrderService.selectById( id );
	    //查询订单的商品详情
	    List< Map< String,Object > > orderdetails = purchaseOrderDetailsDAO.findOrderDetails( id );
	    if ( order.getOrderType().equals( "1" ) ) {
		//查询分期
		List< Map< String,Object > > termList = termDAO.findTermList( id );
		result.put( "termList", termList );
	    }
	    //订单的公司模板
	    PurchaseCompanyMode company = companyModeService.selectById( order.getCompanyId() );
	    //订单的报价单
	    List< Map< String,Object > > contractListMap = contractOrderDAO.findContractOrderList( id );
	    if ( contractListMap.size() > 0 ) {
//		result.put( "orderContract", contractListMap.get( 0 ) );
		order.setContractId( contractListMap.get( 0 ).get( "contract_id" ).toString() );
	    }
	    //查询轮播图
	    List< Map< String,Object > > carouselList = carouselDAO.findByOrderId( order.getId() );
	    result.put( "carouselList", carouselList );

	    result.put( "company", company );
	    result.put( "order", order );
	    result.put( "orderdetails", orderdetails );
	} catch ( Exception e ) {
	    logger.error( "获取报价单信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取报价单信息异常" );
	} return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "获取所有店铺的商品(分页)", notes = "获取所有店铺的商品(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "proName", value = "商品名称", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/getProductAll", method = RequestMethod.POST )
    public ServerResponse getProductAll( HttpServletRequest request, HttpServletResponse response, Integer curPage, String proName ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "proName", proName );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = purchaseOrderService.productList( params );
		    result.put( "page", page );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取所有店铺的商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取所有店铺的商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存或修改报价单信息
     */
    @ApiOperation( value = "保存报价单信息", notes = "保存报价单信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存报价单信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    PurchaseOrder order = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "order" ).toString() ), PurchaseOrder.class );
	    order.setOrderTitle( CommonUtil.urlEncode( order.getOrderTitle() ) );
	    order.setOrderDescribe( CommonUtil.urlEncode( order.getOrderDescribe() ) );
	    order.setOrderExplain( CommonUtil.urlEncode( order.getOrderExplain() ) );
	    order.setBusId( user.getId() );
	    List< PurchaseOrderDetails > orderDetailsList = JSONArray.parseArray( params.get( "orderDetailsList" ).toString(), PurchaseOrderDetails.class );
	    List< PurchaseTerm > termList = null;
	    if ( CommonUtil.isNotEmpty( params.get( "termList" ) ) ) {
		termList = JSONArray.parseArray( params.get( "termList" ).toString(), PurchaseTerm.class );
	    }
	    List< PurchaseCarousel > carouselList = JSONArray.parseArray( params.get( "carouselList" ).toString(), PurchaseCarousel.class );

	    Map< String,Object > map = purchaseOrderService.saveOrder( order, orderDetailsList, termList, carouselList );
	    String flag = map.get( "result" ).toString();
	    if ( !Boolean.valueOf( flag ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存报价单失败" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存报价单信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存报价单信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 修改报价单状态
     */
    @ApiOperation( value = "修改报价单状态", notes = "修改报价单状态" )
    @ResponseBody
    @SysLogAnnotation( description = "修改报价单状态", op_function = "4" )
    @RequestMapping( value = "/updateStatus", method = RequestMethod.POST )
    public ServerResponse updateStatus( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "orderId", value = "报价单Id", required = true ) @RequestParam Integer orderId,
		    @ApiParam( name = "status", value = "状态", required = true ) @RequestParam Integer status ) {
	try {
	    PurchaseOrder order = new PurchaseOrder();
	    order.setId( orderId );
	    order.setOrderStatus( status.toString() );
	    purchaseOrderService.updateById( order );
	} catch ( Exception e ) {
	    logger.error( "修改报价单状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改报价单状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 查看收款详情
     */
    @ApiOperation( value = "查看收款详情", notes = "查看收款详情" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderId", value = "报价单ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "memberId", value = "用户ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/receivablesDetails", method = RequestMethod.POST )
    public ServerResponse receivablesDetails( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer orderId, Integer memberId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = memberService.findMemberById( memberId, null );//查询用户信息
	    if ( member != null && member.getMcId() != null ) { //如果用户存在会员卡
		MemberCard card = memberService.findMemberCardByMcId( member.getMcId() );// 查询会员卡信息
		result.put( "cardNo", card.getCardNo() );
	    }
	    result.put( "member", member );
	    PurchaseOrder order = purchaseOrderService.selectById( orderId );
	    result.put( "order", order );

	    Map< String,Object > parms = new HashMap<>();
	    parms.put( "curPage", curPage );
	    parms.put( "orderId", orderId );
	    PageUtil page = purchaseReceivablesService.findList( parms );//查询报价单收款记录
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "查看收款详情异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查看收款详情异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取留言列表
     */
    @ApiOperation( value = "获取留言列表", notes = "获取留言列表,并将所有留言设为已读" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderId", value = "报价单ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/languageList", method = RequestMethod.POST )
    public ServerResponse languageList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer orderId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > parms = new HashMap<>();
	    parms.put( "orderId", orderId );
	    int pageSize = 10;
	    int count = 0;
	    List< Map< String,Object > > languageList = new ArrayList< Map< String,Object > >();
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    count = languageDAO.findListCount( parms );
	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    parms.put( "pageFirst", ( page.getCurPage() - 1 ) * 10 );
	    parms.put( "pageLast", 10 );
	    if ( count > 0 ) {
		languageList = languageDAO.findList( parms );
		if ( languageList != null && languageList.size() > 0 ) {
		    String memberIds = "";
		    for ( Map< String,Object > map : languageList ) {
			if ( CommonUtil.isNotEmpty( map.get( "memberId" ) ) ) {
			    memberIds += map.get( "memberId" ) + ",";
			}
		    }
		    if ( CommonUtil.isNotEmpty( memberIds ) ) {
			List< Map > memberList = memberService.findMemberByIds( memberIds, user.getId() );
			if ( memberList != null && memberList.size() > 0 ) {
			    for ( Map< String,Object > rankMap : languageList ) {
				String rMemberId = CommonUtil.toString( rankMap.get( "memberId" ) );
				for ( Map< String,Object > member : memberList ) {
				    String memberId = CommonUtil.toString( member.get( "id" ) );
				    if ( rMemberId.equals( memberId ) ) {
					rankMap.put( "headimgurl", member.get( "headimgurl" ) );
					rankMap.put( "nickname", member.get( "nickname" ) );
					break;
				    }
				}
			    }
			}
		    }
		}
	    }
	    page.setSubList( languageList );
	    result.put( "page", page );

	    //设置订单的留言为已阅状态
	    languageDAO.updateLanguangeByOrderId( orderId );
	} catch ( Exception e ) {
	    logger.error( "获取留言列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取留言列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取用户留言详情列表
     */
    @ApiOperation( value = "获取用户留言详情列表", notes = "获取用户留言详情列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderId", value = "报价单ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "memberId", value = "用户ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/languageDetails", method = RequestMethod.POST )
    public ServerResponse languageDetails( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer orderId, Integer memberId ) {

	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > parms = new HashMap<>();
	    parms.put( "orderId", orderId );
	    parms.put( "memberId", memberId );
	    int pageSize = 10;
	    int count = 0;
	    List< Map< String,Object > > languageList = new ArrayList< Map< String,Object > >();
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    count = languageDAO.findDetailListCount( parms );
	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    parms.put( "pageFirst", ( page.getCurPage() - 1 ) * 10 );
	    parms.put( "pageLast", 10 );
	    if ( count > 0 ) {
		languageList = languageDAO.findDetailList( parms );

	    }
	    page.setSubList( languageList );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "获取用户留言详情列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取用户留言详情列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 商家回复买家的留言
     */
    @ApiOperation( value = "商家回复买家的留言", notes = "商家回复买家的留言" )
    @ResponseBody
    @RequestMapping( value = "/replyLanguage", method = RequestMethod.POST )
    public ServerResponse replyLanguage( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "languageId", value = "用户留言ID", required = true ) @RequestParam Integer languageId,
		    @ApiParam( name = "languageContent", value = "留言", required = true ) @RequestParam String languageContent ) {
	try {
	    PurchaseLanguage language = new PurchaseLanguage();
	    language.setId( languageId );
	    language.setIsRead( "1" );
	    language.setAdminContent( CommonUtil.urlEncode( languageContent ) );
	    languageDAO.updateById( language );
	} catch ( Exception e ) {
	    logger.error( "商家回复买家的留言异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商家回复买家的留言" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    @ApiOperation( value = "获取统计列表(分页)", notes = "获取统计列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "nickname", value = "姓名", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "orderId", value = "报价单ID", paramType = "query", required = true, dataType = "int" ) } )
    @RequestMapping( value = "/statisticsList", method = RequestMethod.POST )
    public ServerResponse statisticsList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String nickname, Integer orderId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser busUser = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "nickname", nickname );
	    params.put( "busId", busUser.getId() );
	    params.put( "orderId", orderId );
	    PageUtil page = orderStatisticsService.findList( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "获取统计列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取统计列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }
}
