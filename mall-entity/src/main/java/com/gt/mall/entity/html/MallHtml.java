package com.gt.mall.entity.html;

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
 * 商城里面的H5
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_html" )
public class MallHtml extends Model< MallHtml > {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 模块所有信息
     */
    @TableField( "dataJson" )
    private String  dataJson;
    /**
     * 模块背景信息
     */
    @TableField( "dataBg" )
    private String  dataBg;
    /**
     * 每一页的所有信息
     */
    @TableField( "dataTransverse" )
    private String  dataTransverse;
    /**
     * 背景音乐url
     */
    private String  musicurl;
    /**
     * 音乐位置，1：右上；2：右下，3：左上，4：左下
     */
    private Integer addres;
    /**
     * 音乐名称
     */
    private String  musicname;
    /**
     * 播放器样式：取自字典表1048
     */
    @TableField( "player_style" )
    private String  playerStyle;
    /**
     * 创建人id（bus_user id）
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;
    /**
     * 创建日期
     */
    private String  creattime;
    /**
     * 二维码图片url
     */
    @TableField( "codeUrl" )
    private String  codeUrl;
    /**
     * 状态：0是发布，1：未发布
     */
    private Integer state;
    /**
     * 创建人的父类id（0，代表无父类，其余代表父类bus_user 的id）
     */
    private Integer pid;
    /**
     * 商城名称
     */
    private String  htmlname;
    /**
     * 举报状态，0是正常，1举报
     */
    private Integer reportstate;
    /**
     * 来源:1 后台，2，商家
     */
    @TableField( "source_type" )
    private Integer sourceType;
    /**
     * 模板id ，无模板为0
     */
    private Integer modelid;
    /**
     * 介绍
     */
    private String  introduce;
    /**
     * 背景图
     */
    private String  bakurl;

    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallHtml{" +
            "id=" + id +
            ", dataJson=" + dataJson +
            ", dataBg=" + dataBg +
            ", dataTransverse=" + dataTransverse +
            ", musicurl=" + musicurl +
            ", addres=" + addres +
            ", musicname=" + musicname +
            ", playerStyle=" + playerStyle +
            ", busUserId=" + busUserId +
            ", creattime=" + creattime +
            ", codeUrl=" + codeUrl +
            ", state=" + state +
            ", pid=" + pid +
            ", htmlname=" + htmlname +
            ", reportstate=" + reportstate +
            ", sourceType=" + sourceType +
            ", modelid=" + modelid +
            ", introduce=" + introduce +
            ", bakurl=" + bakurl +
            ", isDelete=" + isDelete +
            "}";
    }
}
