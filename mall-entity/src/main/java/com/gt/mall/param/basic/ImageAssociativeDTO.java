package com.gt.mall.param.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 图片中间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@JsonIgnoreProperties( { "handler", "hibernateLazyInitializer" } )
@ApiModel( value = "imageAssociative" )
public class ImageAssociativeDTO {

    /**
     * 产品分组标识
     */
    @ApiModelProperty( value = "ID" )
    private Integer id;
    /**
     * 图片地址
     */
    @ApiModelProperty( value = "图片地址", required = true )
    private String  imageUrl;
    /**
     * 关联id
     */
    @JsonInclude( JsonInclude.Include.NON_NULL )
    @ApiModelProperty( value = "关联id", required = true )
    private Integer assId;
    /**
     * 是否是主图 0不是主图  1是主图
     */
    @ApiModelProperty( value = "是否是主图 0不是主图  1是主图", required = true )
    private Integer isMainImages;
    /**
     * 关联表类型 1关联商品表 t_mall_product表的id  2关联商品分组表t_mall_group的id  3关联上门自提表 t_mall_take_their表的id   4关联评价表t_mall_comment表的id  5关联店铺表t_mall_store的id
     */
    @ApiModelProperty( value = "关联表类型 1关联商品表id  2关联商品分组表id  3关联上门自提表id   4关联评价表id  5关联店铺表id 6关联店铺认证表", required = true )
    private Integer assType;
    /**
     * 排序
     */
    @ApiModelProperty( value = "排序", required = true )
    private Integer assSort;
    /**
     * 是否已经删除 0未删除 1已删除
     */
   /* @ApiModelProperty( value = "是否已经删除 0未删除 1已删除" )
    private Integer isDelete;*/

}
