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

    /**********维权记录常量START*********************/
    /*
    * 1退款申请
    * {type} 退款 退款退货
    * */
    public static final String RETURN_APPLY = "买家发起{type}申请";

    /*
    * 2待卖家处理
    * */
    public static final String WAIT_SELLER_DISPOSE          = "待卖家处理";
    /*
    * 2待卖家处理 备注
    * {date} 剩余时间
    * */
    public static final String WAIT_SELLER_DISPOSE_REMARK   = "如果卖家拒绝，需要您修改退款申请<br>如果卖家在{date}内未处理，系统将自动退款给您";
    /*
     * 3卖家同意申请
     * */
    public static final String SELLER_AGREE_APPLY           = "卖家同意申请";
    /*
     * 4买家已经退货
     * */
    public static final String BUYER_RETURN_GOODS           = "买家已经退货";
    /*
     * 5卖家已收到货，并退款
     * */
    public static final String SELLER_REFUND                = "卖家已收到货，并退款";
    /*
     * 5卖家已收到货，并退款 备注
     * */
    public static final String SELLER_REFUND_REMARK         = "退款申请完成";
    /*
     * 6退款成功
     * */
    public static final String REFUND_SUCCESS               = "退款成功";
    /*
     * 6退款成功 备注
     *  {payWay} 支付方式
     *  {price} 退款金额
     * */
    public static final String REFUND_SUCCESS_REMARK        = "退款至{payWay}：{price}元";
    /*
     * 7卖家拒绝退款申请
     * */
    public static final String SELLER_REFUSE_REFUND         = "卖家拒绝退款申请";
    /*
     * 7卖家拒绝退款申请 备注
     * */
    public static final String SELLER_REFUSE_REFUND_REMARK  = "卖家拒绝了本次售后服务申请";
    /*
     * 8申请维权介入
     * */
    public static final String PLATFORM_INTERVENTION        = "申请维权介入";
    /*
     * 8申请维权介入 备注 {name} 买家,卖家
     * */
    public static final String PLATFORM_INTERVENTION_REMARK = "{name}申请维权介入";
    /*
     * 买家撤销退款
     * */
    public static final String BUYER_REVOKE_REFUND          = "买家撤销退款";

    /**********维权记录常量END*********************/
    /**
     * 购物车信息存入cookie的时长
     */
    public static final int COOKIE_SHOP_CART_TIME = 60 * 60 * 24 * 30;

}
