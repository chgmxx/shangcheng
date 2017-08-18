package com.gt.mall.bean.member;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 总订单
 *
 * @author pengjiangli
 */
@Getter
@Setter
public class MallAllEntity {

    private Integer memberId;  //粉丝信息  not null
    private Double totalMoney = 0.0; //订单总金额 not null
    private Map<Integer, MallShopEntity> mallShops;  //门店商品信息  key为门店id not null

    private Double discountMemberMoney = 0.0; //会员优惠券金额
    private Double discountConponMoney = 0.0;   //优惠券券优惠金额

    private Integer useFenbi = 0;  //是否使用粉币 not null
    private Double fenbiNum = 0.0;  //使用粉币数量
    private Double discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi = 0;  //是否能用粉币

    private Integer userJifen = 0;  //是否使用积分 not null
    private Integer jifenNum = 0;  //使用积分数量
    private Double discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen = 0;  //是否能用积分

    private Integer userLeague = 0;  //是否使用联盟卡 not null  0未使用 1使用
    private Double leagueDiscount = 1.0; //联盟折扣   userLeague=1 not null
    private Integer leagueJifen = 0; //联盟积分  传入最高能使用积分值   userLeague=1 not null

    private Integer leagueJifenNum = 0;  //联盟积分使用数量
    private Double leagueMoney = 0.0;  //联盟卡优惠金额

    private Double balanceMoney = 0.0;  //支付金额


}

