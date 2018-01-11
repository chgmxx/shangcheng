package com.gt.mall.service.web.common;

import com.gt.api.bean.session.Member;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;

/**
 * <p>
 * 商城订单计算公共 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCalculateService {

    /**
     * 手机端 提交订单计算
     */
    PhoneAddOrderDTO calculateOrder( PhoneAddOrderDTO params, Member member );

}
