package com.gt.mall.controller.api.page.phone;

import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallSearchKeyword;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.service.web.product.MallSearchLabelService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.SessionUtils;
import com.gt.util.entity.param.wx.WxJsSdk;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.wx.WxJsSdkResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城首页接口（手机端）
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 10:55
 */
@Api( value = "phonePage", description = "首页相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@RestController
@CrossOrigin
@RequestMapping( value = "/phonePage/" )
public class PhonePageController {

    private static Logger logger = LoggerFactory.getLogger( PhonePageController.class );

    @Autowired
    private MallPaySetService        mallPaySetService;
    @Autowired
    private MallPageService          mallPageService;
    @Autowired
    private MallStoreService         mallStoreService;
    @Autowired
    private MallSearchLabelService   mallSearchLabelService;
    @Autowired
    private MallSearchKeywordService mallSearchKeywordService;
    @Autowired
    private MallGroupService         mallGroupService;
    @Autowired
    private DictService              dictService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private WxPublicUserService      wxPublicUserService;

    @ApiOperation( value = "获取商家的门店列表", notes = "获取商家的门店列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/shopList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse shopList( HttpServletRequest request, int busId ) {
	try {
	    BusUser user = new BusUser();
	    user.setId( busId );
	    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
	    if ( shopList != null && shopList.size() > 0 ) {

		for ( Map< String,Object > shopMap : shopList ) {
		    //获取门店图片
		    List< WsShopPhoto > imageList = wxShopService.getShopPhotoByShopId( CommonUtil.toInteger( shopMap.get( "wxShopId" ) ) );
		    if ( imageList != null && imageList.size() > 0 ) {
			shopMap.put( "shopImage", imageList.get( 0 ).getLocalAddress() );
		    }
		    shopMap.remove( "wxShopId" );
		    shopMap.remove( "stoLongitude" );
		    shopMap.remove( "stoLatitude" );
		}
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), shopList, false );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商家的门店列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商家的门店列表失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "该商家没有门店列表" );
    }

    @ApiOperation( value = "获取商家的底部菜单", notes = "获取商家的底部菜单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/footerMenu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse footerMenu( HttpServletRequest request, int busId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( busId );//查询商城底部菜单
	    result.putAll( footerMenuMap );

	} catch ( Exception e ) {
	    logger.error( "获取商家的底部菜单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商家的底部菜单失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    @ApiOperation( value = "获取商城首页id", notes = "获取商城首页id", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺id", paramType = "query", required = false, dataType = "int" ) } )
    @PostMapping( value = "79B4DE7C/getHomePageId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getHomePageId( HttpServletRequest request, int busId, int shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    if ( shopId == 0 ) {
		//显示首页的菜单按钮，并查询首页id
		shopId = mallStoreService.getShopBySession( request.getSession(), shopId );//从session获取店铺id  或  把店铺id存入session
	    }
	    int pageId = 0;
	    if ( shopId == 0 ) {
		BusUser busUser = new BusUser();
		busUser.setId( busId );
		List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( busUser, request );
		List< Map< String,Object > > pageList = mallPageService.selectPageIdByUserId( busId, shopList );
		if ( pageList.size() > 0 ) {//获取首页的pageId
		    shopId = CommonUtil.toInteger( pageList.get( 0 ).get( "pag_sto_id" ) );
		    request.setAttribute( "pageid", pageList.get( 0 ).get( "id" ) );
		    mallStoreService.getShopBySession( request.getSession(), shopId );
		}
	    } else {
		pageId = mallPageService.getPageIdByShopId( shopId );
	    }
	    result.put( "pageId", pageId );
	} catch ( Exception e ) {
	    logger.error( "获取店铺首页id异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取店铺首页id失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    @ApiOperation( value = "获取商家的客服", notes = "获取商家的客服", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "shopId", value = "店铺id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/getCustomer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getCustomer( HttpServletRequest request, int shopId ) {
	try {
	    MallStore store = mallStoreService.selectById( shopId );
	    if ( CommonUtil.isNotEmpty( store ) ) {
		if ( CommonUtil.isNotEmpty( store.getStoQqCustomer() ) ) {
		    request.setAttribute( "qq", store.getStoQqCustomer() );
		    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), store.getStoQqCustomer(), false );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商家的客服异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商家的客服失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "该店铺没有客服" );
    }

    @ApiOperation( value = "获取店铺风格", notes = "获取店铺风格", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "店铺id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/getShopStyle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getShopStyle( HttpServletRequest request, int busId ) {
	try {
	    MallPaySet mallPaySet = mallPaySetService.selectByUserId( busId );
	    if ( CommonUtil.isNotEmpty( mallPaySet ) ) {
		if ( CommonUtil.isNotEmpty( mallPaySet.getStyleKey() ) ) {
		    String styleValue = dictService.getDictRuturnValue( "k001", CommonUtil.toInteger( mallPaySet.getStyleKey() ) );
		    if ( CommonUtil.isNotEmpty( styleValue ) ) {
			return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), styleValue.split( "," ), false );
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取店铺风格异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取店铺风格失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "该店铺没有设置风格" );
    }

    @ApiOperation( value = "查询历史搜索和推荐搜索接口", notes = "获取用户已搜索关键词", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "shopId", value = "店铺id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/searchLabel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse searchLabel( HttpServletRequest request, int shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > map = new HashMap<>();
	    map.put( "shopId", shopId );
	    List< Map< String,Object > > labelList = mallSearchLabelService.selectByUser( map );
	    if ( labelList != null && labelList.size() > 0 ) {
		result.put( "labelList", labelList );
	    }
	    if ( CommonUtil.isNotEmpty( member ) ) {
		map.put( "userId", member.getId() );
		//查询历史搜索
		List< MallSearchKeyword > keywordList = mallSearchKeywordService.selectByUser( map );
		if ( keywordList != null && keywordList.size() > 0 ) {
		    result.put( "keywordList", keywordList );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "查询历史搜索和推荐搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询历史搜索和推荐搜索接口失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 清空搜索查询的标签
     */
    @ApiOperation( value = "清空历史搜索接口", notes = "清空用户历史搜索", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "shopId", value = "店铺id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "79B4DE7C/clearSearchGroup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse clearSearchGroup( HttpServletRequest request, HttpServletResponse response, int shopId ) throws IOException {
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    Member member = SessionUtils.getLoginMember( request );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		params.put( "userId", member.getId() );
		mallGroupService.clearSearchKeyWord( params );
	    }
	} catch ( BusinessException e ) {
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "清空历史搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "清空历史搜索接口失败" );
	}
	return ServerResponse.createBySuccessCode();

    }

    /**
     * 获取微信分享
     */
    @ApiOperation( value = "微信分享接口", notes = "获取微信分享", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "url", value = "当前地址", paramType = "query", required = true, dataType = "String" ) )
    @PostMapping( value = "79B4DE7C/wxShare", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse wxShare( HttpServletRequest request, HttpServletResponse response, String url ) throws IOException {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getPublicId() ) ) {
		WxJsSdk wxJsSdk = new WxJsSdk();
		wxJsSdk.setPublicId( member.getPublicId() );
		wxJsSdk.setUrl( url );
		WxJsSdkResult result = wxPublicUserService.wxjssdk( wxJsSdk );
		if ( CommonUtil.isNotEmpty( result ) ) {
		    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "微信分享接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取微信分享接口失败" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.NO_SHARE_ERROR.getCode(), ResponseEnums.NO_SHARE_ERROR.getDesc(), false );

    }

}
