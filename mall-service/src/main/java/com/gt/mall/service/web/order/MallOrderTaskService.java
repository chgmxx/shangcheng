package com.gt.mall.service.web.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallOrderTask;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单任务表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2018-02-02
 */
public interface MallOrderTaskService extends BaseService< MallOrderTask > {

    /**
     * 保存订单任务
     *
     * @param type          类型 1关闭订单 2自动确认收货 3赠送物品 4联盟积分 5收入记录 6取消维权 7自动退款给买家 8自动确认收货并退款至买家
     * @param orderId       订单ID
     * @param orderNo       订单号
     * @param orderRerurnId 订单维权ID
     * @param day           等待天数
     *
     * @return
     */
    boolean saveOrUpdate( Integer type, Integer orderId, String orderNo, Integer orderRerurnId, Integer day );

    /**
     * 查询数据
     *
     * @param params
     *
     * @return
     */
    List< MallOrderTask > findByType( Map< String,Object > params );

}
