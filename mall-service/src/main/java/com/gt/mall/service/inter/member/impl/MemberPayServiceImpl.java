package com.gt.mall.service.inter.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.member.MallAllEntity;
import com.gt.mall.bean.member.PaySuccessBo;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 会员支付相关接口
 * User : yangqian
 * Date : 2017/8/16 0016
 * Time : 17:13
 */
@Service
public class MemberPayServiceImpl implements MemberPayService {

    private static final String MEMBER_COUNT_URL = "/memberAPI/memberCountApi/";//会员计算链接

    /**
     * 会员计算 （还未调试）
     *
     * @param mallAllEntity 对象
     *
     * @return 对象
     */
    public MallAllEntity memberCountMoneyByShop( MallAllEntity mallAllEntity ) {
	String data = HttpSignUtil.signHttpSelect( mallAllEntity, MEMBER_COUNT_URL + "memberCountMoneyByShop" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( data ), MallAllEntity.class );
	}
	return null;
    }

    /**
     * 支付成功回调   传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款
     *
     * @param paySuccessBo 对象
     *
     * @return 对象
     */
    public Map< String,Object > paySuccess( PaySuccessBo paySuccessBo ) {
	return HttpSignUtil.signHttpInsertOrUpdate( paySuccessBo, MEMBER_COUNT_URL + "paySuccess" );
    }
}
