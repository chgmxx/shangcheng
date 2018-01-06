package com.gt.mall.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.utils.CommonUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 商城订单相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallOrder/mallAPI" )
public class MallOrderApiController {

    private static Logger logger = LoggerFactory.getLogger( MallOrderApiController.class );

    @Autowired
    private MallOrderService    mallOrderService;
    @Autowired
    private WxPublicUserService wxPublicUserService;
    @Autowired
    private MemberService       memberService;

    @ApiOperation( value = "线下订单", notes = "扫码支付" )
    //    @ApiImplicitParams( { @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = true, dataType = "int" ),
    //		    @ApiImplicitParam( name = "totalFee", value = "订单金额", paramType = "query", required = true, dataType = "String" ),
    //		    @ApiImplicitParam( name = "memberId", value = "会员ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/scanLinePay", method = RequestMethod.POST )
    public ServerResponse scanLinePay( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );
	    Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
	    WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( member.getBusid() );
	    MallOrder order = new MallOrder();
	    order.setOrderNo( "SC" + System.currentTimeMillis() );
	    order.setOrderMoney( new BigDecimal( params.get( "totalFee" ).toString() ) );
	    order.setOrderPayWay( 5 );//扫码支付
	    if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		order.setSellerUserId( wxPublicUsers.getId() );//公众号ID
	    }
	    order.setShopId( CommonUtil.toInteger( params.get( "shopId" ) ) );
	    order.setOrderStatus( 1 );
	    order.setCreateTime( new Date() );
	    order.setPayTime( new Date() );
	    order.setBuyerUserId( member.getId() );//买家ID
	    order.setBusUserId( member.getBusid() );//商家ID
	    mallOrderService.insert( order );
	} catch ( Exception e ) {
	    logger.error( "线下订单接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "线下订单接口异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    //    @ApiOperation( value = "订单支付成功回调", notes = "订单支付成功回调" )
    //    @ResponseBody
    //    @RequestMapping( value = "/paySuccessOrder", method = RequestMethod.GET )
    //    public ServerResponse paySuccessOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
    //	try {
    //	    params.put( "status", 2 );
    //	    mallOrderService.paySuccessModified( params, null );
    //
    //	} catch ( Exception e ) {
    //	    logger.error( "订单支付成功回调异常：" + e.getMessage() );
    //	    e.printStackTrace();
    //	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单支付成功回调异常" );
    //	}
    //	return ServerResponse.createBySuccessCode();
    //    }
    //
    //    @ApiOperation( value = "找人代付成功回调", notes = "找人代付成功回调" )
    //    @ResponseBody
    //    @RequestMapping( value = "/paySuccessDaifu", method = RequestMethod.GET )
    //    public ServerResponse paySuccessDaifu( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
    //	try {
    //	    mallOrderService.paySuccessDaifu( params );
    //	} catch ( Exception e ) {
    //	    logger.error( "找人代付成功回调异常：" + e.getMessage() );
    //	    e.printStackTrace();
    //	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "找人代付成功回调异常" );
    //	}
    //	return ServerResponse.createBySuccessCode();
    //    }

}
