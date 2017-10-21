package com.gt.mall.enums;

/**
 * 响应成功Code 类
 *
 * @author zhangmz
 * @create 2017/6/12
 */
public enum ResponseEnums {
    SUCCESS( 1, "成功" ),
    ERROR( -1, "请求失败" ),
    NEED_LOGIN( 1001, "请前往登录" ),
    AUTHENTICATION( 10, "非法认证" ),
    SYSTEM_ERROR( 9999, "系統異常" ),
    INTER_ERROR( 1002, "请求接口异常" ),
    NULL_ERROR( 0, "请求数据为空" ),
    REFRESH_PAGE( 1002, "需要刷新页面" ),
    BUS_GUOQI_ERROR( 1004, "商家已过期" ),
    NO_SHARE_ERROR( 1005, "用户没在微信浏览器登陆，不用获取微信接口" ),
    PRODUCT_NULL_ERROR( 1006, "商品已被删除或未上架" ),
    SHOP_NULL_ERROR( 1007, "店铺已被删除" ),
    STOCK_NULL_ERROR( 1008, "您购买商品的库存不够，请重新选择商品" ),
    MAX_BUY_ERROR( 1009, "您购买的数量已经超过限购的数量" ),
    INV_NULL_ERROR( 1010, "您还未选择规格，请选择" ),
    ACTIVITY_ERROR( 1011, "活动被删除" ),
    ACTIVITY_MONEY_ERROR( 1012, "活动保证金未交" ),
    INV_NO_JOIN_ERROR( 1013, "该规格未参加活动" );

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
