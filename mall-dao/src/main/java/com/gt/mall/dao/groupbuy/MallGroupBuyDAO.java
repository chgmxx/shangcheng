package com.gt.mall.dao.groupbuy;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.product.MallGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupBuyDAO extends BaseMapper< MallGroupBuy > {

    /**
     * 统计团购信息
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询团购信息
     *
     * @param params
     * @return
     */
    List<MallGroupBuy> selectByPage(Map<String, Object> params);

    /**
     * 查询未开团商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProByGroup(Map<String, Object> params);

    /**
     * 统计未开团商品
     * @param params
     * @return
     */
    int selectCountProByGroup(Map<String, Object> params);

    /**
     * 通过id查询团购信息商品信息
     * @param id
     * @return
     */
    Map<String, Object> selectByGroupBuyId(Integer id);

    /**
     * 查询是否存在未开始和进行中的商品
     * @param buy
     * @return
     */
    List<MallGroupBuy> selectGroupByProId(MallGroupBuy buy);

    /**
     * 查询店铺下所有的团购商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> params);

    /**
     * 根据id查询团购
     * @param id
     * @return
     */
    MallGroupBuy selectGroupByIds(Integer id);

    /**
     * 根据商品id查询团购信息
     * @param buy
     * @return
     */
    MallGroupBuy selectBuyByProductId(MallGroupBuy buy);

    /**
     *  查询已结束未成团的团购信息
     * @return
     */
    List<Map<String, Object>> selectEndGroupByAll();

    /**
     * 统计团购
     * @param params
     * @return
     */
    int selectCountByShopId(Map<String, Object> params);

    /**
     * 通过商品id查询团购信息
     * @param productId
     * @return
     */
    Map<String, Object> selectGroupByProId(@Param( "productId" ) String productId);
}