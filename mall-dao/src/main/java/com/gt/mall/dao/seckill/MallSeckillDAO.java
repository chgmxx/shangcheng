package com.gt.mall.dao.seckill;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seckill.MallSeckill;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSeckillDAO extends BaseMapper< MallSeckill > {


    /**
     * 统计秒杀信息
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询秒杀信息
     *
     * @param params
     * @return
     */
    List<MallSeckill> selectByPage(Map<String, Object> params);

    /**
     * 查询未开团商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProBySeckill(Map<String, Object> params);

    /**
     * 统计未开团商品
     * @param params
     * @return
     */
    int selectCountProBySeckill(Map<String, Object> params);

    /**
     * 通过id查询秒杀信息商品信息
     * @param id
     * @return
     */
    Map<String, Object> selectBySeckillId(Integer id);

    /**
     * 查询是否存在未开始和进行中的商品
     * @param buy
     * @return
     */
    List<MallSeckill> selectSeckillByProId(MallSeckill buy);

    /**
     * 查询店铺下所有的秒杀商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> params);

    /**
     * 根据id查询秒杀
     * @param id 秒杀id
     * @return
     */
    MallSeckill selectSeckillByIds(Integer id);

    /**
     * 根据商品id查询秒杀信息
     * @param seckill 秒杀信息
     * @return 秒杀信息
     */
    MallSeckill selectBuyByProductId(MallSeckill seckill);

    /**
     * 查询店铺下所有的秒杀商品
     * @param params
     * @return
     */
    int selectCountByShopId(Map<String, Object> params);
}