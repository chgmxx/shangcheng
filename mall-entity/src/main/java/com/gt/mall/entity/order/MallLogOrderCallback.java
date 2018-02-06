package com.gt.mall.entity.order;

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
 * 支付成功，但回调失败的日志
 * </p>
 *
 * @author yangqian
 * @since 2017-10-30
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_log_order_callback" )
public class MallLogOrderCallback extends Model< MallLogOrderCallback > {

    private static final long serialVersionUID = 1401344099726332083L;
    /**
     * 订单回调
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单编号
     */
    @TableField( "order_no" )
    private String  orderNo;
    /**
     * 回调失败说明
     */
    @TableField( "log_message" )
    private String  logMessage;
    /**
     * 日志状态 0回调函数未执行 1 流量充值失败 2联盟未核销 3 erp未同步库存 4 会员未回调
     */
    @TableField( "log_status" )
    private Integer logStatus;
    /**
     * 是否已解决 1已解决 0 未解决
     */
    @TableField( "is_resolved" )
    private Integer isResolved;
    /**
     * 解决时间
     */
    @TableField( "resolved_time" )
    private Date    resolvedTime;
    /**
     * 创建日志时间
     */
    @TableField( "create_time" )
    private Date    createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public MallLogOrderCallback() {
    }

    public MallLogOrderCallback( String orderNo, String logMessage, Integer logStatus, Date createTime ) {
        this.orderNo = orderNo;
        this.logMessage = logMessage;
        this.logStatus = logStatus;
        this.createTime = createTime;
    }
}
