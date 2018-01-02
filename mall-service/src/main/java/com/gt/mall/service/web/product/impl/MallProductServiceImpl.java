package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.auction.MallAuctionDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.pifa.MallPifaPriceDAO;
import com.gt.mall.dao.presale.MallPresaleDAO;
import com.gt.mall.dao.product.*;
import com.gt.mall.dao.seckill.MallSeckillDAO;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.product.*;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.product.ProductResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.*;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.fenbiFlow.BusFlow;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import com.gt.util.entity.param.fenbiFlow.FenbiFlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductServiceImpl extends BaseServiceImpl< MallProductDAO,MallProduct > implements MallProductService {

    private static Logger logger = LoggerFactory.getLogger( MallProductServiceImpl.class );

    @Autowired
    private MallProductDAO mallProductDAO;//商品dao

    @Autowired
    private MallProductInventoryDAO mallProductInventoryDAO;//商品库存dao

    @Autowired
    private MallProductInventoryService mallProductInventoryService;//商品库存业务处理类

    @Autowired
    private MallGroupBuyDAO mallGroupBuyDAO;//团购Dao

    @Autowired
    private MallSeckillDAO mallSeckillDao;//秒杀处理类

    @Autowired
    private MallProductGroupDAO mallProductGroupDAO;//商品分组dao

    @Autowired
    private MallProductDetailService mallProductDetailService;//商品详情业务处理

    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;//图片业务处理类

    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;//商品规格业务处理类

    @Autowired
    private MallProductParamService mallProductParamService;//商品参数业务处理类

    @Autowired
    private MallProductGroupService mallProductGroupService;

    @Autowired
    private MallGroupService mallGroupService;//分组业务处理类

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDao;//订单详情Dao

    @Autowired
    private MallAuctionDAO mallAuctionDAO;//拍卖dao

    @Autowired
    private MallPresaleDAO mallPresaleDAO;//预售dao

    @Autowired
    private MallPifaDAO mallPifaDAO;//批发dao

    @Autowired
    private MallProductSpecificaDAO mallProductSpecificaDao;//商品规格dao

    @Autowired
    private MallPaySetService mallPaySetService;//商城设置业务处理

    @Autowired
    private MallPifaApplyService mallPifaApplyService;//批发申请业务处理

    @Autowired
    private MallPifaService mallPifaService;//批发业务处理

    @Autowired
    private MallPifaPriceDAO mallPifaPriceDao;//批发价格业务处理

    @Autowired
    private MallStoreService mallStoreService;//商城店铺业务处理

    @Autowired
    private MallShopCartDAO mallShopCartDao;//购物车dao

    @Autowired
    private MallOrderDAO mallOrderDAO;//订单dao

    @Autowired
    private MemberService memberService;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private FenBiFlowService fenBiFlowService;

    @Override
    public Map< String,Object > countStatus( Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	params.remove( "isPublish" );
	params.remove( "isPublishs" );
	params.remove( "checkStatus" );
	//全部商品
	Integer status_total = mallProductDAO.selectCountByUserId( params );
	//已上架商品  （审核成功，上架）
	params.put( "isPublish", "1" );
	params.put( "checkStatus", "1" );
	Integer status1 = mallProductDAO.selectCountByUserId( params );
	//未上架商品
	params.remove( "checkStatus" );
	params.remove( "isPublish" );
	params.put( "isPublishs", "0,-1".split( "," ) );
	Integer status2 = mallProductDAO.selectCountByUserId( params );
	//审核不通过
	params.remove( "isPublish" );
	params.remove( "isPublishs" );
	params.put( "checkStatus", "-1" );
	Integer status3 = mallProductDAO.selectCountByUserId( params );

	result.put( "status_total", status_total );
	result.put( "status1", status1 );
	result.put( "status2", status2 );
	result.put( "status3", status3 );
	return result;
    }

    @Override
    public PageUtil selectByUserId( Map< String,Object > param, List< Map< String,Object > > shoplist ) {
	List< Map< String,Object > > productList = null;
	int pageSize = 20;
	int count = 0;

	int type = 0;
	if ( CommonUtil.isNotEmpty( param.get( "type" ) ) ) {
	    type = CommonUtil.toInteger( param.get( "type" ) );
	}
	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	param.put( "curPage", curPage );
	int isJxc = CommonUtil.toInteger( param.get( "isJxc" ) );

	// 统计售罄商品
	if ( type == 1 ) {
	    count = mallProductInventoryDAO.selectCountByUserId( param );
	} else {// 统计出售商品，库存商品
	    count = mallProductDAO.selectCountByUserId( param );
	}
	PageUtil page = new PageUtil( curPage, pageSize, count, "mPro/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    if ( type == 1 ) {
		productList = mallProductInventoryDAO.selectByUserId( param );
	    } else {
		productList = mallProductDAO.selectByUserId( param );// 查询商品总数
	    }
	}

	String shopIds = "";
	String proIds = "";
	if ( productList != null && productList.size() > 0 ) {
	    List< Integer > proIdsList = new ArrayList<>();
	    List< Integer > proIdsInvList = new ArrayList<>();
	    for ( Map< String,Object > map : productList ) {
		int id = CommonUtil.toInteger( map.get( "id" ) );
		proIdsList.add( id );
		if ( CommonUtil.isNotEmpty( map.get( "isSpecifica" ) ) ) {
		    if ( map.get( "isSpecifica" ).toString().equals( "1" ) ) {
			proIdsInvList.add( id );
		    }
		}
	    }
	    //查看商品是否加入到团购
	    List< Map< String,Object > > groupBuyList = mallGroupBuyDAO.selectCountByProList( proIdsList );
	    List< Map< String,Object > > seckillList = mallSeckillDao.selectCountByProList( proIdsList );
	    List< MallProductInventory > invenList = mallProductInventoryService.selectByIdListDefault( proIdsInvList );
	    logger.info( "团购商品数据 = " + JSONObject.toJSONString( groupBuyList ) );
	    logger.info( "秒杀商品数据 = " + JSONObject.toJSONString( seckillList ) );
	    for ( Map< String,Object > map : productList ) {
		int isGroup = 0;
		int isSeckill = 0;
		int id = CommonUtil.toInteger( map.get( "id" ) );
		Map< String,Object > params = new HashMap<>();
		params.put( "productId", map.get( "id" ) );
		params.put( "status", "1" );
		params.put( "isPublic", "1" );
		int shopId = CommonUtil.toInteger( map.get( "shopId" ) );

		if ( shoplist != null && shoplist.size() > 0 ) {
		    for ( Map< String,Object > shopMaps : shoplist ) {
			int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			if ( shopId == shop_id ) {
			    map.put( "shopName", shopMaps.get( "sto_name" ) );
			}
		    }
		}
		map.put( "isGroup", isAddActivityByProId( groupBuyList, id ) );
		//查看商品是否加入到秒杀
		map.put( "isSeckill", isAddActivityByProId( seckillList, id ) );

		if ( CommonUtil.isNotEmpty( map.get( "isSpecifica" ) ) && invenList != null ) {
		    if ( map.get( "isSpecifica" ).toString().equals( "1" ) && invenList.size() > 0 ) {
			for ( MallProductInventory inventory : invenList ) {
			    if ( CommonUtil.toInteger( inventory.getProductId() ) == id ) {
				map.put( "proPrice", inventory.getInvPrice() );
				break;
			    }
			}
		    }
		}

		int viewNum = getViews( map.get( "id" ).toString() );
		map.put( "viewsNum", viewNum );

		if ( CommonUtil.isNotEmpty( map.get( "erpProId" ) ) && isJxc == 1 ) {
		    int erpProId = CommonUtil.toInteger( map.get( "erpProId" ) );
		    if ( erpProId > 0 ) {
			boolean isPro = true;
			String shopid = map.get( "wxShopId" ).toString();
			if ( CommonUtil.isNotEmpty( proIds ) ) {
			    if ( proIds.contains( erpProId + "," ) ) {
				isPro = false;
			    }
			}
			if ( isPro ) {
			    proIds += erpProId + ",";
			}
			boolean isShop = true;
			if ( CommonUtil.isNotEmpty( shopIds ) ) {
			    if ( shopIds.contains( shopid + "," ) ) {
				isShop = false;
			    }
			}
			if ( isShop ) {
			    shopIds += shopid + ",";
			}
		    }
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( proIds ) && CommonUtil.isNotEmpty( shopIds ) ) {
	    proIds = proIds.substring( 0, proIds.length() - 1 );
	    shopIds = shopIds.substring( 0, shopIds.length() - 1 );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopIds", shopIds );
	    params.put( "proIds", proIds );
	    com.alibaba.fastjson.JSONArray erpProArr = MallJxcHttpClientUtil.inventoryByProduct( params, true );
	    List< Map< String,Object > > newProductList = new ArrayList<>();
	    if ( erpProArr != null && erpProArr.size() > 0 ) {
		if ( productList != null && productList.size() > 0 ) {
		    for ( Map< String,Object > map : productList ) {//循环商品
			if ( CommonUtil.isNotEmpty( map.get( "erpProId" ) ) ) {
			    int erpProId = CommonUtil.toInteger( map.get( "erpProId" ) );
			    if ( erpProId > 0 ) {
				for ( Object object : erpProArr ) {//循环
				    JSONArray proArr = JSONArray.parseArray( object.toString() );
				    if ( proArr.size() > 0 ) {
					String erpShopId = proArr.get( 0 ).toString();
					String erpProIds = proArr.get( 1 ).toString();
					if ( erpProIds.equals( CommonUtil.toString( erpProId ) ) && erpShopId.equals( map.get( "wxShopId" ).toString() ) ) {
					    logger.info( "库存：" + proArr.get( 2 ) + "，商品:" + map.get( "id" ) );
					    map.put( "stockTotal", proArr.get( 2 ) );
					    break;
					}
				    }
				}
			    }
			}
			newProductList.add( map );
		    }
		    if ( newProductList.size() > 0 ) {
			productList = new ArrayList<>();
			productList.addAll( newProductList );
		    }
		}
	    }
	}
	EntityDtoConverter converter = new EntityDtoConverter();
	List< ProductResult > productResultList = new ArrayList<>();
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > product : productList ) {
		ProductResult productResult = new ProductResult();
		converter.mapToBean( product, productResult );
		productResultList.add( productResult );
	    }
	}
	page.setSubList( productResultList );
	return page;
    }

    private int isAddActivityByProId( List< Map< String,Object > > activityList, int proId ) {
	int isActivity = 0;
	if ( activityList != null && activityList.size() > 0 ) {
	    for ( int i = 0; i < activityList.size(); i++ ) {
		Map< String,Object > groupMap = activityList.get( i );
		int product_id = CommonUtil.toInteger( groupMap.get( "product_id" ) );
		if ( product_id == proId ) {
		    isActivity = 1;
		    activityList.remove( i );
		    break;
		}
	    }
	}
	return isActivity;
    }

    private int getViews( String proId ) {
	String key = Constants.REDIS_KEY + "proViewNum";
	int viewNum = 0;
	String viewNums = "";
	String field = proId;
	if ( JedisUtil.hExists( key, field ) ) {
	    viewNums = JedisUtil.maoget( key, field );
	}
	if ( viewNums != null && !viewNums.equals( "" ) ) {
	    viewNum = CommonUtil.toInteger( viewNums );
	}
	return viewNum;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean batchUpdateProduct( Map< String,Object > params, String[] ids ) {
	boolean flag = true;
	int num = 0;
	// 判断分组id是否为空
	if ( ids.length > 0 ) {
	    if ( CommonUtil.isNotEmpty( params.get( "viewsNum" ) ) ) {
		String key = Constants.REDIS_KEY + "proViewNum";
		for ( String id : ids ) {
		    JedisUtil.map( key, id, params.get( "viewsNum" ).toString() );
		}
		num = 1;
	    }
	    params.put( "ids", ids );
	    num = mallProductDAO.batchUpdateProduct( params );
	}
	if ( num <= 0 ) {
	    flag = false;
	} else if ( params.containsKey( "isDelete" ) ) {
	    if ( ids != null && ids.length > 0 ) {
		for ( String id : ids ) {
		    if ( CommonUtil.isNotEmpty( id ) ) {
			MallProduct product = mallProductDAO.selectById( CommonUtil.toInteger( id ) );
			if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) ) {
			    if ( product.getFlowRecordId() > 0 ) {
				fenBiFlowService.rollbackFenbiFlowRecord( product.getFlowRecordId() );
			    }
			}
		    }
		}
	    }
	}
	return flag;
    }

    @Override
    public Map< String,Object > selectProductById( Integer id, BusUser user, long isJxc ) throws Exception {
	Map< String,Object > map = new HashMap<>();
	// 查询商品基本信息
	MallProduct product = mallProductDAO.selectById( id );// 根据商品id查询商品的基本信息
	if ( product != null ) {
	    int viewNum = getViews( product.getId().toString() );
	    product.setViewsNum( viewNum );
	    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getFlowId() > 0 ) {
		    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
		    map.put( "busFlow", flow );

		    map.put( "noChangeFlow", 1 );
		}
	    }
	    if ( isJxc == 1 ) {
		if ( product.getProTypeId().toString().equals( "0" ) ) {
		    map.put( "noShowSt", 0 );//显示实体物品
		    map.put( "noUpInvNum", 1 );//有进销存，不能修改商品库存
		    map.put( "noUpSpec", 1 );
		} else {
		    map.put( "noShowSt", 1 );//不显示实体物品
		}
	    }
	    // 查询商品详情
	    MallProductDetail detail = mallProductDetailService.selectByProductId( product.getId() );
	    // 查询商品分组
			/*List<MallProductGroup> groupList = mallProductGroupDao
					.selectByProductId(product.getId());*/
	    Map< String,Object > params = new HashMap<>();
	    params.put( "proId", product.getId() );
	    List< Map< String,Object > > groupList = mallProductGroupDAO.selectgroupsByProductId( params );

	    params.put( "assId", product.getId() );
	    params.put( "assType", 1 );
	    // 查询商品图片
	    List< MallImageAssociative > imageList = mallImageAssociativeService.getParamByProductId( params );
	    // 查询商品规格
	    List< Map< String,Object > > specificaList = mallProductSpecificaService.getSpecificaByProductId( product.getId() );
	    // 查询商品库存
	    List< MallProductInventory > invenList = mallProductInventoryService.getInventByProductId( product.getId() );
	    if ( isJxc == 1 && CommonUtil.isNotEmpty( product.getErpProId() ) ) {//拥有进销存直接查寻进销存的库存
		List< Map< String,Object > > specList = getErpInvByProId( product.getErpProId(), product.getShopId() );
		int stockNum = 0;
		if ( invenList != null && invenList.size() > 0 && specList != null && specList.size() > 0 ) {
		    //获取erp的商品库存
		    for ( MallProductInventory inven : invenList ) {
			if ( CommonUtil.isNotEmpty( inven.getErpInvId() ) ) {
			    String invIds = inven.getErpInvId().toString();
			    int invNum = getInvNumsBySpecs( specList, invIds );
			    stockNum += invNum;
			    inven.setInvNum( invNum );
			}
		    }
		} else {
		    if ( specList != null && specList.size() > 0 ) {
			int invNum = getInvNumsBySpecs( specList, product.getErpInvId().toString() );
			product.setProStockTotal( invNum );
		    }
		}
		if ( stockNum > 0 && stockNum != product.getProStockTotal() ) {
		    product.setProStockTotal( stockNum );
		}
	    }
	    //查询商品参数
	    List< MallProductParam > paramList = mallProductParamService.getParamByProductId( product.getId() );

	    if ( CommonUtil.isInteger( product.getShopId().toString() ) ) {
		/*Map< String,Object > param = new HashMap<>();
		param.put( "shopId", product.getShopId() );*/
		/*List< MallGroup > defaultGroupList = mallGroupService.selectGroupByShopId( product.getShopId(), 0 );
		map.put( "groupList", defaultGroupList );*/
	    }
	    MallGroupBuy groupBuy = new MallGroupBuy();
	    groupBuy.setProductId( product.getId() );
	    groupBuy.setStatus( 1 );
	    //查询商品是否存在于团购中
	    List< MallGroupBuy > buyList = mallGroupBuyDAO.selectGroupByProId( groupBuy );
	    int isDelSpec = 1;//允许修改商品规格
	    int isType = 0;
	    if ( buyList != null && buyList.size() > 0 ) {//商品存在于团购中不允许修改商品规格
		isDelSpec = 0;
		isType = 1;
	    }
	    MallSeckill buy = new MallSeckill();
	    buy.setProductId( product.getId() );
	    buy.setStatus( 1 );
	    //商品存在于秒杀中不允许修改价格
	    List< MallSeckill > seckillList = mallSeckillDao.selectSeckillByProId( buy );
	    if ( seckillList != null && seckillList.size() > 0 ) {
		isDelSpec = 0;
		isType = 2;
	    }
	    //	    String sql = "select count(d.id) from t_mall_order_detail d left join t_mall_order o on d.order_id=o.id where o.order_status!=5 and d.product_id="+product.getId();
	    //	    int len = daoUtil.queryForInt(sql);
	    Map< String,Object > detailParams = new HashMap<>();
	    detailParams.put( "productId", product.getId() );
	    detailParams.put( "noOrderStatus", 5 );
	    int len = mallOrderDetailDao.countDetailByProductId( detailParams );
	    if ( len > 0 ) {
		map.put( "isOrder", 1 );
	    }
	    MallAuction auction = new MallAuction();
	    auction.setProductId( product.getId() );
	    auction.setStatus( 1 );
	    List< MallAuction > auctionList = mallAuctionDAO.selectAuctionByProId( auction );
	    if ( auctionList != null && auctionList.size() > 0 ) {
		isDelSpec = 0;
		isType = 3;
	    }

	    MallPresale presale = new MallPresale();
	    presale.setProductId( product.getId() );
	    //预售
	    List< MallPresale > preList = mallPresaleDAO.selectDepositByProId( presale );

	    if ( preList != null && preList.size() > 0 ) {
		MallPresale pre = preList.get( 0 );
		//		String sqls = "select count(d.id) from t_mall_order_detail d left join t_mall_order o on d.order_id=o.id where o.order_status!=5 and o.order_type = 6 and o.group_buy_id ="+pre.getId()+" and d.product_id="+product.getId();
		//		int lens = daoUtil.queryForInt(sqls);
		detailParams = new HashMap<>();
		detailParams.put( "productId", product.getId() );
		detailParams.put( "noOrderStatus", 5 );
		detailParams.put( "orderType", 6 );
		detailParams.put( "groupBuyId", pre.getId() );
		int lens = mallOrderDetailDao.countDetailByProductId( detailParams );
		if ( lens > 0 ) {
		    isDelSpec = 0;
		    isType = 4;
		}
	    }
	    //批发
	    MallPifa pifa = new MallPifa();
	    pifa.setProductId( product.getId() );
	    pifa.setStatus( 1 );
	    //商品存在于批发不允许修改价格
	    List< MallPifa > pifaList = mallPifaDAO.selectPifaByProId( pifa );
	    if ( pifaList != null && pifaList.size() > 0 ) {
		isDelSpec = 0;
		isType = 5;
	    }

	    map.put( "isType", isType );

	    map.put( "pro", product );
	    map.put( "proDetail", detail );
	    map.put( "proGroupList", groupList );
	    map.put( "imageList", imageList );
	    map.put( "specList", specificaList );
	    map.put( "invenList", invenList );
	    map.put( "isDelSpec", isDelSpec );
	    map.put( "paramList", paramList );
	}
		/*List<JSONObject> cardReceiveMapList = new ArrayList<JSONObject>();
		if(cardReceiveList != null && cardReceiveList.size() > 0){
			//request.setAttribute("cardReceiveList", cardReceiveList);
			for (DuofenCardReceive duofenCardReceive : cardReceiveList) {
				if(CommonUtil.isNotEmpty(duofenCardReceive.getCardtype())){
					if(duofenCardReceive.getCardtype().toString().equals("1")){//礼券包

						double money = 0;

						String[] strId = duofenCardReceive.getCardids().split(",");
						List<Integer> ids = new ArrayList<Integer>();
						for (String str : strId) {
							if (CommonUtil.isNotEmpty(str)) {
								ids.add(CommonUtil.toInteger(str));
							}
						}
						//查询卡券包的卡券
						List<DuofenCard> duofencards = duofenCardDao.findInCardIds(ids);
						if(duofencards != null && duofencards.size() > 0){
							for (DuofenCard duofenCard : duofencards) {
								if(duofenCard.getCardType().toString().equals("4")){
									if(CommonUtil.isNotEmpty(duofenCard.getDatetimeset())){
										List<Map<String, Object>> timeList =(List<Map<String,Object>>)JSONArray.fromObject(duofenCard.getDatetimeset());
										if(timeList != null && timeList.size() > 0){
											for (Map<String, Object> map2 : timeList) {
												if(CommonUtil.isNotEmpty(map2.get("money"))){
													double cardMoney = CommonUtil.toDouble(map2.get("money"));
													if(money > 0 && money < cardMoney){
														money = cardMoney;
													}
												}
											}
										}
									}
									break;
								}
							}
						}
						if(money > 0){
							duofenCardReceive.setBuymoney(money);
						}

					}
				}
				JSONObject obj = JSONObject.fromObject(duofenCardReceive);
				if(CommonUtil.isNotEmpty(duofenCardReceive.getCardmessage())){
					List<Map<String, Object>> messageList = (List<Map<String,Object>>)JSONArray.fromObject(duofenCardReceive.getCardmessage());
					obj.put("messageList", messageList);
				}
				cardReceiveMapList.add(obj);
			}
			map.put("cardReceiveLists", cardReceiveMapList);
		}*/
	return map;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > addProduct( Map< String,Object > params, BusUser user, HttpServletRequest request ) throws Exception {
	int proId = 0;// 商品id
	MallProduct product = null;
	Map< String,Object > resultMap = new HashMap<>();
	// 编辑商品基本信息
	if ( !CommonUtil.isEmpty( params.get( "product" ) ) ) {
	    // System.out.println(params.get("product"));
	    product = JSONObject.parseObject( params.get( "product" ).toString(), MallProduct.class );
	    product.setUserId( user.getId() );
	    product.setCreateTime( new Date() );
	    if ( product.getProTypeId() == 1 ) {// 虚拟商品，清空预售设置
		product.setIsPresell( 0 );
		product.setProPresellEnd( "" );
		product.setProDeliveryStart( "" );
		product.setProDeliveryEnd( "" );
	    }
	    if ( CommonUtil.isEmpty( product.getReturnDay() ) ) {
		product.setReturnDay( 0 );
	    }
	    product.setIsSyncErp( 0 );
	    product.setCheckStatus( 1 );
	    proId = mallProductDAO.insert( product );
	}
	if ( proId > 0 ) {
	    //新增流量冻结
	    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getFlowId() > 0 ) {
		    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
		    if ( CommonUtil.isNotEmpty( flow ) ) {

			Map< String,Object > map = getFlowRecord( product, flow );
			if ( CommonUtil.isNotEmpty( map ) ) {
			    if ( CommonUtil.isNotEmpty( map.get( "id" ) ) ) {
				product.setFlowRecordId( CommonUtil.toInteger( map.get( "id" ) ) );
			    }
			    if ( map.get( "code" ).toString().equals( "-1" ) ) {
				return map;
			    }
			}
		    }
		}
	    }
	    MallProduct mallProduct = new MallProduct();
	    mallProduct.setId( product.getId() );
	    String key = Constants.REDIS_KEY + "proViewNum";
	    if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
		JedisUtil.map( key, product.getId().toString(), product.getViewsNum().toString() );
	    }
	    // 添加商品详细
	    if ( !CommonUtil.isEmpty( params.get( "detail" ) ) ) {
		MallProductDetail detail = JSONObject.parseObject( params.get( "detail" ).toString(), MallProductDetail.class );
		detail.setProductId( product.getId() );
		if ( detail != null ) {
		    mallProductDetailService.insert( detail );
		}
		//		    mallProductDetailDao.insert(detail);

	    }
	    // 批量添加商品分组
	    if ( !CommonUtil.isEmpty( params.get( "groupList" ) ) ) {
		List< MallProductGroup > groupList = JSONArray.parseArray( params.get( "groupList" ).toString(), MallProductGroup.class );
		if ( groupList != null && groupList.size() > 0 ) {
		    for ( MallProductGroup mallProductGroup : groupList ) {
			mallProductGroup.setProductId( product.getId() );
			mallProductGroupDAO.insert( mallProductGroup );
		    }
		}
	    } else {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品分组不能为空" );
	    }
	    if ( CommonUtil.isEmpty( params.get( "imageList" ) ) ) {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品图片不能为空" );
	    }
	    // 批量添加商品图片
	    mallImageAssociativeService.insertUpdBatchImage( params, product.getId() );

	    Map< String,Object > specMap = new HashMap<>();

	    if ( product.getIsSpecifica() == 1 && CommonUtil.isEmpty( params.get( "speList" ) ) ) {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品规格不能为空" );
	    }
	    // 批量添加商品规格
	    if ( !CommonUtil.isEmpty( params.get( "speList" ) ) ) {
		if ( params.get( "speList" ) != null ) {
		    specMap = mallProductSpecificaService.saveOrUpdateBatch( params.get( "speList" ), product.getId(), null, true );
		}
	    }
	    // 批量添加商品库存
	    if ( !CommonUtil.isEmpty( params.get( "invenList" ) ) ) {
		if ( params.get( "invenList" ) != null ) {
		    mallProductInventoryService.saveOrUpdateBatch( specMap, params.get( "invenList" ), product.getId(), null );
		}
	    }
	    // 批量添加商品参数
	    if ( !CommonUtil.isEmpty( params.get( "paramsList" ) ) ) {
		if ( params.get( "paramsList" ) != null ) {
		    mallProductParamService.saveOrUpdateBatch( params.get( "paramsList" ), product.getId(), null, true );
		}
	    }
	}

	if ( proId > 0 ) {
	    int userPId = busUserService.getMainBusId( user.getId() );//通过用户名查询主账号id
	    long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有
	    if ( isJxc == 1 ) {
		boolean flag = saveProductByErp( product, user, userPId );
		if ( !flag ) {
		    JedisUtil.rPush( "is_no_up_erp", product.getId().toString() );
		}
	    }
	    resultMap.put( "code", 1 );
	    resultMap.put( "id", product.getId() );
	}
	return resultMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > updateProduct( Map< String,Object > params, BusUser user, HttpServletRequest request ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	MallProduct product = null;
	// 修改商品信息
	if ( !CommonUtil.isEmpty( params.get( "product" ) ) ) {
	    product = JSONObject.parseObject( params.get( "product" ).toString(), MallProduct.class );
	    product.setEditTime( new Date() );
	    product.setIsPublish( 0 );
	    String key = Constants.REDIS_KEY + "proViewNum";
	    if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
		JedisUtil.map( key, product.getId().toString(), product.getViewsNum().toString() );
	    }
	    //			MallProduct mallProduct = mallProductDao.selectByPrimaryKey(product.getId());
	    // product.setCheckStatus(Byte.valueOf("-2"));
	    if ( product.getProTypeId() == 1 ) {// 虚拟商品，清空预售设置
		product.setIsPresell( 0 );
		product.setProPresellEnd( "" );
		product.setProDeliveryStart( "" );
		product.setProDeliveryEnd( "" );
	    }
	    if ( CommonUtil.isEmpty( product.getChangeIntegral() ) ) {
		product.setChangeIntegral( 0 );
	    }
	    if ( CommonUtil.isEmpty( product.getReturnDay() ) ) {
		product.setReturnDay( 0 );
	    }
	    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getFlowId() > 0 ) {
		    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
		    if ( CommonUtil.isNotEmpty( flow ) ) {
			Map< String,Object > map = getFlowRecord( product, flow );
			if ( CommonUtil.isNotEmpty( map ) ) {
			    if ( map.get( "code" ).toString().equals( "-1" ) ) {
				return map;
			    }
			}
		    }
		}
	    }
	    product.setIsSyncErp( 0 );
	    // 修改商品信息
	    mallProductDAO.updateById( product );
	}
	// 修改商品详细
	if ( !CommonUtil.isEmpty( params.get( "detail" ) ) ) {
	    MallProductDetail detail = JSONObject.parseObject( params.get( "detail" ).toString(), MallProductDetail.class );
	    if ( detail != null ) {
		if ( CommonUtil.isNotEmpty( detail.getProductDetail() ) && CommonUtil.isNotEmpty( detail.getProductIntrodu() ) && CommonUtil
				.isNotEmpty( detail.getProductMessage() ) ) {
		    if ( CommonUtil.isNotEmpty( detail.getId() ) ) {
			mallProductDetailService.updateById( detail );
		    } else {
			detail.setProductId( product.getId() );
			mallProductDetailService.insert( detail );
		    }
		}
	    }

	}

	// 添加或修改图片
	mallImageAssociativeService.insertUpdBatchImage( params, product.getId() );

	Object specObj = null;
	Map< String,Object > specMap = new HashMap<>();
	MallGroupBuy groupBuy = new MallGroupBuy();
	groupBuy.setProductId( product.getId() );
	//查询商品是否存在于团购中
	List< MallGroupBuy > buyList = mallGroupBuyDAO.selectGroupByProId( groupBuy );
	boolean flag = true;//允许修改商品规格
	if ( buyList != null && buyList.size() > 0 ) {//商品存在于团购中不允许修改商品规格
	    flag = false;
	}
	// 需要修改的规格数据
	if ( CommonUtil.isNotEmpty( params.get( "speList" ) ) ) {
	    if ( params.get( "speList" ) != null ) {
		specObj = params.get( "speList" );
	    }
	}
	Map< String,Object > defaultSpecMap = null;
	// 还未修改的规格数据
	if ( CommonUtil.isNotEmpty( params.get( "specDefaultObj" ) ) ) {
	    defaultSpecMap = JSONObject.parseObject( params.get( "specDefaultObj" ).toString() );
	}
	// 批量添加或修改商品规格
	if ( specObj != null || defaultSpecMap != null ) {
	    specMap = mallProductSpecificaService.saveOrUpdateBatch( params.get( "speList" ), product.getId(), defaultSpecMap, flag );
	}
	Object invenObj = null;
	// 需要修改的库存数据
	if ( !CommonUtil.isEmpty( params.get( "invenList" ) ) ) {
	    if ( params.get( "invenList" ) != null ) {
		invenObj = params.get( "invenList" );
	    }
	}
	Map< String,Object > defaultMap = new HashMap<>();
	// 还未修改的库存数据
	if ( !CommonUtil.isEmpty( params.get( "inveDefaultObj" ) ) ) {
	    defaultMap = JSONObject.parseObject( params.get( "inveDefaultObj" ).toString() );
	}
	// 批量添加商品库存
	if ( invenObj != null || defaultMap != null ) {
	    mallProductInventoryService.saveOrUpdateBatch( specMap, invenObj, product.getId(), defaultMap );
	}
	Map< String,Object > paramDefaultMap = new HashMap<>();
	// 还未修改的参数数据
	if ( !CommonUtil.isEmpty( params.get( "paramDefaultObj" ) ) ) {
	    paramDefaultMap = JSONObject.parseObject( params.get( "paramDefaultObj" ).toString() );
	}
	// 批量添加商品参数
	if ( !CommonUtil.isEmpty( params.get( "paramsList" ) ) ) {
	    mallProductParamService.saveOrUpdateBatch( params.get( "paramsList" ), product.getId(), paramDefaultMap, true );
	}
	// 批量添加商品分组
	if ( !CommonUtil.isEmpty( params.get( "groupList" ) ) ) {
	    List< MallProductGroup > groupList = JSONArray.parseArray( params.get( "groupList" ).toString(), MallProductGroup.class );
	    if ( groupList != null && groupList.size() > 0 ) {
		for ( MallProductGroup mallProductGroup : groupList ) {
		    mallProductGroup.setProductId( product.getId() );
		    mallProductGroupDAO.insert( mallProductGroup );
		}
	    }
	}
	// 逻辑删除产品分组
	if ( !CommonUtil.isEmpty( params.get( "updGroupList" ) ) ) {
	    List< MallProductGroup > groupList = JSONArray.parseArray( params.get( "updGroupList" ).toString(), MallProductGroup.class );
	    if ( groupList != null ) {
		for ( MallProductGroup mallProductGroup : groupList ) {
		    mallProductGroupDAO.updateById( mallProductGroup );
		}
	    }

	}
	int userPId = busUserService.getMainBusId( user.getId() );//通过用户名查询主账号id
	long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有
	if ( isJxc == 1 ) {
	    boolean flags = saveProductByErp( product, user, userPId );
	    if ( !flags ) {
		JedisUtil.rPush( Constants.REDIS_KEY + "is_no_up_erp", product.getId().toString() );
	    }
	}
	//return product.getId();
	resultMap.put( "code", 1 );
	resultMap.put( "id", product.getId() );
	return resultMap;
    }

    @Override
    public MallProduct selectByPrimaryKey( Integer proId ) {
	return mallProductDAO.selectById( proId );
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > copyProductByShopId( Map< String,Object > params, BusUser user ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	Wrapper< MallProduct > productWrapper = new EntityWrapper<>();
	productWrapper.where( "shop_id = {0}", params.get( "shopId" ) );
	List< MallProduct > productList = mallProductDAO.selectList( productWrapper );
	if ( productList != null && productList.size() > 0 ) {
	    for ( MallProduct mallProduct : productList ) {
		resultMap = syncProduct( params, user, mallProduct, CommonUtil.toInteger( params.get( "toShopId" ) ), mallProduct.getId() );
	    }
	}
	return resultMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > copyProduct( Map< String,Object > params, BusUser user ) throws Exception {
	if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
	    throw new Exception();
	}
	int id = CommonUtil.toInteger( params.get( "id" ) );
	// 查询商品基本信息
	MallProduct product = mallProductDAO.selectById( id );// 根据商品id查询商品的基本信息
	if ( CommonUtil.isEmpty( product ) ) {
	    throw new Exception();
	}
	Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );

	return syncProduct( params, user, product, shopId, id );
    }

    @Override
    public List< Map< String,Object > > selectProductByWxShop( Map< String,Object > params ) throws Exception {
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "wxShopId" ) ) ) {
		List< Map< String,Object > > proList = mallProductDAO.selectByShopId( params );
		List< Map< String,Object > > productList = new ArrayList<>();
		if ( proList != null && proList.size() > 0 ) {
		    double discount = 1;
		    boolean isPifa = false;
		    if ( CommonUtil.isNotEmpty( params.get( "member_id" ) ) ) {
			Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "member_id" ) ), null );
			discount = getMemberDiscount( "1", member );

			MallPaySet set = mallPaySetService.selectByMember( member );
			int state = mallPifaApplyService.getPifaApplay( member, set );
			if ( CommonUtil.isNotEmpty( set ) ) {
			    if ( CommonUtil.isNotEmpty( set.getIsPf() ) ) {//是否开启批发
				if ( set.getIsPf().toString().equals( "1" ) && state == 1 ) {
				    isPifa = true;
				}
			    }
			}
		    }
		    for ( Map< String,Object > productMap : proList ) {
			productMap = getProductPrice( productMap, discount, isPifa );
			productList.add( productMap );
		    }
		}
		return proList;
	    }
	}
	return null;
    }

    @Override
    public int selectCountProductByWxShop( Map< String,Object > params ) throws Exception {
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "wxShopId" ) ) ) {
		return mallProductDAO.selectCountByUserId( params );
	    }
	}
	return -1;
    }

    @Override
    public Map< String,Object > selectProStockByProId( Map< String,Object > params ) throws Exception {
	boolean isPifa = false;
	Member member = null;
	if ( CommonUtil.isNotEmpty( params.get( "member_id" ) ) ) {
	    member = memberService.findMemberById( CommonUtil.toInteger( params.get( "member_id" ) ), null );
	    isPifa = mallPifaApplyService.isPifa( member );
	}

	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "productId" ) ) ) {
		Map< String,Object > productMap = mallProductDAO.selectByProId( params );
		if ( CommonUtil.isNotEmpty( productMap ) ) {
		    double discount = 1;//商品折扣
		    if ( CommonUtil.isNotEmpty( params.get( "member_id" ) ) && CommonUtil.isNotEmpty( productMap.get( "is_member_discount" ) ) ) {
			String is_member_discount = productMap.get( "is_member_discount" ).toString();//商品是否参加折扣
			discount = getMemberDiscount( is_member_discount, member );
		    }
		    productMap = getProductPrice( productMap, discount, isPifa );
		}
		return productMap;
	    }
	}
	return null;
    }

    @Override
    public Map< String,Object > updateProductStock( Map< String,Object > params ) throws Exception {
	logger.info( "修改商品库存时的参数：" + params );
	Map< String,Object > resultMap = new HashMap<>();
	int type = 1;//操作类型    1加库存   2减库存
	int inventory_id = 0;//库存id
	int proId = 0;//商品id
	int num = 0;//商品数量
	//查询商品是否还有库存
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = CommonUtil.toInteger( params.get( "type" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "inventory_id" ) ) ) {
		inventory_id = CommonUtil.toInteger( params.get( "inventory_id" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "product_id" ) ) ) {
		proId = CommonUtil.toInteger( params.get( "product_id" ) );
	    } else {
		resultMap.put( "success", false );
		resultMap.put( "errorMsg", "参数不完整：商品id" );
		return resultMap;
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "pro_num" ) ) ) {
		num = CommonUtil.toInteger( params.get( "pro_num" ) );
	    } else {
		resultMap.put( "success", false );
		resultMap.put( "errorMsg", "参数不完整：商品数量" );
		return resultMap;
	    }
	}

	if ( type == 2 ) {//减库存前判断商品库存是否足够
	    MallProduct product = mallProductDAO.selectById( proId );
	    if ( product.getProStockTotal() - num < 0 ) {
		resultMap.put( "success", false );
		resultMap.put( "errorMsg", "你购买的数量大于商品现有的库存，请重新选择商品" );
		return resultMap;
	    }
	    if ( inventory_id == 0 ) {
		List< MallProductInventory > inventoryList = mallProductInventoryService.getInventByProductId( proId );
		if ( inventoryList.size() > 0 ) {
		    resultMap.put( "success", false );
		    resultMap.put( "errorMsg", "参数不完整：库存id" );
		    return resultMap;
		}
	    }
	    if ( inventory_id > 0 ) {
		MallProductInventory inventory = mallProductInventoryDAO.selectById( inventory_id );
		if ( inventory.getInvNum() - num < 0 ) {
		    resultMap.put( "success", false );
		    resultMap.put( "errorMsg", "你购买的数量大于商品现有的库存，请重新选择商品规格" );
		    return resultMap;
		}
	    }
	}

	int count = mallProductDAO.updateProductStock( params );
	if ( count <= 0 ) {
	    resultMap.put( "success", false );
	    resultMap.put( "errorMsg", "修改商品库存失败，请稍后重试" );
	    return resultMap;
	}
	if ( inventory_id > 0 ) {
	    count = mallProductInventoryDAO.updateProductStock( params );
	    if ( count <= 0 ) {
		resultMap.put( "success", false );
		resultMap.put( "errorMsg", "修改商品库存失败，请稍后重试" );
		return resultMap;
	    }
	}
	resultMap.put( "success", true );
	return resultMap;
    }

    @Override
    public Map< String,Object > selectProductSpec( Map< String,Object > params ) throws Exception {
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "productId" ) ) ) {
		int productId = CommonUtil.toInteger( params.get( "productId" ) );

		boolean isPifa = false;
		double discount = 1;//保存会员的折扣
		if ( CommonUtil.isNotEmpty( params.get( "member_id" ) ) ) {
		    Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "member_id" ) ), null );
		    isPifa = mallPifaApplyService.isPifa( member );
		    discount = getMemberDiscount( "1", member );//获取会员的折扣
		}

		//查询商品规格集合
		List< Map< String,Object > > specificaList = mallProductSpecificaService.getSpecificaByProductId( productId );

		//查询商品的默认规格
		Map< String,Object > defaultSpecifica = mallProductInventoryService.productSpecifications( productId, null );

		//查询商品库存集合
		List< Map< String,Object > > inventoryList = mallProductInventoryService.guigePrice( productId );
		DecimalFormat df = new DecimalFormat( "######0.00" );

		if ( CommonUtil.isNotEmpty( defaultSpecifica ) ) {
		    if ( isPifa ) {
			params.put( "invId", defaultSpecifica.get( "id" ) );
			Map< String,Object > pfPriceMap = mallPifaPriceDao.selectPriceByInvId( params );
			if ( CommonUtil.isNotEmpty( pfPriceMap ) ) {
			    defaultSpecifica.put( "wholesale_price", pfPriceMap.get( "seckill_price" ) );
			}
		    }
		    //获取会员价
		    if ( discount > 0 && discount < 1 ) {
			if ( CommonUtil.isNotEmpty( defaultSpecifica.get( "inv_price" ) ) ) {
			    double proPrice = CommonUtil.toDouble( defaultSpecifica.get( "inv_price" ) );
			    double memberProPrice = CommonUtil.toDouble( df.format( proPrice * discount ) );
			    defaultSpecifica.put( "member_price", memberProPrice );
			}
		    }
		}

		//
		if ( inventoryList != null && inventoryList.size() > 0 ) {
		    List< Map< String,Object > > interList = new ArrayList<>();
		    for ( Map< String,Object > map : inventoryList ) {
			if ( CommonUtil.isNotEmpty( map.get( "inv_price" ) ) ) {
			    params.put( "invId", map.get( "id" ) );
			    //获取批发价
			    if ( isPifa ) {
				Map< String,Object > pfPriceMap = mallPifaPriceDao.selectPriceByInvId( params );
				if ( CommonUtil.isNotEmpty( pfPriceMap ) ) {
				    map.put( "wholesale_price", pfPriceMap.get( "seckill_price" ) );
				}
			    }
			    //获取会员价
			    if ( discount > 0 && discount < 1 ) {
				if ( CommonUtil.isNotEmpty( map.get( "inv_price" ) ) ) {
				    double proPrice = CommonUtil.toDouble( map.get( "inv_price" ) );
				    double memberProPrice = CommonUtil.toDouble( df.format( proPrice * discount ) );
				    map.put( "member_price", memberProPrice );
				}
			    }
			}
			interList.add( map );
		    }
		    if ( interList.size() > 0 ) {
			inventoryList = interList;
		    }
		}

		Map< String,Object > resultMap = new HashMap<>();

		resultMap.put( "specificaList", specificaList );
		resultMap.put( "defaultSpecifica", defaultSpecifica );
		resultMap.put( "inventoryList", inventoryList );
		return resultMap;
	    }
	}
	return null;
    }

    @Override
    public int getJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid ) {
	HttpSession session = request.getSession();
	if ( isJifen > 0 ) {
	    session.setAttribute( Constants.SESSION_KEY + "isJifen_" + userid, isJifen );
	}
	if ( CommonUtil.isNotEmpty( session.getAttribute( Constants.SESSION_KEY + "isJifen_" + userid ) ) ) {
	    return CommonUtil.toInteger( session.getAttribute( Constants.SESSION_KEY + "isJifen_" + userid ) );
	}

	return isJifen;
    }

    @Override
    public void setJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid ) {
	if ( isJifen > 0 ) {
	    request.getSession().setAttribute( Constants.SESSION_KEY + "isJifen_" + userid, isJifen );
	}
    }

    @Override
    public void clearJifenByRedis( Member member, HttpServletRequest request, int userid ) {
	request.getSession().setAttribute( Constants.SESSION_KEY + "isJifen_" + userid, null );
    }

    @Override
    public Map< String,Object > getProInvIdBySpecId( String specId, int proId ) {
	Map< String,Object > maps = new HashMap<>();

	if ( CommonUtil.isNotEmpty( specId ) ) {
	    StringBuilder specifica_value = new StringBuilder();
	    List< MallProductSpecifica > specificaList = mallProductSpecificaService.selectByValueIds( proId, specId.split( "," ) );
	    String specificaImageUrl = "";
	    String specificaName = "";
	    if ( specificaList != null && specificaList.size() > 0 ) {

		String proSpecId = "";
		for ( MallProductSpecifica specifica : specificaList ) {
		    specificaName = specifica.getSpecificaName();
		    if ( CommonUtil.isNotEmpty( proSpecId ) ) {
			proSpecId += ",";
			specifica_value.append( "," );
		    }
		    proSpecId += specifica.getId();
		    specifica_value.append( CommonUtil.toString( specifica.getSpecificaValue() ) );
		    if ( CommonUtil.isNotEmpty( specifica.getSpecificaImgUrl() ) ) {
			specificaImageUrl = CommonUtil.toString( specifica.getSpecificaImgUrl() );

		    }
		}
		MallProductInventory inventory = mallProductInventoryService.selectBySpecIds( proId, proSpecId );
		if ( CommonUtil.isNotEmpty( inventory ) ) {
		    maps.put( "id", inventory.getId() );
		    maps.put( "inv_price", inventory.getInvPrice() );
		    maps.put( "inv_num", inventory.getInvNum() );
		    maps.put( "erp_inv_id", inventory.getErpInvId() );
		    maps.put( "erp_specvalue_id", inventory.getErpSpecvalueId() );

		    maps.put( "specifica_values", specifica_value.toString() );
		    maps.put( "specificaName", specificaName );
		    if ( CommonUtil.isNotEmpty( specificaImageUrl ) ) {
			maps.put( "specifica_img_url", specificaImageUrl );
		    }

		}
	    }
	}
	return maps;
    }

    @Override
    public double getMemberDiscount( String isMemberDiscount, Member member ) {
	if ( isMemberDiscount.equals( "1" ) && CommonUtil.isNotEmpty( member ) ) {
	    return memberService.getMemberDiscount( member.getId() );
	}
	return 0;
    }

    @Override
    public Map< String,Object > calculateInventory( int proId, Object proSpecificas, int proNum, int memberId ) {
	Map< String,Object > result = new HashMap<>();
	MallProduct pro = mallProductDAO.selectById( proId );
	Integer isSpe = pro.getIsSpecifica();
	int userPId = busUserService.getMainBusId( pro.getUserId() );//通过用户名查询主账号id
	long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	MallStore store = mallStoreService.selectById( pro.getShopId() );
	boolean flag = true;
	int erpInvId = 0;
	if ( isSpe == 1 ) {//是否有规格（0没有 1有）
	    Map< String,Object > invParams = new HashMap<>();
	    invParams.put( "proId", proId );
	    String[] specifica = ( proSpecificas.toString() ).split( "," );
	    StringBuilder ids = new StringBuilder( "0" );
	    for ( String valueIds : specifica ) {
		if ( CommonUtil.isNotEmpty( valueIds ) ) {
		    invParams.put( "specificaValueId", valueIds );
		    //		    int specIds = mOrderMapper.selectSpeBySpeValueId(invParams);
		    Map< String,Object > specificaParams = new HashMap<>();
		    specificaParams.put( "valueId", valueIds );
		    specificaParams.put( "proId", proId );
		    MallProductSpecifica productSpecifica = mallProductSpecificaService.selectByNameValueId( specificaParams );
		    if ( CommonUtil.isNotEmpty( productSpecifica ) && CommonUtil.isNotEmpty( productSpecifica.getId() ) ) {
			ids.append( "," ).append( productSpecifica.getId() );
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( ids.toString() ) ) {
		if ( !"0".equals( ids.toString() ) ) {
		    invParams.put( "specificaIds", ids.substring( 2, ids.length() ) );
		    MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( invParams );
		    if ( null != proInv && CommonUtil.isNotEmpty( proInv ) ) {
			//判断商家是否有进销存
			if ( isJxc == 0 || !"0".equals( pro.getProTypeId().toString() ) ) {//没有进销存才能判断商城的库存
			    if ( proInv.getInvNum() < proNum ) {
				flag = false;
				result.put( "msg", "商品库存不够" );
				throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品库存不够，请重新挑选商品" );
			    }
			} else {
			    erpInvId = proInv.getErpInvId();
			}
		    } else {
			flag = false;
			result.put( "msg", "商品库存不够" );
			throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品库存不够，请重新挑选商品" );
		    }
		} else {
		    isSpe = 0;
		}
	    } else {
		isSpe = 0;
	    }
	}
	if ( null == isSpe || CommonUtil.isEmpty( isSpe ) || isSpe == 0 ) {
	    if ( erpInvId == 0 && isJxc == 1 && "0".equals( pro.getProTypeId().toString() ) ) {
		erpInvId = pro.getErpInvId();
	    }
	    if ( pro.getProStockTotal() < proNum ) {
		flag = false;
		result.put( "msg", "商品库存不够" );
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品库存不够，请重新挑选商品" );
	    }
	}
	if ( isJxc == 1 && erpInvId > 0 && flag ) {
	    Map< String,Object > erpMap = new HashMap<>();
	    erpMap.put( "shopId", store.getWxShopId() );
	    erpMap.put( "attrsId", erpInvId );
	    Object erpInv = MallJxcHttpClientUtil.getInvNumByInvenId( erpMap, true );
	    if ( CommonUtil.isNotEmpty( erpInv ) ) {
		double proStock = CommonUtil.toDouble( erpInv );
		if ( proStock < proNum ) {
		    flag = false;
		    result.put( "msg", "商品库存不够" );
		    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品库存不够，请重新挑选商品" );
		}
	    }
	}
	//判断商品限购
	if ( CommonUtil.isNotEmpty( pro.getProRestrictionNum() ) && flag && memberId > 0 ) {
	    if ( pro.getProRestrictionNum() > 0 ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "productId", pro.getId() );
		params.put( "shopId", pro.getShopId() );
		params.put( "buyerUserId", memberId );
		//查询商品已买数量
		int buyNums = mallOrderDAO.selectMemberBuyProNum( params );
		if ( buyNums + proNum > pro.getProRestrictionNum() ) {
		    flag = false;
		    result.put( "msg", "您购买的数量已经超过限购的数量" );
		    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您购买的数量已经超过限购的数量" );
		}
	    }
	}
	result.put( "result", flag );
	return result;
    }

    @Override
    public Map< String,Object > getSpecNameBySPecId( String specId, int productId ) {
	Map< String,Object > map = new HashMap<>();
	if ( CommonUtil.isNotEmpty( specId ) ) {
	    List< MallProductSpecifica > specificaList = mallProductSpecificaService.selectByValueIds( productId, specId.split( "," ) );
	    String specificaValue = "";
	    if ( specificaList != null && specificaList.size() > 0 ) {
		String imageUrl = "";
		for ( MallProductSpecifica specifica : specificaList ) {
		    if ( CommonUtil.isNotEmpty( specificaValue ) ) {
			specificaValue += " ";
		    }
		    specificaValue += specifica.getSpecificaValue();
		    if ( CommonUtil.isNotEmpty( specifica.getSpecificaImgUrl() ) ) {
			imageUrl = PropertiesUtil.getResourceUrl() + specifica.getSpecificaImgUrl();
		    }
		}
		map.put( "product_speciname", specificaValue );
		map.put( "imageUrl", imageUrl );
	    }
	}
	return map;
    }

    @Override
    public Map< String,Object > isshoppingCart( Map< String,Object > map, int productNum, List< Map< String,Object > > shopList ) {
	Map< String,Object > productMap = new HashMap<>();
	Map< String,Object > resultMap = new HashMap<>();
	String proId = map.get( "product_id" ).toString();
	String proStatus = map.get( "check_status" ).toString();
	int code = 1;
	String msg = "";

	//判断限购
	if ( map.get( "isDelete" ).toString().equals( "1" ) ) {
	    code = 0;
	    msg = "商品已被删除";
	}
	if ( !map.get( "is_publish" ).toString().equals( "1" ) && code == 1 ) {
	    code = 0;
	    msg = "商品未发布";
	}
	if ( !proStatus.equals( "1" ) && code == 1 ) {
	    code = 0;
	    switch ( proStatus ) {
		case "0":
		    msg = "商品审核中";
		    break;
		case "-1":
		    msg = "商品审核失败";
		    break;
		case "-2":
		    msg = "商品还未审核";
		    break;
		default:
		    break;
	    }
	}

	if ( CommonUtil.isNotEmpty( map.get( "pro_restriction_num" ) ) && code == 1 ) {
	    int maxBuyNum = CommonUtil.toInteger( map.get( "pro_restriction_num" ) );
	    if ( maxBuyNum > 0 ) {
		Map< String,Object > proParams = new HashMap<>();
		proParams.put( "productId", proId );
		proParams.put( "shopId", map.get( "shop_id" ) );
		proParams.put( "buyerUserId", map.get( "user_id" ) );
		int num = mallOrderDAO.selectMemberBuyProNum( proParams );
		int productNums = productNum;
		if ( !productMap.containsKey( proId ) ) {
		    productMap.put( proId, productNum );
		} else {
		    int proNum = CommonUtil.toInteger( productMap.get( proId ) );
		    productNums += proNum;
		}
		if ( num > maxBuyNum || productNums > maxBuyNum ) {
		    code = 0;
		    msg = "商品限购" + maxBuyNum + "件";
		}
		if ( num <= maxBuyNum && productNums > maxBuyNum && productNums - maxBuyNum > 0 ) {//如果购买没有超过限购，则修改购物车的数量到限购的上限
		    code = 1;
		    int chaNum = maxBuyNum - ( productNums + num );
		    if ( chaNum < 0 ) {
			chaNum = chaNum * -1;
		    }
		    int buyNums = productNum - chaNum;
		    if ( buyNums > 0 ) {
			MallShopCart cart = new MallShopCart();
			cart.setId( CommonUtil.toInteger( map.get( "id" ) ) );
			cart.setProductNum( buyNums );
			mallShopCartDao.updateById( cart );
			resultMap.put( "product_num", buyNums );
			//			productNum = buyNums;
			productMap.put( proId, buyNums );
		    } else {
			code = 0;
		    }

		}
		resultMap.put( "maxBuy", maxBuyNum - num );
	    }
	}
	boolean isSxShop = false;
	/*if ( CommonUtil.isEmpty( map.get( "wx_shop_id" ) ) ) {
	    code = 0;
	    msg = "店铺已被删除";
	}*/
	String shopId = CommonUtil.toString( map.get( "shop_id" ) );
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map< String,Object > shopMap : shopList ) {
		if ( shopMap.get( "id" ).toString().equals( shopId ) ) {
		    isSxShop = true;
		    resultMap.put( "sto_name", shopMap.get( "sto_name" ) );
		    if ( shopMap.get( "is_delete" ).toString().equals( "1" ) ) {
			code = 0;
			msg = "店铺或门店已被删除";
		    }
		    break;
		}
	    }
	}
	if ( !isSxShop ) {
	    code = 0;
	    msg = "店铺或门店已被删除";
	}
	resultMap.put( "code", code );
	resultMap.put( "msg", msg );
	resultMap.put( "productMap", productMap );
	return resultMap;
    }

    @Override
    public Map< String,Object > saveOrUpdateProductByErp( Map< String,Object > params, HttpServletRequest request ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	int code = 1;
	String msg = "";
	List< Map > list = JSONArray.parseArray( params.get( "product" ).toString(), Map.class );
	int userId = 0;
	String proIds = "";
	if ( list != null && list.size() > 0 ) {
	    for ( Map map : list ) {
		// 新增或修改商品信息
		if ( !CommonUtil.isEmpty( map.get( "product" ) ) ) {
		    MallProduct product = JSONObject.parseObject( map.get( "product" ).toString(), MallProduct.class );
		    userId = product.getUserId();
		    product.setIsPublish( 0 );
		    if ( CommonUtil.isEmpty( product.getChangeIntegral() ) ) {
			product.setChangeIntegral( 0 );
		    }
		    if ( CommonUtil.isEmpty( product.getReturnDay() ) ) {
			product.setReturnDay( 0 );
		    }
		    List< Integer > proIdList = new ArrayList<>();
		    List< MallProduct > productList = null;
		    if ( CommonUtil.isNotEmpty( product.getErpProId() ) ) {
			if ( product.getErpProId() > 0 ) {
			    Map< String,Object > proParams = new HashMap<>();
			    proParams.put( "erpProId", product.getErpProId() );
			    productList = selectByERPId( proParams );
			    if ( productList != null && productList.size() > 0 ) {
				for ( MallProduct mallProduct : productList ) {
				    proIdList.add( mallProduct.getId() );
				}
			    }
			}
		    }
		    if ( CommonUtil.isNotEmpty( product.getIsDelete() ) ) {
			product.setIsDelete( 0 );
		    }
		    if ( CommonUtil.isNotEmpty( map.get( "invenList" ) ) ) {
			product.setIsSpecifica( 1 );
		    }
		    if ( proIdList == null || proIdList.size() == 0 ) {
			product.setCreateTime( new Date() );
			mallProductDAO.insert( product );
			product.setShopId( 0 );
			proIdList.add( product.getId() );
		    }
		    if ( proIdList != null && proIdList.size() > 0 ) {
			for ( Integer proId : proIdList ) {
			    if ( productList != null && productList.size() > 0 ) {
				product.setEditTime( new Date() );
				product.setId( proId );
				// 修改商品信息
				mallProductDAO.updateById( product );
			    }
			    if ( CommonUtil.isNotEmpty( map.get( "invenList" ) ) ) {
				Map< String,Object > invenResultMap = mallProductInventoryService.saleOrUpdateBatchErp( map, proId, product.getUserId() );
				if ( !invenResultMap.get( "code" ).toString().equals( "1" ) ) {
				    logger.error( "新增或修改商品库存异常：" + invenResultMap.get( "errorMsg" ) );
				    throw new Exception();
				}
			    }
			}
		    }
		    if ( CommonUtil.isNotEmpty( proIds ) ) {
			if ( !proIds.contains( product.getErpProId() + "," ) ) {
			    proIds += product.getErpProId() + ",";
			}
		    } else {
			proIds += product.getErpProId() + ",";
		    }
		}
	    }
	}

	resultMap.put( "code", code );
	if ( code == -1 ) {
	    resultMap.put( "errorMsg", msg );
	} else {
	    if ( CommonUtil.isNotEmpty( proIds ) && userId > 0 ) {
		int userPId = busUserService.getMainBusId( userId );//通过用户名查询主账号id
		Map< String,Object > syncParams = new HashMap<>();
		syncParams.put( "productIds", proIds.substring( 0, proIds.length() - 1 ) );
		syncParams.put( "rootUid", userPId );
		MallJxcHttpClientUtil.syncCallback( syncParams, true );
	    }
	}
	return resultMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > syncErpProductByWxShop( Map< String,Object > params, HttpServletRequest request ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	int code = 1;
	String msg = "";
	List< Map > erpList = JSONArray.parseArray( params.get( "invList" ).toString(), Map.class );
	int userId = CommonUtil.toInteger( params.get( "userId" ) );
	if ( userId > 0 ) {
	    syncErpPro( userId, request );//把未同步的商品进行同步
	}
	String productIds = "";
	if ( erpList == null || erpList.size() <= 0 ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "商品参数不完整" );
	    return resultMap;
	} else {
	    for ( Map erpParams : erpList ) {
		if ( code < 1 ) {
		    break;
		}
		int wxShopId = 0;//门店id
		int shopId = 0;//商城的店铺id
		int erpInvId = 0;//规格详情id
		int erpProId = 0;//erp商品id
		if ( CommonUtil.isNotEmpty( erpParams.get( "erpInvId" ) ) ) {
		    erpInvId = CommonUtil.toInteger( erpParams.get( "erpInvId" ) );
		}
		if ( CommonUtil.isNotEmpty( erpParams.get( "shopId" ) ) ) {
		    wxShopId = CommonUtil.toInteger( erpParams.get( "shopId" ) );
		}
		if ( CommonUtil.isNotEmpty( erpParams.get( "erpProId" ) ) ) {
		    erpProId = CommonUtil.toInteger( erpParams.get( "erpProId" ) );
		}
		if ( erpInvId == 0 || erpProId == 0 || wxShopId == 0 ) {
		    code = -1;
		    msg = "商品参数不完整";
		    break;
		}
		//通过门店查询店铺id
		MallStore sto = new MallStore();
		sto.setWxShopId( wxShopId );
		List< MallStore > storeList = mallStoreService.findByShopId( wxShopId, 0 );
		if ( storeList != null && storeList.size() > 0 ) {
		    shopId = CommonUtil.toInteger( storeList.get( 0 ).getId() );
		}
		if ( shopId == 0 ) {//商城没有创建对应的店铺
		    continue;
		}
		Map< String,Object > productParams = new HashMap<>();
		productParams.put( "erpProId", erpProId );
		productParams.put( "shopId", shopId );
		List< MallProduct > productList = selectByERPId( productParams );//根据店铺id和erp商品id查询对应的商品信息
		if ( productList == null || productList.size() == 0 ) {
		    productParams.put( "shopId", 0 );
		    productList = selectByERPId( productParams );//查询没有分配店铺的erp商品
		}
		if ( productList == null || productList.size() == 0 ) {
		    productParams.remove( "shopId" );
		    productList = selectByERPId( productParams );//查询没有分配店铺的erp商品
		}
		if ( productList != null && productList.size() > 0 ) {
		    for ( MallProduct mallProduct : productList ) {
			//						int oldProId = mallProduct.getId();
			int newProId = 0;
			if ( mallProduct.getIsSpecifica().toString().equals( "0" ) ) {
			    mallProduct.setErpInvId( erpInvId );
			}
			if ( mallProduct.getShopId() == 0 && ( productList == null || productList.size() == 0 ) ) {
			    mallProduct.setShopId( shopId );
			    mallProductDAO.updateById( mallProduct );
			}
			if ( mallProduct.getShopId() > 0 && !mallProduct.getShopId().toString().equals( CommonUtil.toString( shopId ) ) ) {
			    mallProduct.setShopId( shopId );
			    mallProduct.setId( null );
			    mallProduct.setIsPublish( 0 );
			    mallProduct.setCheckStatus( 1 );
			    mallProduct.setCreateTime( new Date() );
			    mallProductDAO.insert( mallProduct );
			    newProId = mallProduct.getId();
			}
			if ( mallProduct.getIsSpecifica().toString().equals( "1" ) ) {
			    MallProductInventory inventory = mallProductInventoryService.selectByErpInvId( mallProduct.getId(), erpInvId );

			    if ( newProId > 0 ) {
				String[] specStrs = inventory.getSpecificaIds().split( "," );
				StringBuilder newSpecIds = new StringBuilder();
				List< MallProductSpecifica > specficaList = mallProductSpecificaService.selectBySpecId( specStrs );
				if ( specficaList != null && specficaList.size() > 0 ) {
				    for ( MallProductSpecifica mallProductSpecifica : specficaList ) {
					mallProductSpecifica.setProductId( newProId );
					mallProductSpecifica.setId( null );
					mallProductSpecificaDao.insert( mallProductSpecifica );
					newSpecIds.append( mallProductSpecifica.getId() );
				    }
				}
				if ( CommonUtil.isNotEmpty( newSpecIds.toString() ) ) {
				    inventory.setProductId( newProId );
				    inventory.setId( null );
				    newSpecIds = new StringBuilder( newSpecIds.substring( 0, newSpecIds.length() - 1 ) );
				    inventory.setSpecificaIds( newSpecIds.toString() );
				    mallProductInventoryDAO.insert( inventory );
				} else {
				    code = -1;
				}

			    }
			}
			if ( newProId > 0 ) {
			    if ( CommonUtil.isNotEmpty( productIds ) ) {
				if ( productIds.contains( erpProId + "," ) ) {
				    productIds += erpProId + ",";
				}
			    } else {
				productIds += erpProId + ",";
			    }
			}

		    }
		}
	    }
	}
	resultMap.put( "code", code );
	if ( code < 1 ) {
	    resultMap.put( "errorMsg", msg );
	    throw new Exception( msg );
	} else if ( userId > 0 && CommonUtil.isNotEmpty( productIds ) ) {
	    Map< String,Object > syncParams = new HashMap<>();
	    syncParams.put( "productIds", productIds );
	    syncParams.put( "rootUid", userId );
	    MallJxcHttpClientUtil.syncCallback( syncParams, true );
	}
	return resultMap;
    }

    @Override
    public boolean saveProductByErp( MallProduct product, BusUser user, int userPId ) {
	product = mallProductDAO.selectById( product.getId() );
	if ( product.getProTypeId().toString().equals( "1" ) ) {
	    MallProduct pro = new MallProduct();
	    pro.setId( product.getId() );
	    pro.setIsSyncErp( 1 );
	    mallProductDAO.updateById( pro );
	    return true;
	}
	boolean flag = true;
	Map< String,Object > valueMaps = new HashMap<>();
	Map< String,Object > nameMaps = new HashMap<>();
	int uType = 1;//用户类型 1总账号  0子账号
	if ( user.getId() != userPId ) {
	    uType = 0;
	}

	int wxShopId = mallStoreService.createCangku( product.getShopId(), user, uType );
	if ( wxShopId == 0 ) {
	    return false;
	}

	List< Map< String,Object > > invList = new ArrayList<>();
	//查询商品规格信息
	List< Map< String,Object > > specificaList = mallProductSpecificaService.getSpecificaByProductId( product.getId() );
	if ( specificaList != null && specificaList.size() > 0 ) {
	    for ( Map< String,Object > pMap : specificaList ) {
		if ( !flag ) {
		    break;
		}
		int nameId = CommonUtil.toInteger( pMap.get( "specNameId" ) );
		int erpNameId = 0;
		if ( CommonUtil.isNotEmpty( pMap.get( "erpNameId" ) ) ) {
		    erpNameId = CommonUtil.toInteger( pMap.get( "erpNameId" ) );
		}
		String specName = CommonUtil.toString( pMap.get( "specName" ) );
		if ( erpNameId == 0 ) {
		    //同步erp的父类规格
		    erpNameId = mallProductSpecificaService.addErpSpecificas( 0, specName, user.getId(), userPId, uType, user.getName() );
		    if ( erpNameId <= 0 ) {
			flag = false;
			break;
		    }
		}
		if ( CommonUtil.isNotEmpty( pMap.get( "specValues" ) ) ) {//子类规格
		    List< Map > valueList = JSONArray.parseArray( pMap.get( "specValues" ).toString(), Map.class );
		    if ( valueList != null && valueList.size() > 0 ) {
			for ( Map vMap : valueList ) {
			    if ( !flag ) {
				break;
			    }
			    int valueId = CommonUtil.toInteger( vMap.get( "specValueId" ) );
			    String specValue = vMap.get( "specValue" ).toString();
			    int erpValueId = 0;
			    if ( CommonUtil.isNotEmpty( vMap.get( "erpValueId" ) ) ) {
				erpValueId = CommonUtil.toInteger( vMap.get( "erpValueId" ) );
			    }
			    if ( erpValueId == 0 ) {
				erpValueId = mallProductSpecificaService.addErpSpecificas( erpNameId, specValue, user.getId(), userPId, uType, user.getName() );
				if ( erpValueId <= 0 ) {
				    flag = false;
				    break;
				}
				MallProductSpecifica specifica = new MallProductSpecifica();
				specifica.setErpSpecnameId( erpNameId );
				specifica.setErpSpecvalueId( erpValueId );
				specifica.setId( CommonUtil.toInteger( vMap.get( "id" ) ) );

				flag = mallProductSpecificaService.saveOrUpdateProSpecifica( specifica );

				mallProductSpecificaService.saveOrUpSpecifica( nameId, valueId, erpNameId, erpValueId );
			    }

			    valueMaps.put( vMap.get( "id" ).toString(), erpValueId );
			    nameMaps.put( vMap.get( "id" ).toString(), vMap.get( "specValue" ) );
			}
		    }
		}
	    }

	    List< MallProductInventory > inventoryList = mallProductInventoryService.getInventByProductId( product.getId() );
	    if ( inventoryList != null && inventoryList.size() > 0 ) {
		for ( MallProductInventory inven : inventoryList ) {
		    int erpInvId = 0;
		    if ( CommonUtil.isNotEmpty( inven.getErpInvId() ) ) {
			erpInvId = inven.getErpInvId();
		    }
		    StringBuilder erpSpecValue = new StringBuilder();
		    StringBuilder erpSpecValueId = new StringBuilder();
		    String specIds = inven.getSpecificaIds();
		    for ( String ids : specIds.split( "," ) ) {
			if ( CommonUtil.isNotEmpty( ids ) ) {
			    if ( CommonUtil.isNotEmpty( erpSpecValueId ) ) {
				erpSpecValueId.append( "," );
				erpSpecValue.append( "," );
			    }
			    erpSpecValueId.append( valueMaps.get( ids ) );
			    erpSpecValue.append( nameMaps.get( ids ) );
			}
		    }
		    if ( CommonUtil.isEmpty( erpSpecValueId ) || CommonUtil.isEmpty( erpSpecValue.toString() ) ) {
			flag = false;
			break;
		    }
		    Map< String,Object > invParams = new HashMap<>();
		    if ( erpInvId > 0 ) {
			invParams.put( "id", erpInvId );
		    }
		    invParams.put( "ids", erpSpecValueId );//规格id组
		    invParams.put( "names", erpSpecValue.toString() );
		    invParams.put( "amount", inven.getInvNum() );
		    invParams.put( "price", CommonUtil.toDouble( inven.getInvPrice() ) * 100 );
		    invParams.put( "shopId", wxShopId );
		    invList.add( invParams );
		    if ( CommonUtil.isNotEmpty( erpSpecValueId ) && !erpSpecValueId.toString().equals( inven.getErpSpecvalueId() ) ) {
			MallProductInventory invens = new MallProductInventory();
			invens.setId( inven.getId() );
			invens.setErpSpecvalueId( erpSpecValueId.toString() );
			int count = mallProductInventoryDAO.updateById( invens );
			if ( count <= 0 ) {
			    flag = false;
			}
		    }
		}
	    }
	} else {
	    Map< String,Object > invParams = new HashMap<>();
	    if ( CommonUtil.isNotEmpty( product.getErpInvId() ) && product.getErpInvId() > 0 ) {
		invParams.put( "id", product.getErpInvId() );
	    }
	    invParams.put( "ids", "" );//规格id组
	    invParams.put( "names", "" );
	    invParams.put( "amount", product.getProStockTotal() );
	    invParams.put( "price", CommonUtil.toDouble( product.getProPrice() ) * 100 );
	    invParams.put( "shopId", wxShopId );
	    invList.add( invParams );
	}
	if ( flag ) {
	    Map< String,Object > productParams = new HashMap<>();
	    if ( CommonUtil.isNotEmpty( product.getErpProId() ) && product.getErpProId() > 0 ) {
		productParams.put( "id", product.getErpProId() );
	    }
	    productParams.put( "name", product.getProName() );
	    productParams.put( "norms", invList );

	    List< Map< String,Object > > productList = new ArrayList<>();
	    productList.add( productParams );

	    Map< String,Object > params = new HashMap<>();
	    params.put( "uId", product.getUserId() );
	    params.put( "uType", uType );
	    params.put( "uName", user.getName() );
	    params.put( "rootUid", userPId );
	    params.put( "pros", productList );

	    Map< String,Object > proParams = new HashMap<>();
	    proParams.put( "pros", com.alibaba.fastjson.JSONObject.toJSON( params ) );

	    com.alibaba.fastjson.JSONArray proArr = MallJxcHttpClientUtil.batchSave( proParams, true );
	    	/*System.out.println(proArr);*/
	    Map< Integer,Object > updateMap = new HashMap<>();//已经修改的商品
	    if ( proArr != null && proArr.size() > 0 ) {
		for ( Object object : proArr ) {
		    JSONObject proObj = JSONObject.parseObject( object.toString() );
		    int erpInvId = proObj.getInteger( "id" );

		    Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
		    inventoryWrapper.where( "erp_specvalue_id={0}", proObj.getString( "attrIds" ) );
		    MallProductInventory invens = new MallProductInventory();
		    invens.setErpInvId( erpInvId );
		    mallProductInventoryService.update( invens, inventoryWrapper );

		    if ( CommonUtil.isNotEmpty( proObj.get( "product" ) ) && CommonUtil.isEmpty( updateMap.get( product.getId() ) ) ) {
			JSONObject productObj = JSONObject.parseObject( proObj.get( "product" ).toString() );
			int erpProId = productObj.getInteger( "id" );
			MallProduct pro = new MallProduct();
			pro.setErpProId( erpProId );
			pro.setId( product.getId() );
			pro.setErpInvId( erpInvId );
			pro.setIsSyncErp( 1 );
			mallProductDAO.updateById( pro );
			updateMap.put( pro.getId(), 1 );
		    }
		}
	    } else {
		flag = false;
	    }
	}

	return flag;
    }

    @Override
    public List< Map< String,Object > > getErpInvByProId( int erpProId, int shopId ) throws Exception {
	MallStore store = mallStoreService.selectById( shopId );

	Map< String,Object > erpParams = new HashMap<>();
	erpParams.put( "shopId", store.getWxShopId() );
	erpParams.put( "productId", erpProId );
	com.alibaba.fastjson.JSONObject erpProObj = MallJxcHttpClientUtil.getInventoryById( erpParams, true );//查询进销存的库存

	if ( CommonUtil.isNotEmpty( erpProObj ) ) {
	    if ( CommonUtil.isNotEmpty( erpProObj.get( "attrs" ) ) ) {
		List< Map< String,Object > > specList = new ArrayList<>();
		JSONArray arr = JSONArray.parseArray( erpProObj.getString( "attrs" ) );
		if ( arr != null && arr.size() > 0 ) {
		    for ( Object object : arr ) {
			Map< String,Object > specMap = new HashMap<>();
			JSONObject attrObj = JSONObject.parseObject( object.toString() );
			int nums = CommonUtil.toIntegerByDouble( attrObj.getDouble( "amount" ) );
			specMap.put( "erpInvId", attrObj.getString( "id" ) );
			specMap.put( "invNum", nums );
			specList.add( specMap );
		    }
		}
		return specList;
	    }
	}
	return null;
    }

    @Override
    public int getInvNumsBySpecs( List< Map< String,Object > > specList, String invIds ) {
	int invNum = 0;
	for ( Map< String,Object > map2 : specList ) {
	    String erpInvId = map2.get( "erpInvId" ).toString();
	    int num = CommonUtil.toInteger( map2.get( "invNum" ) );
	    if ( erpInvId.equals( invIds ) ) {
		invNum = num;
		break;
	    }
	}
	return invNum;
    }

    @Override
    public void syncErpPro( int userId, HttpServletRequest request ) {
	int userPId = busUserService.getMainBusId( userId );//通过用户名查询主账号id
	Map< String,Object > params = new HashMap<>();
	params.put( "rootUid", userPId );
	params.put( "sync", 0 );
	logger.info( "同步erp商品参数：" + JSONObject.toJSONString( params ) );
	String proIds = "";
	boolean flag = true;
	com.alibaba.fastjson.JSONArray proArr = MallJxcHttpClientUtil.syncProductCheck( params, true );
	if ( proArr != null && proArr.size() > 0 ) {
	    for ( Object object : proArr ) {//循环商品
		if ( !flag ) {
		    break;
		}
		JSONObject proObj = JSONObject.parseObject( object.toString() );
		Map< String,Object > proParams = new HashMap<>();
		proParams.put( "erpProId", proObj.getInteger( "id" ) );
		List< MallProduct > productList = new ArrayList<>();
		List< MallProduct > list = selectByERPId( proParams );
		if ( list != null && list.size() > 0 ) {
		    for ( MallProduct mallProduct : list ) {
			MallProduct pro = getProductByErp( proObj, mallProduct );
			int count = mallProductDAO.updateById( pro );
			if ( count <= 0 ) {
			    flag = false;
			    break;
			}
			productList.add( pro );
		    }
		} else {
		    MallProduct pro = getProductByErp( proObj, null );
		    pro.setCreateTime( new Date() );
		    pro.setUserId( userId );
		    pro.setIsPublish( 0 );
		    pro.setCheckStatus( 1 );
		    pro.setShopId( 0 );
		    int count = mallProductDAO.insert( pro );
		    if ( count <= 0 ) {
			flag = false;
			break;
		    }
		    productList.add( pro );
		}

		if ( productList != null && productList.size() > 0 ) {
		    for ( MallProduct mallProduct : productList ) {
			JSONArray invenArr = proObj.getJSONArray( "attrs" );
			if ( invenArr != null && invenArr.size() > 0 ) {
			    for ( int j = 0; j < invenArr.size(); j++ ) {//循环库存
				Object object2 = invenArr.get( j );
				JSONObject invenObj = JSONObject.parseObject( object2.toString() );
				String specIds = "";
				int productId = mallProduct.getId();
				double invPrice = CommonUtil.toDouble( invenObj.getDouble( "retailPrice" ) ) / 100;
				int wxShopId = CommonUtil.toInteger( invenObj.getInteger( "shopId" ) );//门店id
				MallStore sto = new MallStore();
				sto.setWxShopId( wxShopId );
				List< MallStore > storeList = mallStoreService.findByShopId( wxShopId, 0 );
				if ( storeList != null && storeList.size() > 0 ) {
				    int storeId = CommonUtil.toInteger( storeList.get( 0 ).getId() );
				    Map< String,Object > erpProMap = new HashMap<>();
				    erpProMap.put( "erpProId", mallProduct.getErpProId() );
				    erpProMap.put( "shopId", storeId );
				    List< MallProduct > proList = selectByERPId( erpProMap );
				    if ( proList != null && proList.size() > 0 ) {
					for ( MallProduct mallProduct2 : proList ) {
					    productId = mallProduct2.getId();
					    mallProduct.setShopId( mallProduct2.getShopId() );
					}
				    }
				    if ( CommonUtil.isEmpty( mallProduct.getShopId() ) || mallProduct.getShopId() == 0 ) {
					MallProduct pro = new MallProduct();
					pro.setShopId( storeId );
					pro.setId( mallProduct.getId() );
					mallProduct.setProPrice( BigDecimal.valueOf( invPrice ) );
					mallProductDAO.updateById( pro );
					mallProduct.setShopId( storeId );
				    } else if ( !CommonUtil.toString( mallProduct.getShopId() ).equals( CommonUtil.toString( storeId ) ) ) {
					mallProduct.setId( null );
					mallProduct.setShopId( storeId );
					mallProduct.setProPrice( BigDecimal.valueOf( invPrice ) );
					mallProductDAO.insert( mallProduct );
					productId = mallProduct.getId();
					mallProduct.setShopId( storeId );
				    }
				} else {//没有创建对应的店铺
				    continue;
				}
				if ( CommonUtil.isNotEmpty( invenObj.get( "norms" ) ) ) {
				    JSONArray specArr = invenObj.getJSONArray( "norms" );
				    specIds = mallProductSpecificaService.saveOrUpdateBatchErpSpec( specArr, userId, productId );
				}
				if ( CommonUtil.isNotEmpty( specIds ) ) {
				    MallProductInventory inven = new MallProductInventory();
				    inven.setInvPrice( BigDecimal.valueOf( invPrice ) );
				    inven.setInvCode( CommonUtil.toString( invenObj.get( "customCoding" ) ) );
				    inven.setErpInvId( invenObj.getInteger( "id" ) );
				    inven.setErpSpecvalueId( invenObj.getString( "attrIds" ) );
				    inven.setProductId( productId );
				    inven.setSpecificaIds( specIds );
				    if ( j == 0 ) {
					inven.setIsDefault( 1 );
				    }
				    Map< String,Object > invParams = new HashMap<>();
				    invParams.put( "proId", productId );
				    invParams.put( "specificaIds", inven.getSpecificaIds() );
				    MallProductInventory proInven = mallProductInventoryService.selectInvNumByProId( invParams );
				    if ( CommonUtil.isNotEmpty( proInven ) ) {
					if ( CommonUtil.isNotEmpty( proInven.getId() ) ) {
					    inven.setId( proInven.getId() );
					}
				    }
				    int count = mallProductInventoryService.insertOrUpdateInven( inven );//新增或修改库存
				    if ( count <= 0 ) {
					flag = false;
					break;
				    }
				}
			    }
			}
			if ( CommonUtil.isNotEmpty( proIds ) ) {
			    if ( !proIds.contains( mallProduct.getErpProId() + "," ) ) {
				proIds += mallProduct.getErpProId() + ",";
			    }
			} else {
			    proIds += mallProduct.getErpProId() + ",";
			}
		    }
		}
	    }
	}
	if ( flag && CommonUtil.isNotEmpty( proIds ) ) {
	    Map< String,Object > syncParams = new HashMap<>();
	    syncParams.put( "productIds", proIds.substring( 0, proIds.length() - 1 ) );
	    syncParams.put( "rootUid", userPId );
	    MallJxcHttpClientUtil.syncCallback( syncParams, true );
	}
    }

    private MallProduct getProductByErp( JSONObject proObj, MallProduct product ) {
	MallProduct pro = new MallProduct();
	if ( CommonUtil.isNotEmpty( product ) ) {
	    pro.setId( product.getId() );
	}
	pro.setErpProId( proObj.getInteger( "id" ) );
	pro.setProName( proObj.getString( "name" ) );
	if ( proObj.getBoolean( "discount" ) ) {//是否打折
	    pro.setIsMemberDiscount( 1 );
	}
	if ( proObj.getBoolean( "returns" ) ) {
	    pro.setIsReturn( 1 );//是否允许退货
	} else {
	    pro.setIsReturn( 0 );
	}
	return pro;
    }

    private Map< String,Object > getProductPrice( Map< String,Object > productMap, double discount, boolean isPifa ) {
	double proPrice = CommonUtil.toDouble( productMap.get( "pro_price" ) );
	double proCostPrice = 0;
	if ( CommonUtil.isNotEmpty( productMap.get( "pro_cost_price" ) ) ) {
	    proCostPrice = CommonUtil.toDouble( productMap.get( "pro_cost_price" ) );
	}
	if ( CommonUtil.isNotEmpty( productMap.get( "is_specifica" ) ) ) {
	    if ( productMap.get( "is_specifica" ).toString().equals( "1" ) ) {
		//查询商品的规格价
		MallProductInventory inven = mallProductInventoryService.selectByIsDefault( CommonUtil.toInteger( productMap.get( "id" ) ) );
		if ( CommonUtil.isNotEmpty( inven ) ) {
		    proCostPrice = proPrice;
		    proPrice = CommonUtil.toDouble( inven.getInvPrice() );
		} else {
		    productMap.put( "is_specifica", 0 );
		}
	    }
	}
	if ( discount > 0 && discount < 1 ) {
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    double memberProPrice = CommonUtil.toDouble( df.format( proPrice * discount ) );
	    productMap.put( "member_price", memberProPrice );
	}
	//允许查询批发价
	if ( isPifa ) {
	    MallPifa pifa = mallPifaService.getPifaByProId( CommonUtil.toInteger( productMap.get( "id" ) ), CommonUtil.toInteger( productMap.get( "shop_id" ) ), 0 );
	    if ( CommonUtil.isNotEmpty( pifa ) ) {
		productMap.put( "wholesale_price", pifa.getPfPrice() );
	    }
	}

	productMap.put( "pro_price", proPrice );
	productMap.put( "pro_cost_price", proCostPrice );
	return productMap;
    }

    private Map< String,Object > syncProduct( Map< String,Object > params, BusUser user, MallProduct product, int shopId, int productId ) throws Exception {
	Map< String,Object > map = new HashMap<>();
	product.setShopId( shopId );
	product.setProSaleTotal( 0 );
	product.setIsPublish( 0 );
	product.setCheckStatus( -2 );
	product.setViewsNum( 0 );
	product.setId( null );
	product.setCreateTime( new Date() );
	product.setUserId( user.getId() );
	product.setEditTime( null );
	// 查询商品详情
	MallProductDetail detail = mallProductDetailService.selectByProductId( productId );

	Map< String,Object > imageParam = new HashMap<>();
	imageParam.put( "assId", productId );
	imageParam.put( "assType", 1 );
	// 查询商品图片
	List< MallImageAssociative > imageList = mallImageAssociativeService.getParamByProductId( imageParam );
	// 查询商品规格

	List< MallProductSpecifica > specList = mallProductSpecificaService.selectByProductId( productId );
	// 查询商品库存
	List< MallProductInventory > invenList = mallProductInventoryService.getInventByProductId( productId );
	//查询商品参数
	List< MallProductParam > paramList = mallProductParamService.getParamByProductId( productId );

	int count = mallProductDAO.insert( product );//copy商品信息
	if ( CommonUtil.isNotEmpty( product.getId() ) && count > 0 ) {
	    int newId = product.getId();
	    //同步商品详情
	    if ( CommonUtil.isNotEmpty( detail ) ) {
		if ( detail.getId() > 0 ) {
		    detail.setId( null );
		    detail.setProductId( newId );
		    mallProductDetailService.insert( detail );
		}
	    }
	    //同步商品分组
	    if ( CommonUtil.isNotEmpty( params.get( "groupList" ) ) ) {
		List< MallProductGroup > groupList = JSONArray.parseArray( params.get( "groupList" ).toString(), MallProductGroup.class );
		if ( groupList != null ) {
		    for ( MallProductGroup mallProductGroup : groupList ) {
			mallProductGroup.setProductId( newId );
			mallProductGroup.setShopId( shopId );
			mallProductGroupDAO.insert( mallProductGroup );
		    }
		}
	    } else {
		params.put( "proId", productId );
		mallGroupService.copyProductGroupByProduct( params, product );
	    }
	    //同步商品图片
	    if ( CommonUtil.isNotEmpty( imageList ) ) {
		if ( imageList.size() > 0 ) {
		    for ( MallImageAssociative images : imageList ) {
			images.setAssId( newId );
			images.setId( null );
			mallImageAssociativeService.insert( images );
		    }
		}
	    }

	    if ( product.getIsSpecifica().toString().equals( "1" ) ) {//商品存在规格值
		Map< String,Object > specMap = new HashMap<>();
		// 批量同步商品规格
		if ( CommonUtil.isNotEmpty( specList ) ) {
		    specMap = mallProductSpecificaService.copyProductSpecifica( specList, newId, shopId, user.getId() );
		}
		// 批量同步商品库存
		if ( CommonUtil.isNotEmpty( invenList ) ) {
		    mallProductInventoryService.copyProductInven( invenList, specMap, newId );
		}
	    }

	    // 批量同步商品参数
	    if ( CommonUtil.isNotEmpty( paramList ) ) {
		mallProductParamService.copyProductParam( paramList, newId, shopId, user.getId() );
	    }

	    map.put( "code", 1 );
	} else {
	    map.put( "msg", "同步商品信息失败，请稍后重试" );
	    map.put( "code", -1 );
	}
	return map;
    }

    @Override
    public List< Map > selectMemberType( int userId ) {
	return memberService.findBuyGradeType( userId );
    }

    private Map< String,Object > getFlowRecord( MallProduct product, BusFlowInfo flow ) {
	FenbiFlowRecord flowRecord = new FenbiFlowRecord();
	flowRecord.setBusUserId( product.getUserId() );//用户ID
	flowRecord.setRecType( 2 );//1：粉币 2：流量
	flowRecord.setRecCount( CommonUtil.toDouble( product.getProStockTotal() ) );//总数量
	flowRecord.setRecDesc( "商家发布流量充值（商城）" );//冻结描述
	flowRecord.setRecFreezeType( 100 );//冻结类型
	flowRecord.setRecFkId( product.getId() );//外键id， 商品id
	flowRecord.setFlowType( flow.getType() );//流量类型
	flowRecord.setFlowId( flow.getId() );//流量表ID
	flowRecord.setId( product.getFlowRecordId() );
	Map< String,Object > resultMap = fenBiFlowService.saveFenbiFlowRecord( flowRecord );
	if ( CommonUtil.isNotEmpty( resultMap.get( "id" ) ) && CommonUtil.isNotEmpty( product.getId() ) ) {
	    MallProduct pro = new MallProduct();
	    pro.setId( product.getId() );
	    pro.setFlowRecordId( CommonUtil.toInteger( resultMap.get( "id" ) ) );
	    mallProductDAO.updateById( pro );
	} else {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), resultMap.get( "errorMsg" ).toString() );
	}
	return resultMap;
    }

    @Override
    public List< BusFlow > selectCountByFlowIds( int userId ) {
	List< BusFlow > flowList = fenBiFlowService.getBusFlowsByUserId( userId );
	if ( flowList == null || flowList.size() == 0 ) {
	    return null;
	}
	List< Integer > list = new ArrayList<>();
	if ( flowList != null && flowList.size() > 0 ) {
	    for ( BusFlow busFlow : flowList ) {
		list.add( busFlow.getId() );
	    }
	}
	Wrapper< MallProduct > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "id,flow_id" );
	wrapper.where( "is_delete=0" ).in( "flow_id", list );

	List< Map< String,Object > > productList = mallProductDAO.selectMaps( wrapper );
	List< BusFlow > newFlowList = new ArrayList<>();
	if ( flowList != null && flowList.size() > 0 && productList != null && productList.size() > 0 ) {

	    for ( BusFlow busFlow : flowList ) {
		boolean flag = true;
		for ( Map< String,Object > productMap : productList ) {
		    if ( productMap.get( "flow_id" ).toString().equals( busFlow.getId().toString() ) ) {
			flag = false;
		    }
		}
		if ( flag ) {
		    newFlowList.add( busFlow );
		}
	    }
	    flowList = newFlowList;
	}
	return flowList;
    }

    @Override
    public List< MallProduct > selectByERPId( Map< String,Object > params ) {
	Wrapper< MallProduct > productWrapper = new EntityWrapper<>();
	productWrapper.where( "erp_pro_id ={0}", params.get( "erpProId" ) );
	if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
	    productWrapper.where( "shop_id = {0}", params.get( "shopId" ) );
	}
	return mallProductDAO.selectList( productWrapper );
    }

    @Override
    public void syncAllProduct( BusUser user, HttpServletRequest request ) {
	int userPId = busUserService.getMainBusId( user.getId() );//通过用户名查询主账号id
	int uType = 1;//用户类型 1总账号  0子账号
	if ( user.getId() != userPId ) {
	    uType = 0;
	}
	Map< String,Object > params;
	List< Map< String,Object > > pros;
	List< Map< String,Object > > norms;

	List< Map< String,Object > > productList = mallProductDAO.selectProByUserIdGroupName( user.getId() );
	System.out.println( "同步所有商品至erp 数量=" + productList.size() );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > proMap : productList ) {
		String is_specifica = proMap.get( "is_specifica" ).toString();
		pros = new ArrayList<>();
		norms = new ArrayList<>();
		String productIds = proMap.get( "id" ).toString();

		if ( is_specifica.equals( "1" ) ) {
		    //封装库存
		    String shopIds = proMap.get( "shop_id" ).toString();
		    String[] shopId = shopIds.split( "," );
		    Integer productId = null;
		    if ( shopId.length > 0 ) {
			for ( String aShopId : shopId ) {
			    pros = new ArrayList<>();
			    norms = new ArrayList<>();

			    List< Map< String,Object > > spec = queryInventoryByproIdShopId( productIds, CommonUtil.toInteger( aShopId ) );
			    norms.addAll( spec );

			    Map< String,Object > product = new HashMap<>();
			    if ( productId != null ) {
				product.put( "id", productId );
			    }
			    product.put( "name", proMap.get( "pro_name" ) );
			    product.put( "norms", norms );
			    pros.add( product );

			    params = new HashMap<>();
			    params.put( "uId", user.getId() );
			    params.put( "uType", uType );
			    params.put( "uName", user.getName() );
			    params.put( "rootUid", userPId );
			    params.put( "pros", pros );
			    System.out.println( params );
			    productId = updateInvAndPro( params, productIds, is_specifica, CommonUtil.toInteger( aShopId ) );
			}
		    }
		} else {
		    MallStore shop = mallStoreService.selectById( CommonUtil.toInteger( proMap.get( "shop_id" ) ) );
		    Map< String,Object > spec = new HashMap<>();
		    spec.put( "ids", "" );//规格id组
		    spec.put( "names", "" );
		    spec.put( "shopId", shop.getWxShopId() );
		    spec.put( "amount", proMap.get( "amount" ) );
		    spec.put( "price", proMap.get( "price" ) );
		    norms.add( spec );

		    Map< String,Object > product = new HashMap<>();
		    product.put( "name", proMap.get( "pro_name" ) );
		    product.put( "norms", norms );
		    pros.add( product );

		    params = new HashMap<>();
		    params.put( "uId", user.getId() );
		    params.put( "uType", uType );
		    params.put( "uName", user.getName() );
		    params.put( "rootUid", userPId );
		    params.put( "pros", pros );
		    System.out.println( params );
		    updateInvAndPro( params, productIds, is_specifica, null );
		}
	    }
	}
    }

    @Override
    public boolean diffProductStock( MallProduct pro, MallOrderDetail detail, MallOrder order ) {
	Map< String,Object > proMap = new HashMap<>();
	Integer total = ( pro.getProStockTotal() - detail.getDetProNum() );
	Integer saleNum = ( pro.getProSaleTotal() + detail.getDetProNum() );
	proMap.put( "total", total );
	proMap.put( "saleNum", saleNum );
	proMap.put( "proId", detail.getProductId() );
	Map< String,Object > productParams = new HashMap<>();
	productParams.put( "type", 2 );
	productParams.put( "product_id", detail.getProductId() );
	productParams.put( "pro_num", detail.getDetProNum() );
	mallProductDAO.updateProductStock( productParams );
	if ( null != pro.getIsSpecifica() && CommonUtil.isNotEmpty( pro.getIsSpecifica() ) ) {
	    if ( pro.getIsSpecifica() == 1 ) {//该商品存在规格
		if ( order.getOrderType() != 7 ) {
		    String[] specifica = ( detail.getProductSpecificas() ).split( "," );
		    StringBuilder ids = new StringBuilder();
		    for ( String aSpecifica : specifica ) {
			if ( CommonUtil.isNotEmpty( aSpecifica ) ) {
			    proMap.put( "valueId", aSpecifica );
			    MallProductSpecifica proSpec = mallProductSpecificaService.selectByNameValueId( proMap );
			    ids.append( proSpec.getId() ).append( "," );
			}
		    }
		    proMap.put( "specificaIds", ids.substring( 0, ids.length() - 1 ) );
		    diffProductInvStockNum( proMap, detail.getDetProNum() );
		} else if ( CommonUtil.isNotEmpty( detail.getProSpecStr() ) ) {//批发商品
		    JSONObject specObj = JSONObject.parseObject( detail.getProSpecStr() );
		    for ( String key : specObj.keySet() ) {
			JSONObject valueObj = specObj.getJSONObject( key );

			List< MallProductSpecifica > specificaList = mallProductSpecificaService.selectByValueIds( detail.getProductId(), key.split( "," ) );
			StringBuilder specIds = new StringBuilder();
			if ( specificaList != null && specificaList.size() > 0 ) {
			    for ( MallProductSpecifica specifica : specificaList ) {
				specIds.append( specifica.getId() ).append( "," );
			    }
			    proMap.put( "specificaIds", specIds.substring( 0, specIds.length() - 1 ) );
			    diffProductInvStockNum( proMap, valueObj.getInteger( "num" ) );
			}

		    }
		}

	    }
	}
	return true;
    }

    private void diffProductInvStockNum( Map< String,Object > proMap, int proNum ) {
	MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );//根据商品规格id查询商品库存
	mallProductInventoryService.updateProductInventory( proInv, proNum, 2 );//修改规格的库存
    }

    /**
     * 同步商品库存
     *
     * @param params
     * @param productIds
     * @param is_specifica
     * @param shopId
     *
     * @return
     */
    private int updateInvAndPro( Map< String,Object > params, String productIds, String is_specifica, Integer shopId ) {

	Map< String,Object > proParams = new HashMap<>();
	proParams.put( "pros", com.alibaba.fastjson.JSONObject.toJSON( params ) );
	System.out.println( proParams );
	int erpProId = 0;
	com.alibaba.fastjson.JSONArray proArr = MallJxcHttpClientUtil.batchSave( proParams, true );
	if ( proArr != null && proArr.size() > 0 ) {
	    for ( Object object : proArr ) {
		net.sf.json.JSONObject proObj = net.sf.json.JSONObject.fromObject( object );
		net.sf.json.JSONObject productObj = net.sf.json.JSONObject.fromObject( proObj.get( "product" ) );
		int erpInvId = proObj.getInt( "id" );
		erpProId = productObj.getInt( "id" );
		String attrIds = proObj.getString( "attrIds" );

		if ( is_specifica.equals( "1" ) ) {
		    Wrapper< MallProductInventory > keywordWrapper = new EntityWrapper<>();
		    String sql1 = " erp_specvalue_id='" + attrIds + "' and product_id in(SELECT id FROM t_mall_product t WHERE t.shop_id=" + shopId + " AND t.id IN(" + productIds
				    + ") )";
		    keywordWrapper.where( sql1 );
		    MallProductInventory productInventory = new MallProductInventory();
		    productInventory.setErpInvId( erpInvId );
		    mallProductInventoryDAO.update( productInventory, keywordWrapper );
		}

		Wrapper< MallProduct > keywordWrapper = new EntityWrapper<>();
		keywordWrapper.where( "id in(" + productIds + ") " );
		MallProduct product = new MallProduct();
		if ( is_specifica.equals( "1" ) ) {
		    product.setErpInvId( erpInvId );
		}
		product.setErpProId( erpProId );
		product.setIsSyncErp( 1 );
		mallProductDAO.update( product, keywordWrapper );

	    }
	}

	return erpProId;
    }

    /**
     * 根据门店id,商品Id列表查询库存
     */
    private List< Map< String,Object > > queryInventoryByproIdShopId( String productIds, Integer shopId ) {

	MallStore shop = mallStoreService.selectById( shopId );
	List< MallProductInventory > invList = mallProductInventoryDAO.selectInvenByProIdsOrShopId( productIds, shopId );
	Map< String,Map< String,Object > > invs = new HashMap<>();
	if ( invList != null && invList.size() > 0 ) {
	    for ( MallProductInventory invMap : invList ) {
		Map< String,Object > inv = new HashMap<>();
		String[] specIds = invMap.getSpecificaIds().split( "," );
		String ids = "";
		String names = "";
		Wrapper< MallProductSpecifica > wrapper = new EntityWrapper<>();
		wrapper.where( "  is_delete = 0" ).in( "id", specIds );
		wrapper.orderBy( "sort" );
		List< MallProductSpecifica > specList = mallProductSpecificaDao.selectList( wrapper );
		for ( MallProductSpecifica specifica : specList ) {
		    if ( CommonUtil.isNotEmpty( ids ) ) {
			ids += ",";
		    }
		    ids += specifica.getErpSpecvalueId();
		    if ( CommonUtil.isNotEmpty( names ) ) {
			names += ",";
		    }
		    names += specifica.getSpecificaValue();
		}
		//添加 库存表erp规格Id
		MallProductInventory inventory = new MallProductInventory();
		inventory.setId( CommonUtil.toInteger( invMap.getId() ) );
		inventory.setErpSpecvalueId( ids );
		mallProductInventoryDAO.updateById( inventory );

		if ( invs.containsKey( ids ) ) {//是否存在
		    inv = invs.get( ids );
		    if ( CommonUtil.toDouble( invMap.getInvPrice() ) > CommonUtil.toDouble( inv.get( "price" ) ) ) {//取最大的价格
			inv.put( "price", invMap.getInvPrice() );
		    }
		    inv.put( "amount", CommonUtil.toDouble( invMap.getInvNum() ) + CommonUtil.toDouble( inv.get( "amount" ) ) );//库存累加

		} else {
		    inv.put( "id", "" );
		    inv.put( "shopId", shop.getWxShopId() );
		    inv.put( "ids", ids );
		    inv.put( "names", names );
		    inv.put( "amount", invMap.getInvNum() );
		    inv.put( "price", invMap.getInvPrice() );
		}
		invs.put( ids, inv );
	    }
	}
	List< Map< String,Object > > reulst = new ArrayList<>();

	for ( Map.Entry< String,Map< String,Object > > entry : invs.entrySet() ) {
	    reulst.add( entry.getValue() );
	}

	return reulst;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean newUpdateProduct( Map< String,Object > params, BusUser user, HttpServletRequest request ) throws Exception {
	MallProduct product = null;
	// 修改商品信息
	if ( !CommonUtil.isEmpty( params.get( "product" ) ) ) {
	    product = JSONObject.parseObject( params.get( "product" ).toString(), MallProduct.class );
	    product.setEditTime( new Date() );
	    product.setIsPublish( 0 );
	    String key = Constants.REDIS_KEY + "proViewNum";
	    if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
		JedisUtil.map( key, product.getId().toString(), product.getViewsNum().toString() );
	    }

	    if ( product.getProTypeId() == 1 ) {// 虚拟商品，清空预售设置
		product.setIsPresell( 0 );
		product.setProPresellEnd( "" );
		product.setProDeliveryStart( "" );
		product.setProDeliveryEnd( "" );
	    }
	    if ( CommonUtil.isEmpty( product.getChangeIntegral() ) ) {
		product.setChangeIntegral( 0 );
	    }
	    if ( CommonUtil.isEmpty( product.getReturnDay() ) ) {
		product.setReturnDay( 0 );
	    }
	    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getFlowId() > 0 ) {
		    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );
		    if ( CommonUtil.isNotEmpty( flow ) ) {
			Map< String,Object > map = getFlowRecord( product, flow );
			if ( CommonUtil.isNotEmpty( map ) ) {
			    if ( map.get( "code" ).toString().equals( "-1" ) ) {
				return false;
			    }
			}
		    }
		}
	    }
	    product.setIsSyncErp( 0 );
	    product.setCheckStatus( 1 );
	    product.setIsPlatformCheck( 0 );//平台审核 未审核
	    // 修改商品信息
	    mallProductDAO.updateById( product );
	}

	// 修改商品详细
	if ( !CommonUtil.isEmpty( params.get( "detail" ) ) ) {
	    MallProductDetail detail = JSONObject.parseObject( params.get( "detail" ).toString(), MallProductDetail.class );
	    if ( detail != null ) {
		if ( CommonUtil.isNotEmpty( detail.getProductDetail() ) && CommonUtil.isNotEmpty( detail.getProductIntrodu() ) && CommonUtil
				.isNotEmpty( detail.getProductMessage() ) ) {
		    if ( CommonUtil.isNotEmpty( detail.getId() ) ) {
			mallProductDetailService.updateById( detail );
		    } else {
			detail.setProductId( product.getId() );
			mallProductDetailService.insert( detail );
		    }
		}
	    }
	}

	if ( CommonUtil.isEmpty( params.get( "imageList" ) ) ) {
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品图片不能为空" );
	}
	// 添加或修改图片
	mallImageAssociativeService.newInsertUpdBatchImage( params, product.getId(), 1 );

	//查询商品是否存在于团购中
	MallGroupBuy groupBuy = new MallGroupBuy();
	groupBuy.setProductId( product.getId() );
	List< MallGroupBuy > buyList = mallGroupBuyDAO.selectGroupByProId( groupBuy );
	boolean flag = true;//允许修改商品规格
	if ( buyList != null && buyList.size() > 0 ) {//商品存在于团购中不允许修改商品规格
	    flag = false;
	}

	Map< String,Object > specMap = new HashMap<>();

	if ( product.getIsSpecifica() == 1 && CommonUtil.isEmpty( params.get( "speList" ) ) ) {
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品规格不能为空" );
	}
	// 批量添加或修改商品规格
	if ( CommonUtil.isNotEmpty( params.get( "speList" ) ) ) {
	    specMap = mallProductSpecificaService.newSaveOrUpdateBatch( params.get( "speList" ), product.getId(), flag );
	}

	// 批量添加商品库存
	if ( !CommonUtil.isEmpty( params.get( "invenList" ) ) ) {
	    mallProductInventoryService.newSaveOrUpdateBatch( specMap, params.get( "invenList" ), product.getId() );
	}

	// 批量添加商品参数
	if ( !CommonUtil.isEmpty( params.get( "paramsList" ) ) ) {
	    mallProductParamService.newSaveOrUpdateBatch( params.get( "paramsList" ), product.getId(), true );
	}

	// 批量添加商品分组
	if ( !CommonUtil.isEmpty( params.get( "groupList" ) ) ) {
	    mallProductGroupService.saveOrUpdate( params.get( "groupList" ), product.getId() );
	} else {
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "商品分组不能为空" );
	}

	int userPId = busUserService.getMainBusId( user.getId() );//通过用户名查询主账号id
	long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有
	if ( isJxc == 1 ) {
	    boolean flags = saveProductByErp( product, user, userPId );
	    if ( !flags ) {
		JedisUtil.rPush( Constants.REDIS_KEY + "is_no_up_erp", product.getId().toString() );
	    }
	}

	return true;
    }

    @Override
    public PageUtil selectWaitCheckList( Map< String,Object > param ) {
	List< Map< String,Object > > productList = null;

	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	int pageSize = CommonUtil.isEmpty( param.get( "pageSize" ) ) ? 15 : CommonUtil.toInteger( param.get( "pageSize" ) );

	param.put( "curPage", curPage );
	Wrapper< MallProduct > wrapper = new EntityWrapper<>();
	wrapper.where( "is_delete=0 and is_mall_show=0 and check_status =1  and is_platform_check = 0" );
	if ( CommonUtil.isNotEmpty( param.get( "userIds" ) ) ) {
	    wrapper.in( "user_id", param.get( "userIds" ).toString() );
	}
	int count = mallProductDAO.selectCount( wrapper );

	PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    if ( CommonUtil.isNotEmpty( param.get( "userIds" ) ) ) {
		param.put( "userIds", param.get( "userIds" ).toString().split( "," ) );
	    }
	    productList = mallProductDAO.selectWaitCheckList( param );// 查询商品总数
	}
	page.setSubList( productList );
	return page;
    }

}
