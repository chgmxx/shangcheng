package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseContractOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseContractOrderDAO extends BaseMapper<PurchaseContractOrder> {

    /**
     * 删除该订单下的合同
     *
     * @param orderId 订单Id
     * @return 是否成功
     */
    int deleteContractOrderData(Integer orderId);

    /**
     * 查看订单的合同
     *
     * @param orderId 订单id
     * @return list
     */
    List<Map<String, Object>> findContractOrderList(Integer orderId);
}