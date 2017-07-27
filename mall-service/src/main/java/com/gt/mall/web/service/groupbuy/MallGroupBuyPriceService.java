package com.gt.mall.web.service.groupbuy;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购价格表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupBuyPriceService extends BaseService<MallGroupBuyPrice> {

    /**
     * 编辑团购价格
     *
     * @param map        priceList：团购价格列表
     * @param groupBuyId 团购Id
     * @param flag       是否更换商品
     */
    void editGroupBuyPrice(Map<String, Object> map, int groupBuyId, boolean flag);

    /**
     * 通过团购id查询团购信息
     *
     * @param groupBuyId 团购Id
     * @return
     */
    List<MallGroupBuyPrice> selectPriceByGroupId(int groupBuyId);
}
