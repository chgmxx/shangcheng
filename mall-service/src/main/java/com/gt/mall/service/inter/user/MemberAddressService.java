package com.gt.mall.service.inter.user;

import com.gt.mall.bean.MemberAddress;

import java.util.List;
import java.util.Map;

/**
 * 粉丝收货地址的接口
 * User : yangqian
 * Date : 2017/9/7 0007
 * Time : 10:41
 */
public interface MemberAddressService {

    /**
     * 根据粉丝ID查询默认地址
     *
     * @param memberIds 粉丝id
     *
     * @return 默认地址
     */
    Map addressDefault( String memberIds );

    /**
     * 根据地址id查询地址
     *
     * @param addresssId 地址id
     */
    MemberAddress addreSelectId( int addresssId );

    /**
     * 查询粉丝的收货地址
     *
     * @param memberids 粉丝id
     *
     * @return 收货地址列表
     */
    List< MemberAddress > addressList( String memberids );

    /**
     * 新增或修改收回地址
     *
     * @return true 新增或修改成功
     */
    boolean addOrUpdateAddre( MemberAddress memberAddress );

    /**
     * 把地址设为默认地址
     *
     * @param addressId 地址id
     *
     * @return true 成功
     */
    boolean updateDefault( int addressId );

}
