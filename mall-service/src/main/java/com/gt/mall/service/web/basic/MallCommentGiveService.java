package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallCommentGive;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论送礼 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommentGiveService extends BaseService<MallCommentGive> {

    /**
     * 评论赠送商品
     *
     * @param commentId 评论id
     * @param memberId  评论用户
     */
    void commentGive(int commentId, HttpServletRequest request, int memberId);

    /**
     *  编辑评论送礼
     * @param giveList 送礼列表
     * @param user 用户
     * @return boolean
     */
    boolean editCommentGive(List<MallCommentGive> giveList, BusUser user);

    /**
     * 通过用户id查询评论送礼设置
     *
     * @param userId 用户Id
     * @return 评论送礼列表
     */
    List<MallCommentGive> getGiveByUserId(int userId);
}
