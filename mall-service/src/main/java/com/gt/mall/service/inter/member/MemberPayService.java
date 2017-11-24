package com.gt.mall.service.inter.member;

import com.gt.entityBo.MallAllEntity;
import com.gt.entityBo.NewErpPaySuccessBo;
import com.gt.entityBo.PaySuccessBo;

import java.util.List;
import java.util.Map;

/**
 * 会员相关方法调用
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:39
 */
public interface MemberPayService {

    /**
     * 会员计算 （还未调试）
     *
     * @param mallAllEntity 对象
     *
     * @return 对象
     */
    MallAllEntity memberCountMoneyByShop( MallAllEntity mallAllEntity );

    /**
     * 支付成功回调   传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款
     *
     * @param paySuccessBo 对象
     *
     * @return 对象
     */
    Map< String,Object > paySuccess( PaySuccessBo paySuccessBo );
    /**
     * 支付成功回调   传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款
     *
     * @param paySuccessBo 对象
     *
     * @return 对象
     */
    Map<String,Object> paySuccessNew(List<NewErpPaySuccessBo> paySuccessBo);

}
