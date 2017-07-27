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
public interface MallGroupJoinDAO extends BaseMapper<MallGroupJoin> {

    /**
     * 查询团购商品的发起人
     *
     * @param params groupBuyId：团购id，joinUserId：用户id,buyerUserId:用户Id
     * @return 团购信息
     */
    List<Map<String, Object>> selectJoinGroupByProId(Map<String, Object> params);

    /**
     * 查询团购参与人、商品信息
     *
     * @param params groupBuyId：团购id，joinId：参团id
     * @return 参与人、商品信息
     */
    List<Map<String, Object>> selectJoinByJoinId(Map<String, Object> params);

    /**
     * 查询团购参与人
     *
     * @param params groupBuyId：团购id，id：参团id
     * @return 团购参与人列表
     */
    List<MallGroupJoin> selectByProJoinId(Map<String, Object> params);

    /**
     * 查询用户参加团购的数量
     *
     * @param params groupBuyId：团购id，joinUserId：用户id
     * @return 数量
     */
    int selectCountByBuyId(Map<String, Object> params);

    /**
     * 查询参团信息
     *
     * @param params groupBuyId：团购id，joinId：参团id
     * @return 参团信息列表
     */
    List<MallGroupJoin> selectByPJoinId(Map<String, Object> params);
}