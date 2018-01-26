package com.gt.mall.service.inter.wxshop;

/**
 * 认证接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 10:18
 */
public interface MemberAuthService {

    /**
     * 获取会员认证类型
     *
     * @param busUserId 商家id
     *
     * @return 2：企业 3:个人
     */
    Integer getMemberAuth( Integer busUserId );

}
