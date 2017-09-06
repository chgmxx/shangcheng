package com.gt.mall.service.inter.union.impl;

import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.union.UnionConsumeService;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用联盟卡核销实现类
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:44
 */
@Service
public class UnionConsumeServiceImpl implements UnionConsumeService {

    public static final String url = "/api/consume/8A5DA52E/";

    @Override
    public boolean unionConsume( Map< String,Object > params ) {
	RequestUtils requestUtils = new RequestUtils();
	requestUtils.setReqdata( params );

	Map< String,Object > result = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, url + "unionConsume" );
	return result.get( "code" ).toString().equals( "1" );
    }

    @Override
    public boolean unionRefund( Map< String,Object > params ) {
	RequestUtils requestUtils = new RequestUtils();
	requestUtils.setReqdata( params );

	Map< String,Object > result = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, url + "unionRefund" );
	return result.get( "code" ).toString().equals( "1" );
    }
}
