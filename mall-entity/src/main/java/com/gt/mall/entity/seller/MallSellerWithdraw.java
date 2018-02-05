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
 * 超级销售员提现记录表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_withdraw" )
public class MallSellerWithdraw extends Model< MallSellerWithdraw > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 销售员id
     */
    @TableField( "sale_member_id" )
    private Integer    saleMemberId;
    /**
     * 申请时间
     */
    @TableField( "applay_time" )
    private Date       applayTime;
    /**
     * 提现金额
     */
    @TableField( "withdraw_money" )
    private BigDecimal withdrawMoney;
    /**
     * 提现状态   1待打款   2已打款
     */
    @TableField( "withdraw_status" )
    private Integer    withdrawStatus;
    /**
     * 提现订单号
     */
    @TableField( "withdraw_order_no" )
    private String     withdrawOrderNo;
    /**
     * 审核状态  0未审核   1审核通过  -1审核失败
     */
    @TableField( "check_status" )
    private Integer    checkStatus;
    /**
     * 审核不通过理由
     */
    @TableField( "check_pass_reason" )
    private String     checkPassReason;
    /**
     * 审核时间
     */
    @TableField( "check_time" )
    private Date       checkTime;
    /**
     * 打款时间
     */
    @TableField( "paysuccess_time" )
    private Date       paysuccessTime;
    /**
     * 提现方式  1微信  2储值卡
     */
    @TableField( "withdraw_type" )
    private Integer    withdrawType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSellerWithdraw{" +
            "id=" + id +
            ", saleMemberId=" + saleMemberId +
            ", applayTime=" + applayTime +
            ", withdrawMoney=" + withdrawMoney +
            ", withdrawStatus=" + withdrawStatus +
            ", withdrawOrderNo=" + withdrawOrderNo +
            ", checkStatus=" + checkStatus +
            ", checkPassReason=" + checkPassReason +
            ", checkTime=" + checkTime +
            ", paysuccessTime=" + paysuccessTime +
            ", withdrawType=" + withdrawType +
            "}";
    }
}
