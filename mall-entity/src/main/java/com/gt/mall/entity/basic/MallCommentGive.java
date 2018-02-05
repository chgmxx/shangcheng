package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 评论送礼
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_comment_give" )
public class MallCommentGive extends Model< MallCommentGive > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 送礼类型 1 送积分   2送粉币
     */
    @TableField( "give_type" )
    private Integer giveType;
    /**
     * 送礼状态  -1 差评 0中评  1好评
     */
    @TableField( "give_status" )
    private Integer giveStatus;
    /**
     * 送礼数量
     */
    private Integer num;
    /**
     * 是否启用  1启用 0禁用
     */
    @TableField( "is_enable" )
    private Integer isEnable;
    /**
     * 用户id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallCommentGive{" +
            "id=" + id +
            ", giveType=" + giveType +
            ", giveStatus=" + giveStatus +
            ", num=" + num +
            ", isEnable=" + isEnable +
            ", userId=" + userId +
            "}";
    }
}
