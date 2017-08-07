package com.gt.mall.web.service.store.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.result.shop.WsWxShopInfo;
import com.gt.mall.constant.Constants;
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

//    @Autowired
//    private WxShopService wxShopService;

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
    public List< Map< String,Object > > findAllStore( Integer userId ) {
	Wrapper< MallStore > storeWrapper = new EntityWrapper<>();
	storeWrapper.setSqlSelect( "id,sto_name" );
	storeWrapper.where( "is_delete = 0 and sto_pid = 0 and sto_user_id = {0}", userId );
	return mallStoreDao.selectMaps( storeWrapper );
    }

    @Override
    public boolean isAdminUser( int userId ) {
	//todo 还需调用陈丹的接口

	return false;
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
	//todo 还需调用陈丹的接口   根据商家id获取所属的门店id
	List< Map< String,Object > > storeList = mallStoreDao.findByShopIds( wxShopIds );
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
		//TODO  wxShopService.getShopById()
		WsWxShopInfo shopInfo =null;// wxShopService.getShopById( mallStore.getWxShopId() );
		if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		    map.put( "name", shopInfo.getBusinessName() );
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
	Wrapper wrapper = new EntityWrapper();
	wrapper.where( "sto_user_id={0} AND is_delete=0", userId );
	wrapper.setSqlSelect( "id,sto_name AS name" );
	return mallStoreDao.selectMaps( wrapper );
    }

    @Override
    public Map< String,Object > findShopByStoreId( Integer id ) throws Exception {
	MallStore store = mallStoreDao.selectById( id );

	Map< String,Object > storeMap = JSONObject.parseObject( JSONObject.toJSONString( store ), Map.class );

	if ( CommonUtil.isNotEmpty( store.getWxShopId() ) ) {
	    //TODO  wxShopService.getShopById()
	    WsWxShopInfo shopInfo = null;
//	    wxShopService.getShopById( store.getWxShopId() );
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
		//todo 调用小屁孩接口   根据门店id查询门店图片  stoPicture
		storeMap.put( "stoPicture", null );

		//todo 调用陈丹接口，根据城市id查询城市名称
		storeMap.put( "stoAddress", shopInfo.getAddress() + shopInfo.getDetail() );
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
			} /*else {
			    // todo 调用小屁孩接口 添加中间表记录shopSubsopService.add
				shopSubsopService.add(sto.getWxShopId()	, sto.getId(), shopSubSopModel);
			}*/
		    } else {
			count = mallStoreDao.updateById( sto );
			if ( count <= 0 ) {
			    throw new Exception( "编辑店铺失败" );
			}
		    }

		    if ( CommonUtil.isEmpty( sto.getStoQrCode() ) ) {
			logger.info( "进入二维码生成！" );
			//todo 调用小屁孩接口  QRcodeKit.buildQRcode
			/*String qrUrl = "";
			String url1 = PropertiesUtil.getHomeUrl() + getGrantPath.replace( "$", "eatPhone/" + user.getId() + "/6" );
			qrUrl = QRcodeKit.buildQRcode(
					url1,
					PropertiesUtil.getResImagePath() + "/2/" + user.getName() + "/" + PropertiesUtil.IMAGE_FOLDER_TYPE_15,
					200,
					200 );
			sto.setStoQrCode( PropertiesUtil.getResourceUrl() + qrUrl.split( "upload/" )[1] );*/
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
	Map<String, Object> msg = new HashMap<String, Object>();
	try {
	    mallStoreDao.updateByIds(ids);//逻辑删除店铺
	    //todo 调用小屁孩接口 删除中间门店记录 shopSubsopService.updateBySubShop
	    /*if(ids != null && ids.length > 0){
		for (String string : ids) {
		    if(CommonUtil.isNotEmpty(string)){
			shopSubsopService.updateBySubShop(CommonUtil.toInteger(string),shopSubSopModel);
		    }
		}
	    }*/
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new  Exception("删除失败，系统异常！");
	} finally {
	    msg.put("result", result);
	    msg.put("message", message);
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
     *
     */
    @Override
    public List<Map<String, Object>> findAllStoByUser(BusUser user) {
	List<Map<String, Object>> ls = null;
	Integer userId = user.getId();
	//todo 调用陈丹接口  根据商家id  查询门店信息
	if (CommonUtil.isNotEmpty(user.getPid()) && user.getPid() > 0) {
	    // 如果子账户，先获取子账户拥有的分店
	    //			String sql = "SELECT a.id,a.sto_name FROM t_mall_store a LEFT JOIN bus_user_branch_relation b ON a.sto_branch_id=b.branchid WHERE a.is_delete=0  AND b.userid="
	    //					+ userId;
	    boolean isAdminFlag = isAdminUser(user.getId());
	    if(isAdminFlag){
//		String sql = "SELECT a.id,ws.business_name as sto_name FROM t_mall_store a LEFT JOIN bus_user_branch_relation b ON a.wx_shop_id=b.branchid left join t_wx_shop ws on ws.id =  a.wx_shop_id WHERE a.is_delete=0 and a.wx_shop_id>0 and ws.id>0  and ws.`status` != -1 AND b.userid="+userId;
//		ls = daoUtil.queryForList(sql);
	    }
	} else {
//	    String sql = "select s.id,ws.business_name as sto_name from t_mall_store s left join t_wx_shop ws on ws.id =  s.wx_shop_id where s.is_delete = 0 and s.wx_shop_id>0 and ws.id>0  and ws.`status` != -1 and s.sto_user_id ="+userId;
//	    ls = daoUtil.queryForList(sql);
	}
	return ls;
    }

}
