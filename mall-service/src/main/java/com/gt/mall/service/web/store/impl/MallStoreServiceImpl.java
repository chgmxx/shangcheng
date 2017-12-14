package com.gt.mall.service.web.store.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.store.StoreResult;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.shop.ShopSubsop;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallStoreServiceImpl extends BaseServiceImpl< MallStoreDAO,MallStore > implements MallStoreService {

    private static Logger logger = LoggerFactory.getLogger( MallStoreServiceImpl.class );

    @Autowired
    private MallStoreDAO mallStoreDao;

    @Autowired
    private WxShopService wxShopService;

    @Autowired
    private BusUserService                busUserService;
    @Autowired
    private MallStoreCertificationService mallStoreCertService;

    @Override
    public PageUtil findByPage( Map< String,Object > params, List< Map< String,Object > > shopList ) {
	int pageSize = 10;
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int rowCount = mallStoreDao.countByPage( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "store/index.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	params.put( "shopList", shopList );

	List< StoreResult > storeResultList = new ArrayList<>();
	EntityDtoConverter converter = new EntityDtoConverter();

	List< Map< String,Object > > list = mallStoreDao.findByPage( params );
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > shopMap : list ) {
		int id = CommonUtil.toInteger( shopMap.get( "id" ) );
		StoreResult storeResult = new StoreResult();
		converter.mapToBean( shopMap, storeResult );
		MallStoreCertification storeCertification = mallStoreCertService.selectByStoreId( id );
		if ( storeCertification != null ) {
		    storeResult.setCertId( storeCertification.getId() );
		    storeResult.setCertStoType( storeCertification.getStoType() );
		    if ( storeCertification.getStoType() == 1 ) {
			storeResult.setCertStoCategory( storeCertification.getStoCategory() );
			storeResult.setCertStoCategoryName( storeCertification.getStoCategoryName() );
		    }
		}
		for ( Map< String,Object > maps : shopList ) {
		    int shopIds = CommonUtil.toInteger( maps.get( "id" ) );
		    if ( id == shopIds ) {
			storeResult.setStoName( maps.get( "sto_name" ).toString() );
			storeResult.setStoAddress( maps.get( "address" ).toString() );
			break;
		    }
		}
		storeResultList.add( storeResult );
	    }
	}

	page.setSubList( storeResultList );
	return page;
    }

    @Override
    public List< Map< String,Object > > findAllStore( Integer userId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.setSqlSelect( "id,sto_name,wx_shop_id" );
	storeWrapper.where( "is_delete = 0 and sto_user_id = {0}", userId );
	return mallStoreDao.selectMaps( storeWrapper );
    }

    @Override
    public boolean isAdminUser( int userId ) {

	return busUserService.getIsAdmin( userId );
    }

    @Override
    public int countStroe( Integer userId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.where( "sto_user_id={0} and is_delete = 0", userId );
	return mallStoreDao.selectCount( storeWrapper );
    }

    @Override
    public List< Map< String,Object > > findByUserId( Integer userId ) {
	List< Integer > wxShopIds = new ArrayList<>();
	List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( userId );
	if ( shopInfoList != null && shopInfoList.size() > 0 ) {
	    for ( WsWxShopInfoExtend wsWxShopInfoExtend : shopInfoList ) {
		if ( !wxShopIds.contains( wsWxShopInfoExtend.getId() ) ) {
		    wxShopIds.add( wsWxShopInfoExtend.getId() );
		}
	    }
	}
	List< Map< String,Object > > newStoreList = new ArrayList<>();
	List< Map< String,Object > > storeList = mallStoreDao.findByShopIds( wxShopIds );
	if ( storeList != null && storeList.size() > 0 ) {
	    for ( Map< String,Object > map : storeList ) {
		Map< String,Object > storeMap = new HashMap<>();
		int wx_shop_id = CommonUtil.isNotEmpty( map.get( "wx_shop_id" ) ) ? CommonUtil.toInteger( map.get( "wx_shop_id" ) ) : 0;
		if ( wx_shop_id > 0 ) {
		    for ( int i = 0; i < shopInfoList.size(); i++ ) {
			WsWxShopInfoExtend shopInfo = shopInfoList.get( i );
			if ( wx_shop_id == shopInfo.getId() ) {
			    storeMap.put( "shopName", shopInfo.getBusinessName() );
			    storeMap.put( "shopPicture", shopInfo.getImageUrl() );
			    storeMap.put( "sto_address", shopInfo.getAddress() );
			    storeMap.put( "longitude", shopInfo.getLongitude() );
			    storeMap.put( "latitude", shopInfo.getLatitude() );
			    storeMap.put( "telephone", shopInfo.getTelephone() );
			    storeMap.put( "id", map.get( "id" ) );
			    storeMap.put( "wx_shop_id", shopInfo.getId() );
			    shopInfoList.remove( i );
			    break;
			}
		    }
		}
		newStoreList.add( storeMap );
	    }
	}
	return newStoreList;
    }

    //    @Override
    //    public int getShopBySession( HttpSession session, int shopId ) {
    //	String sessionKey = Constants.SESSION_KEY + "shopId";
    //	if ( CommonUtil.isEmpty( session.getAttribute( sessionKey ) ) ) {
    //	    session.setAttribute( sessionKey, shopId );
    //	} else {
    //	    if ( !session.getAttribute( sessionKey ).toString().equals( String.valueOf( shopId ) ) ) {
    //		session.setAttribute( sessionKey, shopId );
    //	    } else {
    //		shopId = CommonUtil.toInteger( session.getAttribute( sessionKey ) );
    //	    }
    //	}
    //	return shopId;
    //    }

    @Override
    public int createCangku( int shopId, BusUser user, int uType ) {
	int wxShopId = 0;
	//创建仓库
	Map< String,Object > storeParams = new HashMap<>();
	storeParams.put( "createUid", user.getId() );
	storeParams.put( "uidType", uType );
	List< Map< String,Object > > lists = new ArrayList<>();
	MallStore mallStore = this.selectById( shopId );
	if ( CommonUtil.isNotEmpty( mallStore ) ) {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", mallStore.getWxShopId() );
	    map.put( "phone", mallStore.getStoPhone() );
	    map.put( "principal", mallStore.getStoLinkman() );
	    try {
		WsWxShopInfo shopInfo = wxShopService.getShopById( mallStore.getWxShopId() );
		if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		    map.put( "name", shopInfo.getBusinessName() );
		    map.put( "address", getWxShopDetailAddress( shopInfo ) );
		}
	    } catch ( Exception e ) {
		logger.error( "调用门店接口《根据门店id查询门店信息异常》：" + e.getMessage() );
	    }
	    if ( !map.containsKey( "name" ) ) {
		map.put( "name", mallStore.getStoName() );
	    }
	    if ( !map.containsKey( "address" ) ) {
		map.put( "address", mallStore.getStoAddress() );
	    }
	    lists.add( map );
	} else {
	    return 0;
	}
	storeParams.put( "shopList", com.alibaba.fastjson.JSONArray.toJSON( lists ) );
	boolean flag = MallJxcHttpClientUtil.saveUpdateWarehouse( storeParams, true );
	if ( flag ) {
	    return wxShopId;
	}
	return 0;
    }

    @Override
    public List< MallStore > findByShopId( int wxShopId, int isNotId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.where( "is_delete = 0 and wx_shop_id = {0}", wxShopId );
	if ( isNotId > 0 ) {
	    storeWrapper.where( "id != {0}", isNotId );
	}
	return mallStoreDao.selectList( storeWrapper );
    }

    @Override
    public List< Map< String,Object > > selectStoreByUserId( int userId ) {
	//SELECT id,sto_name AS name FROM t_mall_store WHERE sto_user_id=" + userid + " AND is_delete=0
	Wrapper< MallStore > wrapper = new EntityWrapper< MallStore >();
	wrapper.where( "sto_user_id={0} AND is_delete=0", userId );
	wrapper.setSqlSelect( "id,sto_name AS name" );
	return mallStoreDao.selectMaps( wrapper );
    }

    @Override
    public Map< String,Object > findShopByStoreId( Integer id ) {
	MallStore store = mallStoreDao.selectById( id );

	Map< String,Object > storeMap = JSONObject.parseObject( JSONObject.toJSONString( store ), Map.class );

	if ( CommonUtil.isNotEmpty( store.getWxShopId() ) ) {
	    WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
	    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		storeMap.put( "stoName", shopInfo.getBusinessName() );
		storeMap.put( "stoPhone", shopInfo.getTelephone() );
		storeMap.put( "stoLongitude", shopInfo.getLongitude() );
		storeMap.put( "stoLatitude", shopInfo.getLatitude() );
		storeMap.put( "stoProvince", shopInfo.getProvince() );
		storeMap.put( "stoCity", shopInfo.getCity() );
		storeMap.put( "stoArea", shopInfo.getDistrict() );
		storeMap.put( "adder", shopInfo.getAddress() );
		storeMap.put( "stoHouseMember", shopInfo.getDetail() );
		List< WsShopPhoto > photoList = wxShopService.getShopPhotoByShopId( store.getWxShopId() );
		if ( photoList != null && photoList.size() > 0 ) {
		    storeMap.put( "stoPicture", photoList.get( 0 ).getLocalAddress() );
		} else {
		    storeMap.put( "stoPicture", store.getStoPicture() );
		}

		storeMap.put( "stoAddress", getWxShopDetailAddress( shopInfo ) );
		storeMap.put( "wxShopId", shopInfo.getId() );
	    }
	}
	return storeMap;
    }

    @Override
    public MallStore findShopByShopId( Integer shopId ) {
	MallStore store = mallStoreDao.selectById( shopId );
	if ( CommonUtil.isNotEmpty( store.getWxShopId() ) ) {
	    WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
	    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		store.setStoName( shopInfo.getBusinessName() );
		store.setStoPhone( shopInfo.getTelephone() );
		store.setStoLongitude( shopInfo.getLongitude() );
		store.setStoLatitude( shopInfo.getLatitude() );
		if ( CommonUtil.isNotEmpty( shopInfo.getProvince() ) ) {
		    store.setStoProvince( CommonUtil.toInteger( shopInfo.getProvince() ) );
		}
		if ( CommonUtil.isNotEmpty( shopInfo.getCity() ) ) {
		    store.setStoCity( CommonUtil.toInteger( shopInfo.getCity() ) );
		}
		if ( CommonUtil.isNotEmpty( shopInfo.getDistrict() ) ) {
		    store.setStoArea( CommonUtil.toInteger( shopInfo.getDistrict() ) );
		}
		store.setStoHouseMember( shopInfo.getDetail() );
		List< WsShopPhoto > photoList = wxShopService.getShopPhotoByShopId( store.getWxShopId() );
		if ( photoList != null && photoList.size() > 0 ) {
		    store.setStoPicture( photoList.get( 0 ).getLocalAddress() );
		}
		store.setStoAddress( getWxShopDetailAddress( shopInfo ) );
		if ( shopInfo.getStatus() == -1 ) {
		    store.setIsDelete( 1 );
		}
	    }
	}
	return store;
    }

    private String getWxShopDetailAddress( WsWxShopInfo shopInfo ) {
	String cityids = shopInfo.getProvince() + "," + shopInfo.getCity() + "," + shopInfo.getDistrict();
	List< Map > cityList = wxShopService.queryBasisCityIds( cityids );
	Map< String,String > cityMap = new HashMap<>();
	if ( cityList != null && cityList.size() > 0 ) {
	    for ( Map map : cityList ) {
		String cityId = CommonUtil.toString( map.get( "id" ) );
		String cityName = CommonUtil.toString( map.get( "city_name" ) );
		cityMap.put( cityId, cityName );
	    }
	}
	String details = "";
	if ( CommonUtil.isNotEmpty( cityMap ) && cityMap.size() > 0 ) {
	    details = cityMap.get( shopInfo.getProvince() ) + cityMap.get( shopInfo.getCity() ) + cityMap.get( shopInfo.getDistrict() );
	}
	return details + shopInfo.getAddress() + shopInfo.getDetail();
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean saveOrUpdate( MallStore sto, BusUser user ) throws BusinessException {
	try {
	    String message = valtion( sto );
	    if ( CommonUtil.isNotEmpty( message ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "店铺数据不能为空" );
	    } else {
		sto.setStoIsMain( -1 );
		int count = 0;
		int id = 0;
		if ( CommonUtil.isNotEmpty( sto.getId() ) ) {
		    id = sto.getId();
		}
		//判断是否已经选择店铺id
		List< MallStore > list = findByShopId( sto.getWxShopId(), id );
		if ( list != null && list.size() > 0 ) {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "请重新选择店铺，该店铺已经被添加" );
		} else {
		    if ( CommonUtil.isEmpty( sto.getId() ) ) {
			count = mallStoreDao.insert( sto );
			if ( count <= 0 ) {
			    throw new BusinessException( ResponseEnums.ERROR.getCode(), "编辑店铺失败" );
			} else {
			    ShopSubsop shopSubsop = new ShopSubsop();
			    shopSubsop.setModel( Constants.SHOP_SUB_SOP_MODEL );
			    shopSubsop.setShopId( sto.getWxShopId() );
			    shopSubsop.setSubShop( sto.getId() );
			    boolean flag = wxShopService.addShopSubShop( shopSubsop );
			    if ( !flag ) {
				throw new BusinessException( ResponseEnums.ERROR.getCode(), "添加门店中间表异常" );
			    }
			}
		    } else {
			MallStore store = mallStoreDao.selectById( sto.getId() );
			store.setStoHeadImg( sto.getStoHeadImg() );
			store.setStoLinkman( sto.getStoLinkman() );
			store.setStoPhone( sto.getStoPhone() );
			store.setStoIsSms( sto.getStoIsSms() );
			store.setStoSmsTelephone( sto.getStoSmsTelephone() );
			store.setStoQqCustomer( sto.getStoQqCustomer() );
			count = mallStoreDao.updateAllColumnById( store );
			if ( count <= 0 ) {
			    throw new BusinessException( ResponseEnums.ERROR.getCode(), "编辑店铺失败" );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "编辑店铺失败" );
	}
	return true;
    }

    @Override
    public boolean deleteShop( String[] ids ) throws Exception {
	try {
	    mallStoreDao.updateByIds( ids );//逻辑删除店铺
	    if ( ids != null && ids.length > 0 ) {
		for ( String string : ids ) {
		    if ( CommonUtil.isNotEmpty( string ) ) {
			ShopSubsop shopSubsop = new ShopSubsop();
			shopSubsop.setSubShop( CommonUtil.toInteger( string ) );
			shopSubsop.setModel( Constants.SHOP_SUB_SOP_MODEL );
			wxShopService.updateBySubShop( shopSubsop );
		    }
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "删除失败，系统异常！" );
	}
	return true;
    }

    /**
     * 验证信息
     */
    private String valtion( MallStore sto ) {
	String message = "";
	if ( CommonUtil.isEmpty( sto.getWxShopId() ) ) {
	    message = "请选择门店";
	} else if ( CommonUtil.isEmpty( sto.getStoName() ) ) {
	    message = "请填写店铺名称！";
	} else if ( CommonUtil.isEmpty( sto.getStoPicture() ) ) {
	    message = "请上传店铺图片！";
	}
	//		else if (CommonUtil.isEmpty(sto.getStoCity())) {
	//			message = "请选择店铺所在的省市区！";
	//		}
	else if ( CommonUtil.isEmpty( sto.getStoAddress() ) ) {
	    message = "请选择店铺地址！";
	} else if ( CommonUtil.isEmpty( sto.getStoLinkman() ) ) {
	    message = "请填写联系人！";
	} else if ( CommonUtil.isEmpty( sto.getStoPhone() ) ) {
	    message = "请填写联系电话！";
	}
	//		else if (!CommonUtil.isMobileNO(sto.getStoPhone())) {
	//			message = "请填写正确的联系电话！";
	//		}
	return message;
    }

    /**
     * 获取登录人拥有的店铺集合
     */
    @Override
    public List< Map< String,Object > > findAllStoByUser( BusUser user, HttpServletRequest request ) {
	List< Integer > wxShopIds = new ArrayList<>();

	List< Map< String,Object > > storeList = new ArrayList<>();
	//判断session里面有没有门店集合
	List< Map > shopList = MallSessionUtils.getShopListBySession( user.getId(), request );
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map shopMap : shopList ) {
		storeList.add( shopMap );
	    }
	    return storeList;
	}
	List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( user.getId() );
	if ( shopInfoList != null && shopInfoList.size() > 0 ) {
	    MallSessionUtils.setWxShopNumBySession( user.getId(), shopInfoList.size(), request );

	    for ( WsWxShopInfoExtend wsWxShopInfoExtend : shopInfoList ) {
		wxShopIds.add( wsWxShopInfoExtend.getId() );
	    }
	    Wrapper< MallStore > wrapper = new EntityWrapper<>();
	    wrapper.where( "is_delete = 0" ).in( "wx_shop_id", wxShopIds );
	    wrapper.setSqlSelect( "id,sto_name,wx_shop_id as wxShopId,sto_longitude as stoLongitude,sto_latitude as stoLatitude,sto_picture as stoPicture" );

	    storeList = mallStoreDao.selectMaps( wrapper );
	    storeList = getShopParams( storeList, shopInfoList );
	    MallSessionUtils.setShopListBySession( user.getId(), storeList, request );
	    return storeList;
	}
	return null;
    }

    @Override
    public List< Map< String,Object > > findShopByUserIdAndShops( int userId, List< Integer > shopIdList ) {
	List< Map< String,Object > > storeList = findShopByUserId( userId, null );
	if ( storeList != null && shopIdList.size() > 0 && storeList != null && storeList.size() > 0 ) {
	    List< Map< String,Object > > removeList = new ArrayList<>();
	    for ( Map< String,Object > storeMap : storeList ) {
		boolean errorFlag = false;
		for ( Integer shopId : shopIdList ) {
		    if ( CommonUtil.toString( storeMap.get( "id" ) ).equals( shopId.toString() ) ) {
			errorFlag = true;
			break;
		    }
		}
		if ( !errorFlag ) {
		    removeList.add( storeMap );
		}
	    }
	    if ( removeList != null && removeList.size() > 0 ) {
		storeList.removeAll( removeList );
	    }
	}
	return storeList;
    }

    @Override
    public List< Map< String,Object > > findShopByUserId( int userId, HttpServletRequest request ) {
	List< Integer > wxShopIds = new ArrayList<>();

	List< Map< String,Object > > storeList = new ArrayList<>();
	//判断session里面有没有门店集合
	List< Map > shopList = MallSessionUtils.getShopListBySession( userId, request );
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map shopMap : shopList ) {
		storeList.add( shopMap );
	    }
	    return storeList;
	}
	List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( userId );
	if ( shopInfoList != null && shopInfoList.size() > 0 ) {
	    MallSessionUtils.setWxShopNumBySession( userId, shopInfoList.size(), request );

	    for ( WsWxShopInfoExtend wsWxShopInfoExtend : shopInfoList ) {
		wxShopIds.add( wsWxShopInfoExtend.getId() );
	    }
	    Wrapper< MallStore > wrapper = new EntityWrapper<>();
	    wrapper.where( "is_delete = 0" ).in( "wx_shop_id", wxShopIds );
	    wrapper.setSqlSelect( "id,sto_name,wx_shop_id as wxShopId,sto_longitude as stoLongitude,sto_latitude as stoLatitude,is_delete,sto_picture as stoPicture" );

	    storeList = mallStoreDao.selectMaps( wrapper );
	    storeList = getShopParams( storeList, shopInfoList );

	    MallSessionUtils.setShopListBySession( userId, storeList, request );
	    return storeList;
	}
	return null;
    }

    @Override
    public List< Map< String,Object > > findShopByShopIdList( List< Integer > busIdList ) {
	if ( busIdList == null || busIdList.size() == 0 ) {
	    return null;
	}
	List< Map< String,Object > > newShopList = new ArrayList<>();
	for ( Integer busId : busIdList ) {
	    List< Map< String,Object > > shopList = findShopByUserId( busId, null );
	}

	return null;
    }

    private List< Map< String,Object > > getShopParams( List< Map< String,Object > > storeList, List< WsWxShopInfoExtend > shopInfoList ) {
	if ( storeList != null && storeList.size() > 0 ) {
	    for ( Map< String,Object > storeMap : storeList ) {
		int wxShopId = CommonUtil.toInteger( storeMap.get( "wxShopId" ) );
		for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
		    if ( wxShops.getId() == wxShopId ) {
			storeMap.put( "sto_name", wxShops.getBusinessName() );
			storeMap.put( "address", wxShops.getAddress() );
			storeMap.put( "wxShopId", wxShops.getId() );
			storeMap.put( "stoLongitude", wxShops.getLongitude() );
			storeMap.put( "stoLatitude", wxShops.getLatitude() );
			storeMap.put( "stoPhone", wxShops.getTelephone() );
			break;
		    }
		}
	    }
	}
	return storeList;
    }

    @Override
    public boolean createCangkuAll( BusUser user ) {
	boolean result = true;
	int userPId = busUserService.getMainBusId( user.getId() );
	int uType = 1;//用户类型 1总账号  0子账号
	if ( user.getId() != userPId ) {
	    uType = 0;
	}
	//创建仓库
	Map< String,Object > storeParams = new HashMap< String,Object >();
	storeParams.put( "createUid", user.getId() );
	storeParams.put( "uidType", uType );

	List< Map< String,Object > > lists = new ArrayList< Map< String,Object > >();
	Wrapper< MallStore > wrapper = new EntityWrapper<>();
	wrapper.where( "sto_user_id = {0} AND is_delete=0 ", user.getId() );
	List< MallStore > storeList = mallStoreDao.selectList( wrapper );
	if ( storeList != null && storeList.size() > 0 ) {
	    for ( MallStore mallStore : storeList ) {
		Map< String,Object > map = new HashMap< String,Object >();
		map.put( "id", mallStore.getWxShopId() );
		map.put( "phone", mallStore.getStoPhone() );
		map.put( "principal", mallStore.getStoLinkman() );
		try {
		    WsWxShopInfo shopInfo = wxShopService.getShopById( mallStore.getWxShopId() );
		    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
			map.put( "name", shopInfo.getBusinessName() );
			map.put( "address", getWxShopDetailAddress( shopInfo ) );
		    }
		} catch ( Exception e ) {
		    logger.error( "CXF调用接口《根据门店id查询门店信息异常》：" + e.getMessage() );
		}
		lists.add( map );
	    }
	}
	storeParams.put( "shopList", com.alibaba.fastjson.JSONArray.toJSON( lists ) );
	result = MallJxcHttpClientUtil.saveUpdateWarehouse( storeParams, true );

	return result;
    }

    @Override
    public int getIsErpCount( int userId, HttpServletRequest request ) {
	int isJxc = MallSessionUtils.getIsJxc( userId, request );
	if ( isJxc == -1 ) {//重新获取商家是否开通进销存
	    isJxc = busUserService.getIsErpCount( 8, userId );//判断商家是否有进销存 0没有 1有
	    MallSessionUtils.setIsJxc( userId, isJxc, request );
	}
	return isJxc;
    }

    @Override
    public boolean getIsAdminUser( int userId, HttpServletRequest request ) {
	int isAdmin = MallSessionUtils.getIsAdminUser( userId, request );
	if ( isAdmin == -1 ) {
	    boolean flag = busUserService.getIsAdmin( userId );
	    if ( flag ) {
		isAdmin = 1;
	    } else {
		isAdmin = 0;
	    }
	    MallSessionUtils.setIsAdminUser( userId, isAdmin, request );
	    return flag;
	} else {
	    return true;
	}
    }

}
