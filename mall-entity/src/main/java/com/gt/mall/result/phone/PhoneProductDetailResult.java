package com.gt.mall.result.phone;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.entity.basic.MallImageAssociative;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneGroupDTO", description = "分类验证" )
@Getter
@Setter
public class PhoneProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    @ApiModelProperty( name = "productName", value = "商品名称" )
    private String productName;

    @ApiModelProperty( name = "productLabel", value = "商品标签" )
    private String productLabel = "";

    @ApiModelProperty( name = "productPrice", value = "商品价格" )
    private double productPrice;

    @ApiModelProperty( name = "productCostPrice", value = "商品原价" )
    private double productCostPrice;

    @ApiModelProperty( name = "hyPrice", value = "会员价" )
    private double hyPrice;

    @ApiModelProperty( name = "freightMoney", value = "运费" )
    private double freightMoney;

    @ApiModelProperty( name = "unit", value = "单位" )
    private String unit;//单位

    @ApiModelProperty( name = "maxBuyNum", value = "限购，买家最多购买的数量" )
    private int maxBuyNum = 0;//限购，买家最多购买的数量

    @ApiModelProperty( name = "productStockTotal", value = "商品库存" )
    private int productStockTotal;

    @ApiModelProperty( name = "productSaleTotal", value = "商品销量" )
    private int productSaleTotal;

    @ApiModelProperty( name = "isSpecifica", value = "是否是规格  1有规格 0没有" )
    private int isSpecifica;

    @ApiModelProperty( name = "productDetail", value = "商品详情" )
    private String productDetail = "";

    @ApiModelProperty( name = "productMessage", value = "商品信息栏" )
    private Object productMessage = "";

    @ApiModelProperty( name = "shopName", value = "店铺名称" )
    private String shopName;

    @ApiModelProperty( name = "shopImageUrl", value = "店铺图片" )
    private String shopImageUrl;

    @ApiModelProperty( name = "shopAddress", value = "店铺地址" )
    private String shopAddress;

    @ApiModelProperty( name = "isCollect", value = "是否已收藏  1已收藏" )
    private int isCollect;

    @ApiModelProperty( name = "isSecuritytrade", value = "是否开通了担保交易 true开通了 false未开通" )
    private boolean isSecuritytrade;

    @ApiModelProperty( name = "stoType", value = "个人认证 或 企业认证" )
    private String stoType;

    @ApiModelProperty( name = "categoryName", value = "店铺类型" )
    private String categoryName;

    @ApiModelProperty( name = "isAdvert", value = "是否显示技术支持 1显示 0不显示" )
    private int isAdvert;

    @ApiModelProperty( name = "isShowAddShopButton", value = "是否显示加入购物车按钮 1显示" )
    private int isShowAddShopButton = 1;

    @ApiModelProperty( name = "isShowLiJiBuyButton", value = "是否显示立即购买的按钮 1显示" )
    private int isShowLiJiBuyButton = 1;

    @ApiModelProperty( name = "imageList", value = "图片集合" )
    private List< MallImageAssociative > imageList;

    @ApiModelProperty( name = "invId", value = "库存id" )
    private int invId;

    /*************************************************** 以下参数活动商品需要 ***************************************************/
    @ApiModelProperty( name = "type", value = "活动类型" )
    private int type;//活动类型

    @ApiModelProperty( name = "invList", value = "库存id集合" )
    private List< Integer > invIdList;

    @ApiModelProperty( name = "activityId", value = "活动id" )
    private int activityId;//活动id

    @ApiModelProperty( name = "activityStatus", value = "活动状态 0未开始 1进行中 -1已结束" )
    private Integer activityStatus;

    /*********************************** 以下参数团购商品需要 ***********************************/

    @ApiModelProperty( name = "joinList", value = "参团集合" )
    private List< Map< String,Object > > joinList;

    /*********************************** 以下参数预售商品需要 ***********************************/

    @ApiModelProperty( name = "buyCount", value = "订购量" )
    private int buyCount = 0;

    @ApiModelProperty( name = "isShowPresaleButton", value = "是否显示预定按钮 1显示" )
    private int isShowPresaleButton = 0;

    @ApiModelProperty( name = "isWeiMoneyButton", value = "是否显示支付尾款按钮 1显示" )
    private int isShowWeiMoneyButton = 0;

    @ApiModelProperty( name = "isShowStartButton", value = "是否显示即将开售按钮 1显示" )
    private int isShowStartButton = 0;

    @ApiModelProperty( name = "dingMoney", value = "定金" )
    private double dingMoney = 0;

    @ApiModelProperty( name = "weiMoney", value = "尾款" )
    private double weiMoney = 0;

    @ApiModelProperty( name = "payDespositStatus", value = "缴纳定金状态 1已缴纳 0未缴纳" )
    private int payDespositStatus = 0;

    /*********************************** 以下参数批发商品需要 ***********************************/

    @ApiModelProperty( name = "pfSetObj", value = "批发设置" )
    private JSONObject pfSetObj;

    @ApiModelProperty( name = "pfStatus", value = "批发状态" )
    private int pfStatus = -2;

    @ApiModelProperty( name = "pfType", value = "批发类型" )
    private int pfType;//批发类型

}
