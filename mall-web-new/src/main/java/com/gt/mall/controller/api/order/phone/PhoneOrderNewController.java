package com.gt.mall.controller.api.order.phone;

import com.gt.mall.bean.Member;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneBuyNowDTO;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
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
import java.util.Map;

/**
 * 订单页面相关接口
 * User : yangqian
 * Date : 2017/10/19 0019
 * Time : 17:10
 */
@Api( value = "phoneOrderNew", description = "订单页面相关接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneOrderNew/" )
public class PhoneOrderNewController {

    private static Logger logger = LoggerFactory.getLogger( PhoneOrderNewController.class );

    @Autowired
    private MallProductNewService mallProductNewService;

    @ApiOperation( value = "立即购买接口", notes = "用户点击立即购买", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/buyNow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > buyNow( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneBuyNowDTO params ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
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
}
