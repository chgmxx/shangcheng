package com.gt.mall.entity.seller;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 超级销售员
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller" )
public class MallSeller extends Model< MallSeller > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 销售员id 关联t_wx_bus_member表的id
     */
    @TableField( "member_id" )
    private Integer    memberId;
    /**
     * 推荐人id 关联t_wx_bus_member表的id
     */
    @TableField( "referees_member_id" )
    private Integer    refereesMemberId;
    /**
     * 姓名
     */
    @TableField( "user_name" )
    private String     userName;
    /**
     * 公司名称
     */
    @TableField( "company_name" )
    private String     companyName;
    /**
     * 手机号码
     */
    private String     telephone;
    /**
     * 佣金
     */
    private BigDecimal commission;
    /**
     * 历史累计佣金
     */
    @TableField( "total_commission" )
    private BigDecimal totalCommission;
    /**
     * 冻结佣金
     */
    @TableField( "freeze_commission" )
    private BigDecimal freezeCommission;
    /**
     * 可提现佣金
     */
    @TableField( "can_presented_commission" )
    private BigDecimal canPresentedCommission;
    /**
     * 已提现佣金
     */
    @TableField( "already_presented_commission" )
    private BigDecimal alreadyPresentedCommission;
    /**
     * 收益积分
     */
    @TableField( "income_integral" )
    private BigDecimal incomeIntegral;
    /**
     * 销售金额
     */
    @TableField( "sale_money" )
    private BigDecimal saleMoney;
    /**
     * 用户类型  0普通粉丝   1超级销售员
     */
    @TableField( "user_type" )
    private Integer    userType;
    /**
     * 商家id  关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer    busUserId;
    /**
     * 申请时间
     */
    @TableField( "apply_time" )
    private Date       applyTime;
    /**
     * 审核时间
     */
    @TableField( "check_time" )
    private Date       checkTime;
    /**
     * 审核状态  0未审核  1审核通过  -1审核不通过 -2 待申请销售员
     */
    @TableField( "check_status" )
    private Integer    checkStatus;
    /**
     * 审核不通过理由
     */
    @TableField( "check_pass_reason" )
    private String     checkPassReason;
    /**
     * 二维码路径
     */
    @TableField( "qr_code_path" )
    private String     qrCodePath;
    /**
     * 加入时间
     */
    @TableField( "add_time" )
    private Date       addTime;
    /**
     * 备注
     */
    private String     remark;
    /**
     * 是否开始使用  0 未开始    1已经开始 -1 暂停
     */
    @TableField( "is_start_use" )
    private Integer    isStartUse;
    /**
     * 生成二维码时的key 用来关注送积分
     */
    @TableField( "scene_key" )
    private String     sceneKey;
    /**
     * 生成二维码时返回的ticket
     */
    @TableField( "qr_code_ticket" )
    private String     qrCodeTicket;
    /**
     * 是否已经送过关注积分给推荐人  0 未送过 1 已送过
     */
    @TableField( "is_send_focus_integral" )
    private Integer    isSendFocusIntegral;
    /**
     * 是否已经关注过公众号  0未关注   1已关注
     */
    @TableField( "is_focus_public" )
    private Integer    isFocusPublic;
    /**
     * 销售员的推广海报地址
     */
    @TableField( "promotion_poster_path" )
    private String     promotionPosterPath;
    /**
     * 销售员的用户头像地址，用于生成推广海报的
     */
    @TableField( "head_image_path" )
    private String     headImagePath;
    /**
     * 微信和PC 的数据是否已经合并  0未合并   1已合并
     */
    @TableField( "is_merge_data" )
    private Integer    isMergeData;
    /**
     * 浏览器二维码路径
     */
    @TableField( "ucqr_code_path" )
    private String     ucqrCodePath;
    /**
     * 销售员的浏览器版推广海报地址
     */
    @TableField( "ucpromotion_poster_path" )
    private String     ucpromotionPosterPath;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSeller{" +
            "id=" + id +
            ", memberId=" + memberId +
            ", refereesMemberId=" + refereesMemberId +
            ", userName=" + userName +
            ", companyName=" + companyName +
            ", telephone=" + telephone +
            ", commission=" + commission +
            ", totalCommission=" + totalCommission +
            ", freezeCommission=" + freezeCommission +
            ", canPresentedCommission=" + canPresentedCommission +
            ", alreadyPresentedCommission=" + alreadyPresentedCommission +
            ", incomeIntegral=" + incomeIntegral +
            ", saleMoney=" + saleMoney +
            ", userType=" + userType +
            ", busUserId=" + busUserId +
            ", applyTime=" + applyTime +
            ", checkTime=" + checkTime +
            ", checkStatus=" + checkStatus +
            ", checkPassReason=" + checkPassReason +
            ", qrCodePath=" + qrCodePath +
            ", addTime=" + addTime +
            ", remark=" + remark +
            ", isStartUse=" + isStartUse +
            ", sceneKey=" + sceneKey +
            ", qrCodeTicket=" + qrCodeTicket +
            ", isSendFocusIntegral=" + isSendFocusIntegral +
            ", isFocusPublic=" + isFocusPublic +
            ", promotionPosterPath=" + promotionPosterPath +
            ", headImagePath=" + headImagePath +
            ", isMergeData=" + isMergeData +
            ", ucqrCodePath=" + ucqrCodePath +
            ", ucpromotionPosterPath=" + ucpromotionPosterPath +
            "}";
    }
}
