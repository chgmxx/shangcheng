package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresaleRank;

import java.util.Map;

/**
 * <p>
 * 预售送礼名次 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleRankDAO extends BaseMapper< MallPresaleRank > {

    /**
     * 通过预售id查询预定的排名
     * @param maps
     * @return
     */
    MallPresaleRank selectByPresaleId(Map<String, Object> maps);

    /**
     * 根据预售id。。。查询预售送礼
     * @param rank
     * @return
     */
    MallPresaleRank selectByPresale(MallPresaleRank rank);
}