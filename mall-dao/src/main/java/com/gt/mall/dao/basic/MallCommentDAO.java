package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallComment;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城评论 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommentDAO extends BaseMapper<MallComment> {
    /**
     * 分页查询评论
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    List findByPage(Map<String, Object> params);

    /**
     * 获取评论数量
     *
     * @param param
     * @return
     */
    int countAppraise(Map<String, Object> param);

    /**
     * 查询店家回复消息
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> ownerResponseList(Map<String, Object> param);


    /**
     * 根据商品id查询评价信息
     *
     * @param params 商品id
     * @return
     */
    List<Map<String, Object>> selectCommentByProId(Map<String, Object> params);

    /**
     * 统计总体评价
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCountFeel(Map<String, Object> params);

    /**
     * 统计评价数量
     *
     * @param params
     * @return
     */
    int selectCommentCount(Map<String, Object> params);

    /**
     * 查询评价列表
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCommentList(Map<String, Object> params);

    /**
     * 批量操作评论信息
     *
     * @param params
     * @return
     */
    int batchUpdateComment(Map<String, Object> params);

    /**
     * 查询评价信息
     *
     * @param comment
     * @return
     */
    MallComment selectByComment(MallComment comment);
}