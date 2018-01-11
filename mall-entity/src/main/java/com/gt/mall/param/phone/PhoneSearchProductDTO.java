package com.gt.mall.param.phone;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 搜索商品所需参数
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:49
 */
@ApiModel( value = "PhoneSearchProductDTO", description = "搜索商品验证" )
@Data
public class PhoneSearchProductDTO implements Serializable {

    private static final long serialVersionUID = 4687203902966052326L;

    @ApiModelProperty( name = "shopId", value = "店铺ID，必传", required = true )
    @NotNull( message = "店铺ID不能为空" )
    @Min( value = -1, message = "店铺ID不能小于1" )
    private Integer shopId;

    @ApiModelProperty( name = "busId", value = "商家id，必传", required = true )
    @NotNull( message = "商家id不能为空" )
    @Min( value = 1, message = "商家id不能小于1" )
    private Integer busId;

    @ApiModelProperty( name = "groupId", value = "分类id" )
    private Integer groupId = 0;

    @ApiModelProperty( name = "sort", value = "排序 new：按最新 sale：按销量排序 price 按价格排序" )
    private String sort = "new";

    @ApiModelProperty( name = "isDesc", value = "排序  1、升序 -1 降序 " )
    private Integer isDesc = 0;

    @ApiModelProperty( name = "searchContent", value = "搜索内容" )
    private String searchContent;

    @ApiModelProperty( name = "type", value = "类型，1.团购 3.秒杀 4.拍卖 5 粉币 6预售 7批发" )
    private Integer type = 0;

    @ApiModelProperty( name = "saleMemberId", value = "销售员id " )
    private Integer saleMemberId = 0;

    @ApiModelProperty( name = "curPage", value = "当前页" )
    private Integer curPage = 1;

    @ApiModelProperty( name = "firstNum", value = "起始页", hidden = true )
    private int firstNum;

    @ApiModelProperty( name = "maxNum", value = "显示数量 ", hidden = true )
    private int maxNum;

    @ApiModelProperty( name = "shopList", value = "店铺列表，查询用的，可不传值", hidden = true )
    private List< Map< String,Object > > shopList;

}
