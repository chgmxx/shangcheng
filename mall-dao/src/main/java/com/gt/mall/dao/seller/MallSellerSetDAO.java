package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerSet;

/**
 * <p>
 * 超级销售员设置 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerSetDAO extends BaseMapper< MallSellerSet > {

    /**
     * 通过商家id查询销售规则
     * @param busUserId
     * @return
     */
    MallSellerSet selectByBusUserId(int busUserId);
}