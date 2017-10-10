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
 * 退出担保交易申请表
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_securitytrade_quit" )
public class MallSecuritytradeQuit extends Model< MallSecuritytradeQuit > {

    private static final long serialVersionUID = 1L;

    /**
     * 退出担保交易标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 退出理由id 关联字典表
     */
    @TableField( "quit_reason_id" )
    private Integer quitReasonId;
    /**
     * 补充意见
     */
    private String  remark;
    /**
     * 商家Id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;

    /**
     * 审核状态 0未审核 1审核通过 -1审核不通过
     */
    @TableField( "check_status" )
    private Integer checkStatus;
    /**
     * 不通过理由
     */
    @TableField( "refuse_reason" )
    private String  refuseReason;
    /**
     * 生效时间
     */
    @TableField( "effect_time" )
    private Date    effectTime;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
