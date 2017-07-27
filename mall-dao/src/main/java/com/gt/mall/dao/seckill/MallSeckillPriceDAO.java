package com.gt.mall.dao.seckill;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seckill.MallSeckillPrice;

import java.util.List;

/**
 * <p>
 * 秒杀价格表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSeckillPriceDAO extends BaseMapper< MallSeckillPrice > {

    List<MallSeckillPrice> selectPriceByGroupId(Integer SeckillId);

    int updateBySeckillId(MallSeckillPrice record);
}