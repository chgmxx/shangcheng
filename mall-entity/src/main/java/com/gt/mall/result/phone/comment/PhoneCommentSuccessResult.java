package com.gt.mall.result.phone.comment;

import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 评论成功页面返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneCommentSuccessResult", description = "评论成功页面返回结果" )
@Getter
@Setter
public class PhoneCommentSuccessResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "productResult", value = "商品返回" )
    private PhoneCommentProductResult productResult;

    @ApiModelProperty( name = "mallComment", value = "评论返回" )
    private MallComment mallComment;

    @ApiModelProperty( name = "imageList", value = "评论图片集合" )
    private List< MallImageAssociative > imageList;

}
