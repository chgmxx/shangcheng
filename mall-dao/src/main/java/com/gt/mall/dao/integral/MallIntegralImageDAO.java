package com.gt.mall.dao.integral;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.integral.MallIntegralImage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城图片循环 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallIntegralImageDAO extends BaseMapper<MallIntegralImage> {

    /**
     * 统计积分商城图片
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询积分商城图片
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByPage(Map<String, Object> params);

    /**
     * 查询积分商城图片
     *
     * @param params
     * @return
     */
    List<MallIntegralImage> selectByImage(Map<String, Object> params);
}