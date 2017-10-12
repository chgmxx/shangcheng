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
    SHOP_ERROR( 1003, "店铺被删除" ),
    BUS_GUOQI_ERROR( 1004, "商家已过期" )
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
