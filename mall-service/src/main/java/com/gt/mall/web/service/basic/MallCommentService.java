package com.gt.mall.web.service.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.util.PageUtil;

import java.util.Map;

/**
 * <p>
 * 商城评论 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommentService extends BaseService<MallComment> {

    /**
     * 查询评论列表
     *
     * @param params
     * @return
     */
    PageUtil selectCommentPage(Map<String, Object> params);

    /**
     * 删除或审核评论
     *
     * @param params
     * @return
     */
    boolean checkComment(Map<String, Object> params);

    /**
     * 回复评论信息
     *
     * @param params
     * @return
     */
    boolean replatComment(Map<String, Object> params, int userId);

}
