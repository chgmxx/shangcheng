package com.gt.mall.service.inter.wxshop.impl;

import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.wxshop.MemberAuthService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import org.springframework.stereotype.Service;

/**
 * 认证实现类
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:35
 */
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private static final String MEMBERAUTHUTL = "/8A5DA52E/memberApi/79B4DE7C/";

    @Override
    public Integer getMemberAuth( Integer busUserId ) {
        if ( CommonUtil.isEmpty( busUserId ) || busUserId == 0 ) {
            return null;
        }
        RequestUtils< Integer > requestUtils = new RequestUtils<>();
        requestUtils.setReqdata( busUserId );
        String result = HttpSignUtil.signHttpSelect( requestUtils, MEMBERAUTHUTL + "getMemberAuth", 5 );
        if ( CommonUtil.isNotEmpty( result ) ) {
            return CommonUtil.toInteger( result );
        }
        return null;
    }
}
