package com.gt.mall.param.applet;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 提交订单参数
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Getter
@Setter
public class AppletSubmitOrderDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;
    /**
     * 会员ID
     */
    private Integer memberId;
    /**
     * 购物车商品ID集合
     */
    private String  cartIds;
    /**
     * 订单实付金额（包含运费）
     */
    private Double  totalAllMoney;

    /**
     * 粉币可抵扣的金额
     */
    private Double  fenbi_money;
    /**
     * 粉币数量
     */
    private Integer fenbi;

    /**
     * 积分可抵扣的金额
     */
    private Double  integral_money;
    /**
     * 积分数量
     */
    private Integer integral;
    /**
     * 是否已经使用积分 1已经使用积分   0没使用
     */
    private Integer isUseJifen;
    /**
     * 是否已经使用粉币  1已经使用粉币     0没使用
     */
    private Integer isUseFenbi;
    /**
     * 是否已经使用优惠券  1已经使用     0没使用
     */
    private Integer isUseYhq;
    /**
     * 是否已经使用商家联盟   1已经使用商家联盟     0没使用
     */
    private Integer isUseUnion;
    /**
     * 能使用商家联盟的商品数量
     */
    private Integer unionProNum;
    /**
     * 能使用商家联盟的商品总价
     */
    private Double  unionProMoney;
    /**
     * 能使用粉币的商品数量
     */
    private Integer fenbiProNum;
    /**
     * 能使用粉币的商品总价
     */
    private Double fenbiProMoney;
    /**
     * 能使用积分的商品数量
     */
    private Integer jifenProNum;
    /**
     * 能使用积分的商品总价
     */
    private Double jifenProMoney;
    /**
     * 版本号
     */
    private String  version;

    /**
     * 订单信息 json格式
     */
    private String order;

}
