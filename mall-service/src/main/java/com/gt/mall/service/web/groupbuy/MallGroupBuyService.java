package com.gt.mall.service.web.groupbuy;

import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupBuyService extends BaseService< MallGroupBuy > {
    /**
     * 通过店铺id来查询团购
     *
     * @param param curPage：当前页，type：状态，shoplist：店铺Id集合
     *
     * @return 团购列表
     */
    PageUtil selectGroupBuyByShopId( Map< String,Object > param, int userId, List< Map< String,Object > > shoplist );

    /**
     * 通过团购id查询团购信息
     *
     * @param id 团购id
     *
     * @return 团购信息
     */
    Map< String,Object > selectGroupBuyById( Integer id );

    /**
     * 编辑团购
     *
     * @param params groupBuy：团购信息，specArr：团购价格信息
     * @param userId 用户ID
     *
     * @return 是否成功
     */
    int editGroupBuy( Map< String,Object > params, int userId );

    /**
     * 删除团购
     *
     * @param groupBuy 团购信息
     *
     * @return
     */
    boolean deleteGroupBuy( MallGroupBuy groupBuy );

    /**
     * 根据店铺id查询商品，并分析商品是否加入团购
     *
     * @param params curPage：当前页，shopId：店铺id，shoplist：店铺Id集合，
     *               proName：商品名称，groupId：分组Id
     *
     * @return 商品列表
     */
    PageUtil selectProByGroup( Map< String,Object > params );

    /**
     * 查询所有的团购
     *
     * @param member 用户
     * @param maps   proName：商品名称，shopId：店铺id
     *
     * @return 团购列表
     */
    List< Map< String,Object > > getGroupBuyAll( Member member, Map< String,Object > maps );

    /**
     * 根据商品id查询团购信息和团购价格
     *
     * @param proId      商品Id
     * @param shopId     店铺Id
     * @param activityId 活动Id
     *
     * @return 团购信息
     */
    MallGroupBuy getGroupBuyByProId( Integer proId, Integer shopId, int activityId );

    /**
     * 获取商品的团购信息
     *
     * @param proId  商品id
     * @param shopId 店铺id
     * @param result 返回商品详细页面的结果
     * @param member 会员
     *
     * @return 团购信息
     */
    PhoneProductDetailResult getGroupProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member );

    /**
     * 获取团购信息
     *
     * @param mallGroupBuy 团购对象
     * @param proId        商品id
     *
     * @return map
     */
    Map< String,Object > getGroupBuyById( MallGroupBuy mallGroupBuy, int proId, int joinId );

    /**
     * 通过团购id查询公众号信息
     *
     * @param id 团购id
     *
     * @return 公众号信息
     */
    WxPublicUsers wxPublicByBuyId( int id );

    /**
     * 获取正在进行的团购
     *
     * @param maps isPublic：是否发布，status：状态，id：团购Id，shopId:店铺id,productId:商品Id,
     *             proName：商品名称，groupId：分组Id,type:排序,desc：降序
     *
     * @return list
     */
    List< Map< String,Object > > selectgbProductByShopId( Map< String,Object > maps );

    /**
     * 通过团购id查询团购信息
     *
     * @param id
     * @return
     */
    //    MallGroupBuy selectById(int id);

    /**
     * 判断团购订单是否可以退款
     *
     * @param groupBuyId 团购Id
     * @param orderType  订单类型
     * @param orderId    订单id
     * @param detailId   订单详情id
     * @param buy        团购信息
     *
     * @return 是否可以退款
     */
    int groupIsReturn( int groupBuyId, String orderType, Object orderId, Object detailId, MallGroupBuy buy );

    PageUtil searchGroupBuyProduct( PhoneSearchProductDTO searchProductDTO, Member member );

    /**
     * 判断团购商品是否能购买
     * 1 判断团购商品是否正在进行
     * 2 判断购买的规格是否允许参团
     * 3 判断限购
     *
     * @param groupBuyId   团购id
     * @param invId        库存id
     * @param productNum   商品数量
     * @param memberId     粉丝id
     * @param memberBuyNum 粉丝已购买商品数量
     */
    boolean groupProductCanBuy( int groupBuyId, int invId, int productNum, int memberId, int memberBuyNum );

    /**
     * 团购订单是否能退款
     *
     * @param orderId       订单id
     * @param orderDetailId 订单详情id
     * @param groupBuyid    团购id
     *
     * @return true 能退款
     */
    boolean orderIsCanRenturn( Integer orderId, Integer orderDetailId, Integer groupBuyid );

    /**
     * 查询团购信息
     *
     * @param groupBuyId 团购id
     *
     * @return
     */
    MallGroupBuy selectBuyByProductId( Integer groupBuyId );
}
