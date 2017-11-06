package com.gt.mall.param.phone.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商城评论
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@ApiModel( value = "PhoneMallCommentDTO", description = "评论参数验证" )
@Data
public class PhoneMallCommentDTO implements Serializable {

    /**
     * 评论标识
     */
    private Integer id;
    /**
     * 订单id(关联t_mall_order表的id)
     */
    @ApiModelProperty( name = "orderId", value = "订单id", required = true )
    private Integer orderId;
    /**
     * 订单详情表 关联t_mall_order_detail表的id
     */
    @ApiModelProperty( name = "orderDetailId", value = "订单详情id", required = true )
    private Integer orderDetailId;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @ApiModelProperty( name = "productId", value = "商品id", required = true )
    private Integer productId;
    /**
     * 评论内容
     */
    @ApiModelProperty( name = "content", value = "评论内容", required = true )
    private String  content;
    /**
     * 评论人是否上传图片(0 未上传  1已上传)
     */
    @ApiModelProperty( name = "isUploadImage", value = "评论人是否上传图片 1已上传", hidden = true )
    private Integer isUploadImage;
    /**
     * 总体评价 -1 差评 0 中评 1好评
     */
    @ApiModelProperty( name = "feel", value = "总体评价 -1 差评 0 中评 1好评", required = true )
    private Integer feel;
    /**
     * 商品描述星评
     */
    @ApiModelProperty( name = "descriptStart", value = "商品描述星评", hidden = true )
    private Integer descriptStart;
    /**
     * 卖家服务星评
     */
    @ApiModelProperty( hidden = true )
    private Integer serviceStart;
    /**
     * 发货速度星评
     */
    @ApiModelProperty( hidden = true )
    private Integer speeedStart;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @ApiModelProperty( name = "shopId", value = "店铺id", hidden = true )
    private Integer shopId;

}