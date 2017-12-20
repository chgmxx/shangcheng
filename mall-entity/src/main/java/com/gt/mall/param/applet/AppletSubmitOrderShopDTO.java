package com.gt.mall.param.applet;

import com.gt.mall.bean.member.Coupons;
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
public class AppletSubmitOrderShopDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;
    /**
     * 收货地址
     */
    private Integer receiveId;
    /**
     * 订单总价（不包含运费）
     */
    private Double  productTotalMoney;
    /**
     * 订单金额（包括运费） 折扣优惠后的价格  必传
     */
    private Double  orderMoney;
    /**
     * 运费
     */
    private Double  orderFreightMoney;

    /**
     * 买家留言
     */
    private String orderBuyerMessage;

    /**
     * 付款方式（1，微信支付 2，货到付款 3、储值卡支付 4、积分支付 5扫码支付 6到店支付 7 找人代付 8、粉币支付  9、支付宝支付  10 小程序微信支付） 必传
     */
    private Integer orderPayWay;

    /**
     * 配送方式（1, 快递配送  2,上门自提） 必传
     */
    private Integer deliveryMethod;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 参加活动id
     */
    private Integer pJoinId;

    /**
     * 充值流量手机号码
     */
    private String flowPhone;

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 微信门店id
     */
    private Integer wxShopId;

    /**
     * 商品集合集合
     */
    private List< AppletSubmitOrderProductDTO > orderDetail;

    /**
     * 选择的优惠券对象
     */
    private Coupons selectCounpon;

    //当配送方式 为 2 上门自提时，传以下参数

    /**
     * 预约人姓名
     */
    private String  appointmentName;
    /**
     * 预约人联系方式
     */
    private String  appointmentTelephone;
    /**
     * 预约时间
     */
    private String  appointmentTime;
    /**
     * 自提点id
     */
    private Integer takeTheirId;

    /**
     * 预约开始时间 小时:分钟的格式
     */
    private String appointmentStartTime;
    /**
     * 预约结束时间    小时:分钟的格式
     */
    private String appointmentEndTime;

}
