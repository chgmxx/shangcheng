package com.gt.mall.web.service.pifa;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.pifa.MallPifaPrice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批发价格表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaPriceService extends BaseService< MallPifaPrice > {

    /**
     * 编辑批发价格
     *
     * @Title: editPifaPrice
     */
    void editPifaPrice(Map<String, Object> map,int pfId,boolean flag);

    /**
     * 通过批发id查询批发信息
     * @param groupId
     * @return
     */
    List<MallPifaPrice> selectPriceByGroupId(int groupId);
}
