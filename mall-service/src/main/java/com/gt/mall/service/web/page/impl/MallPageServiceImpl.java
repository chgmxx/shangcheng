package com.gt.mall.service.web.page.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCollectDAO;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.page.MallPageDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.presale.MallPresaleDAO;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.dao.presale.MallPresaleMessageRemindDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductGroupDAO;
import com.gt.mall.dao.product.MallSearchLabelDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleMessageRemind;
import com.gt.mall.entity.product.*;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.groupbuy.MallGroupJoinService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaPriceService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import com.gt.mall.service.web.product.*;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 页面表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPageServiceImpl extends BaseServiceImpl< MallPageDAO,MallPage > implements MallPageService {

    private static Logger logger = LoggerFactory.getLogger( MallPageServiceImpl.class );

    @Autowired
    private MallPageDAO                 mallPageDAO;
    @Autowired
    private MallShopCartDAO             mallShopCartDAO;
    @Autowired
    private MallProductDetailService    mallProductDetailService;
    @Autowired
    private MallGroupBuyService         mallGroupBuyService;
    @Autowired
    private MallSeckillService          mallSeckillService;
    @Autowired
    private MallAuctionService          mallAuctionService;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallStoreDAO                mallStoreDAO;
    @Autowired
    private MallSearchKeywordService    mallSearchKeywordService;
    @Autowired
    private MallSearchLabelDAO          mallSearchLabelDAO;
    @Autowired
    private MallCollectDAO              mallCollectDAO;
    @Autowired
    private MallProductParamService     mallProductParamService;
    @Autowired
    private MallCommentDAO              mallCommentDAO;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallPresaleService          mallPresaleService;
    @Autowired
    private MallPresaleTimeService      mallPresaleTimeService;
    @Autowired
    private MallPresaleDepositDAO       mallPresaleDepositDAO;
    @Autowired
    private MallPresaleDepositService   mallPresaleDepositService;
    @Autowired
    private MallPresaleDAO              mallPresaleDAO;
    @Autowired
    private MallPresaleMessageRemindDAO mallPresaleMessageRemindDAO;
    @Autowired
    private MallPifaService             mallPifaService;
    @Autowired
    private MallPifaDAO                 mallPifaDAO;
    @Autowired
    private MallPifaApplyService        mallPifaApplyService;
    @Autowired
    private MallOrderDAO                mallOrderDAO;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private MallProductDAO              mallProductDAO;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private WxShopService               wxShopService;//微信门店接口
    @Autowired
    private MallProductGroupDAO         mallProductGroupDAO;//商品分组dao
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private MallIntegralDAO             mallIntegralDAO;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private MallGroupService            mallGroupService;
    @Autowired
    private MallGroupJoinService        mallGroupJoinService;
    @Autowired
    private MallImageAssociativeDAO     mallImageAssociativeDAO;
    @Autowired
    private CardService                 cardService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private DictService                 dictService;
    @Autowired
    private MallPifaPriceService        mallPifaPriceService;

    /**
     * 分页查询
     */
    @Override
    public PageUtil findByPage( Map< String,Object > params, BusUser user, HttpServletRequest request ) {
	List< Map< String,Object > > storeList = mallStoreService.findAllStoByUser( user, request );// 根据商家id查询门店id
	if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
	    params.put( "storeList", storeList );
	}
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int rowCount = mallPageDAO.count( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallPage/index.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	List< DictBean > typeList = dictService.getDict( "1073" );//查询页面的所属分类

	//查询店铺id
	List< Map< String,Object > > list = mallPageDAO.findByPage( params );
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > pageMap : list ) {
		int pag_sto_id = CommonUtil.toInteger( pageMap.get( "pag_sto_id" ) );
		int pag_type_id = CommonUtil.toInteger( pageMap.get( "pag_type_id" ) );
		//循环门店
		for ( Map< String,Object > storeMap : storeList ) {
		    int shopId = CommonUtil.toInteger( storeMap.get( "id" ) );
		    if ( shopId == pag_sto_id ) {
			pageMap.put( "business_name", storeMap.get( "sto_name" ) );
			break;
		    }
		}
		//循环页面分类
		for ( DictBean map : typeList ) {
		    int typeId = CommonUtil.toInteger( map.getItem_key() );
		    if ( pag_type_id == typeId ) {
			pageMap.put( "item_value", map.getItem_value() );
			break;
		    }
		}
	    }
	}
	page.setSubList( list );
	return page;
    }

    /**
     * 保存或修改信息
     */
    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > saveOrUpdate( MallPage page, BusUser user ) throws Exception {
	boolean result = true;
	String message = "操作成功！";
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    int count = 0;

	    //判断用户是否存在主页
	    int mainCount = mallPageDAO.selectMainCountByShopId( page );
	    if ( mainCount == 0 ) {//店铺下没有主页
		page.setPagIsMain( 1 );
	    } else {
		page.setPagIsMain( 0 );
	    }
	    if ( CommonUtil.isNotEmpty( page.getId() ) ) {
		count = mallPageDAO.updateById( page );
		if ( count <= 0 ) {
		    throw new Exception( "操作失败" );
		}
	    } else {
		count = mallPageDAO.insert( page );
		Integer id = page.getId();
		count = mallPageDAO.updateById( page );
		if ( count <= 0 ) {
		    throw new Exception( "操作失败！" );
		}
	    }
	    //如果设为主页，需保证只有一个主页
	    if ( page.getPagIsMain() == 1 ) {
		/*String sql = "SELECT count(1) FROM t_mall_page WHERE pag_sto_id=" + page.getPagStoId();*/
		Wrapper< MallPage > pageWrapper = new EntityWrapper<>();
		pageWrapper.where( "pag_sto_id = {0}", page.getPagStoId() );
		long l = mallPageDAO.selectCount( pageWrapper );
		if ( l > 1 ) {
		    count = mallPageDAO.updateOtherisMain( page.getId(), page.getPagStoId() );//如果只有一个情况下，
		    if ( count <= 0 ) {
			throw new Exception( "操作失败！" );
		    }
		}
	    }
	    //判断店铺下是否有主页
	    mainCount = mallPageDAO.selectMainCountByShopId( page );
	    if ( mainCount == 0 ) {
		page.setPagIsMain( 1 );
		//没有主页的情况下，把修改的页面设置为主页
		mallPageDAO.updateById( page );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new Exception( "操作失败！" );
	} finally {
	    msg.put( "result", result );
	    msg.put( "message", message );
	}
	return msg;
    }

    /**
     * 删除页面信息
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > delete( String[] ids ) throws Exception {
	boolean result = true;
	String message = "操作成功！";
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    if ( ids != null && ids.length > 0 ) {
		for ( int i = 0; i < ids.length; i++ ) {
		    int id = Integer.parseInt( ids[i] );
		    MallPage page = mallPageDAO.selectById( id );
		    int pagCount = mallPageDAO.selectCountByShopId( page.getPagStoId() );
		    if ( pagCount <= 1 ) {
			result = false;
			message = "您店铺下的页面只剩下" + pagCount + "个，请新建页面再删除";
			break;
		    } else {
			int count = mallPageDAO.deleteById( id );
			if ( count > 0 ) {
			    //判断店铺下是否有主页
			    int counts = mallPageDAO.selectMainCountByShopId( page );
			    if ( counts == 0 ) {//没有主页，要默认一个主页
				mallPageDAO.updatePageMain( page.getPagStoId() );
			    }
			}
		    }
		}

	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new Exception( "操作失败！" );
	} finally {
	    msg.put( "result", result );
	    msg.put( "message", message );
	}
	return msg;
    }

    /**
     * 设为主页
     */
    @Override
    public Map< String,Object > setMain( Integer id, Integer shopid ) throws Exception {
	boolean result = true;
	String message = "操作成功！";
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    Wrapper< MallPage > pageWrapper = new EntityWrapper<>();
	    pageWrapper.where( "pag_sto_id={0} and pag_is_main=1", shopid );
	    long l = mallPageDAO.selectCount( pageWrapper );
	    if ( l > 0 ) {
		pageWrapper = new EntityWrapper<>();
		pageWrapper.where( "pag_sto_id=" + shopid + " AND pag_is_main=1" );
		//"UPDATE t_mall_page set pag_is_main=0 WHERE pag_sto_id=" + shopid + " AND pag_is_main=1"
		MallPage page = new MallPage();
		page.setPagIsMain( 0 );
		page.setId( id );
		mallPageDAO.update( page, pageWrapper );

	    }
	    MallPage page = new MallPage();
	    page.setPagIsMain( 1 );
	    page.setId( id );
	    mallPageDAO.updateById( page );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new Exception( "设为主页失败！" );
	} finally {
	    msg.put( "result", result );
	    msg.put( "message", message );
	}
	return msg;
    }

    @Override
    public Map< String,Object > select( Integer id, Integer userid ) {
	return mallPageDAO.selectMapById( id );
    }

    @Override
    public Map< String,Object > querySelct( Integer id ) {
	Map< String,Object > productMap = mallProductDAO.selectMapById( id );
	if ( CommonUtil.isEmpty( productMap ) ) {
	    return null;
	}
	//查询商品的订单详情
	MallProductDetail detail = mallProductDetailService.selectByProductId( id );
	if ( CommonUtil.isNotEmpty( detail ) ) {
	    productMap.put( "product_detail", detail.getProductDetail() );
	    productMap.put( "product_message", detail.getProductMessage() );
	}
	//查询商品图片
	Map< String,Object > params = new HashMap<>();
	params.put( "assId", id );
	params.put( "isMainImages", 1 );
	params.put( "assType", 1 );
	List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssId( params );
	if ( imageList != null && imageList.size() > 0 ) {
	    productMap.put( "image_url", imageList.get( 0 ).get( "image_url" ) );
	}
	if ( CommonUtil.isEmpty( productMap.get( "is_specifica" ) ) ) {
	    return productMap;
	}
	if ( !productMap.get( "is_specifica" ).toString().equals( "1" ) ) {
	    return productMap;
	}
	//查询商品库存
	MallProductInventory inven = mallProductInventoryService.selectByIsDefault( id );
	if ( inven != null ) {
	    productMap.put( "inv_price", inven.getInvPrice() );
	    productMap.put( "inv_sale_num", inven.getInvSaleNum() );
	    productMap.put( "specifica_img_id", inven.getSpecificaImgId() );
	    productMap.put( "inv_code", inven.getInvCode() );

	    if ( CommonUtil.isEmpty( inven.getSpecificaImgId() ) ) {
		return productMap;
	    }
	    //查询商品规格图片
	    /*if ( inven.getSpecificaImgId() > 0 ) {
		MallProductSpecifica specifica = mallProductSpecificaDAO.selectById( inven.getSpecificaImgId() );
		if ( CommonUtil.isNotEmpty( specifica ) ) {
		    productMap.put( "specifica_img_url", specifica.getSpecificaImgUrl() );
		}
	    }*/
	}

	return productMap;
    }

    @Override
    public MallPage select( Integer id ) {
	return mallPageDAO.selectById( id );
    }

    @Override
    public PageUtil selectListBranch( Integer stoId, Map< String,Object > params ) {
	params.put( "shopId", stoId );
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPageDAO.selectCountsByShopId( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallPage/branchPage.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > productList = mallPageDAO.selectByShopId( params );

	page.setSubList( productList );
	return page;
    }

    @Override
    public Map< String,Object > selectBranch( Integer id ) {
	return mallPageDAO.selectMapById( id );
    }

    @Override
    public List< Map< String,Object > > imageProductList( Integer id, int asscType ) {
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "assId", id );
	params.put( "assType", asscType );
	return mallImageAssociativeService.selectImageByAssId( params );
    }

    @Override
    public Map< String,Object > productMessage( Integer id ) {
	return mallProductDAO.selectMapById( id );
    }

    @Override
    public Map< String,Object > shopmessage( Integer shopid, WsWxShopInfo wxShop ) {
	Map< String,Object > storeMap = mallStoreDAO.selectMapById( shopid );
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    storeMap.put( "business_name", storeMap.get( "sto_name" ) );
	    try {
		if ( CommonUtil.isEmpty( wxShop ) ) {
		    wxShop = wxShopService.getShopById( CommonUtil.toInteger( storeMap.get( "wx_shop_id" ) ) );
		}
		storeMap.put( "business_name", wxShop.getBusinessName() );
		storeMap.put( "status", wxShop.getStatus() );

		List< WsShopPhoto > photoList = wxShopService.getShopPhotoByShopId( wxShop.getId() );
		if ( photoList != null && photoList.size() > 0 ) {
		    storeMap.put( "stoPicture", photoList.get( 0 ).getLocalAddress() );
		}
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    return storeMap;
	}
	return null;
    }

    @Override
    public Map< String,Object > wxpublicid( Integer pagesid ) {
	Map< String,Object > publicMap = new HashMap<>();
	MallPage page = mallPageDAO.selectById( pagesid );
	if ( CommonUtil.isNotEmpty( page ) ) {
	    WxPublicUsers publicUsers = wxPublicUserService.selectByUserId( page.getPagUserId() );
	    publicMap.put( "id", publicUsers.getId() );
	    publicMap.put( "bus_user_id", page.getPagUserId() );
	    publicMap.put( "head_img", publicUsers.getHeadImg() );
	}
	return publicMap;
    }

    @Override
    public int getMemberShopCartNum( HttpServletRequest request, Member member, List< Integer > memberList ) throws Exception {
	int shopCartNum = 0;
	if ( CommonUtil.isNotEmpty( member ) ) {
	    Object obj = MallSessionUtils.getShopCart( request );

	    if ( CommonUtil.isNotEmpty( obj ) ) {
		MallShopCart shopCart = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( obj ), MallShopCart.class );

		int count = addshopping( shopCart, member, request, null );
		if ( count > 0 ) {
		    MallSessionUtils.setShopCart( null, request );
		    request.setAttribute( "isAddShop", 1 );
		}
	    }

	    Map< String,Object > params = new HashMap<>();
	    params.put( "memberList", memberList );
	    //查询购物车的数量
	    params.put( "type", 0 );
	    params.put( "busUserId", member.getBusid() );
	    shopCartNum = mallShopCartDAO.selectShopCartNum( params );
	} else {
	    Map< String,Object > params = new HashMap<>();
	    String shopCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	    if ( shopCartIds != null && !"".equals( shopCartIds ) ) {
		params.put( "shopCartIds", shopCartIds.split( "," ) );
		shopCartNum = mallShopCartDAO.selectShopCartNum( params );
	    }

	}
	return shopCartNum;
    }

    @Override
    public int addshopping( MallShopCart obj, Member member, HttpServletRequest request, HttpServletResponse response ) {
	obj.setCreateTime( DateTimeKit.getNow() );
	int count = 0;
	List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	String cookieValue = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	if ( CommonUtil.isNotEmpty( member ) ) {
	    obj.setUserId( member.getId() );
	    obj.setBusUserId( member.getBusid() );
	    cartList = mallShopCartDAO.selectByShopCart( obj );
	} else {
	    MallProduct product = mallProductDAO.selectById( obj.getProductId() );
	    if ( product != null ) {
		obj.setBusUserId( product.getUserId() );
	    }
	    if ( CommonUtil.isNotEmpty( cookieValue ) ) {
		cartList = selectByShopCart( obj, cookieValue );
	    }
	}
	if ( cartList != null && cartList.size() > 0 ) {
	    Map< String,Object > map = cartList.get( 0 );

	    if ( CommonUtil.isNotEmpty( obj.getProSpecStr() ) ) {
		obj = getProSpecNum( obj.getProSpecStr(), obj );
	    }
	    obj.setId( CommonUtil.toInteger( map.get( "id" ) ) );
	    count = mallShopCartDAO.updateByShopCart( obj );
	} else {
	    count = mallShopCartDAO.insert( obj );
	    if ( CommonUtil.isEmpty( member ) ) {
		String value = obj.getId().toString();
		if ( CommonUtil.isNotEmpty( cookieValue ) ) {
		    value = cookieValue + "," + obj.getId();
		}
		CookieUtil.addCookie( response, CookieUtil.SHOP_CART_COOKIE_KEY, value, Constants.COOKIE_SHOP_CART_TIME );
	    }
	}

	if ( CommonUtil.isEmpty( member ) ) {
	    count = 1;
	    //saveShopCartBySession(obj, request);
	}
	return count;
    }

    /**
     * 把购物车信息保存到session
     */
    private void saveShopCartBySession( MallShopCart cart, HttpServletRequest request ) {
	String key = Constants.SESSION_KEY + "mall_shopcart";
	String key2 = Constants.SESSION_KEY + "mall_shopcart_id";
	boolean isFlag = true;
	HttpSession session = request.getSession();
	Object cartObj = session.getAttribute( key );
	List< MallShopCart > newCartList = new ArrayList<>();
	StringBuilder ids = new StringBuilder();
	if ( CommonUtil.isNotEmpty( cartObj ) ) {
	    List< MallShopCart > cartList = com.alibaba.fastjson.JSONArray.parseArray( cartObj.toString(), MallShopCart.class );
	    if ( cartList != null && cartList.size() > 0 ) {
		for ( MallShopCart mallShopCart : cartList ) {
		    if ( mallShopCart.getProductId().toString().equals( cart.getProductId().toString() ) ) {

			if ( CommonUtil.isNotEmpty( mallShopCart.getProSpecStr() ) && CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {//判断批发商品的规格
			    cart = getProSpecNum( mallShopCart.getProSpecStr(), cart );
			    mallShopCart.setProSpecStr( cart.getProSpecStr() );
			    mallShopCart.setProductNum( mallShopCart.getProductNum() + cart.getProductNum() );
			} else {
			    if ( CommonUtil.isNotEmpty( mallShopCart.getProductSpecificas() ) ) {
				if ( mallShopCart.getProductSpecificas().equals( cart.getProductSpecificas() ) ) {
				    mallShopCart.setProductNum( mallShopCart.getProductNum() + cart.getProductNum() );
				}
			    } else {
				mallShopCart.setProductNum( mallShopCart.getProductNum() + cart.getProductNum() );
			    }
			}
		    }

		    if ( !ids.toString().contains( mallShopCart.getProductId().toString() ) ) {
			ids.append( mallShopCart.getProductId() ).append( "," );
		    }
		    newCartList.add( mallShopCart );
		}
	    }
	}
	if ( newCartList == null || newCartList.size() == 0 ) {
	    newCartList.add( cart );
	    ids = new StringBuilder( cart.getProductId() + "," );
	}
	if ( isFlag ) {
	    session.setAttribute( key, JSONArray.fromObject( newCartList ) );
	    session.setAttribute( key2, ids.toString() );
	}
    }

    /**
     * 判断批发购物车的规格
     */
    @Override
    public MallShopCart getProSpecNum( String proSpecStr, MallShopCart cart ) {
	if ( CommonUtil.isNotEmpty( proSpecStr ) ) {
	    return cart;
	}
	JSONObject specIdObj = new JSONObject();

	JSONObject speObj = JSONObject.fromObject( proSpecStr );//商品规格和数量

	if ( CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {
	    JSONObject speObj2 = JSONObject.fromObject( cart.getProSpecStr() );

	    Iterator it = speObj.keys();

	    while ( it.hasNext() ) {
		String str = it.next().toString();
		if ( CommonUtil.isNotEmpty( speObj2.get( str ) ) ) {
		    JSONObject proObj = JSONObject.fromObject( speObj.get( str ) );
		    JSONObject proObj2 = JSONObject.fromObject( speObj2.get( str ) );

		    int num = CommonUtil.toInteger( proObj.get( "num" ) );
		    int num2 = CommonUtil.toInteger( proObj2.get( "num" ) );
		    num += num2;

		    proObj2.put( "num", num + "" );

		    specIdObj.put( str, proObj2.toString() );

		    speObj2.remove( str );
		} else {
		    specIdObj.put( str, speObj.get( str ) );

		    speObj2.remove( str );

		}
	    }
	    if ( speObj2 != null && speObj2.size() > 0 ) {
		Iterator it2 = speObj2.keys();

		while ( it2.hasNext() ) {
		    String str = it2.next().toString();
		    specIdObj.put( str, speObj2.get( str ) );
		}
	    }
	    cart.setProSpecStr( specIdObj.toString() );
	}
	return cart;
    }

    @Override
    public void shoppingdelect( String delects, String updStr, int type ) {
	if ( CommonUtil.isNotEmpty( delects ) ) {
	    if ( !delects.equals( "0" ) ) {
		if ( delects.contains( "0," ) ) {
		    delects = delects.substring( 2, delects.length() );
		}
		boolean isDelete = true;
		if ( type == 0 ) {//提交订单，并清除购物车或购物车的规格
		    String[] delSpli = delects.split( "," );
		    if ( delSpli != null && delSpli.length > 0 ) {
			for ( String str : delSpli ) {
			    //查询商品是否有多组规格
			    String sql = "select id,product_num,pro_spec_str from t_mall_shop_cart where id=" + str;
			    Map< String,Object > map = mallShopCartDAO.selectMapById( CommonUtil.toInteger( str ) );
			    if ( CommonUtil.isNotEmpty( map ) ) {
				JSONObject newSpecObj = new JSONObject();
				if ( CommonUtil.isNotEmpty( map.get( "pro_spec_str" ) ) ) {
				    int num = 0;
				    JSONObject obj = JSONObject.fromObject( map.get( "pro_spec_str" ) );
				    Set< String > set = obj.keySet();
				    for ( String key : set ) {
					if ( CommonUtil.isNotEmpty( obj.get( key ) ) ) {
					    JSONObject specObj = JSONObject.fromObject( obj.get( key ) );
					    if ( specObj.get( "isCheck" ).toString().equals( "0" ) ) {
						newSpecObj.put( key, obj.get( key ) );
						num += CommonUtil.toInteger( specObj.get( "num" ) );
					    }
					}
				    }
				    if ( num > 0 && newSpecObj != null && newSpecObj.size() > 0 ) {
					//修改购物车信息
					updateShopCartById( num, newSpecObj, CommonUtil.toInteger( map.get( "id" ) ) );

					isDelete = false;
				    }
				}
			    }
			}
		    }
		}
		if ( isDelete ) {
		    //		    String sql = "DELETE FROM  t_mall_shop_cart where id in (" + delects + ")";
		    List< Integer > list = JSONArray.toList( JSONArray.fromObject( delects.split( "," ) ), Integer.class );
		    //删除购物车信息
		    mallShopCartDAO.deleteBatchIds( list );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( updStr ) ) {
	    JSONArray arr = JSONArray.fromObject( updStr );
	    if ( arr != null && arr.size() > 0 ) {
		for ( Object object : arr ) {
		    JSONObject upObj = JSONObject.fromObject( object );
		    //修改购物车信息
		    updateShopCartById( upObj.getInt( "num" ), upObj.get( "proSpecStr" ), upObj.getInt( "id" ) );
		}
	    }
	}
    }

    private void updateShopCartById( int num, Object newSpecObj, int id ) {
	//修改购物车信息
	MallShopCart shopcart = new MallShopCart();
	shopcart.setProductNum( num );
	shopcart.setProSpecStr( newSpecObj.toString() );
	shopcart.setId( id );
	mallShopCartDAO.updateById( shopcart );
    }

    @Override
    public Map< String,Object > shoporder( String json, String totalnum, String totalprice, String memberId ) {
	Map< String,Object > map = new HashMap<>();
	JSONArray jsonArray = JSONArray.fromObject( json );
	StringBuilder shop_ids = new StringBuilder();
	for ( Object aJsonArray : jsonArray ) {
	    Map maps = (Map) aJsonArray;
	    String check = maps.get( "check" ).toString();
	    Integer id = Integer.valueOf( maps.get( "id" ).toString() );
	    //	    String num = maps.get( "num" ).toString();

	    MallShopCart shopcart = new MallShopCart();
	    shopcart.setProductNum( CommonUtil.toInteger( maps.get( "num" ) ) );
	    if ( CommonUtil.isNotEmpty( maps.get( "specStr" ) ) ) {
		shopcart.setProSpecStr( maps.get( "specStr" ).toString() );
	    }
	    if ( check.equals( "0" ) ) {
		shopcart.setIsCheck( 1 );
		shop_ids.append( id ).append( "," );
	    } else {
		shopcart.setIsCheck( 0 );
	    }
	    shopcart.setId( id );
	    mallShopCartDAO.updateById( shopcart );
	}
	map.put( "cartIds", shop_ids.substring( 0, shop_ids.length() - 1 ) );
	return map;
    }

    @Override
    public List< Map< String,Object > > storeList( Integer stoId, int type, int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	if ( CommonUtil.isNotEmpty( stoId ) && stoId > 0 ) {
	    params.put( "shopId", stoId );
	} else {
	    List< WsWxShopInfoExtend > shopList = wxShopService.queryWxShopByBusId( busUserId );
	    params.put( "wxShopList", shopList );
	}
	List< Map< String,Object > > list = mallProductGroupDAO.selectProductGroupByShopId( params );
	return list;
    }

    @Override
    public PageUtil product( Map< String,Object > params ) {
	params.put( "shopId", params.get( "stoId" ) );
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallProductDAO.selectCountByShopids( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallPage/choosePro.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > productList = mallProductDAO.selectProductByShopids( params );

	page.setSubList( productList );
	return page;
    }

    @Override
    public Map< String,Object > productSpecifications( Integer id, String inv_id ) {
	return mallProductInventoryService.productSpecifications( id, inv_id );
    }

    @Override
    public List< Map< String,Object > > guigePrice( Integer id ) {
	return mallProductInventoryService.guigePrice( id );
    }

    @Override
    public MallProductDetail shopdetails( Integer id ) {
	return mallProductDetailService.selectByProductId( id );
    }

    /**
     * 添加关键词搜索记录
     */
    @Override
    public void saveOrUpdateKeyWord( Map< String,Object > params, int shopid, int userId ) {
	if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
	    String proName = params.get( "proName" ).toString();
	    //保存到搜索关键字表
	    mallSearchKeywordService.insertSeachKeyWord( userId, shopid, proName );
	}
    }

    @Override
    public PageUtil productAllListNew( PhoneSearchProductDTO searchProductDTO, double discount, Member member ) {
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
	int rowCount = mallProductDAO.selectCountAllByShopidsNew( searchProductDTO );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	searchProductDTO.setMaxNum( pageSize );

	List< Map< String,Object > > list = mallProductDAO.selectProductAllByShopidsNew( searchProductDTO );

	list = getSearchProductParam( list, discount, searchProductDTO );
	page.setSubList( list );
	return page;
    }

    @Override
    public List< Map< String,Object > > getSearchProductParam( List< Map< String,Object > > list, double discount, PhoneSearchProductDTO searchProductDTO ) {
	List< Map< String,Object > > xlist = new ArrayList<>();
	List< Integer > proIds = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map1 : list ) {
		map1 = productGetPriceNew( map1, discount, searchProductDTO );
		xlist.add( map1 );

		if ( CommonUtil.isNotEmpty( map1.get( "id" ) ) ) {
		    proIds.add( CommonUtil.toInteger( map1.get( "id" ) ) );
		}
	    }
	    xlist = getProductImages( xlist, proIds );
	}
	return xlist;
    }

    private Map< String,Object > productGetPriceNew( Map< String,Object > map1, double discount, PhoneSearchProductDTO searchProductDTO ) {
	Map< String,Object > proMap = new HashMap<>();
	String is_specifica = map1.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
	double price = 0;//商品价格
	//	double hyPrice = 0;//会员价
	String image_url = "";//图片
	double costPrice = 0;//原价
	if ( CommonUtil.isNotEmpty( map1.get( "pro_cost_price" ) ) ) {
	    costPrice = CommonUtil.toDouble( map1.get( "pro_cost_price" ) );
	}
	if ( is_specifica.equals( "1" ) ) {
	    if ( CommonUtil.isNotEmpty( map1.get( "inv_price" ) ) ) {
		price = CommonUtil.toDouble( map1.get( "inv_price" ) );
		costPrice = CommonUtil.toDouble( map1.get( "pro_price" ) );
	    }
	    if ( CommonUtil.isNotEmpty( map1.get( "specifica_img_url" ) ) ) {
		image_url = CommonUtil.toString( map1.get( "specifica_img_url" ) );
	    }
	}
	if ( CommonUtil.isEmpty( image_url ) && CommonUtil.isNotEmpty( map1.get( "image_url" ) ) ) {
	    image_url = CommonUtil.toString( map1.get( "image_url" ) );
	}
	if ( price <= 0 ) {
	    price = CommonUtil.toDouble( map1.get( "pro_price" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	String is_member_discount = map1.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
	if ( is_member_discount.equals( "1" ) ) {
	    if ( price > 0 && discount != 1 ) {
		double hyPrice = CommonUtil.toDouble( df.format( price * discount ) );
		proMap.put( "hyPrice", df.format( hyPrice ) );
		costPrice = price;
	    }
	}
	if ( CommonUtil.isNotEmpty( searchProductDTO ) ) {
	    if ( searchProductDTO.getType() == 5 ) {
		price = CommonUtil.toDouble( map1.get( "change_fenbi" ) );
		proMap.put( "unit", "粉币" );
	    } else if ( searchProductDTO.getType() > 0 && CommonUtil.isNotEmpty( map1.get( "price" ) ) ) {
		price = CommonUtil.toDouble( map1.get( "price" ) );
	    }
	}
	if ( price > 0 ) {
	    proMap.put( "price", df.format( price ) );
	}
	if ( costPrice == 0 || costPrice <= price ) {
	    costPrice = 0;
	}
	proMap.put( "image_url", PropertiesUtil.getResourceUrl() + image_url );
	if ( CommonUtil.isNotEmpty( map1.get( "pro_type_id" ) ) && CommonUtil.isNotEmpty( map1.get( "member_type" ) ) ) {
	    int proTypeId = CommonUtil.toInteger( map1.get( "pro_type_id" ) );
	    int memberType = CommonUtil.toInteger( map1.get( "member_type" ) );
	    if ( proTypeId == 2 && memberType > 0 ) {
		proMap.put( "return_url", "/phoneMemberController/" + map1.get( "user_id" ) + "/79B4DE7C/findMember_1.do" );
	    }
	}

	int saleTotal = 0;//销量
	if ( CommonUtil.isNotEmpty( map1.get( "pro_sale_total" ) ) ) {
	    saleTotal += CommonUtil.toInteger( map1.get( "pro_sale_total" ) );
	}
	if ( CommonUtil.isNotEmpty( map1.get( "sales_base" ) ) ) {
	    saleTotal += CommonUtil.toInteger( map1.get( "sales_base" ) );
	}
	proMap.put( "pro_cost_price", costPrice );

	proMap.put( "pro_name", map1.get( "pro_name" ) );
	if ( CommonUtil.isNotEmpty( map1.get( "pro_label" ) ) ) {
	    proMap.put( "pro_label", map1.get( "pro_label" ) );
	}

	proMap.put( "sale_total", saleTotal );
	proMap.put( "shop_id", map1.get( "shop_id" ) );
	proMap.put( "id", map1.get( "id" ) );

	if ( CommonUtil.isNotEmpty( searchProductDTO ) && searchProductDTO.getType() > 0 ) {
	    if ( CommonUtil.isNotEmpty( map1.get( "endTime" ) ) && CommonUtil.isEmpty( map1.get( "activityStatus" ) ) ) {
		Date endTimes = DateTimeKit.parse( map1.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTimes = DateTimeKit.parse( map1.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
		Date date = new Date();
		proMap.put( "times", ( endTimes.getTime() - nowTime.getTime() ) / 1000 );
		int status = -1;

		if ( startTimes.getTime() > date.getTime() && endTimes.getTime() > date.getTime() ) {//未开始
		    status = 0;
		    proMap.put( "times ", ( startTimes.getTime() - nowTime.getTime() ) / 1000 );
		} else if ( startTimes.getTime() <= date.getTime() && date.getTime() < endTimes.getTime() ) {//正在进行
		    status = 1;
		}
		proMap.put( "activityStatus", status );//活动状态 0未开始 1正在进行
	    }
	}
	if ( CommonUtil.isNotEmpty( map1.get( "activityId" ) ) ) {
	    proMap.put( "activityId", map1.get( "activityId" ) );
	}
	if ( CommonUtil.isNotEmpty( map1.get( "times" ) ) ) {
	    proMap.put( "times", map1.get( "times" ) );
	}
	if ( CommonUtil.isNotEmpty( map1.get( "activityStatus" ) ) ) {
	    proMap.put( "activityStatus", map1.get( "activityStatus" ) );
	}
	if ( CommonUtil.isNotEmpty( map1.get( "peopleNum" ) ) ) {
	    proMap.put( "peopleNum", map1.get( "peopleNum" ) );//拼团人数
	}
	if ( CommonUtil.isNotEmpty( searchProductDTO ) && searchProductDTO.getType() == 4 ) {
	    proMap.put( "lowerPrice", map1.get( "lowerPrice" ) );//降价金额（每多少分钟降价多少元）
	    proMap.put( "lowerPriceTime", map1.get( "lowerPriceTime" ) );//降价时间（每多少分钟）
	    proMap.put( "lowestPrice", map1.get( "lowestPrice" ) );//最低价格
	    proMap.put( "aucType", map1.get( "aucType" ) );//拍卖类型 1 降价拍 2升价拍
	}
	proMap.put( "user_id", map1.get( "user_id" ) );
	return proMap;
    }

    @Override
    public List< Map< String,Object > > getProductImages( List< Map< String,Object > > xlist, List< Integer > proIds ) {
	List< Map< String,Object > > newList = new ArrayList< Map< String,Object > >();
	if ( proIds != null && proIds.size() > 0 ) {
	    //查询商品图片
	    Map< String,Object > imgMaps = new HashMap< String,Object >();
	    imgMaps.put( "isMainImages", 1 );
	    imgMaps.put( "assType", 1 );
	    imgMaps.put( "assIds", proIds );
	    List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssIds( imgMaps );
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( Map< String,Object > maps : xlist ) {
		    String id = maps.get( "id" ).toString();
		    for ( int i = 0; i < imageList.size(); i++ ) {
			Map< String,Object > imageMaps = imageList.get( i );
			String assIds = imageMaps.get( "ass_id" ).toString();
			if ( CommonUtil.isNotEmpty( imageMaps.get( "image_url" ) ) && assIds.equals( id ) ) {
			    maps.put( "image_url", PropertiesUtil.getResourceUrl() + imageMaps.get( "image_url" ) );
			    imageList.remove( i );
			    break;
			}
		    }
		    newList.add( maps );
		}
	    }
	    if ( newList != null && newList.size() > 0 ) {
		xlist = new ArrayList<>();
		xlist.addAll( newList );
	    }
	}

	/*if ( specImgIds != null && specImgIds.length > 0 ) {
	    newList = new ArrayList< Map< String,Object > >();
	    //查询查询规格图片
	    List< MallProductSpecifica > specList = mallProductSpecificaService.selectBySpecId( specImgIds );
	    if ( specList != null && specList.size() > 0 ) {
		for ( Map< String,Object > maps : xlist ) {
		    if ( CommonUtil.isNotEmpty( maps.get( "specifica_img_id" ) ) ) {
			String imgIds = maps.get( "specifica_img_id" ).toString();
			for ( int i = 0; i < specList.size(); i++ ) {
			    MallProductSpecifica spec = specList.get( i );
			    if ( CommonUtil.isNotEmpty( spec.getSpecificaImgUrl() ) && imgIds.equals( spec.getId().toString() ) ) {
				maps.put( "image_url", PropertiesUtil.getResourceUrl() + spec.getSpecificaImgUrl() );
				specList.remove( i );
				break;
			    }
			}
		    }
		    newList.add( maps );
		}
	    }
	    if ( newList != null && newList.size() > 0 ) {
		xlist = new ArrayList< Map< String,Object > >();
		xlist.addAll( newList );
	    }

	}*/
	return xlist;
    }

    @Override
    public Map< String,Object > productGetPrice( Map< String,Object > map1, double discount, boolean isPifa ) {
	String is_specifica = map1.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
	double price = 0;
	double hyPrice = 0;
	String image_url = "";
	int rType = 0;
	if ( CommonUtil.isNotEmpty( map1.get( "rType" ) ) ) {
	    rType = CommonUtil.toInteger( map1.get( "rType" ) );
	}
	if ( is_specifica.equals( "1" ) ) {
	    if ( CommonUtil.isNotEmpty( map1.get( "inv_price" ) ) ) {
		price = CommonUtil.toDouble( map1.get( "inv_price" ) );
	    }
	    if ( CommonUtil.isNotEmpty( map1.get( "specifica_img_url" ) ) ) {
		image_url = CommonUtil.toString( map1.get( "specifica_img_url" ) );
	    }
	}
	if ( CommonUtil.isEmpty( image_url ) && CommonUtil.isNotEmpty( map1.get( "image_url" ) ) ) {
	    image_url = CommonUtil.toString( map1.get( "image_url" ) );
	}
	if ( price <= 0 ) {
	    price = CommonUtil.toDouble( map1.get( "pro_price" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	String is_member_discount = map1.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
	if ( is_member_discount.equals( "1" ) ) {
	    if ( price > 0 && discount != 1 ) {
		hyPrice = CommonUtil.toDouble( df.format( price * discount ) );

	    }
	}
	if ( rType == 1 ) {
	    price = CommonUtil.toInteger( map1.get( "change_integral" ) );
	    map1.put( "unit", "积分" );
	} else if ( rType == 2 ) {
	    price = CommonUtil.toInteger( map1.get( "change_fenbi" ) );
	    map1.put( "unit", "粉币" );
	}
	if ( price > 0 ) {
	    map1.put( "price", df.format( price ) );
	}
	if ( hyPrice > 0 ) {
	    map1.put( "hyPrice", df.format( hyPrice ) );
	}
	map1.put( "image_url", PropertiesUtil.getResourceUrl() + image_url );
	String return_url = "/mallPage/" + map1.get( "id" ) + "/" + map1.get( "shop_id" ) + "/79B4DE7C/phoneProduct.do?rType=" + rType;
	if ( CommonUtil.isNotEmpty( map1.get( "pro_type_id" ) ) && CommonUtil.isNotEmpty( map1.get( "member_type" ) ) ) {
	    int proTypeId = CommonUtil.toInteger( map1.get( "pro_type_id" ) );
	    int memberType = CommonUtil.toInteger( map1.get( "member_type" ) );
	    if ( proTypeId == 2 && memberType > 0 ) {
		return_url = "/phoneMemberController/" + map1.get( "user_id" ) + "/79B4DE7C/findMember_1.do";
	    }
	}
	map1.put( "return_url", return_url );
	double costPrice = 0;
	if ( CommonUtil.isNotEmpty( map1.get( "pro_cost_price" ) ) ) {
	    costPrice = CommonUtil.toDouble( map1.get( "pro_cost_price" ) );
	    if ( costPrice == 0 || costPrice <= price ) {
		costPrice = 0;
	    }
	}
	map1.put( "pro_cost_price", costPrice );

	double pfPrice = mallPifaService.getPifaPriceByProIds( isPifa, CommonUtil.toInteger( map1.get( "id" ) ) );
	if ( pfPrice >= 0 ) {
	    map1.put( "pfPrice", df.format( pfPrice ) );
	}
	return map1;
    }

    @Override
    public List< Map< String,Object > > shoppage( Integer shopid ) {
	MallStore store = mallStoreDAO.selectById( shopid );
	if ( CommonUtil.isEmpty( store ) ) {
	    return null;
	}
	if ( CommonUtil.isEmpty( store.getWxShopId() ) ) {return null;}
	WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
	if ( shopInfo == null ) {return null;}
	if ( shopInfo.getStatus() == -1 ) {return null;}
	Map< String,Object > params = new HashMap<>();
	params.put( "wxShopId", store.getWxShopId() );
	return mallPageDAO.selectPageByWxShopId( params );
    }

    @Override
    public Map< String,Object > selUser( Integer shopid ) {
	MallStore store = mallStoreService.selectById( shopid );
	if ( CommonUtil.isNotEmpty( store ) ) {
	    if ( store.getIsDelete() == 0 ) {
		BusUser user = busUserService.selectById( store.getStoUserId() );
		Map< String,Object > userMap = new HashMap<>();
		userMap.put( "id", store.getStoUserId() );//商家id
		userMap.put( "pid", busUserService.getMainBusId( store.getStoUserId() ) );//总账号id
		userMap.put( "advert", user.getAdvert() );//是否显示技术支持
		userMap.put( "bus_user_id", store.getStoUserId() );//商家id
		userMap.put( "wx_shop_id", store.getWxShopId() );//微信门店id
		return userMap;
	    }
	}
	return null;
    }

    @Override
    public Map< String,Object > publicMapByUserId( Integer userId ) {
	WxPublicUsers publicUsers = wxPublicUserService.selectByUserId( userId );
	Map< String,Object > publicMap = new HashMap<>();
	publicMap.put( "public_id", publicUsers.getId() );//公众号id
	publicMap.put( "bus_user_id", userId );//商家id
	return publicMap;
    }

    @Override
    public int counttime( Integer shopid, String time ) {
	/*String sql = "SELECT count(1) FROM t_mall_product WHERE shop_id=" + shopid + "  AND is_publish=1 AND check_status=1 AND is_delete=0  and check_time>'" + time + "'";
	return daoUtil.queryForInt( sql );*/
	Wrapper< MallProduct > wrapper = new EntityWrapper<>();
	wrapper.where( "shop_id={0}  AND is_publish=1 AND check_status=1 AND is_delete=0", shopid );
	wrapper.ge( "check_time", time );
	return mallProductDAO.selectCount( wrapper );
    }

    @Override
    public String getProvince( String ip ) {
	String key = Constants.REDIS_KEY + "member_ip";
	if ( JedisUtil.exists( key ) ) {
	    Object value = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( value ) ) {
		return value.toString();
	    }
	}
	String logCity;
	try {
	    String address = AddressUtil.provinceadd( ip );//获取注册地址
	    if ( address == null || address.equals( "" ) ) {
		address = "广东省";
	    }
	    Map map = wxShopService.queryBasisByName( address );
	    if ( map != null ) {
		logCity = map.get( "id" ).toString();
	    } else {
		logCity = "2136";
	    }
	} catch ( Exception e ) {
	    logCity = "2136";
	    logger.error( "获取地址异常：" + e.getMessage() );
	}
	JedisUtil.set( key, logCity, Constants.REDIS_SECONDS );
	return logCity;
    }

    @Override
    public int updateProViewPage( String proId, Map proMap ) {
	String key = Constants.REDIS_KEY + "proViewNum";
	int viewNum = 0;
	String viewNums = "";
	if ( JedisUtil.hExists( key, proId ) ) {
	    viewNums = JedisUtil.maoget( key, proId );
	}
	if ( viewNums == null || viewNums.equals( "" ) ) {
	    if ( CommonUtil.isNotEmpty( proMap.get( "views_num" ) ) ) {
		viewNums = proMap.get( "views_num" ).toString();
	    }
	}
	if ( viewNums != null && !viewNums.equals( "" ) ) {
	    viewNum = CommonUtil.toInteger( viewNums );
	}
	if ( viewNum + 1 < 1000000000 ) {
	    JedisUtil.map( key, proId, ( viewNum + 1 ) + "" );
	}
	return viewNum;
    }

    /**
     * 根据门店id查询是否有积分商品
     */
    @Override
    public List< Integer > shopIsJiFen( List< Integer > list ) {
	List< Integer > resultList = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( Integer shopId : list ) {
		/*String sql = "SELECT p.id FROM t_mall_store s LEFT JOIN t_mall_integral p ON p.shop_id = s.id WHERE s.is_delete=0 AND p.is_delete=0 AND p.is_use=1  AND p.start_time <= NOW() AND NOW() < p.end_time  AND s.wx_shop_id="
				+ shopId;*/
		List< Map< String,Object > > proList = mallIntegralDAO.selectIntegralNumByWxShopId( shopId );
		if ( proList == null || proList.size() == 0 ) {
		    continue;
		}
		resultList.add( shopId );
	    }
	}
	return resultList;
    }

    /**
     * 根据门店id查询是否有粉币商品
     */
    @Override
    public List< Integer > shopIsFenbi( List< Integer > list ) {
	List< Integer > resultList = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( Integer shopId : list ) {
		/*String sql = "select p.id from t_mall_store s left join t_mall_product p on p.shop_id = s.id where s.is_delete=0 and p.is_fenbi_change_pro=1 and p.change_fenbi>0 and p.is_delete=0 and p.is_publish=1 and p.check_status=1 and s.wx_shop_id="
				+ shopId;*/
		List< Map< String,Object > > proList = mallProductDAO.selectFenbiNumByWxShopId( shopId );
		if ( proList == null || proList.size() == 0 ) {
		    continue;
		}
		resultList.add( shopId );
	    }
	}
	return resultList;
    }

    @Override
    public List< Map< String,Object > > typePage( int stoId, BusUser user ) {
	Map< String,Object > maps = new HashMap<>();
	//		maps.put("status", 1);
	maps.put( "shopId", stoId );
	List< Map< String,Object > > typeList = new ArrayList<>();
	Map< String,Object > resultMap = new HashMap<>();
	/*String sql = "SELECT COUNT(p.id) FROM t_mall_integral s LEFT JOIN t_mall_product p ON p.`id` = s.`product_id` WHERE s.is_delete=0 AND p.is_delete=0 AND p.is_publish=1 AND p.check_status=1 AND s.start_time <= NOW() AND NOW() < s.end_time  AND s.shop_id="
			+ stoId;*/
	int count = mallIntegralDAO.selectIntegralNumByShopId( stoId );
	if ( count > 0 ) {
	    resultMap.put( "name", "积分商品" );
	    resultMap.put( "url", "/phoneIntegral/" + stoId + "/79B4DE7C/toIndex.do?uId=" + user.getId() );
	    typeList.add( resultMap );
	}

	/*sql = "select count(p.id) from t_mall_store s left join t_mall_product p on p.shop_id = s.id where s.is_delete=0 and p.is_fenbi_change_pro=1 and p.change_fenbi>0 and p.is_delete=0 and p.is_publish=1 and p.check_status=1 and s.id="
			+ stoId;*/
	count = mallProductDAO.selectFenbiNumByShopId( stoId );
	if ( count > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "粉币商品" );
	    resultMap.put( "url", "/mallPage/" + stoId + "/79B4DE7C/shoppingall.do?rType=2" );
	    typeList.add( resultMap );
	}

	List< Map< String,Object > > productList = mallGroupBuyService.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "团购商品" );
	    resultMap.put( "url", "/mGroupBuy/" + stoId + "/79B4DE7C/groupbuyall.do" );
	    typeList.add( resultMap );
	}
	//判断用户是否有正在进行中的秒杀商品
	List< Map< String,Object > > seckillList = mallSeckillService.selectgbSeckillByShopId( maps );
	if ( seckillList != null && seckillList.size() > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "秒杀商品" );
	    resultMap.put( "url", "/mSeckill/" + stoId + "/79B4DE7C/seckillall.do" );
	    typeList.add( resultMap );
	}
	//判断用户是否有正在进行中的拍卖商品
	List< Map< String,Object > > auctionList = mallAuctionService.selectgbAuctionByShopId( maps );
	if ( auctionList != null && auctionList.size() > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "拍卖商品" );
	    resultMap.put( "url", "/mAuction/" + stoId + "/79B4DE7C/auctionall.do" );
	    typeList.add( resultMap );
	}
	//判断用户是否有正在进行中的预售商品
	List< Map< String,Object > > presaleList = mallPresaleService.selectgbPresaleByShopId( maps );
	if ( presaleList != null && presaleList.size() > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "预售商品" );
	    resultMap.put( "url", "/phonePresale/" + stoId + "/79B4DE7C/presaleall.do" );
	    typeList.add( resultMap );
	}
	List< Map< String,Object > > pifaList = mallPifaService.selectgbPifaByShopId( maps );
	if ( pifaList != null && pifaList.size() > 0 ) {
	    resultMap = new HashMap<>();
	    resultMap.put( "name", "批发商品" );
	    resultMap.put( "url", "/phoneWholesaler/" + stoId + "/79B4DE7C/wholesalerall.do" );
	    typeList.add( resultMap );
	}
	return typeList;
    }

    @Override
    public void getCustomer( HttpServletRequest request, int userid ) {

	if ( CommonUtil.isNotEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
	    int shopId = CommonUtil.toInteger( request.getSession().getAttribute( "shopId" ) );
	    MallStore store = mallStoreService.selectById( shopId );
	    if ( CommonUtil.isNotEmpty( store ) ) {
		if ( CommonUtil.isNotEmpty( store.getStoQqCustomer() ) ) {
		    request.setAttribute( "qq", store.getStoQqCustomer() );
		}
	    }
	}
	if ( userid > 0 ) {
	    request.setAttribute( "userid", userid );//统计商城访问记录需要用的
	}
    }

    @Override
    public void getSearchLabel( HttpServletRequest request, int shopId, Member member, Map< String,Object > params ) {
	Map< String,Object > map = new HashMap<>();
	if ( shopId > 0 ) {
	    map.put( "shopId", shopId );
	}
	//查询搜索推荐
	List< Map< String,Object > > labelList = mallSearchLabelDAO.selectByUser( map );
	if ( labelList != null && labelList.size() > 0 ) {
	    request.setAttribute( "labelList", labelList );
	}
	if ( CommonUtil.isNotEmpty( member ) ) {
	    map.put( "userId", member.getId() );
	    //查询历史搜索
	    List< MallSearchKeyword > keywordList = mallSearchKeywordService.selectByUser( map );
	    if ( keywordList != null && keywordList.size() > 0 ) {
		request.setAttribute( "keywordList", keywordList );
	    }
	}

	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
		//		String sql = "select id,group_p_id,id from t_mall_group where group_name like '%" + params.get( "proName" ) + "%'";
		Map< String,Object > groupMap = mallGroupService.selectGroupBySearchName( params.get( "proName" ).toString() );
		if ( CommonUtil.isNotEmpty( groupMap ) ) {
		    if ( CommonUtil.isNotEmpty( groupMap.get( "group_p_id" ) ) ) {
			if ( CommonUtil.toInteger( groupMap.get( "group_p_id" ) ) > 0 ) {
			    request.setAttribute( "groupPId", groupMap.get( "group_p_id" ) );

			}
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "groupId" ) ) ) {
		MallGroup group = mallGroupService.selectById( CommonUtil.toInteger( params.get( "groupId" ) ) );
		if ( CommonUtil.isNotEmpty( group ) ) {
		    if ( group.getGroupPId() > 0 ) {
			request.setAttribute( "groupPId", group.getGroupPId() );
		    }
		    request.setAttribute( "groupName", group.getGroupName() );
		}
	    }
	}
    }

    @Override
    public List< MallProductParam > getProductParam( int proId ) {
	return mallProductParamService.getParamByProductId( proId );
    }

    @Override
    public List< Map< String,Object > > getProductCollectByMemberId( Member member, double discount ) {
	List< Map< String,Object > > xlist = new ArrayList<>();
	Map< String,Object > params = new HashMap<>();
	List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
	if ( memberList != null && memberList.size() > 1 ) {
	    params.put( "memberIdList", memberList );
	} else {
	    params.put( "memberId", member.getId() );
	}
	List< Map< String,Object > > list = mallCollectDAO.selectCollectByMemberId( params );
	for ( Map< String,Object > map1 : list ) {
	    map1 = productGetPrice( map1, discount, false );
	    xlist.add( map1 );
	}
	return xlist;
    }

    /**
     * 查询门店或店铺是否已经删除
     */
    public boolean wxShopIsDelete( int shopId, WsWxShopInfo wsWxShopInfo ) throws Exception {
	MallStore store = mallStoreService.selectById( shopId );
	if ( CommonUtil.isNotEmpty( store ) ) {
	    if ( store.getIsDelete() == 0 ) {
		if ( CommonUtil.isNotEmpty( store.getWxShopId() ) && store.getWxShopId() > 0 ) {
		    if ( CommonUtil.isEmpty( wsWxShopInfo ) ) {
			wsWxShopInfo = wxShopService.getShopById( store.getWxShopId() );
		    }
		    if ( CommonUtil.isNotEmpty( wsWxShopInfo ) ) {
			if ( !CommonUtil.toString( wsWxShopInfo.getStatus() ).equals( "-1" ) ) {
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    /**
     * TODo 待优化
     *
     * @param stoId
     * @param params
     *
     * @return
     */
    @Override
    public List< Map< String,Object > > productPresale( Integer stoId, Map< String,Object > params ) {

	params.put( "shopId", params.get( "stoId" ) );
	if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
	    params.put( "searchName", params.get( "proName" ) );
	}
	List< Map< String,Object > > list = mallPresaleDAO.selectBySearchNames( params );
	List< Map< String,Object > > proList = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		String startTimes = map.get( "sale_start_time" ).toString();

		Date endTime = DateTimeKit.parse( map.get( "sale_end_time" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTime = DateTimeKit.parse( startTimes, "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

		long nowTimes = new Date().getTime();
		int status = -1;
		long times = 0;
		if ( startTime.getTime() > nowTimes && endTime.getTime() > nowTimes ) {//预售还未开始
		    status = 0;
		    times = ( startTime.getTime() - nowTime.getTime() ) / 1000;
		} else if ( startTime.getTime() <= nowTimes && nowTimes < endTime.getTime() ) {//预售正在进行
		    status = 1;
		    times = ( endTime.getTime() - nowTime.getTime() ) / 1000;
		}
		String proImgUrl = "";
		double proPrice = 0;
		if ( CommonUtil.isNotEmpty( map.get( "is_specifica" ) ) ) {
		    if ( map.get( "is_specifica" ).toString().equals( "1" ) ) {
			if ( CommonUtil.isNotEmpty( map.get( "specifica_img_url" ) ) ) {
			    proImgUrl = map.get( "specifica_img_url" ).toString();
			}
			if ( CommonUtil.isNotEmpty( map.get( "inv_price" ) ) ) {
			    proPrice = CommonUtil.toDouble( map.get( "inv_price" ) );
			}
		    }
		}
		if ( CommonUtil.isEmpty( proImgUrl ) ) {
		    proImgUrl = map.get( "image_url" ).toString();
		}
		if ( proPrice == 0 ) {
		    proPrice = CommonUtil.toDouble( map.get( "pro_price" ) );
		}
		Map< String,Object > presaleMap = new HashMap<>();
		presaleMap.put( "presaleId", map.get( "presaleId" ) );
		//查询预售商品订购的数量
		int orderNum = mallPresaleDepositDAO.selectBuyCountByPreId( presaleMap );
		if ( CommonUtil.isNotEmpty( map.get( "order_num" ) ) ) {
		    orderNum = orderNum + CommonUtil.toInteger( map.get( "order_num" ) );
		}
		map.put( "orderNum", orderNum );
		map.put( "proImgUrl", proImgUrl );
		map.put( "status", status );
		map.put( "proPrice", proPrice );
		map.put( "times", times );
		//		map.put( "preId", map.get( "presaleId" ) );
		proList.add( map );
	    }
	}
	return proList;
    }

    /**
     * 获取预售商品
     */
    public Map< String,Object > getProductPresale( Map< String,Object > map, Member member ) {
	if ( CommonUtil.isNotEmpty( map.get( "preId" ) ) ) {
	    MallPresale presale = mallPresaleDAO.selectById( CommonUtil.toInteger( map.get( "preId" ) ) );

	    String startTimes = presale.getSaleStartTime();

	    Date endTime = DateTimeKit.parse( presale.getSaleEndTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date startTime = DateTimeKit.parse( startTimes, "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

	    long nowTimes = new Date().getTime();
	    int status = -1;
	    long times = 0;
	    if ( startTime.getTime() > nowTimes && endTime.getTime() > nowTimes ) {//预售还未开始
		status = 0;
		times = ( startTime.getTime() - nowTime.getTime() ) / 1000;
	    } else if ( startTime.getTime() <= nowTimes && nowTimes < endTime.getTime() ) {//预售正在进行
		status = 1;
		times = ( endTime.getTime() - nowTime.getTime() ) / 1000;
	    }

	    Map< String,Object > presaleMap = new HashMap<>();
	    presaleMap.put( "presaleId", presale.getId() );
	    //查询预售商品订购的数量
	    int orderNum = mallPresaleDepositDAO.selectBuyCountByPreId( presaleMap );
	    if ( CommonUtil.isNotEmpty( presale.getOrderNum() ) ) {
		orderNum = orderNum + presale.getOrderNum();
	    }
	    map.put( "orderNum", orderNum );
	    map.put( "status", status );
	    map.put( "times", times );
	    map.put( "preId", presale.getId() );

	    int isRemain = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		MallPresaleMessageRemind message = new MallPresaleMessageRemind();
		message.setRemindUserId( member.getId() );
		message.setPresaleId( presale.getId() );
		message.setIsOpen( 1 );
		message.setIsRemind( 0 );
		MallPresaleMessageRemind preMessage = mallPresaleMessageRemindDAO.selectByPresale( message );
		if ( preMessage != null ) {
		    isRemain = 1;
		}
	    }
	    map.put( "isRemain", isRemain );

	}

	return map;
    }

    private Map< String,Object > getProductHome( Map< String,Object > map3, Member member, String http, double discount, MallPaySet set, boolean isPifa ) {
	Map< String,Object > map2 = new HashMap<>();
	map2.put( "id", map3.get( "id" ) );
	if ( CommonUtil.isNotEmpty( map3.get( "userId" ) ) ) {
	    if ( CommonUtil.isNotEmpty( map3.get( "pro_type_id" ) ) && CommonUtil.isNotEmpty( map3.get( "member_type" ) ) ) {
		if ( CommonUtil.toInteger( map3.get( "pro_type_id" ) ) == 2 && CommonUtil.toInteger( map3.get( "pro_type_id" ) ) > 0 ) {
		    map2.put( "url", "/phoneMemberController/" + map3.get( "userId" ) + "/79B4DE7C/findMember_1.do" );
		}
	    }
	}
	map2.put( "title", map3.get( "pro_name" ) );
	String is_specifica = map3.get( "is_specifica" ).toString();//有规格的话，取自规格里面默认的价位和图片，没有的话获取本身的图片
	DecimalFormat df = new DecimalFormat( "######0.00" );
	String isMemberCount = map3.get( "is_member_discount" ).toString();
	if ( is_specifica.equals( "1" ) && CommonUtil.isNotEmpty( map3.get( "inv_price" ) ) ) {
	    if ( CommonUtil.isNotEmpty( member ) ) {
		double price = Double.parseDouble( map3.get( "inv_price" ).toString() );
		double newPrice = CommonUtil.toDouble( df.format( price * discount ) );
		if ( newPrice <= 0 ) {
		    newPrice = 0.01;
		}
		//				map2.put("price",newPrice);
		map2.put( "price", price );
		if ( isMemberCount.equals( "1" ) && discount > 0 && discount < 1 ) {
		    map2.put( "yjPrice", price );
		    map2.put( "hyPrice", newPrice );
		}
	    } else {
		map2.put( "price", map3.get( "inv_price" ) );
	    }
	}
	if ( is_specifica.equals( "0" ) || CommonUtil.isEmpty( map2.get( "price" ) ) ) {
	    if ( CommonUtil.isNotEmpty( member ) ) {
		double price = Double.parseDouble( map3.get( "pro_price" ).toString() );
		double newPrice = CommonUtil.toDouble( df.format( price * discount ) );
		if ( newPrice <= 0 ) {
		    newPrice = 0.01;
		}
		//				map2.put("price",newPrice);
		map2.put( "price", price );

		if ( isMemberCount.equals( "1" ) && discount > 0 && discount < 1 ) {
		    map2.put( "yjPrice", price );
		    map2.put( "hyPrice", newPrice );
		}
	    } else {
		map2.put( "price", map3.get( "pro_price" ) );
	    }
	}
	if ( CommonUtil.isNotEmpty( map3.get( "specifica_img_url" ) ) ) {
	    map2.put( "src", http + map3.get( "specifica_img_url" ) );
	} else {
	    map2.put( "src", http + map3.get( "image_url" ) );
	}
	if ( CommonUtil.isNotEmpty( map3.get( "pro_cost_price" ) ) && CommonUtil.isEmpty( map2.get( "yjPrice" ) ) ) {
	    double costPrice = CommonUtil.toDouble( map3.get( "pro_cost_price" ) );
	    if ( costPrice > 0 && costPrice > CommonUtil.toDouble( map2.get( "price" ) ) ) {
		map2.put( "yjPrice", map3.get( "pro_cost_price" ) );
	    }
	}
	String is_delete = map3.get( "is_delete" ).toString();
	String is_publish = map3.get( "is_publish" ).toString();
	map2.put( "is_delete", is_delete );
	map2.put( "is_publish", is_publish );
	/*Map< String,Object > params = new HashMap<>();
	params.put( "pfType", 1 );
	params.put( "product_id", map3.get( "id" ) );*/
	//查询商品是否已经加入批发
	if ( isPifa ) {
	    if ( CommonUtil.isNotEmpty( set ) ) {
		map2.put( "pfRemark", set.getPfRemark() );
	    }
	    if ( map3.containsKey( "pfPrice" ) ) {
		map2.put( "pfPrice", map3.get( "pfPrice" ) );
	    }
	}
		/*List<MallPifa> pifaList = mallPifaMapper.selectStartPiFaByProductId(params);
		boolean isPfPrice = false;
		if(pifaList != null && pifaList.size() > 0 && isPifa){
			MallPifa pifa = pifaList.get(0);
			if(CommonUtil.isNotEmpty(pifa.getPfPrice())){
				isPfPrice = true;
				map2.put("pfPrice", df.format(pifa.getPfPrice()));
			}
		}
		if(!isPfPrice){
			map2.remove("pfPrice");
		}*/
	if ( CommonUtil.isNotEmpty( map3.get( "pro_label" ) ) ) {
	    map2.put( "pro_label", map3.get( "pro_label" ) );
	}
	return map2;
    }

    @Override
    public Map< String,Object > getCardReceive( int receiveId ) {
	return cardService.findDuofenCardByReceiveId( receiveId );
    }

    @Override
    public void mergeShoppCart( Member member, HttpServletRequest request, HttpServletResponse response ) {
	//1.得到所有cookie中的商品
	//2.遍历，查询该商品在会员购物车中是否存在，  存在则修改数据(数量+1)，并删除cookie商品 ，不存在则新增
	String shoppCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	if ( shoppCartIds == null || "".equals( shoppCartIds ) ) {
	    return;
	}
	Wrapper< MallShopCart > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "id,product_id,shop_id,product_specificas,user_id,product_num" );
	//	wrapper.isNotNull( "user_id" );
	wrapper.in( "id", shoppCartIds.split( "," ) );
	wrapper.where( "id", 0 );
	List< Map< String,Object > > list = mallShopCartDAO.selectMaps( wrapper );
	StringBuilder id = new StringBuilder();//购物车id
	if ( list != null && list.size() > 0 ) {
	    mallShopCartDAO.updateShopCarts( member.getId(), member.getBusid(), shoppCartIds.split( "," ) );
	    List< Integer > ids = new ArrayList<>();
	    String deletes = "";
	    for ( Map< String,Object > map : list ) {
		int product_num = CommonUtil.toInteger( map.get( "product_num" ) );
		wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect( "id,product_num" );
		wrapper.where( "product_id = {0} AND shop_id={1} AND product_specificas= {2} AND user_id={3}", map.get( "product_id" ), map.get( "shop_id" ),
				map.get( "product_specificas" ), member.getId() );
		List< Map< String,Object > > countList = mallShopCartDAO.selectMaps( wrapper );
		if ( countList.size() > 0 ) {
		    int num = CommonUtil.toInteger( countList.get( 0 ).get( "product_num" ) ) + product_num;
		    //数量+1
		    MallShopCart cart = new MallShopCart();
		    cart.setId( CommonUtil.toInteger( countList.get( 0 ).get( "id" ) ) );
		    cart.setProductNum( num );
		    mallShopCartDAO.updateByShopCart( cart );

		    //记录id，删除多余记录
		    if ( "".equals( deletes ) ) {
			deletes = "0," + map.get( "id" ).toString();
		    } else {
			deletes += "," + map.get( "id" ).toString();
		    }
		}
		id.append( map.get( "id" ).toString() ).append( "," );
	    }
	    if ( !"".equals( deletes ) ) {
		shoppingdelect( deletes, "", 1 );
	    }
	    //	    if ( id != null && id.length() > 1 ) {
	    //		id = new StringBuilder( id.substring( 0, id.length() - 1 ) );
	    //		CookieUtil.addCookie( response, CookieUtil.SHOP_CART_COOKIE_KEY, id.toString(), Constants.COOKIE_SHOP_CART_TIME );
	    //	    }
	}

    }

    /***
     * 未登陆时，查询cookie商品记录，用于判断新增/修改
     */
    @Override
    public List< Map< String,Object > > selectByShopCart( MallShopCart obj, String ids ) {
	if ( ids == null || "".equals( ids ) ) {
	    return null;
	}
	Wrapper< MallShopCart > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "id,pro_spec_str" );
	wrapper.in( "id", ids.split( "," ) );
	//	String sql = "  SELECT id,pro_spec_str FROM t_mall_shop_cart where id in (" + ids + ")";
	if ( obj.getProductId() != null && !"".equals( obj.getProductId().toString() ) ) {
	    //	    sql += " AND product_id=" + obj.getProductId();
	    wrapper.andNew( "product_id={0}", obj.getProductId() );
	}
	if ( obj.getShopId() != null && !"".equals( obj.getShopId().toString() ) ) {
	    //	    sql += " AND shop_id=" + obj.getShopId();
	    wrapper.andNew( "shop_id={0}", obj.getShopId() );
	}
	if ( obj.getProductSpecificas() != null && !"".equals( obj.getProductSpecificas() ) ) {
	    //	    sql += " AND product_specificas='" + obj.getProductSpecificas() + "'";
	    wrapper.andNew( "product_specificas={0}", obj.getProductSpecificas() );
	}
	if ( obj.getProType() != null && !"".equals( obj.getProType().toString() ) ) {
	    //	    sql += " AND pro_type=" + obj.getProType();
	    wrapper.andNew( "pro_type={0}", obj.getProType() );
	}
	return mallShopCartDAO.selectMaps( wrapper );
    }

    @Override
    public List< Map< String,Object > > getProductListByIds( String ids, Member member, double discount, MallPaySet set, boolean isPifa ) {
	if ( CommonUtil.isEmpty( ids ) ) {
	    return null;
	}
	String[] idStrs = ids.split( "," );
	Wrapper< MallProduct > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "id,is_delete,is_publish,pro_price ,pro_type_id,member_type,pro_name,is_specifica,is_member_discount,pro_label" );
	wrapper.in( "id", idStrs );
	List< Map< String,Object > > productList = mallProductDAO.selectMaps( wrapper );
	/*System.out.println( "productList:" + productList );*/
	if ( productList == null || productList.size() == 0 ) {
	    return null;
	}
	List< Integer > specProIds = new ArrayList<>();
	for ( Map< String,Object > map : productList ) {
	    if ( CommonUtil.toString( map.get( "is_specifica" ) ).equals( "1" ) ) {
		specProIds.add( CommonUtil.toInteger( map.get( "id" ) ) );
	    }
	}

	//查询商品图片
	Map< String,Object > params = new HashMap<>();
	params.put( "assIds", ids.split( "," ) );
	params.put( "isMainImages", 1 );
	params.put( "assType", 1 );
	List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssIds( params );
	/*System.out.println( "imageList:" + imageList );*/
	if ( imageList != null && imageList.size() >= 0 ) {
	    productList = getProductParams( productList, imageList );
	}
	if ( CommonUtil.isNotEmpty( specProIds ) && specProIds.size() > 0 ) {
	    //查询商品库存
	    List< MallProductInventory > invList = mallProductInventoryService.selectByIdListDefault( specProIds );
	    /*System.out.println( "invList:" + invList );*/
	    if ( invList != null && invList.size() > 0 ) {
		List< Map< String,Object > > newProList = new ArrayList<>();

		for ( Map< String,Object > productMap : productList ) {
		    int productId = CommonUtil.toInteger( productMap.get( "id" ) );
		    for ( int i = 0; i < invList.size(); i++ ) {
			MallProductInventory inventory = invList.get( i );
			int product_id = 0;
			if ( CommonUtil.isNotEmpty( inventory.getProductId() ) ) {
			    product_id = CommonUtil.toInteger( inventory.getProductId() );
			}
			if ( product_id == productId ) {
			    productMap.put( "inv_price", inventory.getInvPrice() );
			    productMap.put( "inv_num", inventory.getInvNum() );
			    productMap.put( "inv_sale_num", inventory.getInvSaleNum() );
			    productMap.put( "specifica_img_id", inventory.getSpecificaImgId() );
			    invList.remove( i );
			    break;
			}
		    }
		    newProList.add( productMap );
		}
		if ( newProList != null && newProList.size() > 0 ) {
		    productList = newProList;
		}
	    }
	}
	List< Map< String,Object > > newProList = new ArrayList<>();

	for ( Map< String,Object > map : productList ) {
	    map = getProductHome( map, member, PropertiesUtil.getResourceUrl(), discount, set, isPifa );
	    newProList.add( map );
	}
	return newProList;
    }

    @Override
    public List< Map< String,Object > > selectPageIdByUserId( Integer userId, List< Map< String,Object > > shopList ) {
	return mallPageDAO.selectPageIdByUserId( userId, shopList );
    }

    @Override
    public int getPageIdByShopId( int shopId ) {
	String key = Constants.REDIS_KEY + "shop_page_" + shopId;
	if ( JedisUtil.exists( key ) ) {
	    Object obj = JedisUtil.get( key );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		return CommonUtil.toInteger( obj );
	    }
	}
	MallStore store = mallStoreDAO.selectById( shopId );
	if ( CommonUtil.isEmpty( store ) ) {
	    return 0;
	}
	if ( CommonUtil.isEmpty( store.getWxShopId() ) ) {return 0;}
	WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
	if ( shopInfo == null ) {return 0;}
	if ( shopInfo.getStatus() == -1 ) {return 0;}
	Map< String,Object > params = new HashMap<>();
	params.put( "wxShopId", store.getWxShopId() );
	List< Map< String,Object > > pageList = mallPageDAO.selectPageByWxShopId( params );
	if ( pageList != null && pageList.size() > 0 ) {
	    String pageId = pageList.get( 0 ).get( "id" ).toString();
	    JedisUtil.set( key, pageId, Constants.REDIS_SECONDS );
	    return CommonUtil.toInteger( pageId );
	}
	return 0;
    }

    private List< Map< String,Object > > getProductParams( List< Map< String,Object > > productList, List< Map< String,Object > > imageList ) {
	List< Map< String,Object > > newProList = new ArrayList<>();

	for ( Map< String,Object > productMap : productList ) {
	    int productId = CommonUtil.toInteger( productMap.get( "id" ) );
	    for ( int i = 0; i < imageList.size(); i++ ) {
		Map< String,Object > imageMap = imageList.get( i );
		int product_id = 0;
		if ( CommonUtil.isNotEmpty( imageMap.get( "product_id" ) ) ) {
		    product_id = CommonUtil.toInteger( imageMap.get( "product_id" ) );
		}
		if ( CommonUtil.isNotEmpty( imageMap.get( "ass_id" ) ) ) {
		    product_id = CommonUtil.toInteger( imageMap.get( "ass_id" ) );
		}
		if ( product_id == productId ) {
		    productMap.putAll( imageMap );
		    imageList.remove( i );
		    break;
		}
	    }
	    newProList.add( productMap );
	}
	if ( newProList != null && newProList.size() > 0 ) {
	    productList = newProList;
	}
	return productList;
    }

}
