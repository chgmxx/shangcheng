package com.gt.mall.service.inter.union;

import java.util.Map;

/**
 * 调用联盟卡接口
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:33
 */
public interface UnionCardService {

    /**
     * 获取商家的联盟折扣
     *
     * @param busUserId 商家id
     *
     * @return 联盟折扣
     */
    Map consumeUnionDiscount( int busUserId );

    /**
     * 绑定联盟卡，获取手机验证码
     *
     * @param phone 手机卡
     *
     * @return 验证码
     */
    String phoneCode( String phone );

    /**
     * 绑定联盟卡
     * @param phone 手机号码
     * @param code 手机code
     * @return true 成功
     */
    boolean uionCardBind(String phone,String code);
}
