package com.gt.mall.param.phone.html;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * h5商城表单信息参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddHtmlFrom", description = "h5商城表单信息参数" )
@Getter
@Setter
public class PhoneAddHtmlFromDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productId", value = "H5ID", required = true )
    @NotNull( message = "H5id不能为空" )
    @Min( value = 1, message = "H5id不能小于1" )
    private Integer htmlId;

    /**
     * 属性1
     */
    @ApiModelProperty( name = "category", value = "属性1" )
    private String category;
    /**
     * 属性1数据
     */
    @ApiModelProperty( name = "categoryname", value = "属性1数据" )
    private String categoryname;
    /**
     * 属性2
     */
    @ApiModelProperty( name = "genre", value = "属性2" )
    private String genre;
    /**
     * 属性2数据
     */
    @ApiModelProperty( name = "genrename", value = "属性2数据" )
    private String genrename;
    /**
     * 属性3
     */
    @ApiModelProperty( name = "family", value = "属性3" )
    private String family;
    /**
     * 属性3数据
     */
    @ApiModelProperty( name = "familyname", value = "属性3数据" )
    private String familyname;
    /**
     * 属性4
     */
    @ApiModelProperty( name = "property", value = "属性4" )
    private String property;
    /**
     * 属性4数据
     */
    @ApiModelProperty( name = "propertyname", value = "属性4数据" )
    private String propertyname;
    /**
     * 属性5
     */
    @ApiModelProperty( name = "nature", value = "属性5" )
    private String nature;
    /**
     * 属性5数据
     */
    @ApiModelProperty( name = "naturename", value = "属性5数据" )
    private String naturename;
    /**
     * 属性6
     */
    @ApiModelProperty( name = "quality", value = "属性6" )
    private String quality;
    /**
     * 属性6数据
     */
    @ApiModelProperty( name = "qualityname", value = "属性6数据" )
    private String qualityname;
    /**
     * 属性7
     */
    @ApiModelProperty( name = "attribute", value = "属性7" )
    private String attribute;
    /**
     * 属性7数据
     */
    @ApiModelProperty( name = "attributename", value = "属性7数据" )
    private String attributename;
}
