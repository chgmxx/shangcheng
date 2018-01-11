package com.gt.mall.param.phone.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单列表验证
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneOrderListDTO", description = "进入订单列表验证" )
@Data
public class PhoneOrderListDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;

    @ApiModelProperty( name = "orderId", value = "订单id" )
    private Integer orderId;

    @ApiModelProperty( name = "curPage", value = "当前页数" )
    private Integer curPage;

    @ApiModelProperty( name = "type", value = "查看订单类型  0查看全部订单 1待付款订单 2待发货订单 3已发货订单 4已完成订单 5 待评价 6 退款 7团购 8 秒杀" )
    private Integer type;

}