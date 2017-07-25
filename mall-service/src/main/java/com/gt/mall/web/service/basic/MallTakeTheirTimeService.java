package com.gt.mall.web.service.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;

/**
 * <p>
 * 自提点接待时间表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallTakeTheirTimeService extends BaseService< MallTakeTheirTime > {

    /**
     * 查询默认的上门自提地址和提货时间
     */
    MallTakeTheir selectDefaultTakeByUserId(int publicId, int loginCity, int takeId);

}
