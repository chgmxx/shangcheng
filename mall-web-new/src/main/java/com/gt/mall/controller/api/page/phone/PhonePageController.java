package com.gt.mall.controller.api.page.phone;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.SessionUtils;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallSearchKeyword;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.result.phone.PhoneCommonResult;
import com.gt.mall.result.phone.PhonePageResult;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallVisitorService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.service.web.product.MallSearchLabelService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.wx.WxJsSdk;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
@RequestMapping( value = "/phonePage/L6tgXlBFeK/" )
public class PhonePageController extends AuthorizeOrUcLoginController {

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
    private WxPublicUserService      wxPublicUserService;
    @Autowired
    private MallCommonService        mallCommonService;
    @Autowired
    private MallSellerService        mallSellerService;
    @Autowired
    private MallPifaApplyService     mallPifaApplyService;
    @Autowired
    private MallProductDAO           mallProductDAO;
    @Autowired
    private MallVisitorService       mallVisitorService;

    /**
     * 手机访问商家主页面接口
     */
    @ApiOperation( value = "获取商城首页数据", notes = "获取商城首页数据", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "pageId", value = "首页id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "url", value = "当前页面地址", paramType = "query", dataType = "String" ) } )
    @GetMapping( value = "pageIndex", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhonePageResult > pageIndex( HttpServletRequest request, HttpServletResponse response, Integer pageId, String url ) throws IOException {
	PhonePageResult result = new PhonePageResult();
	try {
	    //根据页面id查询页面信息
	    MallPage page = mallPageService.selectById( pageId );
	    if ( CommonUtil.isEmpty( page ) ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "页面已删除" );
	    }
	    int userid = page.getPagUserId();
	    //从session中获取member信息
	    Member member = SessionUtils.getLoginMember( request, page.getPagUserId() );
	    //根据商家id获取公众号信息
	    WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( page.getPagUserId() );
	    MallStore mallStore = mallStoreService.findShopByShopId( page.getPagStoId() );

	    PhoneLoginDTO loginDTO = new PhoneLoginDTO();
	    loginDTO.setBusId( page.getPagUserId() );
	    loginDTO.setUcLogin( 1 );
	    loginDTO.setUrl( CommonUtil.isNotEmpty( url ) ? url : CommonUtil.getpath( request ) );
	    loginDTO.setBrowerType( CommonUtil.judgeBrowser( request ) );
	    userLogin( request, response, loginDTO );
	    mallSellerService.clearSaleMemberIdByRedis( member, request, userid );

	    if ( CommonUtil.isEmpty( mallStore ) || CommonUtil.isEmpty( mallStore.getIsDelete() ) || "1".equals( mallStore.getIsDelete().toString() ) ) {
		throw new BusinessException( ResponseEnums.SHOP_NULL_ERROR.getCode(), ResponseEnums.SHOP_NULL_ERROR.getDesc() );
	    }
	    String headImg = "";
	    String http = PropertiesUtil.getResourceUrl();
	    if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		if ( CommonUtil.isNotEmpty( wxPublicUsers.getHeadImg() ) ) {
		    headImg = wxPublicUsers.getHeadImg();
		}
	    }
	    if ( CommonUtil.isEmpty( headImg ) ) {
		if ( CommonUtil.isNotEmpty( mallStore.getStoHeadImg() ) ) {
		    headImg = http + mallStore.getStoHeadImg();
		}
	    }

	    String dataJson = "[]";
	    String picJson = "[]";
	    if ( page.getPagData() != null ) {
		//		MallPaySet set = new MallPaySet();
		//		set.setUserId( userid );
		//		set = mallPaySetService.selectByUserId( set );
		//		int state = mallPifaApplyService.getPifaApplay( member, set );

		net.sf.json.JSONArray jsonobj = net.sf.json.JSONArray.fromObject( page.getPagData() );//转换成JSON数据
		net.sf.json.JSONArray XinJson = new net.sf.json.JSONArray();//获取新的数组对象
		for ( int i = 0; i < jsonobj.size(); i++ ) {
		    net.sf.json.JSONArray XinidJSon = new net.sf.json.JSONArray();
		    if ( CommonUtil.isEmpty( jsonobj.get( i ) ) ) {
			XinJson.add( "null" );
			continue;
		    } else {
			if ( jsonobj.get( i ).equals( "null" ) ) {
			    XinJson.add( "null" );
			    continue;
			}
		    }
		    Map< String,Object > map1 = (Map) jsonobj.get( i );
		    //		    logger.error( "map" + JSONObject.toJSON( map1 ) );
		    if ( CommonUtil.isEmpty( map1.get( "imgID" ) ) ) {
			if ( map1.get( "type" ).toString().equals( "7" ) ) {
			    map1.put( "stoName", mallStore.getStoName() );
			    map1.put( "headImg", headImg );
			    XinJson.add( map1 );
			}
			continue;
		    }
		    String imaid = map1.get( "imgID" ).toString();
		    String type = map1.get( "type" ).toString();//如果type==1 代表来自与页面商品信息，2代表轮播图里面的信息
		    net.sf.json.JSONArray jsonobjX = net.sf.json.JSONArray.fromObject( imaid );//转换成JSON数据
		    for ( int j = 0; j < jsonobjX.size(); j++ ) {
			if ( CommonUtil.isNotEmpty( jsonobjX.get( j ) ) ) {
			    if ( jsonobjX.get( j ).toString().equals( "null" ) ) {
				continue;
			    }
			    Map< String,Object > map2 = (Map) jsonobjX.get( j );
			    Object selecttype = map2.get( "selecttype" );
			    Boolean res = true;
			    if ( selecttype != null ) {
				Object mapid = map2.get( "id" );
				if ( selecttype == "2" || selecttype.equals( "2" ) ) {
				    if ( mapid != null && !mapid.equals( null ) && !mapid.equals( "" ) ) {
					Integer mapid1 = Integer.valueOf( mapid.toString() );
					if ( !mapid1.toString().equals( "-1" ) && !mapid1.toString().equals( "-2" ) ) {
					    try {
						Map< String,Object > map3 = mallPageService.selectBranch( mapid1 );
						map2.put( "title", map3.get( "pag_name" ) );
					    } catch ( Exception e ) {
						map2.remove( "url" );
					    }
					}
				    }
				} else if ( selecttype == "6" || selecttype.equals( "6" ) ) {//预售商品
				    map2 = mallPageService.getProductPresale( map2, member );
				}
			    }
			    //res == true 时，代表商品正常，res == false 代表商品已删除
			    if ( res ) {
				XinidJSon.add( map2 );
			    }
			}
		    }
		    map1.put( "imgID", XinidJSon );
		    XinJson.add( map1 );

		}
		picJson = XinJson.toString();
	    }
	    if ( page.getPagCss() != null ) {
		dataJson = page.getPagCss();
	    }

	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "shopId", page.getPagStoId() );
	    int countproduct = mallProductDAO.selectCountAllByShopids( params );
	    result.setCountProduct( countproduct );
	    //	    Date dat = DateTimeKit.addHours( DateTimeKit.getNow(), -168 );
	    //	    String time = DateTimeKit.getDateTime( dat, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    //	    int xincound = mallPageService.counttime( page.getPagStoId(), time );
	    //	    Integer mainid = 0;
	    //	    List list = mallPageService.pagecountid( page.getPagStoId() );
	    //	    if ( list.size() > 0 ) {
	    //		Map map = (Map) list.get( 0 );
	    //		mainid = Integer.valueOf( map.get( "id" ).toString() );
	    //	    }
	    result.setDataJson( dataJson );
	    result.setPicJson( picJson );

	    MallRedisUtils.getMallShopId( page.getPagStoId() );//从session获取店铺id  或  把店铺id存入session

	    if ( CommonUtil.isEmpty( mallStore.getStoPicture() ) ) {
		result.setStoPicture( PropertiesUtil.getResourceUrl() + mallStore.getStoPicture() );
	    }
	    result.setBusId( page.getPagUserId() );
	    result.setShareTitle( mallStore.getStoName() + "-" + page.getPagName() );
	    result.setStoName( mallStore.getStoName() );
	    result.setSharePicture( headImg );
	    result.setPageName( page.getPagName() );
	    result.setShopId( page.getPagStoId() );
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "访问商城首页异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "访问商城首页异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "获取商家的底部菜单", notes = "获取商家的底部菜单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "footerMenu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
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
    @PostMapping( value = "getHomePageId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getHomePageId( HttpServletRequest request, int busId, int shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    if ( shopId == 0 ) {
		//显示首页的菜单按钮，并查询首页id
		shopId = MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session
	    }
	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    int saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, 0, request, busId );
	    if ( saleMemberId > 0 ) {
		result.put( "saleMemberId", saleMemberId );
	    } else {
		int pageId = 0;
		if ( shopId == 0 ) {
		    BusUser busUser = new BusUser();
		    busUser.setId( busId );
		    List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( busUser, request );
		    List< Map< String,Object > > pageList = mallPageService.selectPageIdByUserId( busId, shopList );
		    if ( pageList.size() > 0 ) {//获取首页的pageId
			shopId = CommonUtil.toInteger( pageList.get( 0 ).get( "pag_sto_id" ) );
			if ( CommonUtil.isNotEmpty( pageList.get( 0 ).get( "id" ) ) ) {
			    pageId = CommonUtil.toInteger( pageList.get( 0 ).get( "id" ) );
			}
			MallRedisUtils.getMallShopId( shopId );
		    }
		} else {
		    pageId = mallPageService.getPageIdByShopId( shopId );
		}
		result.put( "pageId", pageId );
	    }

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
    @PostMapping( value = "getCustomer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneCommonResult > getCustomer( HttpServletRequest request, int shopId ) {
	PhoneCommonResult result = new PhoneCommonResult();
	try {

	    MallStore store = mallStoreService.selectById( shopId );
	    if ( CommonUtil.isNotEmpty( store ) ) {
		if ( CommonUtil.isNotEmpty( store.getStoQqCustomer() ) ) {
		    result.setQq( store.getStoQqCustomer() );
		}
	    }
	    boolean isAdvert = mallCommonService.busIsAdvert( store.getStoUserId() );
	    if ( isAdvert ) {
		result.setIsAdvert( 1 );
	    }
	    MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session
	} catch ( Exception e ) {
	    logger.error( "获取商家的客服异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商家的客服失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    @ApiOperation( value = "获取店铺风格", notes = "获取店铺风格", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "店铺id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "getShopStyle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getShopStyle( HttpServletRequest request, int busId ) {
	try {
	    MallRedisUtils.setUserId( busId );
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
    @PostMapping( value = "searchLabel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse searchLabel( HttpServletRequest request, int shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );
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
	    MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session
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
    @PostMapping( value = "clearSearchGroup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse clearSearchGroup( HttpServletRequest request, HttpServletResponse response, int shopId ) throws IOException {
	try {
	    if ( shopId == 0 ) {
		//	        return se
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		params.put( "userId", member.getId() );
		mallGroupService.clearSearchKeyWord( params );
	    }
	    MallRedisUtils.getMallShopId( shopId );//从session获取店铺id  或  把店铺id存入session
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
    @ApiOperation( value = "微信分享接口", notes = "获取微信分享", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "url", value = "当前地址", paramType = "query", required = true, dataType = "String" ) )
    @GetMapping( value = "wxShare", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse wxShare( HttpServletRequest request, HttpServletResponse response, String url ) throws IOException {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );
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
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), false );
    }

    @ApiOperation( value = "上传图片接口", notes = "上传图片", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "uploadImage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< String > uploadImage( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    int busId = CommonUtil.toInteger( request.getParameter( "busId" ) );
	    //	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    StringBuffer imageUrl = new StringBuffer();
	    boolean flag = false;
	    logger.info( request.getParameter( "busId" ) );
	    if ( request instanceof MultipartHttpServletRequest ) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List< MultipartFile > userfile = multipartRequest.getFiles( "file" );
		if ( CommonUtil.isNotEmpty( userfile ) && userfile.size() != 0 ) {
		    for ( MultipartFile file : userfile ) {
			Map< String,Object > returnMap = CommonUtil.fileUploadByBusUser( file, busId );
			logger.info( "returnMap" + JSONObject.toJSONString( returnMap ) );
			if ( "1".equals( returnMap.get( "reTurn" ) ) ) {
			    if ( CommonUtil.isNotEmpty( imageUrl ) && imageUrl.length() > 0 ) {
				imageUrl.append( "," );
			    }
			    imageUrl.append( returnMap.get( "message" ) );
			    flag = true;

			} else {
			    flag = false;
			    break;
			}
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( imageUrl ) && flag ) {
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), imageUrl.toString() );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "上传图片接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "上传图片接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "上传图片失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "上传图片失败" );
    }

    @ApiOperation( value = "获取店铺ID", notes = "获取店铺ID", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ) )
    @PostMapping( value = "getShopId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getShopId( HttpServletRequest request, int busId ) {
	Integer shopId = 0;
	try {
	    shopId = MallRedisUtils.getShopId();
	    if ( shopId == 0 ) {
		BusUser user = new BusUser();
		user.setId( busId );
		List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, request );
		if ( shopList != null && shopList.size() > 0 ) {
		    shopId = CommonUtil.toInteger( shopList.get( 0 ).get( "id" ) );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取店铺ID异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取店铺ID失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), shopId, false );
    }

    @ApiOperation( value = "新增浏览量", notes = "新增浏览量", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "id", value = "页面ID/商品ID,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "0页面 1商品,必传", paramType = "query", required = true, dataType = "int" ) } )
    @PostMapping( value = "addViewsNum", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse addVisitor( HttpServletRequest request, Integer busId, Integer id, Integer type ) {
	try {
	    Integer memberId = null;
	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    if ( member != null ) {
		memberId = member.getId();
	    }
	    Boolean result = false;
	    if ( type == 0 ) {
		result = mallVisitorService.savePageVisitor( CommonUtil.getIpAddr( request ), memberId, id );
	    } else {
		result = mallVisitorService.saveProductVisitor( CommonUtil.getIpAddr( request ), memberId, id );
	    }
	    if ( !result ) {
		return ServerResponse.createByErrorMessage( "新增浏览量失败" );
	    }
	} catch ( Exception e ) {
	    logger.error( "新增浏览量异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "新增浏览量失败" );
	}
	return ServerResponse.createBySuccessCode();
    }
}
