package com.gt.mall.entity.order;

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
 * 找人代付
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_daifu" )
public class MallDaifu extends Model< MallDaifu > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 订单id关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 代付订单号
     */
    @TableField( "df_order_no" )
    private String     dfOrderNo;
    /**
     * 代付人id 关联t_wx_bus_member表的id
     */
    @TableField( "df_user_id" )
    private Integer    dfUserId;
    /**
     * 代付支付方式 1 微信支付  2  支付宝支付  3小程序支付
     */
    @TableField( "df_pay_way" )
    private Integer    dfPayWay;
    /**
     * 付款时间
     */
    @TableField( "df_pay_time" )
    private Date       dfPayTime;
    /**
     * 代付金额
     */
    @TableField( "df_pay_money" )
    private BigDecimal dfPayMoney;
    /**
     * 代付状态 0未支付 1支付  -1 退款成功
     */
    @TableField( "df_pay_status" )
    private Integer    dfPayStatus;
    /**
     * 代付退款单号
     */
    @TableField( "df_return_no" )
    private String     dfReturnNo;
    /**
     * 代付退款时间
     */
    @TableField( "df_return_time" )
    private Date       dfReturnTime;
    /**
     * 退款价格
     */
    @TableField( "df_return_money" )
    private BigDecimal dfReturnMoney;
    /**
     * 代付退款状态  0还未退款 1发起退款 2 退款成功 -1 退款失败
     */
    @TableField( "df_return_status" )
    private Integer    dfReturnStatus;
    /**
     * 创建时间
     */
    @TableField( "df_create_time" )
    private Date       dfCreateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallDaifu{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", dfOrderNo=" + dfOrderNo +
            ", dfUserId=" + dfUserId +
            ", dfPayWay=" + dfPayWay +
            ", dfPayTime=" + dfPayTime +
            ", dfPayMoney=" + dfPayMoney +
            ", dfPayStatus=" + dfPayStatus +
            ", dfReturnNo=" + dfReturnNo +
            ", dfReturnTime=" + dfReturnTime +
            ", dfReturnStatus=" + dfReturnStatus +
            ", dfCreateTime=" + dfCreateTime +
            "}";
    }
}
