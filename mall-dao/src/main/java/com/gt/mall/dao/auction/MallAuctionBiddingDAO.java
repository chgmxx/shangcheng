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
public interface MallAuctionBiddingDAO extends BaseMapper<MallAuctionBidding> {

    /**
     * 查询用户参加拍卖的数量
     *
     * @param params aucId：拍卖id, joinUserId：用户ID
     * @return 数量
     */
    int selectCountByBuyId(Map<String, Object> params);

    /**
     * 查询竞拍信息
     *
     * @param bidding aucId：拍卖id,userId:用户id,orderId:订单id,proId:商品id
     * @return 竞拍信息
     */
    MallAuctionBidding selectByBidding(MallAuctionBidding bidding);

    /**
     * 查询竞拍信息
     *
     * @param bidding  aucId：拍卖id,userId:用户id
     * @return 竞拍信息
     */
    List<MallAuctionBidding> selectListByBidding(MallAuctionBidding bidding);

    /**
     * 修改根据拍卖id来修改竞拍信息
     *
     * @param bidding aucId：拍卖id,userId:用户id,orderId:订单id,
     *                proId:商品id,orderDetailId:订单详情id,aucStatus:拍卖状态
     * @return 成功与否
     */
    int updateBidByAucId(MallAuctionBidding bidding);

    /**
     * 查询我的获拍
     *
     * @param params oldMemberIds:用户集合，或 userId：用户id
     * @return 获拍列表
     */
    List<Map<String, Object>> selectMyHuoBid(Map<String, Object> params);

    /**
     * 查询我的竞拍
     *
     * @param params oldMemberIds:用户集合，或 memberId：用户id
     * @return 竞拍列表
     */
    List<Map<String, Object>> selectMyBidding(Map<String, Object> params);
}