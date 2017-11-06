package com.gt.mall.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ApiModel( value = "MemberAddress", description = "收货地址验证" )
@Getter
@Setter
public class MemberAddress implements Serializable {

    private static final long serialVersionUID = 3184995296305348418L;

    @ApiModelProperty( name = "id", value = "地址ID" )
    private Integer id;

    @ApiModelProperty( name = "memName", value = "收货人姓名", required = true )
    private String memName;

    @ApiModelProperty( name = "memPhone", value = "收货人手机号码", required = true )
    private String memPhone;

    @ApiModelProperty( name = "memAddress", value = "收货人地址", required = true )
    private String memAddress;

    @ApiModelProperty( name = "memLongitude", value = "经度", required = true )
    private String memLongitude;

    @ApiModelProperty( name = "memLatitude", value = "纬度", required = true )
    private String memLatitude;

    @ApiModelProperty( name = "dfMemberId", value = "粉丝id", hidden = true )
    private Integer dfMemberId;

    @ApiModelProperty( name = "memDefault", value = "是否是默认地址" )
    private Integer memDefault;

    @ApiModelProperty( name = "memHouseMember", value = "门牌号", hidden = true )
    private String memHouseMember;

    @ApiModelProperty( name = "memZipCode", value = "邮政编码", hidden = true )
    private String memZipCode;

    @ApiModelProperty( name = "memProvince", value = "省份id" )
    private Integer memProvince;

    @ApiModelProperty( name = "memCity", value = "城市id" )
    private Integer memCity;

    @ApiModelProperty( name = "memArea", value = "区id " )
    private Integer memArea;

    @ApiModelProperty( name = "provincename", value = "省份名称 ", hidden = true )
    private String provincename;

    @ApiModelProperty( name = "cityname", value = "城市名称 ", hidden = true )
    private String cityname;

    @ApiModelProperty( name = "areaname", value = "区名称 ", hidden = true )
    private String areaname;

}