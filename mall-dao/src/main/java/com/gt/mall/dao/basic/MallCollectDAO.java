package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallCollect;

import java.util.Map;

/**
 * <p>
 * 收藏表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCollectDAO extends BaseMapper<MallCollect> {

    /**
     * 根据用户、商品查询
     *
     * @param map
     * @return
     */
    MallCollect selectByCollect(Map<String, Object> map);

    /**
     * 批量修改收藏信息
     *
     * @param params
     * @return
     */
    int batchUpdateCollect(Map<String, Object> params);
}