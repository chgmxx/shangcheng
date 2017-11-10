package com.gt.mall.controller.api.presale.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.auction.PhoneAddAuctionMarginDTO;
import com.gt.mall.param.phone.pifa.PhoneAddPifaApplyDTO;
import com.gt.mall.param.phone.presale.PhoneAddDepositDTO;
import com.gt.mall.param.phone.presale.PhoneSearchDepositDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 预售管理 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "phonePresale", description = "预售管理页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phonePresale/L6tgXlBFeK/" )
public class PhonePresaleNewController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallPresaleService        mallPresaleService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private MallPageService           pageService;
    @Autowired
    private MemberService             memberService;

    @ApiOperation( value = "进入交纳预收定金页面接口", notes = "进入交纳预收定金页面" )
    @ResponseBody
    @RequestMapping( value = "toAddDeposit", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > toAddDeposit( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, PhoneSearchDepositDTO searchDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    Map mapmessage = pageService.querySelct( searchDTO.getProId() );//获取商品信息
	    int shopid = 0;
	    if ( CommonUtil.isNotEmpty( mapmessage.get( "shop_id" ) ) ) {
		shopid = CommonUtil.toInteger( mapmessage.get( "shop_id" ).toString() );

		if ( CommonUtil.isEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		    request.getSession().setAttribute( "shopId", mapmessage.get( "shop_id" ) );
		} else {
		    if ( !request.getSession().getAttribute( "shopId" ).toString().equals( mapmessage.get( "shop_id" ).toString() ) ) {
			request.getSession().setAttribute( "shopId", mapmessage.get( "shop_id" ) );
		    }
		}
	    }
	    int proNum = 1;
	    if ( CommonUtil.isNotEmpty( searchDTO.getNum() ) ) {
		proNum = searchDTO.getNum();

	    }
	    //查询商品的预售信息
	    MallPresale presale = mallPresaleService.getPresaleByProId( searchDTO.getProId(), shopid, searchDTO.getPresaleId() );
	    if ( CommonUtil.isNotEmpty( presale ) ) {
		DecimalFormat df = new DecimalFormat( "######0.00" );
		double money = CommonUtil.toDouble( presale.getDepositPercent() ) * proNum;
		presale.setDepositPercent( BigDecimal.valueOf( CommonUtil.toDouble( df.format( money ) ) ) );
	    }

	    result.put( "presale", presale );
	    result.put( "mapmessage", mapmessage );

	    List imagelist = pageService.imageProductList( searchDTO.getProId(), 1 );//获取轮播图列表
	    result.put( "imagelist", imagelist.get( 0 ) );
	    String is_specifica = mapmessage.get( "is_specifica" ).toString();
	    Map guige = new HashMap();
	    if ( is_specifica.equals( "1" ) ) {
		guige = pageService.productSpecifications( searchDTO.getProId(), searchDTO.getInvId() + "" );
	    }

	    int memType = memberService.isCardType( member.getId() );
	    result.put( "memType", memType );
	    result.put( "memberId", member.getId() );
	    result.put( "guige", guige );

	    Map< String,Object > publicMap = pageService.publicMapByUserId( member.getBusid() );
	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) ) ) {
		isWxPay = 1;//可以微信支付
	    } else {
		isAliPay = 1;
	    }

	    result.put( "isWxPay", isWxPay );
	    result.put( "isAliPay", isAliPay );
	} catch ( Exception e ) {
	    logger.error( "获取交纳保证金信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取交纳保证金信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 交纳定金
     */
    @ApiOperation( value = "交纳定金", notes = "交纳定金" )
    @ResponseBody
    @RequestMapping( value = "addDeposit", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > addDeposit( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    PhoneAddDepositDTO depositDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    result = mallPresaleDepositService.addDeposit( depositDTO, member, loginDTO.getBrowerType() );
	    if ( !result.get( "code" ).toString().equals( "1" ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "errorMsg" ).toString() );
	    }
	} catch ( Exception e ) {
	    logger.error( "交纳定金异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交纳定金异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 消息提醒
     */
    @ApiOperation( value = "消息提醒", notes = "消息提醒", hidden = true )
    @ResponseBody
    @RequestMapping( value = "messageRemind", method = RequestMethod.POST )
    public ServerResponse messageRemind( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "preId", value = "预售ID", required = true ) @RequestParam String preId,
		    @ApiParam( name = "busId", value = "商家ID", required = true ) @RequestParam Integer busId ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    Map< String,Object > param = new HashMap<>();
	    param.put( "preId", preId );
	    Map< String,Object > result = mallPresaleDepositService.addMessage( param, member.getId().toString() );
	    Boolean flag = (Boolean) result.get( "result" );
	    if ( !flag ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "预售提醒失败，稍后请重新提交" );
	    }
	} catch ( Exception e ) {
	    logger.error( "预售提醒异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "预售提醒异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    @ApiOperation( value = "获取我的定金列表", notes = "获取我的定金列表" )
    @ResponseBody
    @RequestMapping( value = "myDepositList", method = RequestMethod.POST )
    public ServerResponse myDepositList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	List< MallPresaleDeposit > depositList = null;
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    MallPresaleDeposit deposit = new MallPresaleDeposit();
	    if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
		List< String > lists = new ArrayList<>();
		for ( String oldMemberId : member.getOldid().split( "," ) ) {
		    if ( CommonUtil.isNotEmpty( oldMemberId ) ) {
			lists.add( oldMemberId );
		    }
		}
		deposit.setOldUserIdList( lists );
	    } else {
		deposit.setUserId( member.getId() );
	    }

	    depositList = mallPresaleDepositService.getMyPresale( deposit );

	} catch ( Exception e ) {
	    logger.error( "获取我的定金列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取我的定金列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), depositList, true );
    }

}


