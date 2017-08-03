package com.gt.mall.web.service.auction.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.web.service.auction.MallAuctionBiddingService;
import com.gt.mall.web.service.order.MallOrderService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞拍表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAuctionBiddingServiceImpl extends BaseServiceImpl< MallAuctionBiddingDAO,MallAuctionBidding > implements MallAuctionBiddingService {

    private Logger log = Logger.getLogger( MallAuctionBiddingServiceImpl.class );

    @Autowired
    private MallAuctionDAO        auctionDAO;
    @Autowired
    private MallAuctionOfferDAO   auctionOfferDAO;
    @Autowired
    private MallAuctionBiddingDAO auctionBiddingDAO;
    @Autowired
    private MallOrderDAO          orderDAO;
    @Autowired
    private MallOrderService      orderService;

    @Override
    public int selectCountByBuyId( Map< String,Object > params ) {
	return auctionBiddingDAO.selectCountByBuyId( params );
    }

    @Override
    public void addBidding( MallOrder order, List< MallOrderDetail > detailList ) {
	MallOrderDetail detail = detailList.get( 0 );

	MallAuctionBidding bid = new MallAuctionBidding();
	bid.setAucId( order.getGroupBuyId() );
	bid.setUserId( order.getBuyerUserId() );
	bid.setProId( detail.getProductId() );
	//判断是否已经加入到拍卖竞拍中
	MallAuctionBidding bidding = auctionBiddingDAO.selectByBidding( bid );
	bid.setOrderId( order.getId() );
	bid.setOrderDetailId( detail.getId() );
	if ( bidding == null ) {
	    bid.setProName( detail.getDetProName() );
	    bid.setProSpecificaIds( detail.getProductSpecificas() );
	    bid.setProImgUrl( detail.getProductImageUrl() );
	    bid.setAucPrice( detail.getDetProPrice() );
	    bid.setCreateTime( new Date() );
	    auctionBiddingDAO.insert( bid );
	} else {
	    bid.setAucStatus( 0 );
	    //修改拍卖竞拍
	    auctionBiddingDAO.updateBidByAucId( bid );

	    Wrapper wrapper = new EntityWrapper();
	    wrapper.where( " auc_id = {0} AND pro_id = {1} AND user_id ={2}", order.getGroupBuyId(), detail.getProductId(), order.getBuyerUserId() );
	    MallAuctionBidding bidding1 = new MallAuctionBidding();
	    bidding1.setAucStatus( 0 );
	    bidding1.setOrderId( order.getId() );
	    bidding1.setOrderDetailId( detail.getId() );
	    auctionBiddingDAO.update( bidding1, wrapper );
	}
    }

    @Override
    public void upStateBidding( MallOrder order, int status, List< MallOrderDetail > detailList ) {
	MallOrderDetail detail = detailList.get( 0 );
	MallAuctionBidding bid = new MallAuctionBidding();
	bid.setAucId( order.getGroupBuyId() );
	bid.setAucStatus( status );
	bid.setOrderId( order.getId() );
	bid.setOrderDetailId( detail.getId() );
	bid.setProId( detail.getProductId() );
	bid.setUserId( order.getBuyerUserId() );
	//修改拍卖竞拍
	auctionBiddingDAO.updateBidByAucId( bid );

	MallAuction auction = auctionDAO.selectById( order.getGroupBuyId() );

	if ( CommonUtil.isNotEmpty( auction ) ) {
	    String startTimes = auction.getAucStartTime();
	    //计算拍卖中的商品的拍卖的当前价格
	    String endtimes = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    int minTimes = 60000 * auction.getAucLowerPriceTime();
	    if ( auction.getAucType() == 1 ) {
		double second = (double) DateTimeKit.minsBetween( startTimes, endtimes, minTimes, DateTimeKit.DEFAULT_DATETIME_FORMAT );
		String key = Constants.REDIS_KEY + "hAuction";
		String field = bid.getAucId() + "_" + bid.getUserId();
		JSONObject obj = new JSONObject();
		obj.put( "second", second );
		obj.put( "proId", bid.getProId() );
		JedisUtil.map( key, field, obj.toString() );
	    }
	}
    }

    @Override
    public List< Map< String,Object > > selectMyBidding( Member member ) {
	Map< String,Object > params = new HashMap< String,Object >();
	params = orderService.getMemberParams( member, params );
	return auctionBiddingDAO.selectMyBidding( params );
    }

    @Override
    public List< Map< String,Object > > selectMyHuoBid( Member member ) {
	Map< String,Object > params = new HashMap< String,Object >();
	params = orderService.getMemberParams( member, params );
	if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
	    params.put( "userId", params.get( "memberId" ) );
	}
	List< Map< String,Object > > bidList = auctionBiddingDAO.selectMyHuoBid( params );
	if ( bidList != null && bidList.size() > 0 ) {
	    for ( int i = 0; i < bidList.size(); i++ ) {
		Map< String,Object > map = bidList.get( i );
		if ( map.get( "auc_type" ).toString().equals( "2" ) && map.get( "auc_type" ).toString().equals( "0" ) ) {//升价拍
		    //查询用户在升价拍中是否已经领先
		    MallAuctionOffer offer = auctionOfferDAO.selectByOffer( map.get( "id" ).toString() );
		    if ( offer != null ) {
			if ( !offer.getUserId().toString().equals( map.get( "user_id" ).toString() ) ) {
			    bidList.remove( i );
			}
		    }
		}
	    }
	}
	return bidList;
    }
}
