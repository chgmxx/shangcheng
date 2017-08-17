package com.gt.mall.service.inter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.params.UserConsumeParams;
import com.gt.mall.service.inter.MemberService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MemberInterUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员相关方法调用
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:39
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final String MEMBER_URL = "/memberAPI/member/";//会员链接

    /**
     * 判断member是否是空 ，为空则赋值
     *
     * @param memberObj 返回的member json
     * @param member    对象
     *
     * @return 会员对象
     */
    private Member isEmptyMember( JSONObject memberObj, Member member ) {
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
	    if ( CommonUtil.isNotEmpty( memberObj.get( "openid" ) ) ) {
		member.setOpenid( memberObj.getString( "openid" ) );
	    }
	    if ( CommonUtil.isNotEmpty( memberObj.get( "headimgurl" ) ) ) {
		member.setHeadimgurl( memberObj.getString( "headimgurl" ) );
	    }
	    if ( CommonUtil.isNotEmpty( memberObj.get( "mcId" ) ) ) {
		member.setMcId( CommonUtil.toInteger( memberObj.get( "mcId" ) ) );
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
    public Member findMemberById( int memberId, Member member ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findByMemberId" );
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
    public Member bingdingPhone( Map< String,Object > params, Member member ) {
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "bingdingPhone" );
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
    public double getMemberDiscount( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findCardTypeReturnDiscount" );
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
    public Map findMemberCardByMemberId( int memberId, int shopId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	params.put( "shopId", shopId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findCardByMembeId" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( data ), Map.class );
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
    public Map< String,Object > isAdequateMoney( int memberId, double money ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	params.put( "money", money );
	Map< String,Object > resultMap = MemberInterUtil.SignHttpInsertOrUpdate( params, MEMBER_URL + "isAdequateMoney" );
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
    public Map< String,Object > refundMoney( Map< String,Object > params ) {
	Map< String,Object > resultMap = MemberInterUtil.SignHttpInsertOrUpdate( params, MEMBER_URL + "refundMoney" );
	if ( CommonUtil.isNotEmpty( resultMap ) ) {
	    return resultMap;
	}
	return null;
    }

    /**
     * 判断粉丝是否是会员
     *
     * @param memberId 会员id
     *
     * @return 是否是会员
     */
    public boolean isMember( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String result = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "isMember" );
	return CommonUtil.isNotEmpty( result );
    }

    /**
     * 新增会员积分 和记录
     *
     * @param params {memberId：会员id，jifen：积分}
     *
     * @return 是否修改成功
     */
    @Override
    public Map< String,Object > updateJifen( Map< String,Object > params ) {
	return MemberInterUtil.SignHttpInsertOrUpdate( params, MEMBER_URL + "updateJifen" );
    }

    /**
     * 根据会员id查询会员集合
     *
     * @param memberId 会员id
     *
     * @return 会员集合
     */
    @Override
    public List< Integer > findMemberListByIds( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findMemberIdsByid" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONArray.parseArray( data, Integer.class );
	}
	return null;
    }

    /**
     * 获取会员类型
     *
     * @param memberId 会员id
     *
     * @return 会员类型
     */
    @Override
    public Integer isCardType( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "isCardType" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return CommonUtil.toInteger( data );
	}
	return 0;
    }

    /**
     * 查询会员卡片名称
     *
     * @param memberId 会员id
     *
     * @return 会员名称
     */
    @Override
    public Map findGradeType( int memberId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "memberId", memberId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findGradeType" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( data ), Map.class );
	}
	return null;
    }

    /**
     * 查询会员积分记录
     *
     * @param params {mcId：会员卡id，page：页数，pageSize：大小}
     *
     * @return 会员积分记录
     */
    @Override
    public List< Map > findCardrecordList( Map< String,Object > params ) {
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findCardrecord" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONArray.parseArray( data, Map.class );
	}
	return null;
    }

    @Override
    public List< Map > findBuyGradeType( int userId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "busId", userId );
	String data = MemberInterUtil.SignHttpSelect( params, MEMBER_URL + "findBuyGradeType" );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    return JSONArray.parseArray( data, Map.class );
	}
	return null;
    }

    @Override
    public boolean updateUserConsume( UserConsumeParams consumeParams ) {
	Map< String,Object > resultMap = MemberInterUtil.SignHttpInsertOrUpdate( consumeParams, MEMBER_URL + "updateJifen" );
	return CommonUtil.toString( resultMap.get( "code" ) ).equals( "1" );
    }

}
