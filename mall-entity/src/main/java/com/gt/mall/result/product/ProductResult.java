package com.gt.mall.result.product;

import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.result.order.OrderDetailResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品列表返回结果
 * User : yangqian
 * Date : 2017/10/21 0021
 * Time : 16:36
 */
@ApiModel( value = "ProductResult", description = "商品列表返回结果" )
@Getter
@Setter
public class ProductResult {

    @ApiModelProperty( name = "id", value = "商品ID" )
    private Integer id;
    @ApiModelProperty( name = "shopId", value = "店铺ID" )
    private Integer shopId;
    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String  shopName;
    @ApiModelProperty( name = "wxShopId", value = "门店ID" )
    private Integer wxShopId;
    @ApiModelProperty( name = "userId", value = "商家ID" )
    private Integer userId;

    @ApiModelProperty( name = "imageUrl", value = "商品图片" )
    private String     imageUrl;
    @ApiModelProperty( name = "proName", value = "商品名称" )
    private String     proName;
    @ApiModelProperty( name = "proPrice", value = "商品价格" )
    private BigDecimal proPrice;
    @ApiModelProperty( name = "stockTotal", value = "库存总数" )
    private Integer    stockTotal;
    @ApiModelProperty( name = "saleTotal", value = "总销量" )
    private Integer    saleTotal;
    @ApiModelProperty( name = "createTime", value = "创建时间" )
    private Date       createTime;
    @ApiModelProperty( name = "isPublish", value = "是否已经发布  0 还未发布 1 已经发布（上架） -1下架" )
    private Integer    isPublish;
    @ApiModelProperty( name = "checkStatus", value = "审核状态 0:审核中 1:审核成功 -1:审核失败 -2：还未审核" )
    private Integer    checkStatus;
    @ApiModelProperty( name = "checkReason", value = "审核不通过的原因" )
    private String     checkReason;

    /*   @ApiModelProperty( name = "twoCodePath", value = "二维码路径" )
       private String     twoCodePath;*/
    @ApiModelProperty( name = "returnDay", value = "完成订单后在有效天数内退款" )
    private Integer returnDay;
    @ApiModelProperty( name = "viewsNum", value = "浏览量" )
    private Integer viewsNum;
    @ApiModelProperty( name = "isShowView", value = "是否显示浏览量 0 不显示 1显示" )
    private Integer isShowView;
    @ApiModelProperty( name = "isIntegralDeduction", value = "是否开启积分抵扣 0 不开启 1开启" )
    private Integer isIntegralDeduction;
    @ApiModelProperty( name = "isFenbiDeduction", value = "是否开启粉币抵扣 0 不开启 1开启" )
    private Integer isFenbiDeduction;
    @ApiModelProperty( name = "isSpecifica", value = "是否有规格 0没有规格 1有规格" )
    private Integer isSpecifica;
    @ApiModelProperty( name = "salesBase", value = "销量基数" )
    private Integer salesBase;
    @ApiModelProperty( name = "erpProId", value = "关联erp商品id" )
    private Integer erpProId;
    @ApiModelProperty( name = "erpInvId", value = "关联erp商品库存id" )
    private Integer erpInvId;

    @ApiModelProperty( name = "isGroup", value = "商品是否加入到团购 1有 0没有" )
    private Integer isGroup;
    @ApiModelProperty( name = "isSeckill", value = "商品是否加入到秒杀 1有 0没有" )
    private Integer isSeckill;
}
