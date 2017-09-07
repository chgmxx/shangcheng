package com.gt.mall.service.inter.union.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import com.gt.union.api.entity.param.BindCardParam;
import com.gt.union.api.entity.result.UnionDiscountResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调用联盟卡实现类
 * User : yangqian
 * Date : 2017/9/6 0006
 * Time : 17:33
 */
@Service
public class UnionCardServiceImpl implements UnionCardService {

    private static final String url = "/api/card/8A5DA52E/";

    @Override
    public UnionDiscountResult consumeUnionDiscount( int busUserId ) {
	String result = HttpSignUtil.signHttpSelect( busUserId, url + "consumeUnionDiscount", 3 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), UnionDiscountResult.class );
	}
	return null;
    }

    @Override
    public Map phoneCode( String phone ) {
	return HttpSignUtil.signHttpInsertOrUpdate( phone, url + "phoneCode", 3 );
    }

    @Override
    public Map uionCardBind( BindCardParam bindCardParam ) {
	return HttpSignUtil.signHttpInsertOrUpdate( bindCardParam, url + "uionCardBind", 3 );
    }

}
