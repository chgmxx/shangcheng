package com.gt.mall.controller.api;

import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.quartz.MallQuartzNewService;
import com.gt.mall.service.quartz.MallQuartzService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定时任务相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallQuartz/mallAPI" )
public class MallQuartzApiController {

    private static Logger logger = LoggerFactory.getLogger( MallQuartzApiController.class );

    @Autowired
    private MallQuartzNewService mallQuartzNewService;
    @Autowired
    private MallQuartzService    mallQuartzService;

    @ApiOperation( value = "修改支付成功回调失败的订单", notes = "修改支付成功回调失败的订单" )
    @ResponseBody
    @RequestMapping( value = "/orderCallback", method = RequestMethod.POST )
    public ServerResponse orderCallback( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    mallQuartzNewService.orderCallback();
	} catch ( Exception e ) {
	    logger.error( "修改支付成功回调失败的订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改支付成功回调失败的订单异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

//    @ApiOperation( value = "统计商城信息", notes = "统计商城信息" )
//    @ResponseBody
//    @RequestMapping( value = "/mallCount", method = RequestMethod.POST )
//    public ServerResponse mallCount( HttpServletRequest request, HttpServletResponse response ) {
//	try {
//	    //晚上一点
//	    mallQuartzNewService.mallcount();
//	} catch ( Exception e ) {
//	    logger.error( "统计商城信息异常：" + e.getMessage() );
//	    e.printStackTrace();
//	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "统计商城信息异常" );
//	}
//	return ServerResponse.createBySuccessCode();
//    }

    @ApiOperation( value = "订单完成赠送物品", notes = "订单完成赠送物品" )
    @ResponseBody
    @RequestMapping( value = "/orderFinish", method = RequestMethod.POST )
    public ServerResponse orderFinish( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    //每天早上8点扫描
	    mallQuartzNewService.orderFinish();
	} catch ( Exception e ) {
	    logger.error( "订单完成赠送物品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单完成赠送物品异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "关闭30分钟内未支付的订单", notes = "关闭30分钟内未支付的订单" )
    @ResponseBody
    @RequestMapping( value = "/closeOrderNoPay", method = RequestMethod.POST )
    public ServerResponse closeOrderNoPay( HttpServletRequest request, HttpServletResponse response ) {
	//三十分钟更新一次
	/*try {
	    //关闭未付款认单,未支付的秒杀订单
	    mallQuartzNewService.closeOrderNoPay();
	} catch ( Exception e ) {
	    logger.error( "关闭30分钟内未支付的订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "关闭30分钟内未支付的订单异常" );
	}*/
	try {
	    //关闭未付款认单
	    mallQuartzNewService.closeNoPayOrder();
	} catch ( Exception e ) {
	    logger.error( "关闭未付款认单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "关闭未付款认单异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "扫描已结束秒杀", notes = "扫描已结束秒杀" )
    @ResponseBody
    @RequestMapping( value = "/endSeckill", method = RequestMethod.POST )
    public ServerResponse endSeckill( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    //每天早上2点扫描
	    mallQuartzNewService.endSeckill();
	} catch ( Exception e ) {
	    logger.error( "扫描已结束秒杀异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "扫描已结束秒杀异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "统计每天营业额,页面访问数量", notes = "统计每天营业额,页面访问数量" )
    @ResponseBody
    @RequestMapping( value = "/countNum", method = RequestMethod.POST )
    public ServerResponse countNum( HttpServletRequest request, HttpServletResponse response ) {
	//每天晚上23点30扫描
	try {
	    mallQuartzNewService.countIncomeNum();
	} catch ( Exception e ) {
	    logger.error( "统计每天营业额异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "统计每天营业额异常" );
	}
	try {
	    mallQuartzNewService.countPageVisitorNum();
	} catch ( Exception e ) {
	    logger.error( "统计每天页面访问数量异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "统计每天页面访问数量异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "退款维款事件自动处理", notes = "退款维款事件自动处理" )
    @ResponseBody
    @RequestMapping( value = "/returnAutoHandle", method = RequestMethod.POST )
    public ServerResponse returnAutoHandle( HttpServletRequest request, HttpServletResponse response ) {
	//每天晚上23点扫描
	try {
	    mallQuartzNewService.autoConfirmTakeDelivery();//自动确认收货
	} catch ( Exception e ) {
	    logger.error( "订单自动确认收货异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "维权自动确认收货异常" );
	}
	try {
	    mallQuartzNewService.cancelReturn();//自动取消维权
	} catch ( Exception e ) {
	    logger.error( "自动取消维权异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动取消维权异常" );
	}
	try {
	    mallQuartzNewService.autoRefund();//自动退款给买家
	} catch ( Exception e ) {
	    logger.error( "自动退款给买家异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动退款给买家异常" );
	}
	try {
	    mallQuartzNewService.returnGoodsByRefund();//自动确认收货并退款至买家
	} catch ( Exception e ) {
	    logger.error( "自动确认收货并退款至买家异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动确认收货并退款至买家异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "退款流量充值", notes = "退款流量充值" )
    @ResponseBody
    @RequestMapping( value = "/returnFlow", method = RequestMethod.POST )
    public ServerResponse returnFlow( HttpServletRequest request, HttpServletResponse response ) {
	//每天早上3点扫描
	try {
	    mallQuartzService.returnFlow();
	} catch ( Exception e ) {
	    logger.error( "退款流量充值异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退款流量充值异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "已结束未成团的订单进行退款", notes = "已结束未成团的订单进行退款" )
    @ResponseBody
    @RequestMapping( value = "/endGroupReturn", method = RequestMethod.POST )
    public ServerResponse endGroupReturn( HttpServletRequest request, HttpServletResponse response ) {
	//每天早上1点扫描
	try {
	    mallQuartzService.endGroupReturn();
	} catch ( Exception e ) {
	    logger.error( "已结束未成团的订单进行退款异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "已结束未成团的订单进行退款异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "预售商品开售提醒买家", notes = "预售商品开售提醒买家" )
    @ResponseBody
    @RequestMapping( value = "/presaleStar", method = RequestMethod.POST )
    public ServerResponse presaleStar( HttpServletRequest request, HttpServletResponse response ) {
	//两个小时扫描一次
	try {
	    mallQuartzService.presaleStar();
	} catch ( Exception e ) {
	    logger.error( "预售商品开售提醒买家异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "预售商品开售提醒买家异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "调用会员退款接口", notes = "调用会员退款接口" )
    @ResponseBody
    @RequestMapping( value = "/memberRefund", method = RequestMethod.POST )
    public ServerResponse memberRefund( HttpServletRequest request, HttpServletResponse response ) {
	//
	try {
	    mallQuartzNewService.memberRefund();
	} catch ( Exception e ) {
	    logger.error( "调用会员退款接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "调用会员退款接口异常" );
	}
	return ServerResponse.createBySuccessCode();
    }
}
