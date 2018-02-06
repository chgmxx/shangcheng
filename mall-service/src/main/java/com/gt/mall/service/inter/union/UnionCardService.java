package com.gt.mall.service.inter.union;

import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.result.UnionDiscountResult;

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
     * @return 联盟折扣
     */
    UnionDiscountResult consumeUnionDiscount( UnionCardDiscountParam param );

    //    /**
    //     * 绑定联盟卡，获取手机验证码
    //     *
    //     * @return 验证码
    //     */
    //    Map phoneCode( UnionPhoneCodeParam phoneCodeParam );
    //
    //    /**
    //     * 绑定联盟卡
    //     *
    //     * @return true 成功
    //     */
    //    Map uionCardBind( BindCardParam bindCardParam );
}
