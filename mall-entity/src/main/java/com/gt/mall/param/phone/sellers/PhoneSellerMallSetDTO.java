package com.gt.mall.param.phone.sellers;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商城设置参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneSellerMallSet", description = "商城设置参数" )
@Getter
@Setter
public class PhoneSellerMallSetDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "id", value = "ID")
    private Integer id;

    @ApiModelProperty( name = "mallName", value = "商城名称" )
    private String mallName;

    @ApiModelProperty( name = "contactNumber", value = "联系电话" )
    private String contactNumber;

    @ApiModelProperty( name = "qq", value = "QQ" )
    private String qq;

    @ApiModelProperty( name = "mallHeadPath", value = "商城头像地址" )
    private String mallHeadPath;

    @ApiModelProperty( name = "bannerPath", value = "banner地址")
    private String bannerPath;

    @ApiModelProperty( name = "mallIntroducation", value = "商城简介" )
    private String mallIntroducation;

    @ApiModelProperty( name = "isOpenOptional", value = " 是否开启自选  0没有开启自选   1开启自选" )
    private Integer isOpenOptional;

/*    @ApiModelProperty( name = "saleMemberId", value = "销售员id" )
    private Integer saleMemberId;

    @ApiModelProperty( name = "busUserId", value = "商家id" )
    private Integer busUserId;*/

}
