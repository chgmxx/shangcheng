package com.gt.mall.param.phone.freight;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 算运费要传的商品信息
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneFreightShopDTO", description = "算运费要传的商品信息" )
@Data
public class PhoneFreightShopDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    /**
     * 商品类型
     */
    private Integer proTypeId;

    /**
     * 店铺id
     */
    private int shopId;

    /**
     * 门店下的商品总数
     */
    private int totalProductNum;

    /**
     * 门店下商品总价
     */
    private double totalProductPrice;

    /**
     * 门店下商品总重量
     */
    private double totalProductWeight;



}
