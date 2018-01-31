package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallIncomeList;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收入记录表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2018-01-30
 */
public interface MallIncomeListDAO extends BaseMapper< MallIncomeList > {

    /**
     * 获取条数
     */
    int tradeCount( Map< String,Object > params );

    /**
     * 分页查询
     */
    List< Map< String,Object > > findByTradePage( Map< String,Object > params );
}