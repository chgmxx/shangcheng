package com.gt.mall.param.phone.sellers;

import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.param.phone.order.PhoneOrderWayDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 保存商城设置参数
 * User : yangqian
 * Date : 2017/10/31 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddSellersResult", description = "保存商城设置参数" )
@Getter
@Setter
public class PhoneAddMallSetDTO implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "mallSet", value = "商城设置" )
    private PhoneSellerMallSetDTO mallSet;

    @ApiModelProperty( name = "sellerProList", value = "自选商品列表" )
    private List< PhoneSellerProductDTO > sellerProList;

}
