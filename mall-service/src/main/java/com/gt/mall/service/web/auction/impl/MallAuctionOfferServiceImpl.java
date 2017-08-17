package com.gt.mall.service.web.auction.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.auction.MallAuctionBiddingDAO;
import com.gt.mall.dao.auction.MallAuctionOfferDAO;
import com.gt.mall.entity.auction.MallAuctionBidding;
import com.gt.mall.entity.auction.MallAuctionOffer;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.service.web.auction.MallAuctionOfferService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 拍卖出价表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAuctionOfferServiceImpl extends BaseServiceImpl<MallAuctionOfferDAO, MallAuctionOffer> implements MallAuctionOfferService {

    private Logger log = Logger.getLogger(MallAuctionOfferServiceImpl.class);

    @Autowired
    private MallAuctionOfferDAO auctionOfferDAO;
    @Autowired
    private MallAuctionBiddingDAO auctionBiddingDAO;

    @Override
    public Map<String, Object> addOffer(Map<String, Object> params, String memberId) {
        boolean flag = true;
        Map<String, Object> result = new HashMap<String, Object>();
        String msg = "";
        MallAuctionOffer offer = (MallAuctionOffer) JSONObject.toBean(JSONObject.fromObject(params.get("offer")), MallAuctionOffer.class);

        String key =  "zAuctionOffer_" + offer.getAucId();
        Double money = Double.parseDouble(offer.getOfferMoney().toString());
        String value = memberId;
        if (JedisUtil.exists(key)) {
            Set<String> set = JedisUtil.zSort(key, 0, 0);
            for (String str : set) {
                double nowMoney = JedisUtil.zScore(key, str);
                if (str.equals(memberId)) {
                    msg = "您的出价目前已经领先了，不能再次出价";
                    flag = false;
                    break;
                } else if (nowMoney >= money) {
                    msg = "有人抢先一步出价了，请重新出价";
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            offer.setCreateTime(new Date());
            offer.setUserId(Integer.parseInt(memberId));
            auctionOfferDAO.insert(offer);
        } else {//已经交纳了保证金，无需再次交纳
            result.put("result", false);
            result.put("msg", msg);
        }
        if (CommonUtil.isNotEmpty(offer.getId())) {
            if (offer.getId() > 0) {
                MallAuctionBidding bid = (MallAuctionBidding) JSONObject.toBean(JSONObject.fromObject(params.get("bid")), MallAuctionBidding.class);
                bid.setUserId(Integer.parseInt(memberId));
                MallAuctionBidding bidding = auctionBiddingDAO.selectByBidding(bid);
                if (bidding == null) {
                    bid.setCreateTime(new Date());
                    auctionBiddingDAO.insert(bid);
                } else {
                    bid.setAucStatus(0);
                    bid.setId(bidding.getId());
                    auctionBiddingDAO.updateBidByAucId(bid);
                }
                JedisUtil.zAdd(key, money, value);
                result.put("result", true);
                result.put("msg", "出价成功");
            } else {
                result.put("result", false);
                result.put("msg", "出价失败");
            }
        }
        return result;

    }
}
