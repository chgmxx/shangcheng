package com.gt.mall.service.web.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.param.phone.auction.PhoneAddAuctionBiddingDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖出价表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionOfferService extends BaseService< MallAuctionOffer > {

    /**
     * 出价
     *
     * @param params   offer:出价信息，bid：竞拍信息
     * @param memberId 用户id
     *
     * @return 是否成功
     */
    Map< String,Object > addOffer( Map< String,Object > params, String memberId );

    /**
     * 出价
     *
     * @param biddingDTO bid：竞拍信息
     * @param memberId   用户id
     *
     * @return 是否成功
     */
    Map< String,Object > addOffer( PhoneAddAuctionBiddingDTO biddingDTO, String memberId );

}
