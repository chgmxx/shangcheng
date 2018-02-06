package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallCountIncome;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日收入统计表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-10-16
 */
public interface MallCountIncomeDAO extends BaseMapper< MallCountIncome > {

    /**
     * 获取 营业金额或收入金额
     *
     * @param params
     *
     * @return
     */
    String getCountByTimes( Map< String,Object > params );

    /**
     * 获取时间段的收入金额列表
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > getCountListByTimes( Map< String,Object > params );

}
