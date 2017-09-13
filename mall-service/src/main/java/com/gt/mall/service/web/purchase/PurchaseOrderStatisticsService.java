package com.gt.mall.service.web.purchase;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.purchase.PurchaseOrderStatistics;
import com.gt.mall.utils.PageUtil;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseOrderStatisticsService extends BaseService<PurchaseOrderStatistics > {

    /**
     * 分页查询数据
     * @param parms
     * @return
     */
    PageUtil findList(Map<String,Object> parms);
}
