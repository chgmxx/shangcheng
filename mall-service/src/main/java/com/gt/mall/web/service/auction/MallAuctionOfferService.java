package com.gt.mall.web.service.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖出价表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionOfferService extends BaseService<MallAuctionOffer> {

    /**
     * 出价
     *
     * @param params
     * @param memberId
     * @return
     */
    Map<String, Object> addOffer(Map<String, Object> params, String memberId);

}
