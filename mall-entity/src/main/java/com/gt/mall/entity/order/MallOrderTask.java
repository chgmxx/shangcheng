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
 * 订单任务表
 * </p>
 *
 * @author yangqian
 * @since 2018-02-02
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_order_task" )
public class MallOrderTask extends Model< MallOrderTask > {

    private static final long serialVersionUID = 1L;

    /**
     * 订单任务标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 类型 1关闭订单 2自动确认收货 3赠送物品 4联盟积分 5收入记录 6取消维权 7自动退款给买家 8自动确认收货并退款至买家
     */
    @TableField( "task_type" )
    private Integer taskType;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 订单号
     */
    @TableField( "order_no" )
    private String  orderNo;
    /**
     * 订单ID
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 订单维权ID
     */
    @TableField( "order_return_id" )
    private Integer orderReturnId;
    /**
     * 等待天数
     */
    @TableField( "return_day" )
    private Integer returnDay;
    /**
     * 是否已经删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
