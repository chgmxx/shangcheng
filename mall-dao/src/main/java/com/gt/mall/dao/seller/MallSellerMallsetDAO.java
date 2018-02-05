package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerMallset;

/**
 * <p>
 * 超级销售员商城设置 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerMallsetDAO extends BaseMapper< MallSellerMallset > {

    /**
     * 通过销售员的id查询商城设置
     *
     * @param saleMemberId
     *
     * @return
     */
    MallSellerMallset selectBySaleMemberId( int saleMemberId );
}
