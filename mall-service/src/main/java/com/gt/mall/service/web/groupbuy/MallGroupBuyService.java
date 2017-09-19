package com.gt.mall.service.web.groupbuy;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
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
public interface MallGroupBuyService extends BaseService<MallGroupBuy> {
    /**
     * 通过店铺id来查询团购
     *
     * @param param curPage：当前页，type：状态，shoplist：店铺Id集合
     * @return 团购列表
     */
    PageUtil selectGroupBuyByShopId(Map<String, Object> param, int userId);

    /**
     * 通过团购id查询团购信息
     *
     * @param id 团购id
     * @return 团购信息
     */
    Map<String, Object> selectGroupBuyById(Integer id);

    /**
     * 编辑团购
     *
     * @param params groupBuy：团购信息，specArr：团购价格信息
     * @param userId 用户ID
     * @return 是否成功
     */
    int editGroupBuy(Map<String, Object> params, int userId);

    /**
     * 删除团购
     *
     * @param groupBuy 团购信息
     * @return
     */
    boolean deleteGroupBuy(MallGroupBuy groupBuy);

    /**
     * 根据店铺id查询商品，并分析商品是否加入团购
     *
     * @param params curPage：当前页，shopId：店铺id，shoplist：店铺Id集合，
     *               proName：商品名称，groupId：分组Id
     * @return 商品列表
     */
    PageUtil selectProByGroup(Map<String, Object> params);

    /**
     * 查询所有的团购
     *
     * @param member 用户
     * @param maps   proName：商品名称，shopId：店铺id
     * @return 团购列表
     */
    List<Map<String, Object>> getGroupBuyAll(Member member, Map<String, Object> maps);

    /**
     * 根据商品id查询团购信息和团购价格
     *
     * @param proId  商品Id
     * @param shopId 店铺Id
     * @return 团购信息
     */
    MallGroupBuy getGroupBuyByProId(Integer proId, Integer shopId);

    /**
     * 获取团购信息
     *
     * @param memberId 用户Id
     * @param id       团购id
     * @return map
     */
    Map<String, Object> getGroupBuyById(String memberId, int id);


    /**
     * 通过团购id查询公众号信息
     *
     * @param id 团购id
     * @return 公众号信息
     */
    WxPublicUsers wxPublicByBuyId(int id);

    /**
     * 获取正在进行的团购
     *
     * @param maps isPublic：是否发布，status：状态，id：团购Id，shopId:店铺id,productId:商品Id,
     *             proName：商品名称，groupId：分组Id,type:排序,desc：降序
     * @return list
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> maps);

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
     * @return 是否可以退款
     */
    int groupIsReturn(int groupBuyId, String orderType, Object orderId, Object detailId, MallGroupBuy buy);
}
