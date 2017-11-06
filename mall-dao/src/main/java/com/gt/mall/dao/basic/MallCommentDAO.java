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
public interface MallCommentDAO extends BaseMapper< MallComment > {
    /**
     * 分页查询评论
     *
     * @param params type:是否删除，oldMemberIds:用户集合，或 memberId：用户id
     *
     * @return 评论列表
     */
    List< Map< String,Object > > findByPage( Map< String,Object > params );

    /**
     * 获取评论数量
     *
     * @param param oldMemberIds:用户集合，或 memberId：用户id
     *
     * @return 数量
     */
    int countAppraise( Map< String,Object > param );

    /**
     * 查询店家回复消息
     *
     * @param param appraise:商家回复的评论id
     *
     * @return 回复消息列表
     */
    List< MallComment > ownerResponseList( Map< String,Object > param );

    /**
     * 根据商品id查询评价信息
     *
     * @param params checkStatus：审核状态，feel：总体评价，productId:商品Id，repPId:商家回复的评论id
     *
     * @return 评价列表
     */
    List< Map< String,Object > > selectCommentByProId( Map< String,Object > params );

    /**
     * 统计总体评价
     *
     * @param params checkStatus：审核状态，feel：总体评价，productId:商品Id
     *
     * @return 总体评价列表
     */
    List< Map< String,Object > > selectCountFeel( Map< String,Object > params );

    /**
     * 统计评价数量
     *
     * @param params checkStatus：审核状态，feel：总体评价，shoplist：店铺id集合
     *
     * @return 数量
     */
    int selectCommentCount( Map< String,Object > params );

    /**
     * 查询评价列表
     *
     * @param params checkStatus：审核状态，feel：总体评价，shoplist：店铺id集合，firstNum：页数，maxNum 数量
     *
     * @return 评价列表
     */
    List< Map< String,Object > > selectCommentList( Map< String,Object > params );

    /**
     * 批量操作评论信息
     *
     * @param params ids:评论id集合，isDelete;是否删除，checkStatus：审核状态
     *
     * @return 是否成功
     */
    int batchUpdateComment( Map< String,Object > params );

    /**
     * 查询评价信息
     *
     * @param comment orderDetailId：商品详情Id，orderId：订单Id，userId：用户Id
     *
     * @return 评价信息
     */
    MallComment selectByComment( MallComment comment );
}