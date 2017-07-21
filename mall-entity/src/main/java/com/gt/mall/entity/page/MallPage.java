package com.gt.mall.entity.page;

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
 * 页面表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_page" )
public class MallPage extends Model< MallPage > {

    private static final long serialVersionUID = 1L;

    /**
     * 页面标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 页面名称
     */
    @TableField( "pag_name" )
    private String  pagName;
    /**
     * 页面描述
     */
    @TableField( "pag_descript" )
    private String  pagDescript;
    /**
     * 页面分类
     */
    @TableField( "pag_type_id" )
    private Integer pagTypeId;
    /**
     * 样式
     */
    @TableField( "pag_css" )
    private String  pagCss;
    /**
     * 数据
     */
    @TableField( "pag_data" )
    private String  pagData;
    /**
     * 创建用户id(bus_user中的id)
     */
    @TableField( "pag_user_id" )
    private Integer pagUserId;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "pag_sto_id" )
    private Integer pagStoId;
    /**
     * 创建时间
     */
    @TableField( "pag_create_time" )
    private Date    pagCreateTime;
    /**
     * 是否是主页 0不是主页 1 是主页
     */
    @TableField( "pag_is_main" )
    private Integer pagIsMain;
    /**
     * 页面html
     */
    @TableField( "pag_html" )
    private String  pagHtml;
    /**
     * 二维码
     */
    private String  codeUrl;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPage{" +
			"id=" + id +
			", pagName=" + pagName +
			", pagDescript=" + pagDescript +
			", pagTypeId=" + pagTypeId +
			", pagCss=" + pagCss +
			", pagData=" + pagData +
			", pagUserId=" + pagUserId +
			", pagStoId=" + pagStoId +
			", pagCreateTime=" + pagCreateTime +
			", pagIsMain=" + pagIsMain +
			", pagHtml=" + pagHtml +
			", codeUrl=" + codeUrl +
			"}";
    }
}
