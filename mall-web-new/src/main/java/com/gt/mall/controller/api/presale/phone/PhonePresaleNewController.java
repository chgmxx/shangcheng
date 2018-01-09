package com.gt.mall.controller.api.presale.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.presale.MallPresaleTime;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.presale.PhoneAddDepositDTO;
import com.gt.mall.param.phone.presale.PhoneSearchDepositDTO;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.MarginUtil;
import com.gt.util.entity.param.pay.PayWay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private MallPresaleService          mallPresaleService;
    @Autowired
    private MallPresaleDepositService   mallPresaleDepositService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallPresaleTimeService      mallPresaleTimeService;
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private CoreService                 coreService;
    @Autowired
    private PayService                  payService;

    @ApiOperation( value = "进入交纳预收定金页面接口", notes = "进入交纳预收定金页面" )
    @ResponseBody
    @RequestMapping( value = "toAddDeposit", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > toAddDeposit( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, PhoneSearchDepositDTO searchDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( "6" ) );////判断活动是否已经过期

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    //	    userLogin( request, response, loginDTO );

	    MallProduct product = mallProductService.selectById( searchDTO.getProId() );//获取商品信息
	    int shopid = 0;
	    if ( CommonUtil.isNotEmpty( product.getShopId() ) ) {
		shopid = product.getShopId();
		MallRedisUtils.getMallShopId( shopid );
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
	    result.put( "product", product );
	    double proPrice = CommonUtil.toDouble( product.getProPrice() );
	    if ( CommonUtil.isNotEmpty( searchDTO.getInvId() ) && searchDTO.getInvId() > 0 ) {
		MallProductInventory inventory = mallProductInventoryService.selectById( searchDTO.getInvId() );
		if ( "0".equals( inventory.getIsDelete().toString() ) && inventory.getProductId().toString().equals( searchDTO.getProId().toString() ) ) {
		    proPrice = CommonUtil.toDouble( inventory.getInvPrice() );
		}
	    }

	    List< MallPresaleTime > timeList = mallPresaleTimeService.getPresaleTimeByPreId( presale.getId() );
	    proPrice = mallPresaleService.getPresalePrice( proPrice, timeList );
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    proPrice = CommonUtil.toDouble( df.format( proPrice ) );
	    result.put( "orderPrice", df.format( proPrice * searchDTO.getNum() ) );//订购价

	    List imagelist = pageService.imageProductList( searchDTO.getProId(), 1 );//获取轮播图列表
	    result.put( "imagelist", imagelist.get( 0 ) );
	    String is_specifica = product.getIsSpecifica().toString();
	    Map guige = new HashMap();
	    if ( is_specifica.equals( "1" ) ) {
		guige = pageService.productSpecifications( searchDTO.getProId(), searchDTO.getInvId() + "" );
	    }

	    int memType = memberService.isCardType( member.getId() );
	    if ( guige != null && guige.size() > 0 ) {
		result.put( "proSpecificaIds", guige.get( "xids" ) );
	    }

	    PayWay payWay = payService.getPayWay( loginDTO.getBusId() );
	    result.put( "payWayList", MarginUtil.getPayWay( memType, payWay, CommonUtil.judgeBrowser( request ) ) );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
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
	    //	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    result = mallPresaleDepositService.addDeposit( depositDTO, member, loginDTO.getBrowerType() );
	} catch ( BusinessException be ) {
	    return ServerResponse.createByErrorCodeMessage( be.getCode(), be.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "交纳定金异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交纳定金异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 储值卡支付成功的回调
     */
    @ApiOperation( value = "预售缴纳定金回调的接口", notes = "预售缴纳定金回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ApiModelProperty( hidden = true )
    @ResponseBody
    @PostMapping( value = "payWay" )
    public ServerResponse payWay( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) && CommonUtil.isNotEmpty( params.get( "no" ) ) ) {
		params.put( "out_trade_no", params.get( "no" ) );
	    }
	    mallPresaleDepositService.paySuccessPresale( params );
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "预售缴纳定金回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "预售缴纳定金回调异常" );
	}
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
	    coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( "6" ) );////判断活动是否已经过期

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    //	    userLogin( request, response, loginDTO );

	    MallPresaleDeposit deposit = new MallPresaleDeposit();
	    List< Integer > memberList = memberService.findMemberListByIds( member.getId() );//查询会员信息

	    if ( CommonUtil.isNotEmpty( memberList ) && memberList.size() > 0 ) {
		deposit.setOldUserIdList( memberList );
	    } else {
		deposit.setUserId( member.getId() );
	    }

	    depositList = mallPresaleDepositService.getMyPresale( deposit );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取我的定金列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取我的定金列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), depositList, true );
    }

}


