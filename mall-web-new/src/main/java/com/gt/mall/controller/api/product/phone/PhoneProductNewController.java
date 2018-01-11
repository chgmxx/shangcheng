package com.gt.mall.controller.api.product.phone;

import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.*;
import com.gt.mall.result.applet.param.AppletGroupDTO;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.applet.MallHomeAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.*;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
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
@RequestMapping( "/phoneProduct/L6tgXlBFeK/" )
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
    @Autowired
    private MallCollectService            mallCollectService;
    @Autowired
    private MallSellerMallsetService      mallSellerMallSetService;
    @Autowired
    private MallSellerService             mallSellerService;
    @Autowired
    private MallSearchKeywordService      mallSearchKeywordService;
    @Autowired
    private CoreService                   coreService;
    @Autowired
    private WxPublicUserService           wxPublicUserService;
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;

    @ApiOperation( value = "商品分类接口", notes = "商品分类接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @RequestMapping( value = "classAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< AppletGroupDTO > > classAll( HttpServletRequest request, @Valid @ModelAttribute PhoneGroupDTO params ) {
	try {
	    Map< String,Object > map = new HashMap<>();

	    if ( CommonUtil.isEmpty( params.getShopId() ) || params.getShopId() == 0 ) {
		List< Map< String,Object > > shoplist = mallStoreService.findShopByUserId( params.getBusId(), request );
		map.put( "shopList", shoplist );
	    } else {
		map.put( "shopId", params.getShopId() );
	    }
	    if ( CommonUtil.isEmpty( params.getGroupId() ) || params.getGroupId() == 0 ) {
		map.put( "isFrist", 1 );//查询父类
	    } else {
		map.put( "classId", params.getGroupId() );
	    }
	    map.put( "busId", params.getBusId() );
	    List< AppletGroupDTO > classList = mallHomeAppletService.selectGroupsByShopId( map );

	    MallRedisUtils.getMallShopId( params.getShopId() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), classList );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "查询商品分类接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品分类接口失败" );
	}

    }

    @ApiOperation( value = "商品搜索接口", notes = "搜索商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "productAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > productAll( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneSearchProductDTO params, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	try {
	    if ( CommonUtil.isNotEmpty( params.getType() ) && params.getType() > 0 ) {//判断活动是否已经过期
		coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( params.getType().toString() ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.getSearchContent() ) ) {
		params.setSearchContent( CommonUtil.urlEncode( params.getSearchContent() ) );//搜索内容转码
	    }
	    if ( CommonUtil.isEmpty( params.getCurPage() ) || params.getCurPage() == 1 ) {
		if ( params.getShopId() > 0 ) {
		    //判断店铺和门店是否已经被删除
		    boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
		    if ( !isShop ) {
			return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
		    }
		}
		//封装登陆参数
		/*loginDTO.setUcLogin( 1 );//不需要登陆
		userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断*/

		MallRedisUtils.getMallShopId( params.getShopId() );//从session获取店铺id  或  把店铺id存入session

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
	    if ( CommonUtil.isEmpty( params.getShopId() ) || params.getShopId() == 0 ) {
		List< Map< String,Object > > shoplist = mallStoreService.findShopByUserId( params.getBusId(), request );
		params.setShopList( shoplist );
	    }
	    if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( params.getSearchContent() ) ) {
		//新增搜索关键词
		mallSearchKeywordService.insertSeachKeyWord( member.getId(), params.getShopId(), params.getSearchContent() );
	    }
	    PageUtil page = null;
	    if ( params.getType() == 0 || params.getType() == 5 ) {//0 查询普通商品 5 查询粉币商品
		double discount = mallProductService.getMemberDiscount( "1", member );
		page = mallPageService.productAllListNew( params, discount, member );
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
	    } else if ( params.getType() == 8 && CommonUtil.isNotEmpty( params.getSaleMemberId() ) && params.getSaleMemberId() > 0 ) {//查询销售商品
		Integer saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, params.getSaleMemberId(), request, params.getBusId() );
		result.put( "saleMemberId", saleMemberId );

		MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( params.getSaleMemberId() );
		Map< String,Object > map = new HashMap<>();
		map.put( "busUserId", member.getBusid() );
		map.put( "groupId", params.getGroupId() );
		map.put( "curPage", params.getCurPage() );
		map.put( "proName", params.getSearchContent() );
		map.put( "type", params.getType() );
		map.put( "sort", params.getSort() );
		map.put( "isDesc", params.getIsDesc() );
		page = mallSellerMallSetService.selectProductBySaleMember( mallSet, map, member );
	    }
	    result.put( "productList", page );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "商品搜索接口失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    @ApiOperation( value = "商品信息接口", notes = "商品详情页面查询商品信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "getProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneProductDetailResult > getProduct( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneProductDetailDTO params, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	PhoneProductDetailResult result;
	Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	try {
	    if ( CommonUtil.isNotEmpty( params.getType() ) && params.getType() > 0 ) {//判断活动是否已经过期
		coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( params.getType().toString() ) );
	    }
	    //判断店铺和门店是否已经被删除
	    boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
	    if ( !isShop ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
	    }
	    //封装登陆参数
	    //	    loginDTO.setUcLogin( 1 );//不需要登陆
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    MallRedisUtils.getMallShopId( params.getShopId() );//从session获取店铺id  或  把店铺id存入session

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
	    WxPublicUsers publicUser = wxPublicUserService.selectByUserId( loginDTO.getBusId() );
	    if ( CommonUtil.isNotEmpty( publicUser ) && CommonUtil.isNotEmpty( member ) ) {
		if ( CommonUtil.isNotEmpty( publicUser.getQrcodeUrl() ) ) {
		    result.setQrcodeUrl( PropertiesUtil.getResourceUrl() + publicUser.getQrcodeUrl() );
		}
	    }
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "商品信息接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品信息失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    @ApiOperation( value = "商品规格接口", notes = "在商品详情页面弹出商品规格", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "getSpecifica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getSpecifica( HttpServletRequest request, @RequestBody @Valid @ModelAttribute PhoneSpecificaDTO params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {

	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );

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

    @ApiOperation( value = "查询商品规格和商品图片接口", notes = "在首页弹出商品规格", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "getSpecificaAndImage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getSpecificaDialog( HttpServletRequest request, @RequestBody @Valid @ModelAttribute PhoneSpecificaDTO params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {

	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );

	    MallProduct product = mallProductService.selectById( params.getProductId() );

	    if ( product.getIsSpecifica().toString().equals( "1" ) ) {

		List< Map< String,Object > > specificaList = mallProductSpecificaService.getSpecificaByProductId( params.getProductId() );//获取商品规格值
		if ( specificaList != null && specificaList.size() > 0 ) {
		    resultMap.put( "specificaList", specificaList );
		}

		List< Map< String,Object > > guigePrice = mallProductNewService.getProductSpecificaPrice( params, member );
		if ( guigePrice != null && guigePrice.size() > 0 ) {
		    resultMap.put( "guigePrice", guigePrice );
		}
	    }
	    resultMap.put( "proStockTotal", product.getProStockTotal() );
	    resultMap.put( "proPrice", product.getProPrice() );
	    resultMap.put( "busId", product.getUserId() );
	    resultMap.put( "id", product.getId() );
	    resultMap.put( "proTypeId", product.getProTypeId() );
	    if ( product.getProRestrictionNum() != null && product.getProRestrictionNum() > 0 ) {
		resultMap.put( "maxNum", product.getProRestrictionNum() );
	    }
	    List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssId( 1, 1, params.getProductId() );
	    if ( imageList != null && imageList.size() > 0 ) {
		resultMap.put( "imageObj", imageList.get( 0 ) );
	    }

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultMap );

	} catch ( Exception e ) {
	    logger.error( "查询商品规格和商品图片异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询商品规格和商品图片失败" );
	}
    }

    @ApiOperation( value = "查询商品详情接口", notes = "商品详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "getProductDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
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
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "feel", value = "评论状态 1好评 0中评 -1差评", paramType = "query", dataType = "String" ) } )
    @ResponseBody
    @PostMapping( value = "getProductComment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
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
    @PostMapping( value = "getProductParams", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
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

    @ApiOperation( value = "收藏商品的接口", notes = "商品详情页面收藏商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "productId", value = "商品id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "collectProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse collectProduct( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer productId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    mallCollectService.collectionProduct( productId, member.getId() );

	} catch ( BusinessException e ) {
	    logger.error( "收藏商品异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "收藏商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "收藏商品失败" );
	}
	return ServerResponse.createBySuccessCode();

    }

}
