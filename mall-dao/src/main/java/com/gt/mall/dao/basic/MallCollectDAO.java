package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallCollect;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收藏表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCollectDAO extends BaseMapper<MallCollect> {

    /**
     * 根据用户、商品查询
     *
     * @param map userId:用户id，productId：商品Id
     * @return 收藏信息
     */
    MallCollect selectByCollect(Map<String, Object> map);

    /**
     * 批量修改收藏信息
     *
     * @param params ids:收藏id集合，isDelete;是否删除，isCollect：是否收藏
     * @return 是否成功
     */
    int batchUpdateCollect(Map<String, Object> params);

    /**
     * 通过会员id查询会员收藏的信息
     */
    List<Map<String,Object>> selectCollectByMemberId(Map<String,Object> params);
}