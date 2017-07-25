package com.gt.mall.web.service.groupbuy;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.util.PageUtil;

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
     * @Title: selectFreightByShopId
     */
    PageUtil selectGroupBuyByShopId(Map<String, Object> param);

    /**
     * 通过团购id查询团购信息
     */
    Map<String, Object> selectGroupBuyById(Integer id);

    /**
     * 编辑团购
     *
     * @Title: editFreight
     */
    int editGroupBuy(Map<String, Object> params, int userId);

    /**
     * 删除团购
     *
     * @Title: deleteFreight
     */
    boolean deleteGroupBuy(MallGroupBuy groupBuy);

    /**
     * 根据店铺id查询商品，并分析商品是否加入团购
     *
     * @param params
     * @return
     */
    PageUtil selectProByGroup(Map<String, Object> params);

     /**
     * 查询所有的团购
     *
     * @param member
     * @return
     */
    List<Map<String, Object>> getGroupBuyAll(Member member, Map<String, Object> maps);

    /**
     * 根据商品id查询团购信息和团购价格
     *
     * @return
     */
    MallGroupBuy getGroupBuyByProId(Integer proId, Integer shopId);

    /**
     * 获取团购信息
     */
    Map<String, Object> getGroupBuyById(String memberId, int id);



    /**
     * 通过团购id查询公众号信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> wxPublicByBuyId(int id);

    /**
     * 获取正在进行的团购
     *
     * @param maps
     * @return
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
     * @return
     */
    int groupIsReturn(int groupBuyId, String orderType, Object orderId, Object detailId, MallGroupBuy buy);
}
