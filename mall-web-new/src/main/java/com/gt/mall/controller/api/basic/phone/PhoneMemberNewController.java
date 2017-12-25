package com.gt.mall.controller.api.basic.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.result.phone.PhoneMemberResult;
import com.gt.mall.result.phone.PhoneUrlResult;
import com.gt.mall.result.phone.product.PhoneCollectProductResult;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 我的页面
 *
 * @author Administrator
 */
@Api( value = "phoneMember", description = "我的页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneMember/L6tgXlBFeK/" )
public class PhoneMemberNewController extends AuthorizeOrUcLoginController {

    public static final Logger logger = Logger.getLogger( PhoneMemberNewController.class );

    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private MallPifaApplyService mallPifaApplyService;
    @Autowired
    private MallSellerService    mallSellerService;
    @Autowired
    private MallOrderService     mallOrderService;
    @Autowired
    private MallCollectService   mallCollectService;

    @ApiOperation( value = "进入我的页面的接口", notes = "查询我的页面信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "toUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneMemberResult > toUser( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    PhoneMemberResult result = new PhoneMemberResult();
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    if ( CommonUtil.isNotEmpty( loginDTO.getBusId() ) && loginDTO.getBusId() > 0 ) {
		result.setBusId( loginDTO.getBusId() );
	    }
	    if ( CommonUtil.isNotEmpty( member ) ) {
		result.setMemberName( member.getNickname() );
		result.setMemberImageUrl( member.getHeadimgurl() );
		result.setIsLogin( 1 );
	    }

	    int pfStatus = -2;
	    int sellerStatus = -2;
	    double minCosumeMoney = -1;
	    double consumeMoney = -1;

	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( set.getIsPf().toString().equals( "1" ) ) {
		    pfStatus = mallPifaApplyService.getPifaApplay( member, set );
		    result.setIsOpenPf( 1 );
		    result.setPfStatus( pfStatus );
		}
		if ( set.getIsSeller().toString().equals( "1" ) ) {
		    sellerStatus = mallSellerService.selectSellerStatusByMemberId( member, set );
		    result.setIsOpenSeller( 1 );
		    result.setSellerStatus( sellerStatus );
		}
	    }
	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( loginDTO.getBusId() );
	    if ( CommonUtil.isNotEmpty( sellerSet ) ) {
		if ( CommonUtil.isNotEmpty( sellerSet.getConsumeMoney() ) ) {
		    minCosumeMoney = CommonUtil.toDouble( sellerSet.getConsumeMoney() );
		    if ( minCosumeMoney > 0 ) {
			consumeMoney = 0;
			if ( CommonUtil.isNotEmpty( member ) ) {
			    //判断消费金额
			    Map< String,Object > consumeMap = mallOrderService.selectSumSaleMoney( member.getId() );
			    if ( CommonUtil.isNotEmpty( consumeMap ) ) {
				if ( CommonUtil.isNotEmpty( consumeMap.get( "orderMoney" ) ) ) {
				    consumeMoney = CommonUtil.toDouble( consumeMap.get( "orderMoney" ) );
				}
			    }
			}
			if ( minCosumeMoney > consumeMoney ) {
			    result.setConsumeMoney( consumeMoney );
			    result.setMinCosumeMoney( minCosumeMoney );
			}
		    }
		}
	    }
	    result.setPfErrorMsg( CommonUtil.getPifaErrorMsg( pfStatus ) );
	    result.setSellerErrorMsg( CommonUtil.getSellerErrorMsg( sellerStatus, consumeMoney, minCosumeMoney ) );

	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, loginDTO.getBusId() );
	    result.setSaleMemberId( saleMemberId );

	    result.setBusId( loginDTO.getBusId() );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( BusinessException e ) {
	    logger.error( "进入我的页面的异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "进入我的页面的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "是否需要登陆接口", notes = "是否需要登陆订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "Integer" ),
		    @ApiImplicitParam( name = "pageUrl", value = "当前页面地址", paramType = "query", required = true, dataType = "string" )
    } )
    @ResponseBody
    @PostMapping( value = "isLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse isLogin( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "是否需要登陆异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "是否需要登陆异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "是否需要登陆失败" );
	}
    }

    /***************************************   以下接口为：收藏接口    ***************************************/

    @ApiOperation( value = "手机端查看收藏列表的接口", notes = "我的收藏", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "collectList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< PhoneCollectProductResult > > collectList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    List< PhoneCollectProductResult > resultList = mallCollectService.getCollectProductList( member.getId() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultList );
	} catch ( BusinessException e ) {
	    logger.error( "查看收藏列表的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查看收藏列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询我的收藏失败" );
	}
    }

    @ApiOperation( value = "删除收藏商品接口", notes = "删除收藏商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams(
		    @ApiImplicitParam( name = "ids", value = "删除收藏id集合", paramType = "query", required = true, dataType = "string" )
    )
    @ResponseBody
    @PostMapping( value = "deleteCollect", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse deleteCollect( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, String ids ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    mallCollectService.deleteCollect( ids );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "删除收藏商品的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除收藏商品的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "删除收藏商品失败" );
	}
    }

    @ApiOperation( value = "获取其他项目的地址", notes = "获取其他项目的地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "Integer" ) )
    @ResponseBody
    @PostMapping( value = "memberCenter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneUrlResult > memberCenter( HttpServletRequest request, HttpServletResponse response, String busId, Integer type ) {
	try {
	    PhoneUrlResult result = new PhoneUrlResult();
	    String memberCenterUrl = PropertiesUtil.getWxmpDomain() + Constants.MEMBER_URL.replace( "${userid}", busId );
	    //	    String memberCenterUrl = PropertiesUtil.getMemberDomain() + Constants.MEMBER_NEW_URL.replace( "${userid}", busId.toString() );

	    String couponUrl = PropertiesUtil.getWxmpDomain() + Constants.COUPON_URL.replace( "${userid}", busId );
	    result.setMemberCenterUrl( memberCenterUrl );
	    result.setMemberCouponUrl( couponUrl );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( Exception e ) {
	    logger.error( "获取会员中心的地址异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取会员中心的地址" );
	}
    }

}
