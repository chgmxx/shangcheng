package com.gt.mall.service.web.auction;

import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞拍表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionBiddingService extends BaseService< MallAuctionBidding > {

    /**
     * 查询用户参加拍卖的数量
     *
     * @param params aucId：拍卖id, joinUserId：用户ID
     *
     * @return 数量
     */
    int selectCountByBuyId( Map< String,Object > params );

    /**
     * 拍卖竞拍
     *
     * @param order      订单信息
     * @param detailList 订单详情信息
     */
    void addBidding( MallOrder order, List< MallOrderDetail > detailList );

    /**
     * 修改拍卖竞拍的状态
     *
     * @param order      订单信息
     * @param status     竞拍状态
     * @param detailList 订单详情信息
     */
    void upStateBidding( MallOrder order, int status, List< MallOrderDetail > detailList );

    /**
     * 查询我的竞拍
     *
     * @param member 用户
     *
     * @return 竞拍列表
     */
    List< Map< String,Object > > selectMyBidding( Member member );

    /**
     * 查询我的获拍
     *
     * @param member 用户
     *
     * @return 获拍列表
     */
    List< Map< String,Object > > selectMyHuoBid( Member member );

}
