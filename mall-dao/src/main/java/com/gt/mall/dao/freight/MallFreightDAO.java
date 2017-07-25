package com.gt.mall.dao.freight;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.freight.MallFreight;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightDAO extends BaseMapper<MallFreight> {


    /**
     * 通过 店铺id列表 得到物流总数
     *
     * @param param
     * @return
     */
    Integer selectCountByShopId(Map<String, Object> param);

    /**
     * 通过店铺id查询物流
     *
     * @param: param
     */
    List<MallFreight> selectByShopId(Map<String, Object> param);

    /**
     * 通过物流id来删除物流信息
     *
     * @param: ids
     */
    int deleteById(Map<String, Object> ids);

    /**
     * 通过物流id查询物流信息
     *
     * @param: id
     */
    MallFreight selectById(Integer id);

    /**
     * 通过店铺id查询物流信息
     *
     * @param: shopId
     */
    MallFreight selectFreightByShopId(Integer shopId);
}