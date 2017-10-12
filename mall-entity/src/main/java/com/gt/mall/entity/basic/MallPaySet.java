package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商城交易支付设置
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_pay_set" )
public class MallPaySet extends Model< MallPaySet > {

    private static final long serialVersionUID = 1L;

    /**
     * 交易设置标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 是否允许货到付款  0 不允许  1允许
     */
    @TableField( "is_delivery_pay" )
    private Integer isDeliveryPay;
    /**
     * 是否开启上门自提的功能 0 不允许 1允许
     */
    @TableField( "is_take_their" )
    private Integer isTakeTheir;
    /**
     * 用户id 关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否开启招人代付的功能 0不允许 1允许
     */
    @TableField( "is_daifu" )
    private Integer isDaifu;
    /**
     * 是否开启评论 0不开启   1开启 2关闭评论及买家评价
     */
    @TableField( "is_comment" )
    private Integer isComment;
    /**
     * 是否开启评论审核 0不开启   1开启
     */
    @TableField( "is_comment_check" )
    private Integer isCommentCheck;
    /**
     * 是否开启评论送礼 0不开启   1开启
     */
    @TableField( "is_comment_give" )
    private Integer isCommentGive;
    /**
     * 是否开启消息模板  0 未开启   1已开启
     */
    @TableField( "is_message" )
    private Integer isMessage;
    /**
     * 是否开启商品预售  0 未开启   1已开启
     */
    @TableField( "is_presale" )
    private Integer isPresale;
    /**
     * 是否开启预售赠送礼品 0 未开启   1已开启
     */
    @TableField( "is_presale_give" )
    private Integer isPresaleGive;
    /**
     * 模板内容 [{模板标题:模板id}]
     */
    @TableField( "message_json" )
    private String  messageJson;
    /**
     * 是否开启短信消息提醒（提醒买家）
     */
    @TableField( "is_sms_member" )
    private Integer isSmsMember;
    /**
     * 消息短信提醒内容 用于消息短信提醒 {消息类型:消息类容}  消息类型  1 购买成功提醒
     */
    @TableField( "sms_message" )
    private String  smsMessage;
    /**
     * 是否开启批发  0 未开启   1已开启
     */
    @TableField( "is_pf" )
    private Integer isPf;
    /**
     * 是否开启批发审核   0未开启   1已开启
     */
    @TableField( "is_pf_check" )
    private Integer isPfCheck;
    /**
     * 批发设置，json方式存储 is开头的属性 统一是0 未开启  1开启 {isHpMoney:是否是混批金额条件， hpMoney:混批达到的金额条件， isHpNum:是否开启混批达到的件数， hpNum 混批达到的件数， isSpHand:是否开启手批达到的几手,spHand 手批达到的几手 }
     */
    @TableField( "pf_set" )
    private String  pfSet;
    /**
     * 批发商说明
     */
    @TableField( "pf_remark" )
    private String  pfRemark;
    /**
     * 批发商申请说明
     */
    @TableField( "pf_apply_remark" )
    private String  pfApplyRemark;
    /**
     * 是否开启超级销售员   0 未开启  1开启
     */
    @TableField( "is_seller" )
    private Integer isSeller;
    /**
     * 是否开启审核超级销售员  0未开启   1 开启
     */
    @TableField( "is_check_seller" )
    private Integer isCheckSeller;
    /**
     * 手机商城底部菜单
     */
    @TableField( "footer_json" )
    private String  footerJson;

    /**
     * 配色风格key 关联字典表k001的key
     */
    @TableField( "style_key" )
    private Integer  styleKey;
    /**
     * 待付款订单取消时间设置
     */
    @TableField( "order_cancel" )
    private Integer  orderCancel ;
    /**
     * 商家消息模板内容 {模板标题:模板id}
     */
    @TableField( "bus_message_json" )
    private String  busMessageJson ;
    /**
     * 是否开启担保交易  0未开启   1 开启
     */
    @TableField( "is_securitytrade" )
    private Integer isSecuritytrade;
    /**
     * 是否开启编辑手机端菜单 0 未开启 1已开启
     */
    @TableField( "is_footer" )
    private Integer isFooter;
    /**
     * 接收申请审核手机
     */
    @TableField( "check_seller_phone" )
    private String  checkSellerPhone;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPaySet{" +
			"id=" + id +
			", isDeliveryPay=" + isDeliveryPay +
			", isTakeTheir=" + isTakeTheir +
			", userId=" + userId +
			", createTime=" + createTime +
			", isDaifu=" + isDaifu +
			", isComment=" + isComment +
			", isCommentCheck=" + isCommentCheck +
			", isCommentGive=" + isCommentGive +
			", isMessage=" + isMessage +
			", isPresale=" + isPresale +
			", isPresaleGive=" + isPresaleGive +
			", messageJson=" + messageJson +
			", isSmsMember=" + isSmsMember +
			", smsMessage=" + smsMessage +
			", isPf=" + isPf +
			", isPfCheck=" + isPfCheck +
			", pfSet=" + pfSet +
			", pfRemark=" + pfRemark +
			", pfApplyRemark=" + pfApplyRemark +
			", isSeller=" + isSeller +
			", isCheckSeller=" + isCheckSeller +
			", footerJson=" + footerJson +
			", styleKey=" + styleKey +
			", orderCancel=" + orderCancel +
			", busMessageJson=" + busMessageJson +
			", isSecuritytrade=" + isSecuritytrade +
			", isFooter=" + isFooter +
			", checkSellerPhone=" + checkSellerPhone +
			"}";
    }
}
