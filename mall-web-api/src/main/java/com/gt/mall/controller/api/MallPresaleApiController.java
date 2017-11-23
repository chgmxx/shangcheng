package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
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
 * 商品预售相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallPresale/mallAPI" )
public class MallPresaleApiController {

    private static Logger logger = LoggerFactory.getLogger( MallPresaleApiController.class );

    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;

    @ApiOperation( value = "交纳保证金成功回调", notes = "交纳保证金成功回调" )
    @ResponseBody
    @RequestMapping( value = "/paySuccessPresale", method = RequestMethod.GET )
    public ServerResponse paySuccessPresale( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    mallPresaleDepositService.paySuccessPresale( params );
	} catch ( Exception e ) {
	    logger.error( "交纳保证金成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交纳保证金成功回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "退定金成功回调", notes = "退定金成功回调接口" )
    @ResponseBody
    @RequestMapping( value = "/returnSuccessBack", method = RequestMethod.GET )
    public ServerResponse returnSuccessBack( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    logger.info( "退定金成功回调参数：" + JSONObject.fromObject( params ) );
	    String outTradeNo = params.get( "outTradeNo" ).toString();

	    Wrapper< MallPresaleDeposit > wrapper = new EntityWrapper<>();
	    wrapper.where( "deposit_no= {0}", outTradeNo );
	    Map< String,Object > deposit = mallPresaleDepositService.selectMap( wrapper );

	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", deposit.get( "id" ) );
	    map.put( "user_id", deposit.get( "userId" ) );
	    map.put( "pay_way", deposit.get( "payWay" ) );
	    map.put( "deposit_money", deposit.get( "depositMoney" ) );
	    map.put( "deposit_no", deposit.get( "depositNo" ) );
	    Map< String,Object > result = mallPresaleDepositService.returnEndPresale( map );
	    if ( CommonUtil.isNotEmpty( result.get( "result" ) ) ) {
		boolean flag = (boolean) result.get( "result" );
		if ( !flag ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "msg" ).toString() );
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "退定金成功回调接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "退定金成功回调接口异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
