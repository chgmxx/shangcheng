package com.gt.mall.service.web.seckill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.seckill.MallSeckillDAO;
import com.gt.mall.dao.seckill.MallSeckillJoinDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.entity.seckill.MallSeckillJoin;
import com.gt.mall.entity.seckill.MallSeckillPrice;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.PhoneProductDetailResult;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.SocketService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.service.web.seckill.MallSeckillPriceService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 秒杀表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSeckillServiceImpl extends BaseServiceImpl< MallSeckillDAO,MallSeckill > implements MallSeckillService {

    private Logger logger = Logger.getLogger( MallSeckillServiceImpl.class );
    @Autowired
    private MallSeckillDAO mallSeckillDAO;

    @Autowired
    private MallSeckillPriceService mallSeckillPriceService;

    @Autowired
    private MallSeckillJoinDAO mallSeckillJoinDAO;

    @Autowired
    private MallProductInventoryService mallProductInventoryService;

    @Autowired
    private MallSearchKeywordService mallSearchKeywordService;

    @Autowired
    private MallStoreDAO mallStoreDAO;

    @Autowired
    private MallProductDAO mallProductDAO;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private MallStoreService mallStoreService;

    @Autowired
    private SocketService   socketService;
    @Autowired
    private WxShopService   wxShopService;
    @Autowired
    private MallPageService mallPageService;

    /**
     * 通过店铺id来查询秒杀
     */
    @Override
    public PageUtil selectSeckillByShopId( Map< String,Object > params, int userId, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallSeckillDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mSeckill/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断秒杀是否有数据
	    List< MallSeckill > seckillList = mallSeckillDAO.selectByPage( params );
	    if ( seckillList != null && seckillList.size() > 0 ) {
		for ( MallSeckill seckill : seckillList ) {
		    for ( Map< String,Object > shopMaps : shoplist ) {
			int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			if ( seckill.getShopId() == shop_id ) {
			    seckill.setShopName( shopMaps.get( "sto_name" ).toString() );
			    break;
			}
		    }
		}
	    }
	    page.setSubList( seckillList );
	}

	return page;
    }

    /**
     * 通过秒杀id查询秒杀信息
     */
    @Override
    public Map< String,Object > selectSeckillById( Integer id ) {
	Map< String,Object > map = mallSeckillDAO.selectBySeckillId( id );
	int SeckillId = CommonUtil.toInteger( map.get( "id" ) );
	List< MallSeckillPrice > priceList = mallSeckillPriceService.selectPriceByGroupId( SeckillId );
	map.put( "priceList", priceList );
	return map;
    }

    /**
     * 编辑秒杀
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int editSeckill( Map< String,Object > groupMap, BusUser busUser, HttpServletRequest request ) {
	int num = 0;
	boolean flag = false;
	int code = -1;
	int status = 0;
	boolean isAdd = false;
	if ( CommonUtil.isNotEmpty( groupMap.get( "seckill" ) ) ) {
	    MallSeckill seckill = JSONObject.parseObject( groupMap.get( "seckill" ).toString(), MallSeckill.class );
	    // 判断选择的商品是否已经存在未开始和进行中的秒杀中
	    List< MallSeckill > buyList = mallSeckillDAO.selectSeckillByProId( seckill );
	    if ( buyList == null || buyList.size() == 0 ) {
		seckill.setUserId( busUser.getId() );
		if ( CommonUtil.isNotEmpty( seckill.getId() ) ) {
		    List< Map< String,Object > > storeList = mallStoreService.findAllStoByUser( busUser, request );
		    // 判断本商品是否正在秒杀中
		    MallSeckill buy = mallSeckillDAO.selectSeckillByIds( seckill.getId(), storeList );
		    if ( buy.getStatus() == 1 && buy.getJoinId() > 0 ) {// 正在进行秒杀的商品不能修改
			code = -2;
			status = buy.getStatus();
		    } else {
			MallSeckill buyOld = mallSeckillDAO.selectById( seckill.getId() );
			if ( !buyOld.getProductId().equals( seckill.getProductId() ) ) {// 用户更换了商品
			    flag = true;
			}
			num = mallSeckillDAO.updateById( seckill );
		    }
		} else {
		    isAdd = true;
		    seckill.setCreateTime( new Date() );
		    num = mallSeckillDAO.insert( seckill );
		}
		if ( CommonUtil.isNotEmpty( seckill.getId() ) ) {
		    int userPId = busUserService.getMainBusId( busUser.getId() );//通过用户名查询主账号id
		    int isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有

		    String key = "hSeckill";
		    String field = seckill.getId().toString();
		    JedisUtil.map( key, field, seckill.getSNum() + "" );
		    List< Map< String,Object > > productList = new ArrayList<>();
		    if ( status != 1 ) {
			productList = mallSeckillPriceService.editSeckillPrice( groupMap, seckill.getId(), flag, isJxc );
		    }
		    if ( isAdd && isJxc == 1 ) {
			BusUser user = busUserService.selectById( busUser.getId() );
			MallStore store = mallStoreDAO.selectById( seckill.getShopId() );

			int uType = 1;//用户类型 1总账号  0子账号
			if ( !CommonUtil.toString( busUser.getId() ).equals( CommonUtil.toString( userPId ) ) ) {
			    uType = 0;
			}
			Map< String,Object > params = new HashMap<>();
			params.put( "uId", busUser.getId() );
			params.put( "uType", uType );
			params.put( "uName", user.getName() );
			params.put( "rootUid", userPId );
			params.put( "remark", "商品加入秒杀出库" );
			params.put( "shopId", store.getWxShopId() );
			params.put( "type", "0" );

			if ( productList == null || productList.size() == 0 ) {
			    MallProduct product = mallProductDAO.selectById( seckill.getProductId() );

			    if ( CommonUtil.isNotEmpty( product.getErpInvId() ) && product.getErpInvId() > 0 ) {
				Map< String,Object > productMap = new HashMap<>();
				productMap.put( "id", product.getErpInvId() );
				productMap.put( "amount", seckill.getSNum() );//数量
				productMap.put( "price", seckill.getSPrice() );
				productList.add( productMap );
			    }
			}
			if ( productList != null && productList.size() > 0 ) {
			    params.put( "products", productList );
			    List< Map< String,Object > > erpList = new ArrayList<>();
			    erpList.add( params );

			    Map< String,Object > erpMaps = new HashMap<>();
			    erpMaps.put( "orders", JSONArray.toJSONString( erpList ) );
			    MallJxcHttpClientUtil.inventoryOperation( erpMaps, true );
			}
		    }
		}
	    } else {
		code = 0;
	    }
	    if ( num > 0 ) {
		code = 1;
	    }
	}
	return code;
    }

    /**
     * 删除秒杀
     */
    @Override
    public boolean deleteSeckill( MallSeckill Seckill ) {
	int num = mallSeckillDAO.updateById( Seckill );
	return num > 0;
    }

    /**
     * 根据店铺id查询商品，并分析商品是否加入秒杀
     */
    @Override
    public PageUtil selectProByGroup( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallSeckillDAO.selectCountProBySeckill( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mSeckill/getProductByGroup.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    List< Map< String,Object > > productList = mallSeckillDAO.selectProBySeckill( params );
	    page.setSubList( productList );
	}

	return page;
    }

    /**
     * 获取店铺下所有的秒杀
     */
    @Override
    public List< Map< String,Object > > getSeckillAll( Member member, Map< String,Object > maps ) {

	int shopid = 0;
	if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
	    shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
	}
	//新增搜索关键词
	mallSearchKeywordService.insertSeachKeyWord( member.getId(), shopid, maps.get( "proName" ) );

	List< Map< String,Object > > list = new ArrayList<>();// 存放店铺下的商品

	/*double discount = 1;// 商品折扣
	if(CommonUtil.isNotEmpty(member)){
		Map<String, Object> map = memberpayService.findCardType(member.getId());
		String result = map.get("result").toString();
		if (result == "true" || result.equals("true")) {
			discount = Double.parseDouble(map.get("discount").toString());
		}
	}*/
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = mallSeckillDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica.equals( "1" ) ) {
		    map2.put( "old_price", map2.get( "inv_price" ) );
		    if ( CommonUtil.toString( map2.get( "specifica_img_id" ) ).equals( "0" ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "old_price", map2.get( "pro_price" ) );
		    map2.put( "image_url", map2.get( "image_url" ) );
		}
		String is_member_discount = map2.get( "is_member_discount" ).toString();// 商品是否参加折扣,1参加折扣
		if ( is_member_discount.equals( "1" ) ) {
		    /*map2.put("price", Math.ceil((Double.parseDouble(map2.get(
				    "price").toString()) * discount) * 100) / 100);*/
		    map2.put( "price", Math.ceil( ( Double.parseDouble( map2.get( "price" ).toString() ) ) * 100 ) / 100 );
		}
		Date endTimes = DateTimeKit.parse( map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTimes = DateTimeKit.parse( map2.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
		Date date = new Date();
		map2.put( "times", ( endTimes.getTime() - nowTime.getTime() ) / 1000 );
		int isEnd = 1;
		if ( startTimes.getTime() < date.getTime() && endTimes.getTime() <= date.getTime() ) {
		    isEnd = -1;
		}
		map2.put( "isEnd", isEnd );
		int status = -1;

		if ( startTimes.getTime() > date.getTime() && endTimes.getTime() > date.getTime() ) {
		    status = 0;
		    map2.put( "startTimes", ( startTimes.getTime() - nowTime.getTime() ) / 1000 );
		} else if ( startTimes.getTime() <= date.getTime() && date.getTime() < endTimes.getTime() ) {
		    status = 1;
		}
		map2.put( "status", status );
		list.add( map2 );
	    }
	}
	return list;
    }

    /**
     * 根据商品id查询秒杀信息和秒杀价格
     */
    @Override
    public MallSeckill getSeckillByProId( Integer proId, Integer shopId, int activityId ) {
	MallSeckill seckill = new MallSeckill();
	seckill.setProductId( proId );
	seckill.setShopId( shopId );
	seckill.setId( activityId );
	seckill = mallSeckillDAO.selectBuyByProductId( seckill );
	if ( seckill != null && CommonUtil.isNotEmpty( seckill.getId() ) ) {

	    String key = "hSeckill";
	    String field = seckill.getId().toString();
	    if ( JedisUtil.hExists( key, field ) ) {
		String str = JedisUtil.maoget( key, field );
		if ( CommonUtil.isNotEmpty( str ) ) {
		    int invNum = CommonUtil.toInteger( str );
		    if ( invNum > 0 ) seckill.setSNum( invNum );
		}
	    }

	    Date endTime = DateTimeKit.parse( seckill.getSEndTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date startTime = DateTimeKit.parse( seckill.getSStartTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
	    seckill.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    if ( seckill.getStatus() == null || seckill.getStatus() == 0 ) {
		seckill.setStartTimes( ( startTime.getTime() - nowTime.getTime() ) / 1000 );
	    }

	    List< MallSeckillPrice > priceList = mallSeckillPriceService.selectPriceByGroupId( seckill.getId() );
	    List< MallSeckillPrice > list = new ArrayList<>();
	    if ( priceList != null && priceList.size() > 0 ) {
		for ( MallSeckillPrice price : priceList ) {
		    field = seckill.getId() + "_" + price.getSpecificaIds();
		    if ( CommonUtil.isNotEmpty( price.getSpecificaIds() ) && JedisUtil.hExists( key, field ) ) {
			int invNum = CommonUtil.toInteger( JedisUtil.maoget( key, field ) );
			if ( invNum > 0 ) price.setSeckillNum( invNum );
		    }
		    list.add( price );
		}
	    }
	    seckill.setPriceList( list );
	    return seckill;
	}
	return null;
    }

    @Override
    public PhoneProductDetailResult getSeckillProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result ) {
	if ( activityId == 0 ) {
	    return result;
	}
	//通过商品id查询秒杀信息
	MallSeckill seckill = getSeckillByProId( proId, shopId, activityId );
	if ( seckill == null ) {
	    return result;
	}
	result.setActivityId( seckill.getId() );
	if ( CommonUtil.isNotEmpty( seckill.getSMaxBuyNum() ) && seckill.getSMaxBuyNum() > 0 ) {
	    result.setMaxBuyNum( seckill.getSMaxBuyNum() );//限购
	}
	result.setProductStockTotal( seckill.getSNum() );//库存
	double seckillPrice = CommonUtil.toDouble( seckill.getSPrice() );
	List< Integer > invIdList = new ArrayList<>();
	if ( CommonUtil.isNotEmpty( seckill.getPriceList() ) ) {
	    for ( MallSeckillPrice price : seckill.getPriceList() ) {
		if ( price.getIsJoinGroup() == 1 ) {
		    if ( result.getInvId() == 0 ) {
			seckillPrice = CommonUtil.toDouble( price.getSeckillPrice() );
			result.setInvId( price.getInvenId() );
		    }
		    if ( result.getInvId() > 0 ) {
			invIdList.add( price.getInvenId() );
		    }
		}
	    }
	}
	result.setInvIdList( invIdList );
	result.setProductPrice( seckillPrice );
	return result;
    }

    /**
     * 查询用户参加秒杀的数量
     */
    @Override
    public int selectCountByBuyId( Map< String,Object > params ) {
	return mallSeckillJoinDAO.selectCountByBuyId( params );
    }

    /**
     * 判断秒杀的库存是否能够秒杀
     */
    @Override
    public Map< String,Object > isInvNum( MallOrder mallOrder, MallOrderDetail mallOrderDetail ) {
	Map< String,Object > result = new HashMap<>();
	String seckillId = mallOrder.getGroupBuyId().toString();
	String invKey = "hSeckill";//秒杀库存的key
	String specificas = "";
	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( mallOrderDetail.getProductSpecificas() ) ) {
	    specificas = mallOrderDetail.getProductSpecificas();
	}
	//查询秒杀商品的库存
	Integer invNum;
	if ( !specificas.equals( "" ) ) {
	    //有规格，取规格的库存
	    invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, seckillId + "_" + specificas ) );
	} else {
	    //无规格，则取商品库存
	    invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, seckillId ) );
	}
	int proNum = mallOrderDetail.getDetProNum();//购买商品的用户
	//判断库存是否够
	if ( invNum >= proNum && invNum > 0 ) {
	    result.put( "result", true );
	} else {
	    result.put( "msg", "库存不够" );
	    result.put( "result", false );
	}
	return result;
    }

    /**
     * 生成订单成功，减商品的库存（在redis中）
     *
     * @param mallOrderDetail 订单详情
     * @param memberId        下单用户id
     * @param orderId         订单id
     */
    public void invNum( MallOrder mallOrder, MallOrderDetail mallOrderDetail, String memberId, String orderId ) {
	String seckillId = mallOrder.getGroupBuyId().toString();
	String proId = mallOrderDetail.getProductId().toString();
	String key = "hSeckill_nopay";//秒杀用户(用于没有支付，恢复库存用)
	String invKey = "hSeckill";//秒杀库存的key
	String specificas = "";
	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( mallOrderDetail.getProductSpecificas() ) ) {
	    specificas = mallOrderDetail.getProductSpecificas();
	}
	//查询秒杀商品的库存
	Integer invNum;
	if ( !specificas.equals( "" ) ) {
	    String field = seckillId + "_" + specificas;
	    //有规格，取规格的库存
	    invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, field ) );
	    JedisUtil.map( invKey, field, ( invNum - 1 ) + "" );
	}
	invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, seckillId ) );
	JedisUtil.map( invKey, seckillId, ( invNum - 1 ) + "" );

	//把已生成的订单并未支付 丢到redis队列里
	String times = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	JSONObject obj = new JSONObject();
	obj.put( "seckillId", seckillId );
	obj.put( "proId", proId );
	obj.put( "orderId", orderId );
	obj.put( "memberId", memberId );
	obj.put( "proSpec", mallOrderDetail.getProductSpecificas() );
	obj.put( "times", times );
	obj.put( "orderTypes", 3 );
	JedisUtil.map( key, orderId, obj.toString() );
    }

    /**
     * 把要修改的库存丢到redis里
     */
    @Override
    public void addInvNumRedis( MallOrder order, List< MallOrderDetail > detailList ) {
	if ( detailList != null && detailList.size() > 0 ) {
	    for ( MallOrderDetail detail : detailList ) {
		int orderId = order.getId();
		JSONObject obj = new JSONObject();
		obj.put( "orderId", orderId );
		obj.put( "detailId", detail.getId() );
		obj.put( "proSpecificas", detail.getProductSpecificas() );
		obj.put( "productId", detail.getProductId() );
		obj.put( "productNum", detail.getDetProNum() );
		obj.put( "memberId", order.getBuyerUserId() );
		obj.put( "db", PropertiesUtil.getDbname() );
		obj.put( "model", "2" );

		//把订单移除未支付的队列里
		String payKey = "hSeckill_nopay";//秒杀用户(用于没有支付，恢复库存用)
		String field = orderId + "";
		JedisUtil.mapdel( payKey, field );

		//添加参加秒杀信息
		MallSeckillJoin join = new MallSeckillJoin();
		join.setOrderId( orderId );
		join.setOrderDetailId( detail.getId() );
		join.setProductId( detail.getProductId() );
		join.setSeckillId( order.getGroupBuyId() );
		join.setSeckillPrice( order.getOrderMoney() );
		join.setSeckillTime( new Date() );
		join.setSeckillUserId( order.getBuyerUserId() );
		mallSeckillJoinDAO.insert( join );

		try {
		    logger.info( "mq参数：" + obj );
		    socketService.mqSendMessage( obj.toString() );
		} catch ( Exception e ) {
		    e.printStackTrace();
		}

	    }
	}
    }

    @Override
    public List< Map< String,Object > > selectgbSeckillByShopId( Map< String,Object > maps ) {
	return mallSeckillDAO.selectgbProductByShopId( maps );
    }

    @Override
    public void loadSeckill() {

	String key = "hSeckill";
	/*List<MallSeckill> seckillList = mallSeckillDao.selectByPage(null);
	if(seckillList != null && seckillList.size() > 0){
		for (MallSeckill mallSeckill : seckillList) {
			String field = mallSeckill.getId().toString();

			JedisUtil.map(key, field, mallSeckill.getsNum()+"");

			List<MallSeckillPrice> priceList = mallSeckillPriceService.selectPriceByGroupId(mallSeckill.getId());
			if(priceList != null && priceList.size() > 0){
				for (MallSeckillPrice mallSeckillPrice : priceList) {
					field = mallSeckill.getId()+"_"+mallSeckillPrice.getSpecificaIds();
					JedisUtil.map(key, field, mallSeckillPrice.getSeckillNum()+"");
				}
			}
		}
	}*/

	Map< String,Object > maps = new HashMap<>();
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = mallSeckillDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String proId = map2.get( "id" ).toString();
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		String stockNum = map2.get( "stockTotal" ).toString();//商品总库存

		String seckillId = map2.get( "gBuyId" ).toString();
		JedisUtil.map( key, seckillId, stockNum );

		if ( is_specifica.equals( "1" ) ) {
		    //规格库存
		    List< MallProductInventory > invenList = mallProductInventoryService.selectInvenByProductId( Integer.parseInt( proId ) );
		    if ( invenList != null && invenList.size() > 0 ) {
			for ( MallProductInventory inven : invenList ) {
			    StringBuilder specIds = new StringBuilder();
			    for ( MallProductSpecifica spec : inven.getSpecList() ) {
				if ( !specIds.toString().equals( "" ) ) {
				    specIds.append( "," );
				}
				specIds.append( spec.getSpecificaValueId() );
			    }
			    String field = seckillId + "_" + specIds;

			    JedisUtil.map( key, field, inven.getInvNum().toString() );
			}
		    }
		}
	    }
	}

    }

    @Override
    public MallSeckill selectSeckillBySeckillId( int id ) {
	return mallSeckillDAO.selectById( id );
    }

    /**
     * 获取店铺下所有的秒杀
     */
    @Override
    public PageUtil searchSeckillAll( PhoneSearchProductDTO searchProductDTO, Member member ) {

	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( searchProductDTO.getSearchContent() ) ) {
	    //新增搜索关键词
	    mallSearchKeywordService.insertSeachKeyWord( member.getId(), searchProductDTO.getShopId(), searchProductDTO.getSearchContent() );
	}

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
	int rowCount = mallSeckillDAO.selectCountGoingSeckillProduct( searchProductDTO );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	searchProductDTO.setMaxNum( pageSize );

	List< Map< String,Object > > productList = mallSeckillDAO.selectGoingSeckillProduct( searchProductDTO );

	page.setSubList( mallPageService.getSearchProductParam( productList, 1, searchProductDTO ) );
	return page;
    }

}
