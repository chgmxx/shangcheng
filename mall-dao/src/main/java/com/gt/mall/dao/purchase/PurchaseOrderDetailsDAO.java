package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseOrderDetails;

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
public interface PurchaseOrderDetailsDAO extends BaseMapper< PurchaseOrderDetails > {

    /**
     * 使用orderid查询订单详情
     *
     * @param orderId 订单id
     *
     * @return list
     */
    List< Map< String,Object > > findOrderDetails( Integer orderId );

    /**
     * 删除该订单下的未用商品信息
     *
     * @param parms orderId:订单id,detailIds:详情id集合
     *
     * @return 是否成功
     */
    int deleteDetailsByOrderId( Map< String,Object > parms );
}
