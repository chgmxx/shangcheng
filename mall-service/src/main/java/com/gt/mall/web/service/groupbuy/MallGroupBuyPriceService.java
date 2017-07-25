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
public interface MallGroupBuyPriceService extends BaseService< MallGroupBuyPrice > {

    /**
     * 编辑团购价格
     *
     * @Title: editFreight
     */
    void editGroupBuyPrice(Map<String, Object> map, int groupBuyId, boolean flag);

    /**
     * 通过团购id查询团购信息
     * @param groupId
     * @return
     */
    List<MallGroupBuyPrice> selectPriceByGroupId(int groupId);
}
