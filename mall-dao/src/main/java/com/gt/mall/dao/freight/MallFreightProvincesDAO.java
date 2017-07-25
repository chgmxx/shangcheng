package com.gt.mall.dao.freight;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.freight.MallFreightProvinces;

import java.util.Map;

/**
 * <p>
 * 物流配送区域 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightProvincesDAO extends BaseMapper<MallFreightProvinces> {

    /**
     * 通过物流id查询物流配送区域id
     *
     * @param freightId
     * @return
     */
    Map<String, Object> selectByFreightId(Integer freightId);
}