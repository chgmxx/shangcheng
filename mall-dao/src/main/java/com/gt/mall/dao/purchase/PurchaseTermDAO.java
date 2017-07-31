package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseTerm;

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
public interface PurchaseTermDAO extends BaseMapper<PurchaseTerm> {

    /**
     * 查询该订单的分期记录
     *
     * @param orderId 订单号
     * @return list
     */
    List<Map<String, Object>> findTermList(int orderId);

    /**
     * 删除该订单下的所有分期数据
     *
     * @param parms termIds：id集合，orderId：订单id
     * @return 是否成功
     */
    int deleteTermByOrderId(Map<String, Object> parms);

}