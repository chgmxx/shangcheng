package com.gt.mall.controller.api.auction.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.auction.PhoneAddAuctionBiddingDTO;
import com.gt.mall.param.phone.auction.PhoneAddAuctionMarginDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.auction.MallAuctionOfferService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.MarginUtil;
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
 * 拍卖管理 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "phoneAuction", description = "拍卖管理页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneAuction/L6tgXlBFeK/" )
public class PhoneAuctionController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallAuctionService        auctionService;
    @Autowired
    private MallPageService           pageService;
    @Autowired
    private MallAuctionMarginService  auctionMarginService;
    @Autowired
    private MallAuctionBiddingService auctionBiddingService;
    @Autowired
    private MallAuctionOfferDAO       auctionOfferDAO;
    @Autowired
    private MallAuctionBiddingDAO     auctionBiddingDAO;
    @Autowired
    private MallAuctionOfferService   auctionOfferService;
    @Autowired
    private MemberService             memberService;
    @Autowired
    private MallProductService        mallProductService;

    /*交纳保证金接口*/
    @ApiOperation( value = "获取交纳保证金信息", notes = "获取交纳保证金信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "invId", value = "库存ID", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "proId", value = "商品ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "auctionId", value = "拍卖ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "toAddMargin", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > toAddMargin( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer proId, Integer invId, Integer auctionId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    //	    Map mapmessage = pageService.querySelct( proId );//获取商品信息
	    MallProduct product = mallProductService.selectById( proId );//获取商品信息

	    int shopid = 0;
	    if ( CommonUtil.isNotEmpty( product.getShopId() ) ) {
		shopid = product.getShopId();
		MallRedisUtils.getMallShopId( shopid );
	    }

	    //查询商品的拍卖信息
	    MallAuction auction = auctionService.getAuctionByProId( proId, shopid, auctionId );
	    result.put( "aucMargin", auction.getAucMargin() );
	    result.put( "product", product );

	    //	    List imagelist = pageService.imageProductList( proId, 1 );//获取轮播图列表
	    //	    result.put( "imagelist", imagelist.get( 0 ) );

	    String is_specifica = product.getIsSpecifica().toString();
	    Map guige = new HashMap();
	    if ( is_specifica.equals( "1" ) ) {
		guige = pageService.productSpecifications( proId, invId + "" );
	    }

	    int memType = memberService.isCardType( member.getId() );
	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( CommonUtil.judgeBrowser( request ) == 1 ) {
		isWxPay = 1;//可以微信支付
	    } else {
		isAliPay = 1;//可以支付宝支付
	    }
	    result.put( "payWayList", MarginUtil.getPayWay( isWxPay, isAliPay, memType ) );
	    result.put( "proSpecificaIds", guige.get( "xids" ) );
	} catch ( Exception e ) {
	    logger.error( "获取交纳保证金信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取交纳保证金信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 交保证金
     */
    @ApiOperation( value = "交纳保证金", notes = "交纳保证金" )
    @ResponseBody
    @RequestMapping( value = "addMargin", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > addMargin( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    PhoneAddAuctionMarginDTO marginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    result = auctionMarginService.addMargin( marginDTO, member );
	}catch ( BusinessException be ) {
	    return ServerResponse.createByErrorCodeMessage( be.getCode(), be.getMessage() );
	}  catch ( Exception e ) {
	    logger.error( "兑换积分异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "兑换积分异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 储值卡支付成功的回调
     */
    @ApiModelProperty( hidden = true )
    @GetMapping( value = "payWay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse payWay( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) && CommonUtil.isNotEmpty( params.get( "no" ) ) ) {
		params.put( "out_trade_no", params.get( "no" ) );
	    }
	    auctionMarginService.paySuccessAuction( params );
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "拍卖缴纳定金回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "拍卖缴纳定金回调异常" );
	}
    }

    /*抢拍记录 出价记录*/
    @ApiOperation( value = "获取出价记录列表", notes = "获取出价记录列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "auctionId", value = "拍卖ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "shopdetails", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > shopdetails( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer auctionId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );
	    MallAuction auction = auctionService.selectById( auctionId );
	    //	    MallAuction auction = auctionService.getAuctionByProId( productId, shopId, auctionId );
	    /*if ( type == 1 ) {//商品详情
		MallProductDetail obj = pageService.shopdetails( auction.getProductId() );
		result.put( "proDetail", obj );
	    } else if ( type == 2 ) {*/
	    if ( auction != null ) {
		MallAuctionBidding bid = new MallAuctionBidding();
		bid.setAucId( auction.getId() );
		if ( auction.getAucType().toString().equals( "2" ) ) {//升价拍
		    //查询出价次数
		    MallAuctionOffer offer = new MallAuctionOffer();
		    offer.setAucId( auction.getId() );
		    List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( offer );//查询拍卖的出价信息
		    for ( MallAuctionOffer offer1 : offerList ) {
			Member member1 = memberService.findMemberById( offer.getUserId(), null );
			if ( member1 != null ) {
			    offer1.setNickname( member1.getNickname() );
			}
		    }
		    result.put( "offerList", offerList );
		} else {//降价拍
		    List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );//查询用户的竞拍信息
		    result.put( "bidList", bidList );
		}
	    }
	    /*}*/
	    if ( auction != null ) {
		result.put( "aucType", auction.getAucType() );
	    }

	    MallRedisUtils.getMallShopId( auction.getShopId() );
	} catch ( Exception e ) {
	    logger.error( "获取出价记录列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取出价记录列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 拍卖出价
     */
    @ApiOperation( value = "拍卖出价", notes = "拍卖出价" )
    @ResponseBody
    @RequestMapping( value = "addOffer", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > addOffer( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    PhoneAddAuctionBiddingDTO biddingDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    result = auctionOfferService.addOffer( biddingDTO, member.getId().toString() );
	    Boolean flag = (Boolean) result.get( "result" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), result.get( "msg" ).toString() );
	    }
	} catch ( Exception e ) {
	    logger.error( "拍卖出价异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "拍卖出价异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    @ApiOperation( value = "获取我的保证金列表", notes = "获取我的保证金列表" )
    @ResponseBody
    @RequestMapping( value = "myMarginList", method = RequestMethod.POST )
    public ServerResponse myMarginList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	List< MallAuctionMargin > marginList = null;
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    MallAuctionMargin margin = new MallAuctionMargin();

	    List< String > oldUserIdList = new ArrayList< String >();
	    if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
		for ( String oldMemberId : member.getOldid().split( "," ) ) {
		    if ( CommonUtil.isNotEmpty( oldMemberId ) ) {
			oldUserIdList.add( oldMemberId );
		    }
		}
		margin.setOldUserIdList( oldUserIdList );
	    } else {
		margin.setUserId( member.getId() );
	    }
	    marginList = auctionMarginService.getMyAuction( margin );

	} catch ( Exception e ) {
	    logger.error( "获取我的保证金列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取我的保证金列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), marginList, true );
    }

    @ApiOperation( value = "获取我的竞拍列表", notes = "获取我的竞拍列表" )
    @ResponseBody
    @RequestMapping( value = "myBiddingList", method = RequestMethod.POST )
    public ServerResponse myBiddingList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	List< Map< String,Object > > bidList = null;
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    bidList = auctionBiddingService.selectMyBidding( member );

	} catch ( Exception e ) {
	    logger.error( "获取我的竞拍列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取我的竞拍列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), bidList, true );
    }

    @ApiOperation( value = "获取我的获拍金列表", notes = "获取我的获拍金列表" )
    @ResponseBody
    @RequestMapping( value = "myHuoPaiList", method = RequestMethod.POST )
    public ServerResponse myHuoPaiList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	List< Map< String,Object > > bidList = null;
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    bidList = auctionBiddingService.selectMyHuoBid( member );

	} catch ( Exception e ) {
	    logger.error( "获取我的获拍金列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取我的获拍金列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), bidList, true );
    }
}


