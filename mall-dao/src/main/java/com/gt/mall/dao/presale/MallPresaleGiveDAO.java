package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleGive;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售赠送礼品设置 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleGiveDAO extends BaseMapper< MallPresaleGive > {

    /**
     * 获取用户的送礼设置
     * @param userId
     * @return
     */
    List<MallPresaleGive> selectByUserId(Integer userId);

    /**
     * 统计预售送礼的数量
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询预售送礼
     * @param params
     * @return
     */
    List<MallPresaleGive > selectByPage(Map<String, Object> params);
}