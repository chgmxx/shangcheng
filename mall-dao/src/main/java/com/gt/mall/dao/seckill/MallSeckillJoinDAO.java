package com.gt.mall.dao.seckill;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seckill.MallSeckillJoin;

import java.util.Map;

/**
 * <p>
 * 秒杀参与表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSeckillJoinDAO extends BaseMapper< MallSeckillJoin > {

    /**
     * 查询用户参加秒杀的数量
     * @param params
     * @return
     */
    int selectCountByBuyId(Map<String, Object> params);
}