package com.gt.mall.service.inter.union.impl;

import com.gt.mall.service.inter.union.UnionConsumeService;
import com.gt.mall.util.HttpSignUtil;
import com.gt.union.api.entity.param.UnionConsumeParam;
import com.gt.union.api.entity.param.UnionRefundParam;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用联盟卡核销实现类
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:33
 */
@Service
public class UnionConsumeServiceImpl implements UnionConsumeService {

    public static final String url = "/api/consume/8A5DA52E/";

    @Override
    public boolean unionConsume( UnionConsumeParam unionConsumeParam ) {

	Map< String,Object > result = HttpSignUtil.signHttpInsertOrUpdate( unionConsumeParam, url + "unionConsume", 3 );
	return result.get( "code" ).toString().equals( "1" );
    }

    @Override
    public boolean unionRefund( UnionRefundParam unionRefundParam ) {

	Map< String,Object > result = HttpSignUtil.signHttpInsertOrUpdate( unionRefundParam, url + "unionRefund", 3 );
	return result.get( "code" ).toString().equals( "1" );
    }
}
