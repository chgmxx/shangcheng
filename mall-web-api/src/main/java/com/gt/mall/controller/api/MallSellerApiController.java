package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级销售员相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallSeller/mallAPI" )
public class MallSellerApiController {

    private static Logger logger = LoggerFactory.getLogger( MallSellerApiController.class );

    @Autowired
    private MallSellerService mallSellerService;

    @ApiOperation( value = "关注送积分", notes = "关注送积分" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "关注人id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "secenKey", value = "生成二维码时的key", paramType = "query", required = true, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/sellerSendIntegral", method = RequestMethod.POST )
    public ServerResponse sellerSendIntegral( HttpServletRequest request, HttpServletResponse response, Integer memberId, String secenKey ) {
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "memberId", memberId );
	    params.put( "scene_id", secenKey );
	    Map< String,Object > result = mallSellerService.sellerSendIntegral( params );
	    boolean flag = (boolean) result.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "errorMsg" ).toString() );
	    }
	} catch ( Exception e ) {
	    logger.error( "关注送积分异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "关注送积分异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}
