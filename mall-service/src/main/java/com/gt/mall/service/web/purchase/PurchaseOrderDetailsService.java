package com.gt.mall.service.web.purchase;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.purchase.PurchaseOrderDetails;

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
public interface PurchaseOrderDetailsService extends BaseService< PurchaseOrderDetails > {

    List< Map< String,Object > > productImg( Integer productId );
}
