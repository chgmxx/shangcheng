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
 * 物流信息表
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_logistics_info" )
public class MallLogisticsInfo extends Model< MallLogisticsInfo > {

    private static final long serialVersionUID = 1L;

    /**
     * 物流信息标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单ID
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 物流编号
     */
    private String  sn;
    /**
     * 时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 状态
     */
    private String  state;
    /**
     * 内容
     */
    private String  context;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
