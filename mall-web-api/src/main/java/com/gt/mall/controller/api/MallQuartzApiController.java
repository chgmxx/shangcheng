package com.gt.mall.controller.api;

import com.gt.mall.dto.ServerResponse;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.quartz.MallQuartzNewService;
import com.gt.mall.service.quartz.MallQuartzOrderTaskService;
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
    private MallQuartzNewService       mallQuartzNewService;
    @Autowired
    private MallQuartzService          mallQuartzService;
    @Autowired
    private MallQuartzOrderTaskService mallQuartzOrderTaskService;

    //    @ApiOperation( value = "修改支付成功回调失败的订单", notes = "修改支付成功回调失败的订单" )
    //    @ResponseBody
    //    @RequestMapping( value = "/orderCallback", method = RequestMethod.POST )
    //    public ServerResponse orderCallback( HttpServletRequest request, HttpServletResponse response ) {
    //	try {
    //	    mallQuartzNewService.orderCallback();
    //	} catch ( Exception e ) {
    //	    logger.error( "修改支付成功回调失败的订单异常：" + e.getMessage() );
    //	    e.printStackTrace();
    //	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改支付成功回调失败的订单异常" );
    //	}
    //	return ServerResponse.createBySuccessCode();
    //    }

    @ApiOperation( value = "每天早上8点扫描", notes = "订单完成赠送物品" )
    @ResponseBody
    @RequestMapping( value = "/orderFinish", method = RequestMethod.POST )
    public ServerResponse orderFinish( HttpServletRequest request, HttpServletResponse response ) {
        try {
            //每天早上8点扫描
            mallQuartzOrderTaskService.orderFinish();
        } catch ( Exception e ) {
            logger.error( "订单完成赠送物品异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单完成赠送物品异常" );
        }
        //修改支付成功回调失败的订单异常
        try {
            mallQuartzNewService.orderCallback();
        } catch ( Exception e ) {
            logger.error( "修改支付成功回调失败的订单异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改支付成功回调失败的订单异常" );
        }
        return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "30分钟执行一次", notes = "关闭30分钟内未支付的订单 30分钟执行一次" )
    @ResponseBody
    @RequestMapping( value = "/closeOrderNoPay", method = RequestMethod.POST )
    public ServerResponse closeOrderNoPay( HttpServletRequest request, HttpServletResponse response ) {
        try {
            //关闭未付款认单
            mallQuartzOrderTaskService.newCloseOrderNoPay();
        } catch ( Exception e ) {
            logger.error( "关闭未付款认单异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "关闭未付款认单异常" );
        }

        try {
            //会员退款失败，用定时器扫描退款
            mallQuartzNewService.memberRefund();
        } catch ( Exception e ) {
            logger.error( "调用会员退款接口异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "调用会员退款接口异常" );
        }
        return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "每天早上2点扫描", notes = "扫描已结束秒杀" )
    @ResponseBody
    @RequestMapping( value = "/endActivity", method = RequestMethod.POST )
    public ServerResponse endActivity( HttpServletRequest request, HttpServletResponse response ) {
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

    //凌晨2点调用
    @ApiOperation( value = "每天早上1点扫描", notes = "统计每天的收入金额、退款维款事件自动处理" )
    @ResponseBody
    @RequestMapping( value = "/countNum", method = RequestMethod.POST )
    public ServerResponse countNum( HttpServletRequest request, HttpServletResponse response ) {
        //每天晚上23点30扫描
        try {
            mallQuartzOrderTaskService.countIncomeNum();
        } catch ( Exception e ) {
            logger.error( "统计每天的收入金额异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "统计每天的收入金额异常" );
        }

        try {
            mallQuartzNewService.autoConfirmTakeDelivery();//自动确认收货
        } catch ( Exception e ) {
            logger.error( "订单自动确认收货异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "维权自动确认收货异常" );
        }
        try {
            mallQuartzOrderTaskService.cancelReturn();//自动取消维权
        } catch ( Exception e ) {
            logger.error( "自动取消维权异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动取消维权异常" );
        }
        try {
            mallQuartzOrderTaskService.autoRefund();//自动退款给买家
        } catch ( Exception e ) {
            logger.error( "自动退款给买家异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动退款给买家异常" );
        }
        try {
            mallQuartzOrderTaskService.returnGoodsByRefund();//自动确认收货并退款至买家
        } catch ( Exception e ) {
            logger.error( "自动确认收货并退款至买家异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "自动确认收货并退款至买家异常" );
        }

        try {
            //评论送礼
            mallQuartzNewService.commentGive();
        } catch ( Exception e ) {
            logger.error( "评论送礼异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "评论送礼异常" );
        }

        try {
            mallQuartzService.activityRefund();//对活动进行退款
        } catch ( Exception e ) {
            logger.error( "已结束未成团的订单进行退款异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "已结束未成团的订单进行退款异常" );
        }
        //	try {
        //	    mallQuartzNewService.countPageVisitorNum();
        //	} catch ( Exception e ) {
        //	    logger.error( "统计每天页面访问数量异常：" + e.getMessage() );
        //	    e.printStackTrace();
        //	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "统计每天页面访问数量异常" );
        //	}

        return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "2小时执行一次", notes = "预售商品开售提醒买家" )
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
}
