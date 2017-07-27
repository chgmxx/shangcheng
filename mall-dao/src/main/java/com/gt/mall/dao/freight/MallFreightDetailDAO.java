package com.gt.mall.dao.freight;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.freight.MallFreightDetail;

import java.util.Map;

/**
 * <p>
 * 物流详情表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightDetailDAO extends BaseMapper<MallFreightDetail> {

    /**
     * 通过物流id来删除物流详情
     *
     * @param ids 物流id集合
     * @return 是否成功
     */
    int deleteByFreightId(Map<String, Object> ids);

    /**
     * 通过物流id查询 物流详情id
     *
     * @param freightId 物流id
     * @return 物流详情id
     */
    Map<String, Object> selectByFreightId(Integer freightId);

    /**
     * 查询物流详情明细
     *
     * @param params freightId：物流Id，provinceId：省份id
     * @return 物流详情明细
     */
    MallFreightDetail selectFreightByPId(Map<String, Object> params);
}