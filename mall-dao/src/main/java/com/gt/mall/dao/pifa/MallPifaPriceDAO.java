package com.gt.mall.dao.pifa;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.pifa.MallPifaPrice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批发价格表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaPriceDAO extends BaseMapper< MallPifaPrice > {

    /**
     * 通过批发id查询批发信息
     *
     * @param groupId
     *
     * @return
     */
    List< MallPifaPrice > selectPriceByGroupId( int groupId );

    /**
     * 通过批发id修改批发价格信息
     *
     * @param price
     *
     * @return
     */
    int updateByPifaId( MallPifaPrice price );

    /**
     * 根据商品id和库存id查询批发价
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > selectPriceByInvId( Map< String,Object > params );

}
