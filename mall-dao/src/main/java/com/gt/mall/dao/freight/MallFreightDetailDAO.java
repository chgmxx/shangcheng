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
public interface MallFreightDetailDAO extends BaseMapper< MallFreightDetail > {

    /**
     * 通过物流id来删除物流详情
     *
     * @Title: deleteByFreightId
     */
    int deleteByFreightId(Map<String, Object> ids);

    /**
     * 通过物流id查询物流详情id
     *
     * @Title: selectByFreightId
     */
    Map<String, Object> selectByFreightId(Integer freightId);

    /**
     * 查询物流详情细心
     * @Title: selectFreightByPId
     */
    MallFreightDetail selectFreightByPId(Map<String, Object> params);
}