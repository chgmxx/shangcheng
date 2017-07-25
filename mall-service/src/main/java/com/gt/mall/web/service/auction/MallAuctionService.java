package com.gt.mall.web.service.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAuctionService extends BaseService<MallAuction> {

    /**
     * 通过店铺id来查询拍卖
     *
     * @Title: selectFreightByShopId
     */
    PageUtil selectAuctionByShopId(Map<String, Object> param);

    /**
     * 通过拍卖id查询拍卖信息
     */
    Map<String, Object> selectAuctionById(Integer id);

    /**
     * 编辑拍卖
     *
     * @Title: editFreight
     */
    int editAuction(Map<String, Object> params, int userId);

    /**
     * 删除拍卖
     *
     * @Title: deleteFreight
     */
    boolean deleteAuction(MallAuction auction);

    /**
     * 查询所有的拍卖
     *
     * @param member
     * @return
     */
    List<Map<String, Object>> getAuctionAll(Member member, Map<String, Object> maps);

    /**
     * 根据商品id查询拍卖信息和拍卖价格
     *
     * @return
     */
    MallAuction getAuctionByProId(Integer proId, Integer shopId, Integer aId);


    /**
     * 根据店铺id查询拍卖信息
     */
    List<Map<String, Object>> selectgbAuctionByShopId(Map<String, Object> maps);

    /**
     * 判断是否超过了限购
     *
     * @param map
     * @param memberId
     * @return
     */
    Map<String, Object> isMaxNum(Map<String, Object> map, String memberId);

}
