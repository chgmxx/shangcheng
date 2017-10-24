package com.gt.mall.controller.api.product.phone;

import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.controller.api.common.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.phone.*;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.applet.MallHomeAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.*;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品相关（手机端）
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:35
 */
@Api( value = "phoneProduct", description = "商品页面相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneProduct/" )
public class PhoneProductNewController extends AuthorizeOrUcLoginController {

    private static Logger logger = LoggerFactory.getLogger( PhoneProductNewController.class );

    @Autowired
    private MallPageService               mallPageService;//页面业务处理类
    @Autowired
    private MallHomeAppletService         mallHomeAppletService;
    @Autowired
    private MallStoreService              mallStoreService;
    @Autowired
    private MallProductService            mallProductService;
    @Autowired
    private MallProductDetailService      mallProductDetailService;
    @Autowired
    private MallGroupBuyService           mallGroupBuyService;
    @Autowired
    private MallSeckillService            mallSeckillService;
    @Autowired
    private MallAuctionService            mallAuctionService;
    @Autowired
    private MallPresaleService            mallPresaleService;
    @Autowired
    private MallPifaService               mallPifaService;
    @Autowired
    private MallPaySetService             mallPaySetService;
    @Autowired
    private MallProductNewService         mallProductNewService;
    @Autowired
    private BusUserService                busUserService;
    @Autowired
    private MallStoreCertificationService mallStoreCertificationService;
    @Autowired
    private MallProductSpecificaService   mallProductSpecificaService;//商品规格业务处理类
    @Autowired
    private MallCommentService            mallCommentService;//商品评价业务处理类
    @Autowired
    private MallProductParamService       mallProductParamService;//商品规格参数业务处理类
    @Autowired
    private MemberService                 memberService;

