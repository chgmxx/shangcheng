package com.gt.mall.controller.api.html.phone;

import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.constant.Constants;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.auction.PhoneAddAuctionBiddingDTO;
import com.gt.mall.param.phone.auction.PhoneAddAuctionMarginDTO;
import com.gt.mall.param.phone.html.PhoneAddHtmlFromDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.auction.MallAuctionOfferService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.html.MallHtmlFromService;
import com.gt.mall.service.web.html.MallHtmlReportService;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * h5商城 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "phoneHtml", description = "h5商城页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneHtml/L6tgXlBFeK/" )
public class PhoneHtmlController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallHtmlService       htmlService;
    @Autowired
    private MallHtmlFromService   htmlFromService;
    @Autowired
    private MallHtmlReportService htmlReportService;
    @Autowired
    private WxPublicUserService   wxPublicUserService;

    /**
     * 获取h5商城信息
     */
    @ApiOperation( value = "获取h5商城信息", notes = "获取h5商城信息" )
    @ResponseBody
    @RequestMapping( value = "/htmlInfo", method = RequestMethod.POST )
    public ServerResponse htmlInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "h5ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    MallHtml obj = htmlService.selectById( id );
	    Integer style = 1;//0代表是微信有公主号，1没有
	    String ua = ( (HttpServletRequest) request ).getHeader( "user-agent" ).toLowerCase();
	    if ( ua.indexOf( "micromessenger" ) > 0 ) {// 是否来自于微信浏览器打开
		//来自于商家这边
		if ( obj.getSourceType() == 2 ) {
		    WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( obj.getBusUserId() );
		    if ( wxPublicUsers != null ) {
			style = 0;
		    }
		}
	    }
	    result.put( "obj", obj );
	    result.put( "style", style );
	} catch ( Exception e ) {
	    logger.error( "获取h5商城信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取h5商城信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 商城举报
     */
    @ApiOperation( value = "商城举报", notes = "商城举报" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "style", value = "举报ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "htmlId", value = "h5ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "htmlReport", method = RequestMethod.POST )
    public ServerResponse htmlReport( HttpServletRequest request, HttpServletResponse response, Integer style, Integer htmlId ) {
	try {
	    htmlReportService.htmlReport( htmlId, style );
	} catch ( Exception e ) {
	    logger.error( "商城举报异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商城举报异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 提交商城表单信息
     */
    @ApiOperation( value = "提交商城表单信息", notes = "提交商城表单信息" )
    @ResponseBody
    @RequestMapping( value = "htmlfrom", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > htmlfrom( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneAddHtmlFromDTO htmlFromDTO ) {
	try {
	    MallHtmlFrom from = new MallHtmlFrom();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( htmlFromDTO, from );
	    from.setCreattime( DateTimeKit.getDateTime() );
	    htmlFromService.insert( from );
	} catch ( Exception e ) {
	    logger.error( "提交商城表单信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "提交商城表单信息异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}


