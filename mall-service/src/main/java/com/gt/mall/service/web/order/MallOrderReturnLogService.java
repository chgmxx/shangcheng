package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrderReturnLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单维权日志表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
public interface MallOrderReturnLogService extends BaseService< MallOrderReturnLog > {

    /**
     * 查询维权日志信息
     */
    List< Map< String,Object > > selectReturnLogList( Integer returnId );

}
