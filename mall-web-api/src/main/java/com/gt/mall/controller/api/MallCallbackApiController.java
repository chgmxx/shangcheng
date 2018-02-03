package com.gt.mall.controller.api;

import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.order.QuartzOrderService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 回调接口，不会走登陆拦截
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Api( value = "/mallCallback/callbackApi", description = "回调接口相关API接口", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallCallback/callbackApi" )
public class MallCallbackApiController {

    private static Logger logger = LoggerFactory.getLogger( MallCallbackApiController.class );

    @Autowired
    private MallAuctionMarginService  mallAuctionMarginService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private QuartzOrderService        quartzOrderService;
    @Autowired
    private MallOrderService          mallOrderService;

    @ApiOperation( value = "预售交纳保证金成功回调", notes = "预售交纳保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/paySuccessPresale", method = RequestMethod.POST )
    public ServerResponse paySuccessPresale( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    //params 传参 out_trade_no：保证金编号
	    logger.info( "预售交纳保证金成功回调参数：" + params );
	    mallPresaleDepositService.paySuccessPresale( params );
	} catch ( Exception e ) {
	    logger.error( "预售交纳保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "预售交纳保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "预售退保证金成功回调", notes = "预售退保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/refundSuccessPresale", method = RequestMethod.POST )
    public ServerResponse refundSuccessPresale( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    //params 传参 out_trade_no：保证金编号
	    logger.info( "预售退保证金成功回调参数：" + params );
	    mallPresaleDepositService.returnAlipayDeposit( params );
	} catch ( Exception e ) {
	    logger.error( "预售退保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "预售退保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "拍卖交纳保证金成功回调", notes = "拍卖交纳保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/paySuccessAuction", method = RequestMethod.POST )
    public ServerResponse paySuccessAuction( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    //params 传参 out_trade_no：保证金编号
	    logger.info( "拍卖交纳保证金成功回调参数：" + JSONObject.fromObject( params ) );
	    mallAuctionMarginService.paySuccessAuction( params );
	} catch ( Exception e ) {
	    logger.error( "拍卖交纳保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "拍卖交纳保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "拍卖退保证金成功回调", notes = "拍卖退保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/refundSuccessAuction", method = RequestMethod.POST )
    public ServerResponse refundSuccessAuction( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    //params 传参 out_trade_no：保证金编号
	    logger.info( "拍卖退保证金成功回调参数：" + JSONObject.fromObject( params ) );
	    mallAuctionMarginService.returnAlipayMargin( params );
	} catch ( Exception e ) {
	    logger.error( "拍卖退保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "拍卖退保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 支付成功回调
     */
    @ApiOperation( value = "支付成功回调的接口", notes = "支付成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ResponseBody
    @PostMapping( value = "paySuccessModified" )
    public ServerResponse paySuccessModified( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	logger.info( " 支付成功回调参数：" + JSONObject.fromObject( params ) );
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) ) {
		return ServerResponse.createByErrorMessage( "支付成功回调失败：参数=" + JSONObject.fromObject( params ) );
	    }
	    mallOrderService.paySuccessModified( params, null );
	} catch ( BusinessException e ) {
	    logger.error( "支付成功回调异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "支付成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "支付成功回调失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    @ApiOperation( value = "订单退款回调", notes = "订单退款回调" )
    @ResponseBody
    @PostMapping( value = "/agreanOrderReturn" )
    public ServerResponse agreanOrderReturn( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    mallOrderService.agreanOrderReturn( params );
	} catch ( Exception e ) {
	    logger.error( "订单退款回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单退款回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 代付支付成功的回调
     */
    @ApiOperation( value = "代付支付成功回调的接口", notes = "代付支付成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ApiModelProperty( hidden = true )
    @ResponseBody
    @PostMapping( value = "daifuSuccess" )
    public ServerResponse daifuSuccess( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) && CommonUtil.isNotEmpty( params.get( "no" ) ) ) {
		params.put( "out_trade_no", params.get( "no" ) );
	    }
	    mallOrderService.paySuccessDaifu( params );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "代付支付成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "代付支付成功回调异常" );
	}
    }

    /**
     * 流量充值成功回调
     */
    @ApiOperation( value = "流量充值成功回调的接口", notes = "流量充值成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ApiModelProperty( hidden = true )
    @ResponseBody
    @PostMapping( value = "flowSuccess" )
    public ServerResponse flowSuccess( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "id" ) ) && CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
		return ErrorInfo.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	    }
	    quartzOrderService.rollbackOrderByFlow( params );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "流量充值成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "流量充值成功回调异常" );
	}
    }
}
