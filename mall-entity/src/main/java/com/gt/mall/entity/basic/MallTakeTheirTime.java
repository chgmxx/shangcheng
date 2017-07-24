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
 * 自提点接待时间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_take_their_time" )
public class MallTakeTheirTime extends Model< MallTakeTheirTime > {

    private static final long serialVersionUID = 1L;

    /**
     * 自提点接待时间标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 自提点id 关联t_mall_take_their表的id
     */
    @TableField( "take_id" )
    private Integer takeId;
    /**
     * 接待起始时间
     */
    @TableField( "start_time" )
    private String  startTime;
    /**
     * 接待结束时间
     */
    @TableField( "end_time" )
    private String  endTime;
    /**
     * 接待天数 多天用逗号隔开
     */
    @TableField( "visit_days" )
    private String  visitDays;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallTakeTheirTime{" +
			"id=" + id +
			", takeId=" + takeId +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", visitDays=" + visitDays +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			"}";
    }
}
