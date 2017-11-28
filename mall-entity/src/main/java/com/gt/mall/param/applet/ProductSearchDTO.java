package com.gt.mall.param.applet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gt.mall.param.basic.ImageAssociativeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

}
