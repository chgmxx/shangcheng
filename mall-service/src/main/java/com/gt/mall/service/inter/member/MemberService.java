package com.gt.mall.service.inter.member;

import com.gt.api.bean.session.Member;
import com.gt.entityBo.ErpRefundBo;
import com.gt.entityBo.NewErpPaySuccessBo;
import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.bean.member.MemberCard;
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
     * 绑定手机号(小程序)
     *
     * @param params 参数{memberId：会员id，code：短信校验码，phone：手机号，busId：商家id}
     * @param member member对象
     *
     * @return 会员对象
     */
    Member bingdingPhone( Map< String,Object > params, Member member );

    /**
     * 绑定手机号(H5)
     *
     * @param busId    商家id
     * @param phone    手机号码
     * @param memberId 会员id
     *
     * @return true 成功
     */
    boolean bingdingPhoneH5( Integer busId, String phone, Integer memberId );

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
     *
     * @param consumeParams 实体类
     *
     * @return 是否修改成功
     */
    boolean updateJifen( UserConsumeParams consumeParams );

    /**
     * 订单退款
     *
     * @param erpRefundBo 参数
     *
     * @return 否退款成功
     */
    Map< String,Object > refundMoney( ErpRefundBo erpRefundBo );

    /**
     * 查询会员卡信息
     *
     * @param mcId 会员卡id
     *
     * @return 会员卡信息
     */
    MemberCard findMemberCardByMcId( int mcId );

    /**
     * 跨门店 根据memberId和门店集合查询会员数据 返回数据包含会员信息、微信卡券、多粉卡券
     *
     * @param memberId 会员id
     * @param shopIds  门店id集合逗号隔开
     *
     * @return 会员信息、微信卡券、多粉卡券
     */
    Map findCardAndShopIdsByMembeId( int memberId, String shopIds );

    /**
     * 根据ids集合查询粉丝信息返回包含数据(id,昵称，手机号码,头像)
     *
     * @param memberIds 粉丝id  用逗号隔开
     * @param busId     商家id
     *
     * @return id, 昵称，手机号码,头像
     */
    List< Map > findMemberByIds( String memberIds, int busId );

    /**
     * 根据粉丝手机号码查询粉丝信息返回包含数据((id,昵称，手机号码,头像))
     *
     * @param phone 手机号码
     * @param busId 商家id
     *
     * @return id, 昵称，手机号码,头像
     */
    List< Map > findMemberByPhone( String phone, int busId );

    /**
     * 商场修改订单状态(流量充值时用到的)
     *
     * @param params {orderNo：订单号，payType：支付方式，payStatus：支付状态}
     *
     * @return true 成功
     */
    boolean updateUserConsume( Map< String,Object > params );

    /**
     * 查询商家的积分和粉币规则
     *
     * @param busId 商家id
     *
     * @return 积分和粉币规则
     */
    JifenAndFenbiRule jifenAndFenbiRule( int busId );


    /**
     * 根据ids集合查询粉丝信息返回包含数据(id,昵称，手机号码,头像)
     * @param params  busId :商家id    ids:粉丝id集合
     * @return  粉丝信息集合  头像 昵称 等
     */
    List<Map< String,Object >>  findMemberByIds(Map< String,Object > params );

    /**
     * 会员 积分 和 粉币核销 包括优惠券
     * @param newErpPaySuccessBo
     */
    void newPaySuccessByErpBalance(NewErpPaySuccessBo newErpPaySuccessBo);

}
