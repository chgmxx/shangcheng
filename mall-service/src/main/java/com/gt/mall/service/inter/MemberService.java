package com.gt.mall.service.inter;

import com.gt.mall.bean.Member;
import com.gt.mall.bean.member.ReturnParams;
import com.gt.mall.bean.member.UserConsumeParams;

import java.util.List;
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
    Member findMemberById( int memberId, Member member );

    /**
     * 绑定手机号
     *
     * @param params 参数{memberId：会员id，code：短信校验码，phone：手机号，busId：商家id}
     * @param member member对象
     *
     * @return 会员对象
     */
    Member bingdingPhone( Map< String,Object > params, Member member );

    /**
     * 根据粉丝id获取会员折扣
     *
     * @param memberId 会员id
     *
     * @return 折扣数
     */
    double getMemberDiscount( int memberId );

    /**
     * 根据会员id和门店id 查询 会员信息、优惠券信息和卡券信息
     *
     * @param memberId 对象id
     * @param shopId   门店id
     *
     * @return 会员信息、优惠券信息和卡券信息
     */
    Map findMemberCardByMemberId( int memberId, int shopId );

    /**
     * 判断储值卡金额是否充足
     *
     * @param memberId 会员id
     * @param money    消费金额
     *
     * @return 消费是否充足
     */
    Map< String,Object > isAdequateMoney( int memberId, double money );

    /**
     * 储值卡退款
     *
     * @param params {busId:商家id，orderNo：单号，ucType：消费类型，money：退款金额}
     *
     * @return 消费是否充足
     */
    Map< String,Object > refundMoney( Map< String,Object > params );

    /**
     * 判断粉丝是否是会员
     *
     * @param memberId 会员id
     *
     * @return 是否是会员
     */
    boolean isMember( int memberId );

    /**
     * 新增会员积分 和记录
     *
     * @param params {memberId：会员id，jifen：积分}
     *
     * @return 是否修改成功
     */
    Map< String,Object > updateJifen( Map< String,Object > params );

    /**
     * 根据会员id查询会员集合
     *
     * @param memberId 会员id
     *
     * @return 会员集合
     */
    List< Integer > findMemberListByIds( int memberId );

    /**
     * 获取会员类型
     *
     * @param memberId 会员id
     *
     * @return 会员类型
     */
    Integer isCardType( int memberId );

    /**
     * 查询会员卡片名称
     *
     * @param memberId 会员id
     *
     * @return 会员名称
     */
    Map findGradeType( int memberId );

    /**
     * 查询会员积分记录
     *
     * @param params {mcId：会员卡id，page：页数，pageSize：大小}
     *
     * @return 会员积分记录
     */
    List< Map > findCardrecordList( Map< String,Object > params );

    /**
     * 查询购买的会员卡
     *
     * @param userId 商家id
     *
     * @return 会员卡集合
     */
    List< Map > findBuyGradeType( int userId );

    /**
     * 修改交易记录状态
     * @param consumeParams 实体类
     * @return 是否修改成功
     */
    boolean updateUserConsume(UserConsumeParams consumeParams);

    /**
     * 退款包括了储值卡退款(包括积分和粉币)
     * @param returnParams
     * @return 是否退款成功
     */
    Map<String,Object> refundMoneyAndJifenAndFenbi(ReturnParams returnParams);

}
