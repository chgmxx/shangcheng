package com.gt.mall.controller.auction;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.auction.MallAuctionOfferService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * <p>
 * 拍卖 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mAuction" )
public class MallAuctionController extends AuthorizeOrLoginController {

    @Autowired
    private MallAuctionService          auctionService;
    @Autowired
    private MallStoreService            storeService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private MallPaySetService           paySetService;
    @Autowired
    private MallAuctionMarginService    auctionMarginService;
    @Autowired
    private MallAuctionBiddingService   auctionBiddingService;
    @Autowired
    private MallAuctionOfferDAO         auctionOfferDAO;
    @Autowired
    private MallAuctionBiddingDAO       auctionBiddingDAO;
    @Autowired
    private MallFreightService          freightService;
    @Autowired
    private MallOrderService            orderService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MallAuctionOfferService     auctionOfferService;
    @Autowired
    private MallCollectService          mallCollectService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MemberAddressService        memberAddressService;

    /**
     * 拍卖管理列表页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = true;//是管理员
	    if ( CommonUtil.isNotEmpty( user.getPid() ) && user.getPid() > 0 ) {
		isAdminFlag = storeService.isAdminUser( user.getId() );//查询子账户是否是管理员

		if ( !isAdminFlag ) {
		    request.setAttribute( "isNoAdminFlag", 1 );
		}
	    }
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = auctionService.selectAuctionByShopId( params, user.getId() );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "86" ) );
	} catch ( Exception e ) {
	    logger.error( "拍卖列表：" + e );
	    e.printStackTrace();
	}

	return "mall/auction/auction_index";
    }

    /**
     * 拍卖管理列表页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     */
    @RequestMapping( "margin" )
    public String margin( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = auctionMarginService.selectMarginByShopId( params, user.getId() );
		request.setAttribute( "page", page );
		request.setAttribute( "shoplist", shoplist );
	    }
	    request.setAttribute( "type", params.get( "type" ) );
	} catch ( Exception e ) {
	    logger.error( "拍卖列表：" + e );
	    e.printStackTrace();
	}

	return "mall/auction/auction_margin";
    }

    /**
     * 进入拍卖编辑页面
     *
     * @param request
     * @param response
     * @param params
     *
     * @return
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据拍卖id查询拍卖信息
		Map< String,Object > groupMap = auctionService.selectAuctionById( id );
		if ( groupMap != null ) {
		    Object imageUrl = groupMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = groupMap.get( "imageUrl" );
		    }
		    groupMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "auction", groupMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入拍卖编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/auction/auction_edit";
    }

    /**
     * 编辑拍卖
     *
     * @param request
     * @param response
     * @param params
     */
    @SysLogAnnotation( description = "拍卖管理-编辑拍卖", op_function = "2" )
    @RequestMapping( "edit_auction" )
    public void editAuction( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入编辑辑拍卖controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = auctionService.editAuction( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑拍卖：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	try {
	    obj.put( "code", code );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除拍卖
     *
     * @param request
     * @param response
     * @param params
     */
    @SysLogAnnotation( description = "拍卖管理-删除拍卖", op_function = "4" )
    @RequestMapping( "group_remove" )
    public void removeGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入删除拍卖controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) && !CommonUtil.isEmpty( params ) ) {
		int id = 0;
		if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		    id = CommonUtil.toInteger( params.get( "id" ) );
		}
		MallAuction auction = new MallAuction();
		auction.setId( id );
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    int type = CommonUtil.toInteger( params.get( "type" ) );
		    if ( type == -1 ) {// 删除
			auction.setIsDelete( 1 );
		    } else if ( type == -2 ) {// 使失效拍卖
			auction.setIsUse( -1 );
		    }
		}
		boolean flag = auctionService.deleteAuction( auction );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除拍卖：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	try {
	    obj.put( "code", code );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 获取店铺下所有的拍卖（手机端）
     *
     * @param request
     * @param response
     * @param shopid
     * @param params
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/auctionall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctionall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    boolean isShop = pageService.wxShopIsDelete( shopid, null );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    List groupList = pageService.storeList( shopid, 1, 0 );// 获取分类
	    String pageid = "0";
	    List list1 = pageService.shoppage( shopid );// 获取商品主页
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";// 查询条件
	    String desc = "1";// 排序 默认倒序
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = params.get( "type" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    params.put( "shopId", shopid );
	    List< Map< String,Object > > productList = auctionService.getAuctionAll( member, params );// 查询店铺下所有加入拍卖的商品

	    //查询搜索标签，历史记录
	    pageService.getSearchLabel( request, shopid, member, params );

	    request.setAttribute( "shopId", shopid );
	    request.setAttribute( "pageid", pageid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "groupList", groupList );
	    request.setAttribute( "imgHttp", PropertiesUtil.getResourceUrl() );// 图片url链接前缀
	    request.setAttribute( "productList", productList );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "proName", params.get( "proName" ) );

	    String mall_shopId = Constants.SESSION_KEY + "shopId";
	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
		request.getSession().setAttribute( mall_shopId, shopid );
	    } else {
		if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( CommonUtil.toString( shopid ) ) ) {
		    request.getSession().setAttribute( mall_shopId, shopid );
		}
	    }
	    Map< String,Object > footerMenuMap = paySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入拍卖商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/auction/phone/auctionall";
    }

    /**
     * 拍卖详情
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @RequestMapping( "{id}/{shopid}/{aId}/79B4DE7C/auctiondetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String auctiondetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @PathVariable int aId,
		    @RequestParam Map< String,Object > param ) throws Exception {
	String jsp = "mall/auction/phone/auctiondetail";
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    boolean isShop = pageService.wxShopIsDelete( shopid, null );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }
	    //条件类型
	    String inv_id = "";//存放默认库存id
	    Map< String,Object > groupMap = new HashMap< String,Object >();
	    if ( CommonUtil.isNotEmpty( member ) ) {
		groupMap.put( "joinUserId", member.getId() );
	    }

	    //查询商品的拍卖信息
	    MallAuction auction = auctionService.getAuctionByProId( id, shopid, aId );
	    if ( auction != null ) {
		if ( CommonUtil.isNotEmpty( member ) ) {
		    groupMap.put( "aucId", auction.getId() );
		    //查询用户参加拍卖的数量
		    int auctionCount = auctionBiddingService.selectCountByBuyId( groupMap );
		    request.setAttribute( "auctionCount", auctionCount );
		}

		if ( CommonUtil.isNotEmpty( member ) ) {
		    MallAuctionMargin margin = new MallAuctionMargin();
		    margin.setAucId( auction.getId() );
		    margin.setUserId( member.getId() );
		    margin.setMarginStatus( 1 );
		    //查询用户是否已经交纳佣金
		    List< MallAuctionMargin > marginList = auctionMarginService.getMyAuction( margin );
		    request.setAttribute( "marginSize", marginList.size() );
		}
		MallAuctionBidding bid = new MallAuctionBidding();
		bid.setAucId( auction.getId() );
		if ( auction.getAucType().toString().equals( "2" ) ) {//升价拍
		    //查询出价次数
		    MallAuctionOffer offer = new MallAuctionOffer();
		    offer.setAucId( auction.getId() );
		    List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( offer );//查询拍卖的出价信息
		    for ( MallAuctionOffer offer1 : offerList ) {
			Member member1 = memberService.findMemberById( offer.getUserId(), null );
			offer1.setNickname( member1.getNickname() );
		    }
		    request.setAttribute( "offerList", offerList );
		    if ( auction.getStatus() == -1 ) {
			//获取加价拍卖胜出的人
			MallAuctionOffer aucOffer = auctionOfferDAO.selectByOffer( auction.getId().toString() );
			if ( aucOffer != null && CommonUtil.isNotEmpty( member ) ) {
			    if ( aucOffer.getUserId().toString().equals( member.getId().toString() ) ) {
				request.setAttribute( "isWin", 1 );//胜出
			    } else {
				request.setAttribute( "isWin", 0 );//出局
			    }
			    request.setAttribute( "winOffer", aucOffer );

			    bid.setUserId( member.getId() );
			    bid.setProId( auction.getProductId() );
			    MallAuctionBidding bidding = auctionBiddingDAO.selectByBidding( bid );
			    if ( bidding != null ) {
				request.setAttribute( "isSubmit", bidding.getAucStatus() );
			    }
			}
		    }
		} else {//降价拍
		    List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );//查询用户的竞拍信息
		    if ( bidList != null && bidList.size() > 0 ) {
			if ( auction.getStatus() == -1 ) {
			    auction.setNowPrice( CommonUtil.toDouble( bidList.get( 0 ).getAucPrice() ) );
			}
		    }
		    request.setAttribute( "bidList", bidList );
		}
	    }

	    request.setAttribute( "auction", auction );
	    Map mapmessage = pageService.querySelct( id );//获取商品信息
	    String is_delete = mapmessage.get( "is_delete" ).toString();
	    String is_publish = mapmessage.get( "is_publish" ).toString();
	    String check_status = mapmessage.get( "check_status" ).toString();
	    if ( CommonUtil.isNotEmpty( mapmessage.get( "inv_id" ) ) ) {
		inv_id = mapmessage.get( "inv_id" ).toString();
	    }
	    if ( is_delete.equals( "0" ) && is_publish.equals( "1" ) && check_status.equals( "1" ) ) {
		List imagelist = pageService.imageProductList( id, 1 );//获取轮播图列表
		String pageid = "0";
		List list1 = pageService.shoppage( shopid );
		if ( list1.size() > 0 ) {
		    Map map1 = (Map) list1.get( 0 );
		    pageid = map1.get( "id" ).toString();
		}
		if ( CommonUtil.isNotEmpty( mapmessage.get( "product_detail" ) ) ) {
		    request.setAttribute( "proDetail", ( mapmessage.get( "product_detail" ).toString() ).replaceAll( "\"", "'" ).replaceAll( "(\r\n|\r|\n|\n\r)", "" ) );
		}
		int viewNum = pageService.updateProViewPage( id + "", mapmessage );
		request.setAttribute( "viewNum", viewNum );
		request.setAttribute( "pageid", pageid );//商品主页面
		String http = PropertiesUtil.getResourceUrl();
		request.setAttribute( "imagelist", imagelist );
		request.setAttribute( "http", http );
		request.setAttribute( "mapmessage", mapmessage );
		Map shopmessage = pageService.shopmessage( shopid, null );
		double discount = 1;//商品折扣
		String is_member_discount = mapmessage.get( "is_member_discount" ).toString();//商品是否参加折扣
		if ( is_member_discount.equals( "1" ) && CommonUtil.isNotEmpty( member ) ) {
		    discount = memberService.getMemberDiscount( member.getId() );//商品折扣
		}
		request.setAttribute( "discount", discount );//折扣价
		request.setAttribute( "shopid", shopid );
		request.setAttribute( "id", id );
		request.setAttribute( "shopmessage", shopmessage );
		String is_specifica = mapmessage.get( "is_specifica" ).toString();
		Map guige = new HashMap();
		List< Map< String,Object > > specificaList = new ArrayList<>();
		List< Map< String,Object > > guigePrice = new ArrayList<>();//获取商品所有规格值价钱，图片，库存，价钱等
		if ( is_specifica.equals( "1" ) ) {
		    guige = pageService.productSpecifications( id, inv_id );
		    specificaList = productSpecificaService.getSpecificaByProductId( id );//获取商品规格值
		    guigePrice = pageService.guigePrice( id );
		}

		List< MemberAddress > addressList = new ArrayList<>();
		if ( CommonUtil.isNotEmpty( member ) ) {
		    List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
		    addressList = memberAddressService.addressList( CommonUtil.getMememberIds( memberList, member.getId() ) );
		}
		String loginCity;
		if ( addressList == null || addressList.size() == 0 ) {
		    String ip = IPKit.getRemoteIP( request );
		    loginCity = pageService.getProvince( ip );
		} else {
		    loginCity = addressList.get( 0 ).getMemProvince().toString();
		    request.setAttribute( "addressMap", addressList.get( 0 ) );
		}
		if ( loginCity.equals( "" ) ) {
		    loginCity = "";
		}
		request.setAttribute( "loginCity", loginCity );
		if ( loginCity != null && CommonUtil.isNotEmpty( loginCity ) ) {
		    Map< String,Object > map = new HashMap<>();
		    map.put( "province_id", loginCity );
		    JSONArray arr = new JSONArray();
		    JSONObject obj = new JSONObject();
		    obj.put( "shop_id", shopid );
		    float price = 0;
		    if ( mapmessage != null ) {
			float dis = (float) discount;//折扣
			if ( mapmessage.get( "is_specifica" ).equals( "1" ) ) {
			    price = Float.valueOf( mapmessage.get( "inv_price" ).toString() ) * dis;
			} else {
			    price = Float.valueOf( mapmessage.get( "pro_price" ).toString() ) * dis;
			}
		    }
		    obj.put( "price_total", price );
		    obj.put( "proNum", 1 );
		    arr.add( obj );
		    map.put( "orderArr", arr );
		    Map< Integer,Object > priceMap = freightService.getFreightMoney( map );
		    request.setAttribute( "priceMap", priceMap.get( shopid ) );
		}

		if ( CommonUtil.isNotEmpty( member ) ) {
		    request.setAttribute( "memberId", member.getId() );
		}
		request.setAttribute( "guige", guige );
		request.setAttribute( "guigePrice", guigePrice );
		request.setAttribute( "specificaList", specificaList );
		Map< String,Object > map1 = new HashMap<>();
		map1.put( "product", mapmessage );
		map1.put( "imagelist", imagelist );
		map1.put( "discount", discount );
		map1.put( "auction", auction );
		map1.put( "guige", guige );
		map1.put( "guigePrice", guigePrice );
		map1.put( "specificaList", specificaList );
		System.out.println( JSONObject.fromObject( map1 ).toString() );

		String mall_shopId = Constants.SESSION_KEY + "shopId";
		if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
		    request.getSession().setAttribute( mall_shopId, shopid );
		} else {
		    if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( CommonUtil.toString( shopid ) ) ) {
			request.getSession().setAttribute( mall_shopId, shopid );
		    }
		}
		if ( CommonUtil.isNotEmpty( member ) ) {
		    mallCollectService.getProductCollect( request, id, member.getId() );
		}
		pageService.getCustomer( request, userid );

		//商品已下架或者已删除
	    } else if ( is_delete.equals( "1" ) || is_publish.equals( "-1" ) ) {
		jsp = "mall/product/phone/shopdelect";
		//商品审核中或者未上架
	    } else {
		jsp = "mall/product/phone/shopdelect";
	    }
	} catch ( Exception e ) {
	    logger.error( "进入拍卖商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return jsp;

    }

    /**
     * 拍卖详情
     *
     * @param request
     * @param response
     * @param proId
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{proId}/{invId}/{auctionId}/79B4DE7C/toAddMargin" )
    public String toAddMargin( HttpServletRequest request, HttpServletResponse response, @PathVariable int proId, @PathVariable int invId, @PathVariable int auctionId,
		    @RequestParam Map< String,Object > param ) throws Exception {
	logger.info( "进入交纳保证金的页面...." );
	String jsp = "mall/auction/phone/toAddMargin";
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( param.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( param.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map mapmessage = pageService.querySelct( proId );//获取商品信息
	    int shopid = 0;
	    if ( CommonUtil.isNotEmpty( mapmessage.get( "shop_id" ) ) ) {
		shopid = CommonUtil.toInteger( mapmessage.get( "shop_id" ).toString() );
		String mall_shopId = Constants.SESSION_KEY + "shopId";
		if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
		    request.getSession().setAttribute( mall_shopId, mapmessage.get( "shop_id" ) );
		} else {
		    if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( mapmessage.get( "shop_id" ).toString() ) ) {
			request.getSession().setAttribute( mall_shopId, mapmessage.get( "shop_id" ) );
		    }
		}
	    }
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map< String,Object > publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( publicUserid.get( "bus_user_id" ) );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    //查询商品的拍卖信息
	    MallAuction auction = auctionService.getAuctionByProId( proId, shopid, auctionId );
	    request.setAttribute( "auction", auction );
	    request.setAttribute( "mapmessage", mapmessage );

	    List imagelist = pageService.imageProductList( proId, 1 );//获取轮播图列表
	    request.setAttribute( "imagelist", imagelist.get( 0 ) );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    request.setAttribute( "proId", proId );
	    String is_specifica = mapmessage.get( "is_specifica" ).toString();
	    Map guige = new HashMap();
	    if ( is_specifica.equals( "1" ) || is_specifica == "1" ) {
		guige = pageService.productSpecifications( proId, invId + "" );
	    }

	    int memType = memberService.isCardType( member.getId() );

	    int isWxPay = 0;//不能微信支付
	    int isAliPay = 0;//不能支付宝支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicUserid ) ) ) {
		isWxPay = 1;//可以微信支付
	    } else {
		isAliPay = 1;//可以支付宝支付
	    }
	    //TODO 需关连 alipayUserService.findAlipayUserByBusId()方法
	    /*AlipayUser alipayUser = alipayUserService.findAlipayUserByBusId( member.getBusid() );
	    if ( CommonUtil.isNotEmpty( alipayUser ) && isWxPay == 0 ) {
		isAliPay = 1;//可以支付宝支付
	    }*/
	    request.setAttribute( "isWxPay", isWxPay );
	    request.setAttribute( "isAliPay", isAliPay );
	    request.setAttribute( "memType", memType );
	    request.setAttribute( "memberId", member.getId() );
	    request.setAttribute( "guige", guige );
	    //			KeysUtil keysUtil = new KeysUtil();
	    //			request.setAttribute("alipaySubject", keysUtil.getEncString("商城交纳拍卖保证金"));
	    request.setAttribute( "alipaySubject", "商城交纳拍卖保证金" );
	} catch ( Exception e ) {
	    logger.error( "进入交纳保证金的页面出错：" + e );
	    e.printStackTrace();
	}
	return jsp;

    }

    /**
     * 商品详情
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping( "{id}/{shopid}/{auctionId}/79B4DE7C/shopdetails" )
    public String shopdetails( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @PathVariable int auctionId,
		    @RequestParam Map< String,Object > param ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map< String,Object > publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    int type = 1;
	    if ( CommonUtil.isNotEmpty( param.get( "type" ) ) ) {
		type = CommonUtil.toInteger( param.get( "type" ) );
	    }
	    MallAuction auction = auctionService.getAuctionByProId( id, shopid, auctionId );
	    if ( type == 1 ) {//拍品详情
		MallProductDetail obj = pageService.shopdetails( id );
		request.setAttribute( "obj", obj );
	    } else if ( type == 2 ) {
		if ( auction != null ) {
		    request.setAttribute( "aucType", auction.getAucType() );
		    MallAuctionBidding bid = new MallAuctionBidding();
		    bid.setAucId( auction.getId() );
		    if ( auction.getAucType().toString().equals( "2" ) ) {//升价拍
			//查询出价次数
			MallAuctionOffer offer = new MallAuctionOffer();
			offer.setAucId( auction.getId() );
			List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( offer );//查询拍卖的出价信息
			for ( MallAuctionOffer offer1 : offerList ) {
			    Member member1 = memberService.findMemberById( offer.getUserId(), null );
			    offer1.setNickname( member1.getNickname() );
			}
			request.setAttribute( "offerList", offerList );
		    } else {//降价拍
			List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );//查询用户的竞拍信息
			request.setAttribute( "bidList", bidList );
		    }
		}
	    }
	    if ( auction != null ) {
		request.setAttribute( "aucType", auction.getAucType() );
	    }
	    request.setAttribute( "id", id );
	    request.setAttribute( "shopid", shopid );
	    request.setAttribute( "auctionId", auctionId );
	    request.setAttribute( "type", type );
	    String pageid = "0";
	    List list1 = pageService.shoppage( shopid );
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
		request.setAttribute( "pageid", pageid );
	    }
	    if ( CommonUtil.isNotEmpty( shopid ) ) {
		String mall_shopId = Constants.SESSION_KEY + "shopId";
		if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
		    request.getSession().setAttribute( mall_shopId, shopid );
		} else {
		    if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( CommonUtil.toString( shopid ) ) ) {
			request.getSession().setAttribute( mall_shopId, shopid );
		    }
		}
	    }
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    this.logger.error( "MallAuctionController方法异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/auction/phone/productdetail";
    }

    /**
     * 交纳保证金
     *
     * @param request
     * @param param
     * @param response
     */
    @RequestMapping( value = "/79B4DE7C/addMargin" )
    @SysLogAnnotation( op_function = "2", description = "拍卖交纳保证金" )
    public void addMargin( HttpServletRequest request, @RequestParam Map< String,Object > param, HttpServletResponse response ) throws IOException {
	logger.info( "进入生成订单页面-拍卖" );
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    /*String memberId = SessionUtils.getLoginMember( request ).getId().toString();
	    if ( CommonUtil.isNotEmpty( param.get( "userId" ) ) ) {
		memberId = param.get( "userId" ).toString();
	    }*/

	    if ( param != null ) {
		result = auctionMarginService.addMargin( param, SessionUtils.getLoginMember( request ) );
	    }
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "交纳保证金失败，稍后请重新提交" );
	    logger.error( "交纳拍卖保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "交纳保证金花费：" + second + "秒" );

	    CommonUtil.write( response, result );
	}
    }

    /**
     * 储值卡支付成功的回调
     */
    @RequestMapping( value = "/79B4DE7C/payWay" )
    @SysLogAnnotation( op_function = "2", description = "保证金储蓄卡支付成功添加记录和修改" )
    public String payWay( @RequestBody Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "busId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "busId" ) );
	    }
	    if ( CommonUtil.isEmpty( member ) && CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
	    }
	    if ( userid > 0 ) {
		request.setAttribute( "userid", userid );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    String memberId = member.getId().toString();
	    Integer payWay = CommonUtil.toInteger( params.get( "payWay" ) );
	    Map< String,Object > payRresult = new HashMap< String,Object >();
	    double orderMoney = Double.parseDouble( params.get( "orderMoney" ).toString() );

	    auctionMarginService.paySuccessAuction( params );

	    /*if ( payWay == 2 ) {//储蓄卡支付方式
		//TODO 需调用 memberpayService.storePay() 方法
		//		payRresult = memberpayService.storePay(Integer.parseInt(memberId), orderMoney);
		String result = payRresult.get( "result" ).toString();
		if ( result.equals( "2" ) ) {
		    auctionMarginService.paySuccessAuction( params );
		} else if ( result.equals( "1" ) && payWay != 4 ) {
		    return "redirect:/phoneMemberController/79B4DE7C/recharge.do?id=" + memberId;
		}
	    }*/
	} catch ( Exception e ) {
	    logger.error( "保证金储蓄卡支付保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "保证金储蓄卡支付花费：" + second + "秒" );
	}
	return "redirect:/mAuction/79B4DE7C/myMargin.do";
    }

    /**
     * 拍卖出价
     */
    @RequestMapping( value = "/79B4DE7C/addOffer" )
    @SysLogAnnotation( op_function = "2", description = "拍卖出价" )
    public void addOffer( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	logger.info( "出价-拍卖" );
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();
	    String memberId = SessionUtils.getLoginMember( request ).getId().toString();
	    if ( CommonUtil.isNotEmpty( params.get( "userId" ) ) ) {
		memberId = params.get( "userId" ).toString();
	    }

	    if ( params != null ) {
		result = auctionOfferService.addOffer( params, memberId );
	    }
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "拍卖出价失败，稍后请重新提交" );
	    logger.error( "拍卖出价异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	out.write( JSONObject.fromObject( result ).toString() );
	out.flush();
	out.close();
    }

    /**
     * 进入我的保证金页面
     */
    @RequestMapping( value = "/79B4DE7C/myMargin" )
    public String myMargin( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "busId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "busId" ) );
	    }
	    if ( userid > 0 ) {
		request.setAttribute( "userid", userid );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    String memberId = member.getId().toString();
	    if ( CommonUtil.isNotEmpty( params.get( "userId" ) ) ) {
		memberId = params.get( "userId" ).toString();
	    }

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
		margin.setUserId( Integer.parseInt( memberId ) );
	    }
	    List< MallAuctionMargin > marginList = auctionMarginService.getMyAuction( margin );

	    request.setAttribute( "marginList", marginList );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "type", 2 );
	    pageService.getCustomer( request, 0 );
	} catch ( Exception e ) {
	    logger.error( "进入我的保证金页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/auction/phone/mymargin";
    }

    /**
     * 进入我的竞拍
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/myBidding" )
    public String myBidding( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    String memberId = member.getId().toString();
	    if ( CommonUtil.isNotEmpty( params.get( "userId" ) ) ) {
		memberId = params.get( "userId" ).toString();
	    }

	    MallAuctionMargin margin = new MallAuctionMargin();
	    margin.setUserId( Integer.parseInt( memberId ) );
	    List< Map< String,Object > > bidList = auctionBiddingService.selectMyBidding( member );

	    request.setAttribute( "bidList", bidList );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "type", 1 );
	    pageService.getCustomer( request, 0 );
	} catch ( Exception e ) {
	    logger.error( "进入我的竞拍页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/auction/phone/mymargin";
    }

    /**
     * 进入我的获拍金页面
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/myHuoPai" )
    public String myHuoPai( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    List< Map< String,Object > > bidList = auctionBiddingService.selectMyHuoBid( member );

	    request.setAttribute( "bidList", bidList );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "type", 3 );
	    pageService.getCustomer( request, 0 );
	} catch ( Exception e ) {
	    logger.error( "进入我的获拍页面异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/auction/phone/mymargin";
    }

    /**
     * 获取二维码的图片
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/getTwoCode" )
    public void getTwoCode( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    String code = params.get( "code" ).toString();
	    String content = PropertiesUtil.getHomeUrl() + "/mAuction/" + code + "/79B4DE7C/auctiondetail.do";
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "获取拍卖二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 进入退定金的页面
     *
     * @return
     */
    @RequestMapping( "returnMarginPopUp" )
    public String returnMarginPopUp( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int marginId = CommonUtil.toInteger( params.get( "marginId" ) );
	    BusUser user = SessionUtils.getLoginUser( request );

	    MallAuctionMargin margin = auctionMarginService.selectById( marginId );
	    request.setAttribute( "margin", margin );
	    request.setAttribute( "busUserId", user.getId() );
	    request.setAttribute( "http", PropertiesUtil.getHomeUrl() );

	} catch ( Exception e ) {
	    logger.error( "进入退定金的页面异常：" + e );
	    e.printStackTrace();
	}

	return "mall/auction/returnMarginPopUp";
    }

    /**
     * 退保证金
     *
     * @param params
     */
    @RequestMapping( value = "/agreedReturnMargin" )
    @SysLogAnnotation( op_function = "3", description = "退保证金" )
    public void agreedReturnMargin( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	PrintWriter out = null;
	Map< String,Object > result = new HashMap< String,Object >();
	try {
	    out = response.getWriter();

	    int marginId = CommonUtil.toInteger( params.get( "marginId" ) );

	    MallAuctionMargin margin = auctionMarginService.selectById( marginId );

	    Map< String,Object > map = new HashMap< String,Object >();
	    map.put( "id", margin.getId() );
	    map.put( "user_id", margin.getUserId() );
	    map.put( "pay_way", margin.getPayWay() );
	    map.put( "margin_money", margin.getMarginMoney() );
	    map.put( "auc_no", margin.getAucNo() );
	    result = auctionMarginService.returnEndMargin( map );
	    if ( !result.containsKey( "result" ) ) {
		result.put( "result", true );
	    }
	} catch ( Exception e ) {
	    result.put( "result", false );
	    result.put( "msg", "退保证金失败，稍后请重新提交" );
	    logger.error( "退保证金异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	out.write( JSONObject.fromObject( result ).toString() );
	out.flush();
	out.close();
    }

}
