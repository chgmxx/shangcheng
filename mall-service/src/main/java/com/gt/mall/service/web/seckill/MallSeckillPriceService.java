package com.gt.mall.service.web.seckill;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.seckill.MallSeckillPrice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀价格表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSeckillPriceService extends BaseService< MallSeckillPrice > {

    /**
     * 编辑秒杀价格
     *
     * @param map       编辑参数
     * @param seckillId 秒杀id
     * @param flag      是否更换了商品  true 更换了
     * @param isJxc     是否已经加入金校训
     *
     * @return 商品信息
     */
    List< Map< String,Object > > editSeckillPrice( Map< String,Object > map, int seckillId, boolean flag, long isJxc );

    /**
     * 通过秒杀id查询秒杀信息
     *
     * @param groupId 秒杀id
     *
     * @return 秒杀信息
     */
    List< MallSeckillPrice > selectPriceByGroupId( int groupId );

}
