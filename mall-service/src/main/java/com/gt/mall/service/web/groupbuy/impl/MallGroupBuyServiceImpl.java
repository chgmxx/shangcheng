package com.gt.mall.service.web.groupbuy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyPriceDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.seller.MallSellerJoinProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyPriceService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.groupbuy.MallGroupJoinService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PageUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 团购表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallGroupBuyServiceImpl extends BaseServiceImpl< MallGroupBuyDAO,MallGroupBuy > implements MallGroupBuyService {

    private Logger log = Logger.getLogger( MallGroupBuyServiceImpl.class );

    @Autowired
    private MallGroupBuyDAO             mallGroupBuyDAO;
    @Autowired
    private MallGroupBuyPriceDAO        mallGroupBuyPriceDAO;
    @Autowired
    private MallGroupBuyPriceService    mallGroupBuyPriceService;
    @Autowired
    private MallGroupJoinDAO            mallGroupJoinDAO;
    @Autowired
    private MallIntegralDAO             mallIntegralDAO;
    @Autowired
    private MallSellerJoinProductDAO    mallSellerJoinProductDAO;
    @Autowired
    private MallSearchKeywordService    mallSearchKeywordService;
    @Autowired
    private MallOrderDetailDAO          mallOrderDetailDAO;
    @Autowired
    private MallOrderDAO                mallOrderDAO;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallStoreDAO                mallStoreDAO;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private MallPageService             mallPageService;
    @Autowired
    private MallGroupJoinService        mallGroupJoinService;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallProductInventoryService mallProductInventoryService;

    @Override
    public PageUtil selectGroupBuyByShopId( Map< String,Object > params, int userId, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallGroupBuyDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mGroupBuy/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断团购是否有数据
	    List< MallGroupBuy > groupBuyList = mallGroupBuyDAO.selectByPage( params );
	    if ( groupBuyList != null && groupBuyList.size() > 0 ) {
		for ( MallGroupBuy buy : groupBuyList ) {
		    for ( Map< String,Object > shopMaps : shoplist ) {
			int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			if ( shop_id == buy.getShopId() ) {
			    buy.setShopName( shopMaps.get( "sto_name" ).toString() );
			    break;
			}
		    }
		}
	    }
	    page.setSubList( groupBuyList );
	}

	return page;
    }

    @Override
    public Map< String,Object > selectGroupBuyById( Integer id ) {
	Map< String,Object > map = mallGroupBuyDAO.selectByGroupBuyId( id );

	int groupBuyId = CommonUtil.toInteger( map.get( "id" ) );
	//        List<MallGroupBuyPrice> priceList = mallGroupBuyPriceDAO.selectPriceByGroupId(groupBuyId);
	Wrapper< MallGroupBuyPrice > groupWrapper = new EntityWrapper<>();
	groupWrapper.where( "group_buy_id = {0} and is_delete = 0", groupBuyId );
	List< MallGroupBuyPrice > priceList = mallGroupBuyPriceDAO.selectList( groupWrapper );

	map.put( "priceList", priceList );
	return map;
    }

    @Override
    public int editGroupBuy( Map< String,Object > groupMap, int userId ) {
	int num = 0;
	boolean flag = false;
	int code = -1;
	int status = 0;
	if ( CommonUtil.isNotEmpty( groupMap.get( "groupBuy" ) ) ) {
	    MallGroupBuy groupBuy = JSONObject.toJavaObject( JSONObject.parseObject( groupMap.get( "groupBuy" ).toString() ), MallGroupBuy.class );
	    groupBuy.setGName( CommonUtil.urlEncode( groupBuy.getGName() ) );
	    if ( CommonUtil.isEmpty( groupBuy.getShopId() ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "店铺不能为空" );
	    }
	    if ( CommonUtil.isEmpty( groupBuy.getProductId() ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "商品不能为空" );
	    }
	    if ( CommonUtil.isEmpty( groupBuy.getGName() ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "活动名称不能为空" );
	    }
	    if ( CommonUtil.isEmpty( groupBuy.getGStartTime() ) || CommonUtil.isEmpty( groupBuy.getGEndTime() ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "开始或结束时间不能为空" );
	    }

	    //判断选择的商品是否已经存在未开始和进行中的团购中
	    List< MallGroupBuy > buyList = mallGroupBuyDAO.selectGroupByProId( groupBuy );
	    if ( buyList == null || buyList.size() == 0 ) {
		groupBuy.setUserId( userId );
		if ( CommonUtil.isNotEmpty( groupBuy.getId() ) ) {
		    //判断本商品是否正在团购中
		    MallGroupBuy buy = mallGroupBuyDAO.selectGroupByIds( groupBuy.getId() );

		    if ( buy.getStatus() == 1 && CommonUtil.isNotEmpty( buy.getJoinId() ) && !buy.getProductId().toString()
				    .equals( groupBuy.getProductId().toString() ) ) {//正在进行团购的商品不能修改
			code = -2;
			status = buy.getStatus();
		    } else {
			MallGroupBuy buyOld = mallGroupBuyDAO.selectById( groupBuy.getId() );
			if ( !buyOld.getProductId().equals( groupBuy.getProductId() ) ) {//用户更换了商品
			    flag = true;
			}
			num = mallGroupBuyDAO.updateById( groupBuy );
		    }
		} else {
		    groupBuy.setCreateTime( new Date() );
		    num = mallGroupBuyDAO.insert( groupBuy );
		}
		if ( CommonUtil.isNotEmpty( groupBuy.getId() ) && status != 1 ) {
		    mallGroupBuyPriceService.editGroupBuyPrice( groupMap, groupBuy.getId(), flag );
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

    @Override
    public boolean deleteGroupBuy( MallGroupBuy groupBuy ) {
	int num = mallGroupBuyDAO.updateById( groupBuy );
	return num > 0;
    }

    @Override
    public PageUtil selectProByGroup( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallGroupBuyDAO.selectCountProByGroup( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mGroupBuy/getProductByGroup.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量
	String defaultProId = "";
	if ( CommonUtil.isNotEmpty( params.get( "defaultProId" ) ) ) {
	    defaultProId = params.get( "defaultProId" ).toString();
	}

	if ( count > 0 ) {// 判断商品是否有数据
	    List< Map< String,Object > > productList = mallGroupBuyDAO.selectProByGroup( params );
	    List< Map< String,Object > > list = new ArrayList<>();
	    if ( productList != null && productList.size() > 0 ) {
		List< Integer > products = new ArrayList<>();
		for ( Map< String,Object > map : productList ) {
		    products.add( CommonUtil.toInteger( map.get( "id" ) ) );
		}
		List< Map< String,Object > > groupLIst = mallGroupBuyDAO.selectGroupsByProId( products );
		List< Map< String,Object > > seckillList = mallGroupBuyDAO.selectSeckillByProId( products );
		List< Map< String,Object > > auctionList = mallGroupBuyDAO.selectAuctionByProId( products );
		List< Map< String,Object > > presaleList = mallGroupBuyDAO.selectPresaleByProId( products );
		List< Map< String,Object > > pifaList = mallGroupBuyDAO.selectpifaByProId( products );
		for ( Map< String,Object > map : productList ) {
		    String pId = map.get( "id" ).toString();
		    int groupStatus = -1;
		    int seckillStatus = -1;
		    int auctionStatus = -1;
		    int presaleStatus = -1;
		    int pifaStatus = -1;
		    int isCommission = 0;
		    int isIntegral = 0;
		    if ( CommonUtil.isNotEmpty( params.get( "isCommission" ) ) ) {
			isCommission = CommonUtil.toInteger( params.get( "isCommission" ) );
		    }
		    if ( CommonUtil.isNotEmpty( params.get( "isIntegral" ) ) ) {
			isIntegral = CommonUtil.toInteger( params.get( "isIntegral" ) );
		    }
		    if ( isCommission == 0 && isIntegral == 0 ) {
			if ( groupLIst != null && groupLIst.size() > 0 ) {
			    for ( Map< String,Object > group : groupLIst ) {
				if ( group.get( "product_id" ).equals( map.get( "id" ) ) ) {
				    Map groupMap = JSONObject.parseObject( JSON.toJSONString( group ), Map.class );
				    if ( CommonUtil.isNotEmpty( groupMap.get( "g_start_time" ) ) ) {
					String startTime = groupMap.get( "g_start_time" ).toString();
					String endTime = groupMap.get( "g_end_time" ).toString();
					groupStatus = isAddPro( startTime, endTime, defaultProId, pId );
				    }
				    break;
				}
			    }
			}
			if ( seckillList != null && seckillList.size() > 0 ) {
			    for ( Map< String,Object > seckill : seckillList ) {
				if ( seckill.get( "product_id" ).equals( map.get( "id" ) ) ) {
				    Map seckillMap = JSONObject.parseObject( JSON.toJSONString( seckill ), Map.class );
				    if ( CommonUtil.isNotEmpty( seckillMap.get( "s_start_time" ) ) ) {
					String startTime = seckillMap.get( "s_start_time" ).toString();
					String endTime = seckillMap.get( "s_end_time" ).toString();
					seckillStatus = isAddPro( startTime, endTime, defaultProId, pId );
				    }
				    break;
				}
			    }
			}
			if ( auctionList != null && auctionList.size() > 0 ) {
			    for ( Map< String,Object > auction : auctionList ) {
				if ( auction.get( "product_id" ).equals( map.get( "id" ) ) ) {
				    Map auctionMap = JSONObject.parseObject( JSON.toJSONString( auction ), Map.class );
				    if ( CommonUtil.isNotEmpty( auctionMap.get( "auc_start_time" ) ) ) {
					String startTime = auctionMap.get( "auc_start_time" ).toString();
					String endTime = auctionMap.get( "auc_end_time" ).toString();
					auctionStatus = isAddPro( startTime, endTime, defaultProId, pId );
				    }
				    break;
				}
			    }
			}
			if ( presaleList != null && presaleList.size() > 0 ) {
			    for ( Map< String,Object > presale : presaleList ) {
				if ( presale.get( "product_id" ).equals( map.get( "id" ) ) ) {
				    Map presaleMap = JSONObject.parseObject( JSON.toJSONString( presale ), Map.class );
				    if ( CommonUtil.isNotEmpty( presaleMap.get( "sale_start_time" ) ) ) {
					String startTime = presaleMap.get( "sale_start_time" ).toString();
					String endTime = presaleMap.get( "sale_end_time" ).toString();
					presaleStatus = isAddPro( startTime, endTime, defaultProId, pId );
				    }
				    break;
				}
			    }
			}
			if ( pifaList != null && pifaList.size() > 0 ) {
			    for ( Map< String,Object > pifa : pifaList ) {
				if ( pifa.get( "product_id" ).equals( map.get( "id" ) ) ) {
				    Map pifaMap = JSONObject.parseObject( JSON.toJSONString( pifa ), Map.class );
				    if ( CommonUtil.isNotEmpty( pifaMap.get( "pf_start_time" ) ) ) {
					String startTime = pifaMap.get( "pf_start_time" ).toString();
					String endTime = pifaMap.get( "pf_end_time" ).toString();
					pifaStatus = isAddPro( startTime, endTime, defaultProId, pId );
				    }
				    break;
				}
			    }
			}
		    } else if ( isCommission != 0 ) {
			Map< String,Object > sellerMap = new HashMap<>();
			sellerMap.put( "userId", params.get( "userId" ) );
			sellerMap.put( "productId", pId );
			sellerMap.put( "shopId", params.get( "shopId" ) );
			List< MallSellerJoinProduct > jionList = mallSellerJoinProductDAO.selectProductByIsJoin( sellerMap );
			if ( jionList != null && jionList.size() > 0 ) {
			    map.put( "sellerStatus", 1 );
			}
		    } else if ( isIntegral != 0 ) {
			Map< String,Object > integralMap = new HashMap<>();
			integralMap.put( "userId", params.get( "userId" ) );
			integralMap.put( "productId", pId );
			integralMap.put( "shopId", params.get( "shopId" ) );
			List< MallIntegral > integralList = mallIntegralDAO.selectByIntegral( integralMap );
			if ( integralList != null && integralList.size() > 0 ) {
			    map.put( "integralStatus", 1 );
			}
		    }

		    map.put( "groupStatus", groupStatus );
		    map.put( "seckillStatus", seckillStatus );
		    map.put( "auctionStatus", auctionStatus );
		    map.put( "presaleStatus", presaleStatus );
		    map.put( "pifaStatus", pifaStatus );

		    list.add( map );
		}
	    }
	    page.setSubList( list );
	}

	return page;
    }

    /**
     * 判断商品是否已经加入了团购和秒杀
     */
    private int isAddPro( String startTime, String endTime, String defaultProId, String pId ) {
	int groupStatus = -1;
	Date startDate = DateTimeKit.parse( startTime, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	Date endDate = DateTimeKit.parse( endTime, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	Date now = new Date();
	if ( ( startDate.getTime() <= now.getTime() && now.getTime() < endDate.getTime() ) || ( startDate.getTime() > now.getTime() && endDate.getTime() > now.getTime() ) ) {
	    if ( !defaultProId.equals( "" ) ) {
		if ( !defaultProId.equals( pId ) ) {
		    groupStatus = 1;
		}
	    } else {
		groupStatus = 1;
	    }
	}
	return groupStatus;
    }

    @Override
    public List< Map< String,Object > > getGroupBuyAll( Member member, Map< String,Object > maps ) {
	int shopid = 0;
	if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
	    shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
	}
	if ( CommonUtil.isNotEmpty( maps.get( "proName" ) ) && CommonUtil.isNotEmpty( member ) ) {
	    mallSearchKeywordService.insertSeachKeyWord( member.getId(), shopid, maps.get( "proName" ).toString() );
	}

	List< Map< String,Object > > list = new ArrayList<>();//存放店铺下的商品

		/*double discount = 1;//商品折扣
	if(CommonUtil.isNotEmpty(member)){
			Map<String, Object> map = memberpayService.findCardType(member.getId());
			String result = map.get("result").toString();
			if(result=="true"||result.equals("true")){
				discount = Double.parseDouble(map.get("discount").toString());
			}
		}*/
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = mallGroupBuyDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String is_specifica = map2.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica.equals( "1" ) ) {
		    map2.put( "old_price", map2.get( "inv_price" ) );
		    String specifica_img_id = map2.get( "specifica_img_id" ).toString();
		    if ( specifica_img_id.equals( "0" ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "old_price", map2.get( "pro_price" ) );
		    map2.put( "image_url", map2.get( "image_url" ) );
		}
		String is_member_discount = map2.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
		if ( is_member_discount.equals( "1" ) ) {
		    //					map2.put("price", Math.ceil((Double.parseDouble(map2.get("price").toString())*discount)*100)/100);

		    map2.put( "price", Math.ceil( Double.parseDouble( map2.get( "price" ).toString() ) * 100 ) / 100 );
		}

		Date startTime = DateTimeKit.parse( map2.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date endTime = DateTimeKit.parse( map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
		map2.put( "times", ( endTime.getTime() - nowTime.getTime() ) / 1000 );

		Date date = new Date();
		int isEnd = 1;
		if ( startTime.getTime() < date.getTime() && endTime.getTime() <= date.getTime() ) {
		    isEnd = -1;
		}
		map2.put( "isEnd", isEnd );
		int status = 1;
		if ( startTime.getTime() <= date.getTime() && date.getTime() < endTime.getTime() ) {
		    status = -1;
		}
		map2.put( "status", status );

		list.add( map2 );
	    }
	}
	return list;
    }

    @Override
    public MallGroupBuy getGroupBuyByProId( Integer proId, Integer shopId, int activityId ) {
	MallGroupBuy groupBuy = new MallGroupBuy();
	groupBuy.setProductId( proId );
	groupBuy.setShopId( shopId );
	if ( activityId > 0 ) {
	    groupBuy.setId( activityId );
	}
	groupBuy = mallGroupBuyDAO.selectBuyByProductId( groupBuy );
	if ( groupBuy != null && CommonUtil.isNotEmpty( groupBuy.getId() ) ) {

	    Date endTime = DateTimeKit.parse( groupBuy.getGEndTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

	    groupBuy.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    List< MallGroupBuyPrice > priceList = mallGroupBuyPriceService.selectPriceByGroupId( groupBuy.getId() );

	    groupBuy.setPriceList( priceList );
	    return groupBuy;
	}
	return null;
    }

    @Override
    public PhoneProductDetailResult getGroupProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member ) {
	/*if ( activityId == 0 ) {
	    return result;
	}*/
	//查询团购信息
	MallGroupBuy groupBuy = getGroupBuyByProId( proId, shopId, activityId );//通过商品id查询团购信息
	if ( CommonUtil.isEmpty( groupBuy ) ) {
	    return result;
	}
	result.setActivityTimes( groupBuy.getTimes() );
	result.setActivityId( groupBuy.getId() );//活动id
	if ( CommonUtil.isNotEmpty( groupBuy.getGMaxBuyNum() ) && groupBuy.getGMaxBuyNum() > 0 ) {
	    result.setMaxBuyNum( groupBuy.getGMaxBuyNum() );//限购
	}
	double groupPrice = CommonUtil.toDouble( groupBuy.getGPrice() );
	List< Integer > invIdList = new ArrayList<>();
	if ( CommonUtil.isNotEmpty( groupBuy.getPriceList() ) ) {
	    for ( MallGroupBuyPrice price : groupBuy.getPriceList() ) {
		if ( price.getIsJoinGroup() == 1 ) {
		    if ( result.getInvId() == 0 || result.getInvId() == price.getInvenId() ) {
			groupPrice = CommonUtil.toDouble( price.getGroupPrice() );
			result.setInvId( price.getInvenId() );
		    }
		    //		    result.setInvId( price.getInvenId() );
		    if ( result.getInvId() > 0 ) {
			invIdList.add( price.getInvenId() );
		    }
		}
	    }
	}
	result.setInvIdList( invIdList );
	//	result.setProductPrice( groupPrice );
	result.setGroupPrice( groupPrice );
	result.setGroupPeopleNum( groupBuy.getGPeopleNum() );

	Map< String,Object > groupMap = new HashMap<>();
	groupMap.put( "groupBuyId", groupBuy.getId() );
	if ( CommonUtil.isNotEmpty( member ) ) {
	    groupMap.put( "joinUserId", member.getId() );
	}
	//查询参团信息
	List< Map< String,Object > > list = mallGroupJoinService.getJoinGroup( groupMap, member );
	result.setJoinList( list );
	return result;
    }

    @Override
    public Map< String,Object > getGroupBuyById( MallGroupBuy mallGroupBuy, int proId, int joinId ) {
	Map< String,Object > resultMap = new HashMap<>();
	MallGroupJoin mallGroupJoin = mallGroupJoinDAO.selectById( joinId );
	//获取商品信息
	MallProduct product = mallProductService.selectById( proId );
	if ( CommonUtil.isEmpty( product ) || CommonUtil.isEmpty( mallGroupJoin ) ) {
	    return null;
	}
	List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssId( 1, 1, product.getId() );
	if ( imageList != null && imageList.size() > 0 ) {
	    resultMap.put( "imageUrl", imageList.get( 0 ).getImageUrl() );
	}
	double price = CommonUtil.toDouble( product.getProPrice() );
	resultMap.put( "proName", product.getProName() );
	resultMap.put( "id", product.getId() );
	resultMap.put( "shopId", product.getShopId() );
	resultMap.put( "busId", product.getUserId() );
	resultMap.put( "isSpecifica", product.getIsSpecifica() );
	resultMap.put( "maxNum", product.getProRestrictionNum() );

	Integer invId = 0;
	if ( CommonUtil.isNotEmpty( product.getIsSpecifica() ) ) {//存在规格
	    String isSpecifica = product.getIsSpecifica().toString();
	    if ( isSpecifica.equals( "1" ) ) {

		MallProductInventory inven = mallProductInventoryService.selectByIsDefault( product.getId() );
		if ( inven != null ) {
		    invId = inven.getId();
		    resultMap.put( "invId", inven.getId() );
		    price = CommonUtil.toDouble( inven.getInvPrice() );

		    Map< String,Object > guige = mallProductInventoryService.productSpecifications( product.getId(), inven.getId().toString() );
		    if ( CommonUtil.isNotEmpty( guige ) ) {
			resultMap.put( "specificaIds", guige.get( "xids" ) );
			resultMap.put( "specificaName", guige.get( "specifica_name" ) );
		    }
		    //		    resultMap.put( "guige", guige );//规格信息 xids specifica_name
		} else {
		    product.setIsSpecifica( 0 );
		}
	    }
	}
	resultMap.put( "oldPrice", price );
	price = CommonUtil.toDouble( mallGroupBuy.getGPrice() );
	List< MallGroupBuyPrice > priceList = mallGroupBuyPriceService.selectPriceByGroupId( mallGroupBuy.getId() );
	if ( priceList != null && priceList.size() > 0 ) {
	    for ( MallGroupBuyPrice buyPrice : priceList ) {
		if ( invId.toString().equals( buyPrice.getInvenId().toString() ) ) {
		    price = CommonUtil.toDouble( buyPrice.getGroupPrice() );
		    break;
		}
	    }
	}

	//	groupBuy.setPriceList( priceList );
	resultMap.put( "price", price );
	Date endTime = DateTimeKit.parse( mallGroupBuy.getGEndTime(), "yyyy-MM-dd HH:mm:ss" );
	Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
	resultMap.put( "times", ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	return resultMap;
    }

    @Override
    public WxPublicUsers wxPublicByBuyId( int id ) {

	MallGroupBuy groupBuy = mallGroupBuyDAO.selectById( id );
	MallStore store = mallStoreDAO.selectById( groupBuy.getShopId() );
	return wxPublicUserService.selectByUserId( store.getStoUserId() );

	//        String sql= "
	// SELECT a.id,b.id as shopId,a.bus_user_id FROM t_wx_public_users a
	// LEFT JOIN t_mall_store b ON a.bus_user_id=b.sto_user_id
	// LEFT JOIN t_mall_group_buy c ON b.id=c.shop_id
	// WHERE c.id="+id;
	//        return daoUtil.queryForMap(sql);
	//	return null;
    }

    @Override
    public List< Map< String,Object > > selectgbProductByShopId( Map< String,Object > maps ) {
	return mallGroupBuyDAO.selectgbProductByShopId( maps );
    }

    @Override
    public int groupIsReturn( int groupBuyId, String orderType, Object orderId, Object detailId, MallGroupBuy buy ) {
	int groupIsReturn = 0;
	if ( buy != null ) {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", orderId );
	    params.put( "orderDetailId", detailId );
	    params.put( "groupBuyId", buy.getId() );
	    //查询是否已成团
	    Map< String,Object > joinMap = mallGroupJoinDAO.groupJoinPeopleNum( params );
	    if ( joinMap != null ) {
		int count = CommonUtil.toInteger( joinMap.get( "num" ) );
		//团购凑齐人允许退款
		if ( count >= buy.getGPeopleNum() ) {
		    groupIsReturn = 0;
		} else {//拼团人数没达到不允许退款
		    groupIsReturn = 1;
		}
	    }
	}
	return groupIsReturn;
    }

    @Override
    public PageUtil searchGroupBuyProduct( PhoneSearchProductDTO searchProductDTO, Member member ) {
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
	int rowCount = mallGroupBuyDAO.selectCountGoingGroupProduct( searchProductDTO );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	searchProductDTO.setMaxNum( pageSize );

	List< Map< String,Object > > productList = mallGroupBuyDAO.selectGoingGroupProduct( searchProductDTO );

	page.setSubList( mallPageService.getSearchProductParam( productList, 1, searchProductDTO ) );
	return page;
    }

    @Override
    public boolean groupProductCanBuy( int groupBuyId, int invId, int productNum, int memberId, int memberBuyNum ) {
	if ( groupBuyId == 0 ) {
	    return false;
	}
	MallGroupBuy buy = new MallGroupBuy();
	buy.setId( groupBuyId );
	MallGroupBuy mallGroupBuy = mallGroupBuyDAO.selectBuyByProductId( buy );
	if ( CommonUtil.isEmpty( mallGroupBuy ) ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的团购商品被删除或已失效" );
	}
	if ( mallGroupBuy.getStatus() == 0 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的团购商品活动还未开始" );
	} else if ( mallGroupBuy.getStatus() == -1 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的团购商品活动已结束" );
	}
	int maxBuyNum = mallGroupBuy.getGMaxBuyNum();
	if ( maxBuyNum > 0 && memberBuyNum > -1 ) {
	    if ( memberBuyNum + productNum > maxBuyNum ) {
		throw new BusinessException( ResponseEnums.MAX_BUY_ERROR.getCode(), "每人限购" + maxBuyNum + "件" + ResponseEnums.MAX_BUY_ERROR.getDesc() );
	    }
	}
	//判断商品规格是否允许参团
	if ( invId > 0 ) {
	    List< MallGroupBuyPrice > buyPriceList = mallGroupBuyPriceService.selectPriceByInvId( groupBuyId, invId );
	    if ( buyPriceList != null && buyPriceList.size() > 0 ) {
		MallGroupBuyPrice buyPrice = buyPriceList.get( 0 );
		if ( buyPrice.getIsJoinGroup() == 0 ) {
		    throw new BusinessException( ResponseEnums.INV_NO_JOIN_ERROR.getCode(), ResponseEnums.INV_NO_JOIN_ERROR.getDesc() );
		}
	    } else {
		throw new BusinessException( ResponseEnums.INV_NO_JOIN_ERROR.getCode(), ResponseEnums.INV_NO_JOIN_ERROR.getDesc() );
	    }
	}
	return true;
    }

    @Override
    public boolean orderIsCanRenturn( Integer orderId, Integer orderDetailId, Integer groupBuyid ) {
	MallGroupBuy mallGroupBuy = mallGroupBuyDAO.selectById( groupBuyid );
	if ( CommonUtil.isEmpty( mallGroupBuy ) ) {
	    return false;
	}
	int joinNum = mallGroupJoinService.selectGroupJoinPeopleNum( groupBuyid, orderId, orderDetailId );
	return joinNum >= mallGroupBuy.getGPeopleNum();
    }
}
