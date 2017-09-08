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
     * @param param shoplist：店铺id集合
     * @return 数量
     */
    Integer selectCountByShopId(Map<String, Object> param);

    /**
     * 通过店铺id查询物流
     *
     * @param: param shoplist：店铺id集合，firstNum：页数，maxNum 数量
     */
    List<MallFreight> selectByShopId(Map<String, Object> param);

    /**
     * 通过物流id来删除物流信息
     *
     * @param: ids 物流id集合
     */
    int deleteFreightById(Map<String, Object> ids);

    /**
     * 通过物流id查询物流信息
     *
     * @param: id 物流id
     */
    MallFreight selectFreightById(Integer id);

    /**
     * 通过店铺id查询物流信息
     *
     * @param: shopId 店铺id
     */
    MallFreight selectFreightByShopId(Integer shopId);
}