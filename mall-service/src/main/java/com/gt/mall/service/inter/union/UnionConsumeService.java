package com.gt.mall.service.inter.union;

import java.util.Map;

/**
 * 调用联盟卡核销接口
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:44
 */
public interface UnionConsumeService {

    /**
     * 联盟卡核销
     *
     * @return true 成功
     */
    boolean unionConsume( Map< String,Object > params );

    /**
     * 联盟卡核销后退款
     *
     * @return true 成功
     */
    boolean unionRefund( Map< String,Object > params );
}
