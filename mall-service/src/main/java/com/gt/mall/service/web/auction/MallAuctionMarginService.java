package com.gt.mall.service.web.auction;

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
public interface MallAuctionMarginService extends BaseService< MallAuctionMargin > {

    /**
     * 通过店铺id来查询保证金
     *
     * @param param shoplist：店铺Id集合,curPage:当前页
     *
     * @return 保证金列表
     */
    PageUtil selectMarginByShopId( Map< String,Object > param, int userId );

    /**
     * 交纳保证金成功返回
     *
     * @param params out_trade_no：拍卖单号,transaction_id:支付单号
     *
     * @return 是否成功
     */
    int paySuccessAuction( Map< String,Object > params );

    /**
     * 交纳保证金
     *
     * @param params   margin:保证金信息
     * @param memberId 用户Id
     *
     * @return map
     */
    Map< String,Object > addMargin( Map< String,Object > params, String memberId );

    /**
     * 根据用户id查询我的所有的保证金
     *
     * @param margin userId：用户Id，oldUserIdList：用户id集合，aucId：拍卖Id，marginStatus：保证金状态
     *
     * @return 保证金列表
     */
    List< MallAuctionMargin > getMyAuction( MallAuctionMargin margin );

    /**
     * 退还保证金
     *
     * @throws Exception exception
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
     * @param map memberId:用户id,payWay：支付方式，money：金额，aucNo：拍卖单号
     *
     * @return map
     * @throws Exception exception
     */
    Map< String,Object > returnEndMargin( Map< String,Object > map ) throws Exception;

    /**
     * 退款保证金（支付宝）
     *
     * @param params outTradeNo：拍卖单号
     */
    void returnAlipayMargin( Map< String,Object > params );
}
