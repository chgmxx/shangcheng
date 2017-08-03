package com.gt.mall.web.service.order;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallDaifu;

/**
 * <p>
 * 找人代付 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallDaifuService extends BaseService< MallDaifu > {

    /**
     * 根据代付订单号查询代付信息
     */
    MallDaifu selectByDfOrderNo(String dfOrderNo);
}
