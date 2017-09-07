package com.gt.mall.bean.member;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
@Getter
@Setter
public class MallEntity {
    private Integer mallId;  //商品id or 生成序列号  not null
    private Integer number = 1;  // 商品数量  not null
    private Double totalMoneyOne;  //商品应付单价格  not null
    private Double totalMoneyAll; //商品订单总价格  用来计算  not null

    private Integer userCard            = 0;  //是否能用会员卡打折  0不参与 1参与  not null
    private Double  discountMemberMoney = 0.0; //会员优惠券金额
    private Integer canUserCard         = 0;  //是否用了会员

    private Integer useCoupon           = 0;  //是否允许使用优惠券  not null
    private Double  discountConponMoney = 0.0;
    private Integer canUseConpon        = 0;  //是否能用优惠券

    private Integer useFenbi           = 0;  //是否允许使用粉币  not null
    private Double  fenbiNum           = 0.0;  //使用粉币数量
    private Double  discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi        = 0;  //是否能用粉币

    private Integer userJifen          = 0;  //是否允许使用积分  not null
    private Double  jifenNum           = 0.0;  //使用积分数量
    private Double  discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen        = 0;  //是否能用积分

    private Integer useLeague         = 1;     //商品是否允许使用联盟卡
    private Double  leagueJifen       = 0.0;   //  商品联盟积分使用数量
    private Double  leagueMoney       = 0.0;  //联盟卡优惠券金额
    private Integer canUseLeagueJifen = 1;  //是否能用联盟积分

    private Double unitPrice = 0.0;  // 实付单个商品金额

    private Double balanceMoney = 0.0;  //支付总金额

}
