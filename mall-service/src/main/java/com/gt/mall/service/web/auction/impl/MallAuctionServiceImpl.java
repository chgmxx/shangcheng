package com.gt.mall.service.web.auction.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionDAO;
import com.gt.mall.dao.auction.MallAuctionMarginDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.product.PhoneAuctionProductDetailResult;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 拍卖 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAuctionServiceImpl extends BaseServiceImpl< MallAuctionDAO,MallAuction > implements MallAuctionService {
    private Logger log = Logger.getLogger( MallAuctionServiceImpl.class );

    @Autowired
    private MallAuctionDAO auctionDAO;

    @Autowired
    private MallAuctionMarginDAO auctionMarginDAO;

    @Autowired
    private MallAuctionOfferDAO auctionOfferDAO;

    @Autowired
    private MallAuctionBiddingDAO auctionBiddingDAO;

    @Autowired
    private MallSearchKeywordService searchKeywordService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private MallPageService          mallPageService;
    @Autowired
    private MallAuctionMarginService mallAuctionMarginService;

    @Override
    public PageUtil selectAuctionByShopId( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = auctionDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mAuction/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断拍卖是否有数据
	    List< MallAuction > auctionList = auctionDAO.selectByPage( params );
	    if ( auctionList != null && auctionList.size() > 0 && shoplist != null && shoplist.size() > 0 ) {
		for ( MallAuction auction : auctionList ) {
		    int status = isJoinAuction( auction );
		    auction.setJoinId( status );
		    for ( Map< String,Object > shopMaps : shoplist ) {
			int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			if ( auction.getShopId() == shop_id ) {
			    auction.setShopName( shopMaps.get( "sto_name" ).toString() );
			    break;
			}
		    }
		}
	    }
	    page.setSubList( auctionList );
	}

	return page;
    }

    private int isJoinAuction( MallAuction auction ) {
	int status = 0;
	if ( auction.getIsMargin() == 1 ) {//需要交纳保证金的拍卖，是否已经有人加入了拍卖
	    MallAuctionMargin margin = new MallAuctionMargin();
	    margin.setAucId( auction.getId() );
	    List< MallAuctionMargin > marginList = auctionMarginDAO.selectListByMargin( margin );
	    if ( marginList != null && marginList.size() > 0 ) {
		status = 1;
	    }
	} else {//不需要交纳保证金
	    if ( auction.getAucType().toString().equals( "1" ) ) {//降价拍,是否已经有人加入了拍卖
		MallAuctionBidding bidding = new MallAuctionBidding();
		bidding.setAucId( auction.getId() );
		List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bidding );
		if ( bidList != null && bidList.size() > 0 ) {
		    status = 1;
		}
	    } else {//升价拍  查询是否已经有人出价了
		MallAuctionOffer offer = new MallAuctionOffer();
		offer.setAucId( auction.getId() );
		List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( offer );
		if ( offerList != null && offerList.size() > 0 ) {
		    status = 1;
		}
	    }
	}
	return status;
    }

    @Override
    public Map< String,Object > selectAuctionById( Integer id ) {
	Map< String,Object > map = auctionDAO.selectByAuctionId( id );
	if ( map != null ) {
	    float startPrice = Float.valueOf( map.get( "aucStartPrice" ).toString() );
	    float aucLowestPrice = Float.valueOf( map.get( "aucLowestPrice" ).toString() );
	    float aucLowerPrice = Float.valueOf( map.get( "aucLowerPrice" ).toString() );
	    int minuTimes = Integer.valueOf( map.get( "aucLowerPriceTime" ).toString() );

	    int diff = (int) ( ( startPrice - aucLowestPrice ) / aucLowerPrice * minuTimes );

	    int[] times = DateTimeKit.minuteForTimes( diff );
	    map.put( "times", times );
	}
	return map;
    }

    @Override
    public int editAuction( Map< String,Object > groupMap, int userId ) {
	int num = 0;
	int code = -1;
	if ( CommonUtil.isNotEmpty( groupMap.get( "auction" ) ) ) {
	    MallAuction auction = (MallAuction) JSONObject.toJavaObject( JSONObject.parseObject( groupMap.get( "auction" ).toString() ), MallAuction.class );
	    // 判断选择的商品是否已经存在未开始和进行中的拍卖中
	    List< MallAuction > buyList = auctionDAO.selectAuctionByProId( auction );
	    if ( buyList == null || buyList.size() == 0 ) {
		if ( CommonUtil.isNotEmpty( auction.getId() ) ) {
		    // 判断本商品是否正在拍卖中
		    MallAuction auc = auctionDAO.selectAuctionByIds( auction.getId() );

		    /*WsWxShopInfo wxShopInfo = wxShopService.getShopById( auc.getWx_shop_id() );
		    if ( CommonUtil.isNotEmpty( wxShopInfo.getBusinessName() ) ) {
			auc.setShopName( wxShopInfo.getBusinessName() );
		    }*/

		    int status = isJoinAuction( auc );
		    if ( auc.getStatus() == 1 && status > 0 ) {// 正在进行拍卖的商品不能修改
			code = -2;
		    } else {
			num = auctionDAO.updateById( auction );
		    }
		} else {
		    auction.setUserId( userId );
		    auction.setCreateTime( new Date() );
		    num = auctionDAO.insert( auction );
		}
		if ( CommonUtil.isNotEmpty( auction.getId() ) ) {
		    code = 1;
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
    public boolean deleteAuction( MallAuction auction ) {
	int num = auctionDAO.updateById( auction );
	if ( num > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public List< Map< String,Object > > getAuctionAll( Member member, Map< String,Object > maps ) {
	int shopid = 0;
	if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
	    shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
	}
	String proName = "";
	if ( CommonUtil.isNotEmpty( maps.get( "proName" ) ) && CommonUtil.isNotEmpty( member ) ) {
	    proName = maps.get( "proName" ).toString();
	    //保存到搜索关键字表
	    searchKeywordService.insertSeachKeyWord( member.getId(), shopid, proName );
	}

	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();// 存放店铺下的商品

		/*double discount = 1;// 商品折扣
	Map<String, Object> map = memberpayService.findCardType(Integer.valueOf(memberId));
		String result = map.get("result").toString();
		if (result == "true" || result.equals("true")) {
			discount = Double.parseDouble(map.get("discount").toString());
		}*/
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = auctionDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		int id = Integer.valueOf( map2.get( "aucId" ).toString() );
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica == "1" || is_specifica.equals( "1" ) ) {
		    map2.put( "old_price", map2.get( "inv_price" ) );
		    if ( CommonUtil.isEmpty( map2.get( "specifica_img_url" ) ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "old_price", map2.get( "pro_price" ) );
		    map2.put( "image_url", map2.get( "image_url" ) );
		}

		String startTimes = map2.get( "startTime" ).toString();

		Date endTime = DateTimeKit.parse( map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTime = DateTimeKit.parse( startTimes, "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

		long nowTimes = new Date().getTime();
		int isEnd = 1;
		int status = -1;
		if ( startTime.getTime() < nowTimes && endTime.getTime() <= nowTimes ) {//拍卖已经结束
		    isEnd = -1;
		}
		map2.put( "isEnd", isEnd );
		if ( startTime.getTime() > nowTimes && endTime.getTime() > nowTimes ) {//拍卖还未开始
		    status = 0;
		    map2.put( "startTimes", ( startTime.getTime() - nowTime.getTime() ) / 1000 );
		} else if ( startTime.getTime() <= nowTimes && nowTimes < endTime.getTime() ) {//拍卖正在进行
		    status = 1;
		}
		map2.put( "status", status );

		long times = ( endTime.getTime() - nowTime.getTime() ) / 1000;
		map2.put( "times", times );

		//计算拍卖中的商品的拍卖的当前价格
		String endtimes = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
		String startPrice = map2.get( "startPrice" ).toString();
		double nowPrice = 0;
		//降价拍
		if ( map2.get( "aucType" ).toString().equals( "1" ) ) {
		    String lower = map2.get( "lowerPriceTime" ).toString();
		    double minu = Double.valueOf( lower );
		    int minTimes = 60000 * Integer.parseInt( lower );
		    double second = (double) DateTimeKit.minsBetween( startTimes, endtimes, minTimes, DateTimeKit.DEFAULT_DATETIME_FORMAT );
		    double minuPrice = Double.valueOf( map2.get( "lowerPrice" ).toString() );
		    nowPrice = second * ( minu * minuPrice );
		    nowPrice = Double.parseDouble( startPrice ) - nowPrice;
		    if ( status == 0 ) {//正在进行的拍卖
			nowPrice = Double.parseDouble( startPrice );
		    }
		    MallAuctionBidding bid = new MallAuctionBidding();
		    bid.setAucId( id );

		    //查询用户竞拍的次数
		    List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );
		    map2.put( "bidSize", bidList.size() );
		} else {//升价拍
		    //获取最新拍价
		    nowPrice = Double.parseDouble( startPrice );
		    //查询当前出价最高的金额
		    MallAuctionOffer offer = auctionOfferDAO.selectByOffer( map2.get( "aucId" ).toString() );
		    if ( offer != null ) {
			nowPrice = Double.parseDouble( offer.getOfferMoney().toString() );
		    }

		    MallAuctionOffer aucOffer = new MallAuctionOffer();
		    aucOffer.setAucId( id );
		    List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( aucOffer );//查询拍卖的出价信息
		    map2.put( "offerSize", offerList.size() );
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		map2.put( "nowPrice", df.format( nowPrice ) );

		String is_member_discount = map2.get( "is_member_discount" ).toString();// 商品是否参加折扣,1参加折扣
		if ( is_member_discount == "1" || is_member_discount.equals( "1" ) ) {

					/*map2.put("price", Math.ceil((Double.parseDouble(map2.get(
			    "price").toString()) * discount) * 100) / 100);*/
		}
		list.add( map2 );
	    }
	}
	if ( CommonUtil.isNotEmpty( maps.get( "type" ) ) ) {
	    if ( maps.get( "type" ).toString().equals( "3" ) ) {
		Object desc = maps.get( "desc" );
		if ( CommonUtil.isNotEmpty( desc ) ) {
		    if ( CommonUtil.toString( desc ).equals( "1" ) ) {
			Collections.sort( list, new MallComparatorUtil( "nowPrice" ) );
		    }
		}
	    }
	}
	return list;
    }

    @Override
    public MallAuction getAuctionByProId( Integer proId, Integer shopId, Integer aId ) {
	MallAuction auction = new MallAuction();
	auction.setId( aId );
	auction.setProductId( proId );
	auction.setShopId( shopId );
	auction = auctionDAO.selectBuyByProductId( auction );
	if ( auction != null && CommonUtil.isNotEmpty( auction.getId() ) ) {
	    Date endTime = DateTimeKit.parse( auction.getAucEndTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date startTime = DateTimeKit.parse( auction.getAucStartTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
	    auction.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    if ( auction.getStatus() == 0 ) {
		auction.setStartTimes( ( startTime.getTime() - nowTime.getTime() ) / 1000 );
	    }

	    BigDecimal startPrice = auction.getAucStartPrice();
	    String startTimes = auction.getAucStartTime();
	    //计算拍卖中的商品的拍卖的当前价格
	    String endtimes = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    //降价拍
	    if ( auction.getAucType().toString().equals( "1" ) ) {
		if ( auction.getStatus() == 1 ) {
		    //int minTimes = 60000*auction.getAucLowerPriceTime();
		    double second = (double) DateTimeKit.minsBetween( startTimes, endtimes, 60000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
		    double minu = Double.valueOf( auction.getAucLowerPriceTime() );
		    double minuPrice = Double.valueOf( auction.getAucLowerPrice().toEngineeringString() );
		    double nowPrice = second * ( minuPrice / minu );
		    nowPrice = Double.parseDouble( startPrice.toString() ) - nowPrice;
		    DecimalFormat df = new DecimalFormat( "######0.00" );
		    nowPrice = Double.parseDouble( df.format( nowPrice ) );
		    auction.setNowPrice( nowPrice );
		} else {
		    auction.setNowPrice( CommonUtil.toDouble( auction.getAucStartPrice() ) );
		}
	    } else {//升价拍
		//获取最新拍价
		double nowPrice = Double.parseDouble( startPrice.toString() );
		MallAuctionOffer offer = auctionOfferDAO.selectByOffer( auction.getId().toString() );
		if ( offer != null ) {
		    nowPrice = Double.parseDouble( offer.getOfferMoney().toString() );
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		nowPrice = Double.parseDouble( df.format( nowPrice ) );
		auction.setNowPrice( nowPrice );
	    }

	    return auction;
	}
	return null;
    }

    @Override
    public List< Map< String,Object > > selectgbAuctionByShopId( Map< String,Object > maps ) {
	return auctionDAO.selectgbProductByShopId( maps );
    }

    @Override
    public Map< String,Object > isMaxNum( Map< String,Object > map, String memberId ) {
	Map< String,Object > result = new HashMap< String,Object >();
	boolean flag = true;
	int aucId = Integer.parseInt( map.get( "groupBuyId" ).toString() );
	MallAuctionBidding bid = new MallAuctionBidding();
	bid.setAucId( aucId );
	bid.setUserId( Integer.parseInt( memberId ) );
	//判断是否已经加入到拍卖竞拍中
	List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );
	MallAuction auction = auctionDAO.selectById( aucId );
	if ( auction != null ) {
	    if ( bidList != null ) {
		if ( auction.getAucRestrictionNum() < bidList.size() && auction.getAucRestrictionNum() > 0 ) {
		    result.put( "result", false );
		    result.put( "msg", "每人限购" + auction.getAucRestrictionNum() + "件，您已超过每人购买次数限制" );
		    flag = false;
		}
	    }

	    String startTimes = auction.getAucStartTime();
	    if ( auction.getAucType().toString().equals( "1" ) ) {
		//计算拍卖中的商品的拍卖的当前价格
		String endtimes = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
		int minTimes = 60000 * auction.getAucLowerPriceTime();
		double second = (double) DateTimeKit.minsBetween( startTimes, endtimes, minTimes, DateTimeKit.DEFAULT_DATETIME_FORMAT );
		if ( auction.getAucType() == 1 ) {
		    String key = "hAuction";
		    String field = bid.getAucId() + "_" + bid.getUserId();
		    if ( JedisUtil.hExists( key, field ) ) {
			String str = JedisUtil.maoget( key, field );
			JSONObject obj = JSONObject.parseObject( str );
			double secondObj = Double.parseDouble( obj.get( "second" ).toString() );
			if ( secondObj == second ) {
			    result.put( "result", false );
			    result.put( "msg", "本场降价您已经购买，请下次降价后再来购买" );
			    flag = false;
			}
		    }
		}
	    }
	}
	if ( flag ) {
	    result.put( "result", true );
	    result.put( "msg", "" );
	}
	return result;
    }

    @Override
    public PageUtil searchAuctionAll( PhoneSearchProductDTO searchProductDTO, Member member ) {

	if ( CommonUtil.isNotEmpty( searchProductDTO.getSearchContent() ) && CommonUtil.isNotEmpty( member ) ) {
	    //保存到搜索关键字表
	    searchKeywordService.insertSeachKeyWord( member.getId(), searchProductDTO.getShopId(), searchProductDTO.getSearchContent() );
	}

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
	int rowCount = auctionDAO.selectCountGoingAuctionProduct( searchProductDTO );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	searchProductDTO.setMaxNum( pageSize );

	List< Map< String,Object > > list = new ArrayList<>();

	List< Map< String,Object > > productList = auctionDAO.selectGoingAuctionProduct( searchProductDTO );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {

		String start = map2.get( "startTime" ).toString();
		/*int id = Integer.valueOf( map2.get( "activityId" ).toString() );*/

		Date endTimes = DateTimeKit.parse( map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTimes = DateTimeKit.parse( map2.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date date = new Date();
		int status = -1;

		if ( startTimes.getTime() > date.getTime() && endTimes.getTime() > date.getTime() ) {//未开始
		    status = 0;
		} else if ( startTimes.getTime() <= date.getTime() && date.getTime() < endTimes.getTime() ) {//正在进行
		    status = 1;
		}
		map2.put( "activityStatus", status );//活动状态 0未开始 1正在进行

		//计算拍卖中的商品的拍卖的当前价格
		String endtimes = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
		String startPrice = map2.get( "startPrice" ).toString();
		double nowPrice;
		//降价拍
		if ( map2.get( "aucType" ).toString().equals( "1" ) ) {
		    String lower = map2.get( "lowerPriceTime" ).toString();
		    double minu = Double.valueOf( lower );
		    int minTimes = 60000 * Integer.parseInt( lower );
		    double second = (double) DateTimeKit.minsBetween( start, endtimes, minTimes, DateTimeKit.DEFAULT_DATETIME_FORMAT );
		    double minuPrice = Double.valueOf( map2.get( "lowerPrice" ).toString() );
		    nowPrice = second * ( minu * minuPrice );
		    nowPrice = Double.parseDouble( startPrice ) - nowPrice;
		    if ( status == 0 ) {//正在进行的拍卖
			nowPrice = Double.parseDouble( startPrice );
		    }
		   /* MallAuctionBidding bid = new MallAuctionBidding();
		    bid.setAucId( id );

		    //查询用户竞拍的次数
		    List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );
		    map2.put( "bidSize", bidList.size() );*/
		} else {//升价拍
		    //获取最新拍价
		    nowPrice = Double.parseDouble( startPrice );
		    //查询当前出价最高的金额
		    MallAuctionOffer offer = auctionOfferDAO.selectByOffer( map2.get( "activityId" ).toString() );
		    if ( offer != null ) {
			nowPrice = Double.parseDouble( offer.getOfferMoney().toString() );
		    }
		    /*MallAuctionOffer aucOffer = new MallAuctionOffer();
		    aucOffer.setAucId( id );
		    List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( aucOffer );//查询拍卖的出价信息
		    map2.put( "offerSize", offerList.size() );*/
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		map2.put( "price", df.format( nowPrice ) );
		list.add( map2 );
	    }
	    productList = mallPageService.getSearchProductParam( list, 1, searchProductDTO );
	}
	if ( CommonUtil.isNotEmpty( searchProductDTO.getSort() ) && searchProductDTO.getSort().equals( "price" ) ) {
	    if ( CommonUtil.isNotEmpty( searchProductDTO.getIsDesc() ) ) {
		if ( searchProductDTO.getIsDesc() == 1 ) {
		    Collections.sort( productList, new MallComparatorUtil( "price" ) );
		}
	    }
	}
	page.setSubList( productList );
	return page;
    }

    @Override
    public PhoneProductDetailResult getAuctionProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member, MallPaySet mallPaySet ) {
	if ( activityId == 0 ) {
	    return result;
	}
	MallAuction auction = getAuctionByProId( proId, shopId, activityId );
	if ( CommonUtil.isEmpty( auction ) ) {
	    return result;
	}
	result.setActivityTimes( auction.getTimes() );
	result.setActivityId( auction.getId() );
	result.setActivityStatus( auction.getStatus() );
	result.setProductPrice( auction.getNowPrice() );
	if ( auction.getAucRestrictionNum() > 0 ) {
	    result.setMaxBuyNum( auction.getAucRestrictionNum() );//限购
	}
	PhoneAuctionProductDetailResult auctionResult = new PhoneAuctionProductDetailResult();

	MallAuctionMargin margin = new MallAuctionMargin();
	margin.setAucId( auction.getId() );
	margin.setMarginStatus( 1 );
	//查询缴纳保证金数量
	List< MallAuctionMargin > marginList = mallAuctionMarginService.getMyAuction( margin );
	MallAuctionBidding bid = new MallAuctionBidding();
	bid.setAucId( auction.getId() );
	if ( auction.getAucType().toString().equals( "2" ) ) {//升价拍
	    //查询出价次数
	    MallAuctionOffer offer = new MallAuctionOffer();
	    offer.setAucId( auction.getId() );
	    List< MallAuctionOffer > offerList = auctionOfferDAO.selectListByOffer( offer );//查询拍卖的出价信息
	    if ( offerList != null && offerList.size() > 0 ) {
		auctionResult.setMarginNumber( offerList.size() );
	    }
	    if ( auction.getStatus() == -1 ) {
		//获取加价拍卖胜出的人
		MallAuctionOffer aucOffer = auctionOfferDAO.selectByOffer( auction.getId().toString() );
		if ( aucOffer != null && CommonUtil.isNotEmpty( member ) ) {
		    if ( aucOffer.getUserId().toString().equals( member.getId().toString() ) ) {
			auctionResult.setIsWin( 1 );//胜出
		    } else {
			auctionResult.setIsWin( 0 );//出局
		    }
		    result.setProductPrice( CommonUtil.toDouble( aucOffer.getOfferMoney() ) );

		    bid.setUserId( member.getId() );
		    bid.setProId( auction.getProductId() );
		    MallAuctionBidding bidding = auctionBiddingDAO.selectByBidding( bid );
		    if ( bidding != null ) {
			if ( bidding.getAucStatus() == 0 ) {
			    auctionResult.setIsReturnOrder( 1 );
			}
		    }
		}
	    }
	} else {//降价拍
	    List< MallAuctionBidding > bidList = auctionBiddingDAO.selectListByBidding( bid );//查询用户的竞拍信息
	    if ( bidList != null && bidList.size() > 0 ) {
		if ( auction.getStatus() == -1 ) {
		    auction.setNowPrice( CommonUtil.toDouble( bidList.get( 0 ).getAucPrice() ) );
		}
	    }
	    auctionResult.setAuctionNumber( bidList.size() );//抢拍次数
	}
	auctionResult.setMarginNumber( marginList.size() );//报名人数
	if ( auction.getIsMargin().toString().equals( "1" ) ) {
	    auctionResult.setDepositMoney( CommonUtil.toDouble( auction.getAucMargin() ) );//保证金
	}
	auctionResult.setAucType( auction.getAucType() );//拍卖类型
	auctionResult.setAucStartPrice( CommonUtil.toDouble( auction.getAucStartPrice() ) );//起拍价
	String aucTypeVal = "升价拍卖";
	if ( auction.getAucType() == 1 ) {//降价拍
	    auctionResult.setAucLowestPrice( CommonUtil.toDouble( auction.getAucLowestPrice() ) );//最低价
	    auctionResult.setAucLowerPriceTime( auction.getAucLowerPriceTime() );//降价时间（每多少分钟）
	    auctionResult.setAucLowerPrice( CommonUtil.toDouble( auction.getAucLowerPrice() ) );//降价金额（每多少分钟降价多少元）
	    aucTypeVal = "降价拍卖";
	} else {
	    auctionResult.setAucAddPrice( CommonUtil.toDouble( auction.getAucAddPrice() ) );//加价幅度
	}
	auctionResult.setAucTypeVal( aucTypeVal );

	result.setAuctionResult( auctionResult );
	return result;
    }

    @Override
    public boolean auctionProductCanBuy( int auctionId, int invId, int productNum, int memberId, int memberBuyNum ) {
	if ( CommonUtil.isEmpty( auctionId ) || auctionId == 0 ) {
	    return false;
	}
	MallAuction auction = new MallAuction();
	auction.setId( auctionId );
	auction = auctionDAO.selectBuyByProductId( auction );
	if ( CommonUtil.isEmpty( auction ) ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的拍卖商品被删除或已失效" );
	}
	if ( auction.getStatus() == 0 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的拍卖商品活动还未开始" );
	} else if ( auction.getStatus() == -1 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的拍卖商品活动已结束" );
	}
	int maxBuyNum = auction.getAucRestrictionNum();
	if ( maxBuyNum > 0 && memberBuyNum > -1 ) {
	    if ( memberBuyNum + productNum > maxBuyNum ) {
		throw new BusinessException( ResponseEnums.MAX_BUY_ERROR.getCode(), "每人限购" + maxBuyNum + "件" + ResponseEnums.MAX_BUY_ERROR.getDesc() );
	    }
	}
	if ( auction.getIsMargin() == 1 && memberId > 0 ) {
	    MallAuctionMargin margin = new MallAuctionMargin();
	    margin.setUserId( memberId );
	    margin.setAucId( auctionId );
	    MallAuctionMargin auctionMargin = auctionMarginDAO.selectOne( margin );
	    if ( CommonUtil.isEmpty( auctionMargin ) ) {
		throw new BusinessException( ResponseEnums.ACTIVITY_MONEY_ERROR.getCode(), "您未缴纳保证金，不能购买该拍卖商品" );

	    } else {
		if ( auctionMargin.getMarginStatus() != 1 ) {
		    throw new BusinessException( ResponseEnums.ACTIVITY_MONEY_ERROR.getCode(), "您未缴纳保证金，不能购买该拍卖商品" );
		}
	    }
	}
	return true;
    }
}
