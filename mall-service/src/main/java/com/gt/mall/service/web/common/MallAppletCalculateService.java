package com.gt.mall.service.web.common;

import com.gt.api.bean.session.Member;
import com.gt.mall.param.applet.AppletSubmitOrderDTO;

/**
 * <p>
 * 商城小程序订单计算公共 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAppletCalculateService {

    /**
     * 手机端 提交订单计算
     */
    AppletSubmitOrderDTO calculateOrder( AppletSubmitOrderDTO params , Member member );

}
