package com.gt.mall.dao.integral;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.integral.MallIntegral;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商品表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallIntegralDAO extends BaseMapper<MallIntegral> {

    /**
     * 统计积分商品
     *
     * @param params type：状态，shopId：店铺id，shoplist:店铺Id集合
     * @return 数量
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询积分商品
     *
     * @param params type：状态，shopId：店铺id，shoplist:店铺Id集合，firstNum：页数，maxNum：记录数
     * @return 积分商品列表
     */
    List<Map<String, Object>> selectByPage(Map<String, Object> params);

    /**
     * 查询单个的商品信息
     *
     * @param id 积分商品Id
     * @return 商品信息
     */
    Map<String, Object> selectByIds(int id);

    /**
     * 查询积分商品信息
     *
     * @param params  productId：商品Id，shopId：店铺Id,isUse:是否生效，userId：用户Id
     * @return 积分商品列表
     */
    List<MallIntegral> selectByIntegral(Map<String, Object> params);

    /**
     * 统计积分商品
     *
     * @param params shopId：店铺id，shoplist:店铺Id集合
     * @return 数量
     */
    int selectCountIntegralByShopids(Map<String, Object> params);

    /**
     * 查询积分商品
     *
     * @param params shopId：店铺id，shoplist:店铺Id集合 ,firstNum：页数，maxNum：记录数
     * @return 积分商品列表
     */
    List<Map<String, Object>> selectIntegralByShopids(Map<String, Object> params);
}