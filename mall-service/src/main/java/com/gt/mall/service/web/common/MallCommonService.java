package com.gt.mall.service.web.common;

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

}
