package com.gt.mall.service.web.store.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.wx.shop.ShopPhoto;
import com.gt.mall.bean.wx.shop.ShopSubsop;
import com.gt.mall.bean.wx.shop.WsWxShopInfo;
import com.gt.mall.bean.wx.shop.WsWxShopInfoExtend;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MallJxcHttpClientUtil;
import com.gt.mall.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    private BusUserService busUserService;

    @Override
    public PageUtil findByPage( Map< String,Object > params, List< Map< String,Object > > shopList ) {
	int pageSize = 10;
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int rowCount = mallStoreDao.countByPage( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "store/index.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );

	params.put( "shopList", shopList );

	List< Map< String,Object > > list = mallStoreDao.findByPage( params );
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > shopMap : list ) {
		int id = CommonUtil.toInteger( shopMap.get( "id" ) );

		for ( Map< String,Object > maps : shopList ) {
		    int shopIds = CommonUtil.toInteger( maps.get( "id" ) );
		    if ( id == shopIds ) {
			shopMap.put( "sto_name", maps.get( "sto_name" ) );
		    }
		}
	    }
	}

	page.setSubList( list );
	return page;
    }

    @Override
    public List< Map< String,Object > > findAllStore( Integer userId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.setSqlSelect( "id,sto_name" );
	storeWrapper.where( "is_delete = 0 and sto_pid = 0 and sto_user_id = {0}", userId );
	return mallStoreDao.selectMaps( storeWrapper );
    }

    @Override
    public boolean isAdminUser( int userId ) {
	//todo 还需调用陈丹的接口
	return busUserService.getIsAdmin( userId );
    }

    @Override
    public int countBranch( Integer userId ) {
	//	String sql = "SELECT count(1) from bus_user_branch_manage WHERE userid="+userId;
	//todo 调用陈丹  分店接口
	return 0;
    }

    @Override
    public int countStroe( Integer userId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.where( "sto_user_id={0}", userId );
	return mallStoreDao.selectCount( storeWrapper );
    }

    @Override
    public List< Map< String,Object > > findShop( int userId, int shopId ) {
	//todo 调用小屁孩接口 根据商家id查询门店信息
	return null;
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

    @Override
    public int getShopBySession( HttpSession session, int shopId ) {
	String sessionKey = Constants.SESSION_KEY + "shopId";
	if ( CommonUtil.isEmpty( session.getAttribute( sessionKey ) ) ) {
	    session.setAttribute( sessionKey, shopId );
	} else {
	    if ( !session.getAttribute( sessionKey ).toString().equals( String.valueOf( shopId ) ) ) {
		session.setAttribute( sessionKey, shopId );
	    } else {
		shopId = CommonUtil.toInteger( session.getAttribute( sessionKey ) );
	    }
	}
	return shopId;
    }

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
	    // todo 对接李逢喜的接口   根据门店id查询门店信息
	    Map< String,Object > map = new HashMap<>();
	    map.put( "id", mallStore.getWxShopId() );
	    map.put( "phone", mallStore.getStoPhone() );
	    map.put( "principal", mallStore.getStoLinkman() );
	    try {
		WsWxShopInfo shopInfo = wxShopService.getShopById( mallStore.getWxShopId() );
		if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		    map.put( "name", shopInfo.getBusinessName() );
		    //todo 地址不详细
		    map.put( "address", shopInfo.getAddress() );
		}
	    } catch ( Exception e ) {
		logger.error( "CXF调用接口《根据门店id查询门店信息异常》：" + e.getMessage() );
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
    public Map< String,Object > findShopByStoreId( Integer id ) throws Exception {
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
		List< ShopPhoto > photoList = wxShopService.getShopPhotoByShopId( store.getWxShopId() );
		if ( photoList != null && photoList.size() > 0 ) {
		    storeMap.put( "stoPicture", photoList.get( 0 ).getLocalAddress() );
		} else {
		    storeMap.put( "stoPicture", store.getStoPicture() );
		}
		String details = "";
		String cityids = shopInfo.getProvince() + "," + shopInfo.getCity() + "," + shopInfo.getDistrict();
		List< Map > cityList = wxShopService.queryBasisCityIds( cityids );
		if ( cityList != null && cityList.size() > 0 ) {
		    for ( Map map : cityList ) {
			String cityId = CommonUtil.toString( map.get( "id" ) );
			String cityName = CommonUtil.toString( map.get( "city_name" ) );
			if ( cityId.equals( shopInfo.getProvince() ) ) {
			    details = cityName + details;
			} else if ( cityId.equals( shopInfo.getCity() ) ) {
			    details = cityName + details;
			} else if ( cityId.equals( shopInfo.getDistrict() ) ) {
			    details += cityName;
			}
			break;
		    }

		}

		storeMap.put( "stoAddress", details + shopInfo.getAddress() + shopInfo.getDetail() );
	    }
	}
	return storeMap;
    }

    @Override
    public Map< String,Object > saveOrUpdate( MallStore sto, BusUser user ) throws Exception {
	boolean result = true;
	String message = "操作成功！";
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    message = valtion( sto );
	    if ( CommonUtil.isNotEmpty( message ) ) {
		result = false;
	    } else {
		/*if (CommonUtil.isEmpty(sto.getStoPid()) || sto.getStoPid() == 0) {
			sto.setStoIsMain(1);
		} else {
			sto.setStoIsMain(2);
		}*/
		sto.setStoIsMain( -1 );
		int count = 0;
		int id = 0;
		if ( CommonUtil.isNotEmpty( sto.getId() ) ) {
		    id = sto.getId();
		}
		//判断是否已经选择店铺id
		List< MallStore > list = findByShopId( sto.getWxShopId(), id );
		if ( list != null && list.size() > 0 ) {
		    result = false;
		    message = "请重新选择店铺，该店铺已经被添加";
		} else {
		    if ( CommonUtil.isEmpty( sto.getId() ) ) {
			count = mallStoreDao.insert( sto );
			if ( count <= 0 ) {
			    throw new Exception( "编辑店铺失败" );
			} else {
			    ShopSubsop shopSubsop = new ShopSubsop();
			    shopSubsop.setModel( CommonUtil.toInteger( Constants.SHOP_SUB_SOP_MODEL ) );
			    shopSubsop.setShopId( sto.getWxShopId() );
			    shopSubsop.setSubShop( sto.getId() );
			    boolean flag = wxShopService.addShopSubShop( shopSubsop );
			    if ( !flag ) {
				throw new Exception( "添加门店中间表异常" );
			    }
			}
		    } else {
			count = mallStoreDao.updateById( sto );
			if ( count <= 0 ) {
			    throw new Exception( "编辑店铺失败" );
			}
		    }

		    if ( CommonUtil.isEmpty( sto.getStoQrCode() ) ) {
			logger.info( "进入二维码生成！" );
			count = mallStoreDao.updateById( sto );
			if ( count <= 0 ) {
			    throw new Exception( "操作失败，店铺二维码生成失败！" );
			}
		    }
		    message = "操作成功！";
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new Exception( "编辑店铺失败" );
	} finally {
	    msg.put( "result", result );
	    msg.put( "message", message );
	}
	return msg;
    }

    @Override
    public Map< String,Object > deleteShop( String[] ids ) throws Exception {
	boolean result = true;
	String message = "删除成功！";
	Map< String,Object > msg = new HashMap< String,Object >();
	try {
	    mallStoreDao.updateByIds( ids );//逻辑删除店铺
	    //todo 调用小屁孩接口 删除中间门店记录 shopSubsopService.updateBySubShop
	    /*if(ids != null && ids.length > 0){
		for (String string : ids) {
		    if(CommonUtil.isNotEmpty(string)){
			shopSubsopService.updateBySubShop(CommonUtil.toInteger(string),shopSubSopModel);
		    }
		}
	    }*/
	} catch ( Exception e ) {
	    e.printStackTrace();
	    throw new Exception( "删除失败，系统异常！" );
	} finally {
	    msg.put( "result", result );
	    msg.put( "message", message );
	}
	return msg;
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
    public List< Map< String,Object > > findAllStoByUser( BusUser user ) {
	//todo 调用陈丹接口  根据商家id  查询门店信息
	List< Integer > wxShopIds = new ArrayList<>();

	List< Map< String,Object > > storeList = new ArrayList<>();
	//判断session里面有没有门店集合
	/*List< Map > shopList = getShopListBySession( user.getId(), null );
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map shopMap : shopList ) {
		storeList.add( shopMap );
	    }
	    return storeList;
	}*/
	List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( user.getId() );
	if ( shopInfoList != null && shopInfoList.size() > 0 ) {
	    for ( WsWxShopInfoExtend wsWxShopInfoExtend : shopInfoList ) {
		wxShopIds.add( wsWxShopInfoExtend.getId() );
	    }
	    Wrapper< MallStore > wrapper = new EntityWrapper<>();
	    wrapper.where( "is_delete = 0" ).in( "wx_shop_id", wxShopIds );
	    wrapper.setSqlSelect( "id,sto_name,wx_shop_id" );

	    storeList = mallStoreDao.selectMaps( wrapper );
	    if ( storeList != null && storeList.size() > 0 ) {
		for ( Map< String,Object > storeMap : storeList ) {
		    int wxShopId = CommonUtil.toInteger( storeMap.get( "wx_shop_id" ) );
		    for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
			if ( wxShops.getId() == wxShopId ) {
			    storeMap.put( "sto_name", wxShops.getBusinessName() );
			    break;
			}
		    }
		}
	    }
	    return storeList;
	}
	return null;
    }

    private void setShopListBySession( int userId, List< Map > shopList, HttpServletRequest request ) {
	HttpSession session = request.getSession();
	if ( CommonUtil.isNotEmpty( session ) ) {
	    String sessionKey = Constants.SESSION_KEY + "bus_shop_list_" + userId;
	    session.setAttribute( sessionKey, JSONArray.toJSONString( shopList ) );
	}

    }

    private List< Map > getShopListBySession( int userId, HttpServletRequest request ) {
	HttpSession session = request.getSession();
	if ( CommonUtil.isEmpty( session ) ) {
	    return null;
	}
	String sessionKey = Constants.SESSION_KEY + "bus_shop_list_" + userId;
	Object obj = session.getAttribute( sessionKey );
	if ( CommonUtil.isEmpty( obj ) ) {
	    return null;
	}
	return JSONArray.parseArray( obj.toString(), Map.class );
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
			//todo 地址不详细
			map.put( "name", shopInfo.getBusinessName() );
			map.put( "address", shopInfo.getAddress() );
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

}
