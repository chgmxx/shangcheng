package com.gt.mall.param.applet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品查询参数
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Getter
@Setter
public class ProductSearchDTO {
    /**
     * 会员ID
     */
    private Integer memberId;
    /**
     * 关键字
     */
    private String  searchName;
    /**
     * 分组ID
     */
    private Integer groupId;
    /**
     * 店铺id
     */
    private Integer shopId;
    /**
     * 查询类型 1最新 2销量  3价格
     */
    private Integer type;
    /**
     * 排序方式  0升序  1降序
     */
    private Integer desc;
    /**
     * 页数
     */
    private Integer curPage;

    /**
     * 商品类型类型，0.普通商品   1.团购 3.秒杀 4.拍卖 5 粉币 6预售 7批发
     */
    @ApiModelProperty( name = "productType", value = "商品类型类型，0.普通商品   1.团购 3.秒杀 4.拍卖 5 粉币 6预售 7批发" )
    private Integer productType = 0;

}
