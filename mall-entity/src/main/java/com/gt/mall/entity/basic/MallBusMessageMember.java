package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * <p>
 * 商家消息模板提醒用户表
 * </p>
 *
 * @author yangqian
 * @since 2017-12-28
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_bus_message_member" )
public class MallBusMessageMember extends Model< MallBusMessageMember > {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 会员id关联 t_wx_bus_member表的id
     */
    @TableField( "member_id" )
    private Integer memberId;
    /**
     * 公众号id
     */
    @TableField( "public_id" )
    private Integer publicId;
    /**
     * 会员昵称
     */
    @TableField( "nick_name" )
    private String  nickName;
    /**
     * 头像
     */
    @TableField( "head_img_url" )
    private String  headImgUrl;
    /**
     * 关注时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否已删除  1已删除 0未删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 商家ID
     */
    @TableField( "bus_id" )
    private Integer busId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
