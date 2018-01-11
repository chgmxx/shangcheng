package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.result.phone.comment.PhoneCommentListResult;
import com.gt.mall.result.phone.comment.PhoneCommentProductResult;
import com.gt.mall.utils.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城评论 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommentService extends BaseService< MallComment > {

    /**
     * 统计各评价状态的数量
     *
     * @param params shoplist：店铺id集合
     *
     * @return 评论列表
     */
    Map< String,Object > selectCommentCount( Map< String,Object > params );

    /**
     * 查询评论列表
     *
     * @param params curPage:当前页，checkStatus：审核状态，feel：总体评价，shoplist：店铺id集合
     *
     * @return 评论列表
     */
    PageUtil selectCommentPage( Map< String,Object > params, List< Map< String,Object > > shoplist );

    /**
     * 添加评论
     */
    MallComment addAppraise( String imageUrls, MallComment comment, HttpServletRequest request );

    /**
     * 查询商品评论
     *
     * @param busId     商家id
     * @param productId 商品id
     * @param feel      评论状态
     *
     * @return 商品评论
     */
    Map< String,Object > getProductComment( int busId, int productId, String feel );

    /**
     * 查询评论的商品信息
     *
     * @param orderDetailId 订单详情id
     *
     * @return 商品信息
     */
    PhoneCommentProductResult getCommentProduct( Integer orderDetailId );

    /**
     * 查询评论信息
     */
    MallComment selectComment( MallComment comment );

    /**
     * 我的评论列表
     *
     * @param memberId 粉丝id
     * @param busId    商家id
     * @param curPage  当前页面
     *
     * @return 评论列表
     */
    PhoneCommentListResult myCommentList( Integer memberId, Integer busId, Integer curPage );

}
