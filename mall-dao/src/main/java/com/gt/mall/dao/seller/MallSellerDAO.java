package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSeller;

/**
 * <p>
 * 超级销售员 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerDAO extends BaseMapper< MallSeller > {

    /**
     * 修改销售员状态
     * @param user_id
     * @return
     */
    boolean updateStatusByUserId(Integer status, Integer user_id);
}