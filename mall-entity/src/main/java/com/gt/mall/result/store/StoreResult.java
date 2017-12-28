package com.gt.mall.result.store;

import com.baomidou.mybatisplus.annotations.TableField;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.result.order.OrderDetailResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 店铺列表返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "StoreResult", description = "店铺列表返回结果" )
@Getter
@Setter
public class StoreResult {

    @ApiModelProperty( name = "id", value = "店铺ID" )
    private Integer id;

    @ApiModelProperty( name = "stoName", value = "店铺名称" )
    private String stoName;

    @ApiModelProperty( name = "stoPhone", value = "联系人手机号码" )
    private String stoPhone;

    @ApiModelProperty( name = "stoLinkman", value = "联系人名称" )
    private String stoLinkman;

    @ApiModelProperty( name = "stoStatus", value = "店铺状态" )
    private Integer stoStatus;

    @ApiModelProperty( name = "stoPid", value = "父级ID" )
    private Integer stoPid;

    @ApiModelProperty( name = "stoUserId", value = "所属用户ID" )
    private Integer stoUserId;

    @ApiModelProperty( name = "stoIsMain", value = "是否主店(1-主店,2-非,-1 不分主店铺子店铺)" )
    private Integer stoIsMain;

    @ApiModelProperty( name = "stoAddress", value = "详细地址" )
    private String stoAddress;

    @ApiModelProperty( name = "stoQrCode", value = "店铺二维码" )
    private String stoQrCode;

    @ApiModelProperty( name = "stoHouseMember", value = "门牌号" )
    private String stoHouseMember;

    @ApiModelProperty( name = "stoCreateTime", value = "创建时间" )
    private Date stoCreateTime;

    @ApiModelProperty( name = "stoCreatePerson", value = "创建人" )
    private Integer stoCreatePerson;

    @ApiModelProperty( name = "stoProvince", value = "所属省份" )
    private Integer stoProvince;

    @ApiModelProperty( name = "stoCity", value = "所属城市" )
    private Integer stoCity;

    @ApiModelProperty( name = "stoLongitude", value = "经度" )
    private String stoLongitude;

    @ApiModelProperty( name = "stoLatitude", value = "纬度" )
    private String stoLatitude;

    @ApiModelProperty( name = "stoPicture", value = "图片" )
    private String stoPicture;

    @ApiModelProperty( name = "isDelete", value = "是否删除 0未删除  1已删除" )
    private Integer isDelete;

    @ApiModelProperty( name = "stoArea", value = "所属区域" )
    private Integer stoArea;

    @ApiModelProperty( name = "wxShopId", value = "门店id" )
    private Integer wxShopId;

    @ApiModelProperty( name = "stoIsSms", value = "是否开启短信推送  1推送 0 不推送" )
    private Integer stoIsSms;

    @ApiModelProperty( name = "stoSmsTelephone", value = "短信推送手机号码   保存多个手机号用分号;隔开" )
    private String stoSmsTelephone;

    @ApiModelProperty( name = "stoQqCustomer", value = "QQ客服" )
    private String stoQqCustomer;

    @ApiModelProperty( name = "stoHeadImg", value = "商城店铺头像" )
    private String stoHeadImg;

    @ApiModelProperty( name = "pageId", value = "店铺页面ID" )
    private Integer pageId;

    @ApiModelProperty( name = "certId", value = "店铺认证ID" )
    private Integer certId;
    @ApiModelProperty( name = "certStoType", value = "认证类型 0个人 1企业" )
    private Integer certStoType;
    @ApiModelProperty( name = "certStoCategory", value = "认证店铺  1 普通店铺  2旗舰店  3专卖店  4直营店" )
    private Integer certStoCategory;
    @ApiModelProperty( name = "certStoCategoryName", value = "认证店铺名称" )
    private String  certStoCategoryName;
}
