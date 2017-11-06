package com.gt.mall.result.phone.comment;

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
public class PhoneCommentListResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "commentResultList", value = "评论集合返回结果" )
    private List< PhoneCommentListCommentResult > commentResultList;

    @ApiModelProperty( name = "curPage", value = "当前页面" )
    private Integer curPage;

    @ApiModelProperty( name = "pageCount", value = "总页数" )
    private Integer pageCount;

}
