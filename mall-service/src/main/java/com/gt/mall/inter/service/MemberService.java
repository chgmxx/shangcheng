package com.gt.mall.inter.service;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.params.MallAllEntity;
import com.gt.mall.bean.params.PaySuccessBo;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MemberInterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员相关方法调用
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:39
 */
public class MemberService {

    private static Logger logger = LoggerFactory.getLogger( MemberService.class );

    /**
     * 判断member是否是空 ，为空则赋值
     *
     * @param memberObj 返回的member json
     * @param member    对象
     *
     * @return 会员对象
     */
    private static Member isEmptyMember( JSONObject memberObj, Member member ) {
	if ( CommonUtil.isEmpty( member ) ) {
	    member = new Member();
	    member.setId( memberObj.getInteger( "id" ) );
	    member.setBusid( memberObj.getInteger( "busId" ) );
	    if ( CommonUtil.isNotEmpty( memberObj.get( "publicId" ) ) ) {
		member.setPublicId( memberObj.getInteger( "publicId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( memberObj.get( "nickname" ) ) ) {
		member.setNickname( memberObj.getString( "nickname" ) );
	    }
	    if(CommonUtil.isNotEmpty( memberObj.get( "openid" ) )){
	        member.setOpenid( memberObj.getString( "openid" ) );
	    }
	    if(CommonUtil.isNotEmpty( memberObj.get( "headimgurl" ) )){
	        member.setHeadimgurl( memberObj.getString( "headimgurl" ) );
	    }
	}
	return member;
    }

    /**
     * 根据会员id查询会员信息
     *
     * @param memberId 会员id
     * @param member   会员对象
     *
     * @return 会员对象
     */
    public static Member findMemberById( int memberId, Member member ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, "/memberAPI/member/findByMemberId" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject memberObj = JSONObject.parseObject( data );
	    member = isEmptyMember( memberObj, member );
	    member.setFansCurrency( memberObj.getDouble( "fansCurrency" ) );
	    member.setIntegral( memberObj.getInteger( "integral" ) );
	}
	return member;
    }

    /**
     * 绑定手机号
     *
     * @param params 参数{memberId：会员id，code：短信校验码，phone：手机号，busId：商家id}
     * @param member member对象
     *
     * @return 会员对象
     */
    public static Member bingdingPhone( Map< String,Object > params, Member member ) {
	String data = MemberInterUtil.SignHttpSelect( params, "/memberAPI/member/bingdingPhone" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject memberObj = JSONObject.parseObject( data );
	    member.setPhone( memberObj.getString( "phone" ) );
	}
	return member;
    }

    /**
     * 根据粉丝id获取会员折扣
     *
     * @param memberId 会员id
     *
     * @return 折扣数
     */
    public static double getMemberDiscount( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, "/memberAPI/member/findCardTypeReturnDiscount" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return CommonUtil.toDouble( data );
	}
	return 0;
    }

    /**
     * 根据会员id和门店id 查询 会员信息、优惠券信息和卡券信息
     *
     * @param memberId 对象id
     * @param shopId   门店id
     *
     * @return 会员信息、优惠券信息和卡券信息
     */
    public static Member findMemberCardByMemberId( int memberId, int shopId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	params.put( "shopId", shopId );
	String data = MemberInterUtil.SignHttpSelect( params, "/memberAPI/member/findCardByMembeId" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    JSONObject memberObj = JSONObject.parseObject( data );
	}
	return null;
    }

    /**
     * 判断储值卡金额是否充足
     *
     * @param memberId 会员id
     * @param money    消费金额
     *
     * @return 消费是否充足
     */
    public static Map< String,Object > isAdequateMoney( int memberId, double money ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	params.put( "money", money );
	Map< String,Object > resultMap = MemberInterUtil.SignHttpInsertOrUpdate( params, "/memberAPI/member/isAdequateMoney" );
	if ( CommonUtil.isNotEmpty( resultMap ) ) {
	    return resultMap;
	}
	return null;
    }

    /**
     * 储值卡退款
     *
     * @param params {busId:商家id，orderNo：单号，ucType：消费类型，money：退款金额}
     *
     * @return 消费是否充足
     */
    public static Map< String,Object > refundMoney( Map< String,Object > params ) {
	Map< String,Object > resultMap = MemberInterUtil.SignHttpInsertOrUpdate( params, "/memberAPI/member/refundMoney" );
	if ( CommonUtil.isNotEmpty( resultMap ) ) {
	    return resultMap;
	}
	return null;
    }

    /**
     * 会员计算 （还未调试）
     *
     * @param mallAllEntity 对象
     *
     * @return 对象
     */
    public static MallAllEntity memberCountMoneyByShop( MallAllEntity mallAllEntity ) {
	String data = MemberInterUtil.SignHttpSelect( mallAllEntity, "/memberAPI/memberCountApi/memberCountMoneyByShop" );
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
    public static Map< String,Object > paySuccess( PaySuccessBo paySuccessBo ) {
	return MemberInterUtil.SignHttpInsertOrUpdate( paySuccessBo, "/memberAPI/memberCountApi/paySuccess" );
    }

}
