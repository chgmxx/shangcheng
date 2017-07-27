package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresaleGive;

import java.util.List;

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
}