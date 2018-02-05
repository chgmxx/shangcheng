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
public interface MallAuctionMarginDAO extends BaseMapper< MallAuctionMargin > {

    /**
     * 统计保证金
     *
     * @param params shoplist：店铺Id集合
     *
     * @return 数量
     */
    int selectByCount( Map< String,Object > params );

    /**
     * 分页查询保证金信息
     *
     * @param params shoplist:店铺id集合，firstNum：页数，maxNum 数量
     *
     * @return 保证金列表
     */
    List< MallAuctionMargin > selectByPage( Map< String,Object > params );

    /**
     * 根据保证金单号来查询保证金的信息
     *
     * @param aucNo 拍卖单号
     *
     * @return 保证金
     */
    MallAuctionMargin selectByAucNo( String aucNo );

    /**
     * 查询保证金信息
     *
     * @param margin aucId：拍卖id，userId：用户Id
     *
     * @return 保证金
     */
    MallAuctionMargin selectByMargin( MallAuctionMargin margin );

    /**
     * 查询保证金的集合信息
     *
     * @param margin userId：用户Id，oldUserIdList：用户id集合，aucId：拍卖Id，marginStatus：保证金状态
     *
     * @return 保证金列表
     */
    List< MallAuctionMargin > selectListByMargin( MallAuctionMargin margin );

    /**
     * 查询已结束拍卖的保证金信息
     *
     * @return 保证金列表
     */
    List< Map< String,Object > > selectMarginByEnd();

}
