package com.gt.mall.service.web.common.impl;

import com.gt.mall.bean.MemberAddress;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.web.common.MallMemberAddressService;
import com.gt.mall.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员收货地址服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-11-02
 */
@Service
public class MallMemberAddressServiceImpl implements MallMemberAddressService {

    private Logger logger = Logger.getLogger( MallMemberAddressServiceImpl.class );

    @Autowired
    private MemberAddressService memberAddressService;//会员收货地址接口
    @Autowired
    private MemberService        memberService;

    public List< PhoneOrderMemberAddressDTO > getMemberAddressList( Integer memberId ) {
	List< PhoneOrderMemberAddressDTO > addressDTOList = new ArrayList<>();

	//获取会员集合
	List< Integer > memberList = memberService.findMemberListByIds( memberId );
	List< MemberAddress > addressList = memberAddressService.addressList( CommonUtil.getMememberIds( memberList, memberId ) );
	if ( addressList != null && addressList.size() > 0 ) {
	    for ( MemberAddress address : addressList ) {
		PhoneOrderMemberAddressDTO memberAddress = getMemberAddressResult( address );
		addressDTOList.add( memberAddress );
	    }
	}
	return addressDTOList;
    }

    /**
     * 拼装会员地址返回值
     *
     * @param addressMap 查询的地址
     *
     * @return 地址
     */
    @Override
    public PhoneOrderMemberAddressDTO getMemberAddressResult( Map addressMap ) {
	if ( CommonUtil.isEmpty( addressMap ) ) {
	    return null;
	}
	PhoneOrderMemberAddressDTO memberAddress = new PhoneOrderMemberAddressDTO();
	memberAddress.setId( CommonUtil.toInteger( addressMap.get( "id" ) ) );
	memberAddress.setMemberName( addressMap.get( "memName" ).toString() );
	memberAddress.setMemberPhone( addressMap.get( "memPhone" ).toString() );
	String address = addressMap.get( "provincename" ).toString() + addressMap.get( "cityname" ).toString() + addressMap.get( "areaname" ).toString() + addressMap
			.get( "memAddress" ).toString();
	if ( CommonUtil.isNotEmpty( addressMap.get( "memZipCode" ) ) ) {
	    address += addressMap.get( "memZipCode" ).toString();
	}
	memberAddress.setMemberAddress( address );
	memberAddress.setAddressProvince( CommonUtil.toInteger( addressMap.get( "memProvince" ) ) );
	return memberAddress;
    }

    @Override
    public PhoneOrderMemberAddressDTO getMemberAddressResult( MemberAddress memberAddress ) {
	PhoneOrderMemberAddressDTO memberAddressResult = new PhoneOrderMemberAddressDTO();
	memberAddressResult.setId( memberAddress.getId() );
	memberAddressResult.setMemberPhone( memberAddress.getMemPhone() );
	memberAddressResult.setMemberName( memberAddress.getMemName() );
	String address = memberAddress.getProvincename() + memberAddress.getCityname() + memberAddress.getAreaname() + memberAddress.getMemAddress();
	if ( CommonUtil.isNotEmpty( memberAddress.getMemHouseMember() ) ) {
	    address += memberAddress.getMemHouseMember();
	}
	memberAddressResult.setMemberAddress( address );
	memberAddressResult.setMemberDefault( memberAddress.getMemDefault() );
	memberAddressResult.setAddressProvince( memberAddress.getMemProvince() );
	return memberAddressResult;
    }

}
