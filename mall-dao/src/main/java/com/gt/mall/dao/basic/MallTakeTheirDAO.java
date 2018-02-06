package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallTakeTheir;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 到店自提表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallTakeTheirDAO extends BaseMapper< MallTakeTheir > {

    /**
     * 统计上门自提
     *
     * @param map userId：用户Id
     * @return 数量
     */
    //    Integer selectCount(Map<String, Object> map);

    /**
     * 查询上门自提
     *
     * @param map userId：用户Id，firstNum：页数，maxNum 数量
     *
     * @return 上门自提列表
     */
    List< MallTakeTheir > selectTakeTheirList( Map< String,Object > map );

    /**
     * 根据公众号id查询上门自提信息
     *
     * @param map userId：用户Id，provinceId：省份id，takeId：自提id
     *
     * @return 上门自提列表
     */
    List< MallTakeTheir > selectByUserId( Map< String,Object > map );

    /**
     * 统计上门自提
     *
     * @param map userId：用户id
     *
     * @return 数量
     */
    Integer selectCountByBusUserId( Map< String,Object > map );

    /**
     * 根据id查询上门自提信息
     *
     * @param map id：自提id
     *
     * @return 上门自提
     */
    MallTakeTheir selectByIds( Map< String,Object > map );
}
