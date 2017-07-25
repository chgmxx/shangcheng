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
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询积分商品
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByPage(Map<String, Object> params);

    /**
     * 查询单个的商品信息
     *
     * @param id
     * @return
     */
    Map<String, Object> selectByIds(int id);

    /**
     * 查询积分商品信息
     *
     * @param params
     * @return
     */
    List<MallIntegral> selectByIntegral(Map<String, Object> params);

    /**
     * 统计积分商品
     *
     * @param params
     * @return
     */
    int selectCountIntegralByShopids(Map<String, Object> params);

    /**
     * 查询积分商品
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectIntegralByShopids(Map<String, Object> params);
}