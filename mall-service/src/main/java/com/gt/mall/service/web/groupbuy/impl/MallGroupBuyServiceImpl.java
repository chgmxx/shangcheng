package com.gt.mall.service.web.groupbuy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyPriceDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.seller.MallSellerJoinProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyPriceService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PageUtil;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
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
    private MallGroupBuyDAO          groupBuyDAO;
    @Autowired
    private MallGroupBuyPriceDAO     groupBuyPriceDAO;
    @Autowired
    private MallGroupBuyPriceService groupBuyPriceService;
    @Autowired
    private MallGroupJoinDAO         groupJoinDAO;
    @Autowired
    private MallIntegralDAO          integralDAO;
    @Autowired
    private MallSellerJoinProductDAO sellerJoinProductDAO;
    @Autowired
    private MallSearchKeywordService searchKeywordService;
    @Autowired
    private MallOrderDetailDAO       orderDetailDAO;
    @Autowired
    private MallOrderDAO             orderDAO;
    @Autowired
    private MallProductService       productService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private MallStoreDAO             storeDAO;
    @Autowired
    private WxPublicUserService      wxPublicUserService;

    @Override
    public PageUtil selectGroupBuyByShopId( Map< String,Object > params, int userId ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = groupBuyDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mGroupBuy/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断团购是否有数据
	    List< MallGroupBuy > groupBuyList = groupBuyDAO.selectByPage( params );
	    if ( groupBuyList != null && groupBuyList.size() > 0 ) {
		List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( userId );
		for ( MallGroupBuy buy : groupBuyList ) {
		    for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
			if ( wxShops.getId() == buy.getWx_shop_id() ) {
			    if ( CommonUtil.isNotEmpty( wxShops.getBusinessName() ) ) {
				buy.setShopName( wxShops.getBusinessName() );
			    }
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
	Map< String,Object > map = groupBuyDAO.selectByGroupBuyId( id );

	int groupBuyId = CommonUtil.toInteger( map.get( "id" ) );
	//        List<MallGroupBuyPrice> priceList = groupBuyPriceDAO.selectPriceByGroupId(groupBuyId);
	Wrapper< MallGroupBuyPrice > groupWrapper = new EntityWrapper<>();
	groupWrapper.where( "group_buy_id = {0} and is_delete = 0", groupBuyId );
	List< MallGroupBuyPrice > priceList = groupBuyPriceDAO.selectList( groupWrapper );

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
	    MallGroupBuy groupBuy = (MallGroupBuy) JSONObject.toJavaObject( JSONObject.parseObject( groupMap.get( "groupBuy" ).toString() ), MallGroupBuy.class );
	    //判断选择的商品是否已经存在未开始和进行中的团购中
	    List< MallGroupBuy > buyList = groupBuyDAO.selectGroupByProId( groupBuy );
	    if ( buyList == null || buyList.size() == 0 ) {
		groupBuy.setUserId( userId );
		if ( CommonUtil.isNotEmpty( groupBuy.getId() ) ) {
		    //判断本商品是否正在团购中
		    MallGroupBuy buy = groupBuyDAO.selectGroupByIds( groupBuy.getId() );

		    WsWxShopInfo wsWxShopInfo = wxShopService.getShopById( buy.getWx_shop_id() );
		    if ( CommonUtil.isNotEmpty( wsWxShopInfo.getBusinessName() ) ) {
			buy.setShopName( wsWxShopInfo.getBusinessName() );
		    }

		    if ( buy.getStatus() == 1 && CommonUtil.isNotEmpty( buy.getJoinId() ) ) {//正在进行团购的商品不能修改
			code = -2;
			status = buy.getStatus();
		    } else {
			MallGroupBuy buyOld = groupBuyDAO.selectById( groupBuy.getId() );
			if ( !buyOld.getProductId().equals( groupBuy.getProductId() ) ) {//用户更换了商品
			    flag = true;
			}
			num = groupBuyDAO.updateById( groupBuy );
		    }
		} else {
		    groupBuy.setCreateTime( new Date() );
		    num = groupBuyDAO.insert( groupBuy );
		}
		if ( CommonUtil.isNotEmpty( groupBuy.getId() ) && status != 1 ) {
		    groupBuyPriceService.editGroupBuyPrice( groupMap, groupBuy.getId(), flag );
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
	int num = groupBuyDAO.updateById( groupBuy );
	if ( num > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public PageUtil selectProByGroup( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = groupBuyDAO.selectCountProByGroup( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mGroupBuy/getProductByGroup.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量
	String defaultProId = "";
	if ( CommonUtil.isNotEmpty( params.get( "defaultProId" ) ) ) {
	    defaultProId = params.get( "defaultProId" ).toString();
	}
	String proIds = "";
	String shopIds = "";

	if ( count > 0 ) {// 判断商品是否有数据
	    List< Map< String,Object > > productList = groupBuyDAO.selectProByGroup( params );
	    List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	    if ( productList != null && productList.size() > 0 ) {
		List< Integer > products = new ArrayList< Integer >();
		for ( Map< String,Object > map : productList ) {
		    products.add( CommonUtil.toInteger( map.get( "id" ) ) );
		}
		List< Map< String,Object > > groupLIst = groupBuyDAO.selectGroupsByProId( products );
		List< Map< String,Object > > seckillList = groupBuyDAO.selectSeckillByProId( products );
		List< Map< String,Object > > auctionList = groupBuyDAO.selectAuctionByProId( products );
		List< Map< String,Object > > presaleList = groupBuyDAO.selectPresaleByProId( products );
		List< Map< String,Object > > pifaList = groupBuyDAO.selectpifaByProId( products );
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
			/*if ( CommonUtil.isNotEmpty( map.get( "groupMap" ) ) ) {
			    Map groupMap = JSONObject.parseObject( JSON.toJSONString( map.get( "groupMap" ) ), Map.class );
			    if ( CommonUtil.isNotEmpty( groupMap.get( "g_start_time" ) ) ) {
				String startTime = groupMap.get( "g_start_time" ).toString();
				String endTime = groupMap.get( "g_end_time" ).toString();
				groupStatus = isAddPro( startTime, endTime, defaultProId, pId );
			    }
			}
			if ( CommonUtil.isNotEmpty( map.get( "seckillMap" ) ) ) {
			    Map seckillMap = JSONObject.parseObject( JSON.toJSONString( map.get( "seckillMap" ) ), Map.class );
			    if ( CommonUtil.isNotEmpty( seckillMap.get( "s_start_time" ) ) ) {
				String startTime = seckillMap.get( "s_start_time" ).toString();
				String endTime = seckillMap.get( "s_end_time" ).toString();
				seckillStatus = isAddPro( startTime, endTime, defaultProId, pId );
			    }
			}
			if ( CommonUtil.isNotEmpty( map.get( "auctionMap" ) ) ) {
			    Map auctionMap = JSONObject.parseObject( JSON.toJSONString( map.get( "auctionMap" ) ), Map.class );
			    if ( CommonUtil.isNotEmpty( auctionMap.get( "auc_start_time" ) ) ) {
				String startTime = auctionMap.get( "auc_start_time" ).toString();
				String endTime = auctionMap.get( "auc_end_time" ).toString();
				auctionStatus = isAddPro( startTime, endTime, defaultProId, pId );
			    }
			}
			if ( CommonUtil.isNotEmpty( map.get( "presaleMap" ) ) ) {
			    Map presaleMap = JSONObject.parseObject( JSON.toJSONString( map.get( "presaleMap" ) ), Map.class );
			    if ( CommonUtil.isNotEmpty( presaleMap.get( "sale_start_time" ) ) ) {
				String startTime = presaleMap.get( "sale_start_time" ).toString();
				String endTime = presaleMap.get( "sale_end_time" ).toString();
				presaleStatus = isAddPro( startTime, endTime, defaultProId, pId );
			    }
			}
			if ( CommonUtil.isNotEmpty( map.get( "pifaMap" ) ) ) {
			    Map pifaMap = JSONObject.parseObject( JSON.toJSONString( map.get( "pifaMap" ) ), Map.class );
			    if ( CommonUtil.isNotEmpty( pifaMap.get( "pf_start_time" ) ) ) {
				String startTime = pifaMap.get( "pf_start_time" ).toString();
				String endTime = pifaMap.get( "pf_end_time" ).toString();
				pifaStatus = isAddPro( startTime, endTime, defaultProId, pId );
			    }
			}*/
		    } else if ( isCommission != 0 ) {
			Map< String,Object > sellerMap = new HashMap< String,Object >();
			sellerMap.put( "userId", params.get( "userId" ) );
			sellerMap.put( "productId", pId );
			sellerMap.put( "shopId", params.get( "shopId" ) );
			List< MallSellerJoinProduct > jionList = sellerJoinProductDAO.selectProductByIsJoin( sellerMap );
			if ( jionList != null && jionList.size() > 0 ) {
			    map.put( "sellerStatus", 1 );
			}
		    } else if ( isIntegral != 0 ) {
			Map< String,Object > integralMap = new HashMap< String,Object >();
			integralMap.put( "userId", params.get( "userId" ) );
			integralMap.put( "productId", pId );
			integralMap.put( "shopId", params.get( "shopId" ) );
			List< MallIntegral > integralList = integralDAO.selectByIntegral( integralMap );
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
	String proName = "";
	if ( CommonUtil.isNotEmpty( maps.get( "proName" ) ) && CommonUtil.isNotEmpty( member ) ) {
	    proName = maps.get( "proName" ).toString();
	    searchKeywordService.insertSeachKeyWord( member.getId(), shopid, proName.toString() );
	}

	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();//存放店铺下的商品

		/*double discount = 1;//商品折扣
	if(CommonUtil.isNotEmpty(member)){
			Map<String, Object> map = memberpayService.findCardType(member.getId());
			String result = map.get("result").toString();
			if(result=="true"||result.equals("true")){
				discount = Double.parseDouble(map.get("discount").toString());
			}
		}*/
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = groupBuyDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String is_specifica = map2.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica == "1" || is_specifica.equals( "1" ) ) {
		    map2.put( "old_price", map2.get( "inv_price" ) );
		    String specifica_img_id = map2.get( "specifica_img_id" ).toString();
		    if ( specifica_img_id == "0" || specifica_img_id.equals( "0" ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "old_price", map2.get( "pro_price" ) );
		    map2.put( "image_url", map2.get( "image_url" ) );
		}
		String is_member_discount = map2.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
		if ( is_member_discount == "1" || is_member_discount.equals( "1" ) ) {
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
    public MallGroupBuy getGroupBuyByProId( Integer proId, Integer shopId ) {
	MallGroupBuy groupBuy = new MallGroupBuy();
	groupBuy.setProductId( proId );
	groupBuy.setShopId( shopId );
	groupBuy = groupBuyDAO.selectBuyByProductId( groupBuy );
	if ( groupBuy != null && CommonUtil.isNotEmpty( groupBuy.getId() ) ) {

	    Date endTime = DateTimeKit.parse( groupBuy.getGEndTime().toString(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

	    groupBuy.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    //            List<MallGroupBuyPrice> priceList = groupBuyPriceDAO.selectPriceByGroupId(groupBuy.getId());
	    Wrapper< MallGroupBuyPrice > groupWrapper = new EntityWrapper<>();
	    groupWrapper.where( "group_buy_id = {0} and is_delete = 0", groupBuy.getId() );
	    List< MallGroupBuyPrice > priceList = groupBuyPriceDAO.selectList( groupWrapper );

	    groupBuy.setPriceList( priceList );
	    return groupBuy;
	}
	return null;
    }

    @Override
    public Map< String,Object > getGroupBuyById( String memberId, int id ) {
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "id", id );
	//通过团购id获取团购信息
	List< Map< String,Object > > productList = groupBuyDAO.selectgbProductByShopId( params );
	if ( productList != null && productList.size() > 0 ) {
	    Map< String,Object > map2 = productList.get( 0 );
	    String is_specifica = map2.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
	    if ( is_specifica == "1" || is_specifica.equals( "1" ) ) {
		map2.put( "old_price", map2.get( "inv_price" ) );
		if ( CommonUtil.isNotEmpty( map2.get( "specifica_img_url" ) ) ) {
		    map2.put( "image_url", map2.get( "specifica_img_url" ) );
		}
	    } else {
		map2.put( "old_price", map2.get( "pro_price" ) );
	    }
	    String is_member_discount = map2.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
	    if ( is_member_discount == "1" || is_member_discount.equals( "1" ) ) {
		map2.put( "price", Math.ceil( ( Double.parseDouble( map2.get( "price" ).toString() ) ) * 100 ) / 100 );
	    }
	    Date endTime = DateTimeKit.parse( map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

	    map2.put( "times", ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    //			String endTime = map2.get("endTime").toString();//团购结束时间
	    //			long[] times = DateTimeKit.getDistanceTimes(endTime, DateTimeKit.format(new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT));
	    //			map2.put("times", times);

	    if ( CommonUtil.isNotEmpty( memberId ) ) {
		params.put( "groupBuyId", id );
		params.put( "buyerUserId", memberId );
		List< Map< String,Object > > list = groupJoinDAO.selectJoinGroupByProId( params );
		Map< String,Object > resultMap = list.get( 0 );
		int orderDetailId = CommonUtil.toInteger( resultMap.get( "orderDetailId" ) );

		MallOrderDetail detail = orderDetailDAO.selectById( orderDetailId );

		if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
		    String specificaIds = detail.getProductSpecificas();
		    Map< String,Object > map = productService.getProInvIdBySpecId( specificaIds, detail.getProductId() );
		    if ( CommonUtil.isNotEmpty( map ) ) {
			if ( CommonUtil.isNotEmpty( map.get( "specifica_values" ) ) ) {
			    String specificaValues = CommonUtil.toString( map.get( "specifica_values" ) );
			    specificaValues = specificaValues.replace( ",", " " );
			    map2.put( "specifica_values", specificaValues );
			}
		    }
		}
	    }
	    return map2;
	}
	return null;
    }

    @Override
    public WxPublicUsers wxPublicByBuyId( int id ) {

	MallGroupBuy groupBuy = groupBuyDAO.selectById( id );
	MallStore store = storeDAO.selectById( groupBuy.getShopId() );
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
	return groupBuyDAO.selectgbProductByShopId( maps );
    }

    @Override
    public int groupIsReturn( int groupBuyId, String orderType, Object orderId, Object detailId, MallGroupBuy buy ) {
	int groupIsReturn = 0;
	if ( buy != null ) {
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "orderId", orderId );
	    params.put( "orderDetailId", detailId );
	    params.put( "groupBuyId", buy.getId() );
	    //查询是否已成团
	    Map< String,Object > joinMap = orderDAO.groupJoinPeopleNum( params );
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
}
