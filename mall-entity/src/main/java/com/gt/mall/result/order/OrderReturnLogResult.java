package com.gt.mall.result.order;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退款日志返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "OrderReturnLogResult", description = "订单退款日志返回结果" )
@Getter
@Setter
public class OrderReturnLogResult {

    @ApiModelProperty( name = "id", value = "退款日志ID" )
    private int id;

    @ApiModelProperty( name = "returnId", value = "维权订单id" )
    private Integer returnId;

    @ApiModelProperty( name = "createTime", value = "创建时间" )
    private Date createTime;

    @ApiModelProperty( name = "remark", value = "备注" )
    private String remark;

    @ApiModelProperty( name = "statusContent", value = "状态说明" )
    private String statusContent;

    @ApiModelProperty( name = "returnStatus", value = "退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款" )
    private Integer returnStatus;

    @ApiModelProperty( name = "deadlineTime", value = "截止时间(待卖家处理申请)" )
    private Date deadlineTime;

    @ApiModelProperty( name = "userId", value = "操作人id" )
    private Integer userId;

    @ApiModelProperty( name = "operator", value = "操作类型 0买家 1卖家 2系统信息" )
    private Integer operator;

    @ApiModelProperty( name = "getData", value = "是否有数据 0没有 1有" )
    private Integer getData;

}
