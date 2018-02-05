package com.gt.mall.service.web.purchase;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.purchase.PurchaseCarousel;
import com.gt.mall.entity.purchase.PurchaseOrder;
import com.gt.mall.entity.purchase.PurchaseOrderDetails;
import com.gt.mall.entity.purchase.PurchaseTerm;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseOrderService extends BaseService< PurchaseOrder > {

    /**
     * 分页查询数据
     *
     * @param parms
     *
     * @return
     */
    PageUtil findList( Map< String,Object > parms );

    /**
     * 修改订单信息
     *
     * @param order
     * @param orderDetailsList
     * @param termList
     *
     * @return
     */
    Map< String,Object > saveOrder( PurchaseOrder order, List< PurchaseOrderDetails > orderDetailsList, List< PurchaseTerm > termList,
        List< PurchaseCarousel > carouselList );

    /**
     * 查询商品数据
     *
     * @param parms
     *
     * @return
     */
    PageUtil productList( Map< String,Object > parms );
}
