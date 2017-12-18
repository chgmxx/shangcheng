package com.gt.mall.controller.api.integral.phone;

import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.integral.PhoneAddIntegralDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.integral.MallIntegralImageService;
import com.gt.mall.service.web.integral.MallIntegralService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Api( value = "phoneIntegral", description = "积分商城页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneIntegral/L6tgXlBFeK/" )
public class PhoneMallIntegralNewController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallPageService          pageService;
    @Autowired
    private MallIntegralService      integralService;
    @Autowired
    private MallIntegralImageService integralImageService;
    @Autowired
    private MallOrderService         orderService;
    @Autowired
    private MemberService            memberService;
    @Autowired
    private MallStoreService         mallStoreService;

    /**
     * 获取积分商城商品列表
     */
    @ApiOperation( value = "获取积分商城商品列表", notes = "获取积分商城商品列表" )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "integralProductList", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > integralProductList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );
	    BusUser user = new BusUser();
	    user.setId( loginDTO.getBusId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist == null || shoplist.size() == 0 ) {
		throw new BusinessException( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shoplist", shoplist );
	    params.put( "curPage", curPage );
	    PageUtil page = integralService.selectIntegralByUserId( params );

	    result.put( "page", page );
	} catch ( BusinessException be ) {
	    return ServerResponse.createByErrorCodeMessage( be.getCode(), be.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "获取积分商城商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分商城商品列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取积分数量,轮播图片
     */
    @ApiOperation( value = "获取积分数量,轮播图片", notes = "获取积分数量,轮播图片" )
    @ResponseBody
    @RequestMapping( value = "integralImage", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > integralImage( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    //查询我的积分
	    if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
		if ( CommonUtil.isNotEmpty( member.getIntegral() ) ) {
		    result.put( "memberIntegral", member.getIntegral() );
		} else {
		    result.put( "memberIntegral", 0 );
		}
		result.put( "memberId", member.getId() );
	    }

	    //查询轮播图片
	    Map< String,Object > params = new HashMap<>();
	    //	    params.put( "shopId", shopId );
	    params.put( "userId", loginDTO.getBusId() );
	    List< MallIntegralImage > imageList = integralImageService.getIntegralImageByUser( params );
	    result.put( "imageList", imageList );

	    //	    MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取积分数量,轮播图片异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分数量,轮播图片异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取兑换记录列表
     */
    @ApiOperation( value = "获取兑换记录列表", notes = "获取兑换记录列表" )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "当前页数", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "recordList", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > recordList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "buyerUserId", member.getId() );
	    params.put( "busUserId", loginDTO.getBusId() );
	    params.put( "curPage", curPage );
	    List< MallOrder > orderList = orderService.selectIntegeralOrder( params );
	    double totalIntegral = 0;
	    if ( orderList != null && orderList.size() > 0 ) {
		params.put( "totalOrderNum", orderList.size() );
		//查询兑换记录
		PageUtil pageUtil = orderService.selectIntegralOrder( params );
		result.put( "pageUtil", pageUtil );

		if ( orderList != null && orderList.size() > 0 ) {
		    for ( MallOrder mallOrder : orderList ) {
			if ( CommonUtil.isEmpty( mallOrder ) || CommonUtil.isEmpty( mallOrder.getOrderMoney() ) ) {
			    continue;
			}
			totalIntegral = CommonUtil.add( totalIntegral, CommonUtil.toDouble( mallOrder.getOrderMoney() ) );
		    }
		}
	    }
	    result.put( "totalIntegral", totalIntegral );
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取兑换记录列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取兑换记录列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取积分明细列表
     */
    @ApiOperation( value = "获取积分明细列表", notes = "获取积分明细列表" )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "当前页数", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "integralDetail", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > integralDetail( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    Map< String,Object > map = new HashMap<>();
	    int pageSize = 20;
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    int firstNum = pageSize * ( ( curPage <= 0 ? 1 : curPage ) - 1 );
	    map.put( "memberId", member.getId() );
	    map.put( "page", firstNum );
	    map.put( "pageSize", pageSize );
	    List< Map > list = memberService.findCardrecordList( map );
	    result.put( "curPage", curPage );
	    result.put( "integralList", list );
	    //查询我的积分
	    if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
		result.put( "memberIntegral", member.getIntegral() );
	    }

	    //	    if ( CommonUtil.isNotEmpty( shopId ) ) {
	    //		MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session
	    //	    }
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取积分明细列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分明细列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取积分商品信息
     */
    @ApiOperation( value = "获取积分商品信息", notes = "获取积分商品信息" )
    @ApiImplicitParams(
		    @ApiImplicitParam( name = "productId", value = "商品ID", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "integralProduct", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > integralProduct( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer productId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "id", productId );
	    Map< String,Object > resultMap = integralService.selectProductDetail( member, params );
	    if ( resultMap != null && resultMap.size() > 0 ) {
		for ( String str : resultMap.keySet() ) {
		    result.put( str, resultMap.get( str ) );
		}
	    }
	    if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
		boolean isMember = memberService.isMember( member.getId() );
		if ( isMember ) {
		    result.put( "isMember", 1 );
		}
		result.put( "memberId", member.getId() );
		result.put( "memberIntegral", member.getIntegral() );
	    }
	    //	    result.put( "member", member );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(),be.getMessage(),be.getData() );
	}  catch ( Exception e ) {
	    logger.error( "获取积分商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取积分商品信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 兑换积分
     */
    @ApiOperation( value = "兑换积分", notes = "兑换积分" )
    @ResponseBody
    @RequestMapping( value = "recordIntegral", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > recordIntegral( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, PhoneAddIntegralDTO integralDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    member = memberService.findMemberById( member.getId(), member );
	    Integer browser = loginDTO.getBrowerType();//CommonUtil.judgeBrowser( request );
	    if ( browser != 1 ) {
		browser = 2;
	    }

	    Map< String,Object > params = new HashMap<>();
	    params.put( "productId", integralDTO.getProductId() );
	    params.put( "integralId", integralDTO.getIntegralId() );
	    params.put( "productNum", integralDTO.getProductNum() );
	    params.put( "productSpecificas", integralDTO.getProductSpecificas() );
	    params.put( "flowPhone", integralDTO.getFlowPhone() );
	    params.put( "uId", loginDTO.getBusId() );

	    result = integralService.recordIntegral( params, member, browser, request );
	} catch ( BusinessException be ) {
	    return ServerResponse.createByErrorCodeMessage( be.getCode(), be.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "兑换积分异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "兑换积分异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
