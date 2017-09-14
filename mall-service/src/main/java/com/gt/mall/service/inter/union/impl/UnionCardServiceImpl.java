package com.gt.mall.service.inter.union.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.union.api.entity.param.BindCardParam;
import com.gt.union.api.entity.param.RequestApiParam;
import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.param.UnionPhoneCodeParam;
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
    public UnionDiscountResult consumeUnionDiscount( UnionCardDiscountParam param ) {
	RequestApiParam< UnionCardDiscountParam > requestApiParam = new RequestApiParam<>();
	requestApiParam.setReqdata( param );
	String result = HttpSignUtil.signHttpSelect( requestApiParam, url + "consumeUnionDiscount", 3 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), UnionDiscountResult.class );
	}
	return null;
    }

    @Override
    public Map phoneCode( UnionPhoneCodeParam phoneCodeParam ) {
	RequestApiParam< UnionPhoneCodeParam > requestApiParam = new RequestApiParam<>();
	requestApiParam.setReqdata( phoneCodeParam );
	return HttpSignUtil.signHttpInsertOrUpdate( requestApiParam, url + "phoneCode", 3 );
    }

    @Override
    public Map uionCardBind( BindCardParam bindCardParam ) {
	RequestApiParam< BindCardParam > requestApiParam = new RequestApiParam<>();
	requestApiParam.setReqdata( bindCardParam );
	return HttpSignUtil.signHttpInsertOrUpdate( requestApiParam, url + "uionCardBind", 3 );
    }

}
