package com.gt.mall.controller.api.common;

import com.gt.mall.bean.Member;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.result.phone.PhoneMemberResult;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.SessionUtils;
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
import java.util.Map;

/**
 * 我的页面
 *
 * @author Administrator
 */
@Api( value = "phoneMember", description = "我的页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneMember" )
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

    @ApiOperation( value = "进入我的页面的接口", notes = "查询我的页面信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/toUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneMemberResult > toUser( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    PhoneMemberResult result = new PhoneMemberResult();
	    Member member = SessionUtils.getLoginMember( request );
	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    if(CommonUtil.isNotEmpty( loginDTO.getBusId() ) && loginDTO.getBusId() > 0){
		result.setBusId( loginDTO.getBusId() );
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
	    logger.error( "进入我的页面的异常：" + e.getMessage() );
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
    @PostMapping( value = "79B4DE7C/isLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse isLogin( HttpServletRequest request, HttpServletResponse response, PhoneLoginDTO loginDTO ) {
	try {
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "是否需要登陆异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	} catch ( Exception e ) {
	    logger.error( "是否需要登陆异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "是否需要登陆失败" );
	}
    }

}
