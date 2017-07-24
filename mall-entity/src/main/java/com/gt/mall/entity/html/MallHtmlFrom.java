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
 * h5商城里面的表单信息
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_html_from" )
public class MallHtmlFrom extends Model< MallHtmlFrom > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 创建时间
     */
    private String  creattime;
    /**
     * 属性1
     */
    private String  category;
    /**
     * 属性1数据
     */
    private String  categoryname;
    /**
     * 属性2
     */
    private String  genre;
    /**
     * 属性2数据
     */
    private String  genrename;
    /**
     * 属性3
     */
    private String  family;
    /**
     * 属性3数据
     */
    private String  familyname;
    /**
     * 属性4
     */
    private String  property;
    /**
     * 属性4数据
     */
    private String  propertyname;
    /**
     * 属性5
     */
    private String  nature;
    /**
     * 属性5数据
     */
    private String  naturename;
    /**
     * 属性6
     */
    private String  quality;
    /**
     * 属性6数据
     */
    private String  qualityname;
    /**
     * 属性7
     */
    private String  attribute;
    /**
     * 属性7数据
     */
    private String  attributename;
    /**
     * t_mall_html 中的id
     */
    @TableField( "html_id" )
    private Integer htmlId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallHtmlFrom{" +
			"id=" + id +
			", creattime=" + creattime +
			", category=" + category +
			", categoryname=" + categoryname +
			", genre=" + genre +
			", genrename=" + genrename +
			", family=" + family +
			", familyname=" + familyname +
			", property=" + property +
			", propertyname=" + propertyname +
			", nature=" + nature +
			", naturename=" + naturename +
			", quality=" + quality +
			", qualityname=" + qualityname +
			", attribute=" + attribute +
			", attributename=" + attributename +
			", htmlId=" + htmlId +
			"}";
    }
}
