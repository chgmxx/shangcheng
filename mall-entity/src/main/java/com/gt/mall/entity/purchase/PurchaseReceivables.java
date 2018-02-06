package com.gt.mall.entity.purchase;

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
 * <p>
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Data
@Accessors( chain = true )
@TableName( "purchase_receivables" )
public class PurchaseReceivables extends Model< PurchaseReceivables > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单id
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 粉币
     */
    @TableField( "fans_corrency" )
    private Double  fansCorrency;
    /**
     * 积分
     */
    private Double  integral;
    /**
     * 优惠价
     */
    private String  coupon;
    /**
     * 是否分期0不分1分
     */
    @TableField( "have_term" )
    private String  haveTerm;
    /**
     * 金额
     */
    private Double  money;
    /**
     * 付款时间
     */
    @TableField( "buy_time" )
    private Date    buyTime;
    /**
     * 付款方式
     */
    @TableField( "buy_mode" )
    private String  buyMode;
    /**
     * 分期id
     */
    @TableField( "term_id" )
    private Integer termId;
    /**
     * 商家id
     */
    @TableField( "bus_id" )
    private Integer busId;
    /**
     * 用户id
     */
    @TableField( "member_id" )
    private Integer memberId;
    /**
     * 折扣
     */
    private Double  discount;
    /**
     * 0未支付1已支付
     */
    @TableField( "buy_status" )
    private Integer buyStatus;
    /**
     * 收款订单号
     */
    @TableField( "receivables_number" )
    private String  receivablesNumber;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PurchaseReceivables{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", fansCorrency=" + fansCorrency +
            ", integral=" + integral +
            ", coupon=" + coupon +
            ", haveTerm=" + haveTerm +
            ", money=" + money +
            ", buyTime=" + buyTime +
            ", buyMode=" + buyMode +
            ", termId=" + termId +
            ", busId=" + busId +
            ", memberId=" + memberId +
            ", discount=" + discount +
            ", buyStatus=" + buyStatus +
            ", receivablesNumber=" + receivablesNumber +
            "}";
    }
}
