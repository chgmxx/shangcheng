package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.utils.CommonUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品拍卖相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallAuction/mallAPI" )
public class MallAuctionApiController {

    private static Logger logger = LoggerFactory.getLogger( MallAuctionApiController.class );

    @Autowired
    private MallAuctionMarginService mallAuctionMarginService;

    @ApiOperation( value = "交纳保证金成功回调", notes = "交纳保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/paySuccessAuction", method = RequestMethod.GET )
    public ServerResponse paySuccessAuction( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    mallAuctionMarginService.paySuccessAuction( params );
	} catch ( Exception e ) {
	    logger.error( "交纳保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交纳保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "退保证金成功回调", notes = "退保证金成功回调接口" )
    @ResponseBody
    @RequestMapping( value = "/returnSuccessBack", method = RequestMethod.GET )
    public ServerResponse returnSuccessBack( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    logger.info( "退保证金成功回调参数：" + JSONObject.fromObject( params ) );
	    String outTradeNo = params.get( "outTradeNo" ).toString();

	    Wrapper< MallAuctionMargin > wrapper = new EntityWrapper<>();
	    wrapper.where( "auc_no = {0}", outTradeNo );
	    Map< String,Object > margin = mallAuctionMarginService.selectMap( wrapper );

	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", margin.get( "id" ) );
	    map.put( "user_id", margin.get( "userId" ) );
	    map.put( "pay_way", margin.get( "payWay" ) );
	    map.put( "margin_money", margin.get( "marginMoney" ) );
	    map.put( "auc_no", margin.get( "aucNo" ) );
	/*    map.put( "create_time", margin.get( "createTime" ) );
	    map.put( "auc_type", margin.get( "aucType" ) );
	    map.put( "order_id", margin.get( "orderId" ) );
	    map.put( "order_status", margin.get( "orderStatus" ) );*/

	    Map< String,Object > result = mallAuctionMarginService.returnEndMargin( margin );
	    if ( CommonUtil.isNotEmpty( result.get( "result" ) ) ) {
		boolean flag = (boolean) result.get( "result" );
		if ( !flag ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "msg" ).toString() );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "退保证金成功回调接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退保证金成功回调接口异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
