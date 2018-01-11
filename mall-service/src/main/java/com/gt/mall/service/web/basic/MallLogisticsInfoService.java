package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallLogisticsInfo;
import com.gt.mall.entity.basic.MallPaySet;

import java.util.List;

/**
 * <p>
 * 物流信息表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
public interface MallLogisticsInfoService extends BaseService< MallLogisticsInfo > {

    /**
     * @param orderId 订单ID
     * @param sn      物流编号
     *
     * @return
     */
    List< MallLogisticsInfo > selectByOrderId( Integer orderId, String sn );

}
