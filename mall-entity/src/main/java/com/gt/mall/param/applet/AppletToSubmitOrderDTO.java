package com.gt.mall.param.applet;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 进入提交订单参数
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Getter
@Setter
public class AppletToSubmitOrderDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;
    /**
     * 商家ID
     */
    private Integer busUserId;
    /**
     * 会员ID
     */
    private Integer memberId;
    /**
     * 纬度
     */
    private String  latitude;
    /**
     * 经度
     */
    private String  longitude;

    /**
     * 版本号
     */
    private String version;
    /**
     * 1购物车结算 2立即购买 3去支付
     */
    private String from;

    /**************购物车结算传的参数*******************************/
    /**
     * 购物车商品ID集合
     */
    private String cartIds;

    /**************去支付传的参数*******************************/
    private Integer orderId;

    /**************立即购买传的参数*******************************/
    /**
     * 商品ID
     */
    private String  product_id;
    /**
     * 数量
     */
    private Integer product_num;
    /**
     * 商品规格ID集合
     */
    private String  product_specificas;
    /**
     * 商品价格
     */
    private Double  product_price;
    /**
     * 商品原价
     */
    private Double  primary_price;

}
