package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ApiModel( value = "PhoneMemberAddressDTO", description = "收货地址验证" )
@Getter
@Setter
public class PhoneMemberAddressDTO implements Serializable {

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

    @ApiModelProperty( name = "memProvince", value = "省份id", required = true )
    private Integer memProvince;

    @ApiModelProperty( name = "memCity", value = "城市id", required = true )
    private Integer memCity;

    @ApiModelProperty( name = "memArea", value = "区id ", required = true )
    private Integer memArea;

    @ApiModelProperty( name = "memDefault", value = "是否是默认地址", required = true )
    private Integer memDefault;

    @ApiModelProperty( name = "dfMemberId", value = "粉丝id", hidden = true )
    private Integer dfMemberId;

}