package com.gt.mall.entity.presale;

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
 * 预售消息提醒
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale_message_remind" )
public class MallPresaleMessageRemind extends Model< MallPresaleMessageRemind > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 预售id 关联t_mal_presale表的id
     */
    @TableField( "presale_id" )
    private Integer presaleId;
    /**
     * 提醒人id 关联t_wx_bus_member表的id
     */
    @TableField( "remind_user_id" )
    private Integer remindUserId;
    /**
     * 是否已经提醒  0没有提醒  1已经提醒
     */
    @TableField( "is_remind" )
    private Integer isRemind;
    /**
     * 是否开启提醒 0没有开启  1 已经开启
     */
    @TableField( "is_open" )
    private Integer isOpen;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 提醒时间
     */
    @TableField( "remind_time" )
    private Date    remindTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallPresaleMessageRemind{" +
            "id=" + id +
            ", presaleId=" + presaleId +
            ", remindUserId=" + remindUserId +
            ", isRemind=" + isRemind +
            ", isOpen=" + isOpen +
            ", createTime=" + createTime +
            ", remindTime=" + remindTime +
            "}";
    }
}
