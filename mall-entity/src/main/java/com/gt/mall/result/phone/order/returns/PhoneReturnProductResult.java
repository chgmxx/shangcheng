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

    @ApiModelProperty( name = "returnReasonList", value = "退款原因集合" )
    private List< DictBean > returnReasonList;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "returnId", value = "退款id" )
    private int returnId = 0;

    @ApiModelProperty( name = "cargoStatus", value = "货物状态 0 未收到货 1已收到货  -1 未填写" )
    private int cargoStatus;

    @ApiModelProperty( name = "retReasonId", value = "退货原因id" )
    private int retReasonId;

    @ApiModelProperty( name = "retRemark", value = "退货说明" )
    private String retRemark;

    @ApiModelProperty( name = "returnImageUrls", value = "退货图片" )
    private String[] returnImageUrls;

    @ApiModelProperty( name = "isShowCargoStatus", value = "是否显示货物状态 1 显示" )
    private Integer isShowCargoStatus = 0;

    @ApiModelProperty( name = "cargoStatusList", value = "货物状态集合" )
    private List< PhoneReturnWayResult > cargoStatusList;

}
