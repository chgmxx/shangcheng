package com.gt.mall.result.phone.comment;

import com.gt.mall.entity.basic.MallImageAssociative;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 评论列表返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneCommentListResult", description = "评论列表返回结果" )
@Getter
@Setter
public class PhoneCommentListCommentResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "feel", value = "总体评价 -1 差评 0 中评 1好评" )
    private int feel;

    @ApiModelProperty( name = "content", value = "评论内容" )
    private String content;

    @ApiModelProperty( name = "commentTime", value = "评论时间" )
    private String commentTime;

    @ApiModelProperty( name = "productSpecifica", value = "商品规格" )
    private String productSpecifica;

    @ApiModelProperty( name = "productId", value = "商品id" )
    private Integer productId;

    @ApiModelProperty( name = "shopId", value = "店铺id" )
    private Integer shopId;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productImageUrl", value = "商品图片" )
    private String productImageUrl;

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private Double productPrice;

    @ApiModelProperty( name = "replyContent", value = "回复内容" )
    private String replyContent;

    @ApiModelProperty( name = "commentImageList", value = "评论集合" )
    private List< MallImageAssociative > commentImageList;

    @ApiModelProperty( name = "unit", value = "单位" )
    private String unit;

    @ApiModelProperty( name = "orderType", value = "订单类型" )
    private Integer orderType = 0;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private Integer activityId = 0;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId = 0;
}
