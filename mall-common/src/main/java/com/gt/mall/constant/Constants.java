package com.gt.mall.constant;

import com.gt.mall.utils.PropertiesUtil;

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
     * cookie
     */
    public final static String COOKIE_KEY = "Mall-";

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

    /** 每日收入统计 redis key **/
    public static final String INCOME_COUNT_KEY = "todayIncomeCount";

    /** 页面访问统计 redis key **/
    public static final String PAGE_VISITOR_KEY    = "pageVisitor";
    /** 商品访问统计 redis key **/
    public static final String PRODUCT_VISITOR_KEY = "productVisitor";

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
    public static final String RETURN_APPLY                 = "买家发起{type}申请";
    /*
    * 1退款申请
    * {type} 退款 退款退货
    * */
    public static final String RETURN_AGAIN_APPLY           = "买家修改{type}申请";
    /*
    * 2待卖家处理
    * */
    public static final String WAIT_SELLER_DISPOSE          = "等待卖家处理";
    /*
    * 2待卖家处理 备注
    * {date} 剩余时间
    * */
    public static final String WAIT_SELLER_DISPOSE_REMARK   = "如果卖家拒绝，需要您修改退款申请";
    /*
  * 2待卖家处理 备注
  * {date} 剩余时间
  * */
    public static final String WAIT_SELLER_DISPOSE_REMARK1  = " 如果卖家在{date}内未处理，系统将自动退款给您";
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
    public static final String REFUND_SUCCESS_REMARK        = "退款金额{payWay}：{price}元";
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

    public static final String BUYER_REVOKE_REFUND_REMARK = "买家已撤销退款";
    /*
   * 买家重新提交物流信息
   * */
    public static final String BUYER_UPDATE_LOGISTICS     = "买家重新提交物流信息";

    /**********维权记录常量END*********************/
    /**
     * 购物车信息存入cookie的时长
     */
    public static final int COOKIE_SHOP_CART_TIME = 60 * 60 * 24 * 30;

    /**
     * 粉币规则的倍数
     */
    public static final int FENBI_RUL_MULTIPLE = 10;

    /**
     * 订单完成后 能退款的天数
     */
    public static final int ORDER_FINISH_RETURN_DAY = 7;

    /**
     * 物流签收后超过7天未确认收货，系统自动确认收货;
     */
    public static final int AUTO_CONFIRM_TAKE_DAY = 7;

    /**
     * 买家申请退款，卖家没有响应，系统自动退款给买家的天数
     */
    public static final int WAIT_APPLY_RETURN_DAY = 7;

    /**
     * 退货 确认收货 超出10天不做操作 系统自动确认收货天数
     */
    public static final int RETURN_AUTO_CONFIRM_TAKE_DAY = 10;

    /**
     * 退款方式
     */
    public static final String[] RETURN_WAY = { "仅退款", "退货退款", "仅退货" };

    /**
     * 预售 消费方式 对应字典表 1197
     */
    public static final int PRESALE_PAY_TYPE = 101;

    //    public static final String MEMBER_URL = PropertiesUtil.getWxmpDomain() + "phoneMemberController/${userid}/79B4DE7C/findMember_1.do";

    public static final String MEMBER_URL = PropertiesUtil.getMemberDomain() + "html/phone/index.html#/home/${userid}";

    public static final String COUPON_URL = PropertiesUtil.getWxmpDomain() + "phone_2MemberController/79B4DE7C/memberCardList_1.do?busId=${userid}";

    public static final String FINISH_REFUND_STATUS = "-14";

    public static final Integer SELLER_FoLLOW_STATUS = 13;

    //粉丝消息提醒模板
    public static final String   BUY_SUCCESS_NOTICE = "购买成功通知";
    //商家消息模板
    public static final String[] BUS_TEMPLATE_LIST  = { "付款成功通知", "确认收货提醒", "申请维权通知" };

    public static final String REDIS_SECKILL_NAME = "hSeckill";

    //视频教程url
    public static final String VIDEO_URL = PropertiesUtil.getCoreDomain() + "html/video/videoindex.html?serial=";

    /*短信模板*/
    //通知商家买家已付款--{1}成功购买您的商品{2}，请尽快登录商城后台发货
    public static final Long NOTICE_BUS_PAY_SUCCESS_MODEL_ID    = 78284L;
    //通知商家买家确认收货--{1}成功购买您的商品，并确认收货成功，查看详情请登录商城后台。
    public static final Long NOTICE_BUS_CONFIRM_MODEL_ID        = 78286L;
    // 通知商家买家发起维权退款--{1}发起了退款申请，请尽快登录商城后台查看并及时处理。
    public static final Long NOTICE_BUS_ORDER_RETURN_1_MODEL_ID = 78287L;
    //通知商家买家发起维权退货退款 -- {1}发起了退货退款申请，请尽快登录商城后台查看并及时处理。
    public static final Long NOTICE_BUS_ORDER_RETURN_2_MODEL_ID = 78431L;
    //通知商家粉丝申请超级销售员 --{1}正向您申请成为分销商，请及时登录后台操作审核，晚一步会被人抢走哦 ~
    public static final Long NOTICE_BUS_APPLY_SELLER_MODEL_ID   = 78288L;

    //买家购买商品付款成功  -- 支付成功，请您耐心等待，我们将稍后为您发货
    public static final Long NOTICE_MEMBER_PAY_SUCCESS_MODEL_ID = 78291L;
    // 商城发送验证码 -- 您的验证码：({1})，验证码10分钟内有效，请尽快完成验证。
    public static final Long MALL_CODE_MODEL_ID                 = 78293L;
    // 申请批发商验证码 -- 您申请成为批发商的验证码:{1}，5分钟内有效，请尽快完成验证。
    public static final Long APPLY_PIFA_CODE_MODEL_ID           = 78295L;
    // 申请超级销售员验证码 -- 您申请成为超级销售员的验证码为:{1}，5分钟内有效，请尽快完成验证。
    public static final Long APPLY_SELLER_CODE_MODEL_ID         = 78297L;

    public static final String ALIPAY_RETURN_URL = "alipay/79B4DE7C/refundVer2.do";

}
