package com.gt.mall.web.service.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞拍保证金表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionMarginService extends BaseService<MallAuctionMargin> {

    /**
     * 通过店铺id来查询保证金
     *
     * @Title: selectFreightByShopId
     */
    PageUtil selectMarginByShopId(Map<String, Object> param);

    /**
     * 交纳保证金成功返回
     *
     * @param params
     * @return
     */
    int paySuccessAuction(Map<String, Object> params);

    /**
     * 交纳保证金
     *
     * @param params
     * @param memberId
     * @return
     */
    Map<String, Object> addMargin(Map<String, Object> params, String memberId);

    /**
     * 根据用户id查询我的所有的保证金
     *
     * @param margin
     * @return
     */
    List<MallAuctionMargin> getMyAuction(MallAuctionMargin margin);

    /**
     * 退还保证金
     *
     * @return
     */
    void returnMargin() throws Exception;

    /**
     * 根据定金id查询保证金信息
     *
     * @param marginId
     * @return
     */
//    MallAuctionMargin selectByMarginId(int marginId);

    /**
     * 退款保证金（微信和储值卡）
     *
     * @param map
     * @throws Exception
     */
    Map<String, Object> returnEndMargin(Map<String, Object> map) throws Exception;

    /**
     * 退款保证金（支付宝）
     *
     * @param params
     */
    void returnAlipayMargin(Map<String, Object> params);
}
