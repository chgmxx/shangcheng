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
     * @param params type:状态，shoplist：店铺id集合
     * @return 数量
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询拍卖信息
     *
     * @param params type:状态，shoplist：店铺id集合
     * @return 拍卖信息列表
     */
    List<MallAuction> selectByPage(Map<String, Object> params);

    /**
     * 查询未开团商品
     *
     * @param params shopId:店铺id,proName：商品名称，groupId：分组Id,firstNum：页数，maxNum 数量
     * @return 未开团商品列表
     */
    List<Map<String, Object>> selectProByAuction(Map<String, Object> params);

    /**
     * 统计未开团商品
     *
     * @param params shopId:店铺id,proName：商品名称，groupId：分组Id
     * @return 未开团商品数量
     */
    int selectCountProByAuction(Map<String, Object> params);

    /**
     * 通过id查询拍卖信息商品信息
     *
     * @param id 拍卖id
     * @return 拍卖商品信息
     */
    Map<String, Object> selectByAuctionId(Integer id);

    /**
     * 查询是否存在未开始和进行中的商品
     *
     * @param auc  productId:商品Id，id:拍卖id
     * @return 拍卖商品列表
     */
    List<MallAuction> selectAuctionByProId(MallAuction auc);

    /**
     * 查询店铺下所有的拍卖商品
     *
     * @param params status：状态，id：拍卖Id，shopId:店铺id,productId:商品Id,
     *               proName：商品名称，groupId：分组Id,type:排序,desc：降序
     * @return 拍卖商品
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> params);

    /**
     * 根据id查询拍卖
     *
     * @param id 拍卖id
     * @return 拍卖信息
     */
    MallAuction selectAuctionByIds(Integer id);

    /**
     * 根据商品id查询拍卖信息
     *
     * @param auc id:拍卖Id，shopId:店铺id，productId:商品Id
     * @return 拍卖信息
     */
    MallAuction selectBuyByProductId(MallAuction auc);

    /**
     * 统计店铺下所有拍卖商品的数量
     *
     * @param params isPublic:是否发布，status：状态，shopId:店铺id，productId:商品Id
     * @return 数量
     */
    int selectCountByShopId(Map<String, Object> params);
}