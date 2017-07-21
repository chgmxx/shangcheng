package com.gt.mall.entity.pifa;

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
 * 批发申请表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_pifa_apply" )
public class MallPifaApply extends Model< MallPifaApply > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 姓名
     */
    private String  name;
    /**
     * 公司名称
     */
    @TableField( "company_name" )
    private String  companyName;
    /**
     * 手机号码
     */
    private String  telephone;
    /**
     * 备注
     */
    private String  remark;
    /**
     * 申请人id    关联t_wx_member表的id
     */
    @TableField( "member_id" )
    private Integer memberId;
    /**
     * 商户id     关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;
    /**
     * 申请时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 审核状态  0 未审核  1审核通过   -1 审核不通过
     */
    private Integer status;
    /**
     * 审核时间
     */
    @TableField( "check_time" )
    private Date    checkTime;
    /**
     * 是否启用   1启用    -1禁用
     */
    @TableField( "is_use" )
    private Integer isUse;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPifaApply{" +
			"id=" + id +
			", name=" + name +
			", companyName=" + companyName +
			", telephone=" + telephone +
			", remark=" + remark +
			", memberId=" + memberId +
			", busUserId=" + busUserId +
			", createTime=" + createTime +
			", status=" + status +
			", checkTime=" + checkTime +
			", isUse=" + isUse +
			"}";
    }
}
