package com.gt.mall.controller.api.order.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.common.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneBuyNowDTO;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.order.PhoneToOrderDTO;
import com.gt.mall.result.phone.order.PhoneToOrderResult;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.basic.MallTakeTheirTimeService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 订单页面相关接口
 * User : yangqian
 * Date : 2017/10/19 0019
 * Time : 17:10
 */
@Api( value = "phoneOrder", description = "订单页面相关接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneOrder/" )
public class PhoneOrderNewController extends AuthorizeOrUcLoginController {

    private static Logger logger = LoggerFactory.getLogger( PhoneOrderNewController.class );

    @Autowired
    private MallProductNewService    mallProductNewService;//新的商品业务处理类
    @Autowired
    private MallOrderNewService      mallOrderNewService;//新的订单业务处理类
    @Autowired
    private MallTakeTheirTimeService mallTakeTheirTimeService;//上门自提时间业务处理类
    @Autowired
    private MallTakeTheirService     mallTakeTheirService;//上门自提业务处理类

    @ApiOperation( value = "立即购买接口", notes = "用户点击立即购买", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/buyNow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > buyNow( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneBuyNowDTO params ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, params.getBusId() );
	    int memberId = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		memberId = member.getId();
	    }
	    mallProductNewService
			    .calculateInventory( params.getProductId(), params.getProductSpecificas(), params.getProductNum(), params.getType(), params.getActivityId(), memberId );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "立即购买异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "立即购买异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "立即购买失败" );
	}
    }

    @ApiOperation( value = "进入提交订单页面的接口", notes = "提交订单页面的接口，查询商品信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/toOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneToOrderResult > toOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneToOrderDTO params,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    //封装登陆参数
	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneToOrderResult result = mallOrderNewService.toOrder( params, member, loginDTO );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( BusinessException e ) {
	    logger.error( "进入提交订单页面的接口异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "进入提交订单页面的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "请求提交订单的数据失败" );
	}
    }

    @ApiOperation( value = "查询上门自提地址的接口", notes = "根据上门自提id查询上门自提时间", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "takeId", value = "上门自提id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getTakeTheirTime", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< MallTakeTheirTime > > getTakeTheirTime( HttpServletRequest request, HttpServletResponse response, Integer takeId ) {
	try {
	    List< MallTakeTheirTime > timeList = mallTakeTheirTimeService.selectTakeTheirTime( takeId );//查询到店自提的默认地址
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), timeList, false );
	} catch ( Exception e ) {
	    logger.error( "查询上门自提时间的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询上门自提时间失败" );
	}
    }

    @ApiOperation( value = "查询上门自提地址的接口", notes = "根据商家id查询上门自提地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "provinceIds", value = "省份id", paramType = "query", dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getTakeTheir", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< MallTakeTheir > > getTakeTheir( HttpServletRequest request, HttpServletResponse response, Integer busId, Integer provinceIds ) {
	try {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "userId", busId );
	    if ( CommonUtil.isNotEmpty( provinceIds ) && provinceIds > 0 ) {
		map.put( "provinceId", provinceIds );
	    }
	    //根据公众号id查询提取信息
	    List< MallTakeTheir > takeList = mallTakeTheirService.selectByBusUserId( map );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), takeList, false );
	} catch ( Exception e ) {
	    logger.error( "查询上门自提地址的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询上门自提地址失败" );
	}
    }

}
