package com.gt.mall.service.web.common;

import com.gt.mall.result.phone.order.PhoneToOrderBusResult;

/**
 * <p>
 * 公共服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommonService {

    /**
     * 获取手机验证码
     *
     * @param mobile         手机号码
     * @param busId          商家ID
     * @param content        短信内容
     * @param authorizerInfo 公司名称
     *
     * @return
     */
    boolean getValCode( String mobile, Integer busId, String content, String authorizerInfo );

    /**
     * 判断商家是否有技术支持
     *
     * @param busId 商家id
     *
     * @return true 有
     */
    boolean busIsAdvert( int busId );

    /**
     * 获取商家名称或商家头像
     */
    PhoneToOrderBusResult getBusUserNameOrImage( int busId );
}
