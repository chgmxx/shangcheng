package com.gt.mall.result.phone.order.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 订单列表（进入提交列表返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneOrderListResult", description = "订单列表（进入提交列表返回结果）" )
@Getter
@Setter
public class PhoneOrderListResult implements Serializable {

    private static final long serialVersionUID = 6685362436611404285L;

    @ApiModelProperty( name = "isShowCardReceive", value = "是否显示卡券包购买成功提醒  1显示" )
    private Integer isShowCardReceive;

    @ApiModelProperty( name = "cardReceiveId", value = "卡券包id" )
    private Integer cardReceiveId;

    @ApiModelProperty( name = "orderResultList", value = "订单集合" )
    private List< PhoneOrderListOrderResult > orderResultList;

    @ApiModelProperty( name = "curPage", value = "当前页" )
    private int curPage = 0;

    @ApiModelProperty( name = "pageCount", value = "总页数" )
    private int pageCount = 0;

}
