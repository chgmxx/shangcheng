package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallCommentGive;

import java.util.List;

/**
 * <p>
 * 评论送礼 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommentGiveDAO extends BaseMapper< MallCommentGive > {

    /**
     * 查询评论送礼信息
     * @param give
     * @return
     */
    MallCommentGive selectByGive(MallCommentGive give);

    List<MallCommentGive> getGiveByUserId(int userId);
}