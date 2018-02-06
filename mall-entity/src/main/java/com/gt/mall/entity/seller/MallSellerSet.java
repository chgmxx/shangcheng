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

/**
 * <p>
 * 超级销售员设置
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_set" )
public class MallSellerSet extends Model< MallSellerSet > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 姓名是否必填  0 不是必填  1必填
     */
    @TableField( "is_name_required" )
    private Integer    isNameRequired;
    /**
     * 公司名称是否必填  0 不是必填  1必填
     */
    @TableField( "is_company_required" )
    private Integer    isCompanyRequired;
    /**
     * 手机号是否必填  0不是必填    1必填
     */
    @TableField( "is_telephone_required" )
    private Integer    isTelephoneRequired;
    /**
     * 验证码是否必填  0不是必填  1必填
     */
    @TableField( "is_validate_required" )
    private Integer    isValidateRequired;
    /**
     * 备注是否必填 0不是必填  1必填
     */
    @TableField( "is_remark_required" )
    private Integer    isRemarkRequired;
    /**
     * 积分奖励
     */
    @TableField( "integral_reward" )
    private BigDecimal integralReward;
    /**
     * 消费金额满多少元可申请为超级销售员
     */
    @TableField( "consume_money" )
    private BigDecimal consumeMoney;
    /**
     * 提现规则  1按最底提现金额 2按倍数提现
     */
    @TableField( "withdrawal_type" )
    private Integer    withdrawalType;
    /**
     * 提现最底金额
     */
    @TableField( "withdrawal_lowest_money" )
    private BigDecimal withdrawalLowestMoney;
    /**
     * 提现的倍数
     */
    @TableField( "withdrawal_multiple" )
    private BigDecimal withdrawalMultiple;
    /**
     * 销售员说明
     */
    @TableField( "seller_remark" )
    private String     sellerRemark;
    /**
     * 商家id 关联bus_user 表的id
     */
    @TableField( "bus_user_id" )
    private Integer    busUserId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSellerSet{" +
            "id=" + id +
            ", isNameRequired=" + isNameRequired +
            ", isCompanyRequired=" + isCompanyRequired +
            ", isTelephoneRequired=" + isTelephoneRequired +
            ", isValidateRequired=" + isValidateRequired +
            ", isRemarkRequired=" + isRemarkRequired +
            ", integralReward=" + integralReward +
            ", consumeMoney=" + consumeMoney +
            ", withdrawalType=" + withdrawalType +
            ", withdrawalLowestMoney=" + withdrawalLowestMoney +
            ", withdrawalMultiple=" + withdrawalMultiple +
            ", sellerRemark=" + sellerRemark +
            ", busUserId=" + busUserId +
            "}";
    }
}
