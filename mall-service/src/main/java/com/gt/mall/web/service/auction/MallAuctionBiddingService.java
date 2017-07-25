package com.gt.mall.web.service.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
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
public interface MallAuctionBiddingService extends BaseService<MallAuctionBidding> {

    /**
     * 查询用户参加拍卖的数量
     *
     * @param params
     * @return
     */
    int selectCountByBuyId(Map<String, Object> params);

    /**
     * 拍卖竞拍
     *
     * @param order
     * @param detailList
     */
    void addBidding(MallOrder order, List<MallOrderDetail> detailList);

    /**
     * 修改拍卖竞拍的状态
     *
     * @param order
     * @param status
     */
    void upStateBidding(MallOrder order, int status, List<MallOrderDetail> detailList);

    /**
     * 查询我的竞拍
     *
     * @param member
     * @return
     */
    List<Map<String, Object>> selectMyBidding(Member member);

    /**
     * 查询我的获拍
     *
     * @param member
     * @return
     */
    List<Map<String, Object>> selectMyHuoBid(Member member);

}
