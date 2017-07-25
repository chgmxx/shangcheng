package com.gt.mall.web.service.freight;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightService extends BaseService<MallFreight> {

    /**
     * 通过店铺id来查询物流
     *
     * @Title: selectFreightByShopId
     */
    PageUtil selectFreightByShopId(List<Map<String, Object>> shopList, Map<String, Object> param);

    /**
     * 通过物流id查询物流信息
     */
    MallFreight selectFreightById(Integer id);

    /**
     * 编辑物流
     *
     * @Title: editFreight
     */
    boolean editFreight(Map<String, Object> params, int userId);

    /**
     * 删除物流
     *
     * @Title: deleteFreight
     */
    boolean deleteFreight(Map<String, Object> ids);

    /**
     * 通过店铺id来查询物流
     *
     * @Title: selectFreightByShopId
     */
    List<MallFreight> getByShopId(List<Map<String, Object>> shopList);

    /**
     * 获取运费
     *
     * @Title: getFreightMoney
     */
    Map<Integer, Object> getFreightMoney(Map<String, Object> map);

    /**
     * 通过省份名称获取省份id
     *
     * @param province
     * @return
     */
    int getProvinceId(String province);

    /**
     * 根据省份获取运费
     *
     * @return
     */
    double getFreightByProvinces(Map<String, Object> params, Map<String, Object> addressMap, int shopId, double totalPrice, double pro_weight);

}