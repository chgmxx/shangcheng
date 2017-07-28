package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallPaySet;

/**
 * <p>
 * 商城交易支付设置 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPaySetDAO extends BaseMapper<MallPaySet> {

    /**
     * 通过用户 查询商城交易支付设置
     *
     * @param set userId：用户Id
     * @return 商城交易支付设置
     */
//    MallPaySet selectByUserId(MallPaySet set);

}