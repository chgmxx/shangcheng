package com.gt.mall.web.service.page.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCollectDAO;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.page.MallPageDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.presale.MallPresaleDAO;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.dao.presale.MallPresaleMessageRemindDAO;
import com.gt.mall.dao.product.*;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.presale.MallPresaleMessageRemind;
import com.gt.mall.entity.presale.MallPresaleTime;
import com.gt.mall.entity.product.*;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.entity.seckill.MallSeckillPrice;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.*;
import com.gt.mall.web.service.auction.MallAuctionService;
import com.gt.mall.web.service.basic.MallCollectService;
import com.gt.mall.web.service.basic.MallImageAssociativeService;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.groupbuy.MallGroupBuyService;
import com.gt.mall.web.service.groupbuy.MallGroupJoinService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.pifa.MallPifaApplyService;
import com.gt.mall.web.service.pifa.MallPifaService;
import com.gt.mall.web.service.presale.MallPresaleDepositService;
import com.gt.mall.web.service.presale.MallPresaleService;
import com.gt.mall.web.service.presale.MallPresaleTimeService;
import com.gt.mall.web.service.product.*;
import com.gt.mall.web.service.seckill.MallSeckillService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
    private MallPaySetDAO               mallPaySetDAO;
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
    private MallProductInventoryDAO     mallProductInventoryDAO;
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
    //    @Autowired
    //    private WxShopService               wxShopService;//微信门店接口
    @Autowired
    private MallProductGroupDAO         mallProductGroupDAO;//商品分组dao
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private MallProductSpecificaDAO     mallProductSpecificaDAO;
    @Autowired
    private MallIntegralDAO             mallIntegralDAO;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private MallGroupService            mallGroupService;
    @Autowired
    private MallGroupJoinService        mallGroupJoinService;
    @Autowired
    private MallCollectService          mallCollectService;

    /**
     * 分页查询
     */
    @Override
    public PageUtil findByPage( Map< String,Object > params, BusUser user ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	params.put( "pid", user.getPid() );
	params.put( "userid", user.getId() );
	int rowCount = mallPageDAO.count( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallPage/index.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	//todo 调用陈丹的接口 根据商家id查询门店id
	//查询店铺id
	List< Map< String,Object > > list = mallPageDAO.findByPage( params );
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

		mallPageDAO.update( page, pageWrapper );

	    }
	    MallPage page = new MallPage();
	    page.setPagIsMain( 1 );
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
	return mallProductDAO.selectProductMapById( id );
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
    public Map< String,Object > shopmessage( Integer shopid ) {
	/*String sql = "SELECT a.*,"
			+ "IF(s.business_name IS NOT NULL ,s.business_name,a.`sto_name`) AS business_name,s.`status` AS STATUS,"
			+ "(SELECT p.local_address FROM t_wx_shop_photo p WHERE p.shop_id = a.wx_shop_id  ORDER BY p.id LIMIT 1) AS stoPicture,"
			+ "a.sto_head_img  "
			+ "FROM t_mall_store a LEFT JOIN t_wx_shop s ON a.wx_shop_id=s.id WHERE a.id="
			+ shopid;*/
	Map< String,Object > storeMap = mallStoreDAO.selectMapById( shopid );
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    storeMap.put( "business_name", storeMap.get( "sto_name" ) );
	    try {
		//TODO  wxShopService.getShopById()
		/*WsWxShopInfo wxShop = null;//wxShopService.getShopById( CommonUtil.toInteger( storeMap.get( "wx_shop_id" ) ) );
		storeMap.put( "business_name", wxShop.getBusinessName() );
		storeMap.put( "status", wxShop.getStatus() );*/
		//todo 调用小屁孩接口 根据门店id查询门店图片
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    return storeMap;
	}
	return null;
    }

    @Override
    public Map< String,Object > wxpublicid( Integer pagesid ) {
	//	String sql = "SELECT a.id,a.bus_user_id,a.head_img FROM t_wx_public_users a LEFT JOIN t_mall_store b ON a.bus_user_id=b.sto_user_id LEFT JOIN t_mall_page c ON b.id=c.pag_sto_id WHERE c.id=" + pagesid;
	//	return daoUtil.queryForMap( sql );
	Map< String,Object > publicMap = new HashMap<>();
	MallPage page = mallPageDAO.selectById( pagesid );
	if ( CommonUtil.isNotEmpty( page ) ) {
	    // todo 调用小屁孩接口 根据商家id查询公众号信息
	    publicMap.put( "id", "" );
	    publicMap.put( "bus_user_id", page.getPagUserId() );
	    publicMap.put( "head_img", "" );
	}
	return null;
    }

    @Override
    public void isAddShopCart( HttpServletRequest request, Member member ) throws Exception {
	if ( CommonUtil.isNotEmpty( member ) ) {
	    Object obj = request.getSession().getAttribute( "mallShopCart" );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		MallShopCart shopCart = (MallShopCart) JSONObject.toBean( JSONObject.fromObject( obj ), MallShopCart.class );
		int count = addshopping( shopCart, member, request, null );
		if ( count > 0 ) {
		    request.getSession().setAttribute( "mallShopCart", null );
		    request.setAttribute( "isAddShop", 1 );
		}
	    }

	    Map< String,Object > params = new HashMap< String,Object >();
	    //todo 调用小屁孩的接口
	    //查询用户id
	    List< Integer > memberList = new ArrayList<>();//memberPayService.findMemberIds( member.getId() );//查询会员信息
	    params.put( "memberList", memberList );
	    //查询购物车的数量
	    params.put( "type", 0 );
	    params.put( "busUserId", member.getBusid() );
	    int shopCartNum = mallShopCartDAO.selectShopCartNum( params );
	    request.setAttribute( "shopCartNum", shopCartNum );
	} else {
	    Map< String,Object > params = new HashMap< String,Object >();
	    String shopCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	    if ( shopCartIds != null && !"".equals( shopCartIds ) ) {
		params.put( "shopCartIds", shopCartIds.split( "," ) );
		int shopCartNum = mallShopCartDAO.selectShopCartNum( params );
		request.setAttribute( "shopCartNum", shopCartNum );
	    }

	}
    }

    @Override
    public int addshopping( MallShopCart obj, Member member, HttpServletRequest request, HttpServletResponse response ) {
	obj.setCreateTime( DateTimeKit.getNow() );
	int count = 0;
	List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	Cookie cookie = CookieUtil.getCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	if ( CommonUtil.isNotEmpty( member ) ) {
	    obj.setUserId( member.getId() );
	    obj.setBusUserId( member.getBusid() );
	    cartList = mallShopCartDAO.selectByShopCart( obj );
	} else {
	    MallProduct product = mallProductDAO.selectById( obj.getProductId() );
	    if ( product != null ) {
		obj.setBusUserId( product.getUserId() );
	    }
	    if ( cookie != null && cookie.getValue() != null ) {
		cartList = selectByShopCart( obj, cookie.getValue() );
	    }
	}
	if ( cartList != null && cartList.size() > 0 ) {
	    Map< String,Object > map = cartList.get( 0 );

	    if ( CommonUtil.isNotEmpty( obj.getProSpecStr() ) ) {
		obj = getProSpecNum( map, obj );
	    }
	    obj.setId( CommonUtil.toInteger( map.get( "id" ) ) );
	    count = mallShopCartDAO.updateByShopCart( obj );
	} else {
	    count = mallShopCartDAO.insert( obj );
	    if ( CommonUtil.isEmpty( member ) ) {
		String value = obj.getId().toString();
		if ( cookie != null && cookie.getValue() != null ) {
		    value = cookie.getValue() + "," + obj.getId();
		}
		CookieUtil.addCookie( response, CookieUtil.SHOP_CART_COOKIE_KEY, value, 60 * 60 * 24 * 30 );
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
			    Map< String,Object > map = new HashMap< String,Object >();
			    map.put( "pro_spec_str", mallShopCart.getProSpecStr() );
			    cart = getProSpecNum( map, cart );
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
    private MallShopCart getProSpecNum( Map< String,Object > map, MallShopCart cart ) {
	if ( CommonUtil.isNotEmpty( map.get( "pro_spec_str" ) ) ) {
	    JSONObject specIdObj = new JSONObject();

	    JSONObject speObj = JSONObject.fromObject( map.get( "pro_spec_str" ) );//商品规格和数量

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
	}
	return cart;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void shoppingcare( Member member, double discount, int type, HttpServletRequest request, int userid ) {
	int memberId;
	List< Integer > memberList = null;
	Map< String,Object > shopParams = new HashMap<>();
	if ( member != null ) {
	    memberId = CommonUtil.toInteger( member.getId() );
	    //todo 调用彭江丽  根据粉丝id查询会员
	    memberList = new ArrayList<>();//memberPayService.findMemberIds( memberId );//查询会员信息
	}
	if ( memberList != null && memberList.size() > 0 ) {
	    shopParams.put( "memberList", memberList );
	} else {
	    if ( member != null ) {
		shopParams.put( "memberId", member.getId() );
	    } else {
		String shopCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
		shopParams.put( "checkIds", shopCartIds.split( "," ) );
	    }
	}
	if ( userid > 0 ) {
	    shopParams.put( "userId", userid );
	}
	shopParams.put( "type", type );

	//todo 关联小屁孩门店信息
	List< Map< String,Object > > list = mallShopCartDAO.selectShopCartByMemberId( shopParams );
	List< Map< String,Object > > shopList = new ArrayList<>();
	List< Map< String,Object > > shopCartList = new ArrayList<>();

	//保存失效商品
	List< Map< String,Object > > sxCartList = new ArrayList<>();
	List< Map< String,Object > > sxshopCartList = new ArrayList<>();
	int shopId = 0;
	Map< String,Object > shopMap = new HashMap<>();
	Map< String,Object > sxShopMap = new HashMap<>();
	Map< String,Object > productMap = new HashMap<>();
	if ( list != null && list.size() > 0 ) {
	    int j = 0;
	    for ( Map< String,Object > map : list ) {
		String proId = map.get( "product_id" ).toString();
		int productNum = CommonUtil.toInteger( map.get( "product_num" ) );
		int stockNum = CommonUtil.toInteger( map.get( "pro_stock_num" ) );
		double price = CommonUtil.toDouble( map.get( "price" ) );
		int pro_type = 0;
		if ( CommonUtil.isNotEmpty( map.get( "pro_type" ) ) ) {
		    pro_type = CommonUtil.toInteger( map.get( "pro_type" ) );
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		//				double primaryprice = CommonUtil.toDouble(map.get("primary_price"));//折前价格
		MallShopCart cart = new MallShopCart();
		cart.setId( CommonUtil.toInteger( map.get( "id" ) ) );
		String msg = "";
		int code = 1;
		//判断限购和商品是否正在售卖
		Map< String,Object > xgMap = mallProductService.isshoppingCart( map, productNum );
		if ( xgMap.get( "code" ).toString().equals( "1" ) ) {
		    if ( xgMap.containsKey( "product_num" ) ) {
			map.put( "product_num", xgMap.get( "product_num" ) );
		    }
		    if ( xgMap.containsKey( "maxBuy" ) ) {
			map.put( "maxBuy", xgMap.get( "maxBuy" ) );
		    }
		    if ( xgMap.containsKey( "productMap" ) ) {
			productMap.putAll( JSONObject.fromObject( xgMap.get( "productMap" ) ) );
		    }
		} else {
		    code = CommonUtil.toInteger( xgMap.get( "code" ) );
		    msg = CommonUtil.toString( xgMap.get( "msg" ) );
		}
		String proSpec = map.get( "product_specificas" ).toString();
		if ( map.get( "isSpec" ).toString().equals( "1" ) && code == 1 && pro_type == 0 ) {//商品存在规格
		    if ( proSpec.equals( "" ) ) {
			code = 0;
			msg = "商品存在规格";
		    } else {
			//判断商品规格是否异常
			Map< String,Object > invPriceMap = mallProductService.getProInvIdBySpecId( proSpec, CommonUtil.toInteger( proId ) );
			boolean flag = false;

			if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
			    map.put( "inv_num", invPriceMap.get( "inv_num" ) );
			    if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_img_url" ) ) ) {
				map.put( "image_url", invPriceMap.get( "specifica_img_url" ) );
			    }
			    double invPrice = CommonUtil.toDouble( invPriceMap.get( "inv_price" ) );
			    double yhPrice = invPrice;
			    if ( CommonUtil.isNotEmpty( map.get( "is_member_discount" ) ) ) {
				if ( map.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
				    yhPrice = invPrice * discount;
				}
			    }
			    if ( price != invPrice || yhPrice != price ) {//同步价格
				yhPrice = CommonUtil.toDouble( df.format( yhPrice ) );
				if ( yhPrice <= 0 ) {
				    yhPrice = 0.01;
				}
				cart.setPrice( BigDecimal.valueOf( yhPrice ) );
				cart.setPrimaryPrice( BigDecimal.valueOf( invPrice ) );
				mallShopCartDAO.updateById( cart );
				map.put( "hyPrice", df.format( yhPrice ) );
				map.put( "price", df.format( yhPrice ) );
				map.put( "primary_price", df.format( invPrice ) );
			    }
			    if ( CommonUtil.isEmpty( invPriceMap.get( "inv_num" ) ) ) {
				flag = true;
			    } else {
				int invNum = CommonUtil.toInteger( invPriceMap.get( "inv_num" ) );
				if ( invNum <= 0 ) {
				    flag = true;
				} else if ( productNum > invNum ) {
				    flag = true;
				}
			    }
			} else {
			    code = 0;
			    msg = "商品规格不存在";
			}
			if ( flag ) {
			    code = 0;
			    msg = "商品已售罄";
			}
		    }
		} else if ( map.get( "isSpec" ).toString().equals( "0" ) && code == 1 && pro_type == 0 ) {//商品部存在规格
		    if ( !proSpec.equals( "" ) ) {
			code = 0;
			msg = "商品不存在规格";
		    }
		    double proPrice = CommonUtil.toDouble( map.get( "pro_price" ) );

		    double yhPrice = proPrice;
		    if ( CommonUtil.isNotEmpty( map.get( "is_member_discount" ) ) ) {
			if ( map.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
			    yhPrice = proPrice * discount;
			}
		    }

		    if ( price != proPrice || yhPrice != price ) {//同步价格
			yhPrice = CommonUtil.toDouble( df.format( yhPrice ) );
			if ( yhPrice <= 0 ) {
			    yhPrice = 0.01;
			}
			cart.setPrice( BigDecimal.valueOf( yhPrice ) );
			cart.setPrimaryPrice( BigDecimal.valueOf( proPrice ) );
			mallShopCartDAO.updateById( cart );
			map.put( "hyPrice", df.format( yhPrice ) );
			map.put( "price", df.format( yhPrice ) );
			map.put( "primary_price", df.format( proPrice ) );
		    }
		    if ( productNum > stockNum ) {
			code = 0;
			msg = "商品已售罄";
		    }
		}
		if ( pro_type == 1 || pro_type == 2 ) {//批发商品
		    map.put( "pfType", 1 );
		    List< MallPifa > pfList = mallPifaDAO.selectStartPiFaByProductId( map );
		    if ( pfList == null || pfList.size() == 0 ) {
			code = 0;
			msg = "批发商品已结束或已删除";
		    } else {
			MallPifa pifa = pfList.get( 0 );
			if ( !pifa.getPfType().toString().equals( map.get( "pro_type" ).toString() ) ) {
			    code = 0;
			    msg = "商家已更改批发类型";
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( map.get( "pro_spec_str" ) ) ) {
		    Map< String,Object > specStr = JSONObject.fromObject( map.get( "pro_spec_str" ) );
		    Set< String > set = specStr.keySet();
		    if ( set != null && set.size() > 0 ) {
			for ( String str : set ) {
			    if ( CommonUtil.isNotEmpty( specStr.get( str ) ) ) {
				Map< String,Object > valMap = JSONObject.fromObject( specStr.get( str ) );
				Map< String,Object > invPrice = mallProductService.getProInvIdBySpecId( str, CommonUtil.toInteger( proId ) );
				if ( CommonUtil.isNotEmpty( invPrice ) ) {
				    valMap.put( "invNum", invPrice.get( "inv_num" ) );
				    specStr.put( str, valMap );
				}
			    }
			}
		    }
		    map.put( "proSpecStr", specStr );
		}
		map.put( "msg", msg );
		map.put( "code", code );

		if ( code == 1 ) {
		    shopList.add( map );
		} else {
		    sxCartList.add( map );
		}
		if ( !map.get( "shop_id" ).toString().equals( CommonUtil.toString( shopId ) ) || j == 0 ) {
		    shopId = CommonUtil.toInteger( map.get( "shop_id" ) );
		}
		String next_shop_id = "";
		if ( j + 1 < list.size() ) {
		    Map< String,Object > nextMap = list.get( j + 1 );
		    next_shop_id = nextMap.get( "shop_id" ).toString();
		}
		if ( ( !next_shop_id.equals( CommonUtil.toString( shopId ) ) && CommonUtil.isNotEmpty( next_shop_id ) ) || j + 1 == list.size() ) {
		    if ( null != shopList && shopList.size() > 0 ) {
			shopMap = getShopMaps( shopMap, map, pro_type );
			shopMap.put( "proList", shopList );
			shopCartList.add( shopMap );

			shopList = new ArrayList<>();
			shopMap = new HashMap<>();
		    }
		    if ( null != sxCartList && sxCartList.size() > 0 ) {
			sxShopMap = getShopMaps( sxShopMap, map, pro_type );
			sxShopMap.put( "proList", sxCartList );
			sxshopCartList.add( sxShopMap );

			sxCartList = new ArrayList<>();
			sxShopMap = new HashMap<>();
		    }
		}
		j++;
	    }
	}
	if ( shopCartList != null && shopCartList.size() > 0 ) {
	    request.setAttribute( "list", shopCartList );
	}
	if ( sxshopCartList != null && sxshopCartList.size() > 0 ) {
	    request.setAttribute( "sxList", sxshopCartList );
	}

	if ( type == 1 ) {//批发购物车
	    //通过商品id查询预售信息
	    MallPaySet set = mallPaySetService.selectByMember( member );
	    double hpMoney = 0;
	    int hpNum = 1;//混批件数
	    int spHand = 1;
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsPf() ) ) {//是否开启批发
		    if ( CommonUtil.isNotEmpty( set.getPfSet() ) ) {
			JSONObject obj = JSONObject.fromObject( set.getPfSet() );
			if ( CommonUtil.isNotEmpty( obj.get( "isHpMoney" ) ) ) {
			    if ( obj.get( "isHpMoney" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "hpMoney" ) ) ) {
				hpMoney = CommonUtil.toDouble( obj.get( "hpMoney" ) );
			    }
			}
			if ( CommonUtil.isNotEmpty( obj.get( "isHpNum" ) ) ) {
			    if ( obj.get( "isHpNum" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "hpNum" ) ) ) {
				hpNum = CommonUtil.toInteger( obj.get( "hpNum" ) );
			    }
			}
			if ( CommonUtil.isNotEmpty( obj.get( "isSpHand" ) ) ) {
			    if ( obj.get( "isSpHand" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( obj.get( "spHand" ) ) ) {
				spHand = CommonUtil.toInteger( obj.get( "spHand" ) );
			    }
			}
		    }
		}
	    }
	    request.setAttribute( "hpMoney", hpMoney );
	    request.setAttribute( "hpNum", hpNum );
	    request.setAttribute( "spHand", spHand );
	}

    }

    public Map< String,Object > getShopMaps( Map< String,Object > shopMap, Map< String,Object > map, int pro_type ) {
	shopMap.put( "shop_id", map.get( "shop_id" ) );
	shopMap.put( "sto_name", map.get( "sto_name" ) );
	shopMap.put( "pageid", map.get( "pageid" ) );
	shopMap.put( "pro_type", pro_type );
	return shopMap;
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
    public Map< String,Object > shoporder( String json, String totalnum, String totalprice,
		    String memberId ) {
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
	map.put( "shop_ids", shop_ids.substring( 0, shop_ids.length() - 1 ) );
	return map;
    }

    @Override
    public List< Map< String,Object > > storeList( Integer stoId, int type, int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	if ( CommonUtil.isNotEmpty( stoId ) && stoId > 0 ) {
	    params.put( "shopId", stoId );
	} else {
	    //todo 调用陈丹的接口，根据商家id查询门店信息
	    params.put( "wxShopList", null );
	}
	/*String sql = "SELECT distinct(a.group_id) as group_id,b.group_name,b.is_child FROM t_mall_product_group a "
			+ " LEFT JOIN t_mall_group b ON a.group_id=b.id ";
	if ( CommonUtil.isNotEmpty( stoId ) && stoId > 0 ) {
	    sql += " WHERE a.is_delete=0 AND b.is_delete=0 AND a.shop_id=" + stoId;
	} else {

	    sql += " LEFT JOIN t_mall_store s ON s.id=a.`shop_id` "
		+ " LEFT JOIN t_wx_shop ws ON ws.id=s.`wx_shop_id`"
		+ " WHERE  a.is_delete=0 AND b.is_delete=0 AND s.`is_delete`=0  AND ws.`status` != -1";
	    if ( busUserId > 0 ) {
		BusUser user = new BusUser();//busUserMapper.selectByPrimaryKey( busUserId );
		List< Map< String,Object > > shoplist = new ArrayList<>(  );//mallStoreService.findAllStoByUser( user );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    sql += " and (";
		    for ( int i = 0; i < shoplist.size(); i++ ) {
			Map< String,Object > map = shoplist.get( i );

			if ( i > 0 ) {
			    sql += " or ";
			}
			sql += " a.shop_id=" + map.get( "id" );
		    }
		    sql += " )";
		}
	    }
	}*/

	//	sql += " and b.is_first_parents=1 order by b.sort,b.id desc";
	List< Map< String,Object > > list = mallProductGroupDAO.selectProductGroupByShopId( params );
	if ( list != null && list.size() > 0 && type == 1 ) {
	    for ( Map< String,Object > map : list ) {
		String groupId = map.get( "group_id" ).toString();
		if ( CommonUtil.isNotEmpty( map.get( "is_child" ) ) ) {
		    if ( map.get( "is_child" ).toString().equals( "1" ) ) {//存在子类的分类
			/*sql = "SELECT distinct(a.group_id) as group_id,b.group_name,b.is_child,i.image_url FROM t_mall_product_group a "
					+ "LEFT JOIN t_mall_group b ON a.group_id=b.id "
					+ "left join t_mall_image_associative i on i.ass_type=2 and i.ass_id=b.id "
					+ "WHERE  a.is_delete=0 AND b.is_delete=0 AND a.shop_id="
					+ stoId + " and b.group_p_id=" + groupId + " order by b.sort,b.id desc";*/
			Map< String,Object > childParams = new HashMap<>();
			childParams.put( "shopId", stoId );
			childParams.put( "groupParentId", groupId );
			List< Map< String,Object > > childList = mallProductGroupDAO.selectGroupByParentId( childParams );
			if ( childList != null && childList.size() > 0 ) {
			    map.put( "childList", childList );
			}
		    }
		}
	    }
	}
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
    public PageUtil productAllList( Integer shopid,
		    String type, int rType, Member member, double discount, Map< String,Object > params, boolean isPifa ) {
	if ( CommonUtil.isNotEmpty( params ) ) {
	    int memberId = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		memberId = member.getId();
	    }
	    if ( CommonUtil.isNotEmpty( shopid ) ) {
		if ( shopid > 0 && memberId > 0 ) {
		    saveOrUpdateKeyWord( params, shopid, memberId );
		}
	    }
	    params.put( "rType", rType );
	    params.put( "shopId", shopid );
	    params.put( "type", type );
	}
	//params.put("curPage", CommonUtil.isEmpty(params.get("curPage"))?1:CommonUtil.toInteger(params.get("curPage")));
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	int rowCount = mallProductDAO.selectCountAllByShopids( params );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	params.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxNum", pageSize );

	List< Map< String,Object > > xlist = new ArrayList<>();
	List< Map< String,Object > > list = mallProductDAO.selectProductAllByShopids( params );

	for ( Map< String,Object > map1 : list ) {
	    map1.put( "rType", rType );
	    map1 = productGetPrice( map1, discount, isPifa );

	    xlist.add( map1 );
	}

	page.setSubList( xlist );
	return page;
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
	if ( CommonUtil.isEmpty( image_url ) ) {
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
	/*String sql = "SELECT p.id FROM t_mall_page p "
			+ " LEFT JOIN t_mall_store s ON s.id=p.`pag_sto_id` "
			+ " LEFT JOIN t_wx_shop ws ON ws.id=s.`wx_shop_id` "
			+ " WHERE  p.pag_is_main=1 AND s.`is_delete`=0 AND ws.`status` != -1";
	if ( CommonUtil.isNotEmpty( shopid ) ) {
	    if ( shopid > 0 ) {
		sql += " and p.pag_sto_id=" + shopid;
	    }
	}*/
	//todo 调用小屁孩接口，判断门店id是否已删除
	Map< String,Object > params = new HashMap<>();
	params.put( "wxShopId", 0 );
	return mallPageDAO.selectPageByWxShopId( params );
    }

    @Override
    public Map< String,Object > selUser( Integer shopid ) {

	//	String sql = "SELECT b.id,b.pid,b.advert,a.sto_user_id as bus_user_id,a.wx_shop_id FROM t_mall_store a LEFT JOIN bus_user b ON a.sto_user_id=b.id WHERE a.id=" + shopid;
	//	return daoUtil.queryForMap( sql );
	MallStore store = mallStoreService.selectById( shopid );
	if ( CommonUtil.isNotEmpty( store ) ) {
	    if ( store.getIsDelete() == 0 ) {
		//  todo 调用陈丹接口  ， 通过商家id查询商家信息
		Map< String,Object > userMap = new HashMap<>();
		userMap.put( "id", store.getStoUserId() );//商家id
		userMap.put( "pid", "" );//总账号id
		userMap.put( "advert", "" );//是否显示技术支持
		userMap.put( "sto_user_id", store.getStoUserId() );//商家id
		userMap.put( "wx_shop_id", store.getWxShopId() );//微信门店id
		return userMap;
	    }
	}
	return null;
    }

    @Override
    public Map< String,Object > publicMapByUserId( Integer userId ) {
	//	String sql = "SELECT p.id AS public_id,p.bus_user_id FROM  t_wx_public_users p  WHERE p.`bus_user_id`=" + userId;
	Map< String,Object > publicMap = new HashMap<>();
	// todo 调用小屁孩的接口，通过商家id查询公众号信息
	publicMap.put( "public_id", "" );//公众号id
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
    public List< Map< String,Object > > pagecountid( Integer shopid ) {
	//	String sql = "SELECT id FROM t_mall_page WHERE pag_sto_id=" + shopid + " AND pag_is_main=1";
	Wrapper< MallPage > wrapper = new EntityWrapper<>();
	wrapper.where( "pag_sto_id={0} AND pag_is_main=1", shopid );
	return mallPageDAO.selectMaps( wrapper );
    }

    @Override
    public List< Map< String,Object > > productShopList( Integer userid ) {
	return mallStoreService.selectStoreByUserId( userid );
    }

    @Override
    public Map< String,Object > getPage( Integer userid, int wxShopId ) {
	Map< String,Object > params = new HashMap<>();
	if ( wxShopId == 0 ) {
	    params.put( "userId", userid );
	    //	    sql = "SELECT p.id FROM t_mall_page p LEFT JOIN t_mall_store s ON p.pag_sto_id = s.id LEFT JOIN t_wx_shop ws ON ws.id = s.`wx_shop_id` WHERE pag_is_main = 1 AND s.`is_delete` = 0 AND ws.`status` != -1  and pag_user_id="
	    //			    + userid + " ";
	} else {
	    //sql = "SELECT p.id FROM t_mall_page p LEFT JOIN t_mall_store s ON p.pag_sto_id = s.id  WHERE s.`is_delete` = 0 and s.wx_shop_id=" + wxShopId;

	    params.put( "wxShopId", wxShopId );

	}

	List< Map< String,Object > > pageList = mallPageDAO.selectPageByWxShopId( params );
	if ( pageList != null && pageList.size() > 0 ) {
	    return pageList.get( 0 );
	}
	return null;
    }

    @Override
    public String getProvince( String ip ) {
	String logCity;
	try {
	    String address = AddressUtil.provinceadd( ip );//获取注册地址
	    if ( address == null || address.equals( "" ) ) {
		address = "广东省";
	    }
	    //todo 通过地址获取地址id
	    List list = new ArrayList();//busUserMapper.cityid( address );
	    if ( list != null ) {
		if ( list.size() == 1 ) {
		    Map map = (Map) list.get( 0 );
		    logCity = map.get( "id" ).toString();
		} else {
		    logCity = "2136";
		}
	    } else {
		logCity = "2136";
	    }
	} catch ( Exception e ) {
	    logCity = "2136";
	    logger.error( "获取地址异常：" + e.getMessage() );
	}
	return logCity;
    }

    @Override
    public int updateProViewPage( String proId, Map proMap ) {
	String key = "proViewNum";
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
    public BusUser selUserByMember( Member member ) {
	if ( CommonUtil.isNotEmpty( member ) ) {
	    //todo 根据商家id查询商家信息
	    BusUser user = new BusUser();// busUserMapper.selectByPrimaryKey( member.getBusid() );
	    if ( CommonUtil.isNotEmpty( user ) ) {
		return user;
	    }
	}
	return null;
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
    public JSONObject getProduct( JSONObject obj ) throws Exception {
	if ( CommonUtil.isNotEmpty( obj ) ) {
	    if ( CommonUtil.isNotEmpty( obj.get( "product_id" ) ) ) {
		/*String sql = "select pro_name AS product_name,pro_weight,card_type as cardReceiveId,is_coupons as isCoupons,pro_type_id,member_type,is_integral_deduction,is_fenbi_deduction,is_member_discount,pro_type_id,shop_id"
				+ ",return_day,pro_code,shop_id ,flow_id as flowId"
				+ " from t_mall_product where id=" + obj.get( "product_id" );*/
		Wrapper< MallProduct > wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect(
				"pro_name AS product_name,pro_weight,card_type as cardReceiveId,is_coupons as isCoupons,pro_type_id,member_type,is_integral_deduction,is_fenbi_deduction,is_member_discount,pro_type_id,shop_id,return_day,pro_code,shop_id ,flow_id as flowId" );
		wrapper.where( "id={0}", obj.get( "product_id" ) );

		List< Map< String,Object > > proList = mallProductDAO.selectMaps( wrapper );
		Map< String,Object > map = proList.get( 0 );
		if ( CommonUtil.isNotEmpty( map ) ) {
		    String imageUrl = "";
		    int proId = obj.getInt( "product_id" );
		    //查询店铺信息
		    if ( CommonUtil.isNotEmpty( map.get( "shopId" ) ) ) {
			//通过店铺id查询店铺信息
			MallStore store = mallStoreService.selectById( CommonUtil.toInteger( map.get( "shopId" ) ) );
			map.put( "shop_name", store.getStoName() );
			//通过门店id查询门店信息
			if ( CommonUtil.isNotEmpty( store.getWxShopId() ) && store.getWxShopId() > 0 ) {
			    //TODO  根据店铺id查询门店信息 wxShopService.getShopById()
			   /* WsWxShopInfo shop = null;//wxShopService.getShopById( store.getWxShopId() );
			    map.put( "shop_name", shop.getBusinessName() );*/
			}
		    }
		    //查询商品图片
		    Map< String,Object > params = new HashMap<>();
		    params.put( "assType", 1 );
		    params.put( "isMainImages", 1 );
		    params.put( "assId", proId );
		    List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( params );
		    if ( imageList != null && imageList.size() > 0 ) {
			imageUrl = imageList.get( 0 ).getImageUrl();
		    }

		    //查询规格信息
		    if ( CommonUtil.isNotEmpty( obj.get( "product_specificas" ) ) ) {
			/*String specSql = "SELECT id,specifica_value,specifica_img_url FROM t_mall_product_specifica WHERE is_delete=0 AND specifica_value_id IN(" + obj
					.get( "product_specificas" ) + ")  AND product_id=" + proId + " ORDER BY sort";*/
			String[] specificaIds = obj.get( "product_specificas" ).toString().split( "," );
			List< MallProductSpecifica > specMapList = mallProductSpecificaService.selectByValueIds( proId, specificaIds );
			String specificaValue = "";
			if ( specMapList != null && specMapList.size() > 0 ) {
			    for ( MallProductSpecifica specifica : specMapList ) {
				if ( CommonUtil.isNotEmpty( specificaValue ) ) {
				    specificaValue += " ";
				}
				specificaValue += specifica.getSpecificaValue();
				if ( CommonUtil.isNotEmpty( specifica.getSpecificaImgUrl() ) ) {
				    imageUrl = specifica.getSpecificaImgUrl();
				}
			    }
			    map.put( "product_speciname", specificaValue );
			}
		    }
		    if ( CommonUtil.isNotEmpty( imageUrl ) ) map.put( "image_url", imageUrl );
		    double price = obj.getDouble( "price" );
		    int num = obj.getInt( "totalnum" );
		    DecimalFormat df = new DecimalFormat( "######0.00" );
		    map.put( "totalprice", df.format( price * num ) );
		    obj.putAll( map );
		}
	    }
	}
	return obj;
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
    public Map< String,Object > getProductComment( Map< String,Object > params, Member member ) {
	int proId = CommonUtil.toInteger( params.get( "proId" ) );
	Map< String,Object > maps = new HashMap<>();
	//查询是否开启评论审核
	MallPaySet set = mallPaySetService.selectByMember( member );

	Map< String,Object > commentMap = new HashMap<>();
	commentMap.put( "productId", proId );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsCommentCheck() ) ) {
		if ( set.getIsCommentCheck().toString().equals( "1" ) ) {
		    commentMap.put( "checkStatus", 1 );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "feel" ) ) ) {
	    commentMap.put( "feel", params.get( "feel" ) );
	}

	List< Map< String,Object > > countList = mallCommentDAO.selectCountFeel( commentMap );
	if ( countList != null && countList.size() > 0 ) {
	    Map< String,Object > countMap = new HashMap<>();
	    for ( Map< String,Object > map : countList ) {
		String key = "hao";
		if ( map.get( "feel" ).toString().equals( "0" ) ) {
		    key = "zhong";
		} else if ( map.get( "feel" ).toString().equals( "-1" ) ) {
		    key = "cha";
		}
		countMap.put( key, map.get( "count" ) );
	    }
	    maps.put( "countMap", countMap );
	}
	List< Map< String,Object > > productCommentList = new ArrayList<>();
	List< Map< String,Object > > commentList = mallCommentDAO.selectCommentByProId( commentMap );
	if ( commentList != null && commentList.size() > 0 ) {
	    for ( Map< String,Object > map : commentList ) {
		String id = map.get( "id" ).toString();
		if ( CommonUtil.isNotEmpty( map.get( "is_upload_image" ) ) ) {
		    if ( map.get( "is_upload_image" ).toString().equals( "1" ) ) {//评论人已经上传图片
			Map< String,Object > params2 = new HashMap<>();
			params2.put( "assType", 4 );
			params2.put( "assId", id );
			//查询评论图片
			List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( params2 );
			if ( imageList != null && imageList.size() > 0 ) {
			    map.put( "imageList", imageList );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( map.get( "product_specificas" ) ) ) {
		    StringBuilder spec = new StringBuilder();
		    String specificas = map.get( "product_specificas" ).toString();
		    //		    String sSql = "select specifica_name as s_name,specifica_value as s_value "
		    //				    + "from t_mall_product_specifica where product_id = " + proId + " "
		    //				    + "and specifica_value_id in (" + specificas + ") order by sort";
		    List< MallProductSpecifica > specList = mallProductSpecificaService.selectByValueIds( proId, specificas.split( "," ) );
		    if ( specList != null && specList.size() > 0 ) {
			for ( MallProductSpecifica specifica : specList ) {
			    if ( CommonUtil.isNotEmpty( specifica ) ) {
				//spec += " "+specMap.get("s_name")+"："+specMap.get("s_value");
				spec.append( " " ).append( specifica.getSpecificaValue() );
			    }
			}
		    }
		    if ( CommonUtil.isNotEmpty( spec.toString() ) ) {
			map.put( "spec", spec.toString() );
		    }
		}
		/*if ( CommonUtil.isNotEmpty( params.get( "isMemberType" ) ) ) {
		    //查询用户类型
		    //todo 调用彭江丽的接口 根据用户id查询用户的会员卡名称
		    GradeType gradeType = memberPayService.findGradeType( CommonUtil.toInteger( map.get( "user_id" ) ) );//会员卡名称
		    if ( CommonUtil.isNotEmpty( gradeType ) ) {
			map.put( "gradeTypeName", gradeType.getGtName() );
		    }
		}*/
		if ( CommonUtil.isNotEmpty( params.get( "isReply" ) ) && map.get( "is_rep" ).toString().equals( "1" ) ) {
		    //查询回复内容
		    Map< String,Object > replyMap = new HashMap<>();
		    replyMap.put( "appraise", id );
		    List< Map< String,Object > > replayList = mallCommentDAO.ownerResponseList( replyMap );
		    if ( replayList != null && replayList.size() > 0 ) {
			map.put( "replyContent", replayList.get( 0 ).get( "content" ) );
		    }
		}
		productCommentList.add( map );
	    }
	    maps.put( "commentList", productCommentList );
	}
	return maps;
    }

    @Override
    public List< Map< String,Object > > getProductCollectByMemberId( Member member, double discount ) {
	/*String sql = "SELECT c.id as cId,a.id,a.shop_id,a.is_member_discount,a.pro_price,a.pro_name,a.is_specifica,a.is_specifica,c.image_url,d.specifica_img_id,e.specifica_img_url,d.inv_num,d.inv_price,a.change_integral,a.pro_cost_price,a.change_fenbi,a.pro_label "
			+ " from t_mall_collect c  "
			+ " left join t_mall_product a on a.id=c.product_id  "
			+ "	LEFT JOIN (SELECT ass_id,image_url from t_mall_image_associative WHERE ass_type=1 AND is_delete=0 AND is_main_images=1) c ON a.id=c.ass_id "
			+ " LEFT JOIN (SELECT product_id,specifica_img_id,inv_num,inv_price FROM t_mall_product_inventory where is_default=1 AND is_delete=0)d ON a.id=d.product_id "
			+ " LEFT JOIN t_mall_product_specifica e ON d.specifica_img_id=e.id "
			+ " WHERE a.is_publish=1 AND a.check_status=1 AND a.is_delete=0 and c.is_delete=0 and c.is_collect=1 ";
	if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
	    if ( !member.getOldid().equals( "0" ) ) {
		sql += " and ( ";
		int i = 0;
		for ( String oldMemberId : member.getOldid().split( "," ) ) {
		    if ( CommonUtil.isNotEmpty( oldMemberId ) ) {
			if ( i > 0 ) {
			    sql += " or ";
			}
			sql += " c.user_id=" + oldMemberId + " ";
			i++;
		    }

		}
		sql += " ) ";
	    } else {
		sql += " and c.user_id=" + member.getId();
	    }
	} else {
	    sql += " and c.user_id=" + member.getId();
	}
	sql += " order by c.create_time desc ";*/

	List< Map< String,Object > > xlist = new ArrayList<>();
	Map< String,Object > params = new HashMap<>();
	//todo 调用彭江丽  根据会员id查询会员集合
	params.put( "memberId", member.getId() );
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
    public boolean wxShopIsDelete( int shopId ) throws Exception {
	//	String sql = "SELECT s.id FROM t_mall_store s LEFT JOIN t_wx_shop ws ON ws.id=s.wx_shop_id WHERE s.id=" + shopId + " AND s.`is_delete` = 0 AND ws.`status` != -1 LIMIT 1";
	MallStore store = mallStoreService.selectById( shopId );
	if ( store.getIsDelete() == 0 ) {
	    if ( CommonUtil.isNotEmpty( store.getWxShopId() ) && store.getWxShopId() > 0 ) {
		//TODO  根据门店id查询门店信息  wxShopService.getShopById()
		/*WsWxShopInfo shop = null;//wxShopService.getShopById( store.getWxShopId() );
		if ( CommonUtil.isNotEmpty( shop ) ) {
		    if ( !CommonUtil.toString( shop.getStatus() ).equals( "-1" ) ) {
			return true;
		    }
		}*/
	    }
	}
	return false;
    }

    @Override
    public String productActivity( HttpServletRequest request, Member member, int id, int shopid, int userid ) {
	String inv_id = "";
	Map< String,Object > groupMap = new HashMap<>();
	if ( CommonUtil.isNotEmpty( member ) ) {
	    groupMap.put( "joinUserId", member.getId() );
	}

	MallGroupBuy groupBuy = mallGroupBuyService.getGroupBuyByProId( id, shopid );//通过商品id查询团购信息
	request.setAttribute( "groupBuy", groupBuy );
	if ( groupBuy != null ) {
	    groupMap.put( "groupBuyId", groupBuy.getId() );
	    //查询参团信息
	    List< Map< String,Object > > list = mallGroupJoinService.getJoinGroup( groupMap, member );
	    request.setAttribute( "joinList", list );
	    //查询用户参加团购的数量
	    int groupBuyCount = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		groupBuyCount = mallGroupJoinService.selectCountByBuyId( groupMap );
	    }
	    request.setAttribute( "groupBuyCount", groupBuyCount );
	    if ( CommonUtil.isNotEmpty( groupBuy.getPriceList() ) ) {
		for ( MallGroupBuyPrice price : groupBuy.getPriceList() ) {
		    if ( price.getIsJoinGroup() == 1 ) {
			inv_id = price.getInvenId().toString();
			break;
		    }
		}
	    }
	}

	//通过商品id查询秒杀信息
	MallSeckill seckill = mallSeckillService.getSeckillByProId( id, shopid );
	request.setAttribute( "seckill", seckill );
	if ( seckill != null ) {
	    //			seckillId = seckill.getId();
	    groupMap.put( "seckillId", seckill.getId() );
	    int seckillCount = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		seckillCount = mallSeckillService.selectCountByBuyId( groupMap );
	    }
	    request.setAttribute( "seckillCount", seckillCount );
	    if ( CommonUtil.isNotEmpty( seckill.getPriceList() ) ) {
		for ( MallSeckillPrice price : seckill.getPriceList() ) {
		    if ( price.getIsJoinGroup() == 1 ) {
			inv_id = price.getInvenId().toString();
			break;
		    }
		}
	    }
	}
	MallPaySet set = new MallPaySet();
	set.setUserId( userid );
	//通过商品id查询预售信息
	set = mallPaySetService.selectByUserId( set );
	boolean isOpenPresale = false;
	boolean isOpenPifa = false;
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsPresale() ) ) {//是否开启预售
		if ( set.getIsPresale().toString().equals( "1" ) ) {
		    isOpenPresale = true;
		}
	    }
	    if ( CommonUtil.isNotEmpty( set.getIsPf() ) ) {//是否开启批发
		if ( set.getIsPf().toString().equals( "1" ) ) {
		    isOpenPifa = true;
		    if ( CommonUtil.isNotEmpty( set.getPfSet() ) ) {
			JSONObject obj = JSONObject.fromObject( set.getPfSet() );
			request.setAttribute( "pfSet", obj );
		    }
		}
	    }
	}
	if ( isOpenPresale ) {
	    MallPresale presale = mallPresaleService.getPresaleByProId( id, shopid, null );
	    request.setAttribute( "presale", presale );
	    boolean isBuyFlag = false;
	    if ( presale != null ) {
		groupMap.put( "presaleId", presale.getId() );
		int presaleCount = 0;//用户购买预售商品的数量
		if ( CommonUtil.isNotEmpty( member ) ) {
		    presaleCount = mallPresaleService.selectCountByBuyId( groupMap );
		}
		request.setAttribute( "presaleCount", presaleCount );
		if ( CommonUtil.isNotEmpty( member ) ) {
		    MallPresaleDeposit deposit = mallPresaleDepositService.selectCountByPresaleId( groupMap );//用户是否已经交纳定金  》0   已经交纳了定金
		    if ( deposit != null && presaleCount == 0 ) {
			if ( deposit.getDepositStatus().toString().equals( "1" ) ) {
			    isBuyFlag = true;
			}
		    }
		    if ( CommonUtil.isNotEmpty( deposit ) ) {
			Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( deposit.getProSpecificaIds(), deposit.getProductId() );
			if ( CommonUtil.isNotEmpty( invMap ) ) {
			    if ( CommonUtil.isNotEmpty( invMap.get( "id" ) ) ) {
				inv_id = invMap.get( "id" ).toString();
			    }
			}
		    }
		    request.setAttribute( "deposit", deposit );
		}

		//查询预售商品订购的数量
		int buyCout = mallPresaleDepositDAO.selectBuyCountByPreId( groupMap );
		if ( CommonUtil.isNotEmpty( presale.getOrderNum() ) ) {
		    buyCout = buyCout + presale.getOrderNum();
		}
		request.setAttribute( "buyCout", buyCout );

		double presaleDiscount = 100;

		List< MallPresaleTime > timeList = mallPresaleTimeService.getPresaleTimeByPreId( presale.getId() );
		request.setAttribute( "presaleTimeList", timeList );
		if ( timeList != null && timeList.size() > 0 ) {
		    for ( MallPresaleTime mallPresaleTime : timeList ) {
			Date endTime = DateTimeKit.parse( mallPresaleTime.getEndTime(), "yyyy-MM-dd HH:mm:ss" );
			Date startTime = DateTimeKit.parse( mallPresaleTime.getStartTime(), "yyyy-MM-dd HH:mm:ss" );
			Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
			if ( startTime.getTime() <= nowTime.getTime() && nowTime.getTime() < endTime.getTime() ) {
			    if ( mallPresaleTime.getPriceType() == 2 ) {
				presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() );
			    } else {
				presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() ) / 100;
			    }
			    request.setAttribute( "presaleTime", mallPresaleTime );
			    break;
			}

		    }
		}
		/*DecimalFormat df = new DecimalFormat("######0.00");
		presaleDiscount = CommonUtil.toDouble(df.format(presaleDiscount));*/
		request.setAttribute( "presaleDiscount", presaleDiscount );

	    }
	    request.setAttribute( "isBuyFlag", isBuyFlag );

	}
	if ( isOpenPifa ) {
	    int status = mallPifaApplyService.getPifaApplay( member, set );
	    request.setAttribute( "status", status );

	    //通过商品id查询批发信息
	    MallPifa pifa = mallPifaService.getPifaByProId( id, shopid );
	    request.setAttribute( "pifa", pifa );
	    if ( pifa != null ) {
		groupMap.put( "pifaId", pifa.getId() );
		int pifaCount = 0;
		if ( CommonUtil.isNotEmpty( member ) ) {
		    pifaCount = mallPifaDAO.selectCountJoinNum( groupMap );
		}
		request.setAttribute( "pifaCount", pifaCount );

		List< MallProductInventory > invenList = mallProductInventoryService.selectInvenByProductId( pifa.getProductId() );
		if ( invenList != null && invenList.size() > 0 ) {
		    request.setAttribute( "invenList", JSONArray.fromObject( invenList ) );
		}
	    }
	}
	return inv_id;
    }

    @Override
    public List< Map< String,Object > > productPresale( Integer stoId, Map< String,Object > params ) {

	/*String sql = "SELECT distinct(a.id),a.pro_price,a.pro_name as proName,a.is_specifica,b.product_introdu as product_introdu,a.is_specifica,c.image_url,e.specifica_img_url,d.inv_num,d.inv_price,d.specifica_img_id,"
			+ " mp.sale_start_time,mp.sale_end_time,mp.id as presaleId,mp.order_num"
			+ " from t_mall_presale mp"
			+ " left join t_mall_product a  on a.id=mp.product_id"
			+ " LEFT JOIN t_mall_product_detail b ON a.id=b.product_id"
			+ " LEFT JOIN (SELECT ass_id,image_url from t_mall_image_associative WHERE ass_type=1 AND is_delete=0 AND is_main_images=1) c ON a.id=c.ass_id"
			+ " LEFT JOIN (SELECT product_id,specifica_img_id,inv_num,inv_price FROM t_mall_product_inventory where is_default=1 AND is_delete=0)d ON a.id=d.product_id"
			+ " LEFT JOIN t_mall_product_specifica e ON d.specifica_img_id=e.id "
			+ " WHERE a.shop_id=" + stoId + " AND a.is_publish=1 AND a.check_status=1 AND a.is_delete=0 "
			+ " and ((mp.sale_start_time > now() and   mp.sale_end_time > now() ) or (mp.sale_start_time <= now() and now() < mp.sale_end_time))"
			+ " and mp.is_use=1 and mp.is_delete=0";*/
	params.put( "shopId", params.get( "stoId" ) );
	if ( CommonUtil.isNotEmpty( params.get( "proName" ) ) ) {
	    params.put( "searchName", params.get( "proName" ) );
	    /*sql += " and a.pro_name like '%" + params.get( "proName" ) + "%'";*/
	}
	/*if ( CommonUtil.isNotEmpty( params.get( "groupId" ) ) ) {
	    sql += " and a.id in (select product_id from t_mall_product_group where group_id = " + params.get( "groupId" ) + ")";
	}*/
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
		map.put( "preId", map.get( "presaleId" ) );
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

    @Override
    public Map< String,Object > getProductHome( Map< String,Object > map3, Map< String,Object > map2, Member member, String http, double discount, MallPaySet set, int state ) {
	boolean isPifa = mallPifaApplyService.isPifaPublic( member, set );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getPfRemark() ) ) {
		map2.put( "pfRemark", set.getPfRemark() );
	    }
	}
	if ( CommonUtil.isNotEmpty( map2.get( "userId" ) ) ) {
	    if ( CommonUtil.isNotEmpty( map3.get( "pro_type_id" ) ) && CommonUtil.isNotEmpty( map3.get( "member_type" ) ) ) {
		if ( CommonUtil.toInteger( map3.get( "pro_type_id" ) ) == 2 && CommonUtil.toInteger( map3.get( "pro_type_id" ) ) > 0 ) {
		    map2.put( "url", "/phoneMemberController/" + map2.get( "userId" ) + "/79B4DE7C/findMember_1.do" );
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
	    String specifica_img_id = "0";
	    if ( CommonUtil.isNotEmpty( map3.get( "specifica_img_id" ) ) ) {
		specifica_img_id = map3.get( "specifica_img_id" ).toString();
	    }
	    if ( specifica_img_id.equals( "0" ) ) {
		map2.put( "src", http + map3.get( "image_url" ) );
	    } else {
		map2.put( "src", http + map3.get( "specifica_img_url" ) );
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
	Map< String,Object > params = new HashMap<>();
	params.put( "pfType", 1 );
	params.put( "product_id", map3.get( "id" ) );
	//查询商品是否已经加入批发
	List< MallPifa > pifaList = mallPifaDAO.selectStartPiFaByProductId( params );
	boolean isPfPrice = false;
	if ( pifaList != null && pifaList.size() > 0 && isPifa ) {
	    MallPifa pifa = pifaList.get( 0 );
	    if ( CommonUtil.isNotEmpty( pifa.getPfPrice() ) ) {
		isPfPrice = true;
		map2.put( "pfPrice", df.format( pifa.getPfPrice() ) );
	    }
	}
	if ( !isPfPrice ) {
	    map2.remove( "pfPrice" );
	}
	if ( CommonUtil.isNotEmpty( map3.get( "pro_label" ) ) ) {
	    map2.put( "pro_label", map3.get( "pro_label" ) );
	}
	return map2;
    }

    @Override
    public Map< String,Object > getCardReceive( int receiveId ) {
	//todo 调用彭江丽接口   根据卡包查询卡券信息展示 map中key
	//Map< String,Object > cardMap = duofenCardService.findduofenCardByReceiveId( receiveId );
	return null;
    }

    @Override
    public int memberBuyProNum( int memberId, Map< String,Object > params, int type ) {
	params.put( "buyerUserId", memberId );
	return mallOrderDAO.selectMemberBuyProNum( params );
    }

    @Override
    public String setProductParam( String url, Map< String,Object > params ) {
	int size = 2;
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "saleMemberId" ) ) ) {
		size = size + 1;
		url += "&&logo" + size + "=" + params.get( "saleMemberId" );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "share" ) ) ) {
		size = size + 1;
		url += "&&logo" + size + "=" + params.get( "share" );
	    }
	}
	url += "&&paramSize=" + size;
	return url;
    }

    @Override
    public Map< String,Object > getPublicByUserMap( Map< String,Object > userMap ) {
	Map< String,Object > publicUserid = null;
	if ( CommonUtil.isNotEmpty( userMap ) ) {
	    String pid = userMap.get( "pid" ).toString();
	    if ( pid.equals( "0" ) ) {
		publicUserid = publicUserid( Integer.valueOf( userMap.get( "id" ).toString() ) );
	    } else {
		publicUserid = publicUserid( Integer.valueOf( userMap.get( "pid" ).toString() ) );
	    }
	}
	return publicUserid;
    }

    private Map< String,Object > publicUserid( Integer userid ) {
	//todo 调用小屁孩的接口 根据商家id查询公众号i信息
	//	String sql = "SELECT id,qrcode_url,bus_user_id FROM t_wx_public_users WHERE bus_user_id="+userid;
	return null;
    }

    /**
     * 判断用户是否已经登陆
     */
    @Override
    public boolean isLogin( Member member, int userid, HttpServletRequest request ) {
	boolean isLogin = true;//是否已经登陆   true 已登陆   false未登录
	if ( CommonUtil.isNotEmpty( member ) ) {
	    if ( CommonUtil.isNotEmpty( member.getBusid() ) ) {
		if ( !member.getBusid().toString().equals( CommonUtil.toString( userid ) ) ) {
		    request.getSession().setAttribute( "member", null );//清空缓存
		    isLogin = false;
		}
	    } else {
		isLogin = false;
	    }
	} else {
	    isLogin = false;
	}
	/*String code=Constants.UCLOGINKEY;
	String url = CommonUtil.getpath(request);
	JedisUtil.set(code, url, 60*60);*/
	return isLogin;
    }

    /**
     * 保存地址到reids
     */
    @Override
    public Map< String,Object > saveRedisByUrl( Member member, int userid, HttpServletRequest request ) {
	Map< String,Object > loginMap = new HashMap<>();
	String redisKey = CommonUtil.getCode();
	String url = CommonUtil.getpath( request );
	url = url.substring( url.indexOf( request.getServletPath() ), url.length() );
	JedisUtil.set( redisKey, url, 5 * 60 );
	loginMap.put( "redisKey", redisKey );
	request.setAttribute( "userid", userid );
	return loginMap;
    }

    @Override
    public void mergeShoppCart( Member member, HttpServletRequest request, HttpServletResponse response ) {
	//1.得到所有cookie中的商品
	//2.遍历，查询该商品在会员购物车中是否存在，  存在则修改数据(数量+1)，并删除cookie商品 ，不存在则新增
	String shoppCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
	if ( shoppCartIds == null || "".equals( shoppCartIds ) ) {
	    return;
	}
	/*String sql = "SELECT t.id,t.product_id,t.shop_id,t.product_specificas FROM t_mall_shop_cart t WHERE t.`user_id` IS NULL AND  t.id in (" + shoppCartIds + ")";*/

	Wrapper< MallShopCart > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "id,product_id,shop_id,product_specificas" );
	wrapper.isNotNull( "user_id" );
	wrapper.in( "id", shoppCartIds.split( "," ) );
	List< Map< String,Object > > list = mallShopCartDAO.selectMaps( wrapper );
	if ( list != null && list.size() > 0 ) {
	    String[] ids = new String[] {};
	    String deletes = "";
	    for ( Map< String,Object > map : list ) {
		/*String sql1 = "SELECT id FROM t_mall_shop_cart t WHERE t.product_id=" + map.get( "product_id" ) +
				" AND t.shop_id=" + map.get( "shop_id" ) + " AND t.product_specificas= '" + map.get( "product_specificas" ) + "' AND t.user_id=" + member.getId();*/
		wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect( "id" );
		wrapper.where( "t.product_id = {0} AND t.shop_id={1} AND t.product_specificas= {2} AND t.user_id={3}", map.get( "product_id" ), map.get( "shop_id" ),
				map.get( "product_specificas" ), member.getId() );
		List< Map< String,Object > > countList = mallShopCartDAO.selectMaps( wrapper );
		if ( countList.size() > 0 ) {
		    //数量+1
		    MallShopCart cart = new MallShopCart();
		    cart.setId( CommonUtil.toInteger( countList.get( 0 ).get( "id" ) ) );
		    cart.setProductNum( 1 );
		    mallShopCartDAO.updateByShopCart( cart );

		    //记录id，删除多余记录
		    if ( "".equals( deletes ) ) {
			deletes = "0," + map.get( "id" ).toString();
		    } else {
			deletes += "," + map.get( "id" ).toString();
		    }

		} else {
		    //记录id，变更用户
		    ids[ids.length] = map.get( "id" ).toString();
		}
	    }
	    if ( CommonUtil.isNotEmpty( ids ) && ids.length > 0 ) {
		/*String sql2 = "UPDATE t_mall_shop_cart SET user_id =" + member.getId() + " ,bus_user_id=" + member.getBusid();
		sql2 += " WHERE id in(" + ids + ")";*/
		mallShopCartDAO.updateShopCarts( member.getId(), member.getBusid(), ids );

	    }
	    if ( !"".equals( deletes ) ) {
		shoppingdelect( deletes, "", 1 );
	    }
	    CookieUtil.delCookie( request, response, CookieUtil.SHOP_CART_COOKIE_KEY );
	}

    }

    @Override
    public Map< String,Object > queryAreaById( Integer id ) {
	//	String sql = "SELECT  * FROM `basis_city` where id =" + id;
	//	return daoUtil.queryForMap( sql );

	//todo 调用陈丹的接口，通过城市id查询城市信息
	return null;
    }

    /***
     * 未登陆时，查询cookie商品记录，用于判断新增/修改
     */
    private List< Map< String,Object > > selectByShopCart( MallShopCart obj, String ids ) {
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

}
