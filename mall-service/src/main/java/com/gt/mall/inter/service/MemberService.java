package com.gt.mall.inter.service;

import com.gt.mall.bean.Member;
import com.gt.mall.bean.params.MallAllEntity;
import com.gt.mall.bean.params.PaySuccessBo;

import java.util.Map;

/**
 * 会员相关方法调用
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:39
 */
public interface MemberService {

    /**
     * 根据会员id查询会员信息
     *
     * @param memberId 会员id
     * @param member   会员对象
     *
     * @return 会员对象
     */
    public Member findMemberById( int memberId, Member member ) ;

    /**
     * 绑定手机号
     *
     * @param params 参数{memberId：会员id，code：短信校验码，phone：手机号，busId：商家id}
     * @param member member对象
     *
     * @return 会员对象
     */
    public Member bingdingPhone( Map< String,Object > params, Member member ) ;

    /**
     * 根据粉丝id获取会员折扣
     *
     * @param memberId 会员id
     *
     * @return 折扣数
     */
    public double getMemberDiscount( int memberId ) ;

    /**
     * 根据会员id和门店id 查询 会员信息、优惠券信息和卡券信息
     *
     * @param memberId 对象id
     * @param shopId   门店id
     *
     * @return 会员信息、优惠券信息和卡券信息
     */
    public Member findMemberCardByMemberId( int memberId, int shopId ) ;

    /**
     * 判断储值卡金额是否充足
     *
     * @param memberId 会员id
     * @param money    消费金额
     *
     * @return 消费是否充足
     */
    public Map< String,Object > isAdequateMoney( int memberId, double money ) ;

    /**
     * 储值卡退款
     *
     * @param params {busId:商家id，orderNo：单号，ucType：消费类型，money：退款金额}
     *
     * @return 消费是否充足
     */
    public Map< String,Object > refundMoney( Map< String,Object > params );

    /**
     * 判断粉丝是否是会员
     *
     * @param memberId 会员id
     *
     * @return 是否是会员
     */
    public boolean isMember( int memberId ) ;

    /**
     * 会员计算 （还未调试）
     *
     * @param mallAllEntity 对象
     *
     * @return 对象
     */
    public MallAllEntity memberCountMoneyByShop( MallAllEntity mallAllEntity ) ;

    /**
     * 支付成功回调   传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款
     *
     * @param paySuccessBo 对象
     *
     * @return 对象
     */
    public Map< String,Object > paySuccess( PaySuccessBo paySuccessBo );

}
