package com.gt.mall.param.applet;

import com.gt.mall.entity.order.MallOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
     * 合计（订单支付总价）
     */
    private double totalPayMoney;

    /**
     * 粉币可抵扣的金额
     */
    private Double fenbi_money;
    /**
     * 粉币数量
     */
    private Double fenbi;

    /**
     * 积分可抵扣的金额
     */
    private Double  integral_money;
    /**
     * 积分数量
     */
    private Double  integral;
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
     * 是否已经使用了折扣 1已经使用
     */
    private Integer isUseDiscount;

    /**
     * 版本号
     */
    private String version;

    /**
     * 订单集合 json格式
     */
    private String order;

    /**
     * 订单店铺集合
     */
    @ApiModelProperty( name = "orderList", value = "订单店铺集合", hidden = true )
    private List< MallOrder > orderList;

    @ApiModelProperty( name = "wxShopIds", value = "微信门店id", hidden = true )
    private String wxShopIds;

    @ApiModelProperty( name = "memberDiscount", value = "会员折扣", hidden = true )
    private Double memberDiscount;

}
