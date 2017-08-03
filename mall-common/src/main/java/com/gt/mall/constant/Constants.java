package com.gt.mall.constant;

/**
 * 定义常量公用类
 * User : yangqian
 * Date : 2017/7/21 0021
 * Time : 15:54
 */
public class Constants {

    /**
     * 定义session key 开头字符串
     */
    public final static String SESSION_KEY = "Mall-";

    /**
     * 定义 redis key 开头字符串
     */
    public final static String REDIS_KEY = "mall:";

    /**
     * 定义 socke key 开头字符串
     */
    public final static String SOCKEY_KEY = "Mall-";

    /**
     * 微场景
     */
    public static final String IMAGE_FOLDER_TYPE_4 = "4";

    /**
     * 微商城
     */
    public static final String IMAGE_FOLDER_TYPE_15 = "15";

    /**
     * h5商城
     */
    public static final String IMAGE_FOLDER_TYPE_20 = "20";

    /**
     * 微商城
     */
    public static final String IMAGE_FOLDER_TYPE_28 = "28";

    /**
     * 微商城
     */
    public static final String IMAGE_FOLDER_TYPE_29 = "29";

    /**
     * 网址名称
     */
    public final static String doMainName="多粉";

    /**
     * 发短信model
     */
    public static final Integer SMS_MODEL = 5;

    /** 商家账户在session key */
    public static final String SESSION_BUSINESS_KEY = SESSION_KEY + "business_key";

    /** 商家微信在session key */
    public static final String SESSION_WXPUBLICUSERS_KEY = SESSION_KEY + "wxPublicUsers";

    /** 会员 **/
    public static final String SESSION_MEMBER_KEY = SESSION_KEY + "member";

}
