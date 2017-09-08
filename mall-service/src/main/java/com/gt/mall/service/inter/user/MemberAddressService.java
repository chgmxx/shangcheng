package com.gt.mall.service.inter.user;

import java.util.Map;

/**
 * 粉丝收货地址的接口
 * User : yangqian
 * Date : 2017/9/7 0007
 * Time : 10:41
 */
public interface MemberAddressService {
    public static final String url = "/8A5DA52E/fanAddress/";

    /**
     * 根据粉丝ID查询默认地址
     * @param memberIds 粉丝id
     * @return 默认地址
     */
    Map addressDefault(String memberIds);

    /**
     * 根据地址id查询地址
     * @param addresssId 地址id
     */
    Map addreSelectId(int addresssId);

}
