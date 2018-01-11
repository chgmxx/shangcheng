package com.gt.mall.result.phone.order.returns;

import com.gt.mall.bean.DictBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 退款商品返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneReturnProductResult", description = "退款商品返回结果" )
@Getter
@Setter
public class PhoneReturnProductResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productSpecifica", value = "商品规格" )
    private String productSpecifica;

    @ApiModelProperty( name = "productFreight", value = "商品运费" )
    private double productFreight = 0;

    @ApiModelProperty( name = "returnPrice", value = "退款金额" )
    private double returnPrice = 0;

    @ApiModelProperty( name = "returnWayList", value = "退款方式集合" )
    private List< PhoneReturnWayResult > returnWayList;

    @ApiModelProperty( name = "returnReasonList", value = "退款原因集合" )
    private List< DictBean > returnReasonList;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "orderType", value = "活动类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品" )
    private Integer orderType = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "returnId", value = "退款id" )
    private Integer returnId;

    @ApiModelProperty( name = "cargoStatus", value = "货物状态 0 未收到货 1已收到货  -1 未填写" )
    private Integer cargoStatus;

    @ApiModelProperty( name = "retReasonId", value = "退货原因id" )
    private Integer retReasonId;

    @ApiModelProperty( name = "retRemark", value = "退货说明" )
    private String retRemark;

    @ApiModelProperty( name = "returnImageUrls", value = "退货图片" )
    private String[] returnImageUrls;

    @ApiModelProperty( name = "isShowCargoStatus", value = "是否显示货物状态 1 显示" )
    private Integer isShowCargoStatus = 0;

    @ApiModelProperty( name = "returnPhone", value = "退款手机号码" )
    private String returnPhone;

    @ApiModelProperty( name = "returnWay", value = "处理方式  1,我要退款，但不退货  2,我要退款退货" )
    private Integer returnWay;

    @ApiModelProperty( name = "cargoStatusList", value = "货物状态集合" )
    private List< PhoneReturnWayResult > cargoStatusList;

}
