package com.gt.mall.dao.auction;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.auction.MallAuction;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionDAO extends BaseMapper<MallAuction> {

    /**
     * 统计拍卖信息
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询拍卖信息
     *
     * @param params
     * @return
     */
    List<MallAuction> selectByPage(Map<String, Object> params);

    /**
     * 查询未开团商品
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProByAuction(Map<String, Object> params);

    /**
     * 统计未开团商品
     *
     * @param params
     * @return
     */
    int selectCountProByAuction(Map<String, Object> params);

    /**
     * 通过id查询拍卖信息商品信息
     *
     * @param id
     * @return
     */
    Map<String, Object> selectByAuctionId(Integer id);

    /**
     * 查询是否存在未开始和进行中的商品
     *
     * @param auc
     * @return
     */
    List<MallAuction> selectAuctionByProId(MallAuction auc);

    /**
     * 查询店铺下所有的拍卖商品
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> params);

    /**
     * 根据id查询拍卖
     *
     * @param id
     * @return
     */
    MallAuction selectAuctionByIds(Integer id);

    /**
     * 根据商品id查询拍卖信息
     *
     * @param auc
     * @return
     */
    MallAuction selectBuyByProductId(MallAuction auc);

    /**
     * 统计店铺下所有拍卖商品的数量
     *
     * @param params
     * @return
     */
    int selectCountByShopId(Map<String, Object> params);
}