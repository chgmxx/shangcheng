package com.gt.mall.service.web.common;

import com.gt.mall.bean.MemberAddress;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员收货地址服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-11-02
 */
public interface MallMemberAddressService {

    /**
     * 查询会员的地址列表
     *
     * @param memberId 会员id
     *
     * @return 地址列表
     */
    List< PhoneOrderMemberAddressDTO > getMemberAddressList( Integer memberId );

    /**
     * 拼装会员地址返回值
     *
     * @param addressMap 查询的地址
     *
     * @return 地址
     */
    PhoneOrderMemberAddressDTO getMemberAddressResult( Map addressMap );

    /**
     * 拼装会员地址返回值
     *
     * @param memberAddress 查询的地址
     *
     * @return 地址
     */
    PhoneOrderMemberAddressDTO getMemberAddressResult( MemberAddress memberAddress );
}
