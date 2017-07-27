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
     * @param params userId：用户Id
     * @return 数量
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询积分商城图片
     *
     * @param params userId:用户id，firstNum：页数，maxNum：记录数
     * @return 积分商城图片列表
     */
    List<Map<String, Object>> selectByPage(Map<String, Object> params);

    /**
     * 查询积分商城图片
     *
     * @param params userId：用户Id，shopId：店铺id
     * @return 积分商城图片
     */
    List<MallIntegralImage> selectByImage(Map<String, Object> params);
}