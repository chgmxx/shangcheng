package com.gt.mall.service.inter.union;

import com.gt.union.api.entity.param.BindCardParam;
import com.gt.union.api.entity.result.UnionDiscountResult;

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
    UnionDiscountResult consumeUnionDiscount( int busUserId );

    /**
     * 绑定联盟卡，获取手机验证码
     *
     * @param phone 手机卡
     *
     * @return 验证码
     */
    Map phoneCode( String phone );

    /**
     * 绑定联盟卡
     *
     * @return true 成功
     */
    Map uionCardBind( BindCardParam bindCardParam );
}
