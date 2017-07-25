package com.gt.mall.dao.auction;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.auction.MallAuctionMargin;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞拍保证金表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionMarginDAO extends BaseMapper<MallAuctionMargin> {

    /**
     * 统计保证金
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询保证金信息
     *
     * @param params
     * @return
     */
    List<MallAuctionMargin> selectByPage(Map<String, Object> params);

    /**
     * 根据保证金单号来查询保证金的信息
     *
     * @param aucNo
     * @return
     */
    MallAuctionMargin selectByAucNo(String aucNo);

    /**
     * 查询保证金信息
     *
     * @param margin
     * @return
     */
    MallAuctionMargin selectByMargin(MallAuctionMargin margin);

    /**
     * 查询保证金的集合信息
     *
     * @param margin
     * @return
     */
    List<MallAuctionMargin> selectListByMargin(MallAuctionMargin margin);

    /**
     * 查询已结束拍卖的保证金信息
     *
     * @return
     */
    List<Map<String, Object>> selectMarginByEnd();

}