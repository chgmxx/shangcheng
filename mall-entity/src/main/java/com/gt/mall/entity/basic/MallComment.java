package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商城评论
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_comment" )
public class MallComment extends Model< MallComment > {

    private static final long serialVersionUID = 1L;

    /**
     * 评论标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单id(关联t_mall_order表的id)
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 订单详情表 关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer orderDetailId;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 评论内容
     */
    private String  content;
    /**
     * 评论人是否上传图片(0 未上传  1已上传)
     */
    @TableField( "is_upload_image" )
    private Integer isUploadImage;
    /**
     * 总体评价 -1 差评 0 中评 1好评
     */
    private Integer feel;
    /**
     * 商品描述星评
     */
    @TableField( "descript_start" )
    private Integer descriptStart;
    /**
     * 卖家服务星评
     */
    @TableField( "service_start" )
    private Integer serviceStart;
    /**
     * 发货速度星评
     */
    @TableField( "speeed_start" )
    private Integer speeedStart;
    /**
     * 商家回复的评论id  关联t_mall_comment表的id
     */
    @TableField( "rep_p_id" )
    private Integer repPId;
    /**
     * 评价人id  根据评价人类型来判断 。如果是粉丝， 则关联t_wx_bus_member表的id ； 如果是商家则关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 评价人类型  1 粉丝回复    2 商家回复
     */
    @TableField( "user_type" )
    private Integer userType;
    /**
     * 是否已经回复 （0 未回复    1已回复）
     */
    @TableField( "is_rep" )
    private String  isRep;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否已经被删除（0未删除   1已删除）
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 审核状态  （0未审核  1审核同意   -1 审核不同意）
     */
    @TableField( "check_status" )
    private Integer checkStatus;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallComment{" +
			"id=" + id +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", productId=" + productId +
			", content=" + content +
			", isUploadImage=" + isUploadImage +
			", feel=" + feel +
			", descriptStart=" + descriptStart +
			", serviceStart=" + serviceStart +
			", speeedStart=" + speeedStart +
			", repPId=" + repPId +
			", userId=" + userId +
			", userType=" + userType +
			", isRep=" + isRep +
			", shopId=" + shopId +
			", createTime=" + createTime +
			", isDelete=" + isDelete +
			", checkStatus=" + checkStatus +
			"}";
    }
}
