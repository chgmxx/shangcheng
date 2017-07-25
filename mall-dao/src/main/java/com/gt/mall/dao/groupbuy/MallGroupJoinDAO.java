package com.gt.mall.dao.groupbuy;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.groupbuy.MallGroupJoin;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参团表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupJoinDAO extends BaseMapper< MallGroupJoin > {

    /**
     * 查询团购商品的发起人
     * @param params
     * @return
     */
    List<Map<String, Object>> selectJoinGroupByProId(Map<String, Object> params);

    /**
     * 查询团购参与人、商品信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectJoinByjoinId(Map<String, Object> params);

    /**
     * 查询团购参与人
     * @param params
     * @return
     */
    List<MallGroupJoin> selectByProJoinId(Map<String, Object> params);

    /**
     * 查询用户参加团购的数量
     * @param params
     * @return
     */
    int selectCountByBuyId(Map<String, Object> params);

    /**
     * 查询参团信息
     * @param params
     * @return
     */
    List<MallGroupJoin> selectByPJoinId(Map<String, Object> params);
}