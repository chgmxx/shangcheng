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
     * 网址名称
     */
    public final static String doMainName = "多粉";

    /**
     * 发短信model
     */
    public static final Integer SMS_MODEL = 5;

    /** 商家账户在session key */
    public static final String SESSION_BUSINESS_KEY = "business_key";

    /** 商家微信在session key */
    public static final String SESSION_WXPUBLICUSERS_KEY = "wxPublicUsers";

    /** 会员 **/
    public static final String SESSION_MEMBER_KEY = "member";

    /** 获取商家主账号的 session key **/
    public static final String SESSION_ADMIN_KEY = "PidBusId";

    /**
     * 生成二维码所需的场景值
     */
    public static final String SCENE_KEY = "3";

    /**
     * 门店中间表model
     */
    public static final Integer SHOP_SUB_SOP_MODEL = 2;

    /**
     * 商城提现
     */
    public static final String ENTER_PAY_MODEL = "12";

    /**
     * 小程序的style
     */
    public static final Integer APPLET_STYLE = 4;

    /**
     * 联盟 model
     */
    public static final Integer UNION_MODEL = 1;

    /**
     * 支付model
     */
    public static final Integer PAY_MODEL = 3;

    /**
     * redis 缓存时间
     */
    public static final int REDIS_SECONDS = 60 * 30;

    /**
     * 评论送粉币类型
     */
    public static final int COMMNET_GIVE_TYPE = 30;

    /**
     * 粉币赠送类型
     */
    public static final int FENBI_GIEVE_TYPE = 1;

    /**
     * 购物车信息存入cookie的时长
     */
    public static final int COOKIE_SHOP_CART_TIME = 60 * 60 * 24 * 30;

}
