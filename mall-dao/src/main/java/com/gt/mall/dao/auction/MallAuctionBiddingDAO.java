package com.gt.mall.dao.auction;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.auction.MallAuctionBidding;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞拍表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionBiddingDAO extends BaseMapper< MallAuctionBidding > {

    /**
     * 查询用户参加拍卖的数量
     * @param params
     * @return
     */
    int selectCountByBuyId(Map<String, Object> params);

    /**
     * 查询竞拍信息
     * @param bidding
     * @return
     */
    MallAuctionBidding selectByBidding(MallAuctionBidding bidding);

    /**
     * 查询竞拍信息
     * @param bidding
     * @return
     */
    List<MallAuctionBidding> selectListByBidding(MallAuctionBidding bidding);

    /**
     * 修改根据拍卖id来修改竞拍信息
     * @param bidding
     * @return
     */
    int updateBidByAucId(MallAuctionBidding bidding);

    /**
     * 查询我的获拍
     * @param params
     * @return
     */
    List<Map<String, Object>> selectMyHuoBid(Map<String, Object> params);

    /**
     * 查询我的竞拍
     * @param params
     * @return
     */
    List<Map<String, Object>> selectMyBidding(Map<String, Object> params);
}