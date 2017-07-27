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
     * @param params curPage:当前页，checkStatus：审核状态，feel：总体评价，shoplist：店铺id集合
     * @return 评论列表
     */
    PageUtil selectCommentPage(Map<String, Object> params);

    /**
     * 删除或审核评论
     *
     * @param params ids:评论id集合，isDelete;是否删除，checkStatus：审核状态
     * @return boolean
     */
    boolean checkComment(Map<String, Object> params);

    /**
     * 回复评论信息
     *
     * @param params params：评论信息，
     * @param userId 用户Id
     * @return boolean
     */
    boolean replatComment(Map<String, Object> params, int userId);

}
