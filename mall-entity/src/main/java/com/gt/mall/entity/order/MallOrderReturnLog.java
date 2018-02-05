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
 * 订单维权日志表
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_order_return_log" )
public class MallOrderReturnLog extends Model< MallOrderReturnLog > {

    private static final long serialVersionUID = 1L;

    /**
     * 订单维权日志标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 维权订单id 关联t_mall_order_return的id
     */
    @TableField( "return_id" )
    private Integer returnId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 备注
     */
    private String  remark;
    /**
     * 状态说明
     */
    @TableField( "status_content" )
    private String  statusContent;
    /**
     * 退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货
     * 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款
     */
    @TableField( "return_status" )
    private Integer returnStatus;
    /**
     * 截止时间(待卖家处理申请)
     */
    @TableField( "deadline_time" )
    private Date    deadlineTime;
    /**
     * 操作人id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 操作类型 0买家 1卖家 2系统信息
     */
    private Integer operator;
    /**
     * 是否有数据 0没有 1有
     */
    @TableField( "get_data" )
    private Integer getData;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
