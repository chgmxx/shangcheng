package com.gt.mall.param.store;

import com.gt.mall.param.basic.ImageAssociativeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 店铺认证参数
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "StoreCertDTO", description = "店铺认证参数" )
@Getter
@Setter
public class StoreCertDTO {

    @ApiModelProperty( name = "id", value = "店铺认证ID" )
    private Integer id;

    @ApiModelProperty( name = "stoId", value = "店铺id", required = true )
    @NotNull( message = "店铺id不能为空" )
    @Min( value = 1, message = "商品id不能小于1" )
    private Integer stoId;

    @ApiModelProperty( name = "stoType", value = "主体类型 0个人 1企业", required = true )
    private Integer stoType;

    @ApiModelProperty( name = "name", value = "姓名或法人", required = true )
    private String name;

    @ApiModelProperty( name = "companyName", value = "企业名称 主体为企业必传" )
    private String companyName;

    @ApiModelProperty( name = "idNumber", value = "身份证号码", required = true )
    private String idNumber;

    @ApiModelProperty( name = "idCardFront", value = "身份证正面", required = true )
    private String idCardFront;

    @ApiModelProperty( name = "idCardBack", value = "身份证反面", required = true )
    private String idCardBack;

    @ApiModelProperty( name = "busLicenseImg", value = "营业执照 主体为企业必传" )
    private String busLicenseImg;

    @ApiModelProperty( name = "busLicenseNo", value = "营业执照号 主体为企业必传" )
    private String busLicenseNo;

    @ApiModelProperty( name = "stoCategory", value = "店铺类型  1 普通店铺  2旗舰店  3专卖店  4直营店 主体为企业必传" )
    private Integer stoCategory;

    @ApiModelProperty( name = "certType", value = "主体为企业必传 认证类型 6 商标注册通知书 7商标注册证  8商标使用许可证明  9微信渠道授权书 10多粉渠道授权书  11公司总部证明函 12关系证明函" )
    private Integer certType;

    @ApiModelProperty( name = "certImgUrl", value = "主体为企业必传 商标注册通知书,商标注册证，商标使用许可合同，渠道授权书，证明函文件" )
    private String certImgUrl;

    @ApiModelProperty( name = "tradeMarkImg", value = "商标注册证 旗舰店使用" )
    private String tradeMarkImg;

    @ApiModelProperty( name = "isCertDoc", value = "是否有补充资料  0无 1有" )
    private Integer isCertDoc;

    @ApiModelProperty( name = "imageList", value = " 补充资料列表" )
    private List< ImageAssociativeDTO > imageList;
}
