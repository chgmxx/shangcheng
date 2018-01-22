package com.gt.mall.param.phone.freight;

import com.gt.mall.entity.freight.MallFreight;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 每个商品计算运费时传参
 * User : yangqian
 * Date : 2018/1/20 0020
 * Time : 18:27
 */
@Getter
@Setter
public class PhoneFreightProductDTO implements Serializable {

    private static final long serialVersionUID = -1424109806117433065L;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 运费id
     */
    private int freightId;

    /**
     * 购买数量
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

    /**
     * 运费   获取商家运费信息
     */
    private MallFreight mallFreight;
}
