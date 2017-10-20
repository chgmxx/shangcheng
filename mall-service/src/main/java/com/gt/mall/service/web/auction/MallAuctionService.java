package com.gt.mall.service.web.auction;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.auction.MallAuction;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.utils.PageUtil;

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
public interface MallAuctionService extends BaseService< MallAuction > {

    /**
     * 通过店铺id来查询拍卖
     *
     * @param param type:状态，shoplist：店铺id集合，curPage：当前页
     *
     * @return page
     */
    PageUtil selectAuctionByShopId( Map< String,Object > param, List< Map< String,Object > > shoplist );

    /**
     * 通过拍卖id查询拍卖信息
     *
     * @param id 拍卖id
     *
     * @return Map
     */
    Map< String,Object > selectAuctionById( Integer id );

    /**
     * 编辑拍卖
     *
     * @param params auction：拍卖信息
     * @param userId 用户id
     *
     * @return int
     */
    int editAuction( Map< String,Object > params, int userId );

    /**
     * 删除拍卖
     *
     * @param auction id
     *
     * @return boolean
     */
    boolean deleteAuction( MallAuction auction );

    /**
     * 查询所有的拍卖
     *
     * @param member 用户
     * @param maps   shopId：店铺Id，proName：商品名称
     *
     * @return list
     */
    List< Map< String,Object > > getAuctionAll( Member member, Map< String,Object > maps );

    /**
     * 根据商品id查询拍卖信息和拍卖价格
     *
     * @param proId  商品Id
     * @param shopId 店铺id
     * @param aId    拍卖id
     *
     * @return MallAuction
     */
    MallAuction getAuctionByProId( Integer proId, Integer shopId, Integer aId );

    /**
     * 根据店铺id查询拍卖信息
     *
     * @param maps status：状态，id：拍卖Id，shopId:店铺id,productId:商品Id,
     *             proName：商品名称，groupId：分组Id,type:排序,desc：降序
     *
     * @return list
     */
    List< Map< String,Object > > selectgbAuctionByShopId( Map< String,Object > maps );

    /**
     * 判断是否超过了限购
     *
     * @param map      groupBuyId:分组Id
     * @param memberId 用户id
     *
     * @return map
     */
    Map< String,Object > isMaxNum( Map< String,Object > map, String memberId );

    /**
     * 查询正在拍卖的商品
     */
    PageUtil searchAuctionAll( PhoneSearchProductDTO searchProductDTO, Member member );

    /**
     * 获取商品的批发信息
     *
     * @param proId  商品id
     * @param shopId 店铺id
     * @param result 返回商品详细页面的结果
     * @param member 会员
     *
     * @return 批发信息
     */
    PhoneProductDetailResult getAuctionProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member, MallPaySet mallPaySet );

    /**
     * 判断秒杀商品是否能购买
     * 1 判断拍卖商品是否正在进行
     * 2 判断购买的规格是否允许参团
     * 3 判断限购
     *
     * @param auctionId    拍卖id
     * @param invId        库存id
     * @param productNum   商品数量
     * @param memberId     粉丝id
     * @param memberBuyNum 粉丝已购买商品数量
     */
    boolean auctionProductCanBuy( int auctionId, int invId, int productNum, int memberId, int memberBuyNum );

}
