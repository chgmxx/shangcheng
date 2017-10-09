package com.gt.mall.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gt.mall.param.basic.ImageAssociativeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.awt.*;
import java.util.List;

/**
 * <p>
 * 商品分组
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
/*@JsonInclude( JsonInclude.Include.NON_NULL)*/
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Data
@ApiModel( value = "group" )
public class GroupDTO {

    /**
     * 产品分组标识
     */
    @ApiModelProperty( value = "ID" )
    private Integer id;
    /**
     * 分组名称
     */
    @ApiModelProperty( value = "分组名称", required = true )
    private String  groupName;
    /**
     * 父级分类
     */
    @ApiModelProperty( value = "父级分类ID" )
    private Integer groupPId;
    /**
     * 第一优先级
     */
    @ApiModelProperty( value = "第一优先级", required = true )
    private Integer firstPriority;
    /**
     * 第二优先级
     */
    @ApiModelProperty( value = "第二优先级", required = true )
    private Integer secondPriority;
    /**
     * 是否在页面显示分组名称 0 不显示  1 显示
     */
    @ApiModelProperty( value = "是否在页面显示分组名称  0不显示/1显示", required = true )
    private Integer isShowPage;
    /**
     * 店铺id
     */
    @ApiModelProperty( value = "店铺id", required = true )
    private Integer shopId;
    /**
     * 是否是一级父类
     * 0不是一级父类  1 是一级父类
     */
    @ApiModelProperty( value = "是否是一级父类 0不是/1是", required = true )
    private Integer isFirstParents;
    /**
     * 是否有子类 0没有子类  1有子类
     */
    @ApiModelProperty( value = "是否有子类 0没有子类  1有子类", required = true )
    private Integer isChild;
    /**
     * 分组排序
     */
    @ApiModelProperty( value = "分组排序" )
    private Integer sort;

    /**
     * 排序方式 1按热度 2按序号大小
     */
    @ApiModelProperty( value = " 排序方式 1按热度 2按序号大小", required = true )
    private Integer sortOrder;

    @ApiModelProperty( value = " 图片列表" )
    private List< ImageAssociativeDTO > imageList;

   /* @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(getClass().getSimpleName());
	sb.append(" [");
	sb.append("Hash = ").append(hashCode());
	sb.append(", id=").append(id);
	sb.append(", groupName=").append(groupName);
	sb.append(", firstPriority=").append(firstPriority);
	sb.append(", secondPriority=").append(secondPriority);
	sb.append(", isShowPage=").append(isShowPage);
	sb.append(", shopId=").append(shopId);
	sb.append(", isFirstParents=").append(isFirstParents);
	sb.append(", isChild=").append(isChild);
	sb.append(", sort=").append(sort);
	sb.append(", sortOrder=").append(sortOrder);
	sb.append("]");
	return sb.toString();
    }

    public Integer getId() {
	return id;
    }

    public void setId( Integer id ) {
	this.id = id;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName( String groupName ) {
	this.groupName = groupName;
    }

    public Integer getGroupPId() {
	return groupPId;
    }

    public void setGroupPId( Integer groupPId ) {
	this.groupPId = groupPId;
    }

    public Integer getFirstPriority() {
	return firstPriority;
    }

    public void setFirstPriority( Integer firstPriority ) {
	this.firstPriority = firstPriority;
    }

    public Integer getSecondPriority() {
	return secondPriority;
    }

    public void setSecondPriority( Integer secondPriority ) {
	this.secondPriority = secondPriority;

    }

    public Integer getIsShowPage() {
	return isShowPage;
    }

    public void setIsShowPage( Integer isShowPage ) {
	this.isShowPage = isShowPage;
    }

    public Integer getShopId() {
	return shopId;
    }

    public void setShopId( Integer shopId ) {
	this.shopId = shopId;

    }

    public Integer getIsFirstParents() {
	return isFirstParents;
    }

    public void setIsFirstParents( Integer isFirstParents ) {
	this.isFirstParents = isFirstParents;
    }

    public Integer getIsChild() {
	return isChild;
    }

    public void setIsChild( Integer isChild ) {
	this.isChild = isChild;
    }

    public Integer getSort() {
	return sort;
    }

    public void setSort( Integer sort ) {
	this.sort = sort;
    }

    public Integer getSortOrder() {
	return sortOrder;
    }

    public void setSortOrder( Integer sortOrder ) {
	this.sortOrder = sortOrder;
    }*/
}
