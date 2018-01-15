package com.gt.mall.controller.page;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import com.gt.util.entity.param.wx.WxJsSdk;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.wx.WxJsSdkResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * <p>
 * 页面表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallPage" )
public class MallPageController extends AuthorizeOrLoginController {

    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallPageService             mallPageService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private MallFreightService          mallFreightService;
    @Autowired
    private MallPifaApplyService        mallPifaApplyService;
    @Autowired
    private MallSellerService           sellerService;
    @Autowired
    private MallSellerMallsetService    mallSellerMallsetService;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallProductDAO              mallProductDAO;
    @Autowired
    private MallCollectService          mallCollectService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private DictService                 dictService;
    @Autowired
    private WxShopService               wxShopService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private FenBiFlowService            fenBiFlowService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MemberAddressService        memberAddressService;

    @RequestMapping( "/index" )
    public String res_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//是管理员
	    if ( isAdminFlag ) {
		PageUtil page = mallPageService.findByPage( params, user, request );
		request.setAttribute( "page", page );
		request.setAttribute( "pagName", params.get( "pagName" ) );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	    } else {
		request.setAttribute( "isNoAdminFlag", 1 );
	    }
	    request.setAttribute( "urls", request.getHeader( "Referer" ) );
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "78" ) );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "/mall/page/index";
    }

    /**
     * 进入编辑页面
     */
    @RequestMapping( "/to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	BusUser user = SessionUtils.getLoginUser( request );
	if ( CommonUtil.isEmpty( params.get( "id" ) ) ) {
	    request.setAttribute( "pageTitle", "添加信息" );

	} else {
	    MallPage pag = mallPageService.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	    request.setAttribute( "pag", pag );
	    request.setAttribute( "pageTitle", "编辑信息" );
	}
	//获取用户店铺集合
	List< Map< String,Object > > allSto = mallStoreService.findAllStoByUser( user, request );
	//获取页面类型
	List< Map > typeMap = dictService.getDict( "1073" );
	request.setAttribute( "typeMap", typeMap );
	request.setAttribute( "allSto", allSto );
	request.setAttribute( "urls", request.getHeader( "Referer" ) );
	return "/mall/page/edit";
    }

    /**
     * 保存或修改页面信息
     */
    @SysLogAnnotation( description = "页面管理-保存页面信息", op_function = "2" )
    @RequestMapping( "/saveOrUpdate" )
    public void saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    MallPage page = com.alibaba.fastjson.JSONObject.parseObject( params.get( "obj" ).toString(), MallPage.class );
	    page.setPagUserId( SessionUtils.getLoginUser( request ).getId() );
	    page.setPagCreateTime( new Date() );
	    msg = mallPageService.saveOrUpdate( page, user );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 删除页面信息
     */
    @SysLogAnnotation( description = "页面管理-删除页面信息", op_function = "4" )
    @RequestMapping( "/delete" )
    public void delete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    String ids[] = params.get( "ids" ).toString().split( "," );
	    msg = mallPageService.delete( ids );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 设为主页
     */
    @SysLogAnnotation( description = "页面管理-设为主页", op_function = "2" )
    @RequestMapping( "/setMain" )
    public void setMain( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    Integer id = CommonUtil.toInteger( params.get( "id" ) );
	    Integer shopid = CommonUtil.toInteger( params.get( "shopid" ) );
	    msg = mallPageService.setMain( id, shopid );
	} catch ( Exception e ) {
	    msg.put( "result", false );
	    msg.put( "message", e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, msg );
	}
    }

    /**
     * 进入页面设计
     */
    @RequestMapping( "/designPage" )
    public String designPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	String jsp = "mall/page/editor/index";
	try {
	    //获取轮播选择类型
	    Map< String,Object > lbMap = new HashMap<>();
	    lbMap.put( "1", "商品" );
	    lbMap.put( "2", "分类" );
	    request.setAttribute( "lbMap", JSONObject.fromObject( lbMap ).toString() );
	    Integer stoId = CommonUtil.toInteger( params.get( "id" ) );//获取商家设计页面
	    String dataJson = "[]";
	    String picJson = "[]";
	    BusUser obj = SessionUtils.getLoginUser( request );//获取登录人信息
	    Map< String,Object > map = mallPageService.select( stoId, obj.getId() );
	    if ( map.get( "pag_css" ) != null ) {
		dataJson = map.get( "pag_css" ).toString();

	    }
	    //根据商品id获取商品最新的数据
	    if ( map.get( "pag_data" ) != null ) {
		MallPaySet set = new MallPaySet();
		set.setIsPf( 1 );
		set.setIsPfCheck( 1 );
		String http = PropertiesUtil.getResourceUrl();
		JSONArray jsonobj = JSONArray.fromObject( map.get( "pag_data" ).toString() );//转换成JSON数据
		JSONArray XinJson = new JSONArray();//获取新的数组对象
		for ( int i = 0; i < jsonobj.size(); i++ ) {
		    JSONArray XinidJSon = new JSONArray();
		    if ( CommonUtil.isEmpty( jsonobj.get( i ) ) ) {
			XinJson.add( "null" );
			continue;
		    } else {
			if ( jsonobj.get( i ).equals( "null" ) ) {
			    XinJson.add( "null" );
			    continue;
			}
		    }
		    Map< String,Object > map1 = (Map< String,Object >) jsonobj.get( i );
		    if ( CommonUtil.isEmpty( map1.get( "imgID" ) ) ) {
			continue;
		    }
		    String imaid = map1.get( "imgID" ).toString();
		    String type = map1.get( "type" ).toString();//如果type==1 代表来自与页面商品信息，2代表轮播图里面的信息
		    JSONArray jsonobjX = JSONArray.fromObject( imaid );//转换成JSON数据
		    for ( int j = 0; j < jsonobjX.size(); j++ ) {
			if ( CommonUtil.isNotEmpty( jsonobjX.get( j ) ) ) {
			    if ( jsonobjX.get( j ).toString().equals( "null" ) ) {
				continue;
			    }
			    Map< String,Object > map2 = (Map< String,Object >) jsonobjX.get( j );
			    Object selecttype = map2.get( "selecttype" );
			    Boolean res = true;
			    if ( selecttype != null ) {
				if ( selecttype == "2" || selecttype.equals( "2" ) ) {
				    Object mapid = map2.get( "id" );
				    if ( mapid != null && !mapid.equals( null ) && !mapid.equals( "" ) ) {
					Integer mapid1 = Integer.valueOf( mapid.toString() );
					if ( !mapid1.toString().equals( "-1" ) && !mapid1.toString().equals( "-2" ) ) {
					    try {
						Map map3 = mallPageService.selectBranch( mapid1 );
						map2.put( "title", map3.get( "pag_name" ) );
					    } catch ( Exception e ) {
						e.printStackTrace();
						map2.remove( "url" );//发现分类页被删除时，移除url
					    }
					}

				    }
				} else if ( selecttype == "6" || selecttype.equals( "6" ) ) {//预售商品
				    map2 = mallPageService.getProductPresale( map2, null );
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
	    request.setAttribute( "dataJson", dataJson );
	    request.setAttribute( "picJson", picJson );
	    request.setAttribute( "stoId", stoId );
	    request.setAttribute( "shopid", map.get( "pag_sto_id" ).toString() );

	    String headImg = "";
	    Map wxPubMap = mallPageService.wxpublicid( stoId );//根据页面id查询商家公众号
	    if ( CommonUtil.isNotEmpty( wxPubMap ) ) {
		if ( CommonUtil.isNotEmpty( wxPubMap.get( "head_img" ) ) ) {
		    headImg = wxPubMap.get( "head_img" ).toString();
		}
	    }

	    if ( CommonUtil.isNotEmpty( map.get( "pag_sto_id" ) ) ) {
		Map storeMap = mallPageService.shopmessage( CommonUtil.toInteger( map.get( "pag_sto_id" ) ), null );//获取店铺信息

		String name = "";//店铺名称
		if ( CommonUtil.isEmpty( storeMap.get( "business_name" ) ) ) {
		    name = storeMap.get( "sto_name" ).toString();
		} else {
		    name = storeMap.get( "business_name" ).toString();
		}
		//店铺图片
		if ( CommonUtil.isEmpty( storeMap.get( "stoPicture" ) ) ) {
		    storeMap.put( "stoPicture", storeMap.get( "sto_qr_code" ).toString() );
		}

		if ( CommonUtil.isEmpty( headImg ) ) {
		    if ( CommonUtil.isNotEmpty( storeMap ) ) {
			if ( CommonUtil.isNotEmpty( storeMap.get( "sto_head_img" ) ) ) {
			    headImg = PropertiesUtil.getResourceUrl() + storeMap.get( "sto_head_img" ).toString();
			}
		    }
		}

		request.setAttribute( "stoName", name );
		request.setAttribute( "stoPicture", PropertiesUtil.getResourceUrl() + storeMap.get( "stoPicture" ) );
	    }
	    params.put( "shopId", map.get( "pag_sto_id" ) );
	    int countproduct = mallProductDAO.selectCountAllByShopids( params );
	    request.setAttribute( "countproduct", countproduct );

	    request.setAttribute( "headImg", headImg );
	    request.setAttribute( "userid", obj.getId() );

	} catch ( Exception e ) {
	    e.printStackTrace();
	    jsp = "error/404";
	}
	return jsp;
    }

    /**
     * 进入选择商品
     */
    @RequestMapping( "/choosePro" )
    public String choosePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    String http = PropertiesUtil.getResourceUrl();
	    //获取店铺下的分类信息
	    Integer stoId = CommonUtil.toInteger( params.get( "stoId" ) );//stoid代表商铺id
			/*	List<Map<String, Object>> groLs = mallStoreService.findGroupByStore(stoId);*/
	    List groLs = mallPageService.storeList( stoId, 0, 0 );
	    String check = request.getParameter( "check" ).toString();
	    //获取店铺下的商品
			/*	params.remove("stoId");*/
	    PageUtil page = mallPageService.product( params );
	    String url = PropertiesUtil.getHomeUrl();

	    request.setAttribute( "page", page );
	    request.setAttribute( "groLs", groLs );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "check", check );
	    request.setAttribute( "stoId", stoId );
	    request.setAttribute( "http", http );
	    request.setAttribute( "url", url );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "/mall/page/choosePro";
    }

    /**
     * 页面保存
     */
    @SysLogAnnotation( description = "页面管理-页面保存", op_function = "2" )
    @RequestMapping( "/savepage" )
    public void savePage( HttpServletRequest request, HttpServletResponse response, MallPage obj ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    mallPageService.updateById( obj );
	    map.put( "error", "0" );//操作成功
	    map.put( "message", "保存成功" );//操作成功
	} catch ( Exception e ) {
	    map.put( "error", "1" );//操作失败，数据异常
	    map.put( "message", "保存失败，请重新保存" );//操作成功
	    e.printStackTrace();
	}
	CommonUtil.write( response, map );
    }

    /**
     * 保存加预览功能
     */
    @RequestMapping( "/ylpage" )
    public String ylPage( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Integer id = Integer.valueOf( request.getParameter( "id" ).toString() );
	MallPage page = mallPageService.selectById( id );
	String http = PropertiesUtil.getResourceUrl();
	request.setAttribute( "http", http );
	request.setAttribute( "codeUrl", page.getCodeUrl() );
	return "/mall/editor/yl";
    }

    @RequestMapping( "{id}/79B4DE7C/viewHomepage" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String viewHomepage( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws Exception {
	int userid = 0;
	Member member = SessionUtils.getLoginMember( request );
	MallPage obj = mallPageService.select( id );//根据页面id查询页面信息
	if ( CommonUtil.isNotEmpty( obj ) ) {
	    userid = obj.getPagUserId();
	    request.setAttribute( "userid", userid );
	}
	Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	loginMap.put( "uclogin", 1 );
	String returnUrl = userLogin( request, response, loginMap );
	if ( CommonUtil.isNotEmpty( returnUrl ) ) {
	    return returnUrl;
	}
	sellerService.clearSaleMemberIdByRedis( member, request, userid );
	mallProductService.clearJifenByRedis( member, request, userid );
	mallProductService.clearIsShopSession( obj.getPagStoId(), userid, request );

	return "redirect:/mallPage/" + id + "/79B4DE7C/pageIndex.do";
    }

    /**
     * 手机访问商家主页面接口
     */
    @SuppressWarnings( "unchecked" )
    @RequestMapping( "{id}/79B4DE7C/pageIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String pageIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws IOException {
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    int userid = 0;
	    Member member = SessionUtils.getLoginMember( request );
	    Map wxPubMap = mallPageService.wxpublicid( id );//根据页面id查询商家公众号
	    MallPage obj = mallPageService.select( id );//根据页面id查询页面信息
	    Map storeMap = mallPageService.shopmessage( obj.getPagStoId(), null );//获取店铺信息
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		userid = obj.getPagUserId();
		request.setAttribute( "userid", userid );
	    }

	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    sellerService.clearSaleMemberIdByRedis( member, request, userid );

	    boolean isShop = mallPageService.wxShopIsDelete( obj.getPagStoId(), null );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String dataJson = "[]";
	    String picJson = "[]";
	    if ( CommonUtil.isEmpty( storeMap.get( "stoPicture" ) ) ) {
		storeMap.put( "stoPicture", storeMap.get( "sto_qr_code" ).toString() );
	    }
	    String headImg = "";
	    String http = PropertiesUtil.getResourceUrl();
	    if ( CommonUtil.isNotEmpty( wxPubMap ) ) {
		if ( CommonUtil.isNotEmpty( wxPubMap.get( "head_img" ) ) ) {
		    headImg = wxPubMap.get( "head_img" ).toString();
		}
	    }
	    if ( CommonUtil.isEmpty( headImg ) ) {
		if ( CommonUtil.isNotEmpty( storeMap ) ) {
		    if ( CommonUtil.isNotEmpty( storeMap.get( "sto_head_img" ) ) ) {
			headImg = http + storeMap.get( "sto_head_img" ).toString();
		    }
		}
	    }
	    request.setAttribute( "headImg", headImg );
	    String name = "";//店铺名称
	    if ( CommonUtil.isEmpty( storeMap.get( "business_name" ) ) ) {
		name = storeMap.get( "sto_name" ).toString();
	    } else {
		name = storeMap.get( "business_name" ).toString();
	    }
	    request.setAttribute( "stoName", name );
	    request.setAttribute( "stoPicture", PropertiesUtil.getResourceUrl() + storeMap.get( "stoPicture" ) );

	    if ( CommonUtil.isNotEmpty( obj ) ) {
		String pageid = "0";
		List list1 = mallPageService.shoppage( obj.getPagStoId() );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    pageid = map1.get( "id" ).toString();
		}
		request.setAttribute( "pageid", pageid );
	    }

	    if ( obj.getPagData() != null ) {
		MallPaySet set = new MallPaySet();
		set.setUserId( userid );
		set = mallPaySetService.selectByUserId( set );
		int state = mallPifaApplyService.getPifaApplay( member, set );

		JSONArray jsonobj = JSONArray.fromObject( obj.getPagData() );//转换成JSON数据
		JSONArray XinJson = new JSONArray();//获取新的数组对象
		for ( int i = 0; i < jsonobj.size(); i++ ) {
		    JSONArray XinidJSon = new JSONArray();
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
		    if ( CommonUtil.isEmpty( map1.get( "imgID" ) ) ) {
			continue;
		    }
		    String imaid = map1.get( "imgID" ).toString();
		    String type = map1.get( "type" ).toString();//如果type==1 代表来自与页面商品信息，2代表轮播图里面的信息
		    JSONArray jsonobjX = JSONArray.fromObject( imaid );//转换成JSON数据
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
	    if ( obj.getPagCss() != null ) {
		dataJson = obj.getPagCss();
	    }

	    //int countproduct = mallPageService.countproduct(obj.getPagStoId());
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "shopId", obj.getPagStoId() );
	    int countproduct = mallProductDAO.selectCountAllByShopids( params );
	    request.setAttribute( "countproduct", countproduct );
	    Date dat = DateTimeKit.addHours( DateTimeKit.getNow(), -168 );
	    String time = DateTimeKit.getDateTime( dat, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    int xincound = mallPageService.counttime( obj.getPagStoId(), time );
	    Integer mainid = 0;
	    List list = mallPageService.pagecountid( obj.getPagStoId() );
	    if ( list.size() > 0 ) {
		Map map = (Map) list.get( 0 );
		mainid = Integer.valueOf( map.get( "id" ).toString() );
	    }
	    request.setAttribute( "mainid", mainid );
	    request.setAttribute( "xincound", xincound );//店面信息
	    request.setAttribute( "mapbranch", storeMap );//店面信息
	    request.setAttribute( "name", name );
	    request.setAttribute( "http", http );
	    request.setAttribute( "obj", obj );
	    request.setAttribute( "dataJson", dataJson );
	    request.setAttribute( "picJson", picJson );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "shopid", obj.getPagStoId() );

	    SessionUtils.setMallShopId( obj.getPagStoId(), request );

	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    mallPageService.getCustomer( request, userid );

	    if ( CommonUtil.isNotEmpty( wxPubMap ) && CommonUtil.isNotEmpty( member ) && CommonUtil.judgeBrowser( request ) == 1 ) {
		WxJsSdk wxJsSdk = new WxJsSdk();
		wxJsSdk.setPublicId( member.getPublicId() );
		wxJsSdk.setUrl( CommonUtil.getpath( request ) );
		WxJsSdkResult result = wxPublicUserService.wxjssdk( wxJsSdk );
		request.setAttribute( "record", result );
	    }
	} catch ( Exception e ) {
	    logger.error( "访问商城首页异常：" + e.getMessage() );
	    e.printStackTrace();
	    return "error/404";
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "访问商城首页耗时：" + second + "秒" );
	}
	return "/mall/phonepage/phoneHomepage";
    }

    /**
     * 弹出该店铺分类页
     */
    @RequestMapping( "/branchPage" )
    public String branchPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Integer stoId = Integer.valueOf( request.getParameter( "stoId" ).toString() );
	    PageUtil page = mallPageService.selectListBranch( stoId, params );
	    request.setAttribute( "page", page );
	    String ym = PropertiesUtil.getHomeUrl();//域名
	    request.setAttribute( "ym", ym );
	    int pageindex = 0;
	    if ( CommonUtil.isNotEmpty( page ) ) {
		pageindex = page.getCurPage();
	    }
	    if ( pageindex == 1 ) {
		List< Map< String,Object > > typeList = mallPageService.typePage( stoId, user );
		request.setAttribute( "typeList", typeList );
	    }
	    request.setAttribute( "shopId", stoId );
	} catch ( Exception e ) {
	    logger.error( "商城店铺分类弹出框异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "/mall/page/branchPage";

    }

    /**
     * 弹出该店铺预售信息
     */
    @RequestMapping( "/choosePresalePro" )
    public String choosePresalePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    Integer stoId = Integer.valueOf( request.getParameter( "stoId" ).toString() );
	    String url = PropertiesUtil.getHomeUrl();//域名
	    request.setAttribute( "url", url );
	    List< Map< String,Object > > presaleList = mallPageService.productPresale( stoId, params );
	    request.setAttribute( "presaleList", presaleList );
	    request.setAttribute( "shopId", stoId );
	    String http = PropertiesUtil.getResourceUrl();//图片地址
	    request.setAttribute( "http", http );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "check", params.get( "check" ) );
	    request.setAttribute( "stoId", params.get( "stoId" ) );

	} catch ( Exception e ) {
	    logger.error( "选择预售商品出错：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/page/branchPresale";

    }

    /**
     * 显示手机商品页
     */
    @RequestMapping( "{id}/{shopid}/79B4DE7C/phoneProduct" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String phoneProduct( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @RequestParam Map< String,Object > param )
		    throws Exception {
	logger.info( "进入商城商品详细页面。。。" );
	String jsp = "/mall/product/phone/phone_product_detail";
	int userid = 0;
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > mapmessage = mallPageService.querySelct( id );//获取商品信息
	    Map< String,Object > mapuser = mallPageService.selUser( shopid );//查询商家信息
	    WsWxShopInfo shop = wxShopService.getShopById( CommonUtil.toInteger( mapuser.get( "wx_shop_id" ) ) );
	    if ( shop != null ) {//商家地址显示
		String address = mallPageService.queryAreaById( shop.getProvince() + "," + shop.getCity() );
		request.setAttribute( "storeAddress", address );
	    }

	    Map< String,Object > publicUserid = mallPageService.getPublicByUserMap( mapuser );//查询公众号信息
	    userid = CommonUtil.toInteger( mapmessage.get( "user_id" ) );
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    //条件类型
	    int rType = 0;//0 普通商品 1积分商品 2粉币商品
	    String inv_id = "";//存放默认库存id

	    boolean isShop = mallPageService.wxShopIsDelete( shopid, shop );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    if ( CommonUtil.isNotEmpty( param ) ) {
		if ( CommonUtil.isNotEmpty( param.get( "rType" ) ) ) {
		    rType = CommonUtil.toInteger( param.get( "rType" ).toString() );
		}
	    }
	    if ( rType > 0 ) {
		mallProductService.setJifenByRedis( member, request, rType, userid );//设置积分商城、粉币商城的标示
	    } else {
		int isJifen = mallProductService.getJifenByRedis( member, request, rType, userid );//判断是否是积分和粉币商城
		if ( isJifen > 0 ) {
		    rType = isJifen;
		}
	    }
	    if ( rType > 0 ) {
		boolean isSc = true;
		if ( CommonUtil.isNotEmpty( mapmessage.get( "is_integral_change_pro" ) ) && CommonUtil.isNotEmpty( mapmessage.get( "change_integral" ) ) && rType == 1 ) {
		    if ( CommonUtil.toInteger( mapmessage.get( "is_integral_change_pro" ) ) == 1 && CommonUtil.toDouble( mapmessage.get( "change_integral" ) ) > 0 ) {
			rType = 1;
			isSc = false;
		    }
		} else if ( CommonUtil.isNotEmpty( mapmessage.get( "is_fenbi_change_pro" ) ) && CommonUtil.isNotEmpty( mapmessage.get( "change_fenbi" ) ) && rType == 2 ) {
		    if ( CommonUtil.toInteger( mapmessage.get( "is_fenbi_change_pro" ) ) == 1 && CommonUtil.toDouble( mapmessage.get( "change_fenbi" ) ) > 0 ) {
			rType = 2;
			isSc = false;
		    }
		}
		if ( isSc ) {
		    rType = 0;
		}
	    }
	    if ( rType == 0 ) {//普通商品去查询商品是否参加了活动
		String invId = mallPageService.productActivity( request, member, id, shopid, userid );
		if ( CommonUtil.isNotEmpty( invId ) ) {
		    inv_id = invId;
		}
	    } else if ( CommonUtil.isNotEmpty( member ) ) {
		member = memberService.findMemberById( member.getId(), member );
		if ( rType == 1 ) {
		    request.setAttribute( "integral", member.getIntegral() );
		} else if ( rType == 2 ) {
		    request.setAttribute( "fenbi", member.getFansCurrency() );
		}
	    }

	    int toshop = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "toshop" ) ) ) {
		toshop = CommonUtil.toInteger( param.get( "toshop" ) );
		if ( toshop == 1 ) {//到店购买
		    mallProductService.setIsShopBySession( 1, shopid, userid, request );
		} else {//非到店购买
		    mallProductService.clearIsShopSession( shopid, userid, request );
		}
	    } else {
		toshop = mallProductService.getIsShopBySession( shopid, userid, request );
	    }
	    request.setAttribute( "toshop", toshop );
	    String is_delete = mapmessage.get( "is_delete" ).toString();
	    String is_publish = mapmessage.get( "is_publish" ).toString();
	    String check_status = mapmessage.get( "check_status" ).toString();
	    if ( CommonUtil.isNotEmpty( mapmessage.get( "inv_id" ) ) ) {
		inv_id = mapmessage.get( "inv_id" ).toString();
	    }
	    if ( is_delete.equals( "0" ) && is_publish.equals( "1" ) && check_status.equals( "1" ) ) {
		int proId = CommonUtil.toInteger( mapmessage.get( "id" ) );

		if ( CommonUtil.isNotEmpty( mapmessage.get( "product_detail" ) ) ) {
		    request.setAttribute( "proDetail", ( mapmessage.get( "product_detail" ).toString() ).replaceAll( "\"", "&quot;" ).replaceAll( "'", "&apos;" )
				    .replaceAll( "(\r\n|\r|\n|\n\r)", "" ) );
		}

		int saleMemberId = 0;//保存销售员id
		if ( CommonUtil.isNotEmpty( param.get( "saleMemberId" ) ) ) {
		    saleMemberId = CommonUtil.toInteger( param.get( "saleMemberId" ) );
		    sellerService.setSaleMemberIdByRedis( member, saleMemberId, request, userid );
		}
		saleMemberId = sellerService.getSaleMemberIdByRedis( member, saleMemberId, request, userid );

		if ( saleMemberId > 0 ) {
		    MallSeller mallSeller = sellerService.selectSellerByMemberId( saleMemberId );//查询销售员的信息

		    if ( saleMemberId > 0 && CommonUtil.isNotEmpty( mallSeller ) ) {//分享的用户 判断是否是销售员
			sellerService.shareSellerIsSale( member, saleMemberId, mallSeller );
		    }
		    //查询销售商品信息
		    mallSellerMallsetService.selectSellerProduct( request, proId, saleMemberId, param, member );
		}

		if ( CommonUtil.isNotEmpty( param.get( "view" ) ) ) {
		    request.setAttribute( "view", param.get( "view" ) );
		}

		//获取用户购买商品的数量
		int buyNums = 0;
		if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( mapmessage.get( "pro_restriction_num" ) ) ) {
		    if ( CommonUtil.toInteger( mapmessage.get( "pro_restriction_num" ) ) > 0 ) {
			buyNums = mallPageService.memberBuyProNum( member.getId(), mapmessage, 0 );
		    }
		}
		if ( CommonUtil.isNotEmpty( mapmessage.get( "pro_restriction_num" ) ) ) {
		    int maxNum = CommonUtil.toInteger( mapmessage.get( "pro_restriction_num" ) );
		    if ( maxNum <= buyNums && buyNums > 0 && maxNum > 0 ) {
			request.setAttribute( "buyNums", buyNums );
			request.setAttribute( "maxNum", maxNum );
		    }
		}
		List imagelist = mallPageService.imageProductList( id, 1 );//获取轮播图列表
		if ( mapmessage.get( "pro_type_id" ).toString().equals( "2" ) ) {//购买会员卡判断商家是否已经购买了会员卡
		    int code = 0;
		    if ( CommonUtil.isNotEmpty( member ) ) {
			if ( CommonUtil.isEmpty( member.getMcId() ) ) {
			    if ( 1 == member.getIsbuy() ) {//已经购买了会员卡
				code = -1;
			    }
			} else {//已经购买了会员卡
			    code = -1;
			}
		    }
		    request.setAttribute( "buyCode", code );
		} else if ( mapmessage.get( "pro_type_id" ).toString().equals( "3" ) ) {//购买卡券包
		    if ( CommonUtil.isNotEmpty( mapmessage.get( "card_type" ) ) ) {

			Map< String,Object > cardMap = mallPageService.getCardReceive( CommonUtil.toInteger( mapmessage.get( "card_type" ) ) );
			if ( CommonUtil.isNotEmpty( cardMap ) ) {
			    if ( CommonUtil.isNotEmpty( cardMap.get( "recevieMap" ) ) ) {
				request.setAttribute( "recevieMap", cardMap.get( "recevieMap" ) );
				JSONObject cardObj = JSONObject.fromObject( cardMap.get( "recevieMap" ) );
				if ( CommonUtil.isNotEmpty( cardObj.get( "cardMessage" ) ) ) {
				    request.setAttribute( "cardmessage", JSONArray.fromObject( cardObj.get( "cardMessage" ) ) );
				}
			    }
			    if ( CommonUtil.isNotEmpty( cardMap.get( "returnDuofencardList" ) ) ) {
				request.setAttribute( "cardList", cardMap.get( "returnDuofencardList" ) );
			    }
			}
		    }
		}
		int viewNum = mallPageService.updateProViewPage( id + "", mapmessage );
		request.setAttribute( "viewNum", viewNum );
		String http = PropertiesUtil.getResourceUrl();
		request.setAttribute( "imagelist", imagelist );
		request.setAttribute( "http", http );
		request.setAttribute( "mapmessage", mapmessage );
		Map shopmessage = mallPageService.shopmessage( shopid, shop );
		double discount = 1;//商品折扣
		String is_member_discount = mapmessage.get( "is_member_discount" ).toString();//商品是否参加折扣
		if ( is_member_discount.equals( "1" ) && CommonUtil.isNotEmpty( member ) ) {
		    discount = mallProductService.getMemberDiscount( CommonUtil.toString( mapmessage.get( "is_member_discount" ) ), member );
		}
		request.setAttribute( "discount", discount );//折扣价
		request.setAttribute( "shopid", shopid );
		request.setAttribute( "id", id );
		request.setAttribute( "shopmessage", shopmessage );
		String is_specifica = mapmessage.get( "is_specifica" ).toString();
		Map guige = new HashMap();
		List< Map< String,Object > > specificaList = new ArrayList< Map< String,Object > >();
		List< Map< String,Object > > guigePrice = new ArrayList< Map< String,Object > >();//获取商品所有规格值价钱，图片，库存，价钱等
		if ( is_specifica.equals( "1" ) ) {
		    guige = mallPageService.productSpecifications( id, inv_id );
		    specificaList = mallProductSpecificaService.getSpecificaByProductId( id );//获取商品规格值
		    guigePrice = mallPageService.guigePrice( id );
		    if ( guige == null || specificaList == null || specificaList.size() == 0 ) {
			mapmessage.put( "is_specifica", 0 );
		    }
		}
		int userPId = SessionUtils.getAdminUserId( userid, request );//通过用户名查询主账号id
		long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有(从session获取)
		if ( isJxc == 1 ) {
		    List< Map< String,Object > > erpInvList = mallProductService
				    .getErpInvByProId( CommonUtil.toInteger( mapmessage.get( "erp_pro_id" ) ), CommonUtil.toInteger( mapmessage.get( "shop_id" ) ) );
		    if ( erpInvList != null && erpInvList.size() > 0 ) {
			int totalInvNum = 0;
			for ( Map< String,Object > erpMap : erpInvList ) {
			    int erpInvId = CommonUtil.toInteger( erpMap.get( "erpInvId" ) );
			    int invNum = CommonUtil.toInteger( erpMap.get( "invNum" ) );
			    if ( guigePrice != null && guigePrice.size() > 0 ) {
				for ( Map< String,Object > map : guigePrice ) {
				    int erp_inv_id = CommonUtil.toInteger( map.get( "erp_inv_id" ) );
				    if ( erp_inv_id == erpInvId ) {
					totalInvNum += invNum;
					map.put( "inv_num", invNum );
				    }
				}
			    } else {
				int erp_inv_id = CommonUtil.toInteger( mapmessage.get( "erp_inv_id" ) );
				if ( erp_inv_id > 0 && erp_inv_id == erpInvId ) {
				    mapmessage.put( "pro_stock_total", invNum );
				}
			    }
			}
			if ( totalInvNum > 0 ) {
			    mapmessage.put( "pro_stock_total", totalInvNum );
			}
		    }
		}
		Map addressMap = null;
		List< Integer > memberList = new ArrayList<>();
		if ( CommonUtil.isNotEmpty( member ) ) {
		    memberList = memberService.findMemberListByIds( member.getId() );
		    addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, member.getId() ) );
		}
		String loginCity = "";
		if ( addressMap == null || addressMap.size() == 0 ) {
		    String ip = IPKit.getRemoteIP( request );
		    loginCity = mallPageService.getProvince( ip );
		} else {
		    loginCity = addressMap.get( "memProvince" ).toString();
		    request.setAttribute( "addressMap", addressMap );
		}
		if ( loginCity.equals( "" ) ) {
		    loginCity = "";
		}
		request.setAttribute( "loginCity", loginCity );
		if ( loginCity != null && CommonUtil.isNotEmpty( loginCity ) ) {
		    Map< String,Object > map = new HashMap< String,Object >();
		    map.put( "province_id", loginCity );
		    JSONArray arr = new JSONArray();
		    JSONObject obj = new JSONObject();
		    obj.put( "shop_id", shopid );
		    double price = 0;
		    if ( mapmessage != null ) {
			if ( mapmessage.get( "is_specifica" ).equals( "1" ) ) {
			    price = CommonUtil.toDouble( mapmessage.get( "inv_price" ) ) * discount;
			} else {
			    price = CommonUtil.toDouble( mapmessage.get( "pro_price" ) ) * discount;
			}
		    }
		    obj.put( "price_total", price );
		    obj.put( "proNum", 1 );
		    arr.add( obj );
		    map.put( "orderArr", arr );
		    map.put( "toshop", toshop );
		    map.put( "proTypeId", mapmessage.get( "pro_type_id" ) );
		    Map< String,Object > priceMap = mallFreightService.getFreightMoney( map );
		    if ( CommonUtil.toInteger( mapmessage.get( "pro_type_id" ) ) > 0 || toshop == 1 ) {//虚拟商品都不需要运费，到店购买不需要运费
			request.setAttribute( "priceMap", "0" );
		    } else {
			request.setAttribute( "priceMap", priceMap.get( CommonUtil.toString( shopid ) ) );
		    }
		}
		mallPageService.isAddShopCart( request, member, memberList );
		if ( CommonUtil.isNotEmpty( member ) ) {//已登陆
		    mallPageService.mergeShoppCart( member, request, response );//把cookie商品，合并到购物车
		}
		SessionUtils.setMallShopId( shopid, request );

		if ( CommonUtil.isNotEmpty( member ) ) {
		    mallCollectService.getProductCollect( request, id, member.getId() );//查询买家是否已经收藏
		    request.setAttribute( "memberId", member.getId() );
		}
		request.setAttribute( "guige", guige );
		request.setAttribute( "guigePrice", guigePrice );
		request.setAttribute( "specificaList", specificaList );
		request.setAttribute( "rType", rType );
		mallPageService.getCustomer( request, userid );

		if ( CommonUtil.isNotEmpty( mapmessage.get( "flow_id" ) ) ) {
		    int flowId = CommonUtil.toInteger( mapmessage.get( "flow_id" ) );
		    if ( flowId > 0 ) {
			BusFlowInfo flow = fenBiFlowService.getFlowInfoById( flowId );
			if ( CommonUtil.isNotEmpty( flow ) ) {
			    request.setAttribute( "flow_desc", flow.getType() + "M流量" );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( publicUserid ) && CommonUtil.isNotEmpty( member ) ) {
		    if ( CommonUtil.isNotEmpty( publicUserid.get( "qrcode_url" ) ) ) {
			request.setAttribute( "qrcode_url", publicUserid.get( "qrcode_url" ) );
		    }
		}
		if ( CommonUtil.isNotEmpty( mapuser ) ) {
		    if ( CommonUtil.isNotEmpty( mapuser.get( "advert" ) ) ) {
			if ( mapuser.get( "advert" ).toString().equals( "0" ) ) {
			    request.setAttribute( "isAdvert", 1 );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( publicUserid ) && CommonUtil.isNotEmpty( member ) && CommonUtil.judgeBrowser( request ) == 1 ) {
		    WxJsSdk wxJsSdk = new WxJsSdk();
		    wxJsSdk.setPublicId( member.getPublicId() );
		    wxJsSdk.setUrl( CommonUtil.getpath( request ) );
		    WxJsSdkResult result = wxPublicUserService.wxjssdk( wxJsSdk );
		    request.setAttribute( "record", result );
		}
		//商品已下架或者已删除
	    } else if ( is_delete.equals( "1" ) || is_publish.equals( "-1" ) ) {
		jsp = "mall/product/phone/shopdelect";
		//商品审核中或者未上架
	    } else {
		jsp = "mall/product/phone/shopdelect";
	    }
	} catch ( Exception e ) {
	    logger.error( "访问商城商品详细页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return jsp;

    }

    /**
     * 商品加入到购物车里
     */
    @SysLogAnnotation( description = "页面管理-商品购物车保存", op_function = "2" )
    @RequestMapping( "79B4DE7C/addshopping" )
    public void addshopping( HttpServletRequest request, HttpServletResponse response, MallShopCart obj ) throws IOException {
	Map< String,Object > map = new HashMap<>();
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		mallPageService.addshopping( obj, member, request, response );
		map.put( "error", "0" );
	    } else {
		SessionUtils.setShopCart( net.sf.json.JSONObject.fromObject( obj ), request );
		mallPageService.addshopping( obj, null, request, response );

		map.put( "error", "0" );
	    }
	} catch ( Exception e ) {
	    logger.error( "商品加入购物车异常" + e.getMessage() );
	    map.put( "error", "1" );
	    e.printStackTrace();
	}
	PrintWriter p = response.getWriter();
	p.write( net.sf.json.JSONObject.fromObject( map ).toString() );
	p.flush();
	p.close();
    }

    /**
     * 根据会员信息，获取用户购物车
     */
    @RequestMapping( "79B4DE7C/shoppingcare" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingcare( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
		userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    /*loginMap.put( "uclogin", 1 );*/
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    double discount = 1;//商品折扣
	    if ( CommonUtil.isNotEmpty( member ) ) {
		discount = mallProductService.getMemberDiscount( "1", member );
	    }

	    int type = 0;
	    if ( CommonUtil.isNotEmpty( request.getParameter( "type" ) ) ) {
		type = CommonUtil.toInteger( request.getParameter( "type" ) );
	    }

	    if ( CommonUtil.isNotEmpty( member ) ) {//已登陆
		mallPageService.mergeShoppCart( member, request, response );//把cookie商品，合并到购物车
	    }
	    mallPageService.shoppingcare( member, discount, type, request, userid );//获取购物车的信息

	    String http = PropertiesUtil.getResourceUrl();
	    request.setAttribute( "http", http );
	    SessionUtils.setMallShopId( request.getParameter( "shopId" ), request );
	    mallPageService.getCustomer( request, userid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "discount", discount );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    if ( CommonUtil.isNotEmpty( member ) && CommonUtil.judgeBrowser( request ) == 1 ) {
		if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
		    WxJsSdk wxJsSdk = new WxJsSdk();
		    wxJsSdk.setPublicId( member.getPublicId() );
		    wxJsSdk.setUrl( CommonUtil.getpath( request ) );
		    WxJsSdkResult result = wxPublicUserService.wxjssdk( wxJsSdk );
		    request.setAttribute( "record", result );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "购物车异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/phone/shoppingcare";

    }

    /**
     * 移除购物车信息
     */
    @SysLogAnnotation( description = "页面管理-购物车移除", op_function = "4" )
    @RequestMapping( "79B4DE7C/shoppingdelect" )
    public void shoppingdelect( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    String delects = request.getParameter( "delects" );
	    String upArr = request.getParameter( "upArr" );
	    mallPageService.shoppingdelect( delects, upArr, 1 );
	    map.put( "error", "0" );
	} catch ( Exception e ) {
	    logger.error( "删除购物车异常" );
	    map.put( "error", "-1" );
	    e.printStackTrace();
	}
	PrintWriter p = response.getWriter();
	p.write( net.sf.json.JSONObject.fromObject( map ).toString() );
	p.flush();
	p.close();
    }

    /**
     * 购物车提交订单
     */
    @SysLogAnnotation( description = "页面管理-购物车提交订单", op_function = "2" )
    @RequestMapping( "79B4DE7C/shoppingorder" )
    public void shoppingorder( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Map< String,Object > mas = new HashMap< String,Object >();
	try {
	    String json = request.getParameter( "arrayJson" );
	    Member member = SessionUtils.getLoginMember( request );
	    String memberId = null;
	    if ( member != null ) {
		memberId = member.getId().toString();
	    }
			/*String memberId = "200";*/
	    String totalnum = request.getParameter( "totalnum" );
	    String totalprice = request.getParameter( "totalprice" );
	    Map map = mallPageService.shoporder( json, totalnum, totalprice, memberId );
	    String data = net.sf.json.JSONObject.fromObject( map ).toString();

	    mas.put( "error", "0" );
	    mas.put( "type", "1" );
	    mas.put( "result", data );
	} catch ( Exception e ) {
	    mas.put( "error", "1" );
	    e.printStackTrace();

	}
	PrintWriter p = response.getWriter();
	p.write( net.sf.json.JSONObject.fromObject( mas ).toString() );
	p.flush();
	p.close();
    }

    /**
     * 商品详情
     */
    @RequestMapping( "{id}/79B4DE7C/shopdetails" )
    public String shopdetails( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) {
	MallProductDetail obj = mallPageService.shopdetails( id );
	request.setAttribute( "obj", obj );
	return "mall/product/phone/shopdetails";
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @RequestMapping( "{shopid}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    int userid = 0;
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > mapuser = mallPageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = mallPageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }

	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    boolean isShop = mallPageService.wxShopIsDelete( shopid, null );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();//图片url链接前缀
	    List groLs = mallPageService.storeList( shopid, 1, 0 );//获取分类
	    String pageid = "0";
	    List list1 = mallPageService.shoppage( shopid );
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";
	    //获取店铺下的商品
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = CommonUtil.toString( params.get( "type" ) );
	    }
	    String desc = "1";
	    //获取店铺下的商品
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = CommonUtil.toString( params.get( "desc" ) );
	    }
	    //条件类型
	    int rType = 0;//0 普通商品 1积分商品 2粉币商品
	    if ( CommonUtil.isNotEmpty( params.get( "rType" ) ) ) {
		rType = CommonUtil.toInteger( params.get( "rType" ).toString() );
	    }
	    if ( rType > 0 ) {
		mallProductService.setJifenByRedis( member, request, rType, userid );
	    } else {
		/*int isJifen = mallProductService.getJifenByRedis(member, request, rType,userid);
		if(isJifen > 0){
			rType = isJifen;
		}*/
	    }
	    params.put( "type", type );
	    params.put( "desc", desc );
	    params.put( "rType", rType );
	    if ( rType == 1 ) {//兼容以前的链接
		return "redirect:/phoneIntegral/" + shopid + "/79B4DE7C/toIndex.do?uId=" + userid;
	    }

	    double discount = 1;//商品折扣
	    if ( CommonUtil.isNotEmpty( member ) ) {
		discount = mallProductService.getMemberDiscount( "1", member );
	    }
	    request.setAttribute( "discount", discount );

	    boolean isPifa = mallPifaApplyService.isPifa( member );

	    PageUtil page = mallPageService.productAllList( shopid, type, rType, member, discount, params, isPifa );

	    mallPageService.getSearchLabel( request, shopid, member, params );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );//用户id
	    }
	    request.setAttribute( "pageid", pageid );//主店铺id
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "page", page );
	    request.setAttribute( "groLs", groLs );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "shopid", shopid );
	    request.setAttribute( "http", http );
	    request.setAttribute( "rType", rType );
	    request.setAttribute( "member", member );
	    request.setAttribute( "isPifa", isPifa );
	    shopid = mallStoreService.getShopBySession( request.getSession(), shopid );//从session获取店铺id  或  把店铺id存入session
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    mallPageService.getCustomer( request, userid );
	    if ( CommonUtil.isNotEmpty( publicUserid ) && CommonUtil.isNotEmpty( member ) && CommonUtil.judgeBrowser( request ) == 1 ) {
		WxJsSdk wxJsSdk = new WxJsSdk();
		wxJsSdk.setPublicId( member.getPublicId() );
		wxJsSdk.setUrl( CommonUtil.getpath( request ) );
		WxJsSdkResult result = wxPublicUserService.wxjssdk( wxJsSdk );
		request.setAttribute( "record", result );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "商品搜索页面异常：" + e.getMessage() );
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "访问全部商品页面花费：" + second + "秒" );
	}
	return "mall/product/phone/shoppingall";

    }

    /**
     * 分页查询商品
     */
    @RequestMapping( value = "79B4DE7C/shoppingAllPage" )
    public void shoppingAllPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入分页查询商品Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    Member member = SessionUtils.getLoginMember( request );

	    boolean isPifa = false;
	    double discount = 1;//商品折扣
	    int rType = 0;
	    int shopid = 0;
	    String type = "1";
			/*int saleMemberId = 0;*/
	    if ( CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "rType" ) ) ) {
		    rType = CommonUtil.toInteger( params.get( "rType" ) );
		}
		if ( CommonUtil.isNotEmpty( params.get( "shopid" ) ) ) {
		    shopid = CommonUtil.toInteger( params.get( "shopid" ) );
		}
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    type = CommonUtil.toString( params.get( "type" ) );
		}
		if ( CommonUtil.isNotEmpty( params.get( "isPifa" ) ) ) {
		    isPifa = Boolean.valueOf( params.get( "isPifa" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( params.get( "discount" ) ) ) {
		    discount = CommonUtil.toDouble( params.get( "discount" ) );
		}
		/*if(CommonUtil.isNotEmpty(params.get("saleMemberId"))){
			saleMemberId = CommonUtil.toInteger(params.get("saleMemberId"));
		}*/
	    }
	    PageUtil page = new PageUtil();
	    if ( params.containsKey( "isSeller" ) ) {
		MallSellerMallset mallSet = mallSellerMallsetService.selectByMemberId( member.getId() );
		page = mallSellerMallsetService.selectProductBySaleMember( mallSet, params, type, rType, discount, isPifa );
	    } else {
		page = mallPageService.productAllList( shopid, type, rType, member, discount, params, isPifa );
	    }
	    resultMap.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "保存分页查询商品失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 根据店铺id获取商家所有的店铺
     */
    @RequestMapping( "{publicId}/79B4DE7C/shopall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shopall( HttpServletRequest request, HttpServletResponse response, @PathVariable int publicId, @RequestParam Map< String,Object > params ) throws Exception {
	Member member = SessionUtils.getLoginMember( request );
	WxPublicUsers wx = wxPublicUserService.selectById( publicId );
	int userid = wx.getBusUserId();
	Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	loginMap.put( "uclogin", 1 );
	String returnUrl = userLogin( request, response, loginMap );
	if ( CommonUtil.isNotEmpty( returnUrl ) ) {
	    return returnUrl;
	}
	request.setAttribute( "userid", member );
	String http = PropertiesUtil.getResourceUrl();//图片url链接前缀
	List< Map< String,Object > > shopList = mallStoreService.findByUserId( userid );

	request.setAttribute( "shopList", shopList );
	request.setAttribute( "http", http );
	mallPageService.getCustomer( request, userid );
	return "mall/store/phone/shopall";

    }

    /**
     * 根据店铺id获取商家所有的店铺
     */
    @RequestMapping( "{userId}/79B4DE7C/shopall_1" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shopall_1( HttpServletRequest request, HttpServletResponse response, @PathVariable int userId, @RequestParam Map< String,Object > params ) throws Exception {
	Member member = SessionUtils.getLoginMember( request );
	Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userId, request );
	loginMap.put( "uclogin", 1 );
	String returnUrl = userLogin( request, response, loginMap );
	if ( CommonUtil.isNotEmpty( returnUrl ) ) {
	    return returnUrl;
	}
	String http = PropertiesUtil.getResourceUrl();//图片url链接前缀
	List< Map< String,Object > > shopList = mallStoreService.findByUserId( userId );

	request.setAttribute( "shopList", shopList );
	request.setAttribute( "http", http );
	mallPageService.getCustomer( request, userId );
	return "mall/store/phone/shopall";
    }

    /**
     * 根据主页面获取页面规格信息
     */
    @RequestMapping( "{id}/79B4DE7C/mainpageShopping" )
    public void mainpageShopping( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Map< String,Object > mapmessage = mallPageService.querySelct( id );//获取商品信息
	    String is_delete = mapmessage.get( "is_delete" ).toString();
	    String is_publish = mapmessage.get( "is_publish" ).toString();
	    String check_status = mapmessage.get( "check_status" ).toString();
	    if ( is_delete.equals( "0" ) && is_publish.equals( "1" ) && check_status.equals( "1" ) ) {
		Member member = SessionUtils.getLoginMember( request );
		double discount = mallProductService.getMemberDiscount( CommonUtil.toString( mapmessage.get( "is_member_discount" ) ), member );//商品折扣

		String is_specifica = mapmessage.get( "is_specifica" ).toString();
		Map< String,Object > guige = new HashMap<>();
		List< Map< String,Object > > specificaList = new ArrayList<>();
		List< Map< String,Object > > guigePrice = new ArrayList<>();//获取商品所有规格值价钱，图片，库存，价钱等
		if ( is_specifica.equals( "1" ) ) {
		    guige = mallPageService.productSpecifications( id, null );
		    specificaList = mallProductSpecificaService.getSpecificaByProductId( id );//获取商品规格值
		    guigePrice = mallPageService.guigePrice( id );
		}
		map.put( "error", "0" );
		map.put( "discount", discount );
		map.put( "guige", guige );
		map.put( "mapmessage", mapmessage );
		map.put( "guigePrice", guigePrice );
		map.put( "specificaList", specificaList );
	    } else {
		map.put( "error", "2" );
		map.put( "message", "该商品正在审核中" );
	    }
	} catch ( Exception e ) {
	    map.put( "error", "1" );
	    map.put( "message", "该商品可能已下架" );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, JSONObject.fromObject( map ).toString() );
	}
    }

    /**
     * 获取的主页面id
     */
    @RequestMapping( "{shopId}/79B4DE7C/getPageIds" )
    public String getPageIds( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopId ) {
	String jsp = "error/404";
	try {
	    Map< String,Object > pagePage = mallPageService.getPage( 0, shopId );//根据登陆人或店铺id返回页面id
	    if ( CommonUtil.isNotEmpty( pagePage ) ) {
		if ( CommonUtil.isNotEmpty( pagePage.get( "id" ) ) ) {
		    jsp = "redirect:/mallPage/" + pagePage.get( "id" ).toString() + "/79B4DE7C/pageIndex.do";
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取页面id出错:" + e );
	    e.printStackTrace();
	}
	return jsp;
    }

    /**
     * 收藏商品
     */
    @RequestMapping( "79B4DE7C/collection" )
    public void collection( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	PrintWriter p = null;
	boolean result = false;
	JSONObject obj = new JSONObject();
	try {
	    p = response.getWriter();
	    int memberId = SessionUtils.getLoginMember( request ).getId();
	    result = mallCollectService.collectionProduct( params, memberId );
	} catch ( Exception e ) {
	    logger.error( "收藏商品出错:" + e );
	    e.printStackTrace();
	}
	obj.put( "result", result );
	p.write( obj.toString() );
	p.flush();
	p.close();
    }

    /**
     * 查询商品的规格参数
     */
    @RequestMapping( "79B4DE7C/getProductParams" )
    public void getProductParams( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	PrintWriter p = null;
	JSONObject obj = new JSONObject();
	try {
	    p = response.getWriter();
	    if ( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) {
		List< MallProductParam > paramList = mallPageService.getProductParam( CommonUtil.toInteger( params.get( "proId" ) ) );
		obj.put( "paramList", paramList );
	    }
	} catch ( Exception e ) {
	    logger.error( "查询商品规格参数出错:" + e );
	    e.printStackTrace();
	}
	p.write( obj.toString() );
	p.flush();
	p.close();
    }

    /**
     * 查询公众号下面所有的门店
     */
    @RequestMapping( "{shopId}/79B4DE7C/storesAll" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String findStores( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopId, @RequestParam Map< String,Object > params ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    List< Map< String,Object > > shopList = mallStoreService.findByUserId( userid );
	    request.setAttribute( "shopList", shopList );
	    request.setAttribute( "member", member );
	    request.setAttribute( "urls", request.getHeader( "Referer" ) );
	    List list1 = mallPageService.shoppage( shopId );
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		String pageid = map1.get( "id" ).toString();
		request.setAttribute( "pageid", pageid );
	    }
	    mallPageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "查询公众号下面所有的门店异常：" + e.getMessage() );
	}

	return "mall/store/phone/shoplist";
    }

    /**
     * 查询商品的评价
     */
    @RequestMapping( "79B4DE7C/getProductComment" )
    public void getProductComment( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	PrintWriter p = null;
	JSONObject obj = new JSONObject();
	try {
	    p = response.getWriter();

	    Member member = SessionUtils.getLoginMember( request );

	    if ( CommonUtil.isEmpty( member ) && CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) {
		Map< String,Object > maps = mallPageService.getProductComment( params, member );
		obj.put( "maps", maps );
		String http = PropertiesUtil.getResourceUrl();
		obj.put( "http", http );

	    }
	} catch ( Exception e ) {
	    logger.error( "查询商品评价出错:" + e );
	    e.printStackTrace();
	}
	p.write( obj.toString() );
	p.flush();
	p.close();
    }

    /**
     * 查询公众号下面所有的门店
     */
    @RequestMapping( "{shopId}/79B4DE7C/toMallIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String toMallIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopId ) {
	int pageId = 0;
	try {
	    if ( shopId > 0 ) {
		List list1 = mallPageService.shoppage( shopId );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    pageId = CommonUtil.toInteger( map1.get( "id" ) );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "查询公众号下面所有的门店异常：" + e.getMessage() );
	}
	return "redirect:/mallPage/" + pageId + "/79B4DE7C/pageIndex.do";
    }

}