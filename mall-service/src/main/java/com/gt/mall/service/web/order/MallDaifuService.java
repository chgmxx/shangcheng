package com.gt.mall.service.web.order;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.result.phone.order.daifu.PhoneGetDaiFuResult;

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
    MallDaifu selectByDfOrderNo( String dfOrderNo );

    /**
     * 获取代付信息
     */
    PhoneGetDaiFuResult getDaifuResult( Integer orderId, Member member, Integer browerType );
}
