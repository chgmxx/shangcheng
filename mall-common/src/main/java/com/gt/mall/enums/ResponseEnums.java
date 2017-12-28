package com.gt.mall.enums;

/**
 * 响应成功Code 类
 *
 * @author zhangmz
 * @create 2017/6/12
 */
public enum ResponseEnums {
    SUCCESS( 0, "成功" ),
    ERROR( 1, "请求失败" ),
    NULL_ERROR( 1000, "请求数据为空" ),
    NEED_LOGIN( 1001, "请前往登录" ),
    INTER_ERROR( 1002, "请求接口异常" ),
    PARAMS_NULL_ERROR( 1003, "参数传值不完整" ),
    BUS_GUOQI_ERROR( 1004, "商家已过期" ),
    //商品详情页面的响应  start
    CAR_RECEVIE_GUOQI_ERROR( 1005, "卡券包已过期不能购买" ),
    PRODUCT_NULL_ERROR( 1006, "商品已被删除或未上架" ),
    SHOP_NULL_ERROR( 1007, "店铺已被删除" ),
    STOCK_NULL_ERROR( 1008, "您购买商品的库存不够，请重新选择商品" ),
    MAX_BUY_ERROR( 1009, "您购买的数量已经超过限购的数量" ),
    INV_NULL_ERROR( 1010, "您还未选择规格，请选择" ),
    ACTIVITY_ERROR( 1011, "活动被删除" ),
    ACTIVITY_MONEY_ERROR( 1012, "活动保证金未交" ),
    INV_NO_JOIN_ERROR( 1013, "该规格未参加活动" ),
    //商品详情页面的响应  end

    //订单相关的响应 start
    ORDER_PARAMS_ERROR( 1014, "订单参数不完整" ),
    FENBI_NULL_ERROR( 1015, "您的粉币不足，请充值" ),
    JIFEN_NULL_ERROR( 1016, "您的积分不足，请充值" ),
    ORDER_PAY_ERROR( 1017, "订单已支付" ),
    NO_SELECT_PAY_WAY_ERROR( 1018, "您还未选择支付方式" ),
    NO_TIHUO_ERROR( 1019, "您还没填写提货人信息" ),
    NO_SELECT_DELIVERY_WAY( 1020, "您还没选中配送方式" ),
    NO_SELECT_SHOUHUO_ADDRESS( 1021, "您还没选择收货地址" ),
    FLOW_NO_PHONE_ERROR( 1022, "流量充值，您还没输入手机号码" ),
    FLOW_ERROR( 1023, "流量数量不足，不能充值" ),
    FLOW_LIANTONG_ERROR( 1024, "充值失败,联通号码至少30MB" ),

    //订单相关的响应 end

    URL_GUOQI_ERROR( 1025, "链接已过期/模块已删除" ),
    GRAND_ERROR( 1026, "请使用微信进行授权" )
    ;

    private final int    code;
    private final String desc;

    ResponseEnums( int code, String desc ) {
	this.code = code;
	this.desc = desc;
    }

    public int getCode() {
	return code;
    }

    public String getDesc() {
	return desc;
    }
}
