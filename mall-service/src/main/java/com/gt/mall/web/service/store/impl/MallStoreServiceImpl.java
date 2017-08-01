package com.gt.mall.web.service.store.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.result.shop.WsWxShopInfo;
import com.gt.mall.constant.Constants;
import com.gt.mall.cxf.service.WxShopService;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MallJxcHttpClientUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.store.MallStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    @Override
    public PageUtil findByPage( Map< String,Object > params ) {
	int pageSize = 10;
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int rowCount = mallStoreDao.count( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "store/index.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	List< Map< String,Object > > list = mallStoreDao.findByPage( params );
	//todo 还需补充关联门店数据 和店铺id

	page.setSubList( list );
	return page;
    }

    @Override
    public boolean isAdminUser( int userId ) {
	//todo 还需调用陈丹的接口

	return false;
    }

    @Override
    public List< Map< String,Object > > findByUserId( Integer userId ) {
	//todo 还需调用陈丹的接口   根据商家id获取所属的门店id

	List< Integer > shopIds = new ArrayList< Integer >();
	List< Map< String,Object > > storeList = mallStoreDao.findByShopIds( shopIds );
	return null;
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
		if(CommonUtil.isNotEmpty( shopInfo )){
		    map.put("name", shopInfo.getBusinessName());
		    map.put("address", shopInfo.getAddress());
		}
	    }catch ( Exception e ){
	        logger.error( "CXF调用接口《根据门店id查询门店信息异常》："+e.getMessage() );
	    }
	    if(!map.containsKey( "name" )){
	        map.put( "name",mallStore.getStoName() );
	    }
	    if(!map.containsKey( "address" )){
	        map.put( "address",mallStore.getStoAddress() );
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
	    storeWrapper.where( "id !={0}", isNotId );
	}
	return mallStoreDao.selectList( storeWrapper );
    }

    @Override
    public List< Map< String,Object > > selectStoreByUserId( int userId ) {
	//SELECT id,sto_name AS name FROM t_mall_store WHERE sto_user_id=" + userid + " AND is_delete=0
	Wrapper wrapper = new EntityWrapper();
	wrapper.where( "sto_user_id={0} AND is_delete=0", userId );
	wrapper.setSqlSelect( "id,sto_name AS name" );
	return mallStoreDao.selectMaps( wrapper );
    }

}
