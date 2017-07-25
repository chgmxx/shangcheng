package com.gt.mall.dao.auction;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.auction.MallAuctionOffer;

import java.util.List;

/**
 * <p>
 * 拍卖出价表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionOfferDAO extends BaseMapper< MallAuctionOffer > {
    /**
     * 查询当前出价最高者
     * @param id
     * @return
     */
    MallAuctionOffer selectByOffer(String id);

    /**
     * 查询拍卖出价次数
     * @param off
     * @return
     */
    List<MallAuctionOffer> selectListByOffer(MallAuctionOffer off);
}