    @ApiOperation( value = "商品分类接口", notes = "商品分类接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @RequestMapping( value = "79B4DE7C/classAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< Map< String,Object > > > classAll( HttpServletRequest request, @Valid @ModelAttribute PhoneGroupDTO params ) {
	try {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "shopId", params.getShopId() );
	    if ( CommonUtil.isEmpty( params.getGroupId() ) || params.getGroupId() == 0 ) {
		map.put( "isFrist", 1 );
	    } else {
		map.put( "classId", params.getGroupId() );
	    }
	    map.put( "busId", params.getBusId() );
	    List< Map< String,Object > > classList = mallHomeAppletService.selectGroupsByShopId( map );

	    mallStoreService.getShopBySession( request.getSession(), params.getShopId() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), classList );

	} catch ( Exception e ) {
	    logger.error( "查询商品分类接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品分类接口失败" );
	}

    }

    @ApiOperation( value = "商品搜索接口", notes = "搜索商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/productAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > productAll( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneSearchProductDTO params, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	Member member = SessionUtils.getLoginMember( request );
	try {
	    if ( CommonUtil.isNotEmpty( params.getSearchContent() ) ) {
		params.setSearchContent( CommonUtil.urlEncode( params.getSearchContent() ) );//搜索内容转码
	    }
	    if ( CommonUtil.isEmpty( params.getCurPage() ) || params.getCurPage() == 1 ) {
		//判断店铺和门店是否已经被删除
		boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
		if ( !isShop ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
		}
		//封装登陆参数
		loginDTO.setUcLogin( 1 );//不需要登陆
		userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

		mallStoreService.getShopBySession( request.getSession(), params.getShopId() );//从session获取店铺id  或  把店铺id存入session

		if ( CommonUtil.isNotEmpty( params.getType() ) ) {
		    if ( params.getType() == 5 ) {
			mallProductService.setJifenByRedis( member, request, params.getType(), params.getBusId() );
		    } else {
			int isJifen = mallProductService.getJifenByRedis( member, request, params.getType(), params.getBusId() );
			if ( isJifen == 5 ) {
			    params.setType( 5 );
			}
		    }
		}
	    }
	    PageUtil page = null;
	    if ( params.getType() == 0 || params.getType() == 5 ) {//0 查询普通商品 5 查询粉币商品
		//		double discount = mallProductService.getMemberDiscount( "1", member );
		page = mallPageService.productAllListNew( params, 1, member );
	    } else if ( params.getType() == 1 ) {//查询团购商品
		page = mallGroupBuyService.searchGroupBuyProduct( params, member );
	    } else if ( params.getType() == 3 ) {//查询秒杀商品
		page = mallSeckillService.searchSeckillAll( params, member );
	    } else if ( params.getType() == 4 ) {//查询拍卖商品
		page = mallAuctionService.searchAuctionAll( params, member );
	    } else if ( params.getType() == 6 ) {//查询预售商品
		page = mallPresaleService.searchPresaleAll( params, member );
	    } else if ( params.getType() == 7 ) {//查询批发商品
		page = mallPifaService.searchPifaAll( params, member );
	    }
	    result.put( "productList", page );

	} catch ( Exception e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "商品搜索接口失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    @ApiOperation( value = "商品信息接口", notes = "商品详情页面查询商品信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneProductDetailResult > getProduct( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneProductDetailDTO params, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	PhoneProductDetailResult result;
	Member member = SessionUtils.getLoginMember( request );
	try {
	    //判断店铺和门店是否已经被删除
	    boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
	    if ( !isShop ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
	    }
	    //封装登陆参数
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, params.getBusId(), request );
	    loginMap.put( "uclogin", 1 );//不需要登陆

	    loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    mallStoreService.getShopBySession( request.getSession(), params.getShopId() );//从session获取店铺id  或  把店铺id存入session

	    MallPaySet set = new MallPaySet();
	    set.setUserId( params.getBusId() );
	    MallPaySet mallPaySet = mallPaySetService.selectByUserId( set );
	    //查询商品详情数据
	    result = mallProductNewService.selectProductDetail( params, member, mallPaySet );

	    List< Integer > memberList = new ArrayList<>();
	    if ( CommonUtil.isNotEmpty( member ) ) {
		memberList = memberService.findMemberListByIds( member.getId() );
	    }
	    //获取购物车的数量
	    int shopCartNum = mallPageService.getMemberShopCartNum( request, member, memberList );
	    if ( shopCartNum > 0 ) {
		result.setShopCartNum( shopCartNum );
	    }

	    BusUser user = busUserService.selectById( params.getBusId() );//根据商家id获取商家信息
	    if ( CommonUtil.isNotEmpty( user ) ) {
		if ( CommonUtil.isNotEmpty( user.getAdvert() ) && user.getAdvert().toString().equals( "0" ) ) {
		    result.setIsAdvert( 1 );//是否显示技术支持  1显示
		}
	    }

	    //获取商家的认证信息
	    Map< String,Object > map = mallStoreCertificationService.getStoreServiceByShopId( params.getShopId(), params.getBusId() );
	    if ( CommonUtil.isNotEmpty( map ) ) {
		if ( CommonUtil.isNotEmpty( map.get( "stoType" ) ) ) {
		    result.setStoType( map.get( "stoType" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( map.get( "categoryName" ) ) ) {
		    result.setCategoryName( map.get( "categoryName" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( map.get( "isSecuritytrade" ) ) ) {
		    result.setSecuritytrade( Boolean.valueOf( map.get( "isSecuritytrade" ).toString() ) );
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "商品信息接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品信息失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    @ApiOperation( value = "商品规格接口", notes = "在商品详情页面弹出商品规格", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getSpecifica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getSpecifica( HttpServletRequest request, @RequestBody @Valid @ModelAttribute PhoneSpecificaDTO params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {

	    Member member = SessionUtils.getLoginMember( request );

	    List< Map< String,Object > > specificaList = mallProductSpecificaService.getSpecificaByProductId( params.getProductId() );//获取商品规格值
	    resultMap.put( "specificaList", specificaList );

	    List< Map< String,Object > > guigePrice = mallProductNewService.getProductSpecificaPrice( params, member );
	    resultMap.put( "guigePrice", guigePrice );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultMap );

	} catch ( Exception e ) {
	    logger.error( "商品规格异常：" + e.getMessage() );
	    e.printStackTrace();

	    return ServerResponse.createByErrorMessage( "商品规格失败" );
	}

    }

    @ApiOperation( value = "查询商品详情接口", notes = "商品详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getProductDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< String > getProductDetail( HttpServletRequest request, int productId ) {
	try {

	    MallProductDetail mallProductDetail = mallProductDetailService.selectByProductId( productId );
	    String productDetail = "";
	    if ( CommonUtil.isNotEmpty( mallProductDetail ) ) {
		productDetail = mallProductDetail.getProductDetail();
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), productDetail, false );

	} catch ( Exception e ) {
	    logger.error( "商品详情页面查询商品参数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品参数失败" );
	}

    }

    @ApiOperation( value = "查询商品评价接口", notes = "商品详情页面查看商品评价", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "feel", value = "评论状态 1好评 0中评 -1差评", paramType = "query", dataType = "String" )
    } )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getProductComment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getProductComment( HttpServletRequest request, int busId, int productId, String feel ) {
	try {
	    Map< String,Object > resultMap = mallCommentService.getProductComment( busId, productId, feel );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultMap );

	} catch ( Exception e ) {
	    logger.error( "商品详情页面查看商品评价异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查看商品评价失败" );
	}

    }

    @ApiOperation( value = "查询商品参数接口", notes = "商品详情页面查询商品参数", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "79B4DE7C/getProductParams", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< MallProductParam > > getProductParams( HttpServletRequest request, int productId ) {
	try {

	    List< MallProductParam > paramsList = mallProductParamService.getParamByProductId( productId );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), paramsList, false );

	} catch ( Exception e ) {
	    logger.error( "商品详情页面查询商品参数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品参数失败" );
	}

    }

}